<?php
/**
 * Stub CI_Parser for PhpStorm – CodeIgniter 3 Template Parser library.
 * Used so that $this->parser->method() gets parameter hints when load->library('parser') is used.
 *
 * @see https://codeigniter.com/userguide3/libraries/parser.html
 */
class CI_Parser
{
    /**
     * Parses a template from the provided path and variables.
     * Automatically sends output unless $return is TRUE.
     *
     * @param string $template Path to view file (without .php extension)
     * @param array $data Variable data (associative array)
     * @param bool $return Whether to only return the parsed template (default: FALSE)
     * @return string Parsed template string
     */
    public function parse($template, $data, $return = false) {}

    /**
     * Parses a template string directly (instead of loading a view file).
     * Works exactly like parse(), only it accepts the template as a string.
     *
     * @param string $template Template content to parse
     * @param array $data Variable data (associative array)
     * @param bool $return Whether to only return the parsed template (default: FALSE)
     * @return string Parsed template string
     */
    public function parse_string($template, $data, $return = false) {}

    /**
     * Sets the delimiters (opening and closing) for pseudo-variable tags in a template.
     * Default delimiters are '{' and '}'.
     *
     * @param string $l Left delimiter (default: '{')
     * @param string $r Right delimiter (default: '}')
     * @return void
     */
    public function set_delimiters($l = '{', $r = '}') {}
}
