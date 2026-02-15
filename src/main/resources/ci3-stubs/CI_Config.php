<?php
/**
 * Stub CI_Config for PhpStorm – CodeIgniter 3 Config class.
 * @see https://codeigniter.com/userguide3/libraries/config.html
 */
class CI_Config
{
    /** @var array Array of all loaded config values */
    public $config;

    /** @var array Array of all loaded config files */
    public $is_loaded;

    /**
     * Fetch a config file item.
     * @param string $item Config item name
     * @param string $index Index name (when using load(..., true))
     * @return mixed Config item value or NULL if not found
     */
    public function item($item, $index = '') {}

    /**
     * Sets a config file item to the specified value.
     * @param string $item Config item name
     * @param mixed $value Config item value
     * @return void
     */
    public function set_item($item, $value) {}

    /**
     * Same as item() but appends a trailing forward slash if the item exists.
     * @param string $item Config item name
     * @return mixed Config item value with trailing slash or NULL if not found
     */
    public function slash_item($item) {}

    /**
     * Loads a configuration file.
     * @param string $file Configuration file name (without .php)
     * @param bool $use_sections Whether to load into section (index of main config array)
     * @param bool $fail_gracefully Whether to return FALSE or display error on failure
     * @return bool TRUE on success, FALSE on failure
     */
    public function load($file = '', $use_sections = false, $fail_gracefully = false) {}

    /**
     * Site URL with index_page and optional URI segments.
     * @param string $uri URI path to append
     * @param string|null $protocol Protocol (e.g. 'https')
     * @return string
     */
    public function site_url($uri = '', $protocol = null) {}

    /**
     * Base URL without index_page (e.g. for assets).
     * @param string $uri URI path to append
     * @param string|null $protocol Protocol (e.g. 'https')
     * @return string
     */
    public function base_url($uri = '', $protocol = null) {}

    /**
     * URL to the system/ directory.
     * @deprecated System directory should not be publicly accessible
     * @return string
     */
    public function system_url() {}
}
