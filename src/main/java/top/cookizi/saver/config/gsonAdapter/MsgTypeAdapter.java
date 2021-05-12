package top.cookizi.saver.config.gsonAdapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.springframework.stereotype.Component;
import top.cookizi.saver.data.enums.MsgType;
import top.cookizi.saver.data.resp.MsgResp;

import java.lang.reflect.Type;

//@Component
public class MsgTypeAdapter extends BaseJsonDeserializerAdapter<MsgResp> {
    @Override
    public MsgResp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String type = json.getAsJsonObject().get("type").getAsString();
        MsgType msgType = MsgType.parse(type);
        return gson.fromJson(json, msgType.clazz);
    }

    @Override
    public Class<MsgResp> getClazz() {
        return MsgResp.class;
    }
}
