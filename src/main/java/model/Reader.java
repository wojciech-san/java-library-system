package model;

/**
 * Klasa reprezentuje osobe wypozyczjaca ksiazki
 */
public class Reader {
    // unikalny identyfikator wypozyczajacego
    private Long id;
    // imie
    private String firstName;
    // nazwisko
    private String lastName;
    //adres
    private String address;
    // nr legitymacji
    private String cardNumber;

    public Reader(Long id, String firstName, String lastName, String address, String cardNumber){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.cardNumber = cardNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    @Override
    public String toString(){
        return firstName+" "+lastName+" ("+cardNumber+" )";
    }
}
