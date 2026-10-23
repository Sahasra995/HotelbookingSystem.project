package hotel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Main 
{

	public static void main(String[] args) 
	{
		File inputFile = new File("input/bookings.txt");
        File outputDir = new File("output");
        
        if (!outputDir.exists()) 
        {
            outputDir.mkdirs();
        }
        try {
            if (!inputFile.exists()) 
            {
                throw new FileNotFoundException("Input file not found at " + inputFile.getPath());
            }
            if (!inputFile.isFile() || !inputFile.canRead() || inputFile.length() == 0) 
            {
                throw new EmptyInputFileException("Input file is empty or unreadable.");
            }
        } catch (FileNotFoundException | EmptyInputFileException e) 
        {
            System.err.println("File Error: " + e.getMessage());
            return;
        }

        int recordsRead = 0;
        int accepted = 0;
        int rejected = 0;
        Hotel[] bookings = null;
        boolean[] breakfastFlags = null;
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
                BufferedWriter errWriter = new BufferedWriter(new FileWriter("output/errors.txt", true)))
        {

               errWriter.write("=== Execution Log: " + LocalDateTime.now() + " ===");
               errWriter.newLine();

              
               String firstLine = br.readLine();
               if (firstLine == null || firstLine.trim().isEmpty()) 
               {
                   throw new EmptyInputFileException("Missing header row (record count N).");
               }

               int totalN = Integer.parseInt(firstLine.trim());
               bookings = new Hotel[totalN];
               breakfastFlags = new boolean[totalN];

               String line;
               int fileLineNumber = 1;

               while ((line = br.readLine()) != null) 
               {
                   fileLineNumber++;
                   line = line.trim();

                   
                   if (line.isEmpty() || line.startsWith("#")) 
                   {
                       continue;
                   }

                   recordsRead++;

                   try {
                       String[] parts = line.split(",", -1);
                       for (int i = 0; i < parts.length; i++) 
                       {
                           parts[i] = parts[i].trim();
                       }

                       String type = parts[0];

                       if ("H".equalsIgnoreCase(type)) 
                       {
                           if (parts.length != 6) 
                           {
                               throw new InvalidRecordException("Standard hotel booking expects 6 fields, found " + parts.length);
                           }
                           int roomNo = Integer.parseInt(parts[1]);
                           String guestName = parts[2];
                           int nights = Integer.parseInt(parts[3]);
                           double rate = Double.parseDouble(parts[4]);
                           boolean includeBreakfast = Boolean.parseBoolean(parts[5]);

                           Hotel h = new Hotel(roomNo, guestName, nights, rate);
                           bookings[accepted] = h;
                           breakfastFlags[accepted] = includeBreakfast;
                           accepted++;

                       } 
                       else if ("S".equalsIgnoreCase(type))
                           {
                             if (parts.length != 7) 
                             {
                               throw new InvalidRecordException("Suite booking expects 7 fields, found " + parts.length);
                              }
                           int roomNo = Integer.parseInt(parts[1]);
                           String guestName = parts[2];
                           int nights = Integer.parseInt(parts[3]);
                           double rate = Double.parseDouble(parts[4]);
                           double serviceCharge = Double.parseDouble(parts[5]);
                           boolean hasJacuzzi = Boolean.parseBoolean(parts[6]);

                           SuiteBooking s = new SuiteBooking(roomNo, guestName, nights, rate, serviceCharge, hasJacuzzi);
                           bookings[accepted] = s;
                           breakfastFlags[accepted] = false;
                           accepted++;

                           } 
                       else 
                        {
                           throw new InvalidRecordException("Unknown booking type: " + type);
                        }

                   } 
                    catch (NumberFormatException | ArrayIndexOutOfBoundsException e) 
                    {
                       rejected++;
                       InvalidRecordException ire = new InvalidRecordException("Data formatting error: " + e.getMessage(), e);
                       logError(errWriter, fileLineNumber, line, ire);
                    }
                     catch (InvalidNightsException | InvalidRateException | InvalidServiceChargeException | InvalidRecordException e) {
                       rejected++;
                       logError(errWriter, fileLineNumber, line, e);
                   }
               }

           } 
              catch (IOException | EmptyInputFileException e) 
           {
               System.err.println("Critical Read Error: " + e.getMessage());
               return;
           }

          
           try (BufferedWriter repWriter = new BufferedWriter(new FileWriter("output/checkout_report.txt", false))) 
           {

               repWriter.write("==================================================");
               repWriter.newLine();
               repWriter.write("             HOTEL CHECKOUT REPORT                ");
               repWriter.newLine();
               repWriter.write("==================================================");
               repWriter.newLine();

               for (int i = 0; i < accepted; i++) 
               {
                   Hotel b = bookings[i];
                   if (b == null) continue;

                   b.display(repWriter);

                   if (b instanceof SuiteBooking) 
                   {
                       SuiteBooking s = (SuiteBooking) b;
                       double bill = s.calculateBill(false);
                       double discount = s.calculateLoyaltyDiscount();
                       double netPayable = bill - discount;

                       repWriter.write(String.format("Calculated Bill: Rs.%.2f", bill));
                       repWriter.newLine();
                       repWriter.write(String.format("Loyalty Discount: Rs.%.2f", discount));
                       repWriter.newLine();
                       repWriter.write(String.format("Net Payable: Rs.%.2f", netPayable));
                       repWriter.newLine();
                   } 
                   else 
                   {
                       double bill = b.calculateBill(breakfastFlags[i]);
                       repWriter.write(String.format("Calculated Bill: Rs.%.2f", bill));
                       repWriter.newLine();
                   }

                   repWriter.write("--------------------------------------------------");
                   repWriter.newLine();
               }

               
               if (accepted >= 2)
               {
                   int longerStayRoom = bookings[0].compareStay(bookings[1]);
                   repWriter.write("Longer stay between first two rooms: Room " + longerStayRoom);
                   repWriter.newLine();
               }

              
               int longestStayOverall = Hotel.findLongestStay(bookings);
               repWriter.write("Room with the overall longest stay: Room " + longestStayOverall);
               repWriter.newLine();

              
               repWriter.write("Total bookings successfully created: " + Hotel.bookingCount);
               repWriter.newLine();

               
               repWriter.write(String.format("Records read: %d, accepted: %d, rejected: %d.", recordsRead, accepted, rejected));
               repWriter.newLine();

           } catch (IOException e)
           {
               System.err.println("Critical Write Error: " + e.getMessage());
               return;
           }

           
           System.out.println("Processing complete. See output folder.");
       }

       private static void logError(BufferedWriter bw, int lineNum, String lineContent, Exception e) throws IOException 
       {
           bw.write(String.format("Line %d | Error: %s | Message: %s | Raw Data: [%s]",
                   lineNum,
                   e.getClass().getSimpleName(),
                   e.getMessage(),
                   lineContent));
           bw.newLine();
           if (e.getCause() != null) 
           {
               bw.write("   Caused by: " + e.getCause().toString());
               bw.newLine();
           }
       }
   
		
	}


