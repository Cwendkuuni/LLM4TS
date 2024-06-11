package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertInstanceOf;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

@Test
public class StdKeyDeserializerTest {

    @Test
    public void testBoolean() throws Exception {
        StdKeyDeserializer boolKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        Object key = boolKD.deserializeKey("true", new TestContext());
        assertTrue((Boolean) key, "Wrong boolean value");

        key = boolKD.deserializeKey("false", new TestContext());
        assertInstanceOf(Boolean.class, key);
        assertEquals(((Boolean) key).booleanValue(), false);

        key = boolKD.deserializeKey("notABoolean", new TestContext());
        assertEquals(StdKeyDeserializer.class.getName() + ".deserializeKey: not a valid representation", key.toString());
    }

    @Test
    public void testByte() throws Exception {
        StdKeyDeserializer byteKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);

        Object key = byteKD.deserializeKey("1", new TestContext());
        assertEquals(Byte.valueOf((byte) 1), key);

        try {
            key = byteKD.deserializeKey("257", new TestContext());
            fail("Should have thrown IOException: overflow, value cannot be represented as 8-bit value");
        } catch (IOException e) {
            // Expected
        }

        key = byteKD.deserializeKey("-1", new TestContext());
        assertEquals(Byte.valueOf((byte) -1), key);
    }

    @Test
    public void testShort() throws Exception {
        StdKeyDeserializer shortKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class);

        Object key = shortKD.deserializeKey("15", new TestContext());
        assertEquals(Short.valueOf((short) 15), key);

        try {
            key = shortKD.deserializeKey("32768", new TestContext());
            fail("Should have thrown IOException: overflow, value cannot be represented as 16-bit value");
        } catch (IOException e) {
            // Expected
        }

        key = shortKD.deserializeKey("-15", new TestContext());
        assertEquals(Short.valueOf((short) -15), key);
    }

    @Test
    public void testChar() throws Exception {
        StdKeyDeserializer charKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);

        Object key = charKD.deserializeKey("a", new TestContext());
        assertEquals(Character.valueOf('a'), key);

        try {
            key = charKD.deserializeKey("𠮷", new TestContext());
            fail("Should have thrown IOException: can only convert 1-character Strings");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testInt() throws Exception {
        StdKeyDeserializer intKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);

        Object key = intKD.deserializeKey("100", new TestContext());
        assertEquals(Integer.valueOf(100), key);

        key = intKD.deserializeKey("2147483647", new TestContext());
        // Expected: "not a valid representation"
        assertNull(key);

        key = intKD.deserializeKey("-100", new TestContext());
        assertEquals(Integer.valueOf(-100), key);
    }

    @Test
    public void testLong() throws Exception {
        StdKeyDeserializer longKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);

        Object key = longKD.deserializeKey("10000000000", new TestContext());
        assertEquals(Long.valueOf(10000000000L), key);

        try {
            key = longKD.deserializeKey("9223372036854775808", new TestContext());
            fail("Should have thrown IOException: overflow, value cannot be represented as 64-bit value");
        } catch (IOException e) {
            // Expected
        }

        key = longKD.deserializeKey("-10000000000", new TestContext());
        assertEquals(Long.valueOf(-10000000000L), key);
    }

    @Test
    public void testFloat() throws Exception {
        StdKeyDeserializer floatKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class);

        Object key = floatKD.deserializeKey("1.0", new TestContext());
        assertEquals(Float.valueOf(1.0f), key);

        key = floatKD.deserializeKey("3.14", new TestContext());
        assertEquals(Float.valueOf(3.14f), key);

        try {
            key = floatKD.deserializeKey("inf", new TestContext());
            fail("Should have thrown IOException: not a valid representation");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testDouble() throws Exception {
        StdKeyDeserializer doubleKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);

        Object key = doubleKD.deserializeKey("1.0", new TestContext());
        assertEquals(Double.valueOf(1.0), key);

        key = doubleKD.deserializeKey("3.14", new TestContext());
        assertEquals(Double.valueOf(3.14), key);

        key = doubleKD.deserializeKey("1e-304", new TestContext());
        assertEquals(Double.valueOf(1.0E-304), key);

        try {
            key = doubleKD.deserializeKey("inf", new TestContext());
            fail("Should have thrown IOException: not a valid representation");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLocale() throws Exception {
        StdKeyDeserializer localeKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);

        Object key = localeKD.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, key);

        try {
            key = localeKD.deserializeKey("not_a_valid_locale", new TestContext());
            fail("Should have thrown IOException: not a valid representation");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testDate() throws Exception {
        StdKeyDeserializer dateKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);

        Object key = dateKD.deserializeKey("2023-03-03", new TestContext());
        assertEquals(new Date(2023 - 1900, 2 - 1, 3), key);

        try {
            key = dateKD.deserializeKey("not_a_valid_date", new TestContext());
            fail("Should have thrown IOException: not a valid representation");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCalendar() throws Exception {
        StdKeyDeserializer calendarKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class);

        Object key = calendarKD.deserializeKey("2023-03-03", new TestContext());
        GregorianCalendar cal = (GregorianCalendar) key;
        assertEquals(2023, cal.get(Calendar.YEAR));
        assertEquals(2, cal.get(Calendar.MONTH));
        assertEquals(3, cal.get(Calendar.DATE));

        try {
            key = calendarKD.deserializeKey("not_a_valid_date", new TestContext());
            fail("Should have thrown IOException: not a valid representation");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testUUID() throws Exception {
        StdKeyDeserializer uuidKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class);

        Object key = uuidKD.deserializeKey("e89d4cd9-7f12-4567-a714-a71shg567890", new TestContext());
        assertEquals(UUID.fromString("e89d4cd9-7f12-4567-a714-a71shg567890"), key);

        try {
            key = uuidKD.deserializeKey("not_a_valid_uuid", new TestContext());
            fail("Should have thrown IOException: not a valid representation");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testURI() throws Exception {
        StdKeyDeserializer uriKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);

        Object key = uriKD.deserializeKey("http://example.com/path/with/query?param1=value1&param2=value2", new TestContext());
        assertEquals(new URI("http://example.com/path/with/query?param1=value1&param2=value2"), key);

        try {
            key = uriKD.deserializeKey("not_a_valid_uri", new TestContext());
            fail("Should have thrown IOException: not a valid representation");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testURL() throws Exception {
        StdKeyDeserializer urlKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);

        Object key = urlKD.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URL("http://example.com"), key);

        try {
            key = urlKD.deserializeKey("not_a_valid_url", new TestContext());
            fail("Should have thrown MalformedURLException: not a valid representation");
        } catch (MalformedURLException e) {
            // Expected
        }
    }

    @Test
    public void testClass() throws Exception {
        StdKeyDeserializer classKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, String.class);

        Object key = classKD.deserializeKey("java.lang.String", new TestContext());
        assertEquals(String.class, key);

        try {
            key = classKD.deserializeKey("java.util.Date", new TestContext());
            fail("Should have thrown IOException: unable to parse key as Class");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testByteArray() throws Exception {
        StdKeyDeserializer byteArrayKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE_ARRAY, byte[].class);

        Object key = byteArrayKD.deserializeKey("Rm9udCAoKSxoJwopCgoKCgoKCgo=", new TestContext());
        byte[] decoded = ctxt.getConfig().getBase64Variant().decode(key.toString());
        byte[] expected = { 0x72, 0x65, 0x61, 0x75, 0x74, 0x68, 0x6f, 0x64, 0x65, 0x20, 0x63, 0x6f, 0x6d, 0x70, 0x61, 0x6b, 0x65, 0x6e };
        assertArrayEquals(expected, decoded);

        try {
            key = byteArrayKD.deserializeKey("not_a_valid_byte_array", new TestContext());
            fail("Should have thrown IOException: not a valid representation");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testEnum() throws Exception {
        EnumResolver enumResolver = EnumResolver.construct(MyEnum.class, ctxt.getAnnotationIntrospector());
        StdKeyDeserializer enumKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, MyEnum.class, enumResolver);

        Object key = enumKD.deserializeKey("VALUE_ONE", new TestContext());
        assertEquals(MyEnum.VALUE_ONE, key);

        key = enumKD.deserializeKey("VALUE_TWO", new TestContext());
        assertEquals(MyEnum.VALUE_TWO, key);

        key = enumKD.deserializeKey("VALUE_THREE", new TestContext());
        assertEquals(MyEnum.VALUE_THREE, key);

        key = enumKD.deserializeKey("unknownValue", new TestContext());
        assertNull(key);

        key = enumKD.deserializeKey("", new TestContext());
        assertNull(key);
    }

    @Test
    public void testString() throws Exception {
        StdKeyDeserializer stringKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);

        Object key = stringKD.deserializeKey("test", new TestContext());
        assertEquals("test", key);

        key = stringKD.deserializeKey("", new TestContext());
        assertEquals("", key);
    }
}