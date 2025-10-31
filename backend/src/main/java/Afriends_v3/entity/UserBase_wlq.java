package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 用户基础实体类
 * 对应表: v2_user_base
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_base")
public class UserBase_wlq {
    @TableId(value = "user_id", type = IdType.INPUT)
    private Long userId;
    
    @TableField("login_tel_account")
    private String loginTelAccount;
    
    @TableField("login_account")
    private String loginAccount;
    
    @TableField("login_wechat_account")
    private String loginWechatAccount;
    
    @TableField("login_qq_account")
    private String loginQqAccount;
    
    @TableField("bound_douyin_account")
    private String boundDouyinAccount;
    
    @TableField("machine_codes")
    private String machineCodes;
    
    @TableField("password_hash")
    private String passwordHash;
    
    @TableField("created_at")
    private Timestamp createdAt;
    
    @TableField("last_active")
    private Timestamp lastActive;
    
    @TableField("user_kind")
    private String userKind;
}
