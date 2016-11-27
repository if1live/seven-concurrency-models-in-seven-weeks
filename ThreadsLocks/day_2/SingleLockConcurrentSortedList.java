import java.util.concurrent.locks.ReentrantLock;


class SingleLockConcurrentSortedList {
	private class Node {
		int value;
		Node prev;
		Node next;
		
		Node() {}
		Node(int value, Node prev, Node next) {
			this.value = value;
			this.prev = prev;
			this.next = next;
		}
	}

	private final Node head;
	private final Node tail;
	ReentrantLock lock;

	public SingleLockConcurrentSortedList() {
		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.prev = head;
		lock  = new ReentrantLock();
	}

	public void insert(int value) {
		lock.lock();
		try {
			Node current = head;
			Node next = current.next;

			while(true) {
				if(next == tail || next.value < value) {
					Node node = new Node(value, current, next);
					next.prev = node;
					current.next = node;
					return;
				}

				current = current.next;
				next = current.next;
			}

		} finally {
			lock.unlock();
		}
	}

	public int size() {
		int count = 0;
		
		lock.lock();
		try {
			Node current = tail;
			while(current.prev != head) {		
				++count;
				current = current.prev;
			}	 
		} finally {
			lock.unlock();
		}
		return count;
	}

	public static void main(String []args) throws InterruptedException {
		SingleLockConcurrentSortedList list = new SingleLockConcurrentSortedList();
		list.insert(1);
		list.insert(3);
		list.insert(2);
		System.out.println(list.size());
	}
 }
