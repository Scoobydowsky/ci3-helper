<?php
/**
 * CodeIgniter 3 Text Helper (stub for IDE).
 * Load with: $this->load->helper('text');
 * @see https://codeigniter.com/userguide3/helpers/text_helper.html
 */

/**
 * Truncates a string to the number of words specified. Optional suffix (default ellipsis).
 *
 * @param string $str       Input string
 * @param int    $limit     Word limit (default: 100)
 * @param string $end_char  End character, usually an ellipsis (default: '&#8230;')
 * @return string
 */
function word_limiter($str, $limit = 100, $end_char = '&#8230;') {}

/**
 * Truncates a string to the number of characters specified. Maintains word integrity.
 *
 * @param string $str       Input string
 * @param int    $n         Number of characters (default: 500)
 * @param string $end_char  End character (default: '&#8230;')
 * @return string
 */
function character_limiter($str, $n = 500, $end_char = '&#8230;') {}

/**
 * Converts ASCII values to character entities (high ASCII and MS Word chars) for consistent display/storage.
 *
 * @param string $str Input string
 * @return string
 */
function ascii_to_entities($str) {}

/**
 * Transliterates high ASCII characters to low ASCII equivalents (e.g. for URLs).
 * Uses application/config/foreign_chars.php.
 *
 * @param string $str Input string
 * @return string
 */
function convert_accented_characters($str) {}

/**
 * Censors words within a text string. Replaces disallowed words with replacement or ####.
 *
 * @param string $str         Original string
 * @param array  $censored    List of words to censor
 * @param string $replacement What to replace bad words with (default: '')
 * @return string
 */
function word_censor($str, $censored, $replacement = '') {}

/**
 * Colorizes a string of code (PHP, HTML, etc.) via PHP's highlight_string(). Colors from php.ini.
 *
 * @param string $str Input string (code)
 * @return string
 */
function highlight_code($str) {}

/**
 * Highlights a phrase within a text string with opening/closing HTML tags.
 *
 * @param string $str       Original string
 * @param string $phrase    Phrase to highlight
 * @param string $tag_open  Opening tag for the highlight (default: '<mark>')
 * @param string $tag_close Closing tag for the highlight (default: '</mark>')
 * @return string
 */
function highlight_phrase($str, $phrase, $tag_open = '<mark>', $tag_close = '</mark>') {}

/**
 * Wraps text at the specified character count while maintaining complete words.
 *
 * @param string $str     Input string
 * @param int    $charlim Character limit per line (default: 76)
 * @return string
 */
function word_wrap($str, $charlim = 76) {}

/**
 * Strips tags, splits at max length, and inserts an ellipsis. Position 0–1 (left to right).
 *
 * @param string $str        String to ellipsize
 * @param int    $max_length Character limit for final string
 * @param float  $position   Where ellipsis appears: 1=right, .5=middle, 0=left (default: 1)
 * @param string $ellipsis   Ellipsis character (default: '&hellip;')
 * @return string
 */
function ellipsize($str, $max_length, $position = 1, $ellipsis = '&hellip;') {}
