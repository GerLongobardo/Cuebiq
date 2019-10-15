package CuebiqCompetition.CueBiq;

import java.util.ArrayList;
import java.util.Collection;

public class MyRow implements Comparable
{
	public ArrayList<String> fields;
	public int value;
	
	public MyRow(Collection<String> ss, int value)
	{
		fields = new ArrayList<>(ss);
		this.value = value;

	}

	@Override
	public int compareTo(Object o)
	{
		MyRow r = (MyRow) o;
		return value - r.value;
	}
}
