package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.*;

public class PropertyBuilderTest {

    private ObjectMapper mapper;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() throws Exception {
        ObjectMapper.Feature feature = ObjectMapper.Feature.WRITE_EMPTY_JSON_ARRAYS;
        mapper = new ObjectMapper();
        mapper.disable(feature);
    }

    @Test
    public void testWriterCreation_emptyBean_defaultValues() throws JsonProcessingException {
        // Given
        BeanDescription beanDescription = new BeanDescription(Foo.class);
        SerializationConfig config = mapper.getSerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);

        // When
        Foo foo = new Foo();
        Object defaultBean = propertyBuilder.getDefaultBean();
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(mapper.getSerializerProvider(), null, null, null, null, null, null, false);

        // Then
        Object valueToSuppress = beanPropertyWriter.getValueToSuppress();
        Assert.assertNull(valueToSuppress);

        // Test writing default values
        String json = mapper.writeValueAsString(foo);
        Foo fooDeserialized = mapper.readValue(json, Foo.class);
        Assert.assertEquals(foo, fooDeserialized);
    }

    @Test
    public void testWriterCreation_customSerializationType_overriddenDefaultValues() throws JsonProcessingException {
        // Given
        BeanDescription beanDescription = new BeanDescription(FooWithCustomSerializationType.class);
        SerializationConfig config = mapper.getSerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);

        // When
        FooWithCustomSerializationType foo = new FooWithCustomSerializationType();
        Object defaultBean = propertyBuilder.getDefaultBean();
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(mapper.getSerializerProvider(), null, null, null, null, null, null, false);

        // Then
        Object valueToSuppress = beanPropertyWriter.getValueToSuppress();
        Assert.assertEquals("customValue", valueToSuppress);

        // Test writing overridden values
        String json = mapper.writeValueAsString(foo);
        FooWithCustomSerializationType fooDeserialized = mapper.readValue(json, FooWithCustomSerializationType.class);
        Assert.assertEquals(foo, fooDeserialized);
    }

    @Test
    public void testWriterCreation_customSerializationType_suppressNulls() throws JsonProcessingException {
        // Given
        BeanDescription beanDescription = new BeanDescription(FooWithCustomSerializationType.class);
        SerializationConfig config = mapper.getSerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);

        // When
        FooWithCustomSerializationType foo = new FooWithCustomSerializationType();
        Object defaultBean = propertyBuilder.getDefaultBean();
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(mapper.getSerializerProvider(), null, null, null, null, null, null, true);

        // Then
        Object valueToSuppress = beanPropertyWriter.getValueToSuppress();
        Assert.assertEquals("customValue", valueToSuppress);

        // Test writing overridden values without nulls
        foo.setNullValue("nullValue");
        String json = mapper.writeValueAsString(foo);
        FooWithCustomSerializationType fooDeserialized = mapper.readValue(json, FooWithCustomSerializationType.class);
        Assert.assertNotNull(fooDeserialized);
        Assert.assertFalse(fooDeserialized.hasNullValue());
    }

    @Test
    public void testWriterCreation_customSerializationType_suppressEmptyCollections() throws JsonProcessingException {
        // Given
        BeanDescription beanDescription = new BeanDescription(FooWithEmptyList.class);
        SerializationConfig config = mapper.getSerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);

        // When
        FooWithEmptyList foo = new FooWithEmptyList();
        Object defaultBean = propertyBuilder.getDefaultBean();
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(mapper.getSerializerProvider(), null, null, null, null, null, null, false);

        // Then
        Object valueToSuppress = beanPropertyWriter.getValueToSuppress();
        Assert.assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, valueToSuppress);

        // Test writing empty collections
        String json = mapper.writeValueAsString(foo);
        Assert.assertTrue(json.isEmpty());

        // Test writing non-empty collections
        foo.setListValue(Arrays.asList("value1", "value2"));
        json = mapper.writeValueAsString(foo);
        FooWithEmptyList fooDeserialized = mapper.readValue(json, FooWithEmptyList.class);
        Assert.assertEquals(Arrays.asList("value1", "value2"), fooDeserialized.getListValue());
    }

    @Test
    public void testWriterCreation_customSerializationType_customNullSerializer() throws JsonProcessingException {
        // Given
        BeanDescription beanDescription = new BeanDescription(FooWithCustomNullSerializer.class);
        SerializationConfig config = mapper.getSerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);

        // When
        FooWithCustomNullSerializer foo = new FooWithCustomNullSerializer();
        Object defaultBean = propertyBuilder.getDefaultBean();
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(mapper.getSerializerProvider(), null, null, null, null, null, null, false);

        // Then
        Object valueToSuppress = beanPropertyWriter.getValueToSuppress();
        Assert.assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, valueToSuppress);

        // Test writing null values with custom null serializer
        String json = mapper.writeValueAsString(foo);
        Assert.assertTrue(json.isEmpty());

        // Test reading null values with custom null deserializer
        FooWithCustomNullSerializer fooDeserialized = mapper.readValue(json, FooWithCustomNullSerializer.class);
        Assert.assertTrue(fooDeserialized.hasNullValue());
    }

    @Test
    public void testWriterCreation_customSerializationType_customUnwrappingNameTransformer() throws JsonProcessingException {
        // Given
        BeanDescription beanDescription = new BeanDescription(FooWithCustomUnwrappingNameTransformer.class);
        SerializationConfig config = mapper.getSerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);

        // When
        FooWithCustomUnwrappingNameTransformer foo = new FooWithCustomUnwrappingNameTransformer();
        Object defaultBean = propertyBuilder.getDefaultBean();
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(mapper.getSerializerProvider(), null, null, null, null, null, null, false);

        // Then
        Object valueToSuppress = beanPropertyWriter.getValueToSuppress();
        Assert.assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, valueToSuppress);

        // Test writing unwrapped values
        String json = mapper.writeValueAsString(foo);
        FooWithCustomUnwrappingNameTransformer fooDeserialized = mapper.readValue(json, FooWithCustomUnwrappingNameTransformer.class);
        Assert.assertEquals("unwrappedValue", fooDeserialized.getUnwrappedValue());
    }

    // Add more test cases if necessary

    static class Foo {
        // Add getters and setters here
    }

    static class FooWithCustomSerializationType extends Foo {
        private String customValue;

        public String getCustomValue() {
            return customValue;
        }

        public void setCustomValue(String customValue) {
            this.customValue = customValue;
        }
    }

    static class FooWithEmptyList extends Foo {
        private List<String> listValue;

        public List<String> getListValue() {
            return listValue;
        }

        public void setListValue(List<String> listValue) {
            this.listValue = listValue;
        }
    }

    static class FooWithCustomNullSerializer extends Foo {
        private String nullValue;

        public String getNullValue() {
            return nullValue;
        }

        public void setNullValue(String nullValue) {
            this.nullValue = nullValue;
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public boolean hasNullValue() {
            return nullValue != null;
        }
    }

    static class FooWithCustomUnwrappingNameTransformer extends Foo {
        private String unwrappedValue;

        public String getUnwrappedValue() {
            return unwrappedValue;
        }

        public void setUnwrappedValue(String unwrappedValue) {
            this.unwrappedValue = unwrappedValue;
        }

        @JsonUnwrapped
        @JsonProperty("innerValue")
        public String getInnerValue() {
            return unwrappedValue;
        }

        public void setInnerValue(String innerValue) {
            this.unwrappedValue = innerValue;
        }

        @JsonSerialize(using = InnerValueSerializer.class)
        static class InnerValueSerializer extends StdSerializer<String> {
            public InnerValueSerializer() {
                this(null);
            }

            public InnerValueSerializer(Class<String> t) {
                super(t);
            }

            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                jgen.writeFieldName("innerValue");
                jgen.writeString(value);
            }
        }
    }
}