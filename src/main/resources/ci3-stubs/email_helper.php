<?php
/**
 * CodeIgniter 3 Email Helper (stub for IDE).
 * Load with: $this->load->helper('email');
 * @see https://codeigniter.com/userguide3/helpers/email_helper.html
 *
 * Note: This helper is DEPRECATED and kept for backwards compatibility.
 * For a more robust solution, use the Email Library.
 */

/**
 * Checks if the input is a correctly formatted e-mail address.
 * Uses PHP's filter_var($email, FILTER_VALIDATE_EMAIL). Does not verify that the address can receive mail.
 *
 * @param string $email E-mail address
 * @return bool TRUE if valid format, FALSE otherwise
 */
function valid_email($email) {}

/**
 * Sends an email using PHP's native mail() function.
 * For a more robust solution, use CodeIgniter's Email Library.
 *
 * @param string $recipient E-mail address of recipient
 * @param string $subject  Mail subject
 * @param string $message  Message body
 * @return bool TRUE if sent successfully, FALSE on error
 */
function send_email($recipient, $subject, $message) {}
