package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class PropertyBuilderTest {

    private ObjectMapper mapper;
    private SerializationConfig config;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() throws InstantiationException, IllegalAccessException, NoSuchMethodException {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("mapperVersion", 1);
        mapper = new ObjectMapper();
        mapper.setSerializationConfig(config = new SerializationConfig(mapper.getDefaultConfig()));
        config.setRootValueConstructor(null);
        JavaClass javaClass = JavaClass.instance.of(PropertyBuilder.class);
        BeanDescription beanDescription = new BeanDescription();
        beanDescription.setClassAnnotations(javaClass.getAnnotatedClass());
        propertyBuilder = new PropertyBuilder(config, beanDescription);
    }

    @Test
    public void testGetClassAnnotations() throws JsonProcessingException {
        Assert.assertNotNull(propertyBuilder.getClassAnnotations());
    }

    @Test
    public void testBuildWriter_NoSerializer() throws JsonProcessingException {
        ObjectNode rootNode = ObjectMapperTestUtil.createEmptyNode();
        JavaType type = ObjectTypeFactory.instance.constructType(rootNode.getClass());
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("nonExistentProperty", null, type, null, null, null, null, null);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(new TestSerializerProvider(), propDef, type, null, null, null, null, false);
        Assert.assertNull(writer);
    }

    @Test
    public void testBuildWriter_WithSerializer() throws JsonProcessingException {
        TestSerializer serializer = new TestSerializer();
        ObjectNode rootNode = ObjectMapperTestUtil.createEmptyNode();
        JavaType type = ObjectTypeFactory.instance.constructType(rootNode.getClass());
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", null, type, null, null, null, null, null);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(new TestSerializerProvider(), propDef, type, serializer, null, null, null, false);
        Assert.assertNotNull(writer);
    }

    @Test
    public void testFindSerializationType_Empty() throws JsonMappingException {
        JavaType type = JavaTypeFactory.instance.constructType(String[].class);
        AnnotatedMember am = null;
        JavaType actualType = propertyBuilder.findSerializationType(am, false, type);
        Assert.assertNull(actualType);
    }

    @Test
    public void testFindSerializationType_WithAnnotation() throws JsonMappingException {
        String className = JsonSerialize.class.getName();
        Class<?> clazz = Class.forName(className);
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        JsonSerialize jsonSerialize = (JsonSerialize) constructor.newInstance();
        JsonInclude jsonIncludeAnnotation = new JsonInclude();
        jsonIncludeAnnotation.setInclude(JsonInclude.Include.NON_DEFAULT);
        Method setIncludeMethod = JsonSerialize.class.getDeclaredMethod("setInclude", JsonInclude.class);
        setIncludeMethod.setAccessible(true);
        setIncludeMethod.invoke(jsonSerialize, jsonIncludeAnnotation);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        Mockito.when(am.getRawType()).thenReturn(clazz);
        JavaType declaredType = JavaTypeFactory.instance.constructType(clazz);
        JavaType actualType = propertyBuilder.findSerializationType(am, false, declaredType);
        Assert.assertEquals(JsonType.Type.OBJECT, actualType.getBaseType());
        Assert.assertEquals(JsonType.Type.INT, actualType.getContentType().getBaseType());
    }

    @Test
    public void testFindSerializationType_WithStaticTyping() throws JsonMappingException {
        String className = JsonSerialize.class.getName();
        Class<?> clazz = Class.forName(className);
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        JsonSerialize jsonSerialize = (JsonSerialize) constructor.newInstance();
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        Mockito.when(am.getRawType()).thenReturn(clazz);
        JavaType declaredType = JavaTypeFactory.instance.constructType(clazz);
        JavaType actualType = propertyBuilder.findSerializationType(am, true, declaredType);
        Assert.assertEquals(JsonType.Type.OBJECT, actualType.getBaseType());
        Assert.assertEquals(JsonType.Type.INT, actualType.getContentType().getBaseType());
        Assert.assertTrue(actualType.isStaticTyping());
    }

    @Test
    public void testGetDefaultBean() {
        try {
            propertyBuilder.getDefaultBean();
            Assert.fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            Assert.assertEquals("Default bean instance for class com.fasterxml.jackson.databind.ser.PropertyBuilder not found", e.getMessage());
        }
    }

    @Test
    public void testGetPropertyDefaultValue() throws JsonMappingException {
        ObjectNode rootNode = ObjectMapperTestUtil.createEmptyNode();
        BeanDescription beanDescription = new BeanDescription();
        JavaClass javaClass = JavaClass.instance.of(PropertyBuilder.class);
        beanDescription.setClassAnnotations(javaClass.getAnnotatedClass());
        propertyBuilder = new PropertyBuilder(config, beanDescription);
        Object defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNull(defaultBean);

        propertyBuilder = new PropertyBuilder(config, beanDescription);
        Class<?> clazz = String.class;
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("defaultProperty", null, JavaTypeFactory.instance.constructType(clazz));
        Assert.assertEquals("", defaultValue);

        propertyBuilder = new PropertyBuilder(config, beanDescription);
        clazz = Integer[].class;
        propertyBuilder.setUseRealPropertyDefaults(true);
        Object defaultArrayValue = propertyBuilder.getPropertyDefaultValue("defaultArrayProperty", null, JavaTypeFactory.instance.constructArrayType(clazz));
        Assert.assertArrayEquals(new Integer[0], defaultArrayValue);
    }

    static class TestSerializerProvider extends SerializerProvider {
        @Override
        public JsonSerializer<?> findValueSerializer(Class<?> type, PropertyName propertyName) {
            return new TestSerializer();
        }
    }

    static class TestSerializer extends StdSerializer<Object> {
        public TestSerializer() {
            this(null);
        }

        public TestSerializer(Class<Object> t) {
            super(t);
        }

        @Override
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString("TestSerializer");
        }
    }
}