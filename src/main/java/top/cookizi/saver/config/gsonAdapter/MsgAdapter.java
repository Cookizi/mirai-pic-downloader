package top.cookizi.saver.config.gsonAdapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.springframework.stereotype.Component;
import top.cookizi.saver.data.enums.MsgChainType;
import top.cookizi.saver.data.msg.Msg;

import java.lang.reflect.Type;
import java.util.Locale;

@Component
public class MsgAdapter extends BaseJsonDeserializerAdapter<Msg> {
    @Override
    public Msg deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String type = json.getAsJsonObject().get("type").getAsString();
        MsgChainType msgChainType = MsgChainType.valueOf(type.toUpperCase(Locale.ROOT));
        return gson.fromJson(json, msgChainType.clazz);
    }

    @Override
    public Class<Msg> getClazz() {
        return Msg.class;
    }
}
