<?php
/**
 * Stub CI_Unit_test for PhpStorm – CodeIgniter 3 Unit Testing library.
 * Used so that $this->unit->method() gets parameter hints when load->library('unit_test') is used.
 *
 * @see https://codeigniter.com/userguide3/libraries/unit_testing.html
 */
class CI_Unit_test
{
    /**
     * Runs a unit test. Expected result can be a literal match or a data type (e.g. 'is_string', 'is_int').
     *
     * @param mixed $test Test data (result of the code to evaluate)
     * @param mixed $expected Expected result – literal value or type: is_string, is_int, is_float, is_bool, is_array, is_object, is_null, is_numeric, is_double, is_resource, is_true, is_false
     * @param string $test_name Optional name for the test
     * @param string $notes Optional notes
     * @return string Test report (when echoed/returned)
     */
    public function run($test, $expected = true, $test_name = 'undefined', $notes = '') {}

    /**
     * Generates a report about already complete tests (HTML table by default).
     *
     * @param array $result Array containing test results (omit to use internal results)
     * @return string Test report
     */
    public function report($result = []) {}

    /**
     * Returns raw test results as an array.
     *
     * @param array $results Tests results list (omit to use internal results)
     * @return array Raw result data
     */
    public function result($results = []) {}

    /**
     * Enables or disables strict type comparison (=== instead of ==).
     *
     * @param bool $state Strict state flag
     * @return void
     */
    public function use_strict($state = true) {}

    /**
     * Enables or disables unit testing (leave tests in code but skip execution).
     *
     * @param bool $state Whether to enable testing
     * @return void
     */
    public function active($state = true) {}

    /**
     * Sets which items are visible in test output. Valid: notes, line, file, result, res_datatype, test_datatype, test_name.
     *
     * @param array $items List of visible test items
     * @return void
     */
    public function set_test_items($items) {}

    /**
     * Sets the template for displaying test results. Use {rows} {item} {result} {/rows} etc.
     *
     * @param string $template Test result template
     * @return void
     */
    public function set_template($template) {}
}
