/**
 * Philosopher.java
 *
 * This class represents each philosopher thread.
 * Philosophers alternate between eating and thinking.
 *
 */


public class Philosopher implements Runnable
{
	//the number assigned to the philosopher
	 private int n;
	
	//an instance of diningserverimpl so that a philosopher thread can take or return a fork
	private DiningServerImpl ds;
	
	//constructor for philosopher
	public Philosopher(int n, DiningServerImpl ds){
			this.n = n;
			this.ds = ds;
	}
	
	//simulates thinking or eating with a delay
	private void action(){
		try{
			Thread.sleep( (1+(int)(Math.random()*3))*1000 );
		}
		catch(InterruptedException e){
			System.err.println(e);
		}
	}
	
	//the function that runs on the thread
	@Override
	public void run(){
		while (true){
			System.out.println("Philosopher " + n + " is thinking.");
			action();

			System.out.println("Philosopher " + n + " is hungry and is now looking for a pair of chopsticks.");
			ds.takeForks(n);
			
			System.out.println("Philosopher " + n + " found a pair of chopsticks and starts to eat.");
			action();
			
			System.out.println("Philosopher " + n + " finishes eating and returns the pair of chopsticks to the table.");
			ds.returnForks(n);
		}
	}
}
