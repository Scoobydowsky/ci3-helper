<?php
/**
 * Stub CI_Migration for PhpStorm – CodeIgniter 3 Migrations.
 * @see https://codeigniter.com/userguide3/libraries/migration.html
 */
class CI_Migration
{
    /**
     * Migrates up to the current version (whatever is set for $config['migration_version'] in application/config/migration.php).
     * @return mixed TRUE if no migrations are found, current version string on success, FALSE on failure
     */
    public function current() {}

    /**
     * Returns a string of errors that were detected while performing a migration.
     * @return string Error messages
     */
    public function error_string() {}

    /**
     * An array of migration filenames are returned that are found in the migration_path property.
     * @return array An array of migration files
     */
    public function find_migrations() {}

    /**
     * This works much the same way as current() but instead of looking for the $config['migration_version']
     * the Migration class will use the very newest migration found in the filesystem.
     * @return mixed Current version string on success, FALSE on failure
     */
    public function latest() {}

    /**
     * Version can be used to roll back changes or step forwards programmatically to specific versions.
     * It works just like current() but ignores $config['migration_version'].
     * @param mixed $target_version Migration version to process
     * @return mixed TRUE if no migrations are found, current version string on success, FALSE on failure
     */
    public function version($target_version) {}
}
