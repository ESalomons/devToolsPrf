package helpdesk.data;

/**
 * Created by etto on 22/12/16.
 */
public class SoftwareTicket extends Ticket {

    private String productName;

    public SoftwareTicket(int number, String description, User owner, String productName) {
        super(number, description, owner);
        this.productName = productName;
    }


    @Override
    protected String getTicketType() {
        return "Software";
    }

    @Override
    protected String getProductDescription() {
        return productName;
    }
}
