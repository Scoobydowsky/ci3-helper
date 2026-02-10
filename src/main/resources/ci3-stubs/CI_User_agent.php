<?php
/**
 * Stub CI_User_agent for PhpStorm – CodeIgniter 3 User Agent library.
 * Used so that $this->agent->method() gets parameter hints when load->library('user_agent') is used.
 *
 * @see https://codeigniter.com/userguide3/libraries/user_agent.html
 */
class CI_User_agent
{
    /**
     * Check if the user agent is a known web browser.
     *
     * @param string|null $key Optional browser name (e.g. 'Safari')
     * @return bool TRUE if the user agent is a (specified) browser, FALSE if not
     */
    public function is_browser($key = null) {}

    /**
     * Check if the user agent is a known mobile device.
     *
     * @param string|null $key Optional mobile device name (e.g. 'iphone')
     * @return bool TRUE if the user agent is a (specified) mobile device, FALSE if not
     */
    public function is_mobile($key = null) {}

    /**
     * Check if the user agent is a known robot.
     *
     * @param string|null $key Optional robot name
     * @return bool TRUE if the user agent is a (specified) robot, FALSE if not
     */
    public function is_robot($key = null) {}

    /**
     * Check if the user agent was referred from another site.
     *
     * @return bool TRUE if the user agent is a referral, FALSE if not
     */
    public function is_referral() {}

    /**
     * Get the name of the web browser viewing your site.
     *
     * @return string Detected browser name or empty string
     */
    public function browser() {}

    /**
     * Get the version number of the web browser viewing your site.
     *
     * @return string Detected browser version or empty string
     */
    public function version() {}

    /**
     * Get the name of the mobile device viewing your site.
     *
     * @return string Detected mobile device brand or empty string
     */
    public function mobile() {}

    /**
     * Get the name of the robot viewing your site.
     *
     * @return string Detected robot name or empty string
     */
    public function robot() {}

    /**
     * Get the platform/operating system viewing your site (Linux, Windows, OS X, etc.).
     *
     * @return string Detected operating system or empty string
     */
    public function platform() {}

    /**
     * Get the referrer URL if the user agent was referred from another site.
     *
     * @return string Detected referrer or empty string
     */
    public function referrer() {}

    /**
     * Get the full user agent string.
     *
     * @return string Full user agent string or empty string
     */
    public function agent_string() {}

    /**
     * Check if the user agent accepts a particular language.
     *
     * @param string $lang Language key (default: 'en')
     * @return bool TRUE if provided language is accepted, FALSE if not
     */
    public function accept_lang($lang = 'en') {}

    /**
     * Get an array of languages supported by the user agent.
     *
     * @return array List of accepted languages
     */
    public function languages() {}

    /**
     * Check if the user agent accepts a particular character set.
     *
     * @param string $charset Character set (default: 'utf-8')
     * @return bool TRUE if the character set is accepted, FALSE if not
     */
    public function accept_charset($charset = 'utf-8') {}

    /**
     * Get an array of character sets accepted by the user agent.
     *
     * @return array List of accepted character sets
     */
    public function charsets() {}

    /**
     * Parse a custom user-agent string, different from the one reported by the current visitor.
     *
     * @param string $string A custom user-agent string
     * @return void
     */
    public function parse($string) {}
}
