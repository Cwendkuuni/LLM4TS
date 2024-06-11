package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory.PropertyBuilder;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.util.*;

import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private ObjectNode node;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() throws JsonProcessingException {
        config = new SerializationConfig(new AnnotationIntrospector());
        ObjectMapper mapper = new ObjectMapper(config);
        node = mapper.createObjectNode();

        BeanDescription beanDescription = new BeanDescription(Map.class);
        propertyBuilder = new PropertyBuilder(config, beanDescription);
    }

    @Test
    public void testBuilderWithDefaultProperties() throws JsonProcessingException {
        // Given
        Object defaultBean = new HashMap<>();
        propertyBuilder._defaultBean = defaultBean;

        // When
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);

        // Then
        assertNotNull(writer);
    }

    @Test
    public void testBuilderWithCustomSerializationType() throws JsonProcessingException {
        // Given
        Class<Integer> integerType = Integer.class;
        AnnotatedMember member = Mockito.mock(AnnotatedElement.class);
        when(member.getType()).thenReturn(integerType);

        JavaType declaredType = ObjectTypeFactory.defaultInstance().constructType(int[].class);
        propertyBuilder._annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);
        when(propertyBuilder._annotationIntrospector.findSerializationType(Mockito.any(), Mockito.eq(false), Mockito.eq(declaredType))).thenReturn(TypeFactory.defaultInstance().constructType(List.class));

        // When
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, declaredType, null, null, null, member, false);

        // Then
        assertEquals(List.class, writer.getType().getContentType());
    }

    @Test
    public void testBuilderWithIncludeAnnotation() throws JsonProcessingException {
        // Given
        BeanDescription beanDescription = new BeanDescription(MyClass.class);
        propertyBuilder = new PropertyBuilder(config, beanDescription);
        JsonInclude.Include include = JsonInclude.Include.NON_DEFAULT;
        JsonInclude jsonInclude = new JsonInclude();
        jsonInclude.setInclude(include);
        propertyBuilder._annotationIntrospector = new AnnotationIntrospector();
        when(propertyBuilder._annotationIntrospector.findIncludeAnnotation(Mockito.any())).thenReturn(jsonInclude);

        // When
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);

        // Then
        assertNotNull(writer);
        assertTrue(writer.isSuppressNulls());
    }

    @Test
    public void testBuilderWithDefaultValue() throws JsonProcessingException {
        // Given
        BeanDescription beanDescription = new BeanDescription(MyClass.class);
        propertyBuilder = new PropertyBuilder(config, beanDescription);
        propertyBuilder._defaultBean = new MyClass();

        // When
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("test", null, ObjectTypeFactory.defaultInstance().constructType(MyClass.class));

        // Then
        assertNotNull(propertyDefaultValue);
        assertEquals(new MyClass(), propertyDefaultValue);
    }

    @Test
    public void testBuilderWithDefaultValueOfArray() throws JsonProcessingException {
        // Given
        BeanDescription beanDescription = new BeanDescription(MyArrayClass.class);
        propertyBuilder = new PropertyBuilder(config, beanDescription);
        propertyBuilder._defaultBean = new MyArrayClass[0];

        // When
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("test", null, ObjectTypeFactory.defaultInstance().constructType(MyArrayClass[].class));

        // Then
        assertNotNull(propertyDefaultValue);
        assertArrayEquals(new MyArrayClass[0], (MyArrayClass[]) propertyDefaultValue);
    }

    @Test
    public void testBuilderWithDefaultValueOfString() throws JsonProcessingException {
        // Given
        BeanDescription beanDescription = new BeanDescription(MyStringClass.class);
        propertyBuilder = new PropertyBuilder(config, beanDescription);
        propertyBuilder._defaultBean = "default";

        // When
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("test", null, ObjectTypeFactory.defaultInstance().constructType(String.class));

        // Then
        assertNotNull(propertyDefaultValue);
        assertEquals("default", propertyDefaultValue);
    }

    @Test
    public void testBuilderWithNullValue() throws JsonProcessingException {
        // Given
        BeanDescription beanDescription = new BeanDescription(MyClass.class);
        propertyBuilder = new PropertyBuilder(config, beanDescription);

        // When
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("test", null, ObjectTypeFactory.defaultInstance().constructType(String.class));

        // Then
        assertNull(propertyDefaultValue);
    }

    @Test
    public void testBuilderWithEmptyCollection() throws JsonProcessingException {
        // Given
        BeanDescription beanDescription = new BeanDescription(MyListClass.class);
        propertyBuilder = new PropertyBuilder(config, beanDescription);

        // When
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("test", null, ObjectTypeFactory.defaultInstance().constructType(List.class));

        // Then
        assertNotNull(propertyDefaultValue);
        assertTrue(propertyDefaultValue instanceof BeanPropertyWriter.Marker);
        assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, propertyDefaultValue);
    }

    @Test
    public void testBuilderWithEmptyMap() throws JsonProcessingException {
        // Given
        BeanDescription beanDescription = new BeanDescription(MyHashMapClass.class);
        propertyBuilder = new PropertyBuilder(config, beanDescription);

        // When
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("test", null, ObjectTypeFactory.defaultInstance().constructType(Map.class));

        // Then
        assertNotNull(propertyDefaultValue);
        assertTrue(propertyDefaultValue instanceof BeanPropertyWriter.Marker);
        assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, propertyDefaultValue);
    }

    @Test
    public void testBuilderWithValueSerialization() throws JsonProcessingException {
        // Given
        Object valueToSerialize = new MyClass();
        BeanDescription beanDescription = new BeanDescription(MyClass.class);
        propertyBuilder = new PropertyBuilder(config, beanDescription);

        // When
        ObjectWriter objectWriter = config.getObjectWriter();
        ObjectWriter writer = objectWriter.withView(new MyView());
        ObjectWriter valueWriter = writer.writeValueAsString(valueToSerialize);
        String value = valueWriter.getValueAsString();

        // Then
        assertEquals("{\"name\":\"test\"}", value);
    }

    @Test
    public void testBuilderWithValueSerializationWithJsonInclude() throws JsonProcessingException {
        // Given
        Object valueToSerialize = new MyClass();
        BeanDescription beanDescription = new BeanDescription(MyClass.class);
        propertyBuilder = new PropertyBuilder(config, beanDescription);
        JsonInclude jsonInclude = new JsonInclude();
        jsonInclude.setInclude(JsonInclude.Include.NON_EMPTY);
        propertyBuilder._annotationIntrospector = new AnnotationIntrospector();
        when(propertyBuilder._annotationIntrospector.findIncludeAnnotation(Mockito.any())).thenReturn(jsonInclude);

        // When
        ObjectWriter objectWriter = config.getObjectWriter();
        ObjectWriter writer = objectWriter.withView(new MyView());
        ObjectWriter valueWriter = writer.writeValueAsString(valueToSerialize);
        String value = valueWriter.getValueAsString();

        // Then
        assertEquals("{\"name\":\"test\"}", value);
    }

    @Test
    public void testBuilderWithNullValueSerialization() throws JsonProcessingException {
        // Given
        Object valueToSerialize = null;
        BeanDescription beanDescription = new BeanDescription(MyClass.class);
        propertyBuilder = new PropertyBuilder(config, beanDescription);

        // When
        ObjectWriter objectWriter = config.getObjectWriter();
        ObjectWriter writer = objectWriter.withView(new MyView());
        ObjectWriter valueWriter = writer.writeValueAsString(valueToSerialize);
        String value = valueWriter.getValueAsString();

        // Then
        assertNull(value);
    }

    private static void assertArrayEquals(Object expected, Object actual) {
        if (!expected.getClass().isArray()) {
            super.assertEquals(expected, actual);
            return;
        }

        if (expected.getClass().getComponentType().isPrimitive()) {
            Object[] ex = (Object[]) expected;
            Object[] ac = (Object[]) actual;
            for (int i = 0; i < ex.length; i++) {
                assertEquals(ex[i], ac[i]);
            }
        } else {
            assertArrayEquals((Object[]) expected, (Object[]) actual);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    static class MyClass {
        String name;

        public MyClass() {
            this.name = "test";
        }
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    static class MyArrayClass {
        MyClass[] values;

        public MyArrayClass() {
            values = new MyClass[0];
        }
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    static class MyStringClass {
        String value;

        public MyStringClass() {
            this.value = "";
        }
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    static class MyListClass implements Serializable {
        List<Object> values = Collections.emptyList();

        public MyListClass() {}
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    static class MyHashMapClass implements Serializable {
        Map<String, Object> values = Collections.emptyMap();

        public MyHashMapClass() {}
    }

    static class MyView extends BaseJsonSerializer {
        @Override
        public void serializeValue(SerializerProvider provider, Object value, JsonGenerator gen, Serializer serializers) throws IOException {
            gen.writeRawValue(value.toString());
        }
    }
}