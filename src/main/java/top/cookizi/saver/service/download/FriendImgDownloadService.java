package top.cookizi.saver.service.download;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.cookizi.saver.data.enums.MsgChainType;
import top.cookizi.saver.data.msg.ImgMsg;
import top.cookizi.saver.data.msg.Msg;
import top.cookizi.saver.data.resp.MsgResp;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class FriendImgDownloadService extends AbstractDownloadService {

    @Override
    public List<Msg> handleMsg(MsgResp msg) {
        return msg.getMessageChain().stream().filter(x -> MsgChainType.IMAGE.type.equals(x.getType()))
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
        return msg.getMessageChain().stream().anyMatch(x -> MsgChainType.IMAGE.type.equals(x.getType()));
    }


}
