package cn.stylefeng.guns.modular.system.service;

import java.util.Arrays;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.system.entity.CustomerSource;
import cn.stylefeng.guns.modular.system.mapper.CustomerSourceMapper;

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
        if (!StringUtils.isEmpty(customerIdList)) {
            Arrays.asList(customerIdList.split(",")).stream().forEach(customerId -> {
                this.baseMapper.deleteShare(Long.parseLong(customerId), ShiroKit.getUserNotNull().getId());
                if (!StringUtils.isEmpty(userIdList)) {
                    Arrays.asList(userIdList.split(",")).stream().forEach(userId -> {
                        this.baseMapper.saveShare(Long.parseLong(customerId), Long.parseLong(userId), ShiroKit.getUserNotNull().getId());
                    });
                }
            });
        }

    }
}
