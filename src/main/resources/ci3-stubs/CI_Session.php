<?php
/**
 * Stub CI_Session for PhpStorm – CodeIgniter 3 Session library.
 * Used so that $this->session->method() gets parameter hints when load->library('session') is used.
 *
 * @see https://codeigniter.com/userguide3/libraries/sessions.html
 */
class CI_Session
{
    /**
     * Retrieve session userdata. Omit $key to get all userdata.
     * Does NOT return flashdata or tempdata.
     *
     * @param string|null $key Session item key
     * @return mixed Single value or array of all userdata, NULL if key not found
     */
    public function userdata($key = null) {}

    /**
     * Set session userdata. Pass array of key => value or (key, value).
     *
     * @param string|array $data Key or array of key => value
     * @param mixed $value Value (when $data is string)
     * @return void
     */
    public function set_userdata($data, $value = null) {}

    /**
     * Check if a userdata key exists.
     *
     * @param string $key Session item key
     * @return bool
     */
    public function has_userdata($key) {}

    /**
     * Remove session userdata by key(s).
     *
     * @param string|array $key Key or array of keys to unset
     * @return void
     */
    public function unset_userdata($key) {}

    /**
     * Retrieve flashdata. Available only for the next request. Omit $key to get all flashdata.
     *
     * @param string|null $key Flashdata key
     * @return mixed Single value or array of all flashdata, NULL if key not found
     */
    public function flashdata($key = null) {}

    /**
     * Set flashdata (available only for the next request). Array or (key, value).
     *
     * @param string|array $data Key or array of key => value
     * @param mixed $value Value (when $data is string)
     * @return void
     */
    public function set_flashdata($data, $value = null) {}

    /**
     * Mark an existing session item as flashdata (so it is cleared after next request).
     *
     * @param string|array $key Key or array of keys
     * @return void
     */
    public function mark_as_flash($key) {}

    /**
     * Keep flashdata for one more request (so it is not cleared on next request).
     *
     * @param string|array $key Key or array of keys
     * @return void
     */
    public function keep_flashdata($key) {}

    /**
     * Set tempdata (session data with expiration in seconds).
     *
     * @param string|array $key Key or array of key => value
     * @param mixed $value Value (when $key is string). Omit for array form.
     * @param int $ttl Time-to-live in seconds (default 300)
     * @return void
     */
    public function set_tempdata($key, $value = null, $ttl = 300) {}

    /**
     * Retrieve tempdata. Omit $key to get all tempdata.
     *
     * @param string|null $key Tempdata key
     * @return mixed Single value or array of all tempdata, NULL if key not found
     */
    public function tempdata($key = null) {}

    /**
     * Mark existing session item(s) as tempdata with optional TTL.
     * mark_as_temp('key', 300) or mark_as_temp(['key1','key2'], 300) or mark_as_temp(['key1'=>300,'key2'=>240]).
     *
     * @param string|array $key Key, array of keys, or array of key => ttl
     * @param int|null $ttl TTL in seconds (when $key is string or list of keys)
     * @return void
     */
    public function mark_as_temp($key, $ttl = null) {}

    /**
     * Remove tempdata by key before it expires.
     *
     * @param string $key Tempdata key
     * @return void
     */
    public function unset_tempdata($key) {}

    /**
     * Get the current session ID.
     *
     * @return string Session ID
     */
    public function session_id() {}

    /**
     * Destroy the current session (e.g. on logout). Must be last session operation in the request.
     *
     * @return void
     */
    public function sess_destroy() {}
}
