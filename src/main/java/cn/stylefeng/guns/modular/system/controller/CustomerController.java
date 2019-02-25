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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.annotion.Permission;
import cn.stylefeng.guns.core.common.constant.dictmap.CustomerMap;
import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.system.entity.Customer;
import cn.stylefeng.guns.modular.system.entity.User;
import cn.stylefeng.guns.modular.system.model.CustomerDto;
import cn.stylefeng.guns.modular.system.service.CustomerService;
import cn.stylefeng.guns.modular.system.service.UserService;
import cn.stylefeng.guns.modular.system.warpper.CustomerWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;

/**
 * 客户控制器
 *
 * @author fengshuonan
 * @Date 2017-05-09 23:02:21
 */
@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController {

    private String PREFIX = "/modular/system/customer/";

    @Autowired
    private CustomerService customerService;
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
        return PREFIX + "customer.html";
    }

    /**
     * 跳转到添加客户
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping("/customer_add")
    public String CustomerAdd() {
        return PREFIX + "customer_add.html";
    }

    /**
     * 跳转到修改客户
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping("/customer_update")
    public String CustomerUpdate(@RequestParam Long customerId, Model model) {
        Customer customer = this.customerService.getById(customerId);
        model.addAllAttributes(BeanUtil.beanToMap(customer));
        LogObjectHolder.me().set(customer);
        return PREFIX + "customer_edit.html";
    }

    /**
     * 跳转到修改客户
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping("/customer_share")
    public String CustomerShare(@RequestParam Long customerId, Model model) {
        Customer customer = this.customerService.getById(customerId);
        model.addAttribute("customer", customer);
        LogObjectHolder.me().set(customer);
        List<User> userList = userService.list();
        userList = userList.stream()
                .filter(u -> !u.getUserId().equals(ShiroKit.getUserNotNull().getId()))
                .collect(Collectors.toList());
        model.addAttribute("userList", userList);
        return PREFIX + "customer_share.html";
    }

    /**
     * 跳转到修改客户
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping("/share")
    @ResponseBody
    public Object share(Customer customer,@RequestParam(name = "userIdList", required = false) String userIdList, Model model) {
        if (ToolUtil.isOneEmpty(customer.getCustomerId(), userIdList)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        customerService.saveShare(customer.getCustomerId(), userIdList);
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
    public Object list(Customer customer) {
        customer.setCreateUser(ShiroKit.getUserNotNull().getId());//只查自己做的
        Page<Map<String, Object>> list = this.customerService.list(customer);
        Page<Map<String, Object>> wrap = new CustomerWrapper(list).wrap();
        return LayuiPageFactory.createPageInfo(wrap);
    }



    /**
     * 新增客户
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增客户", key = "name", dict = CustomerMap.class)
    public Object add(Customer customer) {
        if (ToolUtil.isOneEmpty(customer, customer.getName(), customer.getContact())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        customer.setCreateUser(ShiroKit.getUserNotNull().getId());
        customer.setCreateTime(new Date());
        customer.setValid(1);
        this.customerService.save(customer);
        return SUCCESS_TIP;
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
        LogObjectHolder.me().set(ConstantFactory.me().getCustomerName(customerId));

        //逻辑删除
        Customer old = this.customerService.getById(customerId);
        old.setValid(0);
        this.customerService.updateById(old);
        //this.customerService.removeById(customerId);

        return SUCCESS_TIP;
    }

    /**
     * 修改客户
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改客户", key = "name", dict = CustomerMap.class)
    public Object update(Customer customer) {
        if (ToolUtil.isOneEmpty(customer, customer.getCustomerId(), customer.getName(), customer.getContact())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        Customer old = this.customerService.getById(customer.getCustomerId());
        BeanUtils.copyProperties(customer, old);
        old.setUpdateTime(new Date());
        old.setUpdateUser(ShiroKit.getUserNotNull().getId());
        this.customerService.updateById(old);
        return SUCCESS_TIP;
    }


    @RequestMapping(value = "/detail/{customerId}")
    @Permission
    @ResponseBody
    public Object detail(@PathVariable("customerId") Long customerId) {
        Customer customer = customerService.getById(customerId);
        CustomerDto customerDto = new CustomerDto();
        BeanUtil.copyProperties(customer, customerDto);
        return customerDto;
    }


}
