package top.cookizi.bot.modle.msg.appMsg;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Meta {
    News news;
}
