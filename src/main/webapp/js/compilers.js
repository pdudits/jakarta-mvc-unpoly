// Load lit-line custom component when it is used in the page
up.compiler('lit-line', (element) => {
    if (window.customElements.get('lit-line')) {
        return;
    }
    // if custom element is not defined, load the script.
    let body = document.body;
    // load it only once though
    if (body.dataset.litLineLoaded === 'true') {
        return;
    } else {
        body.dataset.litLineLoaded = 'true';
    }
    let head = document.head;
    let script = document.createElement('script');
    script.type = 'module';
    script.src = '/webjars/lit-line/0.3.2/cdn/lit-line.js';
    head.appendChild(script);
});

// Remove the flash message after 5 seconds
up.compiler('#flash article', (element) => {
    const handle = up.util.timer( 5000, () =>
        // animate the element out of the view
        up.animate(element, 'move-to-top').then(() =>
            // cleanup the element after it's gone
            up.destroy(element))); // after 5 seconds
    // return a cleanup function
    return () => clearTimeout(handle);
})

// Show a message when the cookie preference is changed
// We would be loading/unloading analytics under normal occasion,
// now it serves as client side usage of Unpoly fragment API.
up.on('cookie-pref:changed',
  (ev) => up.render({
    target: '#flash:after', // append to the flash message
    content: `<article>Cookie preference updated to ${ev.value}</article>`
  }));

// highlight fragments checkbox enables or disables highlighting of
// replaced fragments
up.compiler('#highlight-fragments', (element) => {
    element.value = window.toggleHighlightsHandler || false;
    return up.on(element, 'change', toggleHighlights);
});

function toggleHighlights() {
    if (window.toggleHighlightsHandler) {
        window.toggleHighlightsHandler();
        window.toggleHighlightsHandler = null;
    } else {
        window.toggleHighlightsHandler = up.on('up:fragment:inserted',
            (event, fragment) => {
                fragment.classList.add('new-fragment', 'inserted')
                up.util.timer(0, () => fragment.classList.remove('inserted'))
                up.util.timer(1000, () => fragment.classList.remove('new-fragment'))
        });
    }
}