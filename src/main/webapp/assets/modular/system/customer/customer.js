layui.use(['layer', 'form', 'table', 'admin', 'ax'], function () {
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;

    /**
     * 系统管理--消息管理
     */
    var Customer = {
        tableId: "customerTable"    //表格id
    };

    /**
     * 初始化表格的列
     */
    Customer.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'customerId', hide: true, sort: true, title: 'id'},
            {field: 'name',width:'30%', sort: true, title: '公司名称'},
            {field: 'contact',width:'10%', sort: true, title: '联系人'},
            {field: 'contactTel',width:'11%', sort: true, title: '联系方式'},
            {field: 'statusName', sort: true, title: '状态'},
            {field: 'createrName', sort: true, title: '添加者'},
            {field: 'createTime',width:'15%', sort: true, title: '创建时间'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Customer.search = function () {
        var queryData = {};
        queryData['name'] = $("#name").val();
        queryData['contactTel'] = $("#contactTel").val();
        table.reload(Customer.tableId, {where: queryData});
    };

    /**
     * 弹出添加客户
     */
    Customer.openAddCustomer = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            area : ['800px', '300px'],
            offset: '30px',
            title: '添加客户',
            content: Feng.ctxPath + '/customer/customer_add',
            end: function () {
                admin.getTempData('formOk') && table.reload(Customer.tableId);
            }
        });
    };

    /**
     * 点击编辑客户
     *
     * @param data 点击按钮时候的行数据
     */
    Customer.onEditCustomer = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            area : ['800px', '300px'],
            offset: '30px',
            title: '修改客户',
            content: Feng.ctxPath + '/customer/customer_update?customerId=' + data.customerId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Customer.tableId);
            }
        });
    };

    /**
     * 点击删除客户
     *
     * @param data 点击按钮时候的行数据
     */
    Customer.onDeleteCustomer = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/customer/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(Customer.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("customerId", data.customerId);
            ajax.start();
        };
        Feng.confirm("是否删除客户 " + data.name + "?", operation);
    };
    /**
     * 点击分享客户
     *
     * @param data 点击按钮时候的行数据
     */
    Customer.onShareCustomer = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '分享客户',
            content: Feng.ctxPath + '/customer/customer_share?customerId=' + data.customerId,
            end: function () {
                admin.getTempData('formOk') && table.reload(Customer.tableId);
            }
        });
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Customer.tableId,
        url: Feng.ctxPath + '/customer/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: Customer.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Customer.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Customer.openAddCustomer();
    });

    // 工具条点击事件
    table.on('tool(' + Customer.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            Customer.onEditCustomer(data);
        } else if (layEvent === 'delete') {
            Customer.onDeleteCustomer(data);
        } else if (layEvent === 'share') {
            Customer.onShareCustomer(data);
        }
    });
});
