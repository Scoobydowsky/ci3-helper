<?php
/**
 * CodeIgniter 3 Inflector Helper (stub for IDE).
 * Load with: $this->load->helper('inflector');
 * @see https://codeigniter.com/userguide3/helpers/inflector_helper.html
 */

/**
 * Changes a plural word to singular.
 *
 * @param string $str Input string (plural word)
 * @return string Singular word
 */
function singular($str) {}

/**
 * Changes a singular word to plural.
 *
 * @param string $str Input string (singular word)
 * @return string Plural word
 */
function plural($str) {}

/**
 * Changes a string of words separated by spaces or underscores to camel case.
 *
 * @param string $str Input string
 * @return string Camelized string (e.g. 'my_dog_spot' -> 'myDogSpot')
 */
function camelize($str) {}

/**
 * Takes multiple words separated by spaces and underscores them.
 *
 * @param string $str Input string
 * @return string String containing underscores instead of spaces
 */
function underscore($str) {}

/**
 * Takes multiple words separated by underscores and adds spaces between them. Each word is capitalized.
 *
 * @param string $str       Input string (e.g. 'my_dog_spot')
 * @param string $separator Separator to replace (default: '_'). Use '-' for dashed input.
 * @return string Humanized string (e.g. 'My Dog Spot')
 */
function humanize($str, $separator = '_') {}

/**
 * Checks if the given word has a plural version.
 *
 * @param string $word Input word
 * @return bool TRUE if the word is countable, FALSE if not (e.g. 'equipment' -> FALSE)
 */
function word_is_countable($word) {}
