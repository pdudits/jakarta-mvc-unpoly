@param io.zeromagic.unpolydemo.cookies.CookieModel cookieModel

<section>
<header>
  <h1>We Use Cookies</h1>
  <h2>Like... a lot</h2>
</header>
<p>
And here are the various purposes we use cookies for:</p>
<dl>
  <dt><b>Cookies for you</b></dt>
<dd>So that you can use the application</dd>
  <dt><b>Cookies for us</b></dt>
<dd>So that we have idea how our users use the app</dd>
  <dt><b>Cookies for everyone</b></dt>
<dd>So that our 1652 good friends know how you use the app</dd>
</dl>
</section>
<section id="cookie-preferences">
<article>
<header>
<strong>How many cookies do you prefer?</strong>
  <a href="/static/up-layer-3.html" class="instruction">?</a>
</header>
<form method="post" up-submit>
  <fieldset up-autosubmit>
    <label>
      <input type="radio" name="pref" value="for-me"
             checked="${cookieModel.isForMe()}">
      Cookie for You
    </label>
    <label>
      <input type="radio" name="pref" value="for-you"
             checked="${cookieModel.isForYou()}">
      Cookies For Us
    </label>
    <label>
      <input type="radio" name="pref" value="for-all"
             checked="${cookieModel.isForAll()}">
      Cookies For Everyone!
    </label>
  </fieldset>
  <noscript>
    <button type="submit">Save Preference</button>
  </noscript>
</form>
@if (cookieModel.isOverlay())
  <a href="/cookies" up-layer="parent root">Learn more about cookies.</a>
@endif
</article>
</section>