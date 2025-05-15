package com.healthclub.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> List<T> getAll(String path, TypeReference<List<T>> typeRef) {
        try {
            File file = new File(path);
            if (!file.exists()) return new ArrayList<>();
            return mapper.readValue(file, typeRef);
        } catch (Exception e) {
            LoggerUtil.getLogger().severe("JSON Load Error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static <T> T getOne(String path, TypeReference<List<T>> typeRef, Predicate<T> predicate) {
        return getAll(path, typeRef).stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    public static <T> void add(String path, T newObject, TypeReference<List<T>> typeRef) {
        List<T> list = getAll(path, typeRef);
        list.add(newObject);
        saveList(path, list);
    }

    public static <T> void edit(String path, Predicate<T> matcher, T updatedObject, TypeReference<List<T>> typeRef) {
        List<T> list = getAll(path, typeRef).stream()
                .map(item -> matcher.test(item) ? updatedObject : item)
                .collect(Collectors.toList());
        saveList(path, list);
    }

    public static <T> void delete(String path, Predicate<T> matcher, TypeReference<List<T>> typeRef) {
        List<T> list = getAll(path, typeRef).stream()
                .filter(item -> !matcher.test(item))
                .collect(Collectors.toList());
        saveList(path, list);
    }

    private static <T> void saveList(String path, List<T> list) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), list);
        } catch (Exception e) {
            LoggerUtil.getLogger().severe("JSON Save Error: " + e.getMessage());
        }
    }
}
