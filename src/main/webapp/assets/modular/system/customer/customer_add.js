layui.use(['layer', 'form', 'admin', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var layer = layui.layer;
    // 让当前iframe弹层高度适应
    admin.iframeAuto();


    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var dictIdList = new Array();
        $("input:checkbox[name='dictId']:checked").each(function(i){
            dictIdList[i] = $(this).val();
        });
        data.field.projectDict = dictIdList.join(",");//将数组合并成字符串
        var ajax = new $ax(Feng.ctxPath + "/customer/add", function (data) {
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