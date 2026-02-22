<?php
/**
 * CodeIgniter 3 Smiley Helper (stub for IDE).
 * Load with: $this->load->helper('smiley');
 * @see https://codeigniter.com/userguide3/helpers/smiley_helper.html
 * @deprecated The Smiley helper is deprecated and kept for backwards compatibility only.
 */

/**
 * Returns an array containing smiley images wrapped in clickable links.
 * You must supply the URL to your smiley folder and a field id or field alias.
 *
 * @param string   $image_url URL path to the smileys directory
 * @param string   $alias     Field id or field alias (default: '')
 * @param array|null $smileys Optional array of smileys (default: NULL, uses application/config/smileys.php)
 * @return array Array of ready-to-use smiley link strings
 */
function get_clickable_smileys($image_url, $alias = '', $smileys = null) {}

/**
 * Generates the JavaScript that allows smiley images to be clicked and inserted into a form field.
 * Place the output in the &lt;head&gt; of your page. If you used an alias in get_clickable_smileys(),
 * pass the alias and the corresponding form field id here.
 *
 * @param string $alias   Field alias (default: '')
 * @param string $field_id Form field ID (default: '')
 * @param bool   $inline  Whether we're inserting an inline smiley (default: TRUE)
 * @return string Smiley-enabling JavaScript code
 */
function smiley_js($alias = '', $field_id = '', $inline = true) {}

/**
 * Takes a string of text and replaces plain text smileys (e.g. :-) ) with image equivalents.
 *
 * @param string   $str       Text containing smiley codes
 * @param string   $image_url URL path to the smileys directory (default: '')
 * @param array|null $smileys Optional array of smileys (default: NULL, uses application/config/smileys.php)
 * @return string Parsed string with smiley images
 */
function parse_smileys($str = '', $image_url = '', $smileys = null) {}
