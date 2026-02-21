<?php
/**
 * CodeIgniter 3 HTML Helper (stub for IDE).
 * Load with: $this->load->helper('html');
 * @see https://codeigniter.com/userguide3/helpers/html_helper.html
 */

/**
 * HTML heading tag
 *
 * @param string       $data       Content
 * @param string       $h          Heading level (default: '1')
 * @param array|string $attributes  HTML attributes (default: '')
 * @return string
 */
function heading($data = '', $h = '1', $attributes = '') {}

/**
 * HTML image tag
 *
 * @param string       $src        Image source
 * @param bool         $index_page Whether to add index_page to src (default: FALSE)
 * @param array|string $attributes HTML attributes (default: '')
 * @return string
 */
function img($src = '', $index_page = false, $attributes = '') {}

/**
 * HTML link tag (stylesheet, favicon, RSS, etc.)
 *
 * @param string $href      What we are linking to
 * @param string $rel        Relation type (default: 'stylesheet')
 * @param string $type      Type of the related document (default: 'text/css')
 * @param string $title     Link title (default: '')
 * @param string $media     Media type (default: '')
 * @param bool   $index_page Whether to add index_page to href (default: FALSE)
 * @return string
 */
function link_tag($href = '', $rel = 'stylesheet', $type = 'text/css', $title = '', $media = '', $index_page = false) {}

/**
 * HTML-formatted unordered list
 *
 * @param array        $list       List entries (simple or multi-dimensional)
 * @param array|string $attributes HTML attributes for the ul tag (default: '')
 * @return string
 */
function ul($list, $attributes = '') {}

/**
 * HTML-formatted ordered list (identical to ul() but produces ol tag)
 *
 * @param array        $list       List entries
 * @param array|string $attributes HTML attributes for the ol tag (default: '')
 * @return string
 */
function ol($list, $attributes = '') {}

/**
 * HTML meta tag
 *
 * @param string|array $name    Meta name (or array of meta definitions)
 * @param string       $content Meta content (default: '')
 * @param string       $type    Meta type: 'name' or 'equiv' (default: 'name')
 * @param string       $newline Newline character (default: "\n")
 * @return string
 */
function meta($name = '', $content = '', $type = 'name', $newline = "\n") {}

/**
 * HTML DocType tag
 *
 * @param string $type Doctype name (default: 'xhtml1-strict'). See application/config/doctypes.php for options (e.g. html5, html4-trans).
 * @return string
 */
function doctype($type = 'xhtml1-strict') {}

/**
 * HTML line break tag
 *
 * @param int $count Number of <br /> tags (default: 1)
 * @return string
 * @deprecated Use str_repeat('<br />', $count) instead
 */
function br($count = 1) {}

/**
 * Non-breaking space HTML entities
 *
 * @param int $num Number of &nbsp; to produce (default: 1)
 * @return string
 * @deprecated Use str_repeat('&nbsp;', $num) instead
 */
function nbs($num = 1) {}
