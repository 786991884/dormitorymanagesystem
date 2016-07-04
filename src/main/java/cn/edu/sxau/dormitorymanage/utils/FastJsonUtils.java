package cn.edu.sxau.dormitorymanage.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * alibaba fastjson utils
 */
public final class FastJsonUtils {
    public static <T> void writeJsonToFile(T t, File file) throws IOException {
        String jsonStr = JSONObject.toJSONString(t, SerializerFeature.PrettyFormat);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        bw.write(jsonStr);
        bw.close();
    }

    public static <T> void writeJsonToFile(T t, String filename) throws IOException {
        writeJsonToFile(t, new File(filename));
    }

    public static <T> T readJsonFromFile(Class<T> cls, File file) throws IOException {
        StringBuilder strBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line = null;
        while ((line = br.readLine()) != null) {
            strBuilder.append(line);
        }
        br.close();
        return JSONObject.parseObject(strBuilder.toString(), cls);
    }

    public static <T> T readJsonFromFile(Class<T> cls, String filename) throws IOException {
        return readJsonFromFile(cls, new File(filename));
    }

    public static <T> T readJsonFromFile(TypeReference<T> typeReference, File file) throws IOException {
        StringBuilder strBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line = null;
        while ((line = br.readLine()) != null) {
            strBuilder.append(line);
        }
        br.close();
        return JSONObject.parseObject(strBuilder.toString(), typeReference);
    }

    public static <T> T readJsonFromFile(TypeReference<T> typeReference, String filename) throws IOException {
        return readJsonFromFile(typeReference, new File(filename));
    }
}
