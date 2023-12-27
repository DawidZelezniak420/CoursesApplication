StudentService jak i CourseService posiadają klasę o nazwie TestingData , w której znajdują się przykładowe dane testowe .

STUDENT SERVICE podstawowe informacje: Studenci przechowywani są lokalnie w bazie MySQL. Serwis domyślnie znajduje się na porcie 8080.
Logika tego serwisu zakłada, że każdy student musi mieć unikalny adres e-mail , to on jest wyznacznikiem unikalności każdego studenta.
Warstwa kontrolera podzielona jest na dwie głowne klasy , jedna odpowiedzialna jest tylko i wyłącznie za obsługę zapytań wysyłanych z
poziomu np.aplikacji Postman ,natomiast druga klasa odpowiedzialna jest za obsługę widoku.W widoku nie użyłem wszystkich metod jakie
znajdują się w warstwie serwisowej , ponieważ spełnia on założenia , które chciałem osiągnąć ale przydają się one do szybkiego podglądu
konkretnych danych z poziomu postmana więc je zostawiłem(tak samo jest w course service).W obu serwisach postarałem się o to aby obsłużyć
typowe błędy jakie mogą wystąpić podczas wykonywania operacji na danych i dostarczyć użytkownikowi bardziej szczegółowe informacje.

COURSE SERVICE podstawowe informacje: Kursy przechowywane są w bazie MongoDb(w chmurze).Serwis stoi na porcie 8085.Tutaj logika 
zakłada , że wyznacznikiem unikalności kursu jest jego id typu String np. Java-2024-1.Idea działania aplikacji jest podobna do tego
co napisalem powyżej. Serwis ten posiada interfejs wykorzystujący FeignClient(dlatego postawiłem serwisy na serwerze Eureka) co umożliwia 
pobieranie studentów z bazy danych.Po wywołaniu odpowiedniego endpointu z poziomu postmana lub kliknięiu w przycisk finish-enroll wysyłane 
są wiadomości na e-maile uczęstników kursu.Użyłem do tego smtp od gmail, ponieważ inne poczty pod tym względem były trudne do skonfigurowania 
i tylko gmail mi zadziałał.
