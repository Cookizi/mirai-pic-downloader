package top.cookizi.saver.data.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AuthResp extends BaseResponse{
    String session;
}
