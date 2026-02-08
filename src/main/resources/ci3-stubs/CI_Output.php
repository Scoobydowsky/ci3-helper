<?php
/**
 * Stub CI_Output for PhpStorm – CodeIgniter 3 Output class.
 */
class CI_Output
{
    /**
     * @param string $output Output data
     * @return CI_Output
     */
    public function set_output($output) {}

    /**
     * @param string $output Append output
     * @return CI_Output
     */
    public function append_output($output) {}

    /**
     * @param string $header Header line
     * @param bool $replace Replace previous same header
     * @return CI_Output
     */
    public function set_header($header, $replace = true) {}

    /**
     * @param int $code HTTP status code (e.g. 404)
     * @return CI_Output
     */
    public function set_status_header($code = 200, $text = '') {}

    /** @return void */
    public function _display($output = '') {}
}
