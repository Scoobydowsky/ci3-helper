<?php
/**
 * Stub CI_Zip for PhpStorm – CodeIgniter 3 Zip Encoding library.
 * Used so that $this->zip->method() gets parameter hints when load->library('zip') is used.
 *
 * @see https://codeigniter.com/userguide3/libraries/zip.html
 *
 * @property int $compression_level Compression level 0–9 (default 2)
 */
class CI_Zip
{
    /**
     * Adds data to the Zip archive. Single file or array of file => data pairs.
     *
     * @param string|array $filepath File path/name, or array of filepath => data
     * @param string|null $data File contents (ignored if $filepath is array)
     * @return void
     */
    public function add_data($filepath, $data = null) {}

    /**
     * Adds a directory (or array of directories) to the archive.
     *
     * @param string|array $directory Directory name(s)
     * @return void
     */
    public function add_dir($directory): void {}

    /**
     * Reads an existing file and adds it to the archive.
     *
     * @param string $path Path to file
     * @param bool|string $archive_filepath TRUE = keep path, FALSE = flat, or new path string
     * @return bool TRUE on success, FALSE on failure
     */
    public function read_file($path, $archive_filepath = false): bool {}

    /**
     * Reads a directory recursively and adds it to the archive.
     *
     * @param string $path Path to directory
     * @param bool $preserve_filepath Whether to maintain the original path
     * @param string|null $root_path Part of path to exclude from archive
     * @return bool TRUE on success, FALSE on failure
     */
    public function read_dir($path, $preserve_filepath = true, $root_path = null): bool {}

    /**
     * Writes the Zip file to a path on the server.
     *
     * @param string $filepath Path to target zip archive (e.g. /path/to/my.zip)
     * @return bool TRUE on success, FALSE on failure
     */
    public function archive($filepath): bool {}

    /**
     * Sends the Zip file as a download to the browser.
     *
     * @param string $filename Archive file name (e.g. 'backup.zip')
     * @return void
     */
    public function download($filename = 'backup.zip'): void {}

    /**
     * Returns the Zip-compressed file data as a string.
     *
     * @return string Zip file content
     */
    public function get_zip() {}

    /**
     * Clears the cached zip data (use when creating multiple archives).
     *
     * @return void
     */
    public function clear_data() {}
}
