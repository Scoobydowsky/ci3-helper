<?php
/**
 * Stub CI_Ftp for PhpStorm – CodeIgniter 3 FTP library.
 * Load via: $this->load->library('ftp'); then $this->ftp->connect(), upload(), etc.
 * Note: SFTP and SSL FTP are not supported, only standard FTP.
 *
 * @see https://codeigniter.com/userguide3/libraries/ftp.html
 */
class CI_Ftp
{
    /**
     * Connects and logs into the FTP server.
     *
     * @param array $config Connection values (hostname, username, password, port, passive, debug)
     * @return bool TRUE on success, FALSE on failure
     */
    public function connect($config = []) {}

    /**
     * Uploads a file to the server.
     *
     * @param string $locpath Local file path
     * @param string $rempath Remote file path
     * @param string $mode FTP mode: 'auto', 'binary', or 'ascii' (default 'auto')
     * @param int|null $permissions File permissions (octal, e.g. 0775)
     * @return bool TRUE on success, FALSE on failure
     */
    public function upload($locpath, $rempath, $mode = 'auto', $permissions = null) {}

    /**
     * Downloads a file from the server.
     *
     * @param string $rempath Remote file path
     * @param string $locpath Local file path
     * @param string $mode FTP mode: 'auto', 'binary', or 'ascii' (default 'auto')
     * @return bool TRUE on success, FALSE on failure
     */
    public function download($rempath, $locpath, $mode = 'auto') {}

    /**
     * Renames a file on the server.
     *
     * @param string $old_file Old file name/path
     * @param string $new_file New file name/path
     * @param bool $move Whether a move is being performed (default FALSE)
     * @return bool TRUE on success, FALSE on failure
     */
    public function rename($old_file, $new_file, $move = false) {}

    /**
     * Moves a file. Supply source and destination paths.
     *
     * @param string $old_file Old file name/path
     * @param string $new_file New file name/path
     * @return bool TRUE on success, FALSE on failure
     */
    public function move($old_file, $new_file) {}

    /**
     * Deletes a file on the server.
     *
     * @param string $filepath Path to file to delete
     * @return bool TRUE on success, FALSE on failure
     */
    public function delete_file($filepath) {}

    /**
     * Deletes a directory and everything it contains (recursive). Use with caution.
     *
     * @param string $filepath Path to directory to delete (with trailing slash)
     * @return bool TRUE on success, FALSE on failure
     */
    public function delete_dir($filepath) {}

    /**
     * Retrieves a list of files in the given directory.
     *
     * @param string $path Directory path (default '.')
     * @return array|false Array of file names, or FALSE on failure
     */
    public function list_files($path = '.') {}

    /**
     * Recursively mirrors a local directory to the remote server.
     *
     * @param string $locpath Local path
     * @param string $rempath Remote path
     * @return bool TRUE on success, FALSE on failure
     */
    public function mirror($locpath, $rempath) {}

    /**
     * Creates a directory on the server.
     *
     * @param string $path Path ending with the folder name and trailing slash
     * @param int|null $permissions Permissions (octal, e.g. 0755)
     * @return bool TRUE on success, FALSE on failure
     */
    public function mkdir($path, $permissions = null) {}

    /**
     * Sets file/directory permissions.
     *
     * @param string $path Path to file or directory
     * @param int $perm Permissions (octal, e.g. 0755)
     * @return bool TRUE on success, FALSE on failure
     */
    public function chmod($path, $perm) {}

    /**
     * Changes the current working directory.
     *
     * @param string $path Directory path
     * @param bool $suppress_debug Whether to turn off debug messages for this command (default FALSE)
     * @return bool TRUE on success, FALSE on failure
     */
    public function changedir($path, $suppress_debug = false) {}

    /**
     * Closes the FTP connection.
     *
     * @return bool TRUE on success, FALSE on failure
     */
    public function close() {}
}
