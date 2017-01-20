package helpdesk.data;

/**
 * Created by etto on 22/12/16.
 */
public abstract class Ticket implements Exportable {
    private int number;
    private String description;
    private String solution;
    private User owner;
    private ServiceManager handler;

    public Ticket(int number, String description, User owner) {
        this.number = number;
        this.description = description;
        this.owner = owner;
    }

    protected abstract String getTicketType();

    protected abstract String getProductDescription();

    public void setSolution(ServiceManager handler, String solution) {
        this.handler = handler;
        this.solution = solution;
    }

    public User getOwner() {
        return owner;
    }

    public boolean isResolved() {
        return solution != null;
    }

    @Override
    public String toCSV() {
        String result = String.format("Ticket;%d;%s;%s;%s;%s", number, getTicketType(),
                owner.getName(), getProductDescription(), description);
        if(handler != null){
            result = String.format("%s;%s;%s",result, handler.getName(),solution);
        }
        return result;
    }

    public String toString(){
        String result = String.format(String.format("Ticket nr: %d (%s). " +
                        "\n\tUser:\t%s." +
                        "\n\tProduct:\t%s" +
                        "\n\tProblem:\t%s"
                        , number, getTicketType(), owner.getName(), getProductDescription(), description));
        if(isResolved()){
            result += String.format("\n\tSolved by:\t%s" +
                    "\n\tSolution:\t%s"
                    , handler.getName(), solution);
        }
        return result;
    }

    public boolean hasNumber(int number) {
        return this.number == number;
    }
}
