<?php
/**
 * CodeIgniter 3 CAPTCHA Helper (stub for IDE).
 * Load with: $this->load->helper('captcha');
 * @see https://codeigniter.com/userguide3/helpers/captcha_helper.html
 */

/**
 * Creates a CAPTCHA image from the given options. Returns an array with the image tag, timestamp, and word.
 * Requires the GD image library. Only img_path and img_url are required in $data.
 *
 * @param array|string $data      Array of options: word, img_path (required), img_url (required), font_path,
 *                                img_width, img_height, expiration, word_length, font_size, img_id, pool,
 *                                colors (background, border, text, grid as [r,g,b] arrays). If missing, defaults apply.
 * @param string       $img_path  [DEPRECATED] Path to create the image in. Use $data['img_path'] instead.
 * @param string       $img_url   [DEPRECATED] URL to the CAPTCHA image folder. Use $data['img_url'] instead.
 * @param string       $font_path [DEPRECATED] Server path to font. Use $data['font_path'] instead.
 * @return array{image: string, time: float, word: string} 'image' => image tag, 'time' => microtime used as filename, 'word' => CAPTCHA word
 */
function create_captcha($data = '', $img_path = '', $img_url = '', $font_path = '') {}
