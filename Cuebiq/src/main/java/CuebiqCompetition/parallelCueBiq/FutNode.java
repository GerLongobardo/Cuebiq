package CuebiqCompetition.parallelCueBiq;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FutNode
{
	public String name;
	private Future<Double> value;
	public TreeMap<String, FutNode> children = new TreeMap<>();

	public FutNode(String name, Future<Double> res)
	{
		this.name = name;
		value = res;
	}
	
	public void print() {
		
		_print(0);
	}
	
	private void _print(int lvl)
	{

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lvl; i++)
			sb.append("\t");
		try
		{
			System.out.println(sb.toString() + name + " -> " + value.get());
		} catch (Exception e)
		{			// TODO Auto-generated catch block
			e.printStackTrace();
		};

		for (FutNode n : children.values())
		{
			n._print(lvl + 1);
		}

	}
	public FutNode getNode(String... ss)
	{
		return getNode(Arrays.asList(ss));
	}

	
	public FutNode getNode(List<String> ss)
	{
		if (ss.size() == 0)
			return this;

		FutNode n = children.get(ss.get(0));
		return ss.size() > 1 && n != null ? n.getNode(ss.subList(1, ss.size())) : n;

	}
	
	public Double getValue() {
		try
		{
			return value.get();
		} catch (InterruptedException | ExecutionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0.0;

	}

}
 