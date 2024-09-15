@param io.zeromagic.unpolydemo.comment.CommentsModel comments

<section>
  <h5>Comments</h5>
  <ul class="comment-list">
    @for(var comment : comments.getComments())
    <li>
      <small class="comment-header">
        <span class="author">${comment.author()}</span> at
          <time datetime="${comment.timestamp().toString()}"> ${comment.timestamp().toString()}</time>:
      </small>
      <p>${comment.content()}</p>
    </li>
    @else
    <li><small class="comment-header">No comments</small></li>
    @endfor
  </ul>
  <a href="${comments.newPostLink()}" class="add-comment" up-target=".add-comment" role="button">Add Comment</a>
</section>