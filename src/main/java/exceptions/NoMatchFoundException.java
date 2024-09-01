package exceptions;

public class NoMatchFoundException extends Exception {
	
	private static final long serialVersionUID = 7223150825466766562L;

	public NoMatchFoundException(String errMessage) {
		super(errMessage);
	}
}
