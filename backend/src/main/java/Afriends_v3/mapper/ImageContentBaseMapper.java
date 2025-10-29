package Afriends_v3.mapper;

import Afriends_v3.entity.ImageContentBase_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 图片内容基础信息Mapper接口
 */
@Mapper
public interface ImageContentBaseMapper extends BaseMapper<ImageContentBase_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_image_content_base_@niu")
    int countAllRecords();

    @Select("SELECT * FROM v2_image_content_base_@niu LIMIT 5")
    java.util.List<ImageContentBase_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_image_content_base_@niu")
    java.util.List<ImageContentBase_wlq> selectAllRecords();
}
