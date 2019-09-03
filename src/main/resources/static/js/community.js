/**
 * 回复问题
 */
function post() {
    var questionId = $("#parentId").val();
    var content = $("#comment_content").val();
    comment2target(questionId, 1, content);
}

/**
 *展开评论
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);
    //获取二级评论的展开状态
    var status = e.getAttribute("Collapse");
    if (status) {
        //已经是展开状态 点击关闭
        comments.removeClass("in");
        e.removeAttribute("Collapse");
        e.classList.remove("active");
    } else {
        //先获取二级评论
        var commentBody = $("#comment-" + id);
        if (commentBody.children().length != 1) {
            //点击展开
            comments.addClass("in");
            e.setAttribute("Collapse", "in");
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {
                console.log(data);
                console.log(data.data);
                $.each(data.data.reverse(), function (index, comment) {

                    var mediaLeft = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarurl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu",
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtcreate).format("YYYY-MM-DD")
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeft)
                        .append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 border",
                    }).append(mediaElement);

                    commentBody.prepend(commentElement);
                });
            });
        }
    }
}

function comment2target(targetId, type, content) {
    if (!content) {
        alert("不能回复空内容~~~");
        return;
    }
    $.ajax({
        contentType: 'application/json',
        type: "POST",
        url: "/comment",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
            } else {
                //如果未登录
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        //点击yes
                        window.open("https://github.com/login/oauth/authorize?client_id=6db3c5a336f42c783d22&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.message);
                }

            }
        },
        dataType: "json"
    });
}

//回复评论
function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    comment2target(commentId, 2, content);
}

function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    if (previous.indexOf(value) == -1) {
        if (previous) {
            $("#tag").val(previous + "-" + value);
        } else {
            $("#tag").val(value);
        }
    }
}

function showSelectTag() {
    $("#popTag").show();
}