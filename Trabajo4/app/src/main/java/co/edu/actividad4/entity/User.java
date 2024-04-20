package co.edu.actividad4.entity;


//1. La clase User representa la entidad de usuario en la aplicación.
public class User {

    //3. Atributos de la clase User
    private int document;
    private String names;
    private String lastNames;
    private String User;
    private String password;

    //4. Constructor que inicializa todos los atributos de la clase User.
    public User(int document, String names, String lastNames, String user, String password) {
        this.document = document;
        this.names = names;
        this.lastNames = lastNames;
        User = user;
        this.password = password;
    }

    public User() {
    }
    //5. Métodos getter y setter para acceder y modificar los atributos de la clase User.
    public int getDocument() {
        return document;
    }

    public void setDocument(int document) {
        this.document = document;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getLastNames() {
        return lastNames;
    }

    public void setLastNames(String lastNames) {
        this.lastNames = lastNames;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    //5. Método toString que devuelve una representación de cadena de un objeto User.
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("document=").append(document);
        sb.append(", names='").append(names).append('\'');
        sb.append(", lastNames='").append(lastNames).append('\'');
        sb.append(", User='").append(User).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }


}