<?php
/**
 * CodeIgniter 3 Array Helper (stub for IDE).
 * Load with: $this->load->helper('array');
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
