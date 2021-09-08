package top.cookizi.bot.modle.msg.data;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppContent {
    String app;
    String desc;
    Map<String, Map<String, Object>> meta;
    String prompt;
    String ver;
    String view;
}
