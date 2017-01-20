package helpdesk.data;

/**
 * Created by etto on 22/12/16.
 */
public class User implements Exportable {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return username;
    }

    @Override
    public String toCSV() {
        return String.format("User;%s;%s",username,password);
    }

    public boolean hasPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public String toString() {
        return "User: " + username;
    }

}
