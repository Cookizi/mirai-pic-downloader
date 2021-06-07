package top.cookizi.bot.config.gsonAdapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.springframework.stereotype.Component;
import top.cookizi.bot.common.enums.MsgChainType;
import top.cookizi.bot.common.utils.JsonUtils;
import top.cookizi.bot.modle.msg.Msg;

import java.lang.reflect.Type;
import java.util.Locale;

@Component
public class MsgAdapter extends BaseJsonDeserializerAdapter<Msg> {

    @Override
    public Msg deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String type = json.getAsJsonObject().get("type").getAsString();
        MsgChainType msgChainType = MsgChainType.parse(type);
        return JsonUtils.parseObject(json, msgChainType.clazz);
    }

    @Override
    public Class<Msg> getClazz() {
        return Msg.class;
    }
}
