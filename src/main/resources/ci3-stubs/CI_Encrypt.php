<?php
/**
 * Stub CI_Encrypt for PhpStorm – CodeIgniter 3 Encrypt class (deprecated).
 * Load via: $this->load->library('encrypt'); then $this->encrypt->encode() / decode().
 * Prefer the Encryption library (load->library('encryption')) for new code.
 *
 * @see https://codeigniter.com/userguide3/libraries/encrypt.html
 */
class CI_Encrypt
{
    /**
     * Performs the data encryption and returns it as a string.
     *
     * @param string $string Data to encrypt
     * @param string $key Optional encryption key (uses config if empty)
     * @return string Encrypted string
     */
    public function encode($string, $key = '') {}

    /**
     * Decrypts an encoded string.
     *
     * @param string $string String to decrypt
     * @param string $key Optional encryption key (uses config if empty)
     * @return string Plain-text string
     */
    public function decode($string, $key = '') {}

    /**
     * Sets the Mcrypt cipher. Default: MCRYPT_RIJNDAEL_256.
     *
     * @param int $cipher Valid PHP MCrypt cipher constant (e.g. MCRYPT_BLOWFISH)
     * @return CI_Encrypt instance (method chaining)
     */
    public function set_cipher($cipher) {}

    /**
     * Sets the Mcrypt mode. Default: MCRYPT_MODE_CBC.
     *
     * @param int $mode Valid PHP MCrypt mode constant (e.g. MCRYPT_MODE_CFB)
     * @return CI_Encrypt instance (method chaining)
     */
    public function set_mode($mode) {}

    /**
     * Re-encodes data from CodeIgniter 1.x format to be compatible with CI 2.x/3.x.
     *
     * @param string $string String to encrypt
     * @param int $legacy_mode Mcrypt mode used for original data (default MCRYPT_MODE_ECB)
     * @param string $key Encryption key
     * @return string Newly encrypted string
     */
    public function encode_from_legacy($string, $legacy_mode = null, $key = '') {}
}
