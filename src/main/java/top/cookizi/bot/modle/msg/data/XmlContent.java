package top.cookizi.bot.modle.msg.data;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@XmlRootElement(name = "msg")
public class XmlContent implements Serializable {
    String url;
    XmlItem item;

    @XmlAttribute
    public String getUrl() {
        return url;
    }

    @XmlElement
    public XmlItem getItem() {
        return item;
    }

    @Data
    public static class XmlItem {

        @XmlElement
        public XmlImage getImage() {
            return image;
        }

        XmlImage image;
    }

    @Data
    public static class XmlImage {
        String md5;
        String uuid;

        @XmlAttribute
        public String getMd5() {
            return md5;
        }

        @XmlAttribute
        public String getUuid() {
            return uuid;
        }
    }
}
