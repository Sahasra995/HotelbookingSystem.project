package hotel;

public class InvalidServiceChargeException extends RuntimeException 
{
    private static final long serialVersionUID = 1L;

	public InvalidServiceChargeException(String message) 
    {
        super(message);
    }

    public InvalidServiceChargeException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
}