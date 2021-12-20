function flex(){
    let main = $("#main");
    const height = main.height();
    const winHeight = $(window).innerHeight();
    if (height < (winHeight-100)){
        main.height(winHeight-100);
    }
}
function comment(content,parentId,type){
    $.ajax({
            type: "POST",
            url:"/comment",
            data:JSON.stringify({
                "content":content,
                "parentId":parentId,
                "type":type}),
            dataType:"json",
            contentType:"application/json",
            success:function (data){
                if (200===data.code){
                    window.location.reload();
                }else if(1002===data.code){
                    let isAccepted = confirm("你还未登录是否前往登录");
                    if (isAccepted){
                        window.open("/login");
                        window.localStorage.setItem("closable","true");
                    }else if (1009===data.code){
                        alert("评论内容不能为空");
                    }
                }else {
                    alert("评论失败");
                }
            }
        }
    )
}
function post_comment(){
    let content = $("#comment_text").val();
    let parentId = $("#parent").val();
    let type = 1;
    if (content!=null&&content.trim().length===0){
        alert("评论内容不能为空");
        return;
    }
    if (parentId == null){
        alert("评论参数错误,请刷新页面重试");
        return;
    }
    comment(content,parentId,type);
}
function post_sub_comment(e){

    let cid = $(e).attr("cid");
    let content = $("#subComment_text"+cid).val();
    let parentId = cid;
    let type = 2;
    if (content!=null&&content.trim().length===0){
        alert("评论内容不能为空");
        return;
    }
    if (parentId == null){
        alert("评论参数错误,请刷新页面重试");
        return;
    }
    comment(content,parentId,type);
}
function isLogin(){
    let closable = window.localStorage.getItem("closable")
    if (closable==="true"){
        window.close();
        window.localStorage.removeItem("closable");
    }
}
function collapseComments(e) {
    let id = e.getAttribute("data-id");
    // console.log(id);
    let comments = $("#comment-" + id);

    // 获取一下二级评论的展开状态
    let collapse = e.getAttribute("data-collapse");
    if (collapse) {
        // 折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {

        let subCommentContainer = $("#comment-" + id);

        if (subCommentContainer.children().length !== 1) {
            // console.log("else if "+subCommentContainer.children().length)
            //展开二级评论
            comments.addClass("in");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {
                // console.log(data);
                $.each(data.reverse(), function (index, comment) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object comment_media",
                        "src": comment.user.avatarUrl,
                        "alt": "/images/default.png"
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "class": "comments",
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "date pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD hh:mm:ss')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>").append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                // 标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
}
function showTag(){

    let tips = $("#tips");
    let main_height = $("#main").outerHeight();
    let tips_height= tips.outerHeight();
    // console.log(main_height,tips_height);
    if (main_height>tips_height){
        tips.height(main_height);
    }
    $("#box").hide();
    $("#tagInput").click(function(){
        $("#box").show();
        return false;//关键是这里，阻止冒泡
    });
    tips.click(function(){
        $("#box").hide();//点击文档document隐藏box
    });

}
function inputTags(e){
    let tagName = $(e).attr("tagName");
    let tagInput = $("#tagInput");
    if (tagInput.val().trim().length===0){
        tagInput.val(tagName);
    }else {
        let tags = tagInput.val().split(",");
        for (let tag of tags) {
            if (tag===tagName){
                return;}}
        tagInput.val(tagInput.val()+','+tagName);
    }
}
function validate(){
    if ($("#title").val().trim().length===0){
        alert("标题不能为空")
         return false;
    }
    if ($("#description").val().trim().length===0){
        alert("内容不能为空")
        return false;
    }

    // 标签验证
    let allTags = $("#box").attr("allTags");
    let tagInputs = $("#tagInput").val().split(",");
    if (tagInputs.length>4){
        alert("最多选择4个标签");
        return false;
    }
    allTags = JSON.parse(allTags);
    let tagSet= new Set();
    for (let tag of allTags) {
        tagSet.add(tag);
    }
    let tagInputSet = new Set;
    // 判断重复或不存在
    for (let tagInput of tagInputs){
        if (tagInputSet.has(tagInput)||!tagSet.has(tagInput)){
            alert("标签可能重复或不存在!,请检查标签")
            return false;
        }else {
            tagInputSet.add(tagInput);
        }
    }
    return true;
}


function submit_question_like() {
    if (voidClick()===0){return;}
    let isLike = $("#isLike");
    let status = isLike.attr("status");
    let  questionLikeCount = $("#questionLikeCount");
    // 切换图标,发送请求
    if(status==='1'){
        isLike.attr("src","/images/is-like.png");
        isLike.attr("status",'0');
        // 同步点赞数
        questionLikeCount.text(Number(questionLikeCount.text())-1);
    }else if (status==='0') {
        isLike.attr("src","/images/is-like-active.png");
        isLike.attr("status",'1');
        questionLikeCount.text(Number(questionLikeCount.text())+1);
    }
    post_like(1,Number(isLike.attr("likeId")),Number(isLike.attr("status")))
}
function submit_comment_like(e){
    if (voidClick()===0){return;}
   let isLike =  $(e).attr("like");
   let id = $(e).attr("cid");
   if (isLike==='1'){
       $(e).css('color','#999999');
       let clkCount = $("#commentLike"+id);
       let likeCount = clkCount.text();
       clkCount.text(Number(likeCount)-1);
       $(e).attr("like",'0');
   }else if (isLike==='0'){
       $(e).css('color','#499ef3');
       let clkCount = $("#commentLike"+id);
       let likeCountText = clkCount.text();
       clkCount.text(Number(likeCountText)+1);
       $(e).attr("like",'1');
   }
    post_like(2,Number(id),Number($(e).attr('like')))
}
function post_like(type,likeId,status){
    $.ajax({
        type: "POST",
        url:"/like",
        data:({
            "status":status,
            "likeId":likeId,
            "type":type
            }),
        success:function (data){
        }
    });
}
function voidClick(){
    // 防止连续点击
    let lastClick = window.localStorage.getItem("lastClick");
    if (lastClick==null) {
        window.localStorage.setItem("lastClick", (new Date().getTime()).toString());
    }else {
        let now = new Date().getTime();
        let lastTime = Number(lastClick);
        window.localStorage.setItem("lastClick",now.toString());
        // console.log("now:"+now+","+"lastTime:"+lastTime)
        if (now-lastTime < 300){
            window.localStorage.setItem("lastClick",now.toString());
            return 0;
        }
    }
    return 1;
}
function md_editor(){
    $(function() {
        var editor = editormd("editor", {
            htmlDecode: "style,script,iframe|on*",
            width: "100%",
            height: 500,
            imageUpload          : true,          // Enable/disable upload
            imageFormats         : ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
            imageUploadURL       : "/imgUpload",             // Upload url
            // markdown: "xxxx",     // dynamic set Markdown text
            path : "/editor.md/lib/"  // Autoload modules mode, codemirror, marked... dependents libs path
        });
    });
}
function markdown_view() {
    $(function() {
        var testView = editormd.markdownToHTML("markdown-view", {htmlDecode: "style,script,iframe|on*"});
    });
}
