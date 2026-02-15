<?php
/**
 * Stub CI_Xmlrpc for PhpStorm – CodeIgniter 3 XML-RPC client library.
 * Load with $this->load->library('xmlrpc') → $this->xmlrpc.
 *
 * @see https://codeigniter.com/userguide3/libraries/xmlrpc.html
 */
class CI_Xmlrpc
{
    /**
     * Initializes the XML-RPC library.
     *
     * @param array $config Configuration data
     * @return void
     */
    public function initialize($config = []) {}

    /**
     * Sets the URL and port of the server to which a request is to be sent.
     *
     * @param string $url XML-RPC server URL (basic HTTP auth: http://user:pass@host/)
     * @param int $port Server port (default 80)
     * @param bool|string $proxy Optional proxy (default FALSE)
     * @param int $proxy_port Proxy listening port (default 8080)
     * @return void
     */
    public function server($url, $port = 80, $proxy = false, $proxy_port = 8080) {}

    /**
     * Set a timeout (in seconds) after which the request will be canceled.
     *
     * @param int $seconds Timeout in seconds (default 5)
     * @return void
     */
    public function timeout($seconds = 5) {}

    /**
     * Sets the method to be requested from the XML-RPC server.
     *
     * @param string $function Method name
     * @return void
     */
    public function method($function) {}

    /**
     * Takes an array of data and builds the request to be sent to the XML-RPC server.
     *
     * @param array $incoming Request data (strings or [value, 'type'] e.g. ['int','boolean','struct'])
     * @return void
     */
    public function request($incoming) {}

    /**
     * Sends the request. Set server, method, request, and optionally timeout before calling.
     *
     * @return bool TRUE on success, FALSE on failure
     */
    public function send_request() {}

    /**
     * Returns the error message string if the request failed.
     *
     * @return string Error message
     */
    public function display_error() {}

    /**
     * Returns the response from the remote server (typically an associative array).
     *
     * @return mixed Response data
     */
    public function display_response() {}

    /**
     * Send an error message from your server to the client (when acting as XML-RPC server).
     *
     * @param int|string $number Error number
     * @param string $message Error message
     * @return object XML_RPC_Response instance
     */
    public function send_error_message($number, $message) {}

    /**
     * Send a response back to the client (when acting as XML-RPC server).
     *
     * @param array $response Response array (e.g. array(array('key' => array('value','string')), 'struct'))
     * @return object XML_RPC_Response instance
     */
    public function send_response($response) {}
}
