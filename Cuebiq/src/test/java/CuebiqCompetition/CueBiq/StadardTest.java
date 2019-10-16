package CuebiqCompetition.CueBiq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import CuebiqCompetition.normalCueBiq.App;
import CuebiqCompetition.normalCueBiq.MyNode;
import CuebiqCompetition.normalCueBiq.RowContainer;

class StadardTest
{
	static RowContainer f;
	static MyNode goodNode;
	static RowContainer r = new RowContainer();
	

	@BeforeAll
	static void setUpBeforeClass() throws Exception
	{
		f = new RowContainer();
		r = new RowContainer();
		App.defDataset(f);
		goodNode = f.aggregate("SUM", "Nation", "Eyes", "Hair");
		
		r = new RowContainer();
		r.setTemplate(Arrays.asList(new String[] { "COL1", "COL2" }));
	}

	@Test
	void testTotalCategory()
	{
		assertEquals(2896.0 , goodNode.getNode("Spain").value);
		
		//359 + 778 + 907 + 852
	}
	
	@Test
	void testValidFTotalCategory() throws MyException
	{
		Function<Collection<Integer>, Double> avg_f = x -> 
		{
			return (1.0 * x.stream().mapToInt(a -> a).sum())/ x.size();
		};
		f.addFunction("AVG", avg_f);
		MyNode avgNode = f.aggregate("AVG", "Nation", "Hair", "Eyes");
		assertEquals(220.0 , avgNode.getNode("Germany", "Brown").value);
		//168 +389 + 103 = 660 -> /3=220
	
	}
	
	@Test
	void testInexistentCategory()
	{
		assertNull  ( goodNode.getNode("AAAA","BBBB"));
	}
	
	@Test
	void testWrongColsN()
	{
		RowContainer r = new RowContainer();
		r.setTemplate(Arrays.asList(new String[] { "COL1", "COL2" }));
		assertThrows(MyException.class, () -> r.add(new String[] { "AAAA" }, 1));
	}
	
	@Test
	void testEmptyF() throws Exception
	{

		r.add(new String[] { "AAAA","BBBB" }, 1);
		Function<Collection<Integer>, Double> F = null;
		assertThrows(MyException.class, () -> r.aggregate(F, "COL1"));
	}
	
	@Test
	void testUnknownF() throws Exception
	{
		r.add(new String[] { "AAAA","BBBB" }, 1);
		assertThrows(MyException.class, () -> r.aggregate("DIFF", "COL1"));
	}
	
	@Test
	void testUnknownField() throws Exception
	{
		assertThrows(MyException.class, () -> f.aggregate("SUM", "Nation", "XXXX", "Hair"));
	}

}
