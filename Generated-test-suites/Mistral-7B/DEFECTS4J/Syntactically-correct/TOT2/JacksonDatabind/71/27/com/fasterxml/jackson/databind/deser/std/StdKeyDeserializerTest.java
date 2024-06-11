import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanKeyDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        Object deserializedKey = deserializer.deserializeKey("true", new TestContext());
        assertTrue((Boolean) deserializedKey);
        deserializer = StdKeyDeserializer.forType(Boolean.class);
        try {
            deserializer.deserializeKey("false5", new TestContext());
            fail("Expected JsonMappingException for non-boolean value");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testByteKeyDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        Object deserializedKey = deserializer.deserializeKey("127", new TestContext());
        assertEquals((Byte) new Byte((byte) 127), deserializedKey);
        deserializer = StdKeyDeserializer.forType(Byte.class);
        try {
            deserializer.deserializeKey("-128", new TestContext());
            fail("Expected JsonMappingException for out-of-byte range value");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testShortKeyDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        Object deserializedKey = deserializer.deserializeKey("32767", new TestContext());
        assertEquals((Short) new Short((short) 32767), deserializedKey);
        deserializer = StdKeyDeserializer.forType(Short.class);
        try {
            deserializer.deserializeKey("-32768", new TestContext());
            fail("Expected JsonMappingException for out-of-short range value");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testCharKeyDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        Object deserializedKey = deserializer.deserializeKey("A", new TestContext());
        assertEquals((Character) 'A', deserializedKey);
        deserializer = StdKeyDeserializer.forType(Character.class);
        try {
            deserializer.deserializeKey("\\u0001", new TestContext());
            fail("Expected JsonMappingException for invalid Unicode escape sequence");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testIntKeyDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        Object deserializedKey = deserializer.deserializeKey("2147483647", new TestContext());
        assertEquals((Integer) 2147483647, deserializedKey);
        deserializer = StdKeyDeserializer.forType(Integer.class);
        try {
            deserializer.deserializeKey("-2147483648", new TestContext());
            fail("Expected JsonMappingException for out-of-int range value");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testLongKeyDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        Object deserializedKey = deserializer.deserializeKey("9223372036854775807", new TestContext());
        assertEquals((Long) 9223372036854775807L, deserializedKey);
        deserializer = StdKeyDeserializer.forType(Long.class);
        try {
            deserializer.deserializeKey("-9223372036854775808", new TestContext());
            fail("Expected JsonMappingException for out-of-long range value");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testFloatKeyDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        Object deserializedKey = deserializer.deserializeKey("3.141592653589793", new TestContext());
        assertEquals((Float) 3.141592653589793f, deserializedKey);
        deserializer = StdKeyDeserializer.forType(Float.class);
        try {
            deserializer.deserializeKey("invalid-float", new TestContext());
            fail("Expected JsonMappingException for invalid float value");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testDoubleKeyDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        Object deserializedKey = deserializer.deserializeKey("6.283185307179586E+01", new TestContext());
        assertEquals((Double) 6.283185307179586E+01d, deserializedKey);
        deserializer = StdKeyDeserializer.forType(Double.class);
        try {
            deserializer.deserializeKey("invalid-double", new TestContext());
            fail("Expected JsonMappingException for invalid double value");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleKeyDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
        Object deserializedKey = deserializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, deserializedKey);
        deserializer = StdKeyDeserializer.forType(Locale.class);
        try {
            deserializer.deserializeKey("invalid-locale", new TestContext());
            fail("Expected JsonMappingException for invalid locale value");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testDateKeyDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        Object deserializedKey = deserializer.deserializeKey("2022-01-01", new TestContext());
        assertEquals(new Date(0), deserializedKey); // Testing with an empty Date instance as the expected result, as per StdKeyDeserializer's deserializeKey implementation
        deserializer = StdKeyDeserializer.forType(Date.class);
        try {
            deserializer.deserializeKey("invalid-date-format", new TestContext());
            fail("Expected JsonMappingException for invalid date format");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testCalendarKeyDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
        Object deserializedKey = deserializer.deserializeKey("2022-01-01", new TestContext());
        assertTrue(deserializedKey instanceof Calendar);
        Calendar expectedCalendar = Calendar.getInstance();
        expectedCalendar.set(Calendar.YEAR, 2022);
        expectedCalendar.set(Calendar.MONTH, Calendar.JANUARY);
        expectedCalendar.set(Calendar.DAY_OF_MONTH, 1);
        assertEquals(expectedCalendar, deserializedKey);
        deserializer = StdKeyDeserializer.forType(Calendar.class);
        try {
            deserializer.deserializeKey("invalid-date-format", new TestContext());
            fail("Expected JsonMappingException for invalid date format");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testUUIDKeyDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
        Object deserializedKey = deserializer.deserializeKey("00000000-0000-0000-0000-000000000000", new TestContext());
        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), deserializedKey);
        deserializer = StdKeyDeserializer.forType(UUID.class);
        try {
            deserializer.deserializeKey("invalid-UUID-format", new TestContext());
            fail("Expected JsonMappingException for invalid UUID format");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testURIKeyDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        Object deserializedKey = deserializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URI("http://example.com"), deserializedKey);
        deserializer = StdKeyDeserializer.forType(URI.class);
        try {
            deserializer.deserializeKey("invalid-URI-format", new TestContext());
            fail("Expected JsonMappingException for invalid URI format");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testURLKeyDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        Object deserializedKey = deserializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URL("http://example.com"), deserializedKey);
        deserializer = StdKeyDeserializer.forType(URL.class);
        try {
            deserializer.deserializeKey("invalid-URL-format", new TestContext());
            fail("Expected JsonMappingException for invalid URL format");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testClassKeyDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        Object deserializedKey = deserializer.deserializeKey("String", new TestContext());
        assertEquals(String.class, deserializedKey);
        deserializer = StdKeyDeserializer.forType(String.class);
        try {
            deserializer.deserializeKey("InvalidClass", new TestContext());
            fail("Expected JsonMappingException for invalid class name");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testEnumKeyDeserialization() throws Exception {
        // To test EnumKeyDeserialization, assume there is an Enum class called "Colors" with values: RED, GREEN, BLUE
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Colors.class);
        Object deserializedKey = deserializer.deserializeKey("RED", new TestContext());
        assertEquals(Colors.RED, deserializedKey);
        deserializer = StdKeyDeserializer.forType(Colors.class);
        try {
            deserializer.deserializeKey("INVALID_ENUM_VALUE", new TestContext());
            fail("Expected JsonMappingException for invalid enum value");
        } catch (JsonMappingException e) {
            // Expected
        }
    }
}