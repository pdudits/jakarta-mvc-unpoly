
<article>
  <header><strong>UI preferences</strong></header>
      <label>
        <input type="checkbox" id="highlight-fragments">
        Highlight Updates
      </label>
      <label>
        <input type="checkbox" id="format-timestamps" up-switch="#timestamp-format">
        Format Timestamps
      </label>
  <form id="timestamp-format" up-show-for="on" style="padding-left: 2rem">
    <label>Locale
      <select name="locale">
        <option value="en-US">English (US)</option>
        <option value="de-DE">German</option>
        <option value="sk-SK">Slovak</option>
      </select>
    </label>
    <label>Date format
      <select name="dateStyle">
        <option value="full">Full</option>
        <option value="long">Long</option>
        <option value="medium">Medium</option>
        <option value="short">Short</option>
      </select>
    </label>
    <label>Time format
      <select name="timeStyle">
        <option value="full">Full</option>
        <option value="long">Long</option>
        <option value="medium">Medium</option>
        <option value="short">Short</option>
      </select>
    </label>
    <p><time id="sample-time" datetime="">Example time format will appear here</time></p>
    <button>Save</button>
  </form>
</article>