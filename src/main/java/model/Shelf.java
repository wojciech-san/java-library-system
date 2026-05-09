package model;

/**
 * Klasa reprezentuje miejsce przechowywania ksiazki : regal , polka
 */
public class Shelf {
    // unikalny identyfikator miejsca przechowywania.
    private Long id;
    //numer regalu
    private String rack;
    // numer polki
    private String shelf;

    public Shelf(Long id,String rack, String shelf){
            this.id=id;
            this.rack=rack;
            this.shelf=shelf;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRack() {
        return rack;
    }

    public void setRack(String rack) {
        this.rack = rack;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }
    @Override
    public String toString(){
        return "Regal: "+rack+", polka "+shelf;
    }

}
