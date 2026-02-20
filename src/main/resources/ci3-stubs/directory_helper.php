<?php
/**
 * CodeIgniter 3 Directory Helper (stub for IDE).
 * Load with: $this->load->helper('directory');
 * @see https://codeigniter.com/userguide3/helpers/directory_helper.html
 */

/**
 * Create a map of a directory (files and subdirectories). Paths are relative to the main index.php.
 *
 * @param string $source_dir      Path to the source directory (e.g. './mydirectory/')
 * @param int    $directory_depth Depth of directories to traverse: 0 = fully recursive, 1 = top level only, etc.
 * @param bool   $hidden          Whether to include hidden files/directories (default FALSE)
 * @return array Map of the directory: folder names as array keys (with trailing '/'), their contents as sub-arrays; file names as numerically indexed values
 */
function directory_map($source_dir, $directory_depth = 0, $hidden = false) {}
