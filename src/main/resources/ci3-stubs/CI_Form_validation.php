<?php
/**
 * Stub CI_Form_validation for PhpStorm – CodeIgniter 3 Form Validation.
 * @see https://codeigniter.com/userguide3/libraries/form_validation.html
 */
class CI_Form_validation
{
    /**
     * @param string|array $field Field name or array of rules
     * @param string $label Human name (optional)
     * @param string|array $rules Rules (if $field is string)
     * @return CI_Form_validation
     */
    public function set_rules($field, $label = '', $rules = '') {}

    /**
     * @return bool TRUE if validation passed
     */
    public function run($group = '') {}

    /**
     * @param string $field Field name
     * @param string $prefix Error prefix (e.g. '<p>')
     * @param string $suffix Error suffix (e.g. '</p>')
     * @return string
     */
    public function error($field = '', $prefix = '', $suffix = '') {}

    /** @return array */
    public function error_array() {}

    /**
     * @param string $field Field name
     * @return string
     */
    public function set_value($field = '', $default = '') {}

    /**
     * @param string $field Field name
     * @param string $default Default value
     * @return string
     */
    public function set_select($field = '', $value = '', $default = false) {}

    /**
     * @param string $field Field name
     * @param string $value Value to check
     * @param bool $default Default checked
     * @return string
     */
    public function set_checkbox($field = '', $value = '', $default = false) {}

    /**
     * @param string $field Field name
     * @param string $value Value to check
     * @param bool $default Default selected
     * @return string
     */
    public function set_radio($field = '', $value = '', $default = false) {}

    /**
     * @param string|array $lang Rule name or array of rule => message
     * @param string $val Message (when $lang is string)
     * @return CI_Form_validation
     */
    public function set_message($lang, $val = '') {}

    /**
     * Set data array to validate (instead of $_POST).
     * @param array $data
     * @return CI_Form_validation
     */
    public function set_data(array $data) {}

    /**
     * @param string $prefix HTML prefix for each error
     * @param string $suffix HTML suffix for each error
     * @return CI_Form_validation
     */
    public function set_error_delimiters($prefix = ' ', $suffix = ' ') {}

    /**
     * Get all error messages as string with delimiters.
     * @param string $prefix
     * @param string $suffix
     * @return string
     */
    public function error_string($prefix = '', $suffix = '') {}

    /** Clear rules and field data. Use before validating another set. @return void */
    public function reset_validation() {}

    /**
     * @param string $field Field name
     * @return bool Whether the field has a rule
     */
    public function has_rule($field) {}
}
