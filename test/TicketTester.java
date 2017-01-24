import helpdesk.data.HardwareTicket;
import helpdesk.data.ServiceManager;
import helpdesk.data.Ticket;
import helpdesk.data.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Etto Salomons
 *         created on 20/01/17.
 */
public class TicketTester {
    private User henk;
    private HardwareTicket hTicket;

    @Before
    public void setup() {
        henk = new User("Henk", ",,");
        hTicket = new HardwareTicket(10, "kapot", henk, "abc#$*");
    }

    @Test
    public void createTicket() {
        assertEquals("Henk", hTicket.getOwner().getName());
    }

    @Test
    public void createTicketFalse() {

        assertEquals("Henk", hTicket.getOwner().getName());
    }

    @Test
    public void toStringTest(){
        Ticket t = hTicket;
        assertNotEquals("dinges", t.toString());
        t.setSolution(new ServiceManager("Piet","pt","nothing","ITSM"), "ja");
        assertNotEquals("dinges", t.toString());
    }
}
