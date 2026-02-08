<?php
/**
 * Stub CI_Security for PhpStorm – CodeIgniter 3 Security class.
 */
class CI_Security
{
    /**
     * @param string $str String to clean
     * @return string
     */
    public function xss_clean($str) {}

    /**
     * @param string $str Encoded string
     * @return string
     */
    public function entity_decode($str) {}

    /** @return string|false CSRF token or FALSE */
    public function get_csrf_hash() {}

    /** @return string CSRF token name */
    public function get_csrf_token_name() {}
}
