<?php
/**
 * CodeIgniter 3 Path Helper (stub for IDE).
 * Load with: $this->load->helper('path');
 * @see https://codeigniter.com/userguide3/helpers/path_helper.html
 */

/**
 * Returns a server path without symbolic links or relative directory structures.
 *
 * @param string $path              Path to resolve
 * @param bool   $check_existence   Whether to trigger an error if the path cannot be resolved (default: false)
 * @return string Absolute path (e.g. '/etc/php5/apache2/php.ini' or '/path/to/nowhere' when not checking existence)
 */
function set_realpath($path, $check_existence = false) {}
