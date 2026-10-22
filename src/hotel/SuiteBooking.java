package hotel;

import java.io.BufferedWriter;
import java.io.IOException;

public class SuiteBooking extends Hotel {
   
    private double serviceChargePercent;
    private boolean hasJacuzzi;

    
    public static double jacuzziChargePerNight = 500.0;

    
    public SuiteBooking() {
        super();
    }

    
    public SuiteBooking(int roomNo, String guestName, int nights, double ratePerNight, 
                        double serviceChargePercent, boolean hasJacuzzi) 
                        throws InvalidNightsException, InvalidRateException {
        super(roomNo, guestName, nights, ratePerNight);
        setServiceChargePercent(serviceChargePercent);
        this.hasJacuzzi = hasJacuzzi;
    }

    
    public double getServiceChargePercent() {
        return serviceChargePercent;
    }

    public void setServiceChargePercent(double serviceChargePercent) {
        
        if (serviceChargePercent < 0 || serviceChargePercent > 100) {
            throw new InvalidServiceChargeException("Service charge percent must be between 0 and 100: " + serviceChargePercent);
        }
        this.serviceChargePercent = serviceChargePercent;
    }

    public boolean isHasJacuzzi() {
        return hasJacuzzi;
    }

    public void setHasJacuzzi(boolean hasJacuzzi) {
        this.hasJacuzzi = hasJacuzzi;
    }

    
    public double calculateBill() {
        double baseWithTax = super.calculateBill();
        double serviceFee = getNights() * getRatePerNight() * (serviceChargePercent / 100.0);
        double jacuzziCost = hasJacuzzi ? (jacuzziChargePerNight * getNights()) : 0.0;
        return baseWithTax + serviceFee + jacuzziCost;
    }

    
    public double calculateBill(boolean includeBreakfast) {
        return calculateBill();
    }

   
    public double calculateLoyaltyDiscount() {
        if (getNights() > 5) {
            return 0.10 * calculateBill();
        }
        return 0.0;
    }

    public void display(BufferedWriter bw) throws IOException {
        super.display(bw);
        bw.write("Service Charge Percent: " + String.format("%.2f", serviceChargePercent) + "%");
        bw.newLine();
        bw.write("Jacuzzi Included: " + (hasJacuzzi ? "Yes" : "No"));
        bw.newLine();
    }
}