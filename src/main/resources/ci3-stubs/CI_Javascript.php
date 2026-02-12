<?php
/**
 * Stub CI_Javascript for PhpStorm – CodeIgniter 3 Javascript library.
 * Load with $this->load->library('javascript') → $this->javascript,
 * or $this->load->library('javascript/jquery') → $this->jquery.
 *
 * @see https://codeigniter.com/userguide3/libraries/javascript.html
 *
 * @deprecated This library is deprecated and kept for backwards compatibility only.
 */
class CI_Javascript
{
    /**
     * Compile the JavaScript output.
     *
     * @return string
     */
    public function compile() {}

    /**
     * Write a script to the output.
     *
     * @param string $script Script content
     * @param string $type Script type attribute
     * @return CI_Javascript
     */
    public function script($script, $type = 'text/javascript') {}

    /**
     * Clear collected JavaScript.
     *
     * @return CI_Javascript
     */
    public function clear_js() {}

    /**
     * Add external script reference.
     *
     * @param string $file External script URL
     * @param bool $relative Whether the path is relative
     * @return CI_Javascript
     */
    public function external($file, $relative = true) {}

    /**
     * Add inline script.
     *
     * @param string $script Inline script content
     * @return CI_Javascript
     */
    public function inline($script) {}

    /* ---------- jQuery events (when driver is jquery) ---------- */

    /**
     * Attach event handler. Event: blur, change, click, dblclick, error, focus, hover, keydown, keyup, load, mousedown, mouseup, mouseover, resize, scroll, unload.
     *
     * @param string $element_path jQuery selector (e.g. '#notice_area', '#content a.notice')
     * @param string $code_to_run JavaScript code to run
     * @return CI_Javascript
     */
    public function blur($element_path, $code_to_run) {}
    /** @param string $element_path */
    public function change($element_path, $code_to_run) {}
    /** @param string $element_path */
    public function click($element_path, $code_to_run) {}
    /** @param string $element_path */
    public function dblclick($element_path, $code_to_run) {}
    /** @param string $element_path */
    public function error($element_path, $code_to_run) {}
    /** @param string $element_path */
    public function focus($element_path, $code_to_run) {}
    /** @param string $element_path */
    public function hover($element_path, $code_to_run) {}
    /** @param string $element_path */
    public function keydown($element_path, $code_to_run) {}
    /** @param string $element_path */
    public function keyup($element_path, $code_to_run) {}
    /** @param string $element_path */
    public function load($element_path, $code_to_run) {}
    /** @param string $element_path */
    public function mousedown($element_path, $code_to_run) {}
    /** @param string $element_path */
    public function mouseup($element_path, $code_to_run) {}
    /** @param string $element_path */
    public function mouseover($element_path, $code_to_run) {}
    /** @param string $element_path */
    public function resize($element_path, $code_to_run) {}
    /** @param string $element_path */
    public function scroll($element_path, $code_to_run) {}
    /** @param string $element_path */
    public function unload($element_path, $code_to_run) {}

    /* ---------- jQuery effects ---------- */

    /**
     * Hide element(s).
     *
     * @param string $target jQuery selector
     * @param string|int $speed Optional: slow, normal, fast, or milliseconds
     * @param mixed $extra Optional callback or extra info
     * @return CI_Javascript
     */
    public function hide($target, $speed = null, $extra = null) {}

    /**
     * Show element(s).
     *
     * @param string $target jQuery selector
     * @param string|int $speed Optional: slow, normal, fast, or milliseconds
     * @param mixed $extra Optional callback or extra info
     * @return CI_Javascript
     */
    public function show($target, $speed = null, $extra = null) {}

    /**
     * Toggle visibility of element(s).
     *
     * @param string $target jQuery selector
     * @return CI_Javascript
     */
    public function toggle($target) {}

    /**
     * Animate CSS properties.
     *
     * @param string $target jQuery selector
     * @param array $parameters CSS properties to animate (e.g. ['height' => 80, 'width' => '50%'])
     * @param string|int $speed Optional: slow, normal, fast, or milliseconds
     * @param mixed $extra Optional callback or extra info
     * @return CI_Javascript
     */
    public function animate($target, $parameters, $speed = null, $extra = null) {}

    /**
     * Fade in element(s).
     *
     * @param string $target jQuery selector
     * @param string|int $speed Optional: slow, normal, fast, or milliseconds
     * @param mixed $extra Optional callback or extra info
     * @return CI_Javascript
     */
    public function fadeIn($target, $speed = null, $extra = null) {}

    /**
     * Fade out element(s).
     *
     * @param string $target jQuery selector
     * @param string|int $speed Optional: slow, normal, fast, or milliseconds
     * @param mixed $extra Optional callback or extra info
     * @return CI_Javascript
     */
    public function fadeOut($target, $speed = null, $extra = null) {}

    /**
     * Toggle CSS class on element(s).
     *
     * @param string $target jQuery selector
     * @param string $class CSS class name
     * @return CI_Javascript
     */
    public function toggleClass($target, $class) {}

    /**
     * Slide up element(s).
     *
     * @param string $target jQuery selector
     * @param string|int $speed Optional: slow, normal, fast, or milliseconds
     * @param mixed $extra Optional callback or extra info
     * @return CI_Javascript
     */
    public function slideUp($target, $speed = null, $extra = null) {}

    /**
     * Slide down element(s).
     *
     * @param string $target jQuery selector
     * @param string|int $speed Optional: slow, normal, fast, or milliseconds
     * @param mixed $extra Optional callback or extra info
     * @return CI_Javascript
     */
    public function slideDown($target, $speed = null, $extra = null) {}

    /**
     * Slide toggle element(s).
     *
     * @param string $target jQuery selector
     * @param string|int $speed Optional: slow, normal, fast, or milliseconds
     * @param mixed $extra Optional callback or extra info
     * @return CI_Javascript
     */
    public function slideToggle($target, $speed = null, $extra = null) {}

    /**
     * Load an effect plugin (e.g. 'bounce').
     *
     * @param string $plugin_name Effect/plugin name
     * @param string|null $path Optional path to plugin
     * @return CI_Javascript
     */
    public function effect($plugin_name, $path = null) {}

    /* ---------- jQuery plugins ---------- */

    /**
     * Add corner style to element(s). Style: round, sharp, bevel, bite, dog, etc. Use "tl", "tr", "bl", "br" for corners.
     *
     * @param string $target jQuery selector
     * @param string|null $corner_style Optional (e.g. 'cool tl br')
     * @return CI_Javascript
     */
    public function corner($target, $corner_style = null) {}

    /**
     * Tablesorter plugin.
     *
     * @param string $target jQuery selector
     * @param mixed $options Optional options
     * @return CI_Javascript
     */
    public function tablesorter($target, $options = null) {}

    /**
     * Modal plugin.
     *
     * @param string $target jQuery selector
     * @param mixed $options Optional options
     * @return CI_Javascript
     */
    public function modal($target, $options = null) {}

    /**
     * Calendar plugin.
     *
     * @param string $target jQuery selector
     * @param mixed $options Optional options
     * @return CI_Javascript
     */
    public function calendar($target, $options = null) {}
}
