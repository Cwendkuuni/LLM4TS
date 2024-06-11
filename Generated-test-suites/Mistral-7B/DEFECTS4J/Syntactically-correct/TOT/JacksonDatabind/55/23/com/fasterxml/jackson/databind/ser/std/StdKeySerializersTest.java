package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;

import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test cases for {@link StdKeySerializers} class.
 */
public class StdKeySerializersTest {

    /**
     * Test for method {@link StdKeySerializers#getStdKeySerializer(SerializationConfig, Class, boolean)}.
     * @throws Exception if the test case fails
     */
    @Test
    public void testGetStdKeySerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        
        // Test for null rawKeyType
        assertNull(StdKeySerializers.getStdKeySerializer(config, null, false));

        // Test for Object rawKeyType
        assertNotNull(StdKeySerializers.getStdKeySerializer(config, Object.class, false));

        // Test for String rawKeyType
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER,
                StdKeySerializers.getStdKeySerializer(config, String.class, false));

        // Test for int rawKeyType
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER,
                StdKeySerializers.getStdKeySerializer(config, int.class, false));

        // Test for Calendar rawKeyType
        assertEquals(StdKeySerializers.Default.TYPE_CALENDAR,
                StdKeySerializers.getStdKeySerializer(config, Calendar.class, false)
                        ._typeId);

        // Test for Date rawKeyType
        assertEquals(StdKeySerializers.Default.TYPE_DATE,
                StdKeySerializers.getStdKeySerializer(config, Date.class, false)
                        ._typeId);

        // Test for Enum rawKeyType
        @JsonSerialize(using = StdKeySerializers.Default.class)
        enum TestEnum {
            FOOBAR
        }
        assertEquals(StdKeySerializers.Default.TYPE_ENUM,
                StdKeySerializers.getStdKeySerializer(config, TestEnum.class, false)
                        ._typeId);

        // Test for Class rawKeyType
        assertEquals(StdKeySerializers.Default.TYPE_CLASS,
                StdKeySerializers.getStdKeySerializer(config, Class.class, false)
                        ._typeId);

        // Test for java.util.UUID rawKeyType
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING,
                StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, false)
                        ._typeId);
    }

    /**
     * Test for method {@link StdKeySerializers#getFallbackKeySerializer(SerializationConfig, Class)}.
     * @throws Exception if the test case fails
     */
    @Test
    public void testGetFallbackKeySerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        assertNull(StdKeySerializers.getFallbackKeySerializer(config, null));

        // Test for int rawKeyType
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER,
                StdKeySerializers.getFallbackKeySerializer(config, int.class));

        // Test for String rawKeyType
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER,
                StdKeySerializers.getFallbackKeySerializer(config, String.class));

        // Test for Enum rawKeyType
        @JsonSerialize(using = StdKeySerializers.Default.class)
        enum TestEnum {
            FOOBAR
        }
        assertEquals(StdKeySerializers.Default.TYPE_ENUM,
                StdKeySerializers.getFallbackKeySerializer(config, TestEnum.class)
                        ._typeId);

        // Test for Class rawKeyType
        assertEquals(StdKeySerializers.Default.TYPE_CLASS,
                StdKeySerializers.getFallbackKeySerializer(config, Class.class)
                        ._typeId);

        // Test for java.util.UUID rawKeyType
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING,
                StdKeySerializers.getFallbackKeySerializer(config, java.util.UUID.class)
                        ._typeId);
    }

    /**
     * Test for method {@link StdKeySerializers#getDefault()}.
     * @throws Exception if the test case fails
     */
    @Test
    public void testGetDefault() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER,
                StdKeySerializers.getDefault());
    }

    /**
     * Test for {@link StdKeySerializers#Dynamic} class.
     * @throws Exception if the test case fails
     */
    @Test
    public void testDynamicKeySerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();

        // Create an instance of Dynamic class
        StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();

        // Test serialization with known types
        assertEquals("java.lang.Integer", dynamicSerializer.getClass().getName(),
                dynamicSerializer.serialize(Integer.valueOf(5), null, null).getFieldName());
        assertEquals("java.util.Date", dynamicSerializer.getClass().getName(),
                dynamicSerializer.serialize(new Date(), null, null).getFieldName());
        assertEquals("java.util.Calendar", dynamicSerializer.getClass().getName(),
                dynamicSerializer.serialize(Calendar.getInstance(), null, null).getFieldName());

        // Test serialization with unknown type
        Object unknownObject = new Object() {
            @Override
            public String toString() {
                return "unknownObject";
            }
        };
        assertEquals("unknownObject", dynamicSerializer.serialize(unknownObject, null, null).getFieldName());

        // Test serialization with Enum type
        @JsonSerialize(using = StdKeySerializers.Default.class)
        enum TestEnum {
            FOOBAR
        }
        assertEquals(TestEnum.FOOBAR.name(), dynamicSerializer.serialize(TestEnum.FOOBAR, null, null).getFieldName());

        // Test serialization with Class type
        assertEquals("java.lang.Class", dynamicSerializer.getClass().getName(),
                dynamicSerializer.serialize(getClass(), null, null).getFieldName());

        // Test serialization with java.util.UUID type
        assertEquals("5f0dcaa-7dd7-43bb-a714-5e63b332b2e1", dynamicSerializer.serialize(java.util.UUID.randomUUID(), null, null).getFieldName());

        // Test serialization with annotated class
        @JsonSerialize(using = StdKeySerializers.StringKeySerializer.class)
        class AnnotatedClass {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
        assertEquals("name", dynamicSerializer.serialize(new AnnotatedClass(), null, null).getFieldName());

        // Test serialization with custom KeySerializer
        class CustomKeySerializer extends StdSerializer<Object> {
            public CustomKeySerializer() { super(Object.class, false); }

            @Override
            public void serialize(Object value, JsonGenerator g, SerializerProvider provider) throws IOException {
                g.writeFieldName("customKey");
            }
        }
        CustomKeySerializer customKeySerializer = new CustomKeySerializer();
        ObjectMapper customMapper = new ObjectMapper();
        customMapper.registerSerializer(Object.class, customKeySerializer);
        SerializationConfig customConfig = customMapper.getSerializationConfig();
        assertEquals("customKey", dynamicSerializer.serialize(null, null, customConfig).getFieldName());
    }
}