package CuebiqCompetition.CueBiq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RowContainer
{

	private HashMap<String, Integer>                             template  = new HashMap<>();
	private ArrayList<MyRow>                                     dataset   = new ArrayList<>();
	private HashMap<String, Function<Collection<MyRow>, Double>> functions = new HashMap<>();

	public RowContainer() 
	{
		Function<Collection<MyRow>, Double> sum_f = x -> 
		{
			return 1.0 * x.stream().mapToInt(a -> a.value).sum();
		};
		
		functions.put("SUM", sum_f);
		
		Function<Collection<MyRow>, Double> med_f = x -> 
		{
			MyRow[] l = x.toArray(new MyRow[0]);
			Arrays.parallelSort(l);
			return new Double(l[l.length / 2].value);
		};
		
		functions.put("MEDIAN", med_f);

	}
	
	public void addFunction(String key, Function<Collection<MyRow>, Double> F)
	{	
		functions.put(key, F);
	}
	
	public void setTemplate(Collection<String> ss)
	{
		dataset.clear();
		template.clear();
		int i = 0;
		for (String s : ss)
			template.put(s, i++);
	}

	public void add(Collection<String> ss, int value) throws MyException 
	{
		if (ss.size() != template.size())
			throw new MyException("WRONG NUMBER OF FIELDS: EXPECTED -> " + template.size() + " GOT ->" + ss.size());

		dataset.add(new MyRow(ss, value));
	}
	
	public void add(String[] ss, int value) throws MyException 
	{
		add(Arrays.asList(ss), value);	
	}
	
	public MyNode aggregate(Function<Collection<MyRow>, Double> F, String... fields) throws MyException
	{
		List<String> ff = Arrays.asList(fields);
		
		if (F == null)
			throw new MyException("FUNCTION IS NULL!");
		if(!template.keySet().containsAll(ff))
			throw new MyException("INVALID AGGREGATION FIELD!");
		
		return aggregate(F, dataset, "TOTAL", ff);

	}
	
	public MyNode aggregate(String func, String... fields) throws MyException
	{
		Function<Collection<MyRow>, Double> F = functions.get(func);
		return aggregate(F, fields);
	}
	
	private MyNode aggregate(Function<Collection<MyRow>, Double> F, Collection<MyRow> dataset, String name,
	        List<String> fields)
	{

		MyNode ret = new MyNode(name, F.apply(dataset));

		if (fields.size() > 0)
		{
			int idx = template.get(fields.get(0));

			List<String> _fields = fields.subList(1, fields.size());

			TreeMap<String, ArrayList<MyRow>> subsets = new TreeMap<>();

			for (MyRow row : dataset)
			{
				String cat = row.fields.get(idx);
				ArrayList<MyRow> list = subsets.get(cat);
				if (list == null)
					subsets.put(cat, list = new ArrayList<>());

				list.add(row);
			}

			for (String key : subsets.keySet())
				ret.children.put(key, aggregate(F, subsets.get(key), key, _fields));

		}

		return ret;
	}
	
	public MyNode aggregate2(Function<Collection<MyRow>, Double> F, Collection<MyRow> dataset, String name,
	        List<String> fields) {
		
		MyNode ret = new MyNode(name, F.apply(dataset));
		
		if (fields.size() > 0)
		{
			int idx = template.get(fields.get(0));

			List<String> _fields = fields.subList(1, fields.size() - 1);
		
			Map<Object, List<MyRow>> subsets = dataset.stream().collect(Collectors.groupingBy(x-> ((MyRow)x).fields.get(idx)));
	
			for (Object key : subsets.keySet())
				ret.children.put(key.toString(), aggregate2(F, subsets.get(key), key.toString(), _fields));
		
		}
		
		return ret;
	}
}
