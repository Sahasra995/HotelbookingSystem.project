package hotel;

public class InvalidServiceChargeException extends RuntimeException 
{
    public InvalidServiceChargeException(String message) 
    {
        super(message);
    }

    public InvalidServiceChargeException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
}