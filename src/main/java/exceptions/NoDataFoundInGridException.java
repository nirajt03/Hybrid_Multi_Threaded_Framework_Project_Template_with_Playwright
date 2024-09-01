package exceptions;

public class NoDataFoundInGridException extends Exception {	
	
	private static final long serialVersionUID = 953066183170719366L;

	public NoDataFoundInGridException(String errMessage) {
		super(errMessage);
	}

}
