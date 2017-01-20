package helpdesk;

import helpdesk.data.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Helpdesk {
    /**
     * Contains the user that is currently logged in.
     */
    private User currentUser;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Ticket> tickets = new ArrayList<>();

    /**
     * Reads in a file and creates users and tickets in the system
     *
     * @param filename Filename
     * @return Number of created objects
     * @throws HelpdeskException An exception is thrown when the file cannot be read, or when a line is incomplete
     */
    public int importData(String filename) throws HelpdeskException {
        int lineNr = 0;
        int itemCount = 0;
        try (Scanner in = new Scanner(new File(filename))) {
            while (in.hasNextLine()) {
                lineNr++;
                String line = in.nextLine();
                if (line.startsWith("User") || line.startsWith("Manager")) {
                    users.add(importUser(line));
                    itemCount++;
                } else if (line.startsWith("Ticket")) {
                    tickets.add(importTicket(line));
                    itemCount++;
                }
            }
        } catch (FileNotFoundException e) {
            throw new HelpdeskException(e.getMessage());
        } catch (HelpdeskException hde) {
            throw new HelpdeskException("Line " + lineNr + ": " + hde.getMessage());
        }
        return itemCount;
    }

    /**
     * Processes a CSV line from an input file and tries to create a user object
     *
     * @param line Line of text
     * @return User object
     * @throws HelpdeskException If the line is incorrect an exception is thrown
     */
    private User importUser(String line) throws HelpdeskException {
        User user = null;
        String[] items = line.split(";");
        if (items[0].equals("User")) {
            if (items.length == 3) {
                user = new User(items[1], items[2]);
            } else {
                throw new HelpdeskException("Incorrect number of items for a user");
            }
        } else { // items[0].equals("Manager")
            if (items.length == 5) {
                user = new ServiceManager(items[1], items[2], items[3], items[4]);
            } else {
                throw new HelpdeskException("Incorrect number of items for a user");
            }
        }
        return user;
    }

    /**
     * Processes a CSV line from an input file and tries to create a ticket object
     *
     * @param line Line of text
     * @return User object
     * @throws HelpdeskException If the line is incorrect an exception is thrown
     */
    private Ticket importTicket(String line) throws HelpdeskException {
        Ticket ticket = null;
        try {
            String[] items = line.split(";");
            if (items.length >= 6) {
                int ticketNr = 0;
                try{
                    ticketNr = Integer.parseInt(items[1]);
                } catch (NumberFormatException nfe){
                    throw new HelpdeskException("Invalid ticket number");
                }
                String ticketType = items[2];
                User owner = findUserByUsername(items[3]);
                String specialItem = items[4];
                String description = items[5];
                if(owner == null){
                    throw new HelpdeskException("Unknown user");
                }
                if (ticketType.equalsIgnoreCase("Software")) {
                    ticket = new SoftwareTicket(ticketNr, description, owner, specialItem);
                } else if (ticketType.equalsIgnoreCase("Hardware")) {
                    ticket = new HardwareTicket(ticketNr, description, owner, specialItem);
                } else {
                    throw new HelpdeskException("Unknow ticket type");
                }
                if(items.length == 8){
                    User handler = findUserByUsername(items[6]);
                    String solution = items[7];
                    if(handler instanceof ServiceManager){
                       ticket.setSolution((ServiceManager) handler,solution);
                    }
                } else if (items.length > 6){
                    throw new HelpdeskException("Too many items for ticket");
                }
            } else {
                throw new HelpdeskException("Too few items for a ticket");
            }
        } catch (NumberFormatException nfe) {
            throw new HelpdeskException("Invalid ticket number");
        }
        return ticket;
    }

    /**
     * Helper method for finding a user using a username
     *
     * @param username Username of the user
     * @return Returns the User object that corresponds to the current username, otherwise null is returned
     */
    private User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getName().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Helper method for finding tickets using a ticket number
     * @param number Ticket number
     * @return Ticket object that has the indicated number, null if no such Ticket exists
     */
    private Ticket findTicketByTicketnumber(int number){
        for (Ticket ticket: tickets){
            if(ticket.hasNumber(number)){
                return ticket;
            }
        }
        return null;
    }

    /**
     * Export the data in the system (users and tickets) to the CSV format (semicolon separated!!!)
     * IMPORTANT: EXPORT FIRST THE USERS AND THEN THE TICKETS, OTHERWISE THE FILE CANNOT BE IMPORTED ANYMORE!!!
     *
     * @param filename Filename of the export file
     */
    public void exportData(String filename) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(filename);
        for(User user: users){
            out.println(user.toCSV());
        }

        for(Ticket ticket: tickets){
            out.println(ticket.toCSV());
        }
        out.close();
    }

    /**
     * Login a user/employee
     *
     * @param username Username of the user/employee
     * @param password Password of the user/employee
     * @return True, if the user is logged on successfully
     */
    public boolean login(String username, String password) {
        User user = findUserByUsername(username);
        if(user == null){
            return false;
        }
        currentUser = user;
        return user.hasPassword(password);
    }

    /**
     * Logout a user
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * Returns true if the user is logged on
     *
     * @return True, if the user is logged on
     */
    public boolean isLoggedOn() {
        return currentUser != null;
    }

    /**
     * Returns true if the user that is currently logged in is a manager
     *
     * @return True, if the user is a manager
     */
    public boolean isManager() {
        if(!isLoggedOn()){
            return false;
        }
        return currentUser instanceof ServiceManager;
    }

    /**
     * Add a hardwareticket to the helpdesk
     *
     * @param description description of the problem
     * @param machineCode Code of the computer
     */
    public int addHardwareTicket(String description, String machineCode) {
        if(!isLoggedOn()){
            return -1;
        }
        int ticketNr = tickets.size() + 1;
        tickets.add(new HardwareTicket(ticketNr,description, currentUser, machineCode ));
        return ticketNr;
    }

    /**
     * Add a softwareticket to the helpdesk
     *
     * @param description  description of the problem
     * @param softwareName Name of the piece of software
     */
    public int addSoftwareTicket(String description, String softwareName) {
        if(!isLoggedOn()){
            return -1;
        }
        int ticketNr = tickets.size() + 1;
        tickets.add(new SoftwareTicket(ticketNr,description, currentUser, softwareName ));
        return ticketNr;
    }

    /**
     * Resolve a ticket
     *
     * @param ticketNumber Number of the ticket
     * @param response     Response text
     * @throws HelpdeskException When the ticket is not found, the current user is no manager or the ticket is already resolved
     */
    public void resolveTicket(int ticketNumber, String response) throws HelpdeskException {
        if(!isLoggedOn()){
            throw new HelpdeskException("Nobody logged in");
        }
        if (!(currentUser instanceof ServiceManager)){
            throw new HelpdeskException("User " + currentUser.getName() + " is not a service manager");
        }
        Ticket ticket = findTicketByTicketnumber(ticketNumber);
        if(ticket == null){
            throw new HelpdeskException("Invalid ticket number");
        }

        if(ticket.isResolved()){
            throw new HelpdeskException("Ticket already resolved");
        }
        ticket.setSolution((ServiceManager) currentUser, response);
    }

    /**
     * Print all my tickets
     */
    public void printMyTickets() {
        System.out.println("My tickets: ");
        for(Ticket tk: tickets){
            if(tk.getOwner().equals(currentUser)){
                System.out.println(tk);
            }
        }
    }

    /**
     * Print all the open tickets in the system (only available for managers of the helpdesk)
     *
     * @throws HelpdeskException Exception is thrown when the user that is logged on has not enough privileges
     */
    public void printOpenTickets() throws HelpdeskException {
        if(!(currentUser instanceof ServiceManager)){
            throw new HelpdeskException("Operation not allowed for users.");
        }
        for(Ticket tk: tickets){
            if(!tk.isResolved()){
                System.out.println(tk);
            }
        }
    }

    /**
     * Print all the tickets in the system (only available for managers of the helpdesk)
     *
     * @throws HelpdeskException Exception is thrown when the user that is logged on has not enough privileges
     */
    public void printAllTickets() throws HelpdeskException {
        if(!(currentUser instanceof ServiceManager)){
            throw new HelpdeskException("Operation not allowed for users.");
        }
        for(Ticket ticket:tickets){
            System.out.println(ticket + "\n");
        }
    }

    /**
     * Print all users in the system (only available for employees of the helpdesk)
     *
     * @throws HelpdeskException Exception is thrown when the user that is logged is not employee of the helpdesk
     */
    public void printUsers() throws HelpdeskException {
        if(!(currentUser instanceof ServiceManager)){
            throw new HelpdeskException("Operation not allowed for users.");
        }
        for(User user: users){
            System.out.println(user);
        }
    }
}