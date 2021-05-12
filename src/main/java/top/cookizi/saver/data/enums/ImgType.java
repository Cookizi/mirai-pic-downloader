package top.cookizi.saver.data.enums;

public enum ImgType {
    ORIGIN("原图", "origUrl"),
    THUMB("缩略图", "thumbUrl"),
    ;
    public final String typeCn;
    public final String fieldName;

    ImgType(String typeCn, String fieldName) {
        this.typeCn = typeCn;
        this.fieldName = fieldName;
    }
}
