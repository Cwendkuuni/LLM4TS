import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        Object deserializedKey = serializer.deserializeKey("true", new TestContext());
        assertTrue((Boolean) deserializedKey);
        deserializedKey = serializer.deserializeKey("false", new TestContext());
        assertFalse((Boolean) deserializedKey);
    }

    @Test
    public void testByteType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        Object deserializedKey = serializer.deserializeKey("127", new TestContext());
        assertEquals((Byte) new Byte((byte) 127), deserializedKey);
        deserializedKey = serializer.deserializeKey("-128", new TestContext());
        assertEquals((Byte) new Byte((byte) -128), deserializedKey);
        deserializedKey = serializer.deserializeKey("256", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testShortType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        Object deserializedKey = serializer.deserializeKey("32767", new TestContext());
        assertEquals((Short) new Short((short) 32767), deserializedKey);
        deserializedKey = serializer.deserializeKey("-32768", new TestContext());
        assertEquals((Short) new Short((short) -32768), deserializedKey);
        deserializedKey = serializer.deserializeKey("65536", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testCharacterType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        Object deserializedKey = serializer.deserializeKey("'A'", new TestContext());
        assertEquals((Character) 'A', deserializedKey);
        deserializedKey = serializer.deserializeKey("'\\u00E9'", new TestContext());
        assertEquals((Character) '\u00E9', deserializedKey);
        deserializedKey = serializer.deserializeKey("'\\u013F'", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testIntegerType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        Object deserializedKey = serializer.deserializeKey("2147483647", new TestContext());
        assertEquals((Integer) new Integer(2147483647), deserializedKey);
        deserializedKey = serializer.deserializeKey("-2147483648", new TestContext());
        assertEquals((Integer) new Integer(-2147483648), deserializedKey);
        deserializedKey = serializer.deserializeKey("4611686018427387903", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testLongType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        Object deserializedKey = serializer.deserializeKey("9223372036854775807", new TestContext());
        assertEquals((Long) new Long(9223372036854775807L), deserializedKey);
        deserializedKey = serializer.deserializeKey("-9223372036854775808", new TestContext());
        assertEquals((Long) new Long(-9223372036854775808L), deserializedKey);
        deserializedKey = serializer.deserializeKey("18446744073709551616", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testFloatType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        Object deserializedKey = serializer.deserializeKey("3.14159265", new TestContext());
        assertEquals((Float) new Float(3.14159265F), deserializedKey);
        deserializedKey = serializer.deserializeKey("-1.23e10", new TestContext());
        assertEquals((Float) new Float(-1.23E10F), deserializedKey);
        deserializedKey = serializer.deserializeKey("inf", new TestContext());
        assertEquals((Float) new Float(Float.POSITIVE_INFINITY), deserializedKey);
        deserializedKey = serializer.deserializeKey("nan", new TestContext());
        assertEquals((Float) new Float(Float.NaN), deserializedKey);
        deserializedKey = serializer.deserializeKey("1e-308", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testDateType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(java.util.Date.class);
        Object deserializedKey = serializer.deserializeKey("2022-12-31", new TestContext());
        assertEquals(new java.util.Date("Sat Dec 31 00:00:00 CET 2022"), deserializedKey);
        deserializedKey = serializer.deserializeKey("2023-01-01T01:23:45Z", new TestContext());
        assertEquals(new java.util.Date("Sun Jan 01 01:23:45 CET 2023"), deserializedKey);
    }

    @Test
    public void testCalendarType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(java.util.Calendar.class);
        Object deserializedKey = serializer.deserializeKey("2022-12-31", new TestContext());
        assertEquals(java.util.Calendar.getInstance().with(java.util.Calendar.YEAR, 2022)
                .with(java.util.Calendar.MONTH, java.util.Calendar.DECEMBER)
                .with(java.util.Calendar.DAY_OF_MONTH, 31), deserializedKey);
        deserializedKey = serializer.deserializeKey("2023-01-01T01:23:45Z", new TestContext());
        assertEquals(java.util.Calendar.getInstance().with(java.util.Calendar.YEAR, 2023)
                .with(java.util.Calendar.MONTH, java.util.Calendar.JANUARY)
                .with(java.util.Calendar.DAY_OF_MONTH, 1)
                .with(java.util.Calendar.HOUR_OF_DAY, 1)
                .with(java.util.Calendar.MINUTE, 23)
                .with(java.util.Calendar.SECOND, 45), deserializedKey);
    }

    @Test
    public void testUUIDType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(java.util.UUID.class);
        Object deserializedKey = serializer.deserializeKey("00000000-0000-0000-0000-000000000000", new TestContext());
        assertEquals(java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"), deserializedKey);
        deserializedKey = serializer.deserializeKey("11111111-1111-1111-1111-111111111111", new TestContext());
        assertEquals(java.util.UUID.fromString("11111111-1111-1111-1111-111111111111"), deserializedKey);
    }

    @Test
    public void testURIType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(java.net.URI.class);
        Object deserializedKey = serializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new java.net.URI("http://example.com"), deserializedKey);
        deserializedKey = serializer.deserializeKey("https://example.org/path/%7Euser/file.txt", new TestContext());
        assertEquals(new java.net.URI("https://example.org/path/~user/file.txt"), deserializedKey);
    }

    @Test
    public void testURLType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(java.net.URL.class);
        Object deserializedKey = serializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new java.net.URL("http://example.com"), deserializedKey);
        deserializedKey = serializer.deserializeKey("https://example.org/path/%7Euser/file.txt", new TestContext());
        assertEquals(new java.net.URL("https://example.org/path/~user/file.txt"), deserializedKey);
    }

    @Test
    public void testClassType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(java.lang.String.class);
        Object deserializedKey = serializer.deserializeKey("java.util.ArrayList", new TestContext());
        assertEquals(java.util.ArrayList.class, deserializedKey);
        deserializedKey = serializer.deserializeKey("java.util.HashMap", new TestContext());
        assertEquals(java.util.HashMap.class, deserializedKey);
    }

    @Test
    public void testEnumType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Weekday.class);
        Object deserializedKey = serializer.deserializeKey("MONDAY", new TestContext());
        assertEquals(Weekday.MONDAY, deserializedKey);
        deserializedKey = serializer.deserializeKey("tUESDAY", new TestContext());
        assertEquals(Weekday.TUESDAY, deserializedKey);
        deserializedKey = serializer.deserializeKey("FRIDAY", new TestContext());
        assertEquals(Weekday.FRIDAY, deserializedKey);
        deserializedKey = serializer.deserializeKey("SATURDAY", new TestContext());
        assertEquals(Weekday.SATURDAY, deserializedKey);
        deserializedKey = serializer.deserializeKey("SUNDAY", new TestContext());
        assertEquals(Weekday.SUNDAY, deserializedKey);
        deserializedKey = serializer.deserializeKey("THURSDAY", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testExceptionHandling() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(java.util.Date.class);
        Object deserializedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNotNull(deserializedKey);
        assertInstanceOf(DeserializationProblem.class, deserializedKey);
    }

    @Test
    public void testNullKey() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(java.util.Date.class);
        Object deserializedKey = serializer.deserializeKey(null, new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testUnknownType() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(java.util.Date.class);
        Object deserializedKey = serializer.deserializeKey("invalidType", new TestContext());
        assertNotNull(deserializedKey);
        assertInstanceOf(DeserializationProblem.class, deserializedKey);
    }
}

class TestContext extends DeserializationContext {
    @Override
    public Object newInstance(Class<?> c) throws InstantiationException, IllegalAccessException {
        return c.newInstance();
    }

    @Override
    public Object findValueInMap(Map<String, Object> m, String key) {
        return m.getOrDefault(key, null);
    }

    @Override
    public Object findClass(String name) throws Exception {
        Class<?> clazz = Class.forName(name);
        return clazz.newInstance();
    }

    @Override
    public JsonStreamParser getParser() {
        return null;
    }

    @Override
    public Object readValue(Class<?> type, JsonParser p) throws IOException {
        return null;
    }
}