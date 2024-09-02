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
    const handle = setTimeout( () => {
        // animate the element out of the view
        up.animate(element, 'move-to-top').then(() => {
            // cleanup the element after it's gone
            up.destroy(element);
        });
        }, 5000); // after 5 seconds
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
