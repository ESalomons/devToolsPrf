package helpdesk.data;

/**
 * Created by etto on 22/12/16.
 */
public class HardwareTicket extends Ticket {

    private String machineCode;

    public HardwareTicket(int number, String description, User owner, String machineCode) {
        super(number, description, owner);
        this.machineCode = machineCode;
    }

    @Override
    protected String getTicketType() {
        return "Hardware";
    }

    @Override
    protected String getProductDescription() {
        return machineCode;
    }
}
