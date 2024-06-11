package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test class for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Create test object
        TestBean testBean = new TestBean("baseType");

        // Serialize to JSON
        String json = mapper.writeValueAsString(testBean);

        // Deserialize back to TestBean using TypeDeserializerBase
        TypeDeserializerBase deserializer = new TypeDeserializerBaseBase(
                mapper.getTypeFactory().constructType(TestBean.class),
                null,
                "typeProperty",
                true,
                TestBean.class)
        {
            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };

        // Deserialize JSON to TestBean
        TestBean result = mapper.readerForUpdating(TestBean.class)
                .withValueHandler(new StandardDeserializer(deserializer))
                .readValue(mapper.readTree(json));

        // Check if deserialized object is equal to original one
        assertEquals(testBean, result);
    }

    @Test
    public void testCustomDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.registerModule(new SimpleModule() {
            @Override
            public void setupDeserializers(DeserializationContext ctxt) throws Exception {
                ctxt.findValueDeserializer(TestBeanWithCustomTypeId.class, JsonTypeInfo.As.EXTERNAL_PROPERTY);
            }
        });

        // Create test object
        TestBeanWithCustomTypeId testBean = new TestBeanWithCustomTypeId("customTypeId");

        // Serialize to JSON
        String json = mapper.writeValueAsString(testBean);

        // Deserialize back to TestBeanWithCustomTypeId using TypeDeserializerBase
        TypeDeserializerBase deserializer = new TypeDeserializerBaseBase(
                mapper.getTypeFactory().constructType(TestBeanWithCustomTypeId.class),
                null,
                "typeProperty",
                true,
                TestBeanWithCustomTypeId.class)
        {
            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
                return null;
            }
        };

        // Deserialize JSON to TestBeanWithCustomTypeId
        TestBeanWithCustomTypeId result = mapper.readerForUpdating(TestBeanWithCustomTypeId.class)
                .withValueHandler(new StandardDeserializer(deserializer))
                .readValue(mapper.readTree(json));

        // Check if deserialized object is equal to original one
        assertEquals(testBean, result);
    }

    @Test
    public void testUnknownTypeIdDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Create test object with unknown type id
        TestBeanWithUnknownTypeId testBean = new TestBeanWithUnknownTypeId("unknownTypeId");

        // Serialize to JSON
        String json = mapper.writeValueAsString(testBean);

        // Deserialize back to TestBeanWithUnknownTypeId using TypeDeserializerBase
        TypeDeserializerBase deserializer = new TypeDeserializerBaseBase(
                mapper.getTypeFactory().constructType(TestBeanWithUnknownTypeId.class),
                null,
                "typeProperty",
                true,
                TestBeanWithUnknownTypeId.class)
        {
            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
                return null;
            }

            @Override
            protected Object _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                                                  TypeIdResolver idResolver, JavaType baseType) throws IOException {
                // Handle unknown type id by returning null to indicate failure
                return null;
            }
        };

        try {
            // Deserialize JSON to TestBeanWithUnknownTypeId
            TestBeanWithUnknownTypeId result = mapper.readerForUpdating(TestBeanWithUnknownTypeId.class)
                    .withValueHandler(new StandardDeserializer(deserializer))
                    .readValue(mapper.readTree(json));
            fail("Expected exception");
        } catch (JsonMappingException e) {
            // Check if exception message contains expected text
            String expectedMessage = "Unknown property \"unknownTypeId\" (not known to be a property of this type); check your getters and properties access";
            assertTrue("Unexpected exception message: " + e.getMessage(),
                    e.getMessage().contains(expectedMessage));
        }
    }

    @Test
    public void testEmptyJsonDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Deserialize empty JSON to TestBean using TypeDeserializerBase
        TypeDeserializerBase deserializer = new TypeDeserializerBaseBase(
                mapper.getTypeFactory().constructType(TestBean.class),
                null,
                "typeProperty",
                true,
                TestBean.class)
        {
            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
                return null;
            }
        };

        TestBean result = mapper.readerForUpdating(TestBean.class)
                .withValueHandler(new StandardDeserializer(deserializer))
                .readValue(mapper.readTree(""));

        // Check if deserialized object is instance of TestBean and has default values
        assertTrue(result instanceof TestBean);
        assertEquals("", result.name);
        assertNull(result.value);
    }

    // Test classes

    public static class TestBean {
        private String name;
        private String value;

        public TestBean(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class TestBeanWithCustomTypeId extends TestBean {
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "typeId")
        private String typeId;

        public TestBeanWithCustomTypeId(String name, String typeId) {
            super(name);
            this.typeId = typeId;
        }

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }
    }

    public static class TestBeanWithUnknownTypeId extends TestBean {
        @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
        public String unknownProperty;

        public String getUnknownProperty() {
            return unknownProperty;
        }

        public void setUnknownProperty(String unknownProperty) {
            this.unknownProperty = unknownProperty;
        }
    }
}