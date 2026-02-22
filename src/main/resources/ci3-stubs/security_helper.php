<?php
/**
 * CodeIgniter 3 Security Helper (stub for IDE).
 * Load with: $this->load->helper('security');
 * @see https://codeigniter.com/userguide3/helpers/security_helper.html
 */

/**
 * XSS-clean string. Alias for CI_Input::xss_clean().
 *
 * @param string $str       Input data
 * @param bool   $is_image  Whether we're dealing with an image (default: false)
 * @return string Filtered string
 */
function xss_clean($str, $is_image = false) {}

/**
 * Sanitized file name. Protection against directory traversal. Alias for CI_Security::sanitize_filename().
 *
 * @param string $filename Filename
 * @return string Sanitized filename
 */
function sanitize_filename($filename) {}

/**
 * One-way hash (e.g. for passwords). Uses SHA1 by default. DEPRECATED: use native hash() instead.
 *
 * @param string $str  Input
 * @param string $type Algorithm (default: 'sha1'), e.g. 'md5'
 * @return string Hex-formatted hash
 */
function do_hash($str, $type = 'sha1') {}

/**
 * Strip image tags from string, leaving the image URL as plain text. Alias for CI_Security::strip_image_tags().
 *
 * @param string $str Input string
 * @return string String with no image tags
 */
function strip_image_tags($str) {}

/**
 * Convert PHP tags to entities. xss_clean() does this automatically if you use it.
 *
 * @param string $str Input string
 * @return string Safely formatted string
 */
function encode_php_tags($str) {}
