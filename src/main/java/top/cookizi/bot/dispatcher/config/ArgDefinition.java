package top.cookizi.bot.dispatcher.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArgDefinition {
    int order;
    Class<?> paramClass;

    public String info(){
        return String.format("顺序:%s, 类型:%s", order, paramClass.getName());
    }
}
