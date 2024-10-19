package lesco;

import java.io.*;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Customer_Management
{
    private String custFilename = "CustomerInfo.txt";
    private String billFilename = "BillingInfo.txt";
    private String tariffFilename = "TariffTaxInfo.txt";
    private String[] custInfo;
    private String[] billInfo = new String[]{"Not Found", "Not Found", "Not Found", "Not Found", "Not Found", "Not Found", "Not Found", "Not Found", "Not Found", "Not Found", "Not Found", "Not Found"};
    private String[] tariffInfo;
    private String[] nadraInfo;
    
 public void CustInMenu(){
        Scanner scanner = new Scanner(System.in);
        String id;
        String cnic;
        String month;
        String year;
        int intYear=0;

        Customer_Management c = new Customer_Management();

        while(true) {
            System.out.println("\nGo Back -> -1\n");
            System.out.print("Enter ID: ");
            id = scanner.nextLine();

            if(id.equals("-1"))
            {
                break;
            }

            System.out.print("Enter CNIC: ");
            cnic = scanner.nextLine();

            if(cnic.equals("-1"))
            {
                break;
            }

            while(true)
            {
                System.out.print("Enter Bill Month: ");
                month = scanner.nextLine();

                if(month.equals("-1"))
                {
                    break;
                }
                if(month.equals("Jan") || month.equals("Feb") || month.equals("Mar") || month.equals("April") || month.equals("May") || month.equals("June") || month.equals("July") || month.equals("August") || month.equals("Sept") || month.equals("Oct") || month.equals("Nov") || month.equals("Dec"))
                {
                    break;
                }
                System.out.println("Incorrect Month: Try Again");
            }
            if(month.equals("-1"))
            {
                break;
            }

            while(true) {
                System.out.print("Enter Year: ");
                year = scanner.nextLine();

                if (year.equals("-1")) {
                    break;
                }
                if(year.matches("\\d{4}") && Integer.parseInt(year) > 0)
                {
                    intYear = Integer.parseInt(year);
                    break;
                }
                System.out.println("Invalid Year Try Again");
            }
            if (year.equals("-1")) {
                break;
            }

            if (validateCustomer(id, cnic,month,intYear)) {
                fetchBillData(id, cnic, month, intYear);
            } else {
                System.out.print("\n\nIncorrect ID or CNIC");
            }
        }
    }

 public String[][] fetchBillData(String id, String cnic, String month, int year) {
    String[] custInfo = null;
    String[] billInfo = null;
    String[] tariffInfo = null;

    // Reading customer information
    try (BufferedReader br = new BufferedReader(new FileReader(custFilename))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] rowData = line.split(",");
            if (rowData[0].trim().equals(id) && rowData[1].trim().equals(cnic)) {
                custInfo = rowData;
                System.out.println("Customer Info Found: " + Arrays.toString(custInfo));
                break;
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    // Reading bill information
    try (BufferedReader br = new BufferedReader(new FileReader(billFilename))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] rowData = line.split(",");
            System.out.println("Checking Bill Info: " + Arrays.toString(rowData));

            String billId = rowData[0].trim();
            String billMonth = rowData[1].trim();
            String billDate = rowData[4].trim();  // Assuming the year is in the bill date (20/09/2024)
            int billYear = Integer.parseInt(billDate.split("/")[2]);

            if (billId.equals(id) && billMonth.equalsIgnoreCase(month) && billYear == year) {
                billInfo = rowData;
                System.out.println("Bill Info Found: " + Arrays.toString(billInfo));
                break;
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    // Reading tariff information
    try (BufferedReader br = new BufferedReader(new FileReader(tariffFilename))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] rowData = line.split(",");
            // Assuming the first column is the tariff type
            if (rowData[0].trim().equals("1Phase") || rowData[0].trim().equals("3Phase")) {
                tariffInfo = rowData;
                System.out.println("Tariff Info Found: " + Arrays.toString(tariffInfo));
                break;
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    // If no customer, bill, or tariff info is found, return an empty array
    if (custInfo == null || billInfo == null || tariffInfo == null) {
        System.out.println("Customer, Bill, or Tariff Info Not Found.");
        return new String[][]{{"Customer, Bill, or Tariff Info Not Found"}};
    }

    // Prepare and return the bill data including tariff details
    String[][] billData = {
        {"Customer ID", custInfo[0]},
        {"Customer Name", custInfo[2]},
        {"CNIC Number", custInfo[1]},
        {"Address", custInfo[3]},
        {"Phone Number", custInfo[4]},
        {"Customer Type", custInfo[5]}, // Assuming the customer type is in the sixth column
        {"Meter Type", tariffInfo[0]}, // Assuming the meter type is in the first column of tariff info
        {"Regular Unit Price", "Rs. " + (tariffInfo[1].isEmpty() ? "" : tariffInfo[1])},
        {"Peak Hour Unit Price", "Rs. " + (tariffInfo[2].isEmpty() ? "" : tariffInfo[2])},
        {"Percentage Of Tax", "Rs. " + (tariffInfo[3].isEmpty() ? "" : tariffInfo[3])},
        {"Bill Month", billInfo[1]},
        {"Current Meter Reading Regular", billInfo[2] + " units"},
        {"Current Meter Reading Peak", billInfo[3] + " units"},
        {"Cost of Electricity", "Rs. " + billInfo[5]},
        {"Sales Tax Amount", "Rs. " + billInfo[6]},
        {"Fixed Charges", "Rs. " + (tariffInfo[4].isEmpty() ? "" : tariffInfo[4])},
        {"Total Amount Due", "Rs. " + billInfo[8]},
        {"Due Date", billInfo[9]},
        {"Payment Status", billInfo[10]}
    };

    // Print the bill data to the console in a structured format
    System.out.printf("%-20s: %s%n", "Customer ID", billData[0][1]);
    System.out.printf("%-20s: %s%n", "Customer Name", billData[1][1]);
    System.out.printf("%-20s: %s%n", "CNIC Number", billData[2][1]);
    System.out.printf("%-20s: %s%n", "Address", billData[3][1]);
    System.out.printf("%-20s: %s%n", "Phone Number", billData[4][1]);
    System.out.printf("%-20s: %s%n", "Customer Type", billData[5][1]);
    System.out.printf("%-20s: %s%n", "Meter Type", billData[6][1]);
    System.out.println("----------------------------------------");
    System.out.printf("%-20s: %s%n", "Regular Unit Price", billData[7][1]);
    System.out.printf("%-20s: %s%n", "Peak Hour Unit Price", billData[8][1]);
    System.out.printf("%-20s: %s%n", "Percentage Of Tax", billData[9][1]);
    System.out.println("----------------------------------------");
    System.out.printf("%-20s: %s%n", "Bill Month", billData[10][1]);
    System.out.printf("%-20s: %s%n", "Current Meter Reading Regular", billData[11][1]);
    System.out.printf("%-20s: %s%n", "Current Meter Reading Peak", billData[12][1]);
    System.out.println("----------------------------------------");
    System.out.printf("%-20s: %s%n", "Cost of Electricity", billData[13][1]);
    System.out.printf("%-20s: %s%n", "Sales Tax Amount", billData[14][1]);
    System.out.printf("%-20s: %s%n", "Fixed Charges", billData[15][1]);
    System.out.println("----------------------------------------");
    System.out.printf("%-20s: %s%n", "Total Amount Due", billData[16][1]);
    System.out.printf("%-20s: %s%n", "Due Date", billData[17][1]);
    System.out.printf("%-20s: %s%n", "Payment Status", billData[18][1]);

    return billData;
}  // Fetch Bill for Viewing Bill in Customer Menu
 
public boolean addCustomer(String cnic, String name, String address, String phone, String custType, String meterType) {
    String RUC = "0";
    String PHUC;

    // CNIC Validation (same as your current code)
    int count = cnic_count(cnic);
    if (count >= 3) {
        System.out.println("Not Allowed! Maximum 3 meters allowed per CNIC");
        return false;
    }
    if (cnic.length() != 13 || !isDigits(cnic) || !searchNadraFile(cnic)) {
        System.out.println("Invalid CNIC : Try Again");
        return false;
    }

    // Name validation
    if (!isAlphabets(name)) {
        System.out.println("Incorrect Name Input : Try Again");
        return false;
    }

    // Phone validation
    if (phone.length() != 11 || !isDigits(phone)) {
        System.out.println("Incorrect Phone Number : Try Again");
        return false;
    }

    // Customer type validation
    if (!custType.equalsIgnoreCase("C") && !custType.equalsIgnoreCase("D")) {
        System.out.println("Incorrect Type : Try Again");
        return false;
    }

    // Meter type validation
    if (meterType.equalsIgnoreCase("S")) {
        PHUC = "not_supported";
    } else if (meterType.equalsIgnoreCase("T")) {
        PHUC = "0";
    } else {
        System.out.println("Incorrect Meter Type : Try Again");
        return false;
    }

    // Generating the ID
    String id;
    while (true) {
        Random r = new Random();
        id = String.valueOf(1000 + r.nextInt(9000));
        if (isUnique(id, 0)) {
            break;
        }
    }

    // Getting the current date
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String date = currentDate.format(formatter);

    // Prepare the data to save
    String data = id + "," + cnic + "," + name + "," + address + "," + phone + "," + custType + "," + meterType + "," + date + "," + RUC + "," + PHUC;

    // Write the data to the file
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(custFilename, true))) {
        bw.write(data);
        bw.newLine();
    } catch (IOException e) {
        System.out.println("Error while writing to File: " + e.getMessage());
        return false;
    }
    return true;
}

public boolean validateCustomer(String id, String cnic, String month, int year) {
    boolean valid = false;
    try (BufferedReader br = new BufferedReader(new FileReader(custFilename))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] idCnic = line.split(",");
            if (idCnic[0].equals(id) && idCnic[1].equals(cnic)) {
                custInfo = idCnic;
                valid = true;
                break;
            }
        }
    } catch (IOException e) {
        System.out.println("Error: File Reading: " + e.getMessage());
    }

    if (valid) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try (BufferedReader br = new BufferedReader(new FileReader(billFilename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(id) && data[1].equals(month)) {
                    LocalDate date = LocalDate.parse(data[4], formatter);
                    int fileYear = date.getYear();
                    if (fileYear == year) {
                        billInfo = data;
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    return false;
}

public boolean updateCNIC(String id, String cnic, String newDate) {
        if (!isDigits(id) || !isDigits(cnic) || !searchNadraFile(cnic) || !validateCustomer(id, cnic, "Jan", 2024)) {
            System.out.println("Invalid ID or CNIC");
            return false;
        }

        LocalDate issueDate = getIssueDate(cnic);
        LocalDate parsedNewDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            parsedNewDate = LocalDate.parse(newDate, formatter);
            if (parsedNewDate.isBefore(issueDate)) {
                System.out.println("Error: New Expiry Date cannot be before the Issue Date: " + nadraInfo[1]);
                return false;
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid Date: " + e.getMessage());
            return false;
        }

        return updateNadraFile(cnic, newDate);
    }

private LocalDate getIssueDate(String cnic) {
        // Assume nadraInfo is set when searching the NADRA file.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(nadraInfo[1], formatter);
    }

private boolean updateNadraFile(String cnic, String newDate) {
        ArrayList<String> lines = new ArrayList<>();
        String line;
        String[] data;

        try (BufferedReader br = new BufferedReader(new FileReader("NADRADBfile.txt"))) {
            while ((line = br.readLine()) != null) {
                data = line.split(",");
                if (data[0].equals(cnic)) {
                    lines.add(data[0] + "," + data[1] + "," + newDate);
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return false;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("NADRADBfile.txt"))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
            return false;
        }

        return true;
    }

public void viewExpireCnic() {
        LocalDate today = LocalDate.now();
        LocalDate expiry;
        long daysInBetween;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        ArrayList<String> list = new ArrayList<>();

        String line;
        String[] data;
        try(BufferedReader br = new BufferedReader(new FileReader("NADRADBfile.txt"))){
            while((line=br.readLine())!=null){
                data = line.split(",");
                expiry = LocalDate.parse(data[2],formatter);

                daysInBetween = ChronoUnit.DAYS.between(today,expiry);
                if(daysInBetween<=30 && daysInBetween>0)
                {
                    list.add(data[0]+"          "+data[2]);
                }
            }
        }catch(IOException e){
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\nTotal CNIC's Expiring in 30 Days: " + list.size() + "\n");
        System.out.println("    CNIC\t\t\t   Expiry Date");
        for(int i=0;i<list.size();i++)
        {
            System.out.println(list.get(i));
        }
    }

public String[][] readCustomersFromFile() {
    List<String[]> customerList = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(custFilename))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] customerData = line.split(","); // Assuming CSV format
            customerList.add(customerData);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    
    // Convert List<String[]> to String[][]
    String[][] customerArray = new String[customerList.size()][];
    for (int i = 0; i < customerList.size(); i++) {
        customerArray[i] = customerList.get(i);
    }
    return customerArray;
}

public void writeCustomersToFile(String[][] customerData) {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(custFilename))) {
        for (String[] customer : customerData) {
            for (int j = 0; j < customer.length; j++) {
                bw.write(customer[j]);
                if (j < customer.length - 1) {
                    bw.write(","); // Separate columns with commas
                }
            }
            bw.newLine(); // New line for the next customer
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public int cnic_count(String cnic) {
    String line = "";
    int count = 0;
    String[] data;

    try (BufferedReader br = new BufferedReader(new FileReader(custFilename))) {
        while ((line = br.readLine()) != null) {
            data = line.split(",");

            // Ensure that data has at least two elements (to safely access data[1])
            if (data.length > 1 && data[1].equals(cnic)) {
                count++;
            }
        }
    } catch (IOException e) {
        System.out.println("Error while reading file: " + e.getMessage());
    }

    return count;
}

public boolean searchNadraFile(String cnic)    {
        String line;
        String[] data;
        try(BufferedReader br = new BufferedReader(new FileReader("NADRADBfile.txt")))
        {
            while((line= br.readLine())!=null)
            {
                data = line.split(",");
                if(data[0].equals(cnic))
                {
                    nadraInfo=data;
                    return true;
                }
            }
        }catch (IOException e) {
            System.out.println("Error while reading : " + e.getMessage());
        }
        return false;
    }

public void getTaxData(String custType, String phase)    {
        try(BufferedReader br = new BufferedReader(new FileReader(tariffFilename))){

            String line1 = br.readLine();
            String line2 = br.readLine();
            String line3 = br.readLine();
            String line4 = br.readLine();

            if((custType.equals("D") || custType.equals("d")) && (phase.equals("s") || phase.equals("S")))
            {
                tariffInfo = line1.split(",");
            }
            else if((custType.equals("c") || custType.equals("C")) && (phase.equals("s") || phase.equals("S")))
            {
                tariffInfo = line2.split(",");
            }
            else if((custType.equals("d") || custType.equals("D")) && (phase.equals("t") || phase.equals("T")))
            {
                tariffInfo = line3.split(",");
            }
            else if((custType.equals("c") || custType.equals("C")) && (phase.equals("t") || phase.equals("T")))
            {
                tariffInfo = line4.split(",");
            }
        }catch (IOException e)
        {
            System.out.println("Error Reading Tax File");
        }
    }
   
public boolean isAlphabets(String str) {
        for(int i=0; i<str.length();i++)
        {
            if(!Character.isLetter(str.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }

public boolean isDigits(String str)   {
        for(int i=0; i<str.length();i++)
        {
            if(!Character.isDigit(str.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }
  
public boolean isUnique(String str, int index) {
        try {
            FileReader fr = new FileReader(custFilename);
            BufferedReader br = new BufferedReader(fr);
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[index].equals(str)) {
                    return false;
                }
            }
            fr.close();
            br.close();
        }
        catch(IOException e)
        {
            System.out.println("Reading Error: " + e.getMessage());
        }
        return true;
    }
}
