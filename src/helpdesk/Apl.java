package helpdesk;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * APL for the helpdesk
 * You don't have to make any adjustments to this class
 *
 * @author Evert Duipmans
 */
public class Apl {
    public static void main(String[] args) {
        Helpdesk helpdesk = new Helpdesk();

        // Read all the data
        int numObjects = 0;
        String path = "src/uitwerkingen/week7/helpdesk/";
        try {
            numObjects = helpdesk.importData(path + "i-desk.txt");
        } catch (HelpdeskException he) {
            System.err.println(he.getMessage());
        }
        System.out.println("Read: " + numObjects + " objects.");

        boolean closing = false;
        while (!closing) {
            System.out.println("");
            System.out.println("-----------");
            System.out.println("I-Desk");
            System.out.println("-----------");

            // User should first login
            if (!helpdesk.isLoggedOn()) {
                System.out.print("Please give your username: ");
                String inputUser = askString();
                System.out.print("Please give your password: ");
                String inputPassword = askString();
                if (!helpdesk.login(inputUser, inputPassword)) {
                    System.out.println("ERROR: Cannot login user. Invalid credentials");
                }
            } else {
                // Show menu
                System.out.println("1. Add ticket");
                System.out.println("2. Resolve ticket");
                System.out.println("3. Show my tickets");
                System.out.println("4. Show open tickets");
                System.out.println("5. Show all tickets");
                System.out.println("6. Show users");
                System.out.println("7. Export data");
                System.out.println("8. Logout");
                System.out.println("9. Exit");
                System.out.println("");

                // Ask the user
                System.out.print("Choice: ");
                int choice = askNumber();
                System.out.println("-----------");

                // Process choice
                if (choice == 1) {
                    System.out.println("Add ticket:");
                    System.out.println("Please enter the type (hardware or software):");
                    String type = askString();

                    while (!"hardware".equalsIgnoreCase(type) && !"software".equalsIgnoreCase(type)) {
                        type = askString();
                    }

                    System.out.println("Please enter the description:");
                    String description = askString();

                    if ("hardware".equalsIgnoreCase(type)) {
                        System.out.println("Please enter the machineid:");
                        String machineId = askString();
                        int ticketNumber = helpdesk.addHardwareTicket(description, machineId);
                        System.out.println("New ticket is created with number: " + ticketNumber);
                    } else {
                        System.out.println("Please enter the software name:");
                        String softwareName = askString();
                        int ticketNumber = helpdesk.addSoftwareTicket(description, softwareName);
                        System.out.println("New ticket is created with number: " + ticketNumber);
                    }
                } else if (choice == 2) {
                    System.out.println("Resolve ticket:");
                    System.out.println("Please enter the ticket number:");
                    int number = askNumber();
                    System.out.println("Please enter the response:");
                    String response = askString();

                    try {
                        helpdesk.resolveTicket(number, response);
                    } catch (HelpdeskException hde) {
                        System.err.println(hde.getMessage());
                    }
                } else if (choice == 3) {
                    System.out.println("Show my tickets:");
                    helpdesk.printMyTickets();
                } else if (choice == 4) {
                    System.out.println("Show open tickets:");
                    try {
                        helpdesk.printOpenTickets();
                    } catch (HelpdeskException e) {
                        System.err.println(e.getMessage());
                    }
                } else if (choice == 5) {
                    System.out.println("Show all tickets:");
                    try {
                        helpdesk.printAllTickets();
                    } catch (HelpdeskException e) {
                        System.err.println(e.getMessage());
                    }
                } else if (choice == 6) {
                    System.out.println("Show all users:");
                    try {
                        helpdesk.printUsers();
                    } catch (HelpdeskException e) {
                        System.err.println(e.getMessage());
                    }
                } else if (choice == 7) {
                    System.out.println("Export data:");
                    System.out.println("Please enter the filename:");
                    String filename = path + askString();
                    try {
                        helpdesk.exportData(filename);
                    } catch (FileNotFoundException e) {
                        System.err.println(e.getMessage());
                    }
                } else if (choice == 8) {
                    helpdesk.logout();
                } else if (choice == 9) {
                    closing = true;
                } else {
                    System.out.println("Invalid choice.");
                }
            }
        }
    }

    /**
     * Ask the user to enter a number. The system will ask the user again if incorrect input is provided by the user
     *
     * @return The entered number
     */
    private static int askNumber() {
        int enteredNumber = -1;
        boolean askAgain = true;
        while (askAgain) {
            Scanner input = new Scanner(System.in);
            try {
                enteredNumber = Integer.parseInt(input.nextLine());
                askAgain = false;
            } catch (NumberFormatException e) {
                System.out.print("Error: Please enter a number.");
                askAgain = true;
            }
        }
        return enteredNumber;
    }

    /**
     * Aks the user for a string. The system will ask again if invalid input is provided by the user
     *
     * @return The entered String
     */
    public static String askString() {
        String enteredString = "";
        boolean askAgain = true;
        while (askAgain) {
            Scanner input = new Scanner(System.in);
            enteredString = input.nextLine();
            if (enteredString.length() != 0) {
                askAgain = false;
            } else {
                System.out.print("Error: Please enter at least one character.");
            }
        }
        return enteredString;
    }
}