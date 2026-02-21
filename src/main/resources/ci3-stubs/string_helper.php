<?php
/**
 * CodeIgniter 3 String Helper (stub for IDE).
 * Load with: $this->load->helper('string');
 * @see https://codeigniter.com/userguide3/helpers/string_helper.html
 */

/**
 * Generates a random string based on the type and length you specify.
 * Types: sha1 (40 chars), md5 (32), nozero, numeric, basic, alnum, alpha.
 * Not suitable for password generation; use random_bytes() instead.
 *
 * @param string $type Randomization type (default: 'alnum')
 * @param int    $len  Output string length (default: 8)
 * @return string
 */
function random_string($type = 'alnum', $len = 8) {}

/**
 * Increments a string by appending a number or increasing the number.
 * Useful for creating "copies" or duplicating content with unique titles/slugs.
 *
 * @param string $str       Input string (e.g. "file" or "file_4")
 * @param string $separator Separator to append a duplicate number with (default: '_')
 * @param int    $first     Starting number (default: 1)
 * @return string
 */
function increment_string($str, $separator = '_', $first = 1) {}

/**
 * Allows two or more items to be alternated between when cycling through a loop.
 * Call with no arguments to re-initialize.
 *
 * @param mixed ...$args Variable number of arguments to alternate
 * @return mixed
 */
function alternator(...$args) {}

/**
 * Generates repeating copies of the data you submit.
 *
 * @param string $data Input string
 * @param int    $num  Number of times to repeat (default: 1)
 * @return string
 * @deprecated Use str_repeat() instead
 */
function repeater($data, $num = 1) {}

/**
 * Converts double slashes in a string to a single slash, except in URL protocol prefixes (e.g. http://).
 *
 * @param string $str Input string
 * @return string
 */
function reduce_double_slashes($str) {}

/**
 * Removes any slashes from an array of strings (or a single string; alias for stripslashes()).
 *
 * @param mixed $data Input string or array of strings
 * @return mixed
 */
function strip_slashes($data) {}

/**
 * Removes any leading/trailing slashes from a string.
 *
 * @param string $str Input string
 * @return string
 * @deprecated Use trim($str, '/') instead
 */
function trim_slashes($str) {}

/**
 * Reduces multiple instances of a character occurring directly after each other.
 *
 * @param string $str       Text to search in
 * @param string $character Character to reduce (default: '')
 * @param bool   $trim      Whether to also trim the character at start/end (default: FALSE)
 * @return string
 */
function reduce_multiples($str, $character = '', $trim = false) {}

/**
 * Converts single and double quotes in a string to the corresponding HTML entities.
 *
 * @param string $str Input string
 * @return string
 */
function quotes_to_entities($str) {}

/**
 * Removes single and double quotes from a string.
 *
 * @param string $str Input string
 * @return string
 */
function strip_quotes($str) {}
