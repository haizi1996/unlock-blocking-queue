package singleton.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import singleton.core.RingBuffer;
import utils.SleepUtil;

public class Consumer {
	private ConsumerBarrier consumerBarrier;

	private long availableSeq;
	private long sequence;
	private AtomicLong consumer_cur;

	public Consumer(RingBuffer buffer) {
		consumerBarrier = buffer.getConsumerBarrier();
		consumer_cur = buffer.getConsumer_cur();
	}

	public Object get() {
		sequence = consumerBarrier.consumeSeq(1);
		cyCle(sequence);
		consumerBarrier.waitFor(sequence);
		Object res = consumerBarrier.consumerData(sequence);
		return res;
	}

	public List<Object> getSomeMessage(int n) {
		sequence = consumerBarrier.consumeSeq(n);
		List<Object> res = new ArrayList<Object>();
		int count = 0;
		cyCle(sequence);
		while (count < n) {
			if (availableSeq == 0) {
				availableSeq = consumerBarrier.waitFor(sequence);
			}
			while(availableSeq != 0){
				res.add(consumerBarrier.consumerData(sequence));
				availableSeq--;
			}
		}
		consumer_cur.addAndGet(n);
		return res;
	}
	/**
	 * 自旋转
	 */
	private void cyCle(long seq) {
		// TODO Auto-generated method stub
		int count = 0;
		while(true){
			if(seq - 1 ==  consumer_cur.get()){
				return ;
			}
			if(++count == 10 ){
				SleepUtil.sleep();
				count = 0;
			}
		}
	}
}
