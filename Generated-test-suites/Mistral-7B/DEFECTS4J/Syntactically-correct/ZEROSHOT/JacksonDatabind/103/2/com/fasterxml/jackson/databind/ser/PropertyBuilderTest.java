package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

import java.lang.reflect.Type;

public class PropertyBuilderTest {

    @Test
    public void testPropertyBuilder() throws Exception {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(MyPojo.class, null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // Test getClassAnnotations()
        Annotations classAnnotations = propertyBuilder.getClassAnnotations();
        assertNotNull(classAnnotations);
        assertTrue(classAnnotations.isEnabled(MapperFeature.DEFAULT_INTROSPECTION));

        // Test buildWriter() with basic types
        ObjectWriter objectWriter = new DefaultSerializerProvider().builderForType(MyPojo.class).build();
        BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(objectWriter.getSerializerProvider(), null, MyPojo.class.getType(), null, null, null, null, false);
        assertNotNull(actualWriter);

        // Test buildWriter() with custom serializers
        JsonSerializer<String> customSerializer = new JsonSerializer<String>() {
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString("custom");
            }
        };
        TypeSerializer typeSerializer = TypeSerializers.instance.findValueTypeSerializer(Object.class, String.class, null);
        BeanPropertyWriter actualWriterWithCustomSerializer = propertyBuilder.buildWriter(objectWriter.getSerializerProvider(), null, MyPojo.class.getType(), null, null, typeSerializer, null, false, customSerializer);
        assertEquals(customSerializer, actualWriterWithCustomSerializer.getSerializer());

        // Test buildWriter() with custom type serializer
        TypeSerializer customTypeSerializer = new SimpleTypeSerializer() {
            @Override
            public void serializeWithType(JsonGenerator gen, Object value, TypeSerializer contentTypeSerializer) throws IOException {
                gen.writeStartObject();
                gen.writeFieldName("type");
                contentTypeSerializer.serialize(value.getClass(), gen, null);
                gen.writeEndObject();
            }
        };
        BeanPropertyWriter actualWriterWithCustomTypeSerializer = propertyBuilder.buildWriter(objectWriter.getSerializerProvider(), null, MyPojo.class.getType(), null, null, null, null, false, null, null, customTypeSerializer);
        assertEquals(customTypeSerializer, actualWriterWithCustomTypeSerializer.getTypeSerializer());

        // Test useRealPropertyDefaults
        PropertyBuilder propertyBuilderWithUseRealPropertyDefaults = new PropertyBuilder(config, beanDesc) {
            @Override
            protected boolean _useRealPropertyDefaults = true;
        };
        Object defaultBean = propertyBuilderWithUseRealPropertyDefaults.getDefaultBean();
        assertNotNull(defaultBean);

        // Test getPropertyDefaultValue()
        String name = "name";
        JavaType type = MyPojo.class.getType();
        AnnotatedMember member = null;
        Object propertyDefaultValue = propertyBuilderWithUseRealPropertyDefaults.getPropertyDefaultValue(name, member, type);
        assertNotNull(propertyDefaultValue);
        assertEquals("defaultName", propertyDefaultValue);

        // Test getDefaultValue()
        Object getDefaultValue = propertyBuilderWithUseRealPropertyDefaults.getDefaultValue(type);
        assertNotNull(getDefaultValue);
        assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, getDefaultValue);

        // Test _throwWrapped()
        Throwable throwable = new RuntimeException("Test exception");
        Object propName = "propName";
        Object defaultBean2 = new Object();
        Object actual = propertyBuilderWithUseRealPropertyDefaults._throwWrapped(throwable, propName, defaultBean2);
        fail("_throwWrapped() should have thrown an IllegalArgumentException");
    }
}

class MyPojo {
    @JsonProperty("name")
    private String name;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonSerialize(using = CustomSerializer.class)
    private String customField;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomField() {
        return customField;
    }

    public void setCustomField(String customField) {
        this.customField = customField;
    }

    static class CustomSerializer extends JsonSerializer<String> {
        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString("customSerialized");
        }
    }
}