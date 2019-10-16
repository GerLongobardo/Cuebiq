package CuebiqCompetition.parallelCueBiq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import CuebiqCompetition.CueBiq.MyException;
import CuebiqCompetition.CueBiq.MyRow;

public class ParallelRowContainer
{

	private HashMap<String, Integer>                               template  = new HashMap<>();
	private ArrayList<MyRow>                                     dataset   = new ArrayList<>();
	private HashMap<String, Function<Collection<Integer>, Double>> functions = new HashMap<>();
	private ExecutorService                                       executor  =Executors.newFixedThreadPool(8);
	

	/* L'approccio deve essere necessariamente top-down dato che le Function non necessariamente
	 * possono essere ottenute da risultati di altre Function su subset 
	 * 
	 *
	 */
	
	public ParallelRowContainer() 
	{

		Function<Collection<Integer>, Double> sum_f = x -> 
		{
			return 1.0 * x.stream().mapToInt(a -> a).sum();
		};
		
		functions.put("SUM", sum_f);
		
		Function<Collection<Integer>, Double> med_f = x -> 
		{
			Integer[] l = x.toArray(new Integer[0]);
			Arrays.parallelSort(l);
			return new Double(l[l.length / 2]);
		};
		
		functions.put("MEDIAN", med_f);

	}
	
	public void addFunction(String key, Function<Collection<Integer>, Double> F)
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
	
	public FutNode aggregate(Function<Collection<Integer>, Double> F, String... fields) throws MyException
	{
		List<String> ff = Arrays.asList(fields);
		
		if (F == null)
			throw new MyException("FUNCTION IS NULL!");
		if(!template.keySet().containsAll(ff))
			throw new MyException("INVALID AGGREGATION FIELD!");
		
		return aggregate(F, dataset,  "TOTAL", ff);

	}
	
	public FutNode aggregate(String func, String... fields) throws MyException
	{
		Function<Collection<Integer>, Double> F = functions.get(func);
		return aggregate(F, fields);
	}
	
	private FutNode aggregate(Function<Collection<Integer>, Double> F, ArrayList<MyRow> dataset, String name,
	        List<String> fields)
	{
		List<Integer> values = dataset.stream().map(MyRow::getValue).collect(Collectors.toList());
		FutNode ret = new FutNode(name, executor.submit(()->F.apply(values)));
		
		if(fields.size() > 0)
		{
			int idx = template.get(fields.get(0));

			List<String> _fields = fields.subList(1, fields.size());

			TreeMap<String, ArrayList<MyRow>> subsets = new TreeMap<>();

			for (MyRow row : dataset)
			{
				String cat = row.fields.get(idx);
				ArrayList<MyRow> list_t = subsets.get(cat);
				if (list_t == null)
					subsets.put(cat, list_t = new ArrayList<>());
				

				list_t.add(row);
			}

			for (String key : subsets.keySet())
				ret.children.put(key, aggregate(F, subsets.get(key), key, _fields));

		}

		return ret;
	}
	
	public FutNode aggregate2(Function<Collection<Integer>, Double> F, Collection<MyRow> dataset, String name,
	        List<String> fields) {
		
		List<Integer> values = dataset.stream().map(MyRow::getValue).collect(Collectors.toList());
		FutNode ret = new FutNode(name, executor.submit(()->F.apply(values)));
		
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
