package top.cookizi.saver.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import top.cookizi.saver.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
//@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "bot", ignoreInvalidFields = true)
public class AppConfig {
    String authKey;
    long qq;
    String wsUrl;
    String apiUrl;
    String savePath;
    String forwardGroups;
    String proxyUrl;

    public List<Long> getForwardGroups() {
        if (StringUtils.isBlank(forwardGroups)) {
            return new ArrayList<>();
        }
        return Arrays.stream(forwardGroups.split(","))
                .map(x -> {
                    try {
                        return Long.parseLong(x);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    public String getProxyHost() {
        return this.proxyUrl.split("//|:")[2];
    }

    public int getProxyPort() {
        return Integer.parseInt(this.proxyUrl.split(":")[2]);
    }

}
