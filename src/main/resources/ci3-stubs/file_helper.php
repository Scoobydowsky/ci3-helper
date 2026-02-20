<?php
/**
 * CodeIgniter 3 File Helper (stub for IDE).
 * Load with: $this->load->helper('file');
 * @see https://codeigniter.com/userguide3/helpers/file_helper.html
 */

/**
 * Read File
 *
 * Returns the contents of the file at the given path. Paths are relative to the main index.php.
 * Same behaviour as file_get_contents(). May not work with open_basedir restrictions.
 *
 * @param string $path File path
 * @return string|false File contents on success, FALSE on failure
 */
function read_file($path) {}

/**
 * Write File
 *
 * Writes data to the file specified in the path. Creates a new file if non-existent.
 *
 * @param string $path File path
 * @param string $data Data to write
 * @param string $mode fopen() mode (default: 'wb')
 * @return bool TRUE on success, FALSE on failure
 */
function write_file($path, $data, $mode = 'wb') {}

/**
 * Delete Files
 *
 * Deletes all files contained in the supplied directory path.
 * Files must be writable or owned by the system. If $del_dir is TRUE, subdirectories are removed too.
 * If $htdocs is TRUE, .htaccess and index page files are skipped.
 *
 * @param string $path    File path (directory)
 * @param bool   $del_dir Whether to delete any directories found in the path (default FALSE)
 * @param bool   $htdocs  Whether to skip deleting .htaccess and index page files (default FALSE)
 * @param int    $_level  Current directory depth level (internal use only, default 0)
 * @return bool TRUE on success, FALSE on failure
 */
function delete_files($path, $del_dir = false, $htdocs = false, $_level = 0) {}

/**
 * Get Filenames
 *
 * Reads the specified directory and builds an array containing the filenames.
 * Any sub-folders contained within the specified path are read as well.
 *
 * @param string $source_dir   Path to source directory
 * @param bool   $include_path Whether to include the path as part of the filename (default FALSE)
 * @param bool   $_recursion   Internal - do not use in calls (default FALSE)
 * @return array|false Array of filenames, or FALSE on failure
 */
function get_filenames($source_dir, $include_path = false, $_recursion = false) {}

/**
 * Get Directory File Information
 *
 * Reads the specified directory and builds an array containing the filenames,
 * filesize, dates, and permissions. Sub-folders are read as well if $top_level_only is FALSE.
 *
 * @param string $source_dir     Path to source directory
 * @param bool   $top_level_only Look only at the top level directory (default TRUE)
 * @param bool   $_recursion     Internal - do not use in calls (default FALSE)
 * @return array|false Array of file info arrays, or FALSE on failure
 */
function get_dir_file_info($source_dir, $top_level_only = true, $_recursion = false) {}

/**
 * Get File Info
 *
 * Given a file and path, returns name, path, size, date modified, etc.
 * Second parameter declares what information to return: name, server_path, size, date, readable, writable, executable, fileperms.
 * Returns FALSE if the file cannot be found.
 *
 * @param string       $file            Path to file
 * @param array|string $returned_values Array or comma-separated string of keys: name, server_path, size, date, readable, writable, executable, fileperms (default: name, server_path, size, date)
 * @return array|false Associative array of requested file info, or FALSE if file not found
 */
function get_file_info($file, $returned_values = array('name', 'server_path', 'size', 'date')) {}

/**
 * Get Mime by Extension
 *
 * Translates a file extension into a mime type based on config/mimes.php.
 * Not accurate for security; for convenience only.
 *
 * @param string $filename File name (extension is extracted)
 * @return string|false Mime type, or FALSE if unknown or mime config unavailable
 */
function get_mime_by_extension($filename) {}

/**
 * Symbolic Permissions
 *
 * Takes a numeric value representing a file's permissions and returns standard symbolic notation (e.g. drwxr-xr-x).
 *
 * @param int $perms Permissions (e.g. from fileperms())
 * @return string Symbolic notation string
 */
function symbolic_permissions($perms) {}

/**
 * Octal Permissions
 *
 * Takes a numeric value representing a file's permissions and returns a three character octal string (e.g. '755').
 *
 * @param int $perms Permissions (e.g. from fileperms())
 * @return string Three-character octal permissions
 */
function octal_permissions($perms) {}
