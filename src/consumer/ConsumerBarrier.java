package consumer;

import java.util.concurrent.atomic.AtomicLong;

import core.RingBuffer;

public class ConsumerBarrier {
	
	private RingBuffer ringBuffer;
	
	public ConsumerBarrier(RingBuffer ringBuffer) {
		this.ringBuffer = ringBuffer;
	}
	
	

	public Object consumerData(long seq){
		return this.ringBuffer.getMessageByIndex(seq);
	}
	
	public long waitFor(long nextSeq){
		return ringBuffer.waitFor(nextSeq);
	}



	public long getNextSequence(AtomicLong seq) {
		// TODO Auto-generated method stub
		
		return 0;
	}



	public Long getStartSeq() {
		// TODO Auto-generated method stub
		return this.ringBuffer.getStartSeq();
	}

}
