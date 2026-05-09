package service;

import model.LibraryData;

/**
 * Klasa odpowiadajaca za logike biznesowa systemu bibliotycznego
 */
public class LibraryService {
    //obiekt zwieracacy dane aplikacji
    private final LibraryData data;

    // serwis dla przetwarzania danych biblioteki
    // @param data kontener danych aplikacji
    public LibraryService(LibraryData data){
        this.data = data;
    }

    public LibraryData getData() {
        return data;
    }
}
