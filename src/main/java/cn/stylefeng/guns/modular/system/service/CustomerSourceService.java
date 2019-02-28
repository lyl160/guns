package cn.stylefeng.guns.modular.system.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.system.entity.CustomerSource;
import cn.stylefeng.guns.modular.system.entity.CustomerSourceUser;
import cn.stylefeng.guns.modular.system.entity.User;
import cn.stylefeng.guns.modular.system.mapper.CustomerSourceMapper;
import cn.stylefeng.guns.modular.system.model.CustomerSourceTemplate;

/**
 * <p>
 * 通知表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
@Service
public class CustomerSourceService extends ServiceImpl<CustomerSourceMapper, CustomerSource> {

    @Autowired
    UserService userService;
    @Autowired
    CustomerSourceUserService customerSourceUserService;
    /**
     * 获取通知列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:05 PM
     */
    public Page<Map<String, Object>> list(CustomerSource customer) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.list(page, customer.getName(),customer.getContactTel(),customer.getCreateUser());
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveShare(String customerIdList, String userIdList) {
        if (StringUtils.isNotEmpty(customerIdList)) {
            Arrays.asList(customerIdList.split(",")).stream().forEach(customerId -> {
                this.baseMapper.deleteShare(Long.parseLong(customerId), ShiroKit.getUserNotNull().getId());
                if (StringUtils.isNotEmpty(userIdList)) {
                    Arrays.asList(userIdList.split(",")).stream().forEach(userId -> {
                        this.baseMapper.saveShare(Long.parseLong(customerId), Long.parseLong(userId), ShiroKit.getUserNotNull().getId());
                    });
                }
            });
        }

    }


    public boolean deleteRepeat(){
        int i = this.baseMapper.deleteRepeat();
        return i > 0 ? true : false;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveImport(List<CustomerSourceTemplate> dataList) {
        Long currentUserId = ShiroKit.getUser().getId();
        List<CustomerSource> customerSourceList = new ArrayList<>();
        String[] contactTelArray = null;
        Date createDate = new Date();
        for (CustomerSourceTemplate template : dataList) {
            CustomerSource c = new CustomerSource();
            c.setName(template.getName());
            contactTelArray = template.getContactTel().split(",");
            if (contactTelArray.length >= 1) {
                c.setContactTel(contactTelArray[0]);
            }
            if (contactTelArray.length >= 2) {
                c.setContactTel2(contactTelArray[1]);
            }
            if (contactTelArray.length >= 3) {
                c.setContactTel3(contactTelArray[2]);
            }
            c.setCreateTime(createDate);
            c.setCreateUser(ShiroKit.getUserNotNull().getId());
            c.setValid(1);
            c.setUserName(template.getUserName());
            customerSourceList.add(c);
        }
        boolean batchFlag = this.saveBatch(customerSourceList);
        //数据库端按公司名称去重
        this.deleteRepeat();
        if (batchFlag) {
            //保存用户关联
            List<User> userList = userService.list();//所有用户
            User user = null;
            List<CustomerSourceUser> csUserList = new ArrayList<>();
            for (CustomerSource source : customerSourceList) {
                if (StringUtils.isNotEmpty(source.getUserName())) {
                    for (String name : Arrays.asList(source.getUserName().split(","))) {
                        user = getUserIdByUserName(name, userList);
                        if (user != null) {
                            CustomerSourceUser csUser = new CustomerSourceUser();
                            csUser.setCustomerId(source.getCustomerId());
                            csUser.setUserId(user.getUserId());
                            csUser.setCreateUser(currentUserId);
                            csUser.setCreateTime(createDate);
                            csUser.setValid(1);
                            csUserList.add(csUser);
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(csUserList)) {
                //先清除公司的历史分配
                for (CustomerSourceUser c : csUserList) {
                    this.baseMapper.deleteShare(c.getCustomerId(), currentUserId);
                }
                //保存用户分配信息
                customerSourceUserService.saveBatch(csUserList);
            }

        }
    }

    private User getUserIdByUserName(String name, List<User> userList) {
        List<User> result = userList.stream().filter(u -> u.getName().equals(name)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(result)) {
            return result.get(0);
        }
        return null;
    }
}
