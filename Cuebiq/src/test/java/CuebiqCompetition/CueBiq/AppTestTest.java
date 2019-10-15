package CuebiqCompetition.CueBiq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AppTestTest
{
	static RowContainer f;
	static MyNode goodNode;

	@BeforeAll
	static void setUpBeforeClass() throws Exception
	{
		f = new RowContainer();
		App.defDataset(f);
		goodNode = f.aggregate("SUM", "Nation", "Eyes", "Hair");
	}

	@Test
	void testTotalCategory()
	{
		assertEquals(2896.0 , goodNode.getNode("Spain").value);
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
		RowContainer r = new RowContainer();
		r.setTemplate(Arrays.asList(new String[] { "COL1", "COL2" }));
		r.add(new String[] { "AAAA","BBBB" }, 1);
		Function<Collection<MyRow>, Double> F = null;
		assertThrows(MyException.class, () -> r.aggregate(F, "COL1"));
	}
	
	@Test
	void testUnknownF() throws Exception
	{
		RowContainer r = new RowContainer();
		r.setTemplate(Arrays.asList(new String[] { "COL1", "COL2" }));
		r.add(new String[] { "AAAA","BBBB" }, 1);
		assertThrows(MyException.class, () -> r.aggregate("DIFF", "COL1"));
	}
	
	@Test
	void testUnknownField() throws Exception
	{
		assertThrows(MyException.class, () -> f.aggregate("SUM", "Nation", "XXXX", "Hair"));
	}

}
