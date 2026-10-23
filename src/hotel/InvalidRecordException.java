package hotel;

public class InvalidRecordException extends Exception 
{
    private static final long serialVersionUID = 1L;

	public InvalidRecordException(String message) 
    {
        super(message);
    }

    public InvalidRecordException(String message, Throwable cause) 
    {
        super(message, cause);
    }
    
}