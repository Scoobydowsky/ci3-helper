<?php
/**
 * CodeIgniter 3 Cookie Helper (stub for IDE).
 * Load with: $this->load->helper('cookie');
 * @see https://codeigniter.com/userguide3/helpers/cookie_helper.html
 */

/**
 * Set a browser cookie. Friendlier syntax; alias for CI_Input::set_cookie().
 *
 * @param string|array $name    Cookie name, or associative array of all parameters (name, value, expire, domain, path, prefix, secure, httponly)
 * @param string       $value   Cookie value (default '')
 * @param int          $expire  Number of seconds until expiration (default '')
 * @param string       $domain  Cookie domain, e.g. .yourdomain.com (default '')
 * @param string       $path    Cookie path (default '/')
 * @param string       $prefix  Cookie name prefix (default '')
 * @param bool|null    $secure  Whether to send the cookie only over HTTPS (default NULL)
 * @param bool|null    $httponly Whether to hide the cookie from JavaScript (default NULL)
 * @return void
 */
function set_cookie($name, $value = '', $expire = '', $domain = '', $path = '/', $prefix = '', $secure = null, $httponly = null) {}

/**
 * Get a browser cookie. Friendlier syntax; similar to CI_Input::cookie() but prepends config cookie_prefix.
 *
 * @param string   $index    Cookie name
 * @param bool|null $xss_clean Whether to apply XSS filtering to the returned value (default NULL)
 * @return mixed Cookie value or NULL if not found
 */
function get_cookie($index, $xss_clean = null) {}

/**
 * Delete a cookie. Same options as set_cookie() but without value and expiration.
 *
 * @param string $name   Cookie name
 * @param string $domain Cookie domain, e.g. .yourdomain.com (default '')
 * @param string $path   Cookie path (default '/')
 * @param string $prefix Cookie name prefix (default '')
 * @return void
 */
function delete_cookie($name, $domain = '', $path = '/', $prefix = '') {}
