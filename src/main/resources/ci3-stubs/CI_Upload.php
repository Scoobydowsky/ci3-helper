<?php
/**
 * Stub CI_Upload for PhpStorm – CodeIgniter 3 File Uploading library.
 * Used so that $this->upload->method() gets parameter hints when load->library('upload') is used.
 *
 * @see https://codeigniter.com/userguide3/libraries/file_uploading.html
 */
class CI_Upload
{
    /** @var int Maximum file size (KB). 0 = no limit. */
    public $max_size = 0;
    /** @var int Maximum image width (px). 0 = no limit. */
    public $max_width = 0;
    /** @var int Maximum image height (px). 0 = no limit. */
    public $max_height = 0;
    /** @var int Minimum image width (px). 0 = no limit. */
    public $min_width = 0;
    /** @var int Minimum image height (px). 0 = no limit. */
    public $min_height = 0;
    /** @var int Maximum filename length. 0 = no limit. */
    public $max_filename = 0;
    /** @var int Max increment when overwrite is FALSE. */
    public $max_filename_increment = 100;
    /** @var string|array Allowed file types (e.g. 'gif|jpg|png' or array). */
    public $allowed_types = '';
    /** @var string Temporary file path. */
    public $file_temp = '';
    /** @var string Final filename. */
    public $file_name = '';
    /** @var string Original filename. */
    public $orig_name = '';
    /** @var string MIME type. */
    public $file_type = '';
    /** @var int|null File size in KB. */
    public $file_size;
    /** @var string File extension including dot. */
    public $file_ext = '';
    /** @var bool Force extension to lowercase. */
    public $file_ext_tolower = false;
    /** @var string Upload destination path. */
    public $upload_path = '';
    /** @var bool Overwrite existing file. */
    public $overwrite = false;
    /** @var bool Encrypt filename. */
    public $encrypt_name = false;
    /** @var bool Is image. */
    public $is_image = false;
    /** @var int|null Image width. */
    public $image_width;
    /** @var int|null Image height. */
    public $image_height;
    /** @var string Image type (e.g. jpeg). */
    public $image_type = '';
    /** @var string Image size string for img tag. */
    public $image_size_str = '';
    /** @var array Error messages. */
    public $error_msg = [];
    /** @var bool Remove spaces from filename. */
    public $remove_spaces = true;
    /** @var bool Detect MIME server-side. */
    public $detect_mime = true;
    /** @var bool XSS clean. */
    public $xss_clean = false;
    /** @var bool mod_mime fix for multiple extensions. */
    public $mod_mime_fix = true;
    /** @var string Client-supplied filename. */
    public $client_name = '';

    /**
     * Initialize preferences (e.g. after auto-load or to change config).
     *
     * @param array $config Preferences (upload_path, allowed_types, max_size, max_width, max_height, etc.)
     * @param bool $reset Whether to reset preferences not in $config to defaults
     * @return CI_Upload
     */
    public function initialize(array $config = [], $reset = true) {}

    /**
     * Perform the file upload. Field defaults to 'userfile'.
     *
     * @param string $field Name of the form file input
     * @return bool TRUE on success, FALSE on failure (use display_errors() for messages)
     */
    public function do_upload($field = 'userfile') {}

    /**
     * Get upload data array or a single key (file_name, file_type, file_path, full_path, raw_name, orig_name, client_name, file_ext, file_size, is_image, image_width, image_height, image_type, image_size_str).
     *
     * @param string|null $index Key to return, or NULL for full array
     * @return mixed Array of file data or single value
     */
    public function data($index = null) {}

    /**
     * Get formatted error messages. Does not echo; returns the string.
     *
     * @param string $open Opening HTML (default '<p>')
     * @param string $close Closing HTML (default '</p>')
     * @return string Formatted error string or empty
     */
    public function display_errors($open = '<p>', $close = '</p>') {}

    /**
     * Set upload path (trailing slash added).
     *
     * @param string $path Directory path
     * @return CI_Upload
     */
    public function set_upload_path($path) {}

    /**
     * Set final filename (handles overwrite/encrypt/increment).
     *
     * @param string $path Upload path
     * @param string $filename Desired filename
     * @return string|false Filename or FALSE on error
     */
    public function set_filename($path, $filename) {}

    /**
     * Set maximum file size in kilobytes.
     *
     * @param int $n Size in KB (0 = no limit)
     * @return CI_Upload
     */
    public function set_max_filesize($n) {}

    /**
     * Set maximum filename length.
     *
     * @param int $n Length (0 = no limit)
     * @return CI_Upload
     */
    public function set_max_filename($n) {}

    /**
     * Set maximum image width in pixels.
     *
     * @param int $n Width (0 = no limit)
     * @return CI_Upload
     */
    public function set_max_width($n) {}

    /**
     * Set maximum image height in pixels.
     *
     * @param int $n Height (0 = no limit)
     * @return CI_Upload
     */
    public function set_max_height($n) {}

    /**
     * Set minimum image width in pixels.
     *
     * @param int $n Width (0 = no limit)
     * @return CI_Upload
     */
    public function set_min_width($n) {}

    /**
     * Set minimum image height in pixels.
     *
     * @param int $n Height (0 = no limit)
     * @return CI_Upload
     */
    public function set_min_height($n) {}

    /**
     * Set allowed file types (string 'gif|jpg|png' or array or '*').
     *
     * @param mixed $types Types
     * @return CI_Upload
     */
    public function set_allowed_types($types) {}

    /**
     * Set image properties (width, height, type) from file.
     *
     * @param string $path Path to image file
     * @return CI_Upload
     */
    public function set_image_properties($path = '') {}

    /**
     * Enable/disable XSS cleaning of uploaded file.
     *
     * @param bool $flag TRUE to enable
     * @return CI_Upload
     */
    public function set_xss_clean($flag = false) {}

    /**
     * Whether the uploaded file is an image (gif, jpeg, png, webp).
     *
     * @return bool
     */
    public function is_image() {}

    /**
     * Whether the file type is allowed.
     *
     * @param bool $ignore_mime Skip MIME check
     * @return bool
     */
    public function is_allowed_filetype($ignore_mime = false) {}

    /**
     * Whether file size is within max_size.
     *
     * @return bool
     */
    public function is_allowed_filesize() {}

    /**
     * Whether image dimensions are within min/max width/height.
     *
     * @return bool
     */
    public function is_allowed_dimensions() {}

    /**
     * Validate that upload_path exists and is writable.
     *
     * @return bool
     */
    public function validate_upload_path() {}

    /**
     * Get file extension from filename (with leading dot).
     *
     * @param string $filename Filename
     * @return string Extension (e.g. '.jpg')
     */
    public function get_extension($filename) {}

    /**
     * Truncate filename to given length (keeping extension).
     *
     * @param string $filename Filename
     * @param int $length Max length
     * @return string
     */
    public function limit_filename_length($filename, $length) {}

    /**
     * Run file through XSS clean. Used internally when xss_clean is TRUE.
     *
     * @return bool TRUE if clean/safe
     */
    public function do_xss_clean() {}

    /**
     * Set error message(s). Usually used internally.
     *
     * @param string|array $msg Language key(s)
     * @param string $log_level Log level: 'error', 'debug', 'info'
     * @return CI_Upload
     */
    public function set_error($msg, $log_level = 'error') {}
}
