# Pokrycie funkcjonalności CodeIgniter 3 przez plugin

Mapa standardowej funkcjonalności CI3 vs implementacja w pluginie (cel: ~99%).

---

## Loader (`$this->load`)

| Funkcja CI3 | Plugin | Uwagi |
|-------------|--------|--------|
| `load->view()` | ✅ | Completion + Go to + Find Usages z widoku |
| `load->model()` | ✅ | Completion modeli z `application/models/` |
| `load->library()` | ✅ | Completion (std + własne) + Go to pliku |
| `load->helper()` | ✅ | Completion (std + własne z `application/helpers/`) |
| `load->database()` | ✅ | Completion połączeń z `config/database.php` |
| `load->config()` | ✅ | Completion plików config z `application/config/` |
| `load->language()` | ✅ | Completion plików językowych (`locale/key`) |
| `load->driver()` | ✅ | Completion: cache + własne z `*_driver.php` |

---

## Super Object (`$this` w kontrolerze)

| Komponent | Plugin | Uwagi |
|-----------|--------|--------|
| `$this->load` | ✅ | Stub + completion |
| `$this->db` | ✅ | Stub + completion metod Query Buildera |
| `$this->input` | ✅ | Completion metod + klucze post/get/cookie/server |
| `$this->session` | ✅ | Stub |
| `$this->config` | ✅ | Completion `config->item()` z kluczami z config |
| `$this->uri` | ✅ | Stub |
| `$this->router` | ✅ | Stub |
| `$this->output` | ✅ | Stub |
| `$this->security` | ✅ | Stub |
| `$this->form_validation` | ✅ | Stub |
| Załadowane biblioteki (`$this->my_lib`) | ✅ | Completion metod + suppress „undefined field” |

---

## Typy i stuby

| Element | Plugin | Uwagi |
|---------|--------|--------|
| `$this` w klasie `extends CI_Controller` / `MY_Controller` | ✅ | Ci3ThisTypeProvider → typ `CI_Controller` |
| `get_instance()` | ✅ | Stub `@return CI_Controller` – `$CI->load` itd. |
| Stuby CI_Controller, MY_Controller | ✅ | Include path z `.ci3-helper/stubs` |

---

## Nawigacja (Go to Declaration)

| Kontekst | Plugin |
|----------|--------|
| `load->view('...')` | ✅ → plik widoku |
| `load->model('...')` | ❌ (tylko completion) |
| `load->library('...')` | ✅ → plik biblioteki |
| `$route['...'] = 'controller/method'` | ✅ → kontroler / metoda |

---

## Widoki

| Funkcja | Plugin |
|---------|--------|
| Completion nazw widoków w `load->view()` | ✅ |
| Go to widoku | ✅ |
| Find Usages z pliku widoku | ✅ |
| Brak błędów „Undefined variable” w widokach | ✅ (suppress w `application/views/`) |

---

## Routing

| Funkcja | Plugin |
|---------|--------|
| Completion controller/method w `routes.php` | ✅ |
| Go to kontrolera z wartości trasy | ✅ |

---

## Bazy danych

| Funkcja | Plugin |
|---------|--------|
| Completion połączeń w `load->database()` | ✅ |
| Completion metod Query Buildera przy `$this->db->` | ✅ (select, where, get, insert, update, delete, order_by, group_by, limit, like, count_*) |

---

## Input / request

| Funkcja | Plugin |
|---------|--------|
| Metody `$this->input->` (post, get, cookie, server, …) | ✅ |
| Klucze w post/get/cookie z projektu | ✅ |
| Klucze `$_SERVER` w `input->server()` | ✅ |
| Nagłówki w `get_request_header()` | ✅ |

---

## Pozostałe elementy CI3 (mniej krytyczne)

| Funkcja CI3 | Plugin | Uwagi |
|-------------|--------|--------|
| Hooks (pre_controller, post_controller itd.) | ❌ | Rzadko potrzebne w IDE |
| `$this->load->vars()` | ❌ | Niszowe |
| Core extensions (MY_Model, MY_Loader) | Częściowo | MY_Controller w typie `$this`; MY_Model bez specjalnej obsługi |
| `$this->load->file()` | ❌ | Niszowe |

---

## Podsumowanie

- **Loader**: 8/8 metod (view, model, library, helper, database, config, language, driver) – **100%**.
- **Super Object**: wszystkie standardowe komponenty + załadowane biblioteki – **100%**.
- **Widoki**: completion, go to, Find Usages, suppress undefined variable – **pełna obsługa**.
- **Routing, DB, Input, Config**: completion i (gdzie sensowne) nawigacja – **pokryte**.
- **Brakuje**: Go to z `load->model()`, Hooks, load->vars/file, pełna obsługa MY_Model.

**Szacunek pokrycia standardowej funkcjonalności CI3 używanej na co dzień: ~99%.**

---

## Weryfikacja po migracji

Checklist plików wymaganych do pełnego pokrycia (wszystkie muszą być w `src/`):

| Plik | Opis |
|------|------|
| `ci/CiViewUtils.kt` | Widoki – findViews, resolveViewFile, getViewsDir |
| `ci/completion/CiModelCompletionContributor.kt` | Completion: load (view, model, library, helper, config, language, driver, database), $this->, db, input, config, routes |
| `ci/navigation/CiViewReferenceContributor.kt` | Go to widoku z `load->view()` |
| `ci/navigation/CiViewFindUsagesProvider.kt` | Find Usages z pliku widoku |
| `ci/navigation/CiLibraryReferenceContributor.kt` | Go to biblioteki z `load->library()` |
| `ci/navigation/CiRouteReferenceContributor.kt` | Go to kontrolera z `routes.php` |
| `ci/suppress/Ci3UndefinedFieldSuppressor.kt` | Suppress: undefined field (Super Object, biblioteki), undefined variable (widoki) |
| `ci/stubs/Ci3StubIncludePathContributor.kt` | Include path: CI_Controller, MY_Controller, global_functions |
| `ci/intentions/AddCi3SuperObjectPropertyIntention.kt` | Intencja „Add CI3 Super Object @property” |
| `ci/types/Ci3ThisTypeProvider.kt` | Typ `$this` → CI_Controller |
| `resources/ci3-stubs/CI_Controller.php` | Stub Super Object |
| `resources/ci3-stubs/MY_Controller.php` | Stub MY_Controller |
| `resources/ci3-stubs/global_functions.php` | Stub get_instance() |
| `resources/messages/MyBundle.properties` | Teksty intencji |
| `helper/MyBundle.kt` | Bundle dla intencji |

Po przywróceniu brakujących plików pokrycie jest **pełne** (~99% funkcjonalności CI3).
