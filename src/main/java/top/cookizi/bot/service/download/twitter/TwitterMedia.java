package top.cookizi.bot.service.download.twitter;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
class TwitterMedia {
    String type;
    String media_url_https;
    Map<String, Size> sizes;
    VideoInfo video_info;

    public String toDownloadUrl() {
        if ("photo".equalsIgnoreCase(type)) {
            log.debug("推文为图片类型，开始解析图片地址");
            return readPhotoUrl();
        } else {
            log.debug("推文为视频类型，开始解析视频地址");
            return readVideoUrl();
        }
    }

    private String readVideoUrl() {
        return video_info.getVariants().stream()
                .max(Comparator.comparingInt(Variants::getSize))
                .map(Variants::getUrl)
                .orElse(null);
    }

    private String readPhotoUrl() {
        String type = media_url_https.substring(media_url_https.length() - 3);
        String size = sizes.entrySet().stream()
                .max(Comparator.comparingInt(a -> a.getValue().getSize()))
                .map(Map.Entry::getKey)
                .orElse(null);
        return String.format("%s?format=%s&name=%s", media_url_https, type, size);
    }

}

@Data
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
class Size {
    int h, w;

    public int getSize() {
        return h * w;
    }
}

@Data
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
class VideoInfo {
    List<Variants> variants;
}

@Data
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
class Variants {
    String url;

    public int getSize() {
        Pattern pattern = Pattern.compile("(\\d+x\\d+)");
        Matcher matcher = pattern.matcher(this.url);
        if (matcher.find()) {
            String group = matcher.group(1);
            String[] size = group.split("x");
            return Integer.parseInt(size[0]) * Integer.parseInt(size[1]);
        }
        return -1;
    }
}