layui.use(['layer', 'form', 'admin', 'ax','upload'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var layer = layui.layer;
    var upload = layui.upload;
    // 让当前iframe弹层高度适应
    admin.iframeAuto();
    //指定允许上传的文件类型
    upload.render({
        elem: '#uploadExcel',
        url: Feng.ctxPath + "/customerSource/import",
        accept: 'file', //普通文件
        exts: 'xls|xlsx', //上传文件类型
        number: 1,
        done: function(res){
            console.log(res);
            if (res.success) {
                Feng.success("导入成功！");
                //传给上个页面，刷新table用
                admin.putTempData('formOk', true);

                //关掉对话框
                admin.closeThisDialog();
            }else{
                Feng.error("导入失败！" + res.message);
            }
        },error: function(res){
            Feng.error("导入失败！服务异常");
        }
    });


    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var userIdList = new Array();
        $("input:checkbox[name='userId']:checked").each(function(i){
            userIdList[i] = $(this).val();
        });
        data.field.userIdList = userIdList.join(",");//将数组合并成字符串

        var ajax = new $ax(Feng.ctxPath + "/customerSource/share", function (data) {
            Feng.success("添加成功！");

            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);

            //关掉对话框
            admin.closeThisDialog();
        }, function (data) {
            Feng.error("添加失败！" + data.responseJSON.message)
        });
        ajax.set(data.field);
        ajax.start();
    });
});