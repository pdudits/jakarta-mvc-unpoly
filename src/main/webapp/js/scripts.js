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
    scroll: 'target'
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

// request html (otherwise chosen by random by server, especially where we also offer text/event-stream)
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
});

// macro compilers are executed before regular compilers, therefore we can put in
// unpoly attributes to an element and have them processed by the regular compilers

up.compiler('#up-console', (element) => {
    const toggle = element.querySelector('.up-console-toggle');
    const clearBtn = element.querySelector('.up-console-clear');
    const logEl = element.querySelector('.up-console-log');
    const filterEl = element.querySelector('.up-console-filter');
    const MAX_ENTRIES = 50;
    let unbindRequest = null;
    let unbindResponse = null;

    function xUpHeaders(headers) {
        return Object.entries(headers)
            .filter(([k]) => k.toLowerCase().startsWith('x-up-'))
            .map(([k, v]) => `<span class="up-header">  ${k}: </span>${up.util.escapeHTML(String(v))}`)
            .join('\n');
    }

    function xUpResponseHeaders(response) {
        const raw = response.xhr?.getAllResponseHeaders() || '';
        return raw.split('\r\n')
            .filter(line => line.toLowerCase().startsWith('x-up-'))
            .map(line => {
                const colon = line.indexOf(':');
                const name = line.slice(0, colon).trim();
                const value = line.slice(colon + 1).trim();
                return `<span class="up-header">  ${name}: </span>${up.util.escapeHTML(value)}`;
            })
            .join('\n');
    }

    function appendEntry(html, type) {
        const div = document.createElement('div');
        div.className = `up-console-entry up-console-${type}`;
        div.innerHTML = html;
        logEl.appendChild(div);
        // Trim oldest entries
        while (logEl.children.length > MAX_ENTRIES) {
            logEl.removeChild(logEl.firstElementChild);
        }
        logEl.scrollTo({ top: logEl.scrollHeight, behavior: 'smooth' });
        clearBtn.disabled = false;
    }

    function filtered(url) {
        if (filterEl && filterEl.value) {
            return url.indexOf(filterEl.value) < 0;
        }
        return false;
    }

    function onRequestLoad(event) {
        const req = event.request;
        const headers = xUpHeaders(req.headers);
        const first = `▶ ${req.method.toUpperCase()} ${up.util.escapeHTML(req.url)}`;
        if (filtered(req.url)) return;
        appendEntry(headers ? `${first}\n${headers}` : first, 'request');
    }

    function onFragmentLoaded(event) {
        const res = event.response;
        const target = res.target || event.request?.target || '?';
        const headers = xUpResponseHeaders(res);
        const first = `◀ ${res.status}  ${up.util.escapeHTML(res.url)}  →  ${up.util.escapeHTML(String(target))}`;
        if (filtered(res.url)) return;
        appendEntry(headers ? `${first}\n${headers}` : first, 'response');
    }

    function open() {
        element.classList.add('open');
        toggle.setAttribute('aria-expanded', 'true');
        unbindRequest = up.on('up:request:load', onRequestLoad);
        unbindResponse = up.on('up:fragment:loaded', onFragmentLoaded);
    }

    function close() {
        element.classList.remove('open');
        toggle.setAttribute('aria-expanded', 'false');
        if (unbindRequest) { unbindRequest(); unbindRequest = null; }
        if (unbindResponse) { unbindResponse(); unbindResponse = null; }
    }

    toggle.addEventListener('click', () => {
        element.classList.contains('open') ? close() : open();
    });

    clearBtn.addEventListener('click', () => {
        logEl.innerHTML = '';
        clearBtn.disabled = true;
    });

    return () => close();
});

up.macro('a.instruction', (element) => {
    element.setAttribute('up-layer', 'new');
    element.setAttribute('up-size', 'large');
    element.textContent = '✤';
});