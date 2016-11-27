import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class PhilosopherCondition extends Thread {
	boolean eating;
	PhilosopherCondition left;
	PhilosopherCondition right;
	ReentrantLock table;
	Condition condition;
	Random random;

	public PhilosopherCondition(ReentrantLock table) {
		eating = false;
		this.table = table;
		condition = table.newCondition();
		random = new Random();
	}

	public void setLeft(PhilosopherCondition left) { this.left = left; }
	public void setRight(PhilosopherCondition right) { this.right = right; }

	public void run() {
		try {
			while(true) {
				think();
				eat();
			}
		} catch(InterruptedException e) { }
	}

	void think() throws InterruptedException {
		table.lock();
		try {
			eating = false;
			left.condition.signal();
			right.condition.signal();
		} finally {
			table.unlock();
		}
		Thread.sleep(1000);
	}
	void eat() throws InterruptedException {
		table.lock();
		try {
			while(left.eating || right.eating) {
				condition.await();
			}
			eating = true;
			System.out.printf("eat : %d\n", hashCode());
		} finally {
			table.unlock();
		}
		Thread.sleep(1000);
	}

	public static void main(String []args) throws InterruptedException {
		int count = 5;
		ReentrantLock table = new ReentrantLock();

		PhilosopherCondition[] philosophers = new PhilosopherCondition[count];
		for(int i = 0 ; i < count ; i++) {
			philosophers[i] = new PhilosopherCondition(table);
		}

		for(int i = 0 ; i < count ; i++) {
			PhilosopherCondition left = philosophers[(i + count-1) % count];
			PhilosopherCondition right = philosophers[(i + 1) % count];
			philosophers[i].setLeft(left);
			philosophers[i].setRight(right);
		}

		for(int i = 0 ; i < count ; i++) {
			philosophers[i].start();
		}
		for(int i = 0 ; i < count ; i++) {
			philosophers[i].join();
		}
	}
}
