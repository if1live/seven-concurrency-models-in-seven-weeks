import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;

class PhilosopherTimeout extends Thread {
	private ReentrantLock leftChopstick;
	private ReentrantLock rightChopstick;
	private Random random;

	public PhilosopherTimeout(ReentrantLock leftChopstick, ReentrantLock rightChopstick) {
		this.leftChopstick = leftChopstick;
		this.rightChopstick = rightChopstick;
		random = new Random();
	}

	public void run() {
		try {
			while(true) {
				Thread.sleep(random.nextInt(1000));
				leftChopstick.lock();
				try {
					if(rightChopstick.tryLock(1000, TimeUnit.MILLISECONDS)) {
						try {
							//System.out.println("eat");
							Thread.sleep(random.nextInt(1000));
						} finally {
							rightChopstick.unlock();
						}
					} else {
						;
					}
				} finally {
					leftChopstick.unlock();
				}
			}
		} catch(InterruptedException e) { }
	}

	public static void main(String []args) throws InterruptedException {
		int count = 5;
		ReentrantLock[] chopsticks = new ReentrantLock[count];
		for(int i = 0 ; i < count ; i++) {
			chopsticks[i] = new ReentrantLock();
		}

		PhilosopherTimeout[] philosophers = new PhilosopherTimeout[count];
		for(int i = 0 ; i < count ; i++) {
			ReentrantLock left = chopsticks[i];
			ReentrantLock right = chopsticks[(i + 1) % count];
			philosophers[i] = new PhilosopherTimeout(left, right);
		}

		for(int i = 0 ; i < count ; i++) {
			philosophers[i].start();
		}
	}
}
