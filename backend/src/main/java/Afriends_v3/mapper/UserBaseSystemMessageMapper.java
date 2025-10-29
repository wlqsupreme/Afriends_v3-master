package Afriends_v3.mapper;

import Afriends_v3.entity.UserBaseSystemMessage_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户基础系统消息Mapper接口
 */
@Mapper
public interface UserBaseSystemMessageMapper extends BaseMapper<UserBaseSystemMessage_njj> {

    /**
     * 查询所有用户基础系统消息记录（原生SQL）
     */
    @Select("SELECT * FROM v2_user_base_system_message @heng")
    List<UserBaseSystemMessage_njj> selectAllRecords();
}
