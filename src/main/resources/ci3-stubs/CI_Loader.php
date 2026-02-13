<?php
/**
 * Stub CI_Loader for PhpStorm – CodeIgniter 3 Loader class.
 * Loads libraries, drivers, views, models, helpers, config, language files, and packages.
 * Access in controllers as $this->load (initialized automatically).
 *
 * @see https://codeigniter.com/userguide3/libraries/loader.html
 */
class CI_Loader
{
    /**
     * @param string|array $library Library name or array of names
     * @param array|null $params Optional config to pass to constructor
     * @param string|null $object_name Optional name on $this (e.g. 'my_calendar')
     * @return CI_Loader
     */
    public function library($library, $params = null, $object_name = null) {}

    /**
     * @param string|array $library Driver name or array
     * @param array|null $params Optional config
     * @param string|null $object_name Optional name on $this
     * @return CI_Loader
     */
    public function driver($library, $params = null, $object_name = null) {}

    /**
     * @param string $view View name (e.g. 'welcome_message' or 'folder/view')
     * @param array $vars Data to extract as variables in the view
     * @param bool $return TRUE to return as string, FALSE to output
     * @return string|CI_Loader
     */
    public function view($view, $vars = [], $return = false) {}

    /**
     * @param array|string $vars Variable name(s) or associative array
     * @param mixed $val Value (if $vars is a single name)
     * @return CI_Loader
     */
    public function vars($vars, $val = '') {}

    /**
     * @param string $key Variable name
     * @return mixed
     */
    public function get_var($key) {}

    /** @return array */
    public function get_vars() {}

    /** @return CI_Loader */
    public function clear_vars() {}

    /**
     * @param string|array $model Model name or array, e.g. 'blog/queries'
     * @param string $name Optional object name on $this (alias)
     * @param bool|string $db_conn Database config group name
     * @return CI_Loader
     */
    public function model($model, $name = '', $db_conn = false) {}

    /**
     * @param mixed $params Config group name or array of options
     * @param bool $return TRUE to return DB object
     * @param bool|null $query_builder Whether to load Query Builder
     * @return CI_Loader|object|false
     */
    public function database($params = '', $return = false, $query_builder = null) {}

    /**
     * @param object|null $db Database object
     * @param bool $return TRUE to return DB Forge instance
     * @return CI_Loader|object
     */
    public function dbforge($db = null, $return = false) {}

    /**
     * @param object|null $db Database object
     * @param bool $return TRUE to return DB Utility instance
     * @return CI_Loader|object
     */
    public function dbutil($db = null, $return = false) {}

    /**
     * @param string|array $helpers Helper name(s) without _helper.php
     * @return CI_Loader
     */
    public function helper($helpers) {}

    /**
     * @param string $path File path
     * @param bool $return TRUE to return file contents
     * @return string|CI_Loader
     */
    public function file($path, $return = false) {}

    /**
     * @param string|array $files Language file name(s)
     * @param string $lang Language code
     * @return CI_Loader
     */
    public function language($files, $lang = '') {}

    /**
     * @param string $file Config file name (without .php)
     * @param bool $use_sections Load into sub-arrays by section
     * @param bool $fail_gracefully Return FALSE on failure
     * @return bool
     */
    public function config($file, $use_sections = false, $fail_gracefully = false) {}

    /**
     * @param string $class Class/library name (e.g. 'Form_validation')
     * @return string|false Property name on $this if loaded, FALSE otherwise
     */
    public function is_loaded($class) {}

    /**
     * @param string $path Path to package (e.g. APPPATH.'third_party/foo_bar/')
     * @param bool $view_cascade Whether to cascade view paths
     * @return CI_Loader
     */
    public function add_package_path($path, $view_cascade = true) {}

    /**
     * @param string $path Path to remove (empty = remove last added)
     * @return CI_Loader
     */
    public function remove_package_path($path = '') {}

    /**
     * @param bool $include_base Include BASEPATH
     * @return array
     */
    public function get_package_paths($include_base = true) {}
}
