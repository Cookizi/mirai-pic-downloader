package top.cookizi.bot.modle.msg.appMsg;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppContent {
    String app;
    Config config;
    String desc;
    Extra extra;
    Meta meta;
    String prompt;
    String ver;
    String view;
}
