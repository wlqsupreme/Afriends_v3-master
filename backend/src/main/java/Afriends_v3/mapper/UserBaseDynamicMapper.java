package Afriends_v3.mapper;

import Afriends_v3.entity.UserBaseDynamic_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户动态基础信息Mapper接口
 */
@Mapper
public interface UserBaseDynamicMapper extends BaseMapper<UserBaseDynamic_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_user_base_dynamic @heng")
    int countAllRecords();

    @Select("SELECT * FROM v2_user_base_dynamic @heng")
    java.util.List<UserBaseDynamic_njj> selectAllRecords();
}
