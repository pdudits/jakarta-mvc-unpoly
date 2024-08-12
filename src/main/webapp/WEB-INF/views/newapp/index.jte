@param io.zeromagic.unpolydemo.newapp.NewAppModel newApp
<h2>Upload New Application</h2>

<dialog id="modal" open>
  <article>
    <header>
      <button rel="prev" type="button" aria-label="close"
              up-dismiss></button>
      <p><strong>New Application</strong></p>
    </header>

    <form up-layer="parent root" method="post" up-target="main">
      <label>Name
      <input type="text" id="name" name="name"
             aria-invalid="${newApp.getName().ariaInvalid()}"
             value="${newApp.getName().value()}"required>
      <small>${newApp.getName().message()}</small>
      </label>
      <label>Context Root
      <input type="text" id="contextRoot" name="contextRoot"
             aria-invalid="${newApp.getContextRoot().ariaInvalid()}"
             value="${newApp.getContextRoot().value()}"
             required>
      <small>${newApp.getContextRoot().message()}</small>
      </label>
      <footer>
        <button type="submit">Save</button>
      </footer>
    </form>

  </article>
</dialog>
