package edu.nefu.webapp.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Util For Json String
 */
public class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将对象转换为Json字符串
     * @param object
     * @return json_string
     */
    public static String getJsonString(Object object) {
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage());
        }
        return jsonString;
    }

    /**
     * 从Json字符串中获取Map对象
     * @param jsonString
     * @return map
     */
    public static Map<String, Object> getMapFromJson(String jsonString) {
        Map<String, Object> map;
        try {
            map = objectMapper.readValue(jsonString, Map.class);
        } catch (IOException e) {
            map = null;
            logger.error(e.getLocalizedMessage());
        }
        return map;
    }

}
