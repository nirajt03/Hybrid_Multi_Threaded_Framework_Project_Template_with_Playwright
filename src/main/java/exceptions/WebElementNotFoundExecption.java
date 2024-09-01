package exceptions;

public class WebElementNotFoundExecption extends Exception{

	private static final long serialVersionUID = 635516215671381002L;

	public WebElementNotFoundExecption(String errMessage){
		super(errMessage);
	}
}
