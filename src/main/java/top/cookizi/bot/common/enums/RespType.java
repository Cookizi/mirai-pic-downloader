package top.cookizi.bot.common.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RespType {
    DOWNLOAD_SUCCESS("下载成功，文件名：%s"),
    DOWNLOAD_FAIL("下载失败，文件名：%s，地址：%s")
    ;

    public final String respText;

    public String respText(Object... msg) {
        return String.format(this.respText, msg);
    }
}
