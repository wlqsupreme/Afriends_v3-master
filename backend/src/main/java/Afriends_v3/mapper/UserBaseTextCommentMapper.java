package Afriends_v3.mapper;

import Afriends_v3.entity.UserBaseTextComment_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户基础文本评论Mapper接口
 */
@Mapper
public interface UserBaseTextCommentMapper extends BaseMapper<UserBaseTextComment_njj> {

    /**
     * 查询所有用户基础文本评论记录（原生SQL）
     */
    @Select("SELECT * FROM v2_user_base_text_comment @heng")
    List<UserBaseTextComment_njj> selectAllRecords();
}
