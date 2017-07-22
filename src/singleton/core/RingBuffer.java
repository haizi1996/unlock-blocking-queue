package singleton.core;

import java.util.concurrent.atomic.AtomicLong;

import singleton.consumer.ConsumerBarrier;
import utils.SleepUtil;

public class RingBuffer {
	/**
	 * 数组长度
	 */
	private int size;
	/**
	 * 记录尾指针,保证所有的数据都是顺序写入
	 */
	private AtomicLong cur = new AtomicLong(-1);
	/**
	 * 分发序列号,下一个可产生消息的序列号
	 */
	private AtomicLong next = new AtomicLong(0);
	/**
	 * 环形消息数组
	 */
	private Object[] messages;

	/**
	 * 当前已消费的序列号
	 */
	private AtomicLong consumer_cur = new AtomicLong(-1);
	/**
	 * 用于分发消费的序列号,下一个消费的开始
	 */
	private AtomicLong consumer_next = new AtomicLong(0);

	private volatile ConsumerBarrier consumerBarrier;

	public RingBuffer(int size) {
		this.size = 1 << size;
		messages = new Object[size];

	}

	/**
	 * 获取消费的序列号
	 * 
	 * @param seq
	 * @return
	 */

	public long getNextSeq(int seq) {
		long oldSeq, newSeq;
		do {
			oldSeq = consumer_next.get();
			newSeq = oldSeq + seq;
		} while (consumer_cur.compareAndSet(oldSeq, newSeq));
		return oldSeq;
	}

	/**
	 * 加入数据
	 * 
	 * @param sequence
	 * @param message
	 * @return
	 */
	public boolean put(long sequence, Object message) {
		int index = getIndex(sequence);
		int count = 0;
		long o = sequence - this.size;
		while (o > consumer_cur.get()) {
			if (++count == 10) {
				SleepUtil.sleep();
				count = 0;
			}
		}
		while (!(cur.get() == sequence - 1)) {
			if (++count == 10) {
				SleepUtil.sleep();
				count = 0;
			}
		}
		messages[index] = message;
		cur.incrementAndGet();
		return true;
	}

	/**
	 * 获取下一个序列号
	 * 
	 * @return
	 */
	public long getNextSequence(int n) {
		// TODO Auto-generated method stub
		long cur;
		long nextSeq;
		do {
			cur = next.get();
			nextSeq = cur + n;
		} while (!next.compareAndSet(cur, nextSeq));
		return cur;
	}

	/**
	 * 获取环形数组的长度
	 * 
	 * @return
	 */
	public int getLength() {
		// TODO Auto-generated method stub
		return size;
	}

	/**
	 * 获取尾指针
	 * 
	 * @return
	 */
	public long getTailIndex() {
		return cur.get();
	}

	public ConsumerBarrier getConsumerBarrier() {
		return consumerBarrier;
	}

	/**
	 * 获取可用的序列数
	 * 
	 * @param nextSeq
	 */
	public long waitFor(long nextSeq) {
		// TODO Auto-generated method stub
		int count = 0;
		while (true) {
			if (cur.get() - nextSeq >= 0) {
				return cur.get() - nextSeq + 1;
			}
			if (++count == 10) {
				SleepUtil.sleep();
			}
		}
	}

	public Object getMessageByIndex(long seq) {
		// TODO Auto-generated method stub
		int index = getIndex(seq);
		Object message = messages[index];
		return message;
	}

	public boolean remove(long nextSeq) {
		// TODO Auto-generated method stub
		consumer_cur.incrementAndGet();
		return true;
	}

	private int getIndex(long nextSeq) {
		return (int) (nextSeq & (size - 1));
	}

	public long consumerSeq(int n) {
		// TODO Auto-generated method stub
		this.consumer_next.addAndGet(n);
		long oldSeq, newSeq;
		do {
			oldSeq = consumer_next.get();
			newSeq = oldSeq + n;
		} while (!consumer_next.compareAndSet(oldSeq, newSeq));
		return oldSeq;
	}

	public AtomicLong getConsumer_cur() {
		// TODO Auto-generated method stub
		return consumer_cur;
	}
}
