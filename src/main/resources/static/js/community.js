
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    $.ajax({
        type:"POST",
        url:"/comment",
        contentType:"application/json",
        data: JSON.stringify({
            "parentId":questionId,
            "content":content,
            "type":1
        }),
        success:function (response) {
            if(response.code == 200){
                $("#comment_section").hide();
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