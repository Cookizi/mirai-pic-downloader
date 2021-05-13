package top.cookizi.bot.service.download;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.cookizi.bot.common.enums.MsgChainType;
import top.cookizi.bot.modle.msg.ImgMsg;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.resp.MsgResp;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class FriendImgDownloadService extends AbstractDownloadService {

    @Override
    public List<Msg> handleMsg(MsgResp msg) {
        return msg.getMessageChain().stream().filter(x -> MsgChainType.IMAGE.getType().equals(x.getType()))
                .map(x -> (ImgMsg) x).collect(Collectors.toList());
    }

    @Override
    public String handleFileName(Msg msg) {

        return ((ImgMsg) msg).getImageId().replaceAll("[{}]|\\.(jpg)|\\.(png)|\\.(git)", "");
    }

    @Override
    public String handleImageUrl(Msg msg) {
        return ((ImgMsg) msg).getUrl();
    }

    @Override
    public boolean isTypeMatch(MsgResp msg) {
        return msg.getMessageChain().stream().anyMatch(x -> MsgChainType.IMAGE.getType().equals(x.getType()));
    }


}
