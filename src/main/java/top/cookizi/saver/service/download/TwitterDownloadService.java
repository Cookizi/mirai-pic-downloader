/*
package top.cookizi.saver.service.download;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.cookizi.saver.config.SeleniumConfig;
import top.cookizi.saver.data.enums.DriverType;
import top.cookizi.saver.data.msg.Msg;
import top.cookizi.saver.data.resp.MsgResp;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class TwitterDownloadService extends AbstractDownloadService {

    @Autowired
    private SeleniumConfig seleniumConfig;

    */
/**
     * 根据传入的地址拿到图片的地址
     *
     * @param url 分享的地址
     * @return 解析出来的图片地址
     *//*

    public List<String> getImgUrl(String url) {
        System.setProperty("webdriver.gecko.driver", seleniumConfig.getPath());

//        String id = getId(url);
        RemoteWebDriver driver = DriverType.getDriver(seleniumConfig);

        try {
            driver.manage().timeouts().implicitlyWait(seleniumConfig.getTimeout(), TimeUnit.SECONDS);
            driver.get(url);
        } catch (Exception e) {
            e.printStackTrace();
            driver.quit();
            return null;
        }

        try {
            Thread.sleep(seleniumConfig.getTimeout() * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        WebElement element = driver.findElement(By.cssSelector("article article div[role='button']"));// /span[contains(text(), '查看')]
        if (element != null) {
            element.click();

            try {
                Thread.sleep(seleniumConfig.getTimeout() * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        List<WebElement> tweets = driver.findElements(By.cssSelector("article div[data-testid='tweet']"));
        List<String> imgList = tweets.get(0).findElements(By.cssSelector("a img[src^='https://pbs.twimg.com/media/']"))
                .stream().map(x -> x.getAttribute("src"))
                //.filter(x -> x != null && x.startsWith("https://pbs.twimg.com/media/"))
                .collect(Collectors.toList());

        return imgList;

    }

   */
/* @NotNull
    private List<String> getImgUrls(String id) throws IOException {
        Request request = new Request.Builder()
                .url(TWITTER_API_URL + id)
                .get().build();

        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        String resp = response.body().string();
        JsonObject jsonObject = new Gson().fromJson(resp, JsonObject.class);

        List<String> imgUrlList = new ArrayList<>();

        //obj.globalObjects.tweets.${id}.entities.media[].media_url
        jsonObject.get("globalObjects").getAsJsonObject()
                .get("tweets").getAsJsonObject()
                .get(id).getAsJsonObject()
                .get("entities").getAsJsonObject()
                .get("media").getAsJsonArray()
                .forEach(x -> imgUrlList.add(x.getAsJsonObject().get("media_url").getAsString()));
        return imgUrlList;
    }*//*


    private String getId(String url) {
        String[] split = url.split("/+|\\?");
        return split[4];
    }


    @Override
    public List<Msg> handleMsg(MsgResp msg) {
        return null;
    }

    @Override
    public String handleFileName(Msg msg) {
        return null;
    }

    @Override
    public String handleImageUrl(Msg msg) {
        return null;
    }

    @Override
    public boolean isTypeMatch(MsgResp msg) {
        return false;
    }
}
*/
