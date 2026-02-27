# CI3 Routing – co plugin już robi i co można dodać

Źródło: [CodeIgniter 3 – URI Routing](https://codeigniter.com/userguide3/general/routing.html).

## Już zaimplementowane w pluginie

- **Completion** w `routes.php` dla wartości trasy: `controller` lub `controller/method` (w tym z podkatalogów, np. `products/shoes`).
- **Go to Declaration** z wartości trasy do pliku kontrolera w `application/controllers/`.
- Obsługa **back-reference** w wartości: `'catalog/product_lookup_by_id/$1'` – rozpoznawany jest fragment przed `$` (kontroler).
- Forma **HTTP verb** `$route['path']['PUT'] = 'controller/method'` – completion i Go to Declaration działają na tym samym literale stringowym co zwykła trasa.

## Dodane w tej sesji

- **Completion zarezerwowanych kluczy** w `routes.php`: przy wpisywaniu klucza w `$route['...']` (przed `=`) podpowiadane są:
  - `default_controller`
  - `404_override`
  - `translate_uri_dashes`

## Zaimplementowane (rozszerzenia)

- **Wildcards w completion klucza**: przy wpisywaniu klucza trasy podpowiadane są też `(:num)` i `(:any)` z opisem.
- **Inspekcja default_controller**: ostrzeżenie, gdy wartość `default_controller` zawiera slash (katalog).
- **Inspekcja leading/trailing slash**: ostrzeżenie, gdy klucz trasy zaczyna lub kończy się od `/`.

## Trasy dynamiczne

- **Back-reference w wartości** (`$1`, `$2`, …): wartość np. `'catalog/product_lookup_by_id/$1'` jest rozumiana — do Go to Declaration używana jest część przed `$` (kontroler). Completion w wartości trasy oferuje szablony: `controller/$1`, `controller/method/$1`, `controller/method/$1/$2` z opisem „dynamic: back-reference”.
- **Klucz z wildcardami** `(:num)`, `(:any)`: completion podpowiada te wzorce przy wpisywaniu klucza.
- **Wartość = zmienna PHP** (np. `$route['x'] = $var`): plugin nie tworzy referencji (nie parsuje zmiennej). Nie zgłaszamy błędu — trasa jest uznawana za dynamiczną.
- **Callback (closure)** jako wartość: Go to Declaration działa na prostych stringach w `return '...'`; przy konkatenacji rozpoznawanie kontrolera nie jest możliwe.

## Możliwe rozszerzenia (na później)

1. **Stub CI_Router**  
   Obecny stub ma `fetch_class()`, `fetch_method()`, `fetch_directory()`. W razie potrzeby można dodać kolejne metody z oryginalnego Routera.
