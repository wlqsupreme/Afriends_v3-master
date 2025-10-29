package Afriends_v3.mapper;

import Afriends_v3.entity.UserBaseLikeAction_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户基础点赞行为Mapper接口
 */
@Mapper
public interface UserBaseLikeActionMapper extends BaseMapper<UserBaseLikeAction_njj> {

    /**
     * 查询所有用户基础点赞行为记录（原生SQL）
     */
    @Select("SELECT * FROM v2_user_base_like_action @heng")
    List<UserBaseLikeAction_njj> selectAllRecords();
}
