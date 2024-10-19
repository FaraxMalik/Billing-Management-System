package lesco;

import java.io.*;
import java.util.*;

public class Employees_Management
{
    String filename = "EmployeesData.txt";

    public boolean validateEmployee(String username, String pass)
    {
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);

            String line;

            while ((line = br.readLine()) != null) {
                String[] namePass = line.split(",");
                if(namePass[0].equals(username) && namePass[1].equals(pass))
                {
                    return true;
                }
            }
            fr.close();
            br.close();
        }
        catch(IOException e)
        {
            System.out.println("Error: File Reading: " + e.getMessage());
        }

        return false;
    }

    public String updateMenu(String username, String oldPass)
    {
        Scanner input = new Scanner(System.in);
        String inName;
        String inPass;
        String newPass;

        System.out.print("Enter current Username: ");
        inName = input.nextLine();

        System.out.print("Enter current Password: ");
        inPass = input.nextLine();

        System.out.print("Enter new Password: ");
        newPass = input.nextLine();

        if(newPass.equals(inPass))
        {
            System.out.println("\nNew Password cannot be same as Old Password");
        }
        else if(inName.equals(username) && oldPass.equals(inPass))
        {
            updatePass(username,newPass);
            System.out.println("\nPassword Updated Successfully");
            return newPass;
        }
        else
        {
            System.out.println("\nUsername or Password Do not Match");
        }
        return "no change";
    }

public boolean updatePass(String username, String newPass) {
    ArrayList<String> data = new ArrayList<>();
    boolean userFound = false;

    // Use try-with-resources for better resource management
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
        String line;

        while ((line = br.readLine()) != null) {
            String[] namePass = line.split(",");
            // Check to prevent index out of bounds
            if (namePass.length < 2) {
                continue; // Skip malformed lines
            }
            if (namePass[0].equals(username)) {
                namePass[1] = newPass; // Update password
                userFound = true;
            }
            data.add(namePass[0] + "," + namePass[1]); // Add updated or original record
        }
    } catch (IOException e) {
        System.out.println("Error: File Reading: " + e.getMessage());
        return false;
    }

    if (!userFound) return false; // User was not found

    // Writing back to the file
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
        for (String record : data) {
            bw.write(record);
            bw.newLine();
        }
    } catch (IOException e) {
        System.out.println("Error: File Writing: " + e.getMessage());
        return false;
    }

    return true; 
}

}
