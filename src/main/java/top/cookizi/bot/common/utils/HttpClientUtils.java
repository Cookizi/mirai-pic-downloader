package top.cookizi.bot.common.utils;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class HttpClientUtils {

    public static String httpGet(String url) {
        return httpGet(url, 3000, 3000);
    }

    public static String httpGet(String url, int socketTimeOut, int connectTimeOut) {
        String body = null;
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;

        try {
            Preconditions.checkArgument(url.length() > 0, "url is:''");
            httpclient = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeOut).setConnectTimeout(connectTimeOut).build();

            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);
            httpGet.setURI(new URI(httpGet.getURI().toString()));
            response = httpclient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            log.info("http status code {}", response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                body = EntityUtils.toString(entity, Consts.UTF_8);
            }

            EntityUtils.consume(entity);
        } catch (URISyntaxException | IllegalArgumentException | IOException var21) {
            log.error("Can't send httpGet... " + var21.getMessage(), var21);
        } finally {
            if (null != response) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException var20) {
                    log.error("Can't send httpGet... " + var20.getMessage(), var20);
                }
            }

        }
        return body;
    }

    /**
     * post请求传输json参数
     *
     * @param url       url地址
     * @param jsonParam 参数
     * @return
     */
    public static String httpPost(String url, String jsonParam, int socketTimeOut, int connectTimeOut) {
        // post请求返回结果
        String body = null;
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;

        try {
            Preconditions.checkArgument(url.length() > 0, "url is:''");
            httpclient = HttpClients.createDefault();
            // 设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeOut).setConnectTimeout(connectTimeOut).build();

            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            if (!StringUtils.isEmpty(jsonParam)) {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }

            // 请求发送成功，并得到响应
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            log.info("http status code {}", response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                body = EntityUtils.toString(entity, Consts.UTF_8);
            }

            EntityUtils.consume(entity);
        } catch (IllegalArgumentException | IOException var21) {
            log.error("Can't send httpPost... " + var21.getMessage(), var21);
        } finally {
            if (null != response) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException var20) {
                    log.error("Can't send httpPost... " + var20.getMessage(), var20);
                }
            }
        }
        return body;
    }

//    /**
//     * post请求传输String参数 例如：name=Jack&sex=1&type=2
//     * Content-type:application/x-www-form-urlencoded
//     *
//     * @param url      url地址
//     * @param strParam 参数
//     * @return
//     */
//    public static String httpPost(String url, String strParam, int socketTimeOut, int connectTimeOut) {
//        // post请求返回结果
//        String body = null;
//        CloseableHttpClient httpclient = null;
//        CloseableHttpResponse response = null;
//
//        try {
//            Preconditions.checkArgument(url.length() > 0, "url is:''");
//            httpclient = HttpClients.createDefault();
//            // 设置请求和传输超时时间
//            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeOut).setConnectTimeout(connectTimeOut).build();
//
//            HttpPost httpPost = new HttpPost(url);
//            httpPost.setConfig(requestConfig);
//
//            if (null != strParam) {
//                // 解决中文乱码问题
//                StringEntity entity = new StringEntity(strParam, "utf-8");
//                entity.setContentEncoding("UTF-8");
//                entity.setContentType("application/x-www-form-urlencoded");
//                httpPost.setEntity(entity);
//            }
//            // 请求发送成功，并得到响应
//            response = httpclient.execute(httpPost);
//            HttpEntity entity = response.getEntity();
//            log.info("http status code {}", response.getStatusLine().getStatusCode());
//            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                body = EntityUtils.toString(entity, Consts.UTF_8);
//            }
//
//            EntityUtils.consume(entity);
//        } catch (IllegalArgumentException | IOException var21) {
//            log.error("Can't send httpPost... " + var21.getMessage(), var21);
//        } finally {
//            if (null != response) {
//                try {
//                    EntityUtils.consume(response.getEntity());
//                } catch (IOException var20) {
//                    log.error("Can't send httpPost... " + var20.getMessage(), var20);
//                }
//            }
//        }
//        return body;
//    }

}
