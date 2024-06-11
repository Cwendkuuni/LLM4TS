package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import com.fasterxml.jackson.databind.ser.impl.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.type.ClassType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.BeanProperty;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyBuilderTest {

    private Object testBean;
    private BeanSerializerFactory serializerFactory;
    private AnnotationIntrospector annotationIntrospector;

    @BeforeMethod
    public void setUp() throws InstantiationException, IllegalAccessException {
        // Create test bean
        testBean = new TestBean();

        // Create serializer factory
        SerializationConfig config = new SerializationConfig();
        config.setDefaultSerializerProviderClass(DefaultSerializerProvider.class);
        config.setAnnotationIntrospector(new StandardAnnotationIntrospector());
        serializerFactory = new BeanSerializerFactory(config, null);

        // Create annotation introspector
        annotationIntrospector = new StandardAnnotationIntrospector();
    }

    @Test
    public void testBuildWriter_SimpleProperty() throws JsonMappingException, JsonProcessingException {
        // Given
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializerFactory.getSerializerConfig(), beanDesc);
        BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("simpleProperty");
        JavaType declaredType = new ClassType(String.class);
        JsonSerializer<String> ser = new StringSerializer();
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = new AnnotatedMember(new TestBean(), "simpleProperty", String.class);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(serializerFactory.getProvider(), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        // Then
        Assert.assertNotNull(writer);
        Assert.assertEquals(writer.getSerializerProvider(), serializerFactory.getProvider());
        Assert.assertEquals(writer.getAnnotationIntrospector(), annotationIntrospector);
        Assert.assertEquals(writer.getPropertyName(), "simpleProperty");
        Assert.assertEquals(writer.getMember(), am);
        Assert.assertEquals(writer.getSerializationType(), declaredType);
        Assert.assertNull(writer.getValueSerializer());
    }

    @Test
    public void testBuildWriter_ContainerProperty() throws JsonMappingException, JsonProcessingException {
        // Given
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializerFactory.getSerializerConfig(), beanDesc);
        BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("containerProperty");
        JavaType declaredType = new ClassType(List.class);
        JavaType contentType = new ClassType(Integer.class);
        JsonSerializer<List<Integer>> ser = new CollectionSerializer();
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = new ScalarTypeSerializer(new ClassType(Integer.class), _serializerProvider);
        AnnotatedMember am = new AnnotatedMember(new TestBean(), "containerProperty", declaredType);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(serializerFactory.getProvider(), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        // Then
        Assert.assertNotNull(writer);
        Assert.assertEquals(writer.getSerializerProvider(), serializerFactory.getProvider());
        Assert.assertEquals(writer.getAnnotationIntrospector(), annotationIntrospector);
        Assert.assertEquals(writer.getPropertyName(), "containerProperty");
        Assert.assertEquals(writer.getMember(), am);
        Assert.assertEquals(writer.getSerializationType(), declaredType);
        Assert.assertEquals(writer.getValueSerializer(), contentTypeSer);
    }

    @Test
    public void testFindSerializationType_Annotated() throws JsonMappingException {
        // Given
        Object testObject = new TestAnnotated();
        JavaType declaredType = new ClassType(TestAnnotated.class);

        // When
        JavaType serializationType = new PropertyBuilder(serializerFactory.getSerializerConfig(), null).findSerializationType(new Annotated(testObject, "testProperty"), false, declaredType);

        // Then
        Assert.assertEquals(serializationType, new ClassType(Integer.class));
    }

    @Test
    public void testFindSerializationType_Default() throws JsonMappingException {
        // Given
        Object testObject = new TestDefault();
        JavaType declaredType = new ClassType(TestDefault.class);

        // When
        JavaType serializationType = new PropertyBuilder(serializerFactory.getSerializerConfig(), null).findSerializationType(new Annotated(testObject, "testProperty"), false, declaredType);

        // Then
        Assert.assertEquals(serializationType, declaredType);
    }

    @Test
    public void testGetPropertyDefaultValue() throws JsonMappingException, InstantiationException, IllegalAccessException {
        // Given
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializerFactory.getSerializerConfig(), null);

        // When
        Object defaultBean = propertyBuilder.getDefaultBean();
        Object simplePropertyDefaultValue = propertyBuilder.getPropertyDefaultValue("simpleProperty", new AnnotatedMember(testBean, "simpleProperty", String.class), new ClassType(String.class));
        Object containerPropertyDefaultValue = propertyBuilder.getPropertyDefaultValue("containerProperty", new AnnotatedMember(testBean, "containerProperty", new ClassType(List.class)), new ClassType(List.class));

        // Then
        Assert.assertNull(defaultBean);
        Assert.assertNull(simplePropertyDefaultValue);
        Assert.assertEquals(containerPropertyDefaultValue, Collections.emptyList());
    }

    @Test
    public void testGetPropertyDefaultValue_Annotated() throws JsonMappingException, InstantiationException, IllegalAccessException {
        // Given
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializerFactory.getSerializerConfig(), null);
        Object testBeanWithDefault = new TestBeanWithDefault();

        // When
        Object simplePropertyDefaultValue = propertyBuilder.getPropertyDefaultValue("simpleProperty", new AnnotatedMember(testBeanWithDefault, "simpleProperty", String.class), new ClassType(String.class));
        Object containerPropertyDefaultValue = propertyBuilder.getPropertyDefaultValue("containerProperty", new AnnotatedMember(testBeanWithDefault, "containerProperty", new ClassType(List.class)), new ClassType(List.class));

        // Then
        Assert.assertEquals(simplePropertyDefaultValue, "");
        Assert.assertEquals(containerPropertyDefaultValue, Collections.emptyList());
    }

    @Test
    public void testGetDefaultValue() throws JsonMappingException {
        // Given
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializerFactory.getSerializerConfig(), null);

        // When
        Object simplePropertyDefaultValue = propertyBuilder.getDefaultValue(new ClassType(String.class));
        Object containerPropertyDefaultValue = propertyBuilder.getDefaultValue(new ClassType(List.class));
        Object referenceTypeDefaultValue = propertyBuilder.getDefaultValue(new ClassType(TestReference.class));

        // Then
        Assert.assertNull(simplePropertyDefaultValue);
        Assert.assertEquals(containerPropertyDefaultValue, JsonInclude.Include.NON_EMPTY);
        Assert.assertNull(referenceTypeDefaultValue);
    }

    private static class TestBean {
        private String simpleProperty;
        private List<Integer> containerProperty;

        // Getters and setters
        public String getSimpleProperty() {
            return simpleProperty;
        }

        public void setSimpleProperty(String simpleProperty) {
            this.simpleProperty = simpleProperty;
        }

        public List<Integer> getContainerProperty() {
            return containerProperty;
        }

        public void setContainerProperty(List<Integer> containerProperty) {
            this.containerProperty = containerProperty;
        }
    }

    private static class TestBeanWithDefault {
        private String simpleProperty;
        private List<Integer> containerProperty;

        // Getters and setters
        public String getSimpleProperty() {
            return simpleProperty;
        }

        public void setSimpleProperty(String simpleProperty) {
            this.simpleProperty = simpleProperty;
        }

        public List<Integer> getContainerProperty() {
            return containerProperty;
        }

        public void setContainerProperty(List<Integer> containerProperty) {
            this.containerProperty = containerProperty;
        }

        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        public String nonDefaultProperty;
    }

    private static class TestAnnotated {
        @JsonSerialize(using = StringSerializer.class)
        private int testProperty;

        public int getTestProperty() {
            return testProperty;
        }

        public void setTestProperty(int testProperty) {
            this.testProperty = testProperty;
        }
    }

    private static class TestDefault {
        private String testProperty;

        public String getTestProperty() {
            return testProperty;
        }

        public void setTestProperty(String testProperty) {
            this.testProperty = testProperty;
        }
    }

    private static class TestReference {
        // Empty class for test purposes
    }

    private static class TestSerializer extends StdSerializer<Object> {
        public TestSerializer() {
            this(null);
        }

        public TestSerializer(Class<Object> t) {
            this(null, t);
        }

        public TestSerializer(Class<Object> t, Constructor<Object> c) {
            super(t, c);
        }

        public TestSerializer(Class<Object> t, Constructor<Object> c, boolean quickWrite) {
            super(t, c, quickWrite);
        }

        public TestSerializer(Class<Object> t, Constructor<Object> c, boolean quickWrite, boolean wrapSingleOccurrences) {
            super(t, c, quickWrite, wrapSingleOccurrences);
        }

        public TestSerializer(Class<Object> t, Constructor<Object> c, boolean quickWrite, boolean wrapSingleOccurrences, JsonSerializer.Typing typing) {
            super(t, c, quickWrite, wrapSingleOccurrences, typing);
        }

        @Override
        public void serializeValues(Object p, JsonGenerator g) throws IOException {
            g.writeValue(p);
        }
    }

    private static class CollectionSerializer extends JsonSerializer<List<?>> {
        @Override
        public void serializeValues(List<?> p, JsonGenerator g) throws IOException {
            g.writeStartArray();
            for (Object o : p) {
                g.writeObject(o);
            }
            g.writeEndArray();
        }
    }

    private static class StringSerializer extends JsonSerializer<String> {
        @Override
        public void serializeValues(String p, JsonGenerator g) throws IOException {
            g.writeString(p);
        }
    }

    private static class ScalarTypeSerializer extends TypeSerializer {
        private final Class<?> _valueClass;
        private final JsonSerializer<?> _valueSerializer;

        public ScalarTypeSerializer(Class<?> cls, JsonSerializer<?> serializer) {
            this.valueClass = cls;
            this.valueSerializer = serializer;
        }

        @Override
        public void serializeWithType(JavaType type, Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            valueSerializer.serializeValues(value, gen, serializers);
            gen.writeFieldName("class");
            gen.writeValue(type.getRawClass().getName());
        }

        @Override
        public void deserializeWithType(JavaType type, JsonParser p, DeserializationContext ctxt) throws IOException {
            p.nextToken();
            String className = p.getText();
            try {
                Class<?> clazz = Class.forName(className);
                Object value = ctxt.findValueSerializer(clazz).deserialize(p, ctxt);
                ctxt.handlePrimaryDeserialization(value, null, null);
            } catch (Exception e) {
                throw new IOException("Failed to deserialize with custom type serializer", e);
            }
        }

        @Override
        public void reset(MapperFactory mf, JavaType type) throws SerializationException {
            super.reset(mf, type);
            valueSerializer = null;
        }
    }

    private static class Annotated {
        private Object value;
        private String name;
        private Class<?> type;

        public Annotated(Object value, String name, Class<?> type) {
            this.value = value;
            this.name = name;
            this.type = type;
        }

        public Object getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public Class<?> getType() {
            return type;
        }

        @Override
        public String toString() {
            return "Annotated{" +
                    "value=" + value +
                    ", name='" + name + '\'' +
                    ", type=" + type +
                    '}';
        }
    }

    private static class AnnotatedMember {
        private Object bean;
        private String name;
        private Class<?> type;

        public AnnotatedMember(Object bean, String name, Class<?> type) {
            this.bean = bean;
            this.name = name;
            this.type = type;
        }

        public Object getValue() throws IllegalAccessException, InstantiationException {
            Method method = BeanProperty.findGetter(bean.getClass(), name);
            return method.invoke(bean);
        }

        public void setValue(Object value) throws IllegalAccessException, InstantiationException {
            Method method = BeanProperty.findSetter(bean.getClass(), name);
            method.invoke(bean, value);
        }

        @Override
        public String toString() {
            return "AnnotatedMember{" +
                    "bean=" + bean +
                    ", name='" + name + '\'' +
                    ", type=" + type +
                    '}';
        }
    }
}