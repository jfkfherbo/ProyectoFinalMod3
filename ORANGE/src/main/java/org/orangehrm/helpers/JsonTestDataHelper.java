package org.orangehrm.helpers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class JsonTestDataHelper {
    private static JsonTestDataHelper instance;
    private static final Logger logger = LogManager.getLogger(JsonTestDataHelper.class);

    private JsonTestDataHelper() { }

    public static JsonTestDataHelper getInstance() {
        if (instance == null) {
            synchronized (JsonTestDataHelper.class) {
                if (instance == null) {
                    instance = new JsonTestDataHelper();
                }
            }
        }
        return instance;
    }

    public <T> Object[] getTestData(String filePath, Class<T> type) throws FileNotFoundException {
        logger.info("Leyendo datos de prueba de {}", filePath);
        try (JsonReader reader = new JsonReader(new FileReader(filePath))) {
            List<T> data = new Gson().fromJson(reader,
                    TypeToken.getParameterized(ArrayList.class, type).getType());
            logger.info("Casos leidos: {}", data.size());
            return data.toArray();
        } catch (FileNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("No se pudo leer el JSON de empleados: " + filePath, e);
        }
    }
}
