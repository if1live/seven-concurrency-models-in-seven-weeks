import java.util.Random;

class PhilosopherWaitNotify extends Thread {

	Random random;

	int id;
	boolean eating;

	PhilosopherWaitNotify left;
	PhilosopherWaitNotify right;
	Object lock;

	public PhilosopherWaitNotify(int id) {
		this.id = id;
		eating = false;
		random = new Random();
		lock = new Object();
	}

	public void setLeft(PhilosopherWaitNotify left) { this.left = left; }
	public void setRight(PhilosopherWaitNotify right) { this.right = right; }


	public void run() {
		try {
			while(true) {
				think();
				eat();
			}
		} catch(InterruptedException e) {
		}
	}

	void think() throws InterruptedException {
		eating = false;
		synchronized(left.lock) {
			left.lock.notify();
			//System.out.printf("unlock %d\n", left.id);
		}
		synchronized(right.lock) {
			right.lock.notify();
			//System.out.printf("unlock %d\n", right.id);
		}
		
		Thread.sleep(random.nextInt(1000));
	}
	void eat() throws InterruptedException {
		synchronized(lock) {
			while(left.eating || right.eating) {
				lock.wait();
			}

			eating = true;
			System.out.printf("eat : %d\n", id);
		}
		
		Thread.sleep(random.nextInt(1000));
	}



	public static void main(String[] args) throws InterruptedException {
		int count = 5;

		PhilosopherWaitNotify[] philosophers = new PhilosopherWaitNotify[count];
		for(int i = 0 ; i < count ; i++) {
			philosophers[i] = new PhilosopherWaitNotify(i);
		}		
		for(int i = 0; i < count ; i++) {
			PhilosopherWaitNotify left = philosophers[(i+count-1) % count];
			PhilosopherWaitNotify right = philosophers[(i+1) % count];
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
