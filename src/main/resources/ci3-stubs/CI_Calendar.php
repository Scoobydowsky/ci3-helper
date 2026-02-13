<?php
/**
 * Stub CI_Calendar for PhpStorm – CodeIgniter 3 Calendaring class.
 * Load with $this->load->library('calendar') → $this->calendar.
 *
 * @see https://codeigniter.com/userguide3/libraries/calendar.html
 */
class CI_Calendar
{
    /**
     * Initializes the Calendaring preferences.
     *
     * @param array $config Configuration parameters (template, local_time, start_day, month_type, day_type, show_next_prev, next_prev_url, show_other_days)
     * @return CI_Calendar
     */
    public function initialize($config = []): CI_Calendar {}

    /**
     * Generate the calendar HTML.
     *
     * @param string|int $year  Year (empty = current)
     * @param string|int $month Month (empty = current)
     * @param array     $data  Data to be shown in the calendar cells (day number => content)
     * @return string HTML-formatted calendar
     */
    public function generate($year = '', $month = '', $data = []): string {}

    /**
     * Generates a textual month name based on the numeric month provided.
     *
     * @param int $month Month (1–12)
     * @return string Month name
     */
    public function get_month_name($month): string {}

    /**
     * Returns an array of day names (Sunday, Monday, etc.) based on the type provided.
     *
     * @param string $day_type 'long', 'short', or 'abr'
     * @return array Array of day names
     */
    public function get_day_names($day_type = ''): array {}

    /**
     * Ensures a valid month/year (e.g. month 13 becomes January of next year).
     *
     * @param int $month Month
     * @param int $year  Year
     * @return array Associative array with 'month' and 'year' keys
     */
    public function adjust_date($month, $year): array {}

    /**
     * Total days in a given month.
     *
     * @param int $month Month
     * @param int $year  Year
     * @return int Count of days in the specified month
     */
    public function get_total_days($month, $year): int {}

    /**
     * Sets the default template. Used when you have not created your own template.
     *
     * @return array An array of template values
     */
    public function default_template(): array {}

    /**
     * Harvests the data within the template {pseudo-variables} used to display the calendar.
     *
     * @return CI_Calendar
     */
    public function parse_template(): CI_Calendar {}
}
