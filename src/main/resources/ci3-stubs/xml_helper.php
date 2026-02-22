<?php
/**
 * CodeIgniter 3 XML Helper (stub for IDE).
 * Load with: $this->load->helper('xml');
 * @see https://codeigniter.com/userguide3/helpers/xml_helper.html
 */

/**
 * Converts reserved XML characters to entities.
 * Converts: - ' " < > &
 * Ignores ampersands that are part of existing numbered entities, e.g. &#123;
 *
 * @param string $str         The text string to convert
 * @param bool   $protect_all Whether to protect all content that looks like a potential entity (e.g. &foo;) instead of just numbered entities (default: FALSE)
 * @return string XML-converted string
 */
function xml_convert($str, $protect_all = false) {}
