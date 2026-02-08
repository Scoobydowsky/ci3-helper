<?php
/**
 * Stub CI_URI for PhpStorm – CodeIgniter 3 URI class.
 */
class CI_URI
{
    /** @return array URI segments */
    public function segment_array() {}

    /**
     * @param int $n Segment index (1-based)
     * @param mixed $no_result Default if not found
     * @return mixed
     */
    public function segment($n, $no_result = null) {}

    /** @return string Full URI string */
    public function uri_string() {}

    /** @return int Number of segments */
    public function total_segments() {}

    /** @return int Current segment index */
    public function rsegment($n, $no_result = null) {}
}
