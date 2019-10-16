package CuebiqCompetition.normalCueBiq;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class MyNode
{
	public String name;
	public double value;
	public TreeMap<String, MyNode> children = new TreeMap<>();

	public MyNode(String name, Double res)
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
		System.out.println(sb.toString() + name + " -> " + value);

		for (MyNode n : children.values())
		{
			n._print(lvl + 1);
		}

	}
	public MyNode getNode(String... ss)
	{
		return getNode(Arrays.asList(ss));
	}

	
	public MyNode getNode(List<String> ss)
	{
		if (ss.size() == 0)
			return this;

		MyNode n = children.get(ss.get(0));
		return ss.size() > 1 && n != null ? n.getNode(ss.subList(1, ss.size())) : n;

	}

}
 