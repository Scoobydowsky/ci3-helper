<?php
/**
 * CodeIgniter 3 Typography Helper (stub for IDE).
 * Load with: $this->load->helper('typography');
 * @see https://codeigniter.com/userguide3/helpers/typography_helper.html
 */

/**
 * Formats text so that it is semantically and typographically correct HTML.
 * Converts newlines to paragraphs and applies proper typography formatting.
 * Alias for CI_Typography::auto_typography().
 *
 * @param string $str                 Input string
 * @param bool   $reduce_linebreaks   Whether to reduce multiple double newlines to two (default: false)
 * @return string HTML-formatted typography-safe string
 */
function auto_typography($str, $reduce_linebreaks = false) {}

/**
 * Converts newlines to &lt;br&gt; tags unless they appear within &lt;pre&gt; tags.
 * Identical to PHP's nl2br() except that it ignores content inside pre tags.
 *
 * @param string $str Input string
 * @return string String with HTML-formatted line breaks
 */
function nl2br_except_pre($str) {}

/**
 * Decodes HTML entities. Alias for CI_Security::entity_decode().
 *
 * @param string      $str      Input string
 * @param string|null $charset  Character set (default: null, uses config)
 * @return string String with decoded HTML entities
 */
function entity_decode($str, $charset = null) {}
