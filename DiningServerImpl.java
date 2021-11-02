/**
 * DiningServer.java
 *
 * This class contains the methods called by the  philosophers.
 *
 */

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DiningServerImpl  implements DiningServer
{  

	//States that a philosopher can be in
	enum State {THINKING, HUNGRY, EATING};
	
	//Declaring an array of states to represent the state of each philosopher
	private State[] state = new State[5];
	
	//Conditional variables for each philosopher for waiting and signaling
	private Condition[] pCond= new Condition[5];
	
	//ReentrantLock, a thread can now claim the same lock multiple times without blocking on itself so no deadlock
	private Lock lock = new ReentrantLock();
	
	//Constructor
	public DiningServerImpl() {
		for (int i = 0; i < 5; i++) {
			//create a new condition variable for each philosopher
			pCond[i] = lock.newCondition();
			//set the state of each philosopher to thinking
			state[i] = State.THINKING;
		}
	}
	
	
	//Called when a philosopher wants to eat
	@Override
	public void takeForks(int philNumber) {
		//Acquires the lock, disables the current thread until it acquires it. As a result, only one philosopher is returning/taking the forks at a time which makes sure only one person is altering the states at a time
		lock.lock();
		
		//check if the philosophers next to current philosophers are eating. if they are, wait for a signal and check again. if not, take the chopsticks 
		while ( state[(philNumber + 4) % 5] == State.EATING || state[(philNumber + 1) % 5] == State.EATING){
			try{
				//the current philosopher is now hungry and is looking for chopsticks
				state[philNumber] = State.HUNGRY;
				//pauses execution and waits for a signal from one of the two philosophers next to the current philosopher that they finished eating, once it receives a signal, they attempt to acquire a lock
				// and once they do so they check the condition again
				//once the thread is paused, the lock is also released for other philosophers to perform their actions
				pCond[philNumber].await();
			}
			catch(InterruptedException e){
				System.err.println(e);
			}
		}
		
		//if the while loop exits means that philosopher was able to acquire chopsticks and is now eating
		state[philNumber] = State.EATING;
		
		//Releases the lock for other philosophers to acquire
		lock.unlock();
	}

	// Called when a philosopher is done eating
	@Override
	public void returnForks(int philNumber) {
		//Acquires the lock, disables the current thread until it acquires it. As a result, only one philosopher is returning/taking the forks at a time which makes sure only one person is altering the states at a time
		lock.lock();
		
		//set the state of the philosopher to thinking
		state[philNumber] = State.THINKING;
		
		//signal the philosphers to the right and left of the current philosophers, represents the philosopher putting their chopsticks down
		pCond[(philNumber + 4) % 5].signal();
		pCond[(philNumber + 1) % 5].signal();
		
		//Releases the lock for other philosophers to acquire
		lock.unlock();
	}
	
}
