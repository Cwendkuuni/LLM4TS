package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.test.BaseTest;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class JavaUtilCollectionsDeserializersTest extends BaseTest {

    private static ObjectMapper mapper;

    static {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Map.class, JavaUtilCollectionsDeserializers.findForMap((DeserializationContext) null, null));
        module.addDeserializer(List.class, JavaUtilCollectionsDeserializers.findForCollection((DeserializationContext) null, null));
        module.addDeserializer(Set.class, JavaUtilCollectionsDeserializers.findForCollection((DeserializationContext) null, null));
        mapper = new ObjectMapper();
        mapper.registerModule(module);
    }

    @Test
    public void testFindForMapSingleton() throws IOException {
        String json = "{\"class\":\"java.util.Collections$SingletonMap\", \"key\":\"a\", \"value\":\"b\"}";
        Map<String, String> expectedMap = Collections.singletonMap("a", "b");
        Map<String, String> actualMap = mapper.readValue(json, Map.class);
        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testFindForMapUnmodifiable() throws IOException {
        String json = "{\"class\":\"java.util.Collections$UnmodifiableMap\", \"m\":{\"a\":\"b\"}}";
        Map<String, String> expectedMap = Collections.unmodifiableMap(Collections.singletonMap("a", "b"));
        Map<String, String> actualMap = mapper.readValue(json, Map.class);
        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testFindForListSingleton() throws IOException {
        String json = "{\"class\":\"java.util.Collections$SingletonList\", \"value\":[1]}";
        List<Integer> expectedList = Collections.singletonList(1);
        List<Integer> actualList = mapper.readValue(json, new TypeReference<List<Integer>>(){});
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindForListUnmodifiable() throws IOException {
        String json = "{\"class\":\"java.util.Collections$UnmodifiableList\", \"list\":[1]}";
        List<Integer> expectedList = Collections.unmodifiableList(Collections.singletonList(1));
        List<Integer> actualList = mapper.readValue(json, new TypeReference<List<Integer>>(){});
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindForSetSingleton() throws IOException {
        String json = "{\"class\":\"java.util.Collections$SingletonSet\", \"element\":true}";
        Set<Boolean> expectedSet = Collections.singleton(true);
        Set<Boolean> actualSet = mapper.readValue(json, new TypeReference<Set<Boolean>>(){});
        assertEquals(expectedSet, actualSet);
    }

    @Test
    public void testFindForSetUnmodifiable() throws IOException {
        String json = "{\"class\":\"java.util.Collections$UnmodifiableSet\", \"set\":[true]}";
        Set<Boolean> expectedSet = Collections.unmodifiableSet(Collections.singleton(true));
        Set<Boolean> actualSet = mapper.readValue(json, new TypeReference<Set<Boolean>>(){});
        assertEquals(expectedSet, actualSet);
    }

    @Test
    public void testNotFound() throws IOException {
        String json = "{\"class\":\"java.util.Collection\", \"list\":[1]}";
        List<Integer> expectedList = null;
        List<Integer> actualList = mapper.readValue(json, List.class);
        assertNull(actualList);
        assertEquals(expectedList, actualList);
    }
}