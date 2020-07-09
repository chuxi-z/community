/**
 * Post comments
 */
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();

    commentToTarget(questionId, 1, content);
    // if(!content){
    //     alert("Comment is not allowed to be empty...");
    //     return;
    // }
    //
    // $.ajax({
    //     type:"POST",
    //     url:"/comment",
    //     contentType:"application/json",
    //     data: JSON.stringify({
    //         "parentId":questionId,
    //         "content":content,
    //         "type":1
    //     }),
    //     success:function (response) {
    //         if(response.code == 200){
    //             window.location.reload();
    //         }
    //         else{
    //             if(response.code == 2003){
    //                 var isAccepted = confirm(response.message);
    //                 if(isAccepted){
    //                     window.open("https://github.com/login/oauth/authorize?client_id=b232b7575e5e6f83d4bc&client_secret=541dfb5994031965b0c605513eb339e4d650204c&redirect_uri=http://localhost:8080/callback/&scope=user&state=1");
    //                     window.localStorage.setItem("closable", true);
    //                 }
    //             }
    //             else{
    //                 alert(response.message);
    //             }
    //         }
    //         console.log(response);
    //     },
    //     dataType:"json"
    // });

}

function commentToTarget(targetId, type, content) {
    if(!content){
        alert("Comment is not allowed to be empty...");
        return;
    }

    $.ajax({
        type:"POST",
        url:"/comment",
        contentType:"application/json",
        data: JSON.stringify({
            "parentId": targetId,
            "content":content,
            "type":type
        }),
        success:function (response) {
            if(response.code == 200){
                window.location.reload();
            }
            else{
                if(response.code == 2003){
                    var isAccepted = confirm(response.message);
                    if(isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=b232b7575e5e6f83d4bc&client_secret=541dfb5994031965b0c605513eb339e4d650204c&redirect_uri=http://localhost:8080/callback/&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    }
                }
                else{
                    alert(response.message);
                }
            }
            console.log(response);
        },
        dataType:"json"
    });
}

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-"+commentId).val();
    commentToTarget(commentId, 2, content);
}


/**
 * Expand second level comment
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments=$("#comment-" + id);

    var collapse = e.getAttribute("data-collapse");
    if(collapse){
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    }
    else{
        // $.getJSON( "/comment/"+id, function( data ) {
        //     var items = [];
        //     $.each( data, function( key, val ) {
        //         items.push( "<li id='" + key + "'>" + val + "</li>" );
        //     });
        //
        //     $( "<ul/>", {
        //         "class": "my-new-list",
        //         html: items.join( "" )
        //     }).appendTo( "body" );
        // });

        comments.addClass("in");
        e.setAttribute("data-collapse", "in");
        e.classList.add("active");
    }
}





