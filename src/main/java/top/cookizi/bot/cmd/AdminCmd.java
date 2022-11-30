package top.cookizi.bot.cmd;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import top.cookizi.bot.dispatcher.MiraiCmdDispatcher;
import top.cookizi.bot.dispatcher.annotation.MiraiCmd;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdArg;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdDefine;
import top.cookizi.bot.dispatcher.config.CmdDefinition;
import top.cookizi.bot.dispatcher.config.CommandScope;
import top.cookizi.bot.service.download.twitter.TwitterFeatures;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;
import java.util.stream.Collectors;

@MiraiCmd
public class AdminCmd {

    @SneakyThrows
    @MiraiCmdDefine(name = "twitterFeature", scope = CommandScope.FRIEND, desc = "添加推特参数")
    public String addFeature(@MiraiCmdArg String name) {
        var filePath = TwitterFeatures.featurePath();
        JsonObject json;
        File file = new File(filePath);


        if (!file.exists()) {
            json = TwitterFeatures.creatDefaultFeatures();
        } else {
            try (var fileReader = new FileReader(file)) {
                json = new Gson().fromJson(fileReader, JsonObject.class);
            }
        }
        json.addProperty(name, false);

        try (var fileWriter = new FileWriter(file)) {
            fileWriter.write(json.toString());
            fileWriter.flush();
        }

        return "添加成功";
    }

    @Autowired
    private MiraiCmdDispatcher dispatcher;

    @MiraiCmdDefine(name = "help", scope = CommandScope.FRIEND, desc = "查询所有的命令")
    public String help() {
        Map<String, CmdDefinition> cmdMap = dispatcher.getCmdDefinitionMap();
        return cmdMap.values().stream().map(CmdDefinition::cmdInfoText).collect(Collectors.joining("\n"));
    }
}
