package producter;

import java.util.List;

import core.RingBuffer;

public class Producer {
	
	private ProducerBarrier producerBarrier ;
	
	
	public Producer(RingBuffer buffer) {
		producerBarrier = new ProducerBarrier(buffer);
	}
	/**
	 * 返回下一个生产序列
	 * @return
	 */
	private long nextEntry(int n){
		return producerBarrier.getSequence(n);
	}
	
	private boolean commit(long sequence, Object message){
		this.producerBarrier.commit(sequence,message);
		return true;
	}
	/**
	 * 生产数据
	 * @param message
	 * @return
	 */
	public boolean produceMessage(Object message){
		Long sequence = nextEntry(1);
		return commit(sequence,message);
	}
	
	public boolean produceMessages(List<Object> messages){
		Long seq = nextEntry(messages.size());
		for(int i = 0 ; i < messages.size()  ; i++){
			commit(seq + i, messages.get(i));
		}
		return true;
	}

}
