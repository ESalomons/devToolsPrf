import helpdesk.data.HardwareTicket;
import helpdesk.data.Ticket;
import helpdesk.data.User;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Etto Salomons
 *         created on 20/01/17.
 */
public class TicketTester {

    @Test
    public void createTicket() {
        User user = new User("Henk", ",,");
        Ticket ticket = new HardwareTicket(10, "kapot", user, "abc#$*");

        assertEquals("Henk",ticket.getOwner().getName());
    }

    @Test
    public void createTicketFalse() {
        User user = new User("Henk", ",,");
        Ticket ticket = new HardwareTicket(10, "kapot", user, "abc#$*");

        assertEquals("Henk",ticket.getOwner().getName());
    }
}
