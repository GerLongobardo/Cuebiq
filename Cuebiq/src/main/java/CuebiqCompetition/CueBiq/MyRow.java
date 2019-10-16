package CuebiqCompetition.CueBiq;

import java.util.ArrayList;
import java.util.Collection;

public class MyRow 
{
	public ArrayList<String> fields;
	public int value;
	
	public MyRow(Collection<String> ss, int value)
	{
		fields = new ArrayList<>(ss);
		this.value = value;

	}
	
	public int getValue()
	{
		return value;
	}

}
