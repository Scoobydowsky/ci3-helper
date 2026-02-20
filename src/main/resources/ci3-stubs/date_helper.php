<?php
/**
 * CodeIgniter 3 Date Helper (stub for IDE).
 * Load with: $this->load->helper('date');
 * @see https://codeigniter.com/userguide3/helpers/date_helper.html
 */

/**
 * Returns the current time as a UNIX timestamp, optionally in the given timezone.
 *
 * @param string|null $timezone PHP supported timezone (e.g. 'Australia/Victoria'). NULL = use config time_reference / time()
 * @return int UNIX timestamp
 */
function now($timezone = null) {}

/**
 * Like PHP date() but uses MySQL-style date codes (e.g. %Y %m %d %h:%i %a). No need to escape non-format characters.
 *
 * @param string $datestr Date string with percent-prefixed codes: %Y %m %d %h %i %s %a etc.
 * @param int    $time    UNIX timestamp (default: current time)
 * @return string Formatted date
 */
function mdate($datestr = '', $time = '') {}

/**
 * Generate a date string in a standardized format (DATE_RFC822, DATE_ISO8601, etc.).
 *
 * @deprecated Use date() with DateTime format constants (e.g. date(DATE_RFC822, time())) instead.
 * @param string   $fmt  One of: DATE_ATOM, DATE_COOKIE, DATE_ISO8601, DATE_RFC822, DATE_RFC850, DATE_RFC1036, DATE_RFC1123, DATE_RFC2822, DATE_RSS, DATE_W3C
 * @param int|null $time UNIX timestamp (default: current time)
 * @return string|false Formatted date or FALSE on invalid format
 */
function standard_date($fmt = 'DATE_RFC822', $time = null) {}

/**
 * Convert a local UNIX timestamp to GMT.
 *
 * @param int $time UNIX timestamp (default: current time)
 * @return int UNIX timestamp in GMT
 */
function local_to_gmt($time = '') {}

/**
 * Convert a GMT UNIX timestamp to local time for the given timezone and optional DST.
 *
 * @param int    $time     UNIX timestamp (GMT)
 * @param string $timezone Timezone reference (e.g. 'UM8', 'UTC'). See CI3 date helper timezone reference.
 * @param bool   $dst      Whether Daylight Saving Time is active
 * @return int UNIX timestamp in local time
 */
function gmt_to_local($time = '', $timezone = 'UTC', $dst = false) {}

/**
 * Convert a MySQL timestamp (e.g. '20061124092345') to UNIX timestamp.
 *
 * @param string $time MySQL timestamp
 * @return int UNIX timestamp
 */
function mysql_to_unix($time = '') {}

/**
 * Convert UNIX timestamp to human-readable format (YYYY-MM-DD HH:MM:SS AM/PM).
 *
 * @param int    $time    UNIX timestamp (default: current time)
 * @param bool   $seconds Whether to include seconds
 * @param string $fmt     'us' (U.S.) or 'eu' (European) date format
 * @return string Formatted date string
 */
function unix_to_human($time = '', $seconds = false, $fmt = 'us') {}

/**
 * Convert human-readable date (as from unix_to_human) to UNIX timestamp.
 *
 * @param string $datestr Human-formatted date string
 * @return int|false UNIX timestamp or FALSE on failure
 */
function human_to_unix($datestr = '') {}

/**
 * Convert poorly-formed date strings into a useful format. Accepts many input formats.
 *
 * @deprecated Use PHP DateTime class instead.
 * @param int|string $bad_date Date-like string (e.g. '199605', '9-11-2001')
 * @param string|false $format  PHP date() format for output, or FALSE for UNIX timestamp return
 * @return string|int Formatted date string or UNIX timestamp
 */
function nice_date($bad_date = '', $format = false) {}

/**
 * Format a time span between two UNIX timestamps (e.g. "1 Year, 10 Months, 2 Weeks...").
 *
 * @param int    $seconds Earlier UNIX timestamp
 * @param int|string $time     Later UNIX timestamp (default: current time)
 * @param int    $units   Maximum number of time units to display (optional)
 * @return string Human-readable time difference
 */
function timespan($seconds = 1, $time = '', $units = '') {}

/**
 * Number of days in the given month/year. Accounts for leap years.
 *
 * @param int      $month Numeric month (1-12)
 * @param int|string $year  Numeric year (default: current year)
 * @return int Days in month
 */
function days_in_month($month = 0, $year = '') {}

/**
 * Return an array of dates within a specified period.
 *
 * @param int|string $unix_start Start of range (UNIX timestamp or date string if $is_unix false)
 * @param int|string $mixed      End timestamp or interval in days
 * @param bool       $is_unix    TRUE if $mixed is UNIX timestamp, FALSE if interval in days
 * @param string     $format     Output format (same as PHP date()), default 'Y-m-d'
 * @return array List of formatted date strings
 */
function date_range($unix_start = '', $mixed = '', $is_unix = true, $format = 'Y-m-d') {}

/**
 * Hours offset from UTC for the given timezone reference (e.g. 'UM5').
 *
 * @param string $tz Timezone reference (see CI3 timezone reference)
 * @return int Hour difference from UTC
 */
function timezones($tz = '') {}

/**
 * Generate HTML dropdown menu of timezones for user selection.
 *
 * @param string $default   Selected timezone value (e.g. 'UM8')
 * @param string $class     CSS class for the select element
 * @param string $name      Name attribute for the select (default 'timezones')
 * @param string|array $attributes Additional HTML attributes (string or associative array)
 * @return string HTML select element
 */
function timezone_menu($default = 'UTC', $class = '', $name = 'timezones', $attributes = '') {}
