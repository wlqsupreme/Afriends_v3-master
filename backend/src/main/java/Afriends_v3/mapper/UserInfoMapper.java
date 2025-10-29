package Afriends_v3.mapper;

import Afriends_v3.entity.UserInfo_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * 用户信息Mapper接口
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Update("UPDATE v2_user_info SET gold = #{newGold} WHERE user_id = #{userId}")
    int updateGold(Long userId, Long newGold);
}
