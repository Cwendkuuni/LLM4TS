package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PropertyBuilderTest {

    private ObjectNode defaultBean;
    private PropertyBuilder propertyBuilder;
    private BeanDescription beanDescription;
    private SerializationConfig config;

    @Before
    public void setUp() throws Exception {
        ObjectNodeFactory objectNodeFactory = new ObjectMapper().getNodeFactory();
        defaultBean = objectNodeFactory.objectNode();
        defaultBean.putPOJO("name", "John Doe");
        defaultBean.putPOJO("age", 30);

        BeanDescription.StandardBeanDescription bd = new BeanDescription(Object.class);
        beanDescription = bd;
        config = new SerializationConfig(new ObjectMapper.DefaultSerializerProvider());
        config.setBeanDescriptionFactory(new BeanDescriptionFactory());
        config.setAnnotationIntrospector(new StandardAnnotationIntrospector());
        propertyBuilder = new PropertyBuilder(config, beanDescription);
    }

    @Test
    public void testBuilder() {
        assertEquals(config, propertyBuilder._config);
        assertEquals(beanDescription, propertyBuilder._beanDesc);
        assertEquals(config.getDefaultAnnotationIntrospector(), propertyBuilder._annotationIntrospector);
        assertNull(propertyBuilder._defaultBean);
        assertNull(propertyBuilder._defaultInclusion);
        assertFalse(propertyBuilder._useRealPropertyDefaults);
    }

    @Test
    public void testGetClassAnnotations() throws Exception {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
        assertEquals(Collections.emptyList(), annotations.getAnnotations());
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonSerialize(using = ToStringSerializer.class)
    static class TestClass {
        private int id;
        private String name;

        public TestClass() {
            // empty constructor
        }

        public TestClass(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @JsonProperty("id")
        public int getId() {
            return id;
        }

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        @JsonProperty("unsupported")
        private void setUnsupported(int i) {
            // private setter for unsupported property
        }
    }

    @Test
    public void testBuildWriter() throws Exception {
        BeanPropertyDefinition beanPropertyDefinition = getTestClassPropertyDefinition("id");
        JavaType javaType = TypeFactory.defaultInstance().constructType(TestClass.class);
        JsonSerializer<Object> jsonSerializer = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object o, JsonGenerator jg, SerializerProvider provider) throws IOException {
                jg.writeNumber(o.hashCode());
            }
        };
        TypeSerializer typeSerializer = TypeSerializers.instance.findContentSerializer(javaType, config);

        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(new DefaultSerializerProvider(), beanPropertyDefinition, javaType, jsonSerializer, typeSerializer, null, null, null, false);

        assertNotNull(beanPropertyWriter);
        assertNotNull(beanPropertyWriter.getSerializer());
        assertNotNull(beanPropertyWriter.getTypeSerializer());
        assertFalse(beanPropertyWriter.suppressNulls());
        assertNull(beanPropertyWriter.getValueToSuppress());

        Method writeMethod = beanPropertyWriter.getClass().getDeclaredMethod("write");
        writeMethod.setAccessible(true);
        writeMethod.invoke(beanPropertyWriter, null, null, null);

        ObjectNode testObjectNode = objectNodeFactory.objectNode();
        testObjectNode.put("id", 123);

        ObjectWriter objectWriter = config.getObjectWriter();
        String result = objectWriter.writeValueAsString(testObjectNode);
        JsonParser jsonParser = config.getFactory().createParser(new ByteArrayInputStream(result.getBytes()));

        TestClass testClass = objectMapper.readValue(jsonParser, TestClass.class);
        assertEquals(123, testClass.getId());
    }

    @Test
    public void testFindSerializationType() throws Exception {
        TestClass testClass = new TestClass(1, "Jane Doe");
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        when(annotatedMember.getName()).thenReturn("name");
        when(annotatedMember.getType()).thenReturn(String.class);
        when(annotatedMember.getAnnotations()).thenReturn(new JsonInclude(JsonInclude.Include.NON_DEFAULT));

        JavaType javaType = TypeFactory.defaultInstance().constructType(TestClass.class);
        JavaType foundJavaType = propertyBuilder.findSerializationType(annotatedMember, false, javaType);

        assertEquals(String.class, foundJavaType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationTypeWithIncompatibleType() throws Exception {
        TestClass testClass = new TestClass(1, "Jane Doe");
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        when(annotatedMember.getName()).thenReturn("name");
        when(annotatedMember.getType()).thenReturn(Integer.class);
        when(annotatedMember.getAnnotations()).thenReturn(new JsonInclude(JsonInclude.Include.NON_DEFAULT));

        JavaType javaType = TypeFactory.defaultInstance().constructType(TestClass.class);
        propertyBuilder.findSerializationType(annotatedMember, false, javaType);
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        TestClass testClass = new TestClass(1, "John Doe");
        AnnotatedElement annotatedElement = Mockito.mock(AnnotatedElement.class);
        when(annotatedElement.getName()).thenReturn("name");
        when(annotatedElement.getType()).thenReturn(String.class);

        String result = propertyBuilder.getPropertyDefaultValue("name", annotatedElement, TypeFactory.defaultInstance().constructType(TestClass.class));

        assertEquals("", result);
    }

    @Test
    public void testGetDefaultValue() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(TestClass.class);

        Object result = propertyBuilder.getDefaultValue(javaType);

        assertNull(result);
    }

    @Test
    public void testGetDefaultBean() throws Exception {
        assertEquals(defaultBean, propertyBuilder.getDefaultBean());
    }

    private BeanPropertyDefinition getTestClassPropertyDefinition(String propertyName) throws Exception {
        Method getterMethod = TestClass.class.getDeclaredMethod("get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1));
        BeanPropertyDefinition beanPropertyDefinition = new BeanPropertyDefinition(
                getterMethod,
                TestClass.class.getDeclaredField("name"),
                new BeanPropertyModifier[0]);
        beanPropertyDefinition.setGetter(getterMethod);
        return beanPropertyDefinition;
    }
}