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
            {field: 'contactTel',width:'11%', sort: true, title: '联系方式'},
            {field: 'contactTel2',width:'11%', sort: true, title: '联系方式2'},
            {field: 'contactTel3',width:'11%', sort: true, title: '联系方式3'},
            {field: 'createrName', sort: true, title: '添加者'},
            {field: 'createTime',width:'15%', sort: true, title: '创建时间'}
            // {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
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
     * 点击分享客户
     *
     * @param data 点击按钮时候的行数据
     */
    Customer.openShareCustomer = function (data) {
        var checkRows = table.checkStatus(Customer.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要分配的客户");
        } else {
            console.info(checkRows.data);

            var customerIdList = new Array();
            checkRows.data.forEach(function(i){
                customerIdList.push(i.customerId);
            })
            admin.putTempData('formOk', false);
            top.layui.admin.open({
                type: 2,
                title: '分配客户',
                content: Feng.ctxPath + '/customerSource/customer_share?customerIdList=' + customerIdList.join(","),
                end: function () {
                    admin.getTempData('formOk') && table.reload(Customer.tableId);
                }
            });
        }
    };
    /**
     * 导入客户
     *
     * @param data 点击按钮时候的行数据
     */
    Customer.onImport = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '导入客户来源',
            content: Feng.ctxPath + '/customerSource/customer_import',
            end: function () {
                admin.getTempData('formOk') && table.reload(Customer.tableId);
            }
        });
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Customer.tableId,
        url: Feng.ctxPath + '/customerSource/list',
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
    $('#btnShare').click(function () {
        Customer.openShareCustomer();
    });

    // 工具条点击事件
    table.on('tool(' + Customer.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        // if (layEvent === 'dettail') {
        //     Customer.onEditCustomer(data);
        // }
    });
});
