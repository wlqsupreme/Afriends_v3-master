package Afriends_v3.mapper;

import Afriends_v3.entity.UserBase_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户基础Mapper接口
 */
@Mapper
public interface UserBaseMapper extends BaseMapper<UserBase_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_user_base")
    int countAllRecords();

    @Select("SELECT * FROM v2_user_base LIMIT 5")
    java.util.List<UserBase_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_user_base")
    java.util.List<UserBase_wlq> selectAllRecords();
}
