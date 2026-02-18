<?php
/**
 * Stub CI_Email for PhpStorm – CodeIgniter 3 Email library.
 * Load with $this->load->library('email') → $this->email.
 *
 * @see https://codeigniter.com/userguide3/libraries/email.html
 */
class CI_Email
{
    /**
     * Sets the "From" email address and name.
     *
     * @param string $from "From" e-mail address
     * @param string $name "From" display name
     * @param string|null $return_path Optional email address to redirect undelivered e-mail to
     * @return CI_Email
     */
    public function from($from, $name = '', $return_path = null) {}

    /**
     * Sets the reply-to address.
     *
     * @param string $replyto E-mail address for replies
     * @param string $name Display name for the reply-to e-mail address
     * @return CI_Email
     */
    public function reply_to($replyto, $name = '') {}

    /**
     * Sets the recipient email address(es). Can be a single e-mail, comma-delimited list or array.
     *
     * @param string|array $to Comma-delimited string or an array of e-mail addresses
     * @return CI_Email
     */
    public function to($to) {}

    /**
     * Sets the CC email address(es).
     *
     * @param string|array $cc Comma-delimited string or an array of e-mail addresses
     * @return CI_Email
     */
    public function cc($cc) {}

    /**
     * Sets the BCC email address(es). If $limit is set, batch mode is enabled.
     *
     * @param string|array $bcc Comma-delimited string or an array of e-mail addresses
     * @param int $limit Maximum number of e-mails to send per batch (enables batch mode)
     * @return CI_Email
     */
    public function bcc($bcc, $limit = '') {}

    /**
     * Sets the email subject line.
     *
     * @param string $subject E-mail subject line
     * @return CI_Email
     */
    public function subject($subject) {}

    /**
     * Sets the e-mail message body.
     *
     * @param string $body E-mail message body
     * @return CI_Email
     */
    public function message($body) {}

    /**
     * Sets the alternative plain-text message (for HTML emails).
     *
     * @param string $str Alternative e-mail message body
     * @return CI_Email
     */
    public function set_alt_message($str) {}

    /**
     * Appends additional headers to the e-mail.
     *
     * @param string $header Header name
     * @param string $value Header value
     * @return CI_Email
     */
    public function set_header($header, $value) {}

    /**
     * Initializes all email variables to empty state. Use in loops between sends.
     *
     * @param bool $clear_attachments Whether to clear attachments as well
     * @return CI_Email
     */
    public function clear($clear_attachments = false) {}

    /**
     * Sends the e-mail.
     *
     * @param bool $auto_clear Whether to clear message data automatically after send
     * @return bool TRUE on success, FALSE on failure
     */
    public function send($auto_clear = true) {}

    /**
     * Attaches a file. For multiple attachments call multiple times.
     *
     * @param string $filename File path/name or buffer string
     * @param string $disposition 'attachment' or 'inline'
     * @param string|null $newname Custom file name in the e-mail
     * @param string $mime MIME type (for buffered data)
     * @return CI_Email
     */
    public function attach($filename, $disposition = '', $newname = null, $mime = '') {}

    /**
     * Returns an attachment's Content-ID for embedding inline in HTML (e.g. images).
     *
     * @param string $filename Already attached file name
     * @return string|false Content-ID or FALSE if not found
     */
    public function attachment_cid($filename) {}

    /**
     * Returns formatted debug data (server messages, headers, message). Use after send(FALSE).
     *
     * @param array $include Which parts to print: 'headers', 'subject', 'body'
     * @return string
     */
    public function print_debugger($include = ['headers', 'subject', 'body']) {}

    /**
     * Initializes email preferences (protocol, smtp_host, charset, wordwrap, etc.).
     *
     * @param array $config Preference values (e.g. protocol, mailpath, smtp_host, charset)
     * @return CI_Email
     */
    public function initialize($config = []) {}
}
