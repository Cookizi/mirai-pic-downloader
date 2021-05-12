/*
package top.cookizi.saver.cmd;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.RawCommand;
import net.mamoe.mirai.console.command.descriptor.CommandSignature;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.message.data.FriendImage;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.MiraiLogger;
import org.jetbrains.annotations.NotNull;
import top.cookizi.saver.PicSaver;
import top.cookizi.saver.data.enums.MsgType;
import top.cookizi.saver.utils.ImageUtil;

import java.util.List;


public final class FriendPicCmd extends JRawCommand {

    public static FriendPicCmd INSTANCE = new FriendPicCmd();


    MiraiLogger log = PicSaver.INSTANCE.logger;

    public FriendPicCmd() {
        super(PicSaver.INSTANCE, "test", new String[0],
                "没有使用说明"
                , "没有表述",
                PicSaver.INSTANCE.getParentPermission(),
                false);
        // 可选设置如下属性
//        setPermission(CommandPermission.Operator.INSTANCE)
//        setPrefixOptional(true)
    }


    public Object onCommand(@NotNull CommandSender commandSender, @NotNull MessageChain messageChain, @NotNull Continuation<? super Unit> continuation) {
        messageChain.stream()
                .peek(x -> log.info("------>" + x.toString()))
                .filter(x -> x instanceof FriendImage)
                .map(x -> ImageUtil.getImageInfo(x, MsgType.FRIEND))
                .forEach(x -> commandSender.sendMessage(x.toString()));


        return null;
    }

    @NotNull
    @Override
    public List<? extends CommandSignature> getOverloads() {
        return null;
    }
}
*/
