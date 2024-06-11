package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Test;

/**
 * Test cases for PropertyBuilder class.
 */
public class PropertyBuilderTest {

    @Test
    public void testDefaultConstruction() throws Exception {
        // Test default construction
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, ConfigTest.SimplePojo.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        assertNotNull(builder);
    }

    @Test
    public void testGetterClassAnnotations() throws Exception {
        // Test getClassAnnotations method
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, ConfigTest.SimplePojo.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        Annotations annotations = builder.getClassAnnotations();
        assertNotNull(annotations);
        assertTrue(annotations.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS));
    }

    @Test
    public void testBuildWriterNormalCase() throws Exception {
        // Test buildWriter method with normal case
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, ConfigTest.SimplePojo.class);
        ObjectWriter writer = new ObjectMapper().writer().withSerializerProvider(new StandardSerializerProvider());
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("prop1", "prop1", null, null, null, null, null, null, null, null, null);
        BeanPropertyWriter writerInstance = builder.buildWriter(new TestSerializerProvider(), propDef, null, null, null, null, null, false);
        assertNotNull(writerInstance);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriterNullContentTypeSer() throws Exception {
        // Test buildWriter method with null contentTypeSer
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, ConfigTest.SimplePojo.class);
        ObjectWriter writer = new ObjectMapper().writer().withSerializerProvider(new StandardSerializerProvider());
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("prop1", "prop1", null, null, null, null, null, null, null, null, null);
        TypeSerializer typeSer = TypeSerializers.empty();
        BeanPropertyWriter writerInstance = builder.buildWriter(new TestSerializerProvider(), propDef, null, null, null, null, null, false);
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriterNullDeclaredType() throws Exception {
        // Test buildWriter method with null declaredType
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, ConfigTest.SimplePojo.class);
        ObjectWriter writer = new ObjectMapper().writer().withSerializerProvider(new StandardSerializerProvider());
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("prop1", "prop1", null, null, null, null, null, null, null, null, null);
        BeanPropertyWriter writerInstance = builder.buildWriter(new TestSerializerProvider(), propDef, null, null, null, null, null, false);
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriterNullSer() throws Exception {
        // Test buildWriter method with null ser
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, ConfigTest.SimplePojo.class);
        ObjectWriter writer = new ObjectMapper().writer().withSerializerProvider(new StandardSerializerProvider());
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("prop1", "prop1", null, null, null, null, null, null, null, null, null);
        TypeSerializer typeSer = TypeSerializers.empty();
        JavaType declaredType = TypeFactory.defaultInstance();
        BeanPropertyWriter writerInstance = builder.buildWriter(new TestSerializerProvider(), propDef, declaredType, null, typeSer, null, null, false);
        fail("Expected JsonMappingException");
    }

    @Test
    public void testFindSerializationTypeNormalCase() throws Exception {
        // Test findSerializationType method with normal case
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, ConfigTest.SimplePojo.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance();
        JavaType result = builder.findSerializationType(a, false, declaredType);
        assertEquals(declaredType, result);
    }

    @Test
    public void testFindSerializationTypeWithJsonSerializeAnnotation() throws Exception {
        // Test findSerializationType method with JsonSerialize annotation
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, ConfigTest.SimplePojoWithJsonSerialize.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance();
        JavaType result = builder.findSerializationType(a, false, declaredType);
        JavaType expected = TypeFactory.constructType(String.class);
        assertEquals(expected, result);
    }

    @Test
    public void testFindSerializationTypeWithJsonSerializeAnnotationAndDeclaredType() throws Exception {
        // Test findSerializationType method with JsonSerialize annotation and declaredType
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, ConfigTest.SimplePojoWithJsonSerialize.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = TypeFactory.constructType(Integer.class);
        JavaType result = builder.findSerializationType(a, false, declaredType);
        JavaType expected = TypeFactory.constructType(Integer.class);
        assertEquals(expected, result);
    }

    @Test
    public void testFindSerializationTypeWithJsonSerializeAnnotationSuperType() throws Exception {
        // Test findSerializationType method with JsonSerialize annotation and super type
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, ConfigTest.SimplePojoWithJsonSerializeSuperType.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = TypeFactory.constructType(String[].class);
        JavaType result = builder.findSerializationType(a, false, declaredType);
        JavaType expected = TypeFactory.constructType(String[].class);
        assertEquals(expected, result);
    }

    @Test
    public void testFindSerializationTypeWithJsonSerializeAnnotationSubType() throws Exception {
        // Test findSerializationType method with JsonSerialize annotation and sub type
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, ConfigTest.SimplePojoWithJsonSerializeSubType.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = TypeFactory.constructType(String.class);
        JavaType result = builder.findSerializationType(a, false, declaredType);
        JavaType expected = TypeFactory.constructType(ConfigTest.SimplePojoWithJsonSerializeSubType.class);
        assertEquals(expected, result);
    }

    @Test
    public void testGetPropertyDefaultValueNullName() throws Exception {
        // Test getPropertyDefaultValue method with null name
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, ConfigTest.SimplePojo.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        String name = null;
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.defaultInstance();
        Object result = builder.getPropertyDefaultValue(name, member, type);
        assertNull(result);
    }

    @Test
    public void testGetPropertyDefaultValueNoDefaultBean() throws Exception {
        // Test getPropertyDefaultValue method with no defaultBean
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, ConfigTest.SimplePojo.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        String name = "prop1";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.defaultInstance();
        Object result = builder.getPropertyDefaultValue(name, member, type);
        assertNull(result);
    }

    @Test
    public void testGetPropertyDefaultValueStringProperty() throws Exception {
        // Test getPropertyDefaultValue method with String property
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, ConfigTest.SimplePojo.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        String name = "prop1";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.defaultInstance();
        Object result = builder.getPropertyDefaultValue(name, member, type);
        assertEquals("", result);
    }

    @Test
    public void testGetPropertyDefaultValueIntegerProperty() throws Exception {
        // Test getPropertyDefaultValue method with Integer property
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, ConfigTest.SimplePojo.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        String name = "prop1";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.constructType(Integer.class);
        Object result = builder.getPropertyDefaultValue(name, member, type);
        assertEquals(0, result);
    }

    @Test
    public void testGetPropertyDefaultValueListProperty() throws Exception {
        // Test getPropertyDefaultValue method with List property
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, ConfigTest.SimplePojo.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        String name = "prop1";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.constructType(ArrayList.class, Integer.class);
        Object result = builder.getPropertyDefaultValue(name, member, type);
        assertTrue(result instanceof Collection);
        assertEquals(0, ((Collection)result).size());
    }

    @Test
    public void testGetPropertyDefaultValueMapProperty() throws Exception {
        // Test getPropertyDefaultValue method with Map property
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, ConfigTest.SimplePojo.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        String name = "prop1";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.constructType(HashMap.class, String.class, Integer.class);
        Object result = builder.getPropertyDefaultValue(name, member, type);
        assertTrue(result instanceof Map);
        assertEquals(0, ((Map)result).size());
    }

    @Test
    public void testGetPropertyDefaultValueBeanProperty() throws Exception {
        // Test getPropertyDefaultValue method with Bean property
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, ConfigTest.NestedPojo.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        String name = "prop1";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.constructType(ConfigTest.NestedPojo.class);
        Object result = builder.getPropertyDefaultValue(name, member, type);
        assertNull(result);
    }

    /**
     * Helper class for testing {@link PropertyBuilder#buildWriter} method.
     */
    private static class TestSerializerProvider extends StandardSerializerProvider {
        @Override
        public JsonSerializer<?> findValueSerializer(SerializationConfig config, BeanPropertyDefinition prop, JavaType type) {
            return null;
        }

        @Override
        public JsonSerializer<?> findContentSerializer(SerializationConfig config, BeanPropertyDefinition prop, JavaType type) {
            return null;
        }
    }
}