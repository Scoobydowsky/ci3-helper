<?php
/**
 * CodeIgniter 3 URL Helper (stub for IDE).
 * Load with: $this->load->helper('url');
 * @see https://codeigniter.com/userguide3/helpers/url_helper.html
 */

/**
 * Returns your site URL with index.php and any URI segments appended.
 *
 * @param string|array $uri      URI string or segments array (default: '')
 * @param string|null  $protocol Protocol, e.g. 'http' or 'https' (default: NULL)
 * @return string
 */
function site_url($uri = '', $protocol = null) {}

/**
 * Returns your site base URL without index_page or url_suffix.
 *
 * @param string       $uri      URI string (default: '')
 * @param string|null  $protocol Protocol, e.g. 'http' or 'https' (default: NULL)
 * @return string
 */
function base_url($uri = '', $protocol = null) {}

/**
 * Returns the full URL (including segments) of the page being currently viewed.
 *
 * @return string
 */
function current_url() {}

/**
 * Returns the URI segments of the current page.
 *
 * @return string
 */
function uri_string() {}

/**
 * Returns your site index_page value as specified in the config file.
 *
 * @return mixed
 */
function index_page() {}

/**
 * Creates a standard HTML anchor link based on your local site URL.
 *
 * @param string       $uri        URI string or segments
 * @param string       $title      Anchor title (default: '')
 * @param string|array $attributes HTML attributes (default: '')
 * @return string
 */
function anchor($uri = '', $title = '', $attributes = '') {}

/**
 * Creates an anchor that opens the URL in a new window.
 *
 * @param string       $uri        URI string or segments
 * @param string       $title      Anchor title (default: '')
 * @param array|bool   $attributes Window attributes or FALSE (default: FALSE)
 * @return string
 */
function anchor_popup($uri = '', $title = '', $attributes = false) {}

/**
 * Creates a standard HTML e-mail link.
 *
 * @param string       $email      E-mail address
 * @param string       $title      Anchor title (default: '')
 * @param string|array $attributes HTML attributes (default: '')
 * @return string
 */
function mailto($email, $title = '', $attributes = '') {}

/**
 * Creates a spam-safe (obfuscated) "mail to" hyperlink.
 *
 * @param string       $email      E-mail address
 * @param string       $title      Anchor title (default: '')
 * @param string|array $attributes HTML attributes (default: '')
 * @return string
 */
function safe_mailto($email, $title = '', $attributes = '') {}

/**
 * Automatically turns URLs and/or e-mail addresses in a string into links.
 *
 * @param string $str   Input string
 * @param string $type  Link type: 'url', 'email' or 'both' (default: 'both')
 * @param bool   $popup Open links in new window (default: FALSE)
 * @return string
 */
function auto_link($str, $type = 'both', $popup = false) {}

/**
 * Takes a string and creates a human-friendly URL slug.
 *
 * @param string $str       Input string
 * @param string $separator Word separator (default: '-')
 * @param bool   $lowercase Force lowercase (default: FALSE)
 * @return string
 */
function url_title($str, $separator = '-', $lowercase = false) {}

/**
 * Adds http:// to a URL if a protocol prefix is missing.
 *
 * @param string $str URL string (default: '')
 * @return string
 */
function prep_url($str = '') {}

/**
 * Does a header redirect to the specified URI. Terminates script execution.
 *
 * @param string $uri   URI string (default: '')
 * @param string $method Redirect method: 'auto', 'location' or 'refresh' (default: 'auto')
 * @param int|null $code HTTP response code, e.g. 301 or 302 (default: NULL)
 * @return void
 */
function redirect($uri = '', $method = 'auto', $code = null) {}
