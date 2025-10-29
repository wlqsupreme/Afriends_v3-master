package Afriends_v3.mapper;

import Afriends_v3.entity.UserReviewBase_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户评论基础Mapper接口
 */
@Mapper
public interface UserReviewBaseMapper extends BaseMapper<UserReviewBase_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_user_review_base")
    int countAllRecords();

    @Select("SELECT * FROM v2_user_review_base")
    java.util.List<UserReviewBase_njj> selectAllRecords();
}
