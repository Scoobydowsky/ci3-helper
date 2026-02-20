<?php
/**
 * CodeIgniter 3 Form Helper (stub for IDE).
 * Load with: $this->load->helper('form');
 * @see https://codeigniter.com/userguide3/helpers/form_helper.html
 */

/**
 * Form Open
 *
 * Creates an opening form tag with a base URL built from your config preferences.
 *
 * @param string       $action     Form action/target URI string (default: '')
 * @param array|string $attributes HTML attributes as array or string (default: '')
 * @param array        $hidden     Array of hidden fields' definitions (default: array())
 * @return string HTML form opening tag
 */
function form_open($action = '', $attributes = '', $hidden = array()) {}

/**
 * Form Open Multipart
 *
 * Identical to form_open() except that it adds a multipart attribute for file uploads.
 *
 * @param string       $action     Form action/target URI string (default: '')
 * @param array        $attributes HTML attributes (default: array())
 * @param array        $hidden     Array of hidden fields' definitions (default: array())
 * @return string HTML multipart form opening tag
 */
function form_open_multipart($action = '', $attributes = array(), $hidden = array()) {}

/**
 * Form Hidden
 *
 * Generates hidden input field(s). Pass name/value or associative array.
 *
 * @param string|array $name  Field name, or array of name => value
 * @param string|array $value Field value (default: '')
 * @return string HTML hidden input field tag(s)
 */
function form_hidden($name, $value = '') {}

/**
 * Form Input
 *
 * Generates a standard text input field.
 *
 * @param array|string $data  Field attributes data or field name (default: '')
 * @param string       $value Field value (default: '')
 * @param array|string $extra Extra attributes as array or string (default: '')
 * @return string HTML text input field tag
 */
function form_input($data = '', $value = '', $extra = '') {}

/**
 * Form Password
 *
 * Identical to form_input() except that it uses the "password" input type.
 *
 * @param array|string $data  Field attributes data or field name (default: '')
 * @param string       $value Field value (default: '')
 * @param array|string $extra Extra attributes as array or string (default: '')
 * @return string HTML password input field tag
 */
function form_password($data = '', $value = '', $extra = '') {}

/**
 * Form Upload
 *
 * Generates a file upload input field. Use with form_open_multipart().
 *
 * @param array|string $data  Field attributes data or field name (default: '')
 * @param string       $value Field value (default: '')
 * @param array|string $extra Extra attributes as array or string (default: '')
 * @return string HTML file upload input field tag
 */
function form_upload($data = '', $value = '', $extra = '') {}

/**
 * Form Textarea
 *
 * Generates a textarea. Use rows and cols instead of maxlength/size.
 *
 * @param array|string $data  Field attributes data or field name (default: '')
 * @param string       $value Field value (default: '')
 * @param array|string $extra Extra attributes as array or string (default: '')
 * @return string HTML textarea tag
 */
function form_textarea($data = '', $value = '', $extra = '') {}

/**
 * Form Dropdown
 *
 * Creates a standard drop-down field. Third parameter can be single value or array for multiple select.
 *
 * @param string       $name     Field name (default: '')
 * @param array        $options  Associative array of options (default: array())
 * @param array|string $selected Selected value(s) (default: array())
 * @param array|string $extra    Extra attributes as array or string (default: '')
 * @return string HTML select field tag
 */
function form_dropdown($name = '', $options = array(), $selected = array(), $extra = '') {}

/**
 * Form Multiselect
 *
 * Creates a multiselect field. Name should use POST array syntax (e.g. foo[]).
 *
 * @param string       $name     Field name (default: '')
 * @param array        $options  Associative array of options (default: array())
 * @param array|string $selected Selected value(s) (default: array())
 * @param array|string $extra    Extra attributes as array or string (default: '')
 * @return string HTML multiselect field tag
 */
function form_multiselect($name = '', $options = array(), $selected = array(), $extra = '') {}

/**
 * Form Fieldset
 *
 * Generates fieldset/legend opening tag.
 *
 * @param string       $legend_text Text for the legend (default: '')
 * @param array        $attributes  Attributes for the fieldset tag (default: array())
 * @return string HTML fieldset opening tag
 */
function form_fieldset($legend_text = '', $attributes = array()) {}

/**
 * Form Fieldset Close
 *
 * Produces a closing </fieldset> tag. Optional string is appended after the tag.
 *
 * @param string $extra Anything to append after the closing tag (default: '')
 * @return string HTML fieldset closing tag plus optional extra
 */
function form_fieldset_close($extra = '') {}

/**
 * Form Checkbox
 *
 * Generates a checkbox input field.
 *
 * @param array|string $data    Field attributes data or field name (default: '')
 * @param string       $value   Field value (default: '')
 * @param bool         $checked Whether the checkbox is checked (default: FALSE)
 * @param array|string $extra   Extra attributes as array or string (default: '')
 * @return string HTML checkbox input tag
 */
function form_checkbox($data = '', $value = '', $checked = false, $extra = '') {}

/**
 * Form Radio
 *
 * Identical to form_checkbox() except that it uses the "radio" input type.
 *
 * @param array|string $data    Field attributes data or field name (default: '')
 * @param string       $value   Field value (default: '')
 * @param bool         $checked Whether the radio is checked (default: FALSE)
 * @param array|string $extra   Extra attributes as array or string (default: '')
 * @return string HTML radio input tag
 */
function form_radio($data = '', $value = '', $checked = false, $extra = '') {}

/**
 * Form Label
 *
 * Generates a <label> tag.
 *
 * @param string       $label_text Text for the label (default: '')
 * @param string       $id         ID of the form element the label is for (default: '')
 * @param array|string $attributes HTML attributes (default: array())
 * @return string HTML label tag
 */
function form_label($label_text = '', $id = '', $attributes = array()) {}

/**
 * Form Submit
 *
 * Generates a submit button.
 *
 * @param array|string $data  Button name or attributes array (default: '')
 * @param string       $value Button value/label (default: '')
 * @param array|string $extra Extra attributes as array or string (default: '')
 * @return string HTML submit input tag
 */
function form_submit($data = '', $value = '', $extra = '') {}

/**
 * Form Reset
 *
 * Generates a reset button. Use is identical to form_submit().
 *
 * @param array|string $data  Button name or attributes array (default: '')
 * @param string       $value Button value/label (default: '')
 * @param array|string $extra Extra attributes as array or string (default: '')
 * @return string HTML reset input tag
 */
function form_reset($data = '', $value = '', $extra = '') {}

/**
 * Form Button
 *
 * Generates a <button> element.
 *
 * @param array|string $data    Button name or attributes array (default: '')
 * @param string       $content Button label (default: '')
 * @param array|string $extra   Extra attributes as array or string (default: '')
 * @return string HTML button tag
 */
function form_button($data = '', $content = '', $extra = '') {}

/**
 * Form Close
 *
 * Produces a closing </form> tag. Optional string is appended after the tag.
 *
 * @param string $extra Anything to append after the closing tag (default: '')
 * @return string HTML form closing tag plus optional extra
 */
function form_close($extra = '') {}

/**
 * Set Value
 *
 * Sets the value of an input form or textarea. Uses $_POST or Form Validation library if loaded.
 *
 * @param string $field       Field name
 * @param string $default     Default value (default: '')
 * @param bool   $html_escape Whether to HTML-escape the value (default: TRUE)
 * @return string Field value
 */
function set_value($field, $default = '', $html_escape = true) {}

/**
 * Set Select
 *
 * Returns 'selected' or '' for use in dropdown options to show the selected item.
 *
 * @param string $field   Select field name
 * @param string $value   Option value to check (default: '')
 * @param bool   $default Whether this value is also the default (default: FALSE)
 * @return string 'selected' attribute or empty string
 */
function set_select($field, $value = '', $default = false) {}

/**
 * Set Checkbox
 *
 * Returns 'checked' or '' for use in checkboxes to show the submitted state.
 *
 * @param string $field   Checkbox field name
 * @param string $value   Checkbox value to check (default: '')
 * @param bool   $default Whether this value is also the default (default: FALSE)
 * @return string 'checked' attribute or empty string
 */
function set_checkbox($field, $value = '', $default = false) {}

/**
 * Set Radio
 *
 * Returns 'checked' or '' for use in radio buttons. Identical to set_checkbox().
 *
 * @param string $field   Radio field name
 * @param string $value   Radio value to check (default: '')
 * @param bool   $default Whether this value is also the default (default: FALSE)
 * @return string 'checked' attribute or empty string
 */
function set_radio($field, $value = '', $default = false) {}

/**
 * Form Error
 *
 * Returns a validation error message from the Form Validation Library for the specified field.
 *
 * @param string $field  Field name (default: '')
 * @param string $prefix Error opening tag (default: '')
 * @param string $suffix Error closing tag (default: '')
 * @return string HTML-formatted form validation error message(s)
 */
function form_error($field = '', $prefix = '', $suffix = '') {}

/**
 * Validation Errors
 *
 * Returns all validation error messages from the Form Validation Library with optional wrapping tags.
 *
 * @param string $prefix Opening tag for each message (default: '')
 * @param string $suffix Closing tag for each message (default: '')
 * @return string HTML-formatted form validation error message(s)
 */
function validation_errors($prefix = '', $suffix = '') {}

/**
 * Form Prep
 *
 * Safely escape HTML/quotes for use in form elements. DEPRECATED - use html_escape() instead.
 *
 * @param string $str Value to escape
 * @return string Escaped value
 * @deprecated Use html_escape() from common functions instead
 */
function form_prep($str) {}
