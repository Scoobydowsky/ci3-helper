<?php
/**
 * Stub CI_Input for PhpStorm – CodeIgniter 3 Input class.
 * @see https://codeigniter.com/userguide3/libraries/input.html
 *
 * @property string $raw_input_stream php://input data
 */
class CI_Input
{
    /**
     * @param mixed $index POST key name, array of keys, or NULL for all
     * @param bool|null $xss_clean Apply XSS filter
     * @return mixed
     */
    public function post($index = null, $xss_clean = null) {}

    /**
     * @param mixed $index GET key name, array of keys, or NULL for all
     * @param bool|null $xss_clean Apply XSS filter
     * @return mixed
     */
    public function get($index = null, $xss_clean = null) {}

    /**
     * @param string $index Parameter name (POST then GET)
     * @param bool|null $xss_clean Apply XSS filter
     * @return mixed
     */
    public function post_get($index, $xss_clean = null) {}

    /**
     * @param string $index Parameter name (GET then POST)
     * @param bool|null $xss_clean Apply XSS filter
     * @return mixed
     */
    public function get_post($index, $xss_clean = null) {}

    /**
     * @param mixed $index Cookie name, array of names, or NULL for all
     * @param bool|null $xss_clean Apply XSS filter
     * @return mixed
     */
    public function cookie($index = null, $xss_clean = null) {}

    /**
     * @param mixed $index $_SERVER key (e.g. 'REQUEST_URI'), or array of keys
     * @param bool|null $xss_clean Apply XSS filter
     * @return mixed
     */
    public function server($index, $xss_clean = null) {}

    /**
     * @param mixed $index Key name or NULL for full stream array
     * @param bool|null $xss_clean Apply XSS filter
     * @return mixed
     */
    public function input_stream($index = null, $xss_clean = null) {}

    /**
     * @param string|array $name Cookie name or array of params (name, value, expire, ...)
     * @param string $value Cookie value
     * @param int $expire Expiration in seconds
     * @param string $domain Domain
     * @param string $path Path (default '/')
     * @param string $prefix Cookie name prefix
     * @param bool|null $secure HTTPS only
     * @param bool|null $httponly HTTP only (no JS)
     * @param string|null $samesite 'Lax', 'Strict', 'None'
     * @return void
     */
    public function set_cookie($name = '', $value = '', $expire = '', $domain = '', $path = '/', $prefix = '', $secure = null, $httponly = null, $samesite = null) {}

    /** @return string IP address or '0.0.0.0' */
    public function ip_address() {}

    /**
     * @param string $ip IP address
     * @param string $which 'ipv4' or 'ipv6'
     * @return bool
     */
    public function valid_ip($ip, $which = '') {}

    /**
     * @param bool|null $xss_clean Apply XSS filter
     * @return string|null User agent string
     */
    public function user_agent($xss_clean = null) {}

    /**
     * @param bool $xss_clean Apply XSS filter
     * @return array
     */
    public function request_headers($xss_clean = false) {}

    /**
     * @param string $index Header name (e.g. 'Content-Type')
     * @param bool $xss_clean Apply XSS filter
     * @return string|null
     */
    public function get_request_header($index, $xss_clean = false) {}

    /** @return bool */
    public function is_ajax_request() {}

    /** @return bool */
    public function is_cli_request() {}

    /**
     * @param bool $upper Return uppercase (e.g. 'POST')
     * @return string Request method (e.g. 'post', 'POST')
     */
    public function method($upper = false) {}
}
