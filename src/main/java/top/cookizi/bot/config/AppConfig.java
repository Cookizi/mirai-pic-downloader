package top.cookizi.bot.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.cookizi.bot.common.utils.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
//@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "bot", ignoreInvalidFields = true)
public class AppConfig {
    long admin;
    String authKey;
    long qq;
    String wsUrl;
    String apiUrl;
    String savePath;
    String forwardGroups;
    String cmdPrefix = "/";
    boolean proxy;
    String proxyHost;
    int proxyPort;
    String miraiHttpApiPath;
    int setuRate = 25;
    Map<String, String> headers;
    Map<String, String> cookies;

    List<Long> jobWhiteGroup = new ArrayList<>();

    public boolean isAdmin(long qq){
        return this.admin == qq;
    }

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

}
