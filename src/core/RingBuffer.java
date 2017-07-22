package core;
/**
 * 环形数组
 * @author haizi
 *
 */

import java.util.concurrent.atomic.AtomicLong;

import consumer.ConsumerBarrier;
import consumer.ConsumerTracking;
import utils.SleepUtil;

public class RingBuffer {
	/**
	 * 数组长度
	 */
	private int size;
	/**
	 * 记录尾指针,保证所有的数据都是顺序写入
	 */
	private  AtomicLong cur = new AtomicLong(-1);
	/**
	 * 分发序列号
	 */
	private  AtomicLong next = new AtomicLong(0);
	
	
	
	/**
	 * 环形消息数组
	 */
	private  Object[] messages;
	
	/**
	 * 环形消息数组的s状态
	 */
//	private  AtomicInteger[] status;
	
	private ConsumerTracking tracking ; 
	
	
	private volatile ConsumerBarrier consumerBarrier;

	public RingBuffer(int size) {
		this.size = 1<<size ;
		messages = new Object[size];
		consumerBarrier = new ConsumerBarrier(this);
		tracking = new ConsumerTracking();
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
		int count = 0 ;
		long o = sequence - this.size;
		while(o >= tracking.getMin()){
			if( ++ count == 10){
				SleepUtil.sleep();
				count = 0;
			}
		}
		while(!(cur.get() == sequence - 1)){
			if( ++ count == 10){
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
		long cur ;
		long nextSeq;
		do{
			cur = next.get();
			nextSeq = cur +n;
		}while(!next.compareAndSet(cur, nextSeq));
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
				count = 0;
			}
		}
	}

	public Object getMessageByIndex(long seq) {
		// TODO Auto-generated method stub
		int index = getIndex(seq);
		Object message = messages[index];
		return message;
	}



	
	public void addConsumer(AtomicLong seq){
		this.tracking.addConsumer(seq);
	}




	private int getIndex(long nextSeq) {
		return (int)(nextSeq &( size - 1));
	}
	public long getStartSeq(){
		return cur.incrementAndGet();
	}

	
}
