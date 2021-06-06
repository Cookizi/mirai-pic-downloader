package top.cookizi.bot.modle.msg.appMsg;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Extra {
    int app_type;
    long appid;
    long uin;//QQ号
}
