package hotel;

public class InvalidRateException extends Exception 
{
    public InvalidRateException(String message) 
    {
        super(message);
    }

    public InvalidRateException(String message, Throwable cause) 
    {
        super(message, cause);
    }
    
}



