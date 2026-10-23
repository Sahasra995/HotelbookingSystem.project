package hotel;

		public class InvalidNightsException extends Exception 
   {
		    private static final long serialVersionUID = 1L;
			private int invalidNights;

		    public InvalidNightsException(String message) 
		    {
		        super(message);
		    }

		    public InvalidNightsException(String message, Throwable cause) 
		    {
		        super(message, cause);
		    }

		    public InvalidNightsException(String message, int invalidNights) 
		    {
		        super(message);
		        this.invalidNights = invalidNights;
		    }

		    public int getInvalidNights() 
		    {
		        return invalidNights;
		    }
		
		

 }


