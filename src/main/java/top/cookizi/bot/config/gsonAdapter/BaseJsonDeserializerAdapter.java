package top.cookizi.bot.config.gsonAdapter;

import com.google.gson.JsonDeserializer;


public abstract class BaseJsonDeserializerAdapter<T> implements JsonDeserializer<T> {

    protected Class<T> clazz;

    public BaseJsonDeserializerAdapter() {
        this.clazz = getClazz();
    }

    public abstract Class<T> getClazz();

}
