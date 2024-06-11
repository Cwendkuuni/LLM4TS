package com.example.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

/**
 * Test file for StdKeyDeserializer class.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @org.junit.Test
    public void testDeserializeKey() throws Exception {
        // Test boolean type
        StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);
        Object result = booleanDeserializer.deserializeKey("true", mapper.getDeserializationContext());
        assertEquals(Boolean.TRUE, result);

        result = booleanDeserializer.deserializeKey("false", mapper.getDeserializationContext());
        assertEquals(Boolean.FALSE, result);

        result = booleanDeserializer.deserializeKey("not_a_boolean", mapper.getDeserializationContext());
        assertNull(result);

        // Test byte type
        StdKeyDeserializer byteDeserializer = StdKeyDeserializer.forType(Byte.class);
        result = byteDeserializer.deserializeKey("1", mapper.getDeserializationContext());
        assertEquals((Byte) new Byte((byte) 1), result);

        result = byteDeserializer.deserializeKey("-127", mapper.getDeserializationContext());
        assertEquals((Byte) new Byte((byte) -127), result);

        result = byteDeserializer.deserializeKey("128", mapper.getDeserializationContext());
        assertNull(result);

        // Test short type
        StdKeyDeserializer shortDeserializer = StdKeyDeserializer.forType(Short.class);
        result = shortDeserializer.deserializeKey("1", mapper.getDeserializationContext());
        assertEquals((Short) new Short((short) 1), result);

        result = shortDeserializer.deserializeKey("-32768", mapper.getDeserializationContext());
        assertEquals((Short) new Short((short) -32768), result);

        result = shortDeserializer.deserializeKey("32768", mapper.getDeserializationContext());
        assertNull(result);

        // Test char type
        StdKeyDeserializer charDeserializer = StdKeyDeserializer.forType(Character.class);
        result = charDeserializer.deserializeKey("a", mapper.getDeserializationContext());
        assertEquals((Character) 'a', result);

        result = charDeserializer.deserializeKey("z", mapper.getDeserializationContext());
        assertEquals((Character) 'z', result);

        result = charDeserializer.deserializeKey("not_a_char", mapper.getDeserializationContext());
        assertNull(result);

        // Test int type
        StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);
        result = intDeserializer.deserializeKey("1", mapper.getDeserializationContext());
        assertEquals(1, result.hashCode());
        assertEquals(Integer.valueOf(1), result);

        result = intDeserializer.deserializeKey("-2147483648", mapper.getDeserializationContext());
        assertEquals(-2147483648, result.hashCode());
        assertEquals(Integer.valueOf(-2147483648), result);

        result = intDeserializer.deserializeKey("2147483647", mapper.getDeserializationContext());
        assertNull(result);

        // Test long type
        StdKeyDeserializer longDeserializer = StdKeyDeserializer.forType(Long.class);
        result = longDeserializer.deserializeKey("1", mapper.getDeserializationContext());
        assertEquals(1L, result.hashCode());
        assertEquals(Long.valueOf(1), result);

        result = longDeserializer.deserializeKey("-9223372036854775808", mapper.getDeserializationContext());
        assertEquals(-9223372036854775808L, result.hashCode());
        assertEquals(Long.valueOf(-9223372036854775808L), result);

        result = longDeserializer.deserializeKey("9223372036854775807", mapper.getDeserializationContext());
        assertNull(result);

        // Test float type
        StdKeyDeserializer floatDeserializer = StdKeyDeserializer.forType(Float.class);
        result = floatDeserializer.deserializeKey("1.0", mapper.getDeserializationContext());
        assertEquals(Float.valueOf(1.0f), result);

        result = floatDeserializer.deserializeKey("-3.4e38f", mapper.getDeserializationContext());
        assertEquals(Float.valueOf(-3.4E38f), result);

        result = floatDeserializer.deserializeKey("NaN", mapper.getDeserializationContext());
        assertEquals(Float.NaN, result, 0.0);

        result = floatDeserializer.deserializeKey("Infinity", mapper.getDeserializationContext());
        assertEquals(Float.POSITIVE_INFINITY, result, 0.0);

        result = floatDeserializer.deserializeKey("not_a_float", mapper.getDeserializationContext());
        assertNull(result);

        // Test double type
        StdKeyDeserializer doubleDeserializer = StdKeyDeserializer.forType(Double.class);
        result = doubleDeserializer.deserializeKey("1.0", mapper.getDeserializationContext());
        assertEquals(1.0, result, 0.0);
        assertEquals(Double.valueOf(1.0), result);

        result = doubleDeserializer.deserializeKey("-3.4e308", mapper.getDeserializationContext());
        assertEquals(-3.4E308, result, 0.0);
        assertEquals(Double.valueOf(-3.4E308), result);

        result = doubleDeserializer.deserializeKey("NaN", mapper.getDeserializationContext());
        assertEquals(Double.NaN, result, 0.0);

        result = doubleDeserializer.deserializeKey("Infinity", mapper.getDeserializationContext());
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0);

        result = doubleDeserializer.deserializeKey("not_a_double", mapper.getDeserializationContext());
        assertNull(result);

        // Test Locale type
        StdKeyDeserializer localeDeserializer = StdKeyDeserializer.forType(Locale.class);
        result = localeDeserializer.deserializeKey("en_US", mapper.getDeserializationContext());
        assertEquals(Locale.US, result);

        result = localeDeserializer.deserializeKey("fr_FR", mapper.getDeserializationContext());
        assertEquals(Locale.FRANCE, result);

        result = localeDeserializer.deserializeKey("not_a_locale", mapper.getDeserializationContext());
        assertNull(result);

        // Test URI type
        StdKeyDeserializer uriDeserializer = StdKeyDeserializer.forType(URI.class);
        result = uriDeserializer.deserializeKey("https://www.example.com/test", mapper.getDeserializationContext());
        assertEquals(new URI("https://www.example.com/test"), result);

        result = uriDeserializer.deserializeKey("not_a_uri", mapper.getDeserializationContext());
        assertNull(result);

        // Test URL type
        StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);
        result = urlDeserializer.deserializeKey("https://www.example.com/test", mapper.getDeserializationContext());
        assertEquals(new URL("https://www.example.com/test"), result);

        result = urlDeserializer.deserializeKey("not_a_url", mapper.getDeserializationContext());
        assertNull(result);

        // Test Class type
        StdKeyDeserializer classDeserializer = StdKeyDeserializer.forType(String.class);
        Class<?> clazz = Class.forName("java.util.Date");
        result = classDeserializer.deserializeKey(clazz.getName(), mapper.getDeserializationContext());
        assertEquals(clazz, result);

        result = classDeserializer.deserializeKey("not_a_class", mapper.getDeserializationContext());
        assertNull(result);

        // Test Enum type
        EnumResolver enumResolver = mock(EnumResolver.class);
        AnnotatedMethod enumFactoryMethod = mock(AnnotatedMethod.class);
        when(enumResolver.getEnumIds()).thenReturn(EnumSet.allOf(YourEnum.class));
        when(enumFactoryMethod.getDeclaringClass()).thenReturn(YourEnum.class);
        when(enumFactoryMethod.call1((Object) anyString())).thenReturn(YourEnum.VALUE1);

        StdKeyDeserializer enumDeserializer = new StdKeyDeserializer(enumResolver, enumFactoryMethod);
        result = enumDeserializer.deserializeKey("VALUE1", mapper.getDeserializationContext());
        assertEquals(YourEnum.VALUE1, result);

        result = enumDeserializer.deserializeKey("VALUE2", mapper.getDeserializationContext());
        assertEquals(YourEnum.VALUE2, result);

        result = enumDeserializer.deserializeKey("not_an_enum_value", mapper.getDeserializationContext());
        assertNull(result);

        // Test custom deserializer functionality
        BeanPropertyDefinition propertyDefinition = mock(BeanPropertyDefinition.class);
        JsonDeserializer<Date> dateDeserializer = mock(JsonDeserializer.class);

        StdKeyDeserializer delegatingDeserializer = new DelegatingKD(Date.class, dateDeserializer);
        result = delegatingDeserializer.deserializeKey("2023-02-15", mapper.getDeserializationContext());
        assertEquals(new Date(), result);

        // Test nullable key handling
        StdKeyDeserializer nullableDeserializer = StdKeyDeserializer.forType(String.class);
        result = nullableDeserializer.deserializeKey(null, mapper.getDeserializationContext());
        assertNull(result);

        // Test deserialization of custom serializable key classes
        StdKeyDeserializer customDeserializer = new StringCtorKeyDeserializer(YourCustomKeyClass.class.getConstructor(String.class));
        result = customDeserializer.deserializeKey("TestKey", mapper.getDeserializationContext());
        assertEquals(new YourCustomKeyClass("TestKey"), result);

        StdKeyDeserializer customFactoryDeserializer = new StringFactoryKeyDeserializer(YourCustomKeyClass.class.getMethod("createFromString"));
        result = customFactoryDeserializer.deserializeKey("TestKey", mapper.getDeserializationContext());
        assertEquals(new YourCustomKeyClass(), result);
    }

    private static class YourCustomKeyClass {
        private final String key;

        public YourCustomKeyClass(String key) {
            this.key = key;
        }

        public static YourCustomKeyClass createFromString(String key) {
            return new YourCustomKeyClass(key);
        }
    }

    private static enum YourEnum {
        VALUE1, VALUE2
    }
}