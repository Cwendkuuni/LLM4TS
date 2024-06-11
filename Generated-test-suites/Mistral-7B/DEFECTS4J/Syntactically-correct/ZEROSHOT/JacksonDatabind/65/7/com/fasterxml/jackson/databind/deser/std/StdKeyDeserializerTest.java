package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;
    private StdKeyDeserializer stringKD;
    private StdKeyDeserializer stringCtorKD;
    private StdKeyDeserializer stringFactoryKD;
    private StdKeyDeserializer intKD;
    private StdKeyDeserializer longKD;
    private StdKeyDeserializer booleanKD;
    private StdKeyDeserializer charKD;
    private StdKeyDeserializer dateKD;
    private StdKeyDeserializer uriKD;
    private StdKeyDeserializer urlKD;
    private StdKeyDeserializer localeKD;
    private StdKeyDeserializer uuidKD;
    private StdKeyDeserializer enumKD;
    private StdKeyDeserializer currencyKD;
    private StdKeyDeserializer classKD;
    private EnumResolver enumResolver;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();

        stringKD = StdKeyDeserializer.forType(String.class);
        stringCtorKD = new StdKeyDeserializer(-1, String.class, String.class.getDeclaredConstructor(String.class));
        stringFactoryKD = new StdKeyDeserializer(-1, String.class, String.class.getDeclaredMethod("valueOf", String.class));
        intKD = StdKeyDeserializer.forType(Integer.class);
        longKD = StdKeyDeserializer.forType(Long.class);
        booleanKD = StdKeyDeserializer.forType(Boolean.class);
        charKD = StdKeyDeserializer.forType(Character.class);
        dateKD = StdKeyDeserializer.forType(java.util.Date.class);
        uriKD = StdKeyDeserializer.forType(URI.class);
        urlKD = StdKeyDeserializer.forType(URL.class);
        localeKD = StdKeyDeserializer.forType(Locale.class);
        uuidKD = StdKeyDeserializer.forType(UUID.class);
        enumResolver = objectMapper.getFactory().getEnumResolver();
        enumKD = new StdKeyDeserializer(-1, enumResolver.getEnumClass());
        currencyKD = StdKeyDeserializer.forType(Currency.class);
        classKD = StdKeyDeserializer.forType(Class.class);
    }

    @Test
    public void testStringKeyDeserializer() throws IOException {
        JavaType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Integer.class);
        String key = "test";
        Object result = objectMapper.readValue("{\"k\":\"" + key + "\", \"v\":1}", mapType);
        Assertions.assertEquals(key, ((Map) result).get(key));
    }

    @Test
    public void testIntKeyDeserializer() throws IOException {
        JavaType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Integer.class);
        int key = 1;
        Object result = objectMapper.readValue("{\"k\":1, \"v\":2}", mapType);
        Assertions.assertEquals(key, ((Map) result).get(key));
    }

    @Test
    public void testBooleanKeyDeserializer() throws IOException {
        JavaType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Boolean.class);
        boolean key = true;
        Object result = objectMapper.readValue("{\"k\":true, \"v\":false}", mapType);
        Assertions.assertEquals(key, ((Map) result).get(key));
    }

    @Test
    public void testCharKeyDeserializer() throws IOException {
        JavaType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Character.class);
        char key = 'a';
        Object result = objectMapper.readValue("{\"k\":\"a\", \"v\":'b'}", mapType);
        Assertions.assertEquals(key, ((Map) result).get(key));
    }

    @Test
    public void testDateKeyDeserializer() throws IOException {
        JavaType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, java.util.Date.class);
        String keyString = "2022-12-31";
        java.util.Date key = java.util.Date.from(java.time.LocalDate.parse(keyString).atStartOfDay().atZone(java.util.ZoneId.systemDefault()).toInstant());
        Object result = objectMapper.readValue("{\"k\":\"" + keyString + "\", \"v\":\"value\"}", mapType);
        Assertions.assertEquals(key, ((Map) result).get(key));
    }

    @Test
    public void testUriKeyDeserializer() throws IOException {
        JavaType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, URI.class);
        String keyString = "http://test.com";
        URI key = new URI(keyString);
        Object result = objectMapper.readValue("{\"k\":\"" + keyString + "\", \"v\":\"value\"}", mapType);
        Assertions.assertEquals(key, ((Map) result).get(key));
    }

    @Test
    public void testUrlKeyDeserializer() throws IOException {
        JavaType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, URL.class);
        String keyString = "http://test.com";
        URL key = new URL(keyString);
        Object result = objectMapper.readValue("{\"k\":\"" + keyString + "\", \"v\":\"value\"}", mapType);
        Assertions.assertEquals(key, ((Map) result).get(key));
    }

    @Test
    public void testLocaleKeyDeserializer() throws IOException {
        JavaType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Locale.class);
        String keyString = "en_US";
        Locale key = Locale.forLanguageTag(keyString);
        Object result = objectMapper.readValue("{\"k\":\"" + keyString + "\", \"v\":\"value\"}", mapType);
        Assertions.assertEquals(key, ((Map) result).get(key));
    }

    @Test
    public void testEnumKeyDeserializer() throws IOException {
        JavaType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Enum.class);
        String keyString = "TEST";
        Enum enumConstant = EnumSet.allOf(TestEnum.class)[0];
        Object result = objectMapper.readValue("{\"k\":\"" + keyString + "\", \"v\":\"value\"}", mapType);
        Assertions.assertEquals(enumConstant, ((Map) result).get(keyString));
    }

    @Test
    public void testCurrencyKeyDeserializer() throws IOException {
        JavaType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Currency.class);
        String keyString = "USD";
        Currency key = Currency.getInstance(keyString);
        Object result = objectMapper.readValue("{\"k\":\"" + keyString + "\", \"v\":\"value\"}", mapType);
        Assertions.assertEquals(key, ((Map) result).get(key));
    }

    @Test
    public void testClassKeyDeserializer() throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException {
        JavaType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, String.class);
        Class<?> clazz = String.class;
        Constructor<?> constructor = clazz.getDeclaredConstructor(String.class);
        Object result = objectMapper.readValue("{\"k\":\"" + clazz.getName() + "\", \"v\":\"value\"}", mapType);
        Assertions.assertEquals(constructor.newInstance("test"), ((Map) result).get(clazz.getName()));
    }

    @Test
    public void testDeserializeKeyWithUnsupportedType() throws IOException {
        JavaType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Double.class);
        String keyString = "test";
        Assertions.assertThrows(JsonMappingException.class, () -> objectMapper.readValue("{\"k\":\"" + keyString + "\", \"v\":1.0}", mapType));
    }

    @Test
    public void testDeserializeNullKey() throws IOException {
        JavaType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Integer.class);
        Object result = objectMapper.readValue("{\"k\":null, \"v\":1}", mapType);
        Assertions.assertNull(((Map) result).get(null));
    }

    @Test
    public void testDeserializeInvalidKey() throws IOException {
        JavaType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Integer.class);
        Object result = objectMapper.readValue("{\"k\":\"test\", \"v\":1}", mapType);
        Assertions.assertEquals("test", ((Map) result).get("test"));
        Assertions.assertThrows(JsonMappingException.class, () -> objectMapper.readValue("{\"k\":\"test\", \"v\":1}", mapType));
    }

    @Test
    public void testDeserializeKeyWithInvalidType() throws IOException {
        JavaType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Integer.class);
        Object result = objectMapper.readValue("{\"k\":1, \"v\":1}", mapType);
        Assertions.assertEquals(1, ((Map) result).get(1));
        Assertions.assertThrows(JsonMappingException.class, () -> objectMapper.readValue("{\"k\":\"test\", \"v\":1}", mapType));
    }

    @Test
    public void testDeserializeKeyWithEnumClass() throws IOException {
        JavaType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Enum.class);
        Enum enumConstant = EnumSet.allOf(TestEnum.class)[1];
        Object result = objectMapper.readValue("{\"k\":\"TEST2\", \"v\":\"value\"}", mapType);
        Assertions.assertEquals(enumConstant, ((Map) result).get("TEST2"));
    }

    @Test
    public void testDeserializeKeyWithNonStringKey() throws IOException {
        JavaType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, Number.class, Integer.class);
        long key = 1;
        Object result = objectMapper.readValue("{\"k\":1, \"v\":1}", mapType);
        Assertions.assertEquals(key, ((Map) result).get(key));
    }

    private static class TestEnum {
        TEST1 test1;
        TEST2 test2;

        @JsonDeserialize(using = StdKeyDeserializer.class)
        enum TEST {
            TEST1,
            TEST2
        }

        public static class TEST1 {
            @JacksonStdImpl
            public static class Test1StringKD extends StdKeyDeserializer {
                public Test1StringKD() {
                    super(-1, TEST1.class);
                }
            }

            public static class Test1StringCtorKD extends StdKeyDeserializer {
                public Test1StringCtorKD() {
                    super(-1, TEST1.class, TEST1.class.getDeclaredConstructor(String.class));
                }
            }

            public static class Test1StringFactoryKD extends StdKeyDeserializer {
                public Test1StringFactoryKD() {
                    super(-1, TEST1.class, TEST1.class.getDeclaredMethod("valueOf", String.class));
                }
            }
        }

        public static class TEST2 {
            @JacksonStdImpl
            public static class Test2StringKD extends StdKeyDeserializer {
                public Test2StringKD() {
                    super(-1, TEST2.class);
                }
            }

            public static class Test2StringCtorKD extends StdKeyDeserializer {
                public Test2StringCtorKD() {
                    super(-1, TEST2.class, TEST2.class.getDeclaredConstructor(String.class));
                }
            }

            public static class Test2StringFactoryKD extends StdKeyDeserializer {
                public Test2StringFactoryKD() {
                    super(-1, TEST2.class, TEST2.class.getDeclaredMethod("valueOf", String.class));
                }
            }
        }

        public static class TEST1 {
            @JacksonStdImpl
            public static class Test1StringKD extends StdKeyDeserializer {
                public Test1StringKD() {
                    super(-1, TEST1.class);
                }
            }

            public static class Test1StringCtorKD extends StdKeyDeserializer {
                public Test1StringCtorKD() {
                    super(-1, TEST1.class, TEST1.class.getDeclaredConstructor(String.class));
                }
            }

            public static class Test1StringFactoryKD extends StdKeyDeserializer {
                public Test1StringFactoryKD() {
                    super(-1, TEST1.class, TEST1.class.getDeclaredMethod("valueOf", String.class));
                }
            }
        }

        public static class TEST2 {
            @JacksonStdImpl
            public static class Test2StringKD extends StdKeyDeserializer {
                public Test2StringKD() {
                    super(-1, TEST2.class);
                }
            }

            public static class Test2StringCtorKD extends StdKeyDeserializer {
                public Test2StringCtorKD() {
                    super(-1, TEST2.class, TEST2.class.getDeclaredConstructor(String.class));
                }
            }

            public static class Test2StringFactoryKD extends StdKeyDeserializer {
                public Test2StringFactoryKD() {
                    super(-1, TEST2.class, TEST2.class.getDeclaredMethod("valueOf", String.class));
                }
            }
        }
    }
}