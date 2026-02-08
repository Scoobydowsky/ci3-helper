<?php
/**
 * Stub CI_Benchmark for PhpStorm – CodeIgniter 3 Benchmarking class.
 * Always loaded by the framework; no need to load manually.
 *
 * @see https://codeigniter.com/userguide3/libraries/benchmark.html
 */
class CI_Benchmark
{
    /**
     * Sets a benchmark marker.
     *
     * @param string $name Name assigned to the marker (e.g. 'code_start', 'code_end')
     * @return void
     */
    public function mark($name): void {}

    /**
     * Time difference between two marked points, or total elapsed time if no params.
     *
     * @param string $point1 First marker name (empty = total execution time)
     * @param string $point2 Second marker name
     * @param int $decimals Decimal places (default 4)
     * @return string Elapsed time
     */
    public function elapsed_time($point1 = '', $point2 = '', $decimals = 4): string {}

    /**
     * Returns the {memory_usage} pseudo-variable (replaced by Output class at the end).
     *
     * @return string Memory usage
     */
    public function memory_usage(): string {}
}
