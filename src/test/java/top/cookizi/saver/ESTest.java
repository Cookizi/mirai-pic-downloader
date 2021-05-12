package top.cookizi.saver;

import com.google.gson.Gson;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import top.cookizi.saver.utils.ImagePHash;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ESTest {


    public static void main(String[] args) throws IOException {

        String files = "1.jpg,2.jpg,image31181D1E-0DFF-1E88-30D8-06D48EB67E24.mirai.jpg,imageA59D203F-3683-F712-67AD-FABDBF9DCF03.jpg,image40661B76-9BC0-DA43-3D8B-62771CE08369.jpg,image96FD8307-70BC-89D4-2A70-185C43A3E72E.jpg,image6262C763-B601-20CE-D306-2F63102555AE.jpg,image9FC1F182-2352-2ED7-87C5-828E9194D6D7.jpg";
        String path = "D:\\Desktop\\cf\\";

        Gson gson = new Gson();


        Connection connection = Jsoup.connect("http://localhost:9200/setu_hash/").method(Connection.Method.PUT);

        for (String f : files.split(",")) {
            try(BufferedInputStream stream=new BufferedInputStream(new FileInputStream(path+f))){
                String hash = ImagePHash.getHash(stream);
                String setu = gson.toJson(new SehtuHash(hash, f));
                String body = connection.requestBody(setu).execute().body();
                System.out.printf("文件名：%s，hash:%s，响应：%s%n", f, hash, body);
            }
        }
    }

    private static void computeHash() {
        String path = "D:\\Desktop\\cf";
        File file = new File(path);
        File[] imgs = file.listFiles();
        assert imgs != null;
        int count = 5;

        List<List<File>> imgList = subList(imgs, count);


        ExecutorService threadPool = Executors.newFixedThreadPool(count + 1);

        List<ImgHash> hashList = imgList.parallelStream().map(x -> threadPool.submit(() -> hash(x)))
                .map(x -> {
                    try {
                        return x.get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }).filter(Objects::nonNull).flatMap(Collection::stream)
                .collect(Collectors.toList());

        int size = hashList.size();


        List<List<String>> similarList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            List<String> similarImgList = new ArrayList<>();
            for (int j = i + 1; j < size; j++) {
                int distance = ImagePHash.distance(hashList.get(i).hash, hashList.get(j).hash);
                if (distance < 10) {
                    similarImgList.add(hashList.get(i).img.getName());
                    similarImgList.add(hashList.get(j).img.getName());
                }
            }
            if (!similarImgList.isEmpty()) {
                similarList.add(similarImgList);
            }
        }

        similarList.forEach(x -> {
            x.forEach(y -> System.out.println("\t" + y));
            System.out.println("--------------");
        });
    }

    private static List<List<File>> subList(File[] imgs, int count) {
        int l = imgs.length;
        int size = l / count;

        List<List<File>> list = new ArrayList<>();

        List<File> temp = new ArrayList<>();
        for (int i = 0; i < l; i++) {
            temp.add(imgs[i]);
            if (i % size == 0 && i > 0) {
                list.add(temp);
                temp = new ArrayList<>();
            }
        }
        return list;


    }

    private static List<ImgHash> hash(List<File> imgs) {
        List<ImgHash> list = new ArrayList<>();
        for (int i = 0; i < imgs.size(); i++) {
            long start = System.currentTimeMillis();
            try {
                File file = imgs.get(i);
                ImgHash imgHash = new ImgHash(ImagePHash.getHash(new BufferedInputStream(new FileInputStream(file))), file);
                list.add(imgHash);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            System.out.printf("线程%s第%s张hash计算完成，耗时%sms\n", Thread.currentThread().getName(), i, end - start);

        }
        return list;
    }

}

class ImgHash {
    String hash;
    File img;

    public ImgHash(String hash, File img) {
        this.hash = hash;
        this.img = img;
    }
}


class SehtuHash {
    public SehtuHash(String hash, String name) {
        char[] chars = hash.toCharArray();
        this.hash = new int[chars.length];
        for (int i = 0; i < chars.length; i++) {
            this.hash[i] = chars[i] - '0';
        }
        this.name = name;

    }

    int[] hash;
    String name;
    String id;
}