package top.cookizi.bot.modle.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Group {
    long id;
    String name;
    String permission;
}
