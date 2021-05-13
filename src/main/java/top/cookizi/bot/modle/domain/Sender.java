package top.cookizi.bot.modle.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Sender {
    long id;
    String nickname;
    String remark;
    Group group;
}
