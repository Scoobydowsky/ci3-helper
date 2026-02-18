<?php
/**
 * Stub CI_Typography for PhpStorm – CodeIgniter 3 Typography library.
 * Load with $this->load->library('typography') → $this->typography.
 *
 * @see https://codeigniter.com/userguide3/libraries/typography.html
 */
class CI_Typography
{
    /**
     * When using with the Template Parser library, set to TRUE to protect
     * single and double quotes within curly braces.
     *
     * @var bool
     */
    public $protect_braced_quotes = false;

    /**
     * Formats text so that it is semantically and typographically correct HTML.
     * Handles paragraphs, line breaks, curly quotes, apostrophes, em-dashes, ellipsis, double spacing.
     *
     * @param string $str Input string
     * @param bool $reduce_linebreaks Whether to reduce consecutive line breaks to two
     * @return string HTML typography-safe string
     */
    public function auto_typography($str, $reduce_linebreaks = false) {}

    /**
     * Character conversion only: quotes, apostrophes, em-dashes, ellipsis, double spacing.
     * Does not wrap text in paragraph tags.
     *
     * @param string $str Input string
     * @return string Formatted string
     */
    public function format_characters($str) {}

    /**
     * Converts newlines to &lt;br /&gt; tags unless they appear within &lt;pre&gt; tags.
     *
     * @param string $str Input string
     * @return string Formatted string
     */
    public function nl2br_except_pre($str) {}
}
