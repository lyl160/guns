package cn.stylefeng.guns.modular.system.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.system.entity.Customer;
import cn.stylefeng.guns.modular.system.mapper.CustomerMapper;

/**
 * <p>
 * 通知表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
@Service
public class CustomerService extends ServiceImpl<CustomerMapper, Customer> {

    /**
     * 获取通知列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:05 PM
     */
    public Page<Map<String, Object>> list(Customer customer) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.list(page, customer.getName(),customer.getContactTel());
    }
}
