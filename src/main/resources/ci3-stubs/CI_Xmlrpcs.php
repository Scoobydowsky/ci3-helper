<?php
/**
 * Stub CI_Xmlrpcs for PhpStorm – CodeIgniter 3 XML-RPC Server library.
 * Load with $this->load->library('xmlrpc') and $this->load->library('xmlrpcs') → $this->xmlrpcs.
 *
 * @see https://codeigniter.com/userguide3/libraries/xmlrpc.html
 */
class CI_Xmlrpcs
{
    /**
     * Initializes the XML-RPC Server with configuration (functions map, object, debug, xss_clean).
     *
     * @param array $config e.g. ['functions' => ['MethodName' => ['function' => 'ClassName.methodName']], 'object' => $this]
     * @return void
     */
    public function initialize($config = []) {}

    /**
     * Starts the XML-RPC server and processes the incoming request.
     *
     * @return void
     */
    public function serve() {}
}
