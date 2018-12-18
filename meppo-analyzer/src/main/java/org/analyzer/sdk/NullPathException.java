package org.analyzer.sdk;

public class NullPathException extends Exception {
	
	public NullPathException() {
		super("the dic path is null");
	}
	
	public NullPathException(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = 628375128425584018L;
}
