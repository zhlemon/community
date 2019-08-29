function post() {
    var questionId = $("#parentId").val();
    var content = $("#comment_content").val();
    $.ajax({
        contentType: 'application/json',
        type: "POST",
        url: "/comment",
        data: JSON.stringify({
            "parentId": questionId,
            "content": content,
            "type": 1
        }),
        success: function (response) {
            if (response.code == 200) {
                $("#comment_section").hide();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        //点击yes
                        window.open("https://github.com/login/oauth/authorize?client_id=6db3c5a336f42c783d22&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closable",true);
                    }
                } else {
                    alert(response.message);
                }

            }
        },
        dataType: "json"
    });

}