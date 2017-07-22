package consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ConsumerTracking {
	private List<AtomicLong> tracking ;
	
	public ConsumerTracking() {
		// TODO Auto-generated constructor stub
		tracking = new ArrayList<AtomicLong>();
	}
	
	public void addConsumer(AtomicLong seq){
		tracking.add(seq);
	}
	
	public long getMin(){
		long min = Long.MAX_VALUE;
		
		for(AtomicLong seq : tracking){
			if(min < seq.get()){
				min = seq.get();
			}
		}
		return min;
	}

}
