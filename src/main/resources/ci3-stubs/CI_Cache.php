<?php
/**
 * Stub CI_Cache for PhpStorm – CodeIgniter 3 Cache Driver library.
 * Used so that $this->cache->method() gets parameter hints when load->driver('cache') is used.
 *
 * @see https://codeigniter.com/userguide3/libraries/caching.html
 *
 * @property CI_Cache_driver $apc APC cache driver
 * @property CI_Cache_driver $file File-based cache driver
 * @property CI_Cache_driver $memcached Memcached cache driver
 * @property CI_Cache_driver $wincache WinCache cache driver
 * @property CI_Cache_driver $redis Redis cache driver
 * @property CI_Cache_driver $dummy Dummy cache driver (always misses)
 */
class CI_Cache
{
    /**
     * Check if a caching driver is supported in the hosting environment.
     * Automatically called when accessing drivers via $this->cache->get().
     *
     * @param string $driver The name of the caching driver
     * @return bool TRUE if supported, FALSE if not
     */
    public function is_supported($driver) {}

    /**
     * Fetch an item from the cache store.
     *
     * @param string $id Cache item name
     * @return mixed|false Item value or FALSE if not found
     */
    public function get($id) {}

    /**
     * Save an item to the cache store.
     *
     * @param string $id Cache item name
     * @param mixed $data The data to save
     * @param int $ttl Time To Live, in seconds (default: 60)
     * @param bool $raw Whether to store the raw value (for increment/decrement) - only for APC and Memcache
     * @return bool TRUE on success, FALSE on failure
     */
    public function save($id, $data, $ttl = 60, $raw = false) {}

    /**
     * Delete a specific item from the cache store.
     *
     * @param string $id Name of cached item
     * @return bool TRUE on success, FALSE on failure
     */
    public function delete($id) {}

    /**
     * Perform atomic incrementation of a raw stored value.
     *
     * @param string $id Cache ID
     * @param int $offset Step/value to add (default: 1)
     * @return mixed|false New value on success, FALSE on failure
     */
    public function increment($id, $offset = 1) {}

    /**
     * Perform atomic decrementation of a raw stored value.
     *
     * @param string $id Cache ID
     * @param int $offset Step/value to reduce by (default: 1)
     * @return mixed|false New value on success, FALSE on failure
     */
    public function decrement($id, $offset = 1) {}

    /**
     * Clean the entire cache.
     *
     * @return bool TRUE on success, FALSE on failure
     */
    public function clean() {}

    /**
     * Get information on the entire cache database.
     *
     * @return mixed Information on the entire cache (structure depends on adapter)
     */
    public function cache_info() {}

    /**
     * Get detailed information on a specific item in the cache.
     *
     * @param string $id Cache item name
     * @return mixed Metadata for the cached item (structure depends on adapter)
     */
    public function get_metadata($id) {}
}

/**
 * Base stub for Cache Driver implementations.
 * All cache drivers (apc, file, memcached, wincache, redis, dummy) implement these methods.
 */
class CI_Cache_driver
{
    /**
     * Check if this driver is supported in the hosting environment.
     *
     * @return bool TRUE if supported, FALSE if not
     */
    public function is_supported() {}

    /**
     * Fetch an item from the cache store.
     *
     * @param string $id Cache item name
     * @return mixed|false Item value or FALSE if not found
     */
    public function get($id) {}

    /**
     * Save an item to the cache store.
     *
     * @param string $id Cache item name
     * @param mixed $data The data to save
     * @param int $ttl Time To Live, in seconds (default: 60)
     * @param bool $raw Whether to store the raw value (for increment/decrement) - only for APC and Memcache
     * @return bool TRUE on success, FALSE on failure
     */
    public function save($id, $data, $ttl = 60, $raw = false) {}

    /**
     * Delete a specific item from the cache store.
     *
     * @param string $id Name of cached item
     * @return bool TRUE on success, FALSE on failure
     */
    public function delete($id) {}

    /**
     * Perform atomic incrementation of a raw stored value.
     *
     * @param string $id Cache ID
     * @param int $offset Step/value to add (default: 1)
     * @return mixed|false New value on success, FALSE on failure
     */
    public function increment($id, $offset = 1) {}

    /**
     * Perform atomic decrementation of a raw stored value.
     *
     * @param string $id Cache ID
     * @param int $offset Step/value to reduce by (default: 1)
     * @return mixed|false New value on success, FALSE on failure
     */
    public function decrement($id, $offset = 1) {}

    /**
     * Clean the entire cache.
     *
     * @return bool TRUE on success, FALSE on failure
     */
    public function clean() {}

    /**
     * Get information on the entire cache database.
     *
     * @return mixed Information on the entire cache (structure depends on adapter)
     */
    public function cache_info() {}

    /**
     * Get detailed information on a specific item in the cache.
     *
     * @param string $id Cache item name
     * @return mixed Metadata for the cached item (structure depends on adapter)
     */
    public function get_metadata($id) {}
}
