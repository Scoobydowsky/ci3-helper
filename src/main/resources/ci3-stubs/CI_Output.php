<?php
/**
 * Stub CI_Output for PhpStorm – CodeIgniter 3 Output class.
 * Core class, always available as $this->output in controllers.
 */
class CI_Output
{
    /**
     * Get the current output string (e.g. from load->view()).
     * @return string
     */
    public function get_output() {}

    /**
     * Set the final output string.
     * @param string $output Output data
     * @return CI_Output
     */
    public function set_output($output) {}

    /**
     * Append data to the existing output string.
     * @param string $output Append output
     * @return CI_Output
     */
    public function append_output($output) {}

    /**
     * Set an HTTP header (e.g. Cache-Control, Content-Type).
     * @param string $header Header line
     * @param bool $replace Replace previous same header
     * @return CI_Output
     */
    public function set_header($header, $replace = true) {}

    /**
     * Set HTTP response status (e.g. 404, 500).
     * @param int $code HTTP status code (e.g. 404)
     * @param string $text Optional status text
     * @return CI_Output
     */
    public function set_status_header($code = 200, $text = '') {}

    /**
     * Set the MIME type and optional charset (e.g. 'application/json', 'text/html').
     * @param string $mime_type MIME type
     * @param string|null $charset Character set (e.g. 'utf-8')
     * @return CI_Output
     */
    public function set_content_type($mime_type, $charset = null) {}

    /**
     * Enable or disable the profiler (debug bar).
     * @param bool $val Enable profiler
     * @return CI_Output
     */
    public function enable_profiler($val = true) {}

    /**
     * Set cache time in seconds for the output (internal use).
     * @param int $time Cache duration in seconds
     * @return CI_Output
     */
    public function set_cache($time) {}

    /**
     * Enable page caching for the current output. Page is cached in application/cache/.
     * Works only when output is generated via load->view(). $n = minutes to cache.
     * If you change config that affects output, delete cache manually (e.g. delete_cache() or clear application/cache/).
     * @see https://codeigniter.com/userguide3/general/caching.html
     * @param int $n Number of minutes the page should remain cached before refresh
     * @return CI_Output
     */
    public function cache($n) {}

    /**
     * Delete cached page(s). Call with no args for current URI, or pass URI path.
     * Use after config changes that affect output; otherwise old cached pages may be served.
     * @param string|null $uri URI segment to delete (e.g. '/foo/bar'), or null for current URI
     * @return void
     */
    public function delete_cache($uri = null) {}

    /** @return void */
    public function _display($output = '') {}
}
