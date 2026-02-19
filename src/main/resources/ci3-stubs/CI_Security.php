<?php
/**
 * Stub CI_Security for PhpStorm – CodeIgniter 3 Security class.
 * @see https://codeigniter.com/userguide3/libraries/security.html
 */
class CI_Security
{
    /**
     * XSS-clean data. Optional $is_image = true to test images for XSS (returns bool).
     * @param string|array $str String or array of strings to clean
     * @param bool $is_image
     * @return string|bool Cleaned string, or TRUE/FALSE when $is_image is true
     */
    public function xss_clean($str, $is_image = false) {}

    /**
     * Sanitize filename to prevent directory traversal. Set $relative_path = true to allow paths.
     * @param string $str File name/path
     * @param bool $relative_path
     * @return string
     */
    public function sanitize_filename($str, $relative_path = false) {}

    /** @return string CSRF token name */
    public function get_csrf_token_name() {}

    /** @return string|false CSRF hash or FALSE */
    public function get_csrf_hash() {}

    /**
     * Decode HTML entities (like html_entity_decode in ENT_COMPAT mode).
     * @param string $str Encoded string
     * @param string|null $charset Character set (default: config charset)
     * @return string
     */
    public function entity_decode($str, $charset = null) {}

    /**
     * Get random bytes (mcrypt_create_iv, /dev/urandom, or openssl_random_pseudo_bytes).
     * @param int $length Output length
     * @return string|false Binary random bytes or FALSE on failure
     */
    public function get_random_bytes($length) {}
}
