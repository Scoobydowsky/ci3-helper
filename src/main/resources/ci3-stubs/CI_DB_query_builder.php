<?php
/**
 * Stub CI_DB_query_builder for PhpStorm – CodeIgniter 3 Query Builder.
 * @see https://codeigniter.com/userguide3/database/query_builder.html
 */
class CI_DB_query_builder
{
    /**
     * @param string $select Field(s) to select (comma-separated or '*')
     * @param bool $escape Whether to escape identifiers
     * @return CI_DB_query_builder
     */
    public function select($select = '*', $escape = null) {}

    /**
     * @param string $from Table name
     * @return CI_DB_query_builder
     */
    public function from($from) {}

    /**
     * @param mixed $key Field name or associative array
     * @param mixed $value Value (if $key is string)
     * @param bool $escape Escape values
     * @return CI_DB_query_builder
     */
    public function where($key, $value = null, $escape = null) {}

    /**
     * @param string $field Field name
     * @param array $values Array of values
     * @return CI_DB_query_builder
     */
    public function where_in($field, $values) {}

    /**
     * @param mixed $field Field name or array of field => value
     * @param mixed $match Match value (if $field is string)
     * @param string $side 'both', 'none', 'before', 'after'
     * @return CI_DB_query_builder
     */
    public function like($field, $match = '', $side = 'both') {}

    /**
     * @param string $table Table to join
     * @param string $cond Join condition
     * @param string $type 'left', 'right', 'outer', 'inner', 'left outer', 'right outer'
     * @return CI_DB_query_builder
     */
    public function join($table, $cond, $type = '') {}

    /**
     * @param string $orderby Field or "field ASC/DESC"
     * @param string $direction 'ASC' or 'DESC'
     * @return CI_DB_query_builder
     */
    public function order_by($orderby, $direction = '') {}

    /**
     * @param string $by Group by clause
     * @param bool $escape Escape identifiers
     * @return CI_DB_query_builder
     */
    public function group_by($by, $escape = null) {}

    /**
     * @param int $value LIMIT value
     * @param int $offset OFFSET value
     * @return CI_DB_query_builder
     */
    public function limit($value, $offset = 0) {}

    /**
     * @param string|null $table Table name (optional if from() was used)
     * @param int|null $limit Limit
     * @param int|null $offset Offset
     * @return object Query result object
     */
    public function get($table = null, $limit = null, $offset = null) {}

    /**
     * @param string $table Table name
     * @param mixed $where Where key/value or array
     * @param int|null $limit Limit
     * @param int|null $offset Offset
     * @return object
     */
    public function get_where($table = '', $where = null, $limit = null, $offset = null) {}

    /**
     * @param string $table Table name
     * @param array|object $set Associative array of field => value
     * @return bool
     */
    public function insert($table = '', $set = null) {}

    /**
     * @param string $table Table name
     * @param array|object $set Associative array of field => value
     * @param mixed $where Where key/value or array
     * @return bool
     */
    public function update($table = '', $set = null, $where = null) {}

    /**
     * @param string $table Table name
     * @param mixed $where Where key/value or array
     * @param int $limit Limit
     * @return bool
     */
    public function delete($table = '', $where = '', $limit = null) {}

    /**
     * @param string $table Table name
     * @return int
     */
    public function count_all($table = '') {}

    /**
     * @return int
     */
    public function count_all_results($table = '') {}
}
