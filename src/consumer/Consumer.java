package consumer;

import java.util.concurrent.atomic.AtomicLong;

import core.RingBuffer;

public class Consumer {

	private ConsumerBarrier consumerBarrier;

	private AtomicLong seq;
	private long availableSeq;

	public Consumer(RingBuffer buffer) {
		consumerBarrier = buffer.getConsumerBarrier();
		seq = new AtomicLong(consumerBarrier.getStartSeq());
		buffer.addConsumer(seq);
	}

	public Object get() {
		if (availableSeq == 0) {
			availableSeq = consumerBarrier.waitFor(seq.get());
		}
		Object res = consumerBarrier.consumerData(seq.get());
		seq.incrementAndGet();
		availableSeq--;
		return res;
	}
}
