Poniżej masz treść przygotowaną bezpośrednio w **Markdown (.md)**, gotową do zapisania jako np. `ci3_reference.md` i użycia przy budowie pluginu do PHPStorm.

Możesz skopiować całość do pliku.

---

```md
# CodeIgniter 3 – Reference for IDE / Plugin Development

Praktyczne techniczne zestawienie elementów CodeIgniter 3 przydatnych przy tworzeniu pluginu IDE.

---

# 📦 Podstawy CodeIgniter 3

CodeIgniter 3 to lekki framework PHP oparty na wzorcu:

MVC = Model – View – Controller

Struktura projektu:

```

application/
controllers/
models/
views/
config/
libraries/
helpers/
hooks/
core/
system/
index.php

````

---

# 🎮 Kontrolery (Controllers)

Kontroler jest punktem wejścia aplikacji.

Przykład:

```php
class Users extends CI_Controller {

    public function index()
    {
        echo "Hello";
    }
}
````

URL:

```
example.com/users/index
```

---

# 🔑 Super Object `$this` w CI3

W kontrolerze `$this` jest instancją CI Super Object i daje dostęp do wszystkich komponentów frameworka.

Najczęściej dostępne właściwości:

```
$this->load
$this->db
$this->input
$this->session
$this->config
$this->uri
$this->router
$this->output
$this->security
$this->form_validation
```

---

## Dostęp poza kontrolerem

W helperach i bibliotekach:

```php
$CI =& get_instance();
$CI->load->model('User_model');
```

---

# 🔄 Loader (`$this->load`)

## Widoki

```php
$this->load->view('users/list', $data);
```

---

## Modele

```php
$this->load->model('User_model');
$this->User_model->getUsers();
```

Alias:

```php
$this->load->model('User_model', 'users');
$this->users->getUsers();
```

---

## Biblioteki

```php
$this->load->library('session');
```

Własne:

```
application/libraries/MyLib.php
```

---

## Helpery

```php
$this->load->helper('url');
```

Kilka:

```php
$this->load->helper(['url', 'form']);
```

---

## Database

```php
$this->load->database();
```

---

## Config

```php
$this->load->config('custom');
$this->config->item('key');
```

---

## Language files

```php
$this->load->language('messages');
```

---

## Driver

```php
$this->load->driver('cache');
```

---

# ⚙ Autoload

Plik:

```
application/config/autoload.php
```

Można automatycznie ładować:

* biblioteki
* helpery
* modele
* bazę danych
* config
* language

---

# 🧠 Najważniejsze komponenty `$this`

## Input

Obsługa requestów:

```php
$this->input->post('name');
$this->input->get('id');
$this->input->cookie('user');
$this->input->ip_address();
```

---

## Database (`$this->db`)

Query Builder:

```php
$this->db->get('users');
$this->db->where('id', 1);
$this->db->insert('users', $data);
$this->db->update('users', $data);
$this->db->delete('users');
```

Raw SQL:

```php
$this->db->query("SELECT * FROM users");
```

---

## Session

```php
$this->session->set_userdata('user_id', 1);
$this->session->userdata('user_id');
$this->session->unset_userdata('user_id');
```

Flashdata:

```php
$this->session->set_flashdata('msg', 'Saved');
```

---

## Form Validation

```php
$this->form_validation->set_rules(
    'email',
    'Email',
    'required|valid_email'
);

if ($this->form_validation->run()) {}
```

---

## URI

```php
$this->uri->segment(1);
```

---

## Router

```php
$this->router->class;
$this->router->method;
```

---

## Output

```php
$this->output->set_output("Hello");
```

JSON:

```php
$this->output
     ->set_content_type('application/json')
     ->set_output(json_encode($data));
```

---

## Security

```php
$this->security->xss_clean($data);
```

---

# 📚 Standardowe biblioteki CI3

Najczęściej używane:

* session
* form_validation
* email
* pagination
* upload
* image_lib
* cart
* encryption
* table
* ftp
* xmlrpc

---

# 🧩 Helpery

Popularne:

* url_helper
* form_helper
* html_helper
* text_helper
* date_helper
* array_helper
* file_helper

---

# 🧬 Modele

```php
class User_model extends CI_Model {

    public function getUsers()
    {
        return $this->db->get('users')->result();
    }
}
```

---

# 🖼 Widoki

Widoki to czyste pliki PHP:

```php
<h1><?= $title ?></h1>
```

---

# ⚙ Routing

Plik:

```
application/config/routes.php
```

Przykład:

```php
$route['users/(:num)'] = 'users/show/$1';
```

---

# 🔌 Hooks

Hooki pozwalają wykonywać kod:

* przed kontrolerem
* po kontrolerze
* przed wysłaniem outputu

---

# 📦 Core Extensions

Można rozszerzać:

```
MY_Controller
MY_Model
MY_Loader
```

---

# 🧠 Lifecycle requestu

```
URL
 → Router
 → Controller
 → Model
 → View
 → Output
```

---

# ⭐ Najczęściej używane elementy

W praktyce projekty korzystają głównie z:

* `$this->load`
* `$this->db`
* `$this->input`
* `$this->session`
* `$this->form_validation`
* `$this->load->view()`
* `$this->load->model()`

---

# 📌 Wskazówki dla pluginu IDE

Plugin powinien wykrywać:

* `$this->load->model()` → autocomplete modeli
* `$this->load->library()` → autocomplete bibliotek
* `$this->load->helper()` → helper functions
* routing → mapowanie URL → controller
* `$this->input->post()` keys
* `$this->config->item()`
* widoki ładowane przez loader

---

## Checklist funkcji pluginu (CodeIgniter 3 Helper)

Mapa: wymagania ze specyfikacji vs stan implementacji w pluginie PHPStorm.

| # | Funkcja ze spec | Opis | Status |
|---|-----------------|------|--------|
| 1 | `$this->load->model()` | Autocomplete nazw modeli (z `application/models/`) | ✅ Zaimplementowane |
| 2 | `$this->load->library()` | Autocomplete bibliotek (własne + standardowe CI3) | ✅ Zaimplementowane |
| 3 | `$this->load->helper()` | Autocomplete helperów (własne + standardowe CI3) | ✅ Zaimplementowane |
| 4 | Widoki ładowane przez loader | Completion + „go to” widoku przy `load->view('...')` | ✅ Zaimplementowane |
| 5 | Super Object `$this->` | Autocomplete: load, db, input, session, config, uri, router, output, security, form_validation | ✅ Zaimplementowane |
| 6 | `$this->load->database()` | Autocomplete nazw połączeń z `config/database.php` | ✅ Zaimplementowane |
| 7 | Metody Query Buildera przy `$this->db->` | select, where, get, insert, update, delete, order_by, group_by, limit, like, count_* | ✅ Zaimplementowane |
| 8 | Routing (URL → controller) | Completion controller/method w `routes.php` + „go to” na kontroler | ✅ Zaimplementowane |
| 9 | `$this->input->post()` keys | Sugestie kluczy z formularzy (form_input, set_value itd.) i z post/get w projekcie | ✅ Zaimplementowane |
| 10 | `$this->config->item()` | Autocomplete kluczy z plików w `application/config/*.php` | ✅ Zaimplementowane |
| 11 | **Obsługa requestów (Input)** | Metody `$this->input->`: post, get, get_post, cookie, server, user_agent, ip_address, valid_ip, method, request_headers, get_request_header, input_stream | ✅ Zaimplementowane |
| 12 | Klucze `input->cookie()` | Completion z projektu (set_cookie, get_cookie, ->cookie) | ✅ Zaimplementowane |
| 13 | Klucze `input->server()` | Completion: standardowe $_SERVER + skan `$_SERVER['...']` w projekcie | ✅ Zaimplementowane |

### Biblioteki CI3 – obsługa w pluginie

* **Completion** – przy `load->library(...)`: standardowe (session, form_validation, email, pagination, upload, image_lib, cart, encryption, table, ftp, xmlrpc) + pliki z `application/libraries/` (w tym w podkatalogach).
* **Go to Declaration** – z `load->library('Name')` / `load->library('name')` na plik `application/libraries/Name.php` (lub w podkatalogu).
* **Property not found** – dla załadowanych bibliotek (`$this->my_lib` po `load->library('My_lib')`) inspekcja „undefined field” jest wyłączana, jeśli plik biblioteki istnieje w `application/libraries/`.

### Standardowe biblioteki CI3 (używane przy `load->library()`)

session, form_validation, email, pagination, upload, image_lib, cart, encryption, table, ftp, xmlrpc.

### Standardowe helpery CI3 (używane przy `load->helper()`)

url, form, html, text, date, array, file.

### Obsługa requestów (Input) – kompletna

* **`$this->input->`** – completion metod: post, get, get_post, cookie, server, user_agent, ip_address, valid_ip, method, request_headers, get_request_header, input_stream.
* **Klucze w post/get/cookie** – sugestie z formularzy (form_input, set_value, set_cookie, get_cookie itd.) oraz z wywołań ->post(), ->get(), ->cookie() w projekcie.
* **Klucze w server()** – standardowe klucze $_SERVER (REQUEST_URI, HTTP_HOST, REMOTE_ADDR itd.) oraz klucze znalezione w `$_SERVER['...']` w application/.

### Wszystkie punkty checklisty są zaimplementowane.

```

---

Jeśli chcesz, w następnym kroku mogę wygenerować:

✅ mapę klas CI3 do autocomplete  
✅ indeksowanie modeli i loadera  
✅ resolver `$this->model`  
✅ parser routes  
✅ plugin feature checklist  
✅ strukturę pluginu PHPStorm  

Możemy zrobić z tego solidny plugin pod CI3.
```
