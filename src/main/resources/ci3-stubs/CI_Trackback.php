<?php
/**
 * Stub CI_Trackback for PhpStorm – CodeIgniter 3 Trackback library.
 * Used so that $this->trackback->method() gets parameter hints when load->library('trackback') is used.
 *
 * @see https://codeigniter.com/userguide3/libraries/trackback.html
 *
 * @property array $data Trackback data array ('url' => '', 'title' => '', 'excerpt' => '', 'blog_name' => '', 'charset' => '')
 * @property bool $convert_ascii Whether to convert high ASCII and MS Word characters to HTML entities (default: TRUE)
 */
class CI_Trackback
{
    /**
     * Send a trackback to one or more URLs.
     *
     * @param array $tb_data Trackback data array with keys:
     *   - 'ping_url' (string) - URL(s) to send trackback to (comma-separated for multiple)
     *   - 'url' (string) - URL to YOUR site where the entry can be seen
     *   - 'title' (string) - Title of your entry
     *   - 'excerpt' (string) - Content of your entry (max 500 chars, HTML stripped)
     *   - 'blog_name' (string) - Name of your weblog
     *   - 'charset' (string) - Character encoding (default: 'utf-8')
     * @return bool TRUE on success, FALSE on failure
     */
    public function send($tb_data) {}

    /**
     * Validate incoming trackback data.
     * Sets valid data to $this->data array if successful.
     *
     * @return bool TRUE on success, FALSE on failure
     */
    public function receive() {}

    /**
     * Send an error response to a trackback request.
     * Note: This method will terminate script execution.
     *
     * @param string $message Error message (default: 'Incomplete information')
     * @return void
     */
    public function send_error($message = 'Incomplete information') {}

    /**
     * Send a success response to a trackback request.
     * Note: This method will terminate script execution.
     *
     * @return void
     */
    public function send_success() {}

    /**
     * Get a single item from the trackback data array.
     *
     * @param string $item Data key ('url', 'title', 'excerpt', or 'blog_name')
     * @return string Data value or empty string if not found
     */
    public function data($item) {}

    /**
     * Open a socket connection and pass data to the server.
     *
     * @param string $url Target URL
     * @param string $data Raw POST data
     * @return bool TRUE on success, FALSE on failure
     */
    public function process($url, $data) {}

    /**
     * Extract URLs from a comma or space-separated string into an array.
     * Used for sending multiple trackbacks.
     *
     * @param string $urls Comma or space-separated URL list
     * @return array Array of URLs
     */
    public function extract_urls($urls) {}

    /**
     * Validate and normalize a trackback URL.
     * Adds http:// prefix if not already present.
     *
     * @param string $url Trackback URL (passed by reference)
     * @return void
     */
    public function validate_url(&$url) {}

    /**
     * Find and return a trackback URL's ID.
     *
     * @param string $url Trackback URL
     * @return string|false URL ID or FALSE on failure
     */
    public function get_id($url) {}

    /**
     * Convert reserved XML characters to entities.
     *
     * @param string $str Input string
     * @return string Converted string
     */
    public function convert_xml($str) {}

    /**
     * Limit string length based on character count, preserving complete words.
     *
     * @param string $str Input string
     * @param int $n Max characters number (default: 500)
     * @param string $end_char Character to put at end of string (default: '&#8230;')
     * @return string Shortened string
     */
    public function limit_characters($str, $n = 500, $end_char = '&#8230;') {}

    /**
     * Convert high ASCII text and MS Word special characters to HTML entities.
     *
     * @param string $str Input string
     * @return string Converted string
     */
    public function convert_ascii($str) {}

    /**
     * Set and log an error message.
     *
     * @param string $msg Error message
     * @return void
     */
    public function set_error($msg) {}

    /**
     * Display error messages formatted in HTML.
     *
     * @param string $open Open tag (default: ' ')
     * @param string $close Close tag (default: ' ')
     * @return string HTML formatted error messages or empty string if no errors
     */
    public function display_errors($open = ' ', $close = ' ') {}
}
