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
