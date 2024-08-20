up.compiler('lit-line', (element) => {
    if (window.customElements.get('lit-line')) {
        return;
    }
    let body = document.body;
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

up.on('cookie-pref:changed', 
    (ev) => {alert('Cookie preference is now ' + ev.value)});
