package beans;

public class User {
    private String name, login, password;

    public User(String name, String login, String password){
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public User(){}

    public String getName(){return name;}
}
