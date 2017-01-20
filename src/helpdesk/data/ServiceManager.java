package helpdesk.data;

/**
 * Created by etto on 22/12/16.
 */
public class ServiceManager extends User {
    private String expertise;
    private String department;

    public ServiceManager(String username, String password, String expertise, String department) {
        super(username, password);
        this.expertise = expertise;
        this.department = department;
    }

    @Override
    public String toCSV() {
        return String.format("%s;%s;%s", super.toCSV().replace("User", "Manager"), expertise, department);
    }

    @Override
    public String toString() {
        return String.format("Service Manager: %s. Department: %s. Specialisation: %s.",
                getName(), department, expertise);
    }
}
