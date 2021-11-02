/**
 * DiningPhilosophers.java
 *
 * This program starts the dining philosophers problem.
 *
 */


public class DiningPhilosophers
{  
   public static void main(String args[])
   {
		
		DiningServerImpl dsi = new DiningServerImpl();
		Philosopher[] ph = new Philosopher[5];
		
		//create philosophers
		for( int i = 0; i < 5; i++ ){
			ph[i] = new Philosopher(i, dsi);
		}
		
		//create threads for the philosophers to run on
		Thread[] threads = new Thread[5];
		for (int i = 0; i < 5; i++) {
			threads[i] = new Thread(ph[i]);
		}
		
		//start each thread
		for (int i = 0; i < 5; i++) {
			threads[i].start();
		}
		
   }
}
