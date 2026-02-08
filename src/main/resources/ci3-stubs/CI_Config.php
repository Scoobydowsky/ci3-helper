<?php
/**
 * Stub CI_Config for PhpStorm – CodeIgniter 3 Config class.
 * @see https://codeigniter.com/userguide3/libraries/config.html
 */
class CI_Config
{
    /**
     * @param string $item Config item key (e.g. 'base_url')
     * @param mixed $index Index for array values
     * @return mixed
     */
    public function item($item, $index = '') {}

    /**
     * @param string $file Config file name (without .php)
     * @param bool $use_sections Use sections
     * @param bool $fail_gracefully Return FALSE on failure
     * @return bool
     */
    public function load($file = '', $use_sections = false, $fail_gracefully = false) {}

    /**
     * @param string $key Config item key
     * @param mixed $val Value to set
     * @return void
     */
    public function set_item($key, $val) {}
}
