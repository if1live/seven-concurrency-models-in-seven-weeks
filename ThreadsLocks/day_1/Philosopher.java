import java.util.Random;

class Chopstick {}

public class Philosopher extends Thread {
	private Chopstick left, right;
	private Random random;
	private int id;

	public Philosopher(Chopstick left, Chopstick right, int id) {
		this.id = id;
		random = new Random();

		this.left = left;
		this.right = right;
	}

	public void run() {
		try {
			while(true) {
				Thread.sleep(random.nextInt(1000));
				synchronized(left) {
					synchronized(right) {
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
			chopsticks[i] = new Chopstick();
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
