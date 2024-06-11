package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.SerializableStringSerializer;
import com.fasterxml.jackson.databind.util.EnumResolver;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.*;

/**
 * Test class for {@link StdKeyDeserializer}
 */
public class StdKeyDeserializerTest {

    @SuppressWarnings("unchecked")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY,
            property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = StringKeyDeserializerTest.StringMap.StringBooleanKey.class, name = "STRING_BOOLEAN"),
            @JsonSubTypes.Type(value = StringKeyDeserializerTest.StringMap.StringByteKey.class, name = "STRING_BYTE"),
            @JsonSubTypes.Type(value = StringKeyDeserializerTest.StringMap.StringCharKey.class, name = "STRING_CHAR"),
            @JsonSubTypes.Type(value = StringKeyDeserializerTest.StringMap.StringShortKey.class, name = "STRING_SHORT"),
            @JsonSubTypes.Type(value = StringKeyDeserializerTest.StringMap.StringIntKey.class, name = "STRING_INT"),
            @JsonSubTypes.Type(value = StringKeyDeserializerTest.StringMap.StringLongKey.class, name = "STRING_LONG"),
            @JsonSubTypes.Type(value = StringKeyDeserializerTest.StringMap.StringFloatKey.class, name = "STRING_FLOAT"),
            @JsonSubTypes.Type(value = StringKeyDeserializerTest.StringMap.StringDoubleKey.class, name = "STRING_DOUBLE"),
            @JsonSubTypes.Type(value = StringKeyDeserializerTest.StringMap.StringLocaleKey.class, name = "STRING_LOCALE"),
            @JsonSubTypes.Type(value = StringKeyDeserializerTest.StringMap.StringDateKey.class, name = "STRING_DATE"),
            @JsonSubTypes.Type(value = StringKeyDeserializerTest.StringMap.StringCalendarKey.class, name = "STRING_CALENDAR"),
            @JsonSubTypes.Type(value = StringKeyDeserializerTest.StringMap.StringUuidKey.class, name = "STRING_UUID"),
            @JsonSubTypes.Type(value = StringKeyDeserializerTest.StringMap.StringUriKey.class, name = "STRING_URI"),
            @JsonSubTypes.Type(value = StringKeyDeserializerTest.StringMap.StringUrlKey.class, name = "STRING_URL"),
            @JsonSubTypes.Type(value = StringKeyDeserializerTest.StringMap.StringClassKey.class, name = "STRING_CLASS"),
            @JsonSubTypes.Type(value = StringKeyDeserializerTest.StringMap.StringEnumKey.class, name = "STRING_ENUM"),
            @JsonSubTypes.Type(value = StringKeyDeserializerTest.StringMap.StringCurrencyKey.class, name = "STRING_CURRENCY")
    })
    public static class StringMap {

        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY,
                property = "type")
        @JsonSubTypes({
            @JsonSubTypes.Type(value = StringBooleanKey.class, name = "BOOLEAN"),
            @JsonSubTypes.Type(value = StringByteKey.class, name = "BYTE"),
            @JsonSubTypes.Type(value = StringCharKey.class, name = "CHAR"),
            @JsonSubTypes.Type(value = StringShortKey.class, name = "SHORT"),
            @JsonSubTypes.Type(value = StringIntKey.class, name = "INT"),
            @JsonSubTypes.Type(value = StringLongKey.class, name = "LONG"),
            @JsonSubTypes.Type(value = StringFloatKey.class, name = "FLOAT"),
            @JsonSubTypes.Type(value = StringDoubleKey.class, name = "DOUBLE"),
            @JsonSubTypes.Type(value = StringLocaleKey.class, name = "LOCALE"),
            @JsonSubTypes.Type(value = StringDateKey.class, name = "DATE"),
            @JsonSubTypes.Type(value = StringCalendarKey.class, name = "CALENDAR"),
            @JsonSubTypes.Type(value = StringUuidKey.class, name = "UUID"),
            @JsonSubTypes.Type(value = StringUriKey.class, name = "URI"),
            @JsonSubTypes.Type(value = StringUrlKey.class, name = "URL"),
            @JsonSubTypes.Type(value = StringClassKey.class, name = "CLASS"),
            @JsonSubTypes.Type(value = StringEnumKey.class, name = "ENUM"),
            @JsonSubTypes.Type(value = StringCurrencyKey.class, name = "CURRENCY")
        })
        public static class Map {
            private Map() {
            }

            @JsonProperty("map")
            public Map<String, Object> getMap() {
                return map;
            }

            @JsonProperty("map")
            public void setMap(Map<String, Object> map) {
                this.map = map;
            }

            private Map<String, Object> map;
        }

        public static class StringBooleanKey extends StringKeyBase<Boolean> {
            public StringBooleanKey() {
                super(TypeReferenceFactory.stringToValueType("java.lang.Boolean"));
            }
        }

        public static class StringByteKey extends StringKeyBase<Byte> {
            public StringByteKey() {
                super(TypeReferenceFactory.stringToValueType("byte"));
            }
        }

        public static class StringCharKey extends StringKeyBase<Character> {
            public StringCharKey() {
                super(TypeReferenceFactory.stringToValueType("char"));
            }
        }

        public static class StringShortKey extends StringKeyBase<Short> {
            public StringShortKey() {
                super(TypeReferenceFactory.stringToValueType("short"));
            }
        }

        public static class StringIntKey extends StringKeyBase<Integer> {
            public StringIntKey() {
                super(TypeReferenceFactory.stringToValueType("int"));
            }
        }

        public static class StringLongKey extends StringKeyBase<Long> {
            public StringLongKey() {
                super(TypeReferenceFactory.stringToValueType("long"));
            }
        }

        public static class StringFloatKey extends StringKeyBase<Float> {
            public StringFloatKey() {
                super(TypeReferenceFactory.stringToValueType("float"));
            }
        }

        public static class StringDoubleKey extends StringKeyBase<Double> {
            public StringDoubleKey() {
                super(TypeReferenceFactory.stringToValueType("double"));
            }
        }

        public static class StringLocaleKey extends StringKeyBase<Locale> {
            public StringLocaleKey() {
                super(TypeReferenceFactory.stringToValueType("java.util.Locale"));
            }
        }

        public static class StringDateKey extends StringKeyBase<Date> {
            public StringDateKey() {
                super(TypeReferenceFactory.stringToValueType("java.util.Date"));
            }
        }

        public static class StringCalendarKey extends StringKeyBase<Calendar> {
            public StringCalendarKey() {
                super(TypeReferenceFactory.stringToValueType("java.util.Calendar"));
            }
        }

        public static class StringUuidKey extends StringKeyBase<UUID> {
            public StringUuidKey() {
                super(TypeReferenceFactory.stringToValueType("java.util.UUID"));
            }
        }

        public static class StringUriKey extends StringKeyBase<URI> {
            public StringUriKey() {
                super(TypeReferenceFactory.stringToValueType("java.net.URI"));
            }
        }

        public static class StringUrlKey extends StringKeyBase<URL> {
            public StringUrlKey() {
                super(TypeReferenceFactory.stringToValueType("java.net.URL"));
            }
        }

        public static class StringClassKey extends StringKeyBase<Class<?>> {
            public StringClassKey() {
                super(ClassUtil.weirdTypeDescriptor(Object.class));
            }
        }

        public static class StringEnumKey extends StringKeyBase<Enum<?>> {
            public StringEnumKey() {
                super(Enum.class);
            }
        }

        public static class StringCurrencyKey extends StringKeyBase<Currency> {
            public StringCurrencyKey() {
                super(Currency.class);
            }
        }

        public static class StringKeyBase<T> extends AbstractTypeValidator.BaseWithValueType<T> {
            protected StringKeyBase(TypeValueType vt) {
                super(vt);
            }

            protected T _parse(String key, DeserializationContext ctxt) throws IOException {
                return (T) StdKeyDeserializer.forType(getValueType()).deserializeKey(key, ctxt);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static class TestMapDeserializer extends JsonDeserializer<Map<String, Object>> {
        @Override
        public Map<String, Object> deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            ObjectNode node = p.getCodec().readTree(p);
            Map<String, Object> map = new ObjectMapper().readValue(node, StringMap.Map.class);
            return map;
        }
    }

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializerFactory(new DefaultSerializerProvider.Factory());
        mapper.setSerializerProvider(mapper.getSerializerProvider());
        mapper.registerModule(new SimpleModule() {
            @Override
            public void setupSerializerProvider(SerializerProvider p) {
                p.setNullValueSerializer(new SerializableStringSerializer());
            }
        });

        String json = "{\"map\":{\"STRING_BOOLEAN\":{\"type\":\"STRING_BOOLEAN\", \"key\":\"true\"}, \"STRING_BYTE\":{\"type\":\"STRING_BYTE\", \"key\":\"10\"}, \"STRING_CHAR\":{\"type\":\"STRING_CHAR\", \"key\":\"a\"}, \"STRING_SHORT\":{\"type\":\"STRING_SHORT\", \"key\":\"-123\"}, \"STRING_INT\":{\"type\":\"STRING_INT\", \"key\":\"4567\"}, \"STRING_LONG\":{\"type\":\"STRING_LONG\", \"key\":\"123456789012345\"}, \"STRING_FLOAT\":{\"type\":\"STRING_FLOAT\", \"key\":\"123.321\"}, \"STRING_DOUBLE\":{\"type\":\"STRING_DOUBLE\", \"key\":\"123.321\"}, \"STRING_LOCALE\":{\"type\":\"STRING_LOCALE\", \"key\":\"fr_FR\"}, \"STRING_DATE\":{\"type\":\"STRING_DATE\", \"key\":\"2010-12-12\"}, \"STRING_CALENDAR\":{\"type\":\"STRING_CALENDAR\", \"key\":\"2010-12-12\"}, \"STRING_UUID\":{\"type\":\"STRING_UUID\", \"key\":\"123e4567-e89b-12d3-a456-426655440000\"}, \"STRING_URI\":{\"type\":\"STRING_URI\", \"key\":\"http://example.com/\"}, \"STRING_URL\":{\"type\":\"STRING_URL\", \"key\":\"http://example.com/\"}, \"STRING_CLASS\":{\"type\":\"STRING_CLASS\", \"key\":\"java.lang.String\"}, \"STRING_ENUM\":{\"type\":\"STRING_ENUM\", \"key\":\"Apple\"}, \"STRING_CURRENCY\":{\"type\":\"STRING_CURRENCY\", \"key\":\"USD\"}}}";

        Map<String, Object> map = mapper.readValue(json, TestMapDeserializer.class);
        System.out.println(map);

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Class<?> valueClass = value.getClass();

            StdKeyDeserializer deserializer = StdKeyDeserializer.forType(valueClass);
            Object parsedKey = deserializer.deserializeKey(key, mapper.getDeserializationContext());

            assertEquals(value, parsedKey);
        }
    }
}

class TestStringDeserializer extends StdKeyDeserializer {
    TestStringDeserializer() {
        super(TypeKey.STRING);
    }

    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        return key;
    }
}

class TestEnumDeserializer extends EnumKD {
    TestEnumDeserializer() {
        super(EnumResolver.construct(TestEnum.class));
    }
}

class TestEnum {
    @JsonValue
    public String name;

    TestEnum(String name) {
        this.name = name;
    }

    public static final TestEnum APPLE = new TestEnum("Apple");
    public static final TestEnum BANANA = new TestEnum("Banana");
}