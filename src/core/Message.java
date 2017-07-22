package core;

public class Message<T> {
	
	
	private T message;

	public Message(T message) {
		this.message = message;
	}

	public T getMessage() {
		return message;
	}

	public void setMessage(T message) {
		this.message = message;
	}

	
	
}
