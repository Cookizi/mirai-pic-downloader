package top.cookizi.bot.modle.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VerifyResp extends BaseResponse{
    String msg;
}
