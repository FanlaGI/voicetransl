var root = window.location.protocol + '//' + window.location.host;
root = 'http://127.0.0.1:12001' //test
var INDEX;
$(document).ready(function(){
    var bindUpload = function() {
        $("#file").pekeUpload({
            dragMode: true,//开启拖拽
            dragText: "请拖拽音频文件到此区域！",//拖拽提示信息
            bootstrap: true,//自定义
            btnText: '上传音频',//按钮信息
            allowedExtensions: 'wav|flac|opus|m4a|mp3', //允许格式
            invalidExtError: '请选择正确的音频格式！', //格式不允许时提示信息
            maxSize: 0,//限制大小 0不限
            sizeError: '上传文件太大！',//超限提示
            showPreview: false, //显示预览
            showFilename: false, //显示预览名
            showPercent: true,//显示百分比
            showErrorAlerts: false,//显示错误提示
            errorOnResponse: '上传出错！', //错误提示信息
            onSubmit: false,//选择上传还是点击按钮上传
            url: root + '/genTask',//上传路径
            data: {},//附加参数
            limit: 1,//上传文件数量限制
            limitError: '文件数量超过限制！',//超量提示
            delfiletext: '移除！', //移除提示
            onFileError: function(file,data) { //上传失败回调
                layer.close(INDEX);
                console.log("失败：");
                console.log(file);
                console.log(data);
            },
            onFileSuccess: function(file,data) { //上传成功回调
                layer.close(INDEX);
                if (data.success) {
                    layer.msg("任务创建成功！请保存好任务ID以便查询进度和下载");
                    $('.file-name').text(data.body);
                    $('.task-id').text(data.message);
                    $('.inp').val(data.message);
                    $('.info').show();
                } else {
                    layer.msg(data.message);
                }
                $('.pkdragarea').remove();
                bindUpload();
            },
            before : function() {
                $('.info').hide();
                $('.file-name').text('');
                $('.task-id').text('');
                $('.inp').val('');
                INDEX = layer.msg('上传处理中，请稍候...', {
                    time: 0,
                    icon: 16,
                    shade: 0.1,
                    success: function (lay, index) {}
                });
            }
        });
    }
    bindUpload();


    $('.search-btn').click(function() {
        var taskId = $('.inp').val().trim();
        var fileName = $('.file-name').text().trim();
        if (taskId == '') {
            layer.msg("请输入任务ID");
        } else {
            $.ajax({
                type: 'GET',
                url:  root + '/getTransProgress',
                data: {taskId:taskId},
                dataType: 'json',
                timeout: 30000,
                success: function (data, textStatus, xhr) {
                    if (data.success) {
                        window.open(root + '/doExport?taskId=' + taskId + '&fileName=' + fileName);
                        layer.msg("下载成功！");
                    } else {
                        layer.msg(data.message);
                    }
                }
            });
        }
    });
});