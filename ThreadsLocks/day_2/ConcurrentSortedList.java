import java.util.concurrent.locks.ReentrantLock;


class ConcurrentSortedList {
	private class Node {
		int value;
		Node prev;
		Node next;
		ReentrantLock lock = new ReentrantLock();
		Node() {}
		Node(int value, Node prev, Node next) {
			this.value = value;
			this.prev = prev;
			this.next = next;
		}
	}

	private final Node head;
	private final Node tail;

	public ConcurrentSortedList() {
		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.prev = head;
	}

	public void insert(int value) {
		Node current = head;
		current.lock.lock();
		Node next = current.next;

		try {
			while(true) {
				next.lock.lock();

				try {
					if(next == tail || next.value < value) {
						Node node = new Node(value, current, next);
						next.prev = node;
						current.next = node;
						return;
					}
				} finally {
					current.lock.unlock();
				}

				current = current.next;
				next = current.next;
			}
		} finally {
			next.lock.unlock();
		}
	}

	public int size() {
		Node current = tail;
		int count = 0;
		while(current.prev != head) {
			ReentrantLock lock = current.lock;
			lock.lock();

			try {
				++count;
				current = current.prev;
			} finally {
				lock.unlock();
			}
		}
		return count;
	}

	public static void main(String []args) throws InterruptedException {
		ConcurrentSortedList list = new ConcurrentSortedList();
		list.insert(1);
		list.insert(3);
		list.insert(2);
		System.out.println(list.size());
	}
 }
