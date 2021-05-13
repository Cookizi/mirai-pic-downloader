/*
package top.cookizi.saver.data.enums;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.RemoteWebDriver;
import top.cookizi.saver.config.SeleniumConfig;

import java.io.File;

@Slf4j
@AllArgsConstructor
public enum DriverType {
    FIREFOX(),
   */
/* CHROME(new ChromeDriver(), new ChromeOptions()
//            .setHeadless(true)
            .setExperimentalOption("profile.managed_default_content_settings.images", 2)
            .setExperimentalOption("permissions.default.stylesheet", 2)
    )*//*
;


    public RemoteWebDriver driver(SeleniumConfig config) {

        File file = new File(config.getProfile());
        if (!file.exists()) {
            boolean mkdir = file.mkdir();
            log.info("创建配置文件夹结果：{}", mkdir);
        }

        Proxy p = new Proxy();
//        p.setProxyAutoconfigUrl(config.getProxy());
        p.setHttpProxy(config.getProxy());
        p.setSslProxy(config.getProxy());

        FirefoxDriver driver = new FirefoxDriver(new FirefoxOptions()
//            .setHeadless(true)//不启动浏览器
                .setProxy(p)
//                .setProfile(new FirefoxProfile(file))
                .addArguments("-profile", config.getProfile())
                .addArguments("-foreground")
//                .addPreference("browser.migration.version", 9001)//不加载css
//                .addPreference("permissions.default.image", 2)
        );

        return driver;
    }

    public static RemoteWebDriver getDriver(SeleniumConfig config) {
        for (DriverType type : DriverType.values()) {
            if (type.name().equalsIgnoreCase(config.getType())) return type.driver(config);
        }
        throw new RuntimeException("不支持的浏览器驱动类型:" + config.getType());
    }


}
*/
