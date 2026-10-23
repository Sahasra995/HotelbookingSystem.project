package hotel;

public class InvalidRateException extends Exception 
{
    private static final long serialVersionUID = 1L;

	public InvalidRateException(String message) 
    {
        super(message);
    }

    public InvalidRateException(String message, Throwable cause) 
    {
        super(message, cause);
    }
    
}



