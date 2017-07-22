package test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {
	public static void main(String[] args) {
		List<AtomicInteger> list = new ArrayList<AtomicInteger>();
		AtomicInteger a = new AtomicInteger(4);
		
		list.add(a);
		a.incrementAndGet();
		
		System.out.println(list.get(0));
		
	}

}
