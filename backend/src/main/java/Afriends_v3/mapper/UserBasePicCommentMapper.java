package Afriends_v3.mapper;

import Afriends_v3.entity.UserBasePicComment_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户基础图片评论Mapper接口
 */
@Mapper
public interface UserBasePicCommentMapper extends BaseMapper<UserBasePicComment_njj> {

    /**
     * 查询所有用户基础图片评论记录（原生SQL）
     */
    @Select("SELECT * FROM v2_user_base_pic_comment @heng")
    List<UserBasePicComment_njj> selectAllRecords();
}
