# Role w projekcie

## Wojciech Czok

Odpowiedzialny za interfejs użytkownika Swing oraz integrację funkcji aplikacji.

Zakres prac:

* utworzenie głównego okna aplikacji `MainFrame`,
* przygotowanie paneli GUI:

    * `BooksPanel`,
    * `DepartmentsPanel`,
    * `ShelvesPanel`,
    * `ReadersPanel`,
    * `LoansPanel`,
    * `SearchPanel`,
* integracja paneli z klasą `LibraryService`,
* dodanie obsługi zapisu i odczytu danych z poziomu GUI,
* dodanie zapisu danych w osobnym wątku przy użyciu `SwingWorker`,
* dodanie przełączania języka interfejsu PL/EN,
* dostosowanie tekstów interfejsu do `LanguageManager`,
* testowanie działania aplikacji okienkowej.

## Rafał Częścik

Odpowiedzialna za logikę biznesową aplikacji.

Zakres prac:

* przygotowanie klas modelu:

    * `Book`,
    * `Department`,
    * `Shelf`,
    * `Reader`,
    * `Loan`,
    * `LibraryData`,
* implementacja klasy `LibraryService`,
* obsługa operacji CRUD na danych biblioteki,
* obsługa wypożyczania i zwracania książek,
* sprawdzanie dostępności książek,
* wyszukiwanie książek,
* wyszukiwanie przeterminowanych wypożyczeń,
* przygotowanie testów jednostkowych logiki biznesowej.

## Hubert Mokry

Odpowiedzialna za zapis i odczyt danych aplikacji.

Zakres prac:

* przygotowanie klasy `FileStorageService`,
* implementacja zapisu danych z użyciem `DataOutputStream`,
* implementacja odczytu danych z użyciem `DataInputStream`,
* obsługa zapisu i odczytu:

    * działów literatury,
    * regałów i półek,
    * czytelników,
    * książek,
    * wypożyczeń,
* odtwarzanie relacji między obiektami po wczytaniu danych,
* przygotowanie testu `FileStorageServiceTest`.

## Adam Sosiński

Odpowiedzialna za internacjonalizację, dokumentację i finalizację projektu.

Zakres prac:

* przygotowanie plików językowych:

    * `messages_pl.properties`,
    * `messages_en.properties`,
* przygotowanie klasy `LanguageManager`,
* sprawdzenie poprawności przełączania języka w aplikacji,
* dodanie klasy konfiguracyjnej `AppConfig`,
* przygotowanie dokumentacji Javadoc,
* dodanie plików `package-info.java`,
* sprawdzenie budowania projektu przez Maven,
* przygotowanie aplikacji do prezentacji końcowej.

## Wspólne zadania zespołu

Wspólnie zespół odpowiadał za:

* testowanie aplikacji,
* poprawianie błędów,
* przygotowanie końcowej wersji projektu,
* sprawdzenie działania aplikacji zgodnie z wymaganiami,
* przygotowanie scenariusza prezentacji projektu.
