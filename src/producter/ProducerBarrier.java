package producter;

	
import core.RingBuffer;

/**
 * 负责所有的交互细节来从 Ring Buffer 中找到下一个节点，然后才允许生产者向它写入数据。
 * 
 * @author haizi
 *
 */
public class ProducerBarrier {
	private RingBuffer buffer;
	public ProducerBarrier(RingBuffer buffer) {
		this.buffer = buffer;
	}

	/**
	 * 返回下一个生产序列
	 * 
	 * @return
	 */
	public long getSequence(int n) {
		Long sequence = buffer.getNextSequence(n);
		return sequence;
	}


	public boolean commit(long sequence, Object message) {
		// TODO Auto-generated method stub
		return this.buffer.put(sequence, message);
	}
}
