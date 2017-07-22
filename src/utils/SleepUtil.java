package utils;

public class SleepUtil {
	@SuppressWarnings("static-access")
	public static void sleep(){
		try {
			Thread.currentThread().sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
