package top.cookizi.bot.service.afterDownload;

import java.util.List;

public abstract class AbstractAfterDownloadService {

    public abstract void process(String session, List<String> url);
}
