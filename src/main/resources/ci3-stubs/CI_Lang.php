<?php
/**
 * Stub CI_Lang for PhpStorm – CodeIgniter 3 Language library.
 * Used so that $this->lang->method() gets parameter hints when load->library('language') is used.
 *
 * @see https://codeigniter.com/userguide3/libraries/language.html
 */
class CI_Lang
{
    /**
     * Load a language file. Use default idiom from config if $idiom is omitted.
     *
     * @param string|array $langfile   Language file to load (without _lang.php), or array of files
     * @param string       $idiom     Language name (e.g. 'english')
     * @param bool         $return    Whether to return the loaded array of translations
     * @param bool         $add_suffix Whether to add the '_lang' suffix to the filename
     * @param string       $alt_path  Alternative path to look for the language file
     * @return array|void Array of language lines if $return is true, otherwise void
     */
    public function load($langfile, $idiom = '', $return = false, $add_suffix = true, $alt_path = '') {}

    /**
     * Fetch a single translation line from the loaded language files.
     *
     * @param string $line       Language line key name
     * @param bool   $log_errors Whether to log an error if the line is not found
     * @return string|false Language line or false on failure
     */
    public function line($line, $log_errors = true) {}
}
