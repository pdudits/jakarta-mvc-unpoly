<form class="add-comment"
    up-target="section .comment-list:after, .add-comment"
    up-scroll="target"
    method="post">
<article>
  <header><strong>Add comment</strong></header>
    <label>Author<input type="text" name="author"></label>
    <label>Comment<textarea name="content"></textarea></label>
    <footer>
      <button type="submit">Add Comment<a href="/static/up-target-2.html" class="instruction">?</a></button>
    </footer>
</article>
</form>