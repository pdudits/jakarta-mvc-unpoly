<article>
  <header><strong>UI preferences</strong></header>
  <label>
    <input type="checkbox" id="highlight-fragments">
    Highlight Updates
  </label>
  <!-- Little bit of alpine for interactive island -->
  <div x-data="{
    locale: document.body.dataset.dateLocale,
    dateStyle: document.body.dataset.dateStyle || 'short',
    timeStyle: document.body.dataset.timeStyle || 'short',
    get enabled() { return !!this.locale },
    set enabled(value) { this.locale = value ? 'en-US': null }}"
       x-effect="$dispatch('dateformat:changed',{locale, dateStyle, timeStyle})">
    <label>
      <input type="checkbox" x-model="enabled">
      Format Timestamps
    </label>
    <form x-show="enabled" x-cloak style="padding-left: 2rem">
      <label>Locale
        <select name="locale" x-model="locale">
          <option value="en-US" :selected="locale==$el.value">English (US)</option>
          <option value="de-DE" :selected="locale==$el.value">German</option>
          <option value="sk-SK" :selected="locale==$el.value">Slovak</option>
        </select>
      </label>
      <label>Date format
        <select name="dateStyle" x-model="dateStyle">
          <option value="full" :selected="dateStyle==$el.value">Full</option>
          <option value="long" :selected="dateStyle==$el.value">Long</option>
          <option value="medium" :selected="dateStyle==$el.value">Medium</option>
          <option value="short" :selected="dateStyle==$el.value">Short</option>
        </select>
      </label>
      <label>Time format
        <select name="timeStyle" x-model="timeStyle">
          <option value="full" :selected="timeStyle==$el.value">Full</option>
          <option value="long" :selected="timeStyle==$el.value">Long</option>
          <option value="medium" :selected="timeStyle==$el.value">Medium</option>
          <option value="short" :selected="timeStyle==$el.value">Short</option>
        </select>
      </label>
      <p id="sample-time">
        <time datetime=""></time>
      </p>
    </form>
  </div>
</article>