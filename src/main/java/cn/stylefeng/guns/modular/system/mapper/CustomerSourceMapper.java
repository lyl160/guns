package cn.stylefeng.guns.modular.system.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.stylefeng.guns.modular.system.entity.CustomerSource;

/**
 * <p>
 * 通知表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
public interface CustomerSourceMapper extends BaseMapper<CustomerSource> {

    /**
     * 获取通知列表
     */
    Page<Map<String,Object>> list(@Param("page") Page page, @Param("name") String name, @Param("contactTel") String contactTel, @Param("createUser") Long createUser);

    int deleteShare(@Param("customerId") Long customerId, @Param("createUser") Long id);
    int saveShare(@Param("customerId") Long customerId, @Param("userId") Long userId, @Param("createUser") Long id);
}
