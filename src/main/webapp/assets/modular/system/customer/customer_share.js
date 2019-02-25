layui.use(['layer', 'form', 'admin', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var layer = layui.layer;
    var userList = new Set();
    // 让当前iframe弹层高度适应
    admin.iframeAuto();

    form.on('checkbox', function(data){
        if(data.elem.checked){
            userList.add(data.value);
        }else{
            userList.delete(data.value);
        }
        console.log(data.elem); //得到checkbox原始DOM对象
        console.log(data.elem.checked); //是否被选中，true或者false
        console.log(userList); //复选框value值，也可以通过data.elem.value得到
    });
    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var userIdList="";
        userList.forEach(function (element, sameElement, set) {
            userIdList+=element+",";
        });
        data.field.userIdList = userIdList;
        var ajax = new $ax(Feng.ctxPath + "/customer/share", function (data) {
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