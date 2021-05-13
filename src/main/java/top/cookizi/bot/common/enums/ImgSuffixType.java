package top.cookizi.bot.common.enums;

public enum ImgSuffixType {
    JPG("jpg", new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF}),//FF D8 FF E0
    PNG("png", new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A}),//89 50 4E 47
    GIF("gif", new byte[]{0x47, 0x49, 0x46}),//47 49 46 38
    OTHER("null", new byte[]{}),
    ;


    ImgSuffixType(String suffix, byte[] header) {
        this.suffix = suffix;
        this.header = header;
    }

    public final String suffix;
    private final byte[] header;


    public static ImgSuffixType checkType(byte[] f) {

        typeChecker:
        for (ImgSuffixType type : ImgSuffixType.values()) {
            for (int i = 0; i < type.header.length; i++) {
                if (f[i] != type.header[i]) continue typeChecker;
            }
            return type;
        }
        return OTHER;
    }

    public String getName(String filename) {
        return filename + "." + this.suffix;
    }

}