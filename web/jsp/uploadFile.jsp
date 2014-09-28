<%@ page language="java" pageEncoding="utf-8"%>
<html>
<head>
    <title>文件上传</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/script/uploadfile/uploadify.css" type="text/css">
    <script src="<%=request.getContextPath()%>/script/uploadfile/jquery-1.3.2.min.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/script/uploadfile/swfobject.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/script/uploadfile/jquery.uploadify.v2.1.0.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/script/common/uuid.js" type="text/javascript"></script>

    <script type="text/javascript">

        var _rootPATH = '<%=request.getContextPath()%>';
        var fileUploadCompleted = false;//文件是否上传完成
        var uuid = createUUID();
        var queueCount = 0;
        $(document).ready(function() {
            $("#uploadify").uploadify({
                'uploader'       : _rootPATH + '/script/uploadfile/uploadify.swf',
                'script'         : _rootPATH + '/fileupload?swfuuid='+uuid+'&',//servlet的路径或者.jsp 这是访问servlet 'scripts/uploadif'
                'method'         : 'GET',  //如果要传参数，就必须改为GET
                'cancelImg'      : _rootPATH + '/images/uploadfile/cancel.png',
                'buttonImg'		: '',
                'buttonText' 	: '浏览',
                'folder'         : '', //要上传到的服务器路径，
                'queueID'        : 'fileQueue',
                'auto'           : true, //选定文件后是否自动上传，默认false
                'multi'          : true, //是否允许同时上传多文件，默认false
                'simUploadLimit' : 200, //一次同步上传的文件数目
                'sizeLimit'      : 52428800, //设置单个文件大小限制500M，单位为byte
                'queueSizeLimit' : 200, //限制在一次队列中的次数（可选定几个文件）。默认值= 999，而一次可传几个文件有 simUploadLimit属性决定。
                'fileDesc'       : '', //如果配置了以下的'fileExt'属性，那么这个属性是必须的  : 支持格式:jpg或gif
                'fileExt'        : '',//允许的格式 : *.jpg;*.gif
                'scriptData'     :{'uploadtype':'swf','fileid':'','isConver':'false','custompath':''}, // 多个参数用逗号隔开 'name':$('#name').val(),'num':$('#num').val(),'ttl':$('#ttl').val()		   　

                onComplete: function (event, queueID, fileObj, response, data) {

                    var value = response;
                    var valarr = value.split(";");
                    var message = valarr[0];
                    var filelist = valarr[1];

                    if(message != ''){//如果有错误信息，则显示出来
                        alert("文件:" + fileObj.name + "上传错误 : " + message);
                    }else{

                        if(window.opener){
                            window.opener.document.getElementById("outparam").value = outparams;
                            window.opener.document.getElementById("uploadtxt").value = filelist;
                        }

                        window.opener.document.getElementById("uploadtxt").onchange();
                        fileUploadCompleted = true;
                        window.close();
                    }
                 },

                onSelect: function(event, queueID, fileObj){
                    var objsize = fileObj.size;
                    if(objsize > 52428800){
                        alert(fileObj.name + " 超出文件大小限制(50MB) ");
                        return;
                    }

                    $("#uploadify").uploadifySettings('scriptData',{'fileid':'','isConver':'false','custompath':''}, true);
                },

                onError: function(event, queueID, fileObj,errorObj) {
                    alert(errorObj.type + ":" + errorObj.info);
                }
            });
        });

        //判断窗口关闭前事件
        window.onbeforeunload = function(){

            if(!fileUploadCompleted){

                if(event.clientX > document.body.clientWidth && event.clientY < 0 || event.altKey){
                    return "文件尚未上传完成。";
                }else{
                    return "文件尚未上传完成。";
                }
            }else{

                return;
            }

        }

    </script>
</head>
<body>
<form enctype="multipart/form-data" method="get" style="width:720px">
    <div style='text-align:left;left:10px;position:relative;font:15px sans-serif'>请选择需要上传的文件:</div>
    <div style='text-align:left;left:10px;position:relative;margin-top:10px'>
        <input type="file" name="uploadify" id="uploadify" />
        <div id="fileQueue"></div>
    </div>
</form>
</body>
</html>