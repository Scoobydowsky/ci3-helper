<?php
/**
 * Stub funkcji globalnych CI3 dla PhpStorm.
 * @see https://codeigniter.com/userguide3/general/ancillary_classes.html#get_instance
 * @see https://codeigniter.com/userguide3/general/common_functions.html
 *
 * @return CI_Controller
 */
function get_instance() {}

/**
 * Common Functions — globally available, no library or helper required.
 * @see https://codeigniter.com/userguide3/general/common_functions.html
 */

/**
 * Determines if the PHP version being used is equal to or greater than the supplied version number.
 *
 * @param string $version Version number (e.g. '5.3', '7.4')
 * @return bool TRUE if the running PHP version is at least the one specified, FALSE otherwise
 */
function is_php($version) {}

/**
 * Determines if a file is actually writable (avoids Windows is_writable() unreliability).
 *
 * @param string $file File path
 * @return bool TRUE if the path is writable, FALSE if not
 */
function is_really_writable($file) {}

/**
 * Retrieves a single configuration key. Prefer Config library for multiple keys.
 *
 * @param string $key Config item key
 * @return mixed Configuration key value or NULL if not found
 */
function config_item($key) {}

/**
 * Manually set an HTTP response status header.
 *
 * @param int    $code HTTP response status code (e.g. 401, 404)
 * @param string $text Optional custom message to set with the status code
 * @return void
 */
function set_status_header($code, $text = '') {}

/**
 * Removes invisible characters (e.g. NULL bytes) to prevent strings like Java\0script.
 *
 * @param string $str         Input string
 * @param bool   $url_encoded Whether to remove URL-encoded characters as well (default TRUE)
 * @return string Sanitized string
 */
function remove_invisible_characters($str, $url_encoded = true) {}

/**
 * HTML-escapes a string or array of strings (alias for htmlspecialchars with array support). Use to prevent XSS.
 *
 * @param mixed $var Variable to escape (string or array)
 * @return mixed HTML escaped string(s)
 */
function html_escape($var) {}

/**
 * Returns a reference to the MIMEs array from application/config/mimes.php.
 *
 * @return array<string, string> Associative array of file extension => MIME type
 */
function get_mimes() {}

/**
 * Returns whether the current request uses HTTPS.
 *
 * @return bool TRUE if using HTTP-over-SSL, FALSE otherwise
 */
function is_https() {}

/**
 * Returns whether the application is running from the command line.
 *
 * @return bool TRUE if running under CLI, FALSE otherwise
 */
function is_cli() {}

/**
 * Checks if a function exists and is usable (not disabled by e.g. Suhosin).
 *
 * @param string $function_name Function name to check (e.g. 'eval', 'exec')
 * @return bool TRUE if the function can be used, FALSE otherwise
 */
function function_usable($function_name) {}

/**
 * Array Helper (load with: $this->load->helper('array');)
 * @see https://codeigniter.com/userguide3/helpers/array_helper.html
 */

/**
 * Fetch a single item from an array. Returns the value if the index exists and has a value, otherwise NULL or the default.
 *
 * @param string $item   Item to fetch from the array
 * @param array  $array  Input array
 * @param mixed  $default Default value if the array key is not set or is empty (default NULL)
 * @return mixed
 */
function element($item, $array, $default = null) {}

/**
 * Fetch multiple items from an array by passing an array of keys. Returns a new array with the requested keys; non-existent keys get NULL or the default.
 *
 * @param array $items   Array of keys to fetch
 * @param array $array   Input array
 * @param mixed $default Default value for missing keys (default NULL)
 * @return array
 */
function elements($items, $array, $default = null) {}

/**
 * Takes an array and returns a random element from it.
 *
 * @param array $array Input array
 * @return mixed
 */
function random_element($array) {}

/**
 * Compatibility Functions — backports for PHP functions available only in newer PHP or extensions.
 * Always available when dependencies are met. Use MB_ENABLED to check multibyte support.
 * @see https://codeigniter.com/userguide3/general/compatibility_functions.html
 */

/** Password hashing (PHP 5.5 backport). Algorithm constant. */
if (!defined('PASSWORD_DEFAULT')) {
    define('PASSWORD_DEFAULT', 1);
}
/** Password hashing. Bcrypt algorithm. */
if (!defined('PASSWORD_BCRYPT')) {
    define('PASSWORD_BCRYPT', 1);
}
/** TRUE if mbstring/iconv multibyte support is available. */
if (!defined('MB_ENABLED')) {
    define('MB_ENABLED', false);
}

/* --- Password Hashing --- */

/**
 * Get information about a password hash.
 * @param string $hash Password hash
 * @return array{algo?: int, algoName?: string, options?: array}
 */
function password_get_info($hash) {}

/**
 * Create a password hash.
 * @param string $password Plain-text password
 * @param int $algo Algorithm (PASSWORD_BCRYPT, PASSWORD_DEFAULT)
 * @param array $options Options (e.g. 'cost' for bcrypt)
 * @return string|false Hash or FALSE on failure
 */
function password_hash($password, $algo, $options = []) {}

/**
 * Check if a hash should be rehashed to match the given algorithm and options.
 * @param string $hash Password hash
 * @param int $algo Algorithm
 * @param array $options Options
 * @return bool TRUE if rehash needed, FALSE otherwise
 */
function password_needs_rehash($hash, $algo, $options = []) {}

/**
 * Verify a password against a hash.
 * @param string $password Plain-text password
 * @param string $hash Password hash
 * @return bool TRUE if match, FALSE otherwise
 */
function password_verify($password, $hash) {}

/* --- Hash (Message Digest) --- */

/**
 * Timing-safe string comparison.
 * @param string $known_string Known string
 * @param string $user_string User-supplied string
 * @return bool TRUE if equal, FALSE otherwise
 */
function hash_equals($known_string, $user_string) {}

/**
 * PBKDF2 key derivation.
 * @param string $algo Hashing algorithm (e.g. 'sha256')
 * @param string $password Password
 * @param string $salt Salt
 * @param int $iterations Iteration count
 * @param int $length Output length (0 = algorithm default)
 * @param bool $raw_output TRUE for raw binary output
 * @return string|false Derived key or FALSE on failure
 */
function hash_pbkdf2($algo, $password, $salt, $iterations, $length = 0, $raw_output = false) {}

/* --- Multibyte String (limited, uses iconv when mbstring unavailable) --- */

/**
 * Get string length (multibyte-safe). Uses config charset when $encoding is NULL.
 * @param string $str Input string
 * @param string|null $encoding Character set (NULL = config charset)
 * @return int|false Character count or FALSE on failure
 */
function mb_strlen($str, $encoding = null) {}

/**
 * Find position of needle in haystack (multibyte-safe).
 * @param string $haystack String to search in
 * @param string $needle String to find
 * @param int $offset Search offset (default 0)
 * @param string|null $encoding Character set (NULL = config charset)
 * @return int|false Position or FALSE if not found
 */
function mb_strpos($haystack, $needle, $offset = 0, $encoding = null) {}

/**
 * Get part of string (multibyte-safe).
 * @param string $str Input string
 * @param int $start Start position
 * @param int|null $length Max characters (NULL = to end)
 * @param string|null $encoding Character set (NULL = config charset)
 * @return string|false Substring or FALSE on failure
 */
function mb_substr($str, $start, $length = null, $encoding = null) {}

/* --- Standard (newer PHP backports) --- */

/**
 * Return the values from a single column in the input array.
 * @param array $array Input array
 * @param int|string|null $column_key Column key to return
 * @param int|string|null $index_key Column to use as index (optional)
 * @return array Column values
 */
function array_column(array $array, $column_key, $index_key = null) {}

/**
 * Decode a hexadecimally encoded binary string.
 * @param string $data Hexadecimal representation of data
 * @return string|false Binary string or FALSE on failure
 */
function hex2bin($data) {}
