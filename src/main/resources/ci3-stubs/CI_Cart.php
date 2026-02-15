<?php
/**
 * Stub CI_Cart for PhpStorm – CodeIgniter 3 Shopping Cart library.
 * Load with $this->load->library('cart') → $this->cart.
 *
 * @see https://codeigniter.com/userguide3/libraries/cart.html
 *
 * @deprecated This library is deprecated and kept for backwards compatibility only.
 */
class CI_Cart
{
    /** @var string Regular expression rules for validating product ID (alpha-numeric, dashes, underscores, periods). */
    public $product_id_rules = '.a-z0-9_-';

    /** @var string Regular expression rules for product name (alpha-numeric, dashes, underscores, colons, periods). */
    public $product_name_rules = '\w \-.:';

    /** @var bool Whether to only allow safe product names. Default TRUE. */
    public $product_name_safe = true;

    /**
     * Insert items into the cart and save to the session.
     *
     * @param array $items Item(s) to insert. Required keys: id, qty, price, name. Optional: options (array).
     * @return bool|string TRUE on success, FALSE on failure; or rowid when inserting a single item
     */
    public function insert($items = []) {}

    /**
     * Update cart item(s). Array must contain rowid for each item.
     *
     * @param array $items Item(s) to update (e.g. ['rowid' => '...', 'qty' => 3])
     * @return bool TRUE on success, FALSE on failure
     */
    public function update($items = []) {}

    /**
     * Remove an item from the cart by row ID.
     *
     * @param string $rowid Row ID of the item to remove
     * @return bool TRUE on success, FALSE on failure
     */
    public function remove($rowid) {}

    /**
     * Get the total amount in the cart.
     *
     * @return float Total amount
     */
    public function total() {}

    /**
     * Get the total number of items in the cart.
     *
     * @return int Total number of items
     */
    public function total_items() {}

    /**
     * Returns an array of all cart contents.
     *
     * @param bool $newest_first Whether to sort with newest items first
     * @return array Cart contents
     */
    public function contents($newest_first = false) {}

    /**
     * Get item data for a specific row ID.
     *
     * @param string $row_id Row ID to retrieve
     * @return array|false Item data or FALSE if not found
     */
    public function get_item($row_id) {}

    /**
     * Check if a cart row has options.
     *
     * @param string $row_id Row ID to inspect
     * @return bool TRUE if options exist, FALSE otherwise
     */
    public function has_options($row_id = '') {}

    /**
     * Get product options for a cart row.
     *
     * @param string $row_id Row ID
     * @return array Product options
     */
    public function product_options($row_id = '') {}

    /**
     * Format a number (e.g. price) for display.
     *
     * @param string|float $n Number to format
     * @return string Formatted number
     */
    public function format_number($n) {}

    /**
     * Destroy the cart (clear all items).
     *
     * @return void
     */
    public function destroy() {}
}
