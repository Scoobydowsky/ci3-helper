<?php
/**
 * Stub CI_Session for PhpStorm – CodeIgniter 3 Session library.
 * @see https://codeigniter.com/userguide3/libraries/sessions.html
 */
class CI_Session
{
    /**
     * @param string $key Session item key
     * @return mixed
     */
    public function userdata($key = null) {}

    /**
     * @param string|array $data Key or array of key => value
     * @param mixed $value Value (if $data is string)
     * @return void
     */
    public function set_userdata($data, $value = null) {}

    /**
     * @param string|array $key Key(s) to unset
     * @return void
     */
    public function unset_userdata($key) {}

    /**
     * @param string $key Flashdata key
     * @return mixed
     */
    public function flashdata($key = null) {}

    /**
     * @param string|array $data Key or array for flashdata
     * @param mixed $value Value (if $data is string)
     * @return void
     */
    public function set_flashdata($data, $value = null) {}

    /** @return string Session ID */
    public function userdata_id() {}

    /** @return void */
    public function sess_destroy() {}
}
