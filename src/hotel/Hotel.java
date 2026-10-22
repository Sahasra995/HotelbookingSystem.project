package hotel;

import java.io.BufferedWriter;
import java.io.IOException;

public class Hotel 
{
    
    private int roomNo;
    private String guestName;
    private int nights;
    private double ratePerNight;

   
    public static String hotelName = "Grand Resort";
    public static double tourismTaxRate = 0.12;
    public static double breakfastCharge = 350.0;
    public static int bookingCount = 0;

    
    public Hotel() 
    {
        bookingCount++;
    }

    
    public Hotel(int roomNo, String guestName, int nights) throws InvalidNightsException 
    {
        this();
        setRoomNo(roomNo);
        setGuestName(guestName);
        setNights(nights);
    }

   
    public Hotel(int roomNo, String guestName, int nights, double ratePerNight) 
            throws InvalidNightsException, InvalidRateException 
    {
        this(roomNo, guestName, nights);
        setRatePerNight(ratePerNight);
    }

    
    public int getRoomNo() 
    {
        return roomNo;
    }

    public void setRoomNo(int roomNo) 
    {
        this.roomNo = roomNo;
    }

    public String getGuestName() 
    {
        return guestName;
    }

    public void setGuestName(String guestName) 
    {
        this.guestName = guestName;
    }

    public int getNights() 
    {
        return nights;
    }

    public void setNights(int nights) throws InvalidNightsException
    {
        if (nights < 1) 
        {
            throw new InvalidNightsException("Nights must be >= 1. Provided: " + nights, nights);
        }
        this.nights = nights;
    }

    public double getRatePerNight() 
    {
        return ratePerNight;
    }

    public void setRatePerNight(double ratePerNight) throws InvalidRateException
    {
        if (ratePerNight <= 0) 
        {
            throw new InvalidRateException("Rate per night must be > 0. Provided: " + ratePerNight);
        }
        this.ratePerNight = ratePerNight;
    }

    
    public double calculateBill() 
    {
        return nights * ratePerNight * (1.0 + tourismTaxRate);
    }

    public double calculateBill(boolean includeBreakfast) 
    {
        double total = calculateBill();
        if (includeBreakfast) {
            total += (breakfastCharge * nights);
        }
        return total;
    }

    public int compareStay(Hotel other)
    {
        if (other == null || this.nights >= other.nights) 
        {
            return this.roomNo;
        }
        return other.roomNo;
    }

    public void display(BufferedWriter bw) throws IOException {
        bw.write("Room No: " + roomNo);
        bw.newLine();
        bw.write("Guest Name: " + guestName);
        bw.newLine();
        bw.write("Nights: " + nights);
        bw.newLine();
        bw.write(String.format("Rate Per Night: %.2f", ratePerNight));
        bw.newLine();
    }
}