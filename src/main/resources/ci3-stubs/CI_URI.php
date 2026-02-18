<?php
/**
 * Stub CI_URI for PhpStorm – CodeIgniter 3 URI class.
 * The URI class is initialized automatically by the system ($this->uri).
 *
 * @see https://codeigniter.com/userguide3/libraries/uri.html
 */
class CI_URI
{
    /**
     * Retrieve a specific segment. Segments are numbered from left to right (1-based).
     *
     * @param int $n Segment index number
     * @param mixed $no_result What to return if the searched segment is not found
     * @return mixed Segment value or $no_result value if not found
     */
    public function segment($n, $no_result = null) {}

    /**
     * Same as segment(), but retrieves a segment from the re-routed URI (when using URI Routing).
     *
     * @param int $n Segment index number
     * @param mixed $no_result What to return if the searched segment is not found
     * @return mixed Routed segment value or $no_result value if not found
     */
    public function rsegment($n, $no_result = null) {}

    /**
     * Same as segment(), but adds trailing and/or leading slash. $where: 'trailing', 'leading', or 'both'.
     *
     * @param int $n Segment index number
     * @param string $where Where to add the slash ('trailing', 'leading', or 'both')
     * @return string Segment with slash(es), or '/' if segment not found
     */
    public function slash_segment($n, $where = 'trailing') {}

    /**
     * Same as slash_segment(), but for the re-routed URI.
     *
     * @param int $n Segment index number
     * @param string $where Where to add the slash ('trailing', 'leading', or 'both')
     * @return string Routed segment with slash(es), or '/' if not found
     */
    public function slash_rsegment($n, $where = 'trailing') {}

    /**
     * Turn URI segments into an associative array of key/value pairs (e.g. name/joe/location/UK → ['name'=>'joe','location'=>'UK']).
     *
     * @param int $n Segment index to start from (default 3, after controller/method)
     * @param array $default Default keys so the array always contains expected indexes
     * @return array Associative URI segments array
     */
    public function uri_to_assoc($n = 3, $default = []) {}

    /**
     * Same as uri_to_assoc(), but uses the re-routed URI.
     *
     * @param int $n Segment index to start from (default 3)
     * @param array $default Default keys
     * @return array Associative routed URI segments array
     */
    public function ruri_to_assoc($n = 3, $default = []) {}

    /**
     * Takes an associative array and generates a URI string (e.g. ['product'=>'shoes','size'=>'large'] → product/shoes/size/large).
     *
     * @param array $array Input array of key/value pairs
     * @return string URI string
     */
    public function assoc_to_uri($array) {}

    /**
     * Returns the complete URI string (e.g. news/local/345).
     *
     * @return string Full URI string
     */
    public function uri_string() {}

    /**
     * Same as uri_string(), but returns the re-routed URI.
     *
     * @return string Routed URI string
     */
    public function ruri_string() {}

    /**
     * Returns the total number of segments.
     *
     * @return int Number of segments
     */
    public function total_segments() {}

    /**
     * Same as total_segments(), but for the re-routed URI.
     *
     * @return int Count of routed URI segments
     */
    public function total_rsegments() {}

    /**
     * Returns an array containing all URI segments.
     *
     * @return array URI segments array (1-based indices)
     */
    public function segment_array() {}

    /**
     * Same as segment_array(), but for the re-routed URI.
     *
     * @return array Routed URI segments array
     */
    public function rsegment_array() {}
}
