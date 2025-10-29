package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 用户聊天详情实体类
 * 对应表: v3_user_chat_detail
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_chat_detail_r")
public class UserChatDetail_njj {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private Long sessionId;
    private Timestamp createdAt;
    private String demandParty;
    private String message;
    private String responseParty;
    private String senderType;
}
