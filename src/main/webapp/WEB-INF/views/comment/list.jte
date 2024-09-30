@param io.zeromagic.unpolydemo.comment.CommentsModel comments

<section>
  @if(comments.getEvent() != null)
  <nav aria-label="breadcrumb">
    <ul>
      <li><a href="/app/${comments.getEvent().key().name()}">${comments.getEvent().key().name()}</a></li>
      <li><a href="/app/${comments.getEvent().key().name()}#event-${comments.getEvent().id()}">${comments.getEvent().message()}</a></li>
    </ul>
  </nav>
  @endif
  <h5>Comments<a href="/static/up-defer-2.html" class="instruction">?</a></h5>
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
  <span style="padding-right: 3em" class="add-comment"><!-- add-comment were originally on a before instruction was added -->
  <a href="${comments.newPostLink()}"
    up-target=".add-comment" role="button">Add Comment</a> <a href="/static/up-target-1.html" class="instruction"></a>
  </span>
</section>