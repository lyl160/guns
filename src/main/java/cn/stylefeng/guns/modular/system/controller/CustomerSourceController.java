/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.stylefeng.guns.modular.system.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.constant.dictmap.CustomerMap;
import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.core.common.constant.state.ManagerStatus;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.system.entity.CustomerSource;
import cn.stylefeng.guns.modular.system.entity.User;
import cn.stylefeng.guns.modular.system.model.CustomerSourceTemplate;
import cn.stylefeng.guns.modular.system.service.CustomerSourceService;
import cn.stylefeng.guns.modular.system.service.UserService;
import cn.stylefeng.guns.modular.system.warpper.CustomerWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ErrorResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;

/**
 * 客户控制器
 *
 * @author fengshuonan
 * @Date 2017-05-09 23:02:21
 */
@Controller
@RequestMapping("/customerSource")
public class CustomerSourceController extends BaseController {

    private String PREFIX = "/modular/system/customerSource/";

    @Autowired
    private CustomerSourceService customerSourceService;
    @Autowired
    private UserService userService;

    /**
     * 跳转到客户列表首页
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("currentUserId", ShiroKit.getUserNotNull().getId());
        model.addAttribute("canImport", ShiroKit.isQueryer() || ShiroKit.isAdmin());
        return PREFIX + "source.html";
    }

    /**
     * 跳转到添加客户
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping("/customer_import")
    public String CustomerImport(Model model) {
        return PREFIX + "source_import.html";
    }

    @RequestMapping("/customer_share")
    public String CustomerShare(@RequestParam String customerIdList, Model model) {
        List<User> userList = userService.list();
        userList = userList.stream()
                .filter(u -> !u.getUserId().equals(ShiroKit.getUserNotNull().getId()) && u.getStatus().equals(ManagerStatus.OK.getCode()))
                .collect(Collectors.toList());
        model.addAttribute("userList", userList);
        model.addAttribute("customerIdList", customerIdList);
        return PREFIX + "source_share.html";
    }

    @RequestMapping("/import")
    @ResponseBody
    public Object uploadImport(@RequestParam MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 判断文件名是否为空
        if (file == null)
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        Map<String, Object> map = new HashMap<String, Object>();
        // 获取文件名
        String name = file.getOriginalFilename();
        // 判断文件大小、即名称
        long size = file.getSize();
        if (name == null || ("").equals(name) && size == 0)
            throw new ServiceException(BizExceptionEnum.FILE_ERROR);
        List<CustomerSourceTemplate> dataList = ExcelImportUtil.importExcel(file.getInputStream(), CustomerSourceTemplate.class,
                new ImportParams());
        if (CollectionUtils.isNotEmpty(dataList)) {
            dataList = dataList.stream().filter(t -> StringUtils.isNotEmpty(t.getName()) && StringUtils.isNotEmpty(t.getContactTel())).collect(Collectors.toList());
            //自身按名称去重
            List<CustomerSourceTemplate> dataListWithoutRepeat = new LinkedList<>();
            for (CustomerSourceTemplate t : dataList) {
                boolean b = dataListWithoutRepeat.stream().anyMatch(u -> u.getName().equals(t.getName()));
                if (!b) {
                    dataListWithoutRepeat.add(t);
                }
            }

            if (CollectionUtils.isNotEmpty(dataListWithoutRepeat)) {
                customerSourceService.saveImport(dataListWithoutRepeat);
            } else {
                return new ErrorResponseData("表格无数据");
            }
        } else {
            return new ErrorResponseData("表格无数据");
        }
        return SUCCESS_TIP;
    }

    /**
     * 页面分享客户
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping("/share")
    @ResponseBody
    public Object share(@RequestParam(name = "customerIdList", required = false) String customerIdList,@RequestParam(name = "userIdList", required = false) String userIdList, Model model) {
        if (ToolUtil.isOneEmpty(customerIdList)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        customerSourceService.saveShare(customerIdList, userIdList);
        return SUCCESS_TIP;
    }



    /**
     * 获取客户列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(CustomerSource customer) {
        if (ShiroKit.isQueryer() || ShiroKit.isAdmin()) {
        } else {
            customer.setCreateUser(ShiroKit.getUserNotNull().getId());//只查自己做的
        }
        Page<Map<String, Object>> list = this.customerSourceService.list(customer);
        Page<Map<String, Object>> wrap = new CustomerWrapper(list).wrap();
        return LayuiPageFactory.createPageInfo(wrap);
    }


    /**
     * 删除客户
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    @BussinessLog(value = "删除客户", key = "customerId", dict = CustomerMap.class)
    public Object delete(@RequestParam Long customerId) {

        //缓存客户名称
        LogObjectHolder.me().set(ConstantFactory.me().getCustomerSourceName(customerId));

        //逻辑删除
        CustomerSource old = this.customerSourceService.getById(customerId);
        old.setValid(0);
        this.customerSourceService.updateById(old);

        return SUCCESS_TIP;
    }




}
