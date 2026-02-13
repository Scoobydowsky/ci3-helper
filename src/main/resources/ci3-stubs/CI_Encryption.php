<?php
/**
 * Stub CI_Encryption for PhpStorm – CodeIgniter 3 Encryption library.
 * Load via: $this->load->library('encryption'); then $this->encryption->encrypt() / decrypt().
 *
 * @see https://codeigniter.com/userguide3/libraries/encryption.html
 */
class CI_Encryption
{
    /**
     * Initializes (configures) the library to use a different driver, cipher, mode or key.
     *
     * @param array $params Configuration parameters (driver, cipher, mode, key)
     * @return CI_Encryption instance (method chaining)
     */
    public function initialize($params = []) {}

    /**
     * Encrypts the input data and returns its ciphertext.
     *
     * @param string $data Data to encrypt
     * @param array|null $params Optional parameters (cipher, mode, key, hmac, hmac_digest, hmac_key, raw_data)
     * @return string|false Encrypted data or FALSE on failure
     */
    public function encrypt($data, $params = null) {}

    /**
     * Decrypts the input data and returns it in plain-text.
     *
     * @param string $data Data to decrypt
     * @param array|null $params Optional parameters (cipher, mode, key, hmac, hmac_digest, hmac_key, raw_data)
     * @return string|false Decrypted data or FALSE on failure
     */
    public function decrypt($data, $params = null) {}

    /**
     * Creates a cryptographic key by fetching random data from the OS (e.g. /dev/urandom).
     *
     * @param int $length Output length in bytes (e.g. 16 for AES-128)
     * @return string|false A pseudo-random cryptographic key, or FALSE on failure
     */
    public function create_key($length) {}

    /**
     * Derives a key from another key (HKDF – RFC 5869). Uses SHA-2 family only (no SHA1).
     *
     * @param string $key Input key material
     * @param string $digest A SHA-2 family digest algorithm (default 'sha512')
     * @param string|null $salt Optional salt
     * @param int|null $length Optional output length
     * @param string $info Optional context/application-specific info
     * @return string|false A pseudo-random key or FALSE on failure
     */
    public function hkdf($key, $digest = 'sha512', $salt = null, $length = null, $info = '') {}
}
