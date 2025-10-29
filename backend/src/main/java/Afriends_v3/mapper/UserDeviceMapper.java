package Afriends_v3.mapper;

import Afriends_v3.entity.UserDevice_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户设备Mapper接口
 */
@Mapper
public interface UserDeviceMapper extends BaseMapper<UserDevice_njj> {

    /**
     * 查询所有用户设备（原生SQL）
     */
    @Select("SELECT * FROM v3_user_device")
    List<UserDevice_njj> selectAllRecords();
}