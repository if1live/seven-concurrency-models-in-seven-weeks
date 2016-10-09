import java.util.Random;

class Chopstick {
	private int id;
	public Chopstick(int id) {
		this.id = id;
	}
	public int getId() { return id; }
}

public class PhilosopherLockOrder extends Thread {
	private Chopstick first, second;
	private Random random;
	private int id;

	public PhilosopherLockOrder(Chopstick left, Chopstick right, int id) {
		this.id = id;
		random = new Random();

		// lock order
		if(left.getId() < right.getId()) {
			first = left;
			second = right;
		} else {
			first = right;
			second = left;
		}
	}

	public void run() {
		try {
			while(true) {
				Thread.sleep(random.nextInt(1000));
				synchronized(first) {
					synchronized(second) {
						System.out.println("eat. id=" + id);
						Thread.sleep(random.nextInt(1000));
					}
				}
			}
		} catch(InterruptedException e) {}
	}

	public static void main(String[] args) throws InterruptedException {
		int count = 5;
		Chopstick[] chopsticks = new Chopstick[count];
		for(int i = 0 ; i < count ; i++) {
			chopsticks[i] = new Chopstick(i);
		}

		Philosopher[] philosophers = new Philosopher[count];
		for(int i = 0 ; i < count ; i++) {
			Chopstick left = chopsticks[i];
			Chopstick right = chopsticks[(i + 1) % count];
			philosophers[i] = new Philosopher(left, right, i);
		}

		for(int i = 0 ; i < count ; i++) {
			philosophers[i].start();
		}
	}
}
