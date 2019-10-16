package CuebiqCompetition.parallelCueBiq;

import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class ParallelApp 
{
    public static void main( String[] args )
    {
        System.out.println( "Begin!" );
        
		ParallelRowContainer f = new ParallelRowContainer();
		defDataset(f);
		try
		{
			FutNode n = f.aggregate("SUM", "Nation", "Eyes", "Hair");
		
			
			n.print();
			
			String[] ss = {"Germany", "Dark"};
			
			FutNode q = n.getNode(Arrays.asList(ss));
			q.print();
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public static void defDataset(ParallelRowContainer f)
	{
		f.setTemplate(Arrays.asList("Nation","Eyes", "Hair"));
		
		try {
		
		f.add(new String[] { "Germany", "Green", "Brown" }, 168);
		f.add(new String[] { "Spain", "Green", "Brown" }, 359);
		f.add(new String[] { "Germany", "Blue", "Brown" }, 389);
		f.add(new String[] { "Germany", "Dark", "Black" }, 468);
		f.add(new String[] { "Germany", "Dark", "Brown" }, 103);
		f.add(new String[] { "France", "Blue", "Black" }, 506);
		f.add(new String[] { "Italy", "Dark", "Black" }, 148);
		f.add(new String[] { "Spain", "Brown", "Red" }, 778);
		f.add(new String[] { "Germany", "Green", "Red" }, 536);
		f.add(new String[] { "France", "Green", "Blonde" }, 288);
		f.add(new String[] { "France", "Green", "Black" }, 857);
		f.add(new String[] { "Spain", "Dark", "Black" }, 907);
		f.add(new String[] { "Germany", "Green", "Red" }, 906);
		f.add(new String[] { "Germany", "Brown", "Red" }, 753);
		f.add(new String[] { "Spain", "Blue", "Black" }, 852);
		f.add(new String[] { "France", "Blue", "Black" }, 498);
		
		}catch(Exception e) {
			
			e.printStackTrace();
		}
		
		
		
	}
}
