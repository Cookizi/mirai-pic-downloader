package top.cookizi.saver.data;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Sender {
    long id;
    String nickname;
    String remark;
}
