package lesco;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Billing_Management
{
    private String billFilename = "BillingInfo.txt";
    private String custFilename = "CustomerInfo.txt";
    private String taxFilename = "TariffTaxInfo.txt";
    private String[] arrayList;
    private String[] billList;
    private String[][] bills; 
    
// Helper method to validate if a string is numeric
private boolean isDigits(String str) {
    return str.matches("\\d+");
}

public boolean addNewBill(String custID, String billingMonth, String currentMeterReading, String currentMeterReadingPeak) {
    // Default bill values
    String paidStatus = "UnPaid";
    String paymentDate = "Not Paid";

    // Format for date handling
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate currentDate = LocalDate.now();
    String readingEntryDate = currentDate.format(formatter);
    String dueDate = currentDate.plusDays(7).format(formatter);

    // Initialize arrayList if necessary
    if (arrayList == null) {
        arrayList = loadCustomerDetails(custID);
        if (arrayList == null) {
            System.out.println("Error: Unable to load customer details.");
            return false;
        }
    }

    // Check if the bill already exists for the current year and month
    if (isBillExists(custID, billingMonth)) {
        System.out.println("\nThe Bill For the Month Already Exists.");
        return false;
    }

    // Validate current meter readings
    if (!isValidReading(currentMeterReading, "Invalid Current Meter Reading") || !isValidReading(currentMeterReadingPeak, "Invalid Peak Reading")) {
        return false;
    }

    // Fetch tax data based on meter type
    String meterType = arrayList[6];
    String[] tax = getTaxData("D", meterType);

    // **Calculate the cost of electricity based on meter type**
    float costOfElectricity = 0;
    float regularUnitsConsumed = Float.parseFloat(currentMeterReading) - Float.parseFloat(arrayList[8]); // Previous regular reading
    if (meterType.equalsIgnoreCase("s")) { // Single-phase meter
        costOfElectricity = regularUnitsConsumed * 5; // Domestic rate for single-phase
    } else if (meterType.equalsIgnoreCase("t")) { // Three-phase meter
        float peakUnitsConsumed = Float.parseFloat(currentMeterReadingPeak) - Float.parseFloat(arrayList[9]); // Previous peak reading
        costOfElectricity = (regularUnitsConsumed * 8) + (peakUnitsConsumed * 12); // Domestic rates for three-phase
    }

    // Calculate tax, fixed charges, and total bill amount
    float salesTaxAmount = costOfElectricity * (Float.parseFloat(tax[3]) / 100);
    float fixedCharges = Float.parseFloat(tax[4]);
    float totalBillingAmount = costOfElectricity + salesTaxAmount + fixedCharges;

    // Prepare bill data to be written to the file
    String billData = String.join(",", custID, billingMonth, currentMeterReading, currentMeterReadingPeak,
            readingEntryDate, String.valueOf(costOfElectricity), String.valueOf(salesTaxAmount),
            String.valueOf(fixedCharges), String.valueOf(totalBillingAmount), dueDate, paidStatus, paymentDate);

    // Append bill data to the billing file
    appendFile(billFilename, billData);

    System.out.println("Bill Added Successfully!");
    return true;
}

public String[] loadCustomerDetails(String custID) {
    try {
        BufferedReader reader = new BufferedReader(new FileReader(custFilename));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] customerData = line.split(",");
            if (customerData[0].equals(custID)) {
                reader.close();
                return customerData; // Return the customer details as an array
            }
        }
        reader.close();
        System.out.println("Customer with ID " + custID + " not found.");
        return null; // Customer not found
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
}

public boolean isValidReading(String reading, String errorMessage) {
    try {
        int meterReading = Integer.parseInt(reading);
        if (meterReading >= 0) {
            return true; // Valid meter reading
        } else {
            System.out.println(errorMessage + ": Meter reading cannot be negative.");
            return false;
        }
    } catch (NumberFormatException e) {
        System.out.println(errorMessage + ": Invalid format for meter reading.");
        return false;
    }
}


// Method to append new data to the file
private void appendFile(String filename, String data) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
        writer.write(data);
        writer.newLine(); // Move to the next line
    } catch (IOException e) {
        System.out.println("Error Writing to File: " + e.getMessage());
    }
}

// Method to check if a bill already exists for the customer in the current year and billing month
private boolean isBillExists(String custID, String billingMonth) {
    try (BufferedReader reader = new BufferedReader(new FileReader(billFilename))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] billData = line.split(",");
            String existingCustID = billData[0];
            String existingBillingMonth = billData[1];

            // Check if the same customer and billing month exist in the file
            if (existingCustID.equals(custID) && existingBillingMonth.equals(billingMonth)) {
                return true; // Bill exists
            }
        }
    } catch (IOException e) {
        System.out.println("Error Reading the Bill File: " + e.getMessage());
    }

    return false; // No matching bill found
}

public boolean changePaidStatus(String custID, String billingMonth, String entryDate, String paymentDate) {
    // Validate billingMonth and entryDate
    if (!validateBillingMonth(billingMonth)) {
        System.out.println("Incorrect Month: Try Again");
        return false;
    }
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    try {
        LocalDate.parse(entryDate, formatter);
    } catch (DateTimeParseException e) {
        System.out.println("Invalid Date: Try Again");
        return false;
    }

    if (!validateCustomerID(custID)) {
        System.out.println("Customer ID Invalid: Try Again");
        return false;
    }

    String readingEntryDate = "";
    String RUC = "";
    String PHUC = "";
    String status = "";
    String line;
    String[] data;
    boolean found = false;

    // Search for the bill
    try (BufferedReader br = new BufferedReader(new FileReader(billFilename))) {
        while ((line = br.readLine()) != null) {
            data = line.split(",");
            if (data[0].equals(custID) && data[1].equals(billingMonth) && data[4].equals(entryDate)) {
                readingEntryDate = data[4];
                RUC = data[2];
                PHUC = data[3];
                status = data[10];
                found = true;
                break;
            }
        }
    } catch (IOException e) {
        System.out.println("Error Reading File: " + e.getMessage());
        return false;
    }

    if (!found) {
        System.out.println("No Such Bill Found");
        return false;
    }

    if (status.equals("Paid")) {
        System.out.println("The Status was Already Updated to Paid");
        return false;
    }

    // Validate the paid date
    LocalDate readingDate = LocalDate.parse(readingEntryDate, formatter);
    try {
        LocalDate paidDate = LocalDate.parse(paymentDate, formatter);
        if (paidDate.isBefore(readingDate)) {
            System.out.println("Error: Payment Date is before Reading Date");
            return false;
        }
    } catch (DateTimeParseException e) {
        System.out.println("Invalid Date: Try Again");
        return false;
    }

    // Update the bill status in the file
    ArrayList<String> array = new ArrayList<>();
    try {
        BufferedReader br = new BufferedReader(new FileReader(billFilename));
        String line2;
        while ((line2 = br.readLine()) != null) {
            String[] getLine = line2.split(",");
            if (getLine[0].equals(custID) && getLine[1].equals(billingMonth) && getLine[4].equals(entryDate)) {
                getLine[11] = paymentDate;
                array.add(getLine[0] + "," + getLine[1] + "," + getLine[2] + "," + getLine[3] + "," + getLine[4] + "," +
                          getLine[5] + "," + getLine[6] + "," + getLine[7] + "," + getLine[8] + "," + getLine[9] + "," + 
                          "Paid" + "," + getLine[11]);
            } else {
                array.add(line2);
            }
        }
        br.close();
    } catch (IOException e) {
        System.out.println("Error: File Reading: " + e.getMessage());
        return false;
    }

    // Write the updated data back to the file
    writeFile(array, billFilename);
    updateCustomerFile(custID, RUC, PHUC);
    
    return true;
}

public boolean validateBillingMonth(String billingMonth) {
    String[] validMonths = {
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    for (String month : validMonths) {
        if (month.equalsIgnoreCase(billingMonth)) {
            return true;
        }
    }
    return false;
}

    public void updateCustomerFile(String custID, String RUC, String PHUC) {
        ArrayList<String> array = new ArrayList<>();
        try {
            FileReader fr = new FileReader(custFilename);
            BufferedReader br = new BufferedReader(fr);

            String line2;

            while ((line2 = br.readLine()) != null) {
                String[] getLine = line2.split(",");
                if (getLine[0].equals(custID)) {
                    getLine[8] = RUC;
                    getLine[9] = PHUC;
                    array.add(getLine[0] + "," + getLine[1] + "," + getLine[2] + "," + getLine[3] + "," + getLine[4] + "," + getLine[5] + "," + getLine[6] + "," + getLine[7] + "," + getLine[8] + "," + getLine[9]);
                } else {
                    array.add(line2);
                }
            }
            fr.close();
            br.close();
        }
        catch(IOException e)
        {
            System.out.println("Error: File Reading: " + e.getMessage());
        }

        writeFile(array,custFilename);
    }

    public void writeFile(ArrayList<String> array, String filename) {
        try {
            FileWriter fw = new FileWriter(filename);
            BufferedWriter bw = new BufferedWriter(fw);

            for (int i = 0; i < array.size(); i++) {
                bw.write(array.get(i));
                bw.newLine();
            }
            bw.close();
        }
        catch (IOException e)
        {
            System.out.println("Error: File Writing");
        }
    }

    public String[] getCustomerData(String custID) {
    String line;
    String[] data;

    try (BufferedReader br = new BufferedReader(new FileReader("CustomerInfo.txt"))) {
        while ((line = br.readLine()) != null) {
            data = line.split(","); // Split the line by comma to get customer fields
            if (data[0].equals(custID)) { // Check if the customer ID matches
                return data; // Return the found customer data
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    // Return null if the customer ID was not found
    return null;
}

private String[] getTaxData(String customerType, String meterType) {
    // Load your tax data and return the appropriate row based on customerType and meterType
    // Ensure the data is correctly formatted in your TariffTaxInfo file
    // Here’s a simple check:
    if (customerType.equals("D") && meterType.equals("S")) {
        return new String[]{"5", "0", "0", "17", "150"}; // Example for domestic single-phase
    } else if (customerType.equals("C") && meterType.equals("S")) {
        return new String[]{"15", "0", "0", "20", "250"}; // Example for commercial single-phase
    } else if (customerType.equals("D") && meterType.equals("T")) {
        return new String[]{"8", "12", "0", "17", "150"}; // Example for domestic three-phase
    } else if (customerType.equals("C") && meterType.equals("T")) {
        return new String[]{"18", "25", "0", "20", "250"}; // Example for commercial three-phase
    }
    return null; // Handle this case in your main method to prevent further errors
}

    public boolean validateCustomerID(String id)  {
        try {
            FileReader fr = new FileReader(custFilename);
            BufferedReader br = new BufferedReader(fr);

            String line;
            while ((line = br.readLine()) != null) {
                String[] index = line.split(",");
                if (index[0].equals(id)) {
                    arrayList = index;
                    return true;
                }
            }
        }
        catch(IOException e)
        {
            System.out.println("Error File Reading: " + e.getMessage());
        }

        return false;
    }

    public boolean validateCustomerIDfromBillFile(String id,String month,String date)  {
        try {
            FileReader fr = new FileReader(billFilename);
            BufferedReader br = new BufferedReader(fr);

            String line;
            while ((line = br.readLine()) != null) {
                String[] index = line.split(",");
                if (index[0].equals(id) && index[1].equals(month) && index[4].equals(date)) {
                    billList = index;
                    return true;
                }
            }
        }
        catch(IOException e)
        {
            System.out.println("Error File Reading: " + e.getMessage());
        }

        return false;
    }

    public boolean viewBill()  {
        Scanner scanner = new Scanner(System.in);
        String custID;
        String billingMonth;
        while(true)
        {
            System.out.print("Enter Billing Month: ");
            billingMonth = scanner.nextLine();

            if(billingMonth.equals("00"))
            {
                return false;
            }
            if(billingMonth.equals("Jan") || billingMonth.equals("Feb") || billingMonth.equals("Mar") || billingMonth.equals("April") || billingMonth.equals("May") || billingMonth.equals("June") || billingMonth.equals("July") || billingMonth.equals("August") || billingMonth.equals("Sept") || billingMonth.equals("Oct") || billingMonth.equals("Nov") || billingMonth.equals("Dec"))
            {
                break;
            }
            System.out.println("Incorrect Month: Try Again");
        }

        String entryDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        while(true)
        {
            System.out.print("Enter Bill Entry Date (dd/MM/yyyy): ");
            entryDate = scanner.nextLine();
            if(entryDate.equals("00"))
            {
                return false;
            }
            try{
                LocalDate date = LocalDate.parse(entryDate,formatter);
                break;
            }catch(DateTimeParseException e)
            {
                System.out.println("Invalid Date : Try Again");
            }
        }

        while(true)
        {
            System.out.print("Enter Customer ID: ");
            custID = scanner.nextLine();

            if(custID.equals("00"))
            {
                return false;
            }
            if(validateCustomerIDfromBillFile(custID,billingMonth,entryDate))
            {
                break;
            }
            System.out.println("No Such Bill Found: Try Again");
        }

        System.out.println("\n--------------------------------------------------\n\t\t\tWelcome to LESCO Management system\n--------------------------------------------------\n\n" +
                "Customer ID:                 "+billList[0]+"\n"+
                "Billing Month:               "+billList[1]+"\n"+
                "Current Meter Reading:       "+billList[2]+" units\n"+
                "Peak Meter Reading:          "+billList[3]+" units\n"+
                "Reading Entry Date:          "+billList[4]+"\n"+
                "Cost of Electricity:         Rs. "+billList[5]+"\n"+
                "Sales Tax Amount:            Rs. "+billList[6]+"\n"+
                "Fixed Charges:               Rs. "+billList[7]+"\n"+
                "Total Billing Amount:        Rs. "+billList[8]+"\n"+
                "Due Date:                    "+billList[9]+"\n"+
                "Bill Paid Status:            "+billList[10]+"\n"+
                "Bill Payment Date:           "+billList[11]+"\n");
        return true;
    }

    public void viewReport()  {
        float sum_paid=0;
        float sum_unpaid=0;
        String line;
        String[] data;

        try(BufferedReader br = new BufferedReader(new FileReader(billFilename)))
        {
            while ((line=br.readLine())!=null)
            {
                data=line.split(",");
                if(data[10].equals("Paid"))
                {
                    sum_paid = sum_paid + Float.parseFloat(data[8]);
                }
                else if(data[10].equals("UnPaid"))
                {
                    sum_unpaid = sum_unpaid + Float.parseFloat(data[8]);
                }
            }
        }catch (IOException e){
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n------------------------------\n\tStatus Report\n------------------------------\nAmount Paid So Far: " + sum_paid + "\nAmount Unpaid So Far: " + sum_unpaid);
    }
    
    public String[][] readBillsFromFile() {
        List<String[]> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(billFilename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines.toArray(new String[0][]);
    } // For reading Bill in tables 

    public void writeBillsToFile(String[][] data) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(billFilename))) {
            for (String[] row : data) {
                writer.println(String.join(",", row));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }// For writing directly to tables
}
