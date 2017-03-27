package orj.worf.util;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {

    private static final Gson gson = new GsonBuilder().create();
    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        VisibilityChecker<?> visibilityChecker = mapper.getSerializationConfig().getDefaultVisibilityChecker();
        mapper.setVisibilityChecker(visibilityChecker.withFieldVisibility(Visibility.ANY)
                .withCreatorVisibility(Visibility.NONE).withGetterVisibility(Visibility.NONE)
                .withSetterVisibility(Visibility.NONE).withIsGetterVisibility(Visibility.NONE));
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static <T> String toJSON(final T object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            return gson.toJson(object);
        }
    }

    public static <T> T parseJSON(final String json, final Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            return gson.fromJson(json, clazz);
        }
    }

    public static <T> T parseJSON(final String json, final Class<?> parametrized, final Class<?>... parameterClass) {
        try {
            JavaType type = mapper.getTypeFactory().constructParametricType(parametrized, parameterClass);
            return mapper.readValue(json, type);
        } catch (IOException e) {
            return null;
        }
    }
}
