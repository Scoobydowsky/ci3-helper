<?php
/**
 * CodeIgniter 3 Download Helper (stub for IDE).
 * Load with: $this->load->helper('download');
 * @see https://codeigniter.com/userguide3/helpers/download_helper.html
 */

/**
 * Generates server headers which force data to be downloaded to the desktop.
 * If $data is NULL and $filename is an existing readable file path, its content is read.
 * If $set_mime is TRUE, the actual file MIME type (from filename extension) is sent.
 *
 * @param string $filename Name for the downloaded file
 * @param mixed  $data     File contents, or NULL to read from the path given in $filename
 * @param bool   $set_mime Whether to send the actual MIME type (default FALSE)
 * @return void
 */
function force_download($filename = '', $data = '', $set_mime = false) {}
