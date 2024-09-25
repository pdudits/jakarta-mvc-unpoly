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
    content: `<article>Cookie preference updated to ${ev.value}</article>`,
  }));

// highlight fragments checkbox enables or disables highlighting of
// replaced fragments
up.compiler('#highlight-fragments', (element) => {
    element.checked = window.toggleHighlightsHandler ? true : false;
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
                up.util.timer(1000, () => fragment.classList.remove('inserted'))
                up.util.timer(3000, () => fragment.classList.remove('new-fragment'))
        });
    }
}

up.compiler('time', (element) => {
    const timeAttr = element.getAttribute('datetime');
    if (!document.body.dataset.dateLocale || !timeAttr) {
        return;
    }
    const date = new Date(timeAttr);
    const formatter = new Intl.DateTimeFormat(document.body.dataset.dateLocale, {
        dateStyle: document.body.dataset.dateStyle || 'short',
        timeStyle: document.body.dataset.timeStyle || 'short'
    });
    element.textContent = formatter.format(date);
})

// Load Alpine on demand
up.compiler('[x-data]', (element) => {
    if (!window.Alpine) {
        if (!document.body.dataset.alpineLoaded) {
            document.body.dataset.alpineLoaded = 'true';
            let head = document.head;
            let script = document.createElement('script');
            script.src = '/webjars/alpinejs/3.14.1/dist/cdn.min.js';
            head.appendChild(script);
        }
    }
});

up.on('dateformat:changed', (event) => {
    console.log("Date format changed to", event.detail);
    if (event.detail.locale) {
        document.body.dataset.dateLocale = event.detail.locale;
        document.body.dataset.dateStyle = event.detail.dateStyle;
        document.body.dataset.timeStyle = event.detail.timeStyle;
        up.render({
            target: '#sample-time',
            fallback: ':none',
            content: `Example: <time datetime="${new Date().toISOString()}">${new Date()}</time>`
        })
    } else {
        delete document.body.dataset.dateLocale;
    }
});

// request html (otherwise chosen by random by server)
up.on('up:request:load', function (event) {
    event.request.headers['Accept'] = 'text/html,*/*;q=0.8';
});

up.on('up:fragment:keep', 'bar-chart', (ev) => {
    // add the first child of ev.nextElement to ev.element
    ev.target.appendChild(ev.newFragment.firstElementChild);
    // remove first element if we have more than 5 childen
    if (ev.target.children.length > 5) {
        ev.target.removeChild(ev.target.firstElementChild);
    }
})