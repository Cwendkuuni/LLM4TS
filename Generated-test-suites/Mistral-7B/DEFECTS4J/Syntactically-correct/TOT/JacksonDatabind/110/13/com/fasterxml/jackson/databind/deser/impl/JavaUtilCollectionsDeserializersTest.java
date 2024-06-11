package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaUtilCollectionsDeserializersTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFindForCollection_SingletonSet() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        Object result = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        Assert.assertEquals(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET, result.getClass());
    }

    @Test
    public void testFindForCollection_SingletonList() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        Object result = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        Assert.assertEquals(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST, result.getClass());
    }

    @Test
    public void testFindForCollection_SingletonMap() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        Object result = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        Assert.assertEquals(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP, result.getClass());
    }

    @Test
    public void testFindForCollection_UnmodifiableSet() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        JavaType unmodifiableType = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        JavaType.Builder unmodifiableBuilder = unmodifiableType.builder();
        unmodifiableBuilder.asInstanceOf(java.util.Collection.class);
        JavaType unmodifiableJavaType = unmodifiableBuilder.build();
        Object result = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), unmodifiableJavaType);
        Assert.assertEquals(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET, result.getClass());
    }

    @Test
    public void testFindForCollection_UnmodifiableList() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JavaType unmodifiableType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JavaType.Builder unmodifiableBuilder = unmodifiableType.builder();
        unmodifiableBuilder.asInstanceOf(java.util.Collection.class);
        JavaType unmodifiableJavaType = unmodifiableBuilder.build();
        Object result = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), unmodifiableJavaType);
        Assert.assertEquals(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST, result.getClass());
    }

    @Test
    public void testFindForCollection_UnmodifiableMap() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        JavaType unmodifiableType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        JavaType.Builder unmodifiableBuilder = unmodifiableType.builder();
        unmodifiableBuilder.asInstanceOf(java.util.Map.class);
        JavaType unmodifiableJavaType = unmodifiableBuilder.build();
        Object result = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), unmodifiableJavaType);
        Assert.assertEquals(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP, result.getClass());
    }

    @Test
    public void testFindForMap_SingletonMap() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        Object result = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);
        Assert.assertEquals(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP, result.getClass());
    }

    @Test
    public void testFindForMap_UnmodifiableMap() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        JavaType unmodifiableType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        JavaType.Builder unmodifiableBuilder = unmodifiableType.builder();
        unmodifiableBuilder.asInstanceOf(java.util.Map.class);
        JavaType unmodifiableJavaType = unmodifiableBuilder.build();
        Object result = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), unmodifiableJavaType);
        Assert.assertEquals(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP, result.getClass());
    }

    @Test
    public void testConvert_SingletonSet() throws Exception {
        Set<Boolean> set = Arrays.asList(Boolean.TRUE, Boolean.FALSE).stream().map(b -> (Set<Boolean>) JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(new TypeReference<JavaType>(){}), mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE))).findFirst().get();
        Object result = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE), Set.class).convert(set);
        Assert.assertEquals(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET, result.getClass());
    }

    @Test
    public void testConvert_SingletonList() throws Exception {
        List<Boolean> list = Arrays.asList(Boolean.TRUE, Boolean.FALSE).stream().map(b -> (List<Boolean>) JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(new TypeReference<JavaType>(){}), mapper.getTypeFactory().constructType(List.class, Boolean.TYPE))).findFirst().get();
        Object result = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, mapper.getTypeFactory().constructType(List.class, Boolean.TYPE), List.class).convert(list);
        Assert.assertEquals(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST, result.getClass());
    }

    @Test
    public void testConvert_SingletonMap() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("a", "b");
        Object result = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, mapper.getTypeFactory().constructType(Map.class, String.class, String.class), Map.class).convert(map);
        Assert.assertEquals(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP, result.getClass());
    }

    @Test
    public void testConvert_UnmodifiableSet() throws Exception {
        Set<Boolean> set = Arrays.asList(Boolean.TRUE, Boolean.FALSE).stream().map(b -> (Set<Boolean>) JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(new TypeReference<JavaType>(){}), mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE))).findFirst().get();
        Object result = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE), Set.class).convert(Collections.unmodifiableSet(set));
        Assert.assertEquals(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET, result.getClass());
    }

    @Test
    public void testConvert_UnmodifiableList() throws Exception {
        List<Boolean> list = Arrays.asList(Boolean.TRUE, Boolean.FALSE).stream().map(b -> (List<Boolean>) JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(new TypeReference<JavaType>(){}), mapper.getTypeFactory().constructType(List.class, Boolean.TYPE))).findFirst().get();
        Object result = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, mapper.getTypeFactory().constructType(List.class, Boolean.TYPE), List.class).convert(Collections.unmodifiableList(list));
        Assert.assertEquals(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST, result.getClass());
    }

    @Test
    public void testConvert_UnmodifiableMap() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("a", "b");
        Object result = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, mapper.getTypeFactory().constructType(Map.class, String.class, String.class), Map.class).convert(Collections.unmodifiableMap(map));
        Assert.assertEquals(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP, result.getClass());
    }

    @Test
    public void testFindForCollection_AsList() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        Object result = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        Assert.assertEquals(JavaUtilCollectionsDeserializers.CLASS_AS_ARRAYS_LIST, result.getClass());
    }

    @Test
    public void testConvert_AsList() throws Exception {
        Object listObject = new Object[] {Boolean.TRUE, Boolean.FALSE};
        JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        Object result = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, javaType, List.class).convert(listObject);
        Assert.assertEquals(JavaUtilCollectionsDeserializers.CLASS_AS_ARRAYS_LIST, result.getClass());
    }
}