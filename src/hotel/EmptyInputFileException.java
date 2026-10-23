package hotel;

public class EmptyInputFileException extends Exception
{
    private static final long serialVersionUID = 1L;

	public EmptyInputFileException(String message) 
    {
        super(message);
    }

    public EmptyInputFileException(String message, Throwable cause) 
    {
        super(message, cause);
    }
    
}