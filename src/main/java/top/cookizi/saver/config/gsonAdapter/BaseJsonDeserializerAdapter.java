package top.cookizi.saver.config.gsonAdapter;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;


public abstract class BaseJsonDeserializerAdapter<T> implements JsonDeserializer<T> {
    protected Gson gson = new Gson();
    protected Class<T> clazz;

    public BaseJsonDeserializerAdapter() {
        this.clazz = getClazz();
    }

    public abstract Class<T> getClazz();
}
