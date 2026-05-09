package model;

/**
 * Kalsa reprezentuje dział literatury, np. HIST, IT, BIO.
 */
public class Department {
    // unikalny identyfikator dzialu
    private Long id;
    // kod dzialu
    private String code;
    // nazwa dzialu
    private String name;
    // opis dzialu
    private String description;

    public Department(Long id, String code, String name, String description){
        this.id=id;
        this.code=code;
        this.name=name;
        this.description=description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String toString(){
        return code +" - "+name;
    }
}
