package tpnotesynchro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class OurAlgorithm implements Algorithm
{
	private Instance instance;
	private long max_cpu;
	private long startTime;
	private Random rnd;
	
	private final int nbrThreads = 8;
	
	@Override
	public Solution run(Properties config) {
		//read instance
		InstanceReader ir=new InstanceReader();
		ir.buildInstance(config.getProperty("instance"));
		//get the instance 
		instance=ir.getInstance();
		//print some distances
		System.out.println("d(1,2)="+instance.getDistance(1, 2));
		System.out.println("d(10,19)="+instance.getDistance(10, 19));
		//read maximum CPU time
		max_cpu=Long.valueOf(config.getProperty("maxcpu"));
		//build a random solution
		rnd=new Random(Long.valueOf(config.getProperty("seed")));
		
		ExecutorService executor = Executors.newFixedThreadPool(nbrThreads);
		List<Future<Solution>> future = null;
		List<Callable<Solution>> tasks = new ArrayList<>();
		
		for(int i = 0; i < nbrThreads; i++)
			tasks.add(new mainCallable());
			
		startTime=System.currentTimeMillis();
		
		Solution best = null; 
		try{
			future = executor.invokeAll(tasks);
			best = future.get(0).get();

			for(Future<Solution> s : future)
			{
				Solution sp = s.get();
				TSPCostCalculator costCalculator = new TSPCostCalculator(instance.getDistanceMatrix());
				sp.setOF(costCalculator.calcOF(sp));
				best.setOF(costCalculator.calcOF(best));
				if(sp.getOF() < best.getOF())
					best = sp.clone();			
			}
		}catch(Exception e){System.out.println(e.getMessage());}
		
		executor.shutdown();
		return best;
	}
	
	private class mainCallable implements Callable <Solution>
	{
		public Solution call() throws InterruptedException
		{
			Solution s=new Solution();
			for(int i=0; i<instance.getN(); i++)
				s.add(i);
			Solution best=s.clone();
			while((System.currentTimeMillis()-startTime)/1_000<=max_cpu)
			{
				//System.out.println((System.currentTimeMillis()-startTime) / 1_000);
				Collections.shuffle(s, rnd);
				s = localSearch(s);
				TSPCostCalculator costCalculator = new TSPCostCalculator(instance.getDistanceMatrix());
				s.setOF(costCalculator.calcOF(s));
				best.setOF(costCalculator.calcOF(best));
				if(s.getOF() < best.getOF())
					best = s.clone();
			}
			return best;
		}
	}
	
	private Solution localSearch(Solution s) throws InterruptedException
	{
		boolean cont = true;
		Solution sp = null;
		
		while(cont && (System.currentTimeMillis()-startTime)/1_000<=max_cpu )
		{
			sp = exploreNeighborhood(s);
			TSPCostCalculator costCalculator = new TSPCostCalculator(instance.getDistanceMatrix());
			sp.setOF(costCalculator.calcOF(sp));
			s.setOF(costCalculator.calcOF(s));
			if(sp.getOF() < s.getOF())
				s = sp.clone();
			else
				cont = false;
		}
		
		return s;
	}	
	
	private Solution exploreNeighborhood(Solution s)
	{
		Solution sp = s.clone();
		
		for(int i = 0; i < s.size(); i++)
			for(int j = 0; j < s.size(); j++)
			{
				if(i != j)
				{
					s.swap(i, j);
					TSPCostCalculator costCalculator = new TSPCostCalculator(instance.getDistanceMatrix());
					sp.setOF(costCalculator.calcOF(sp));
					s.setOF(costCalculator.calcOF(s));
					if(s.getOF() < sp.getOF())
						sp = s.clone();
				}
			}
		
		return sp;
	}
}
