package edu.mscd.cs.jclo;

import static org.junit.Assert.*;
import org.junit.Test;

public class JCLOTest {

    @Test
    public void testJCLOConstructorObject() {
        JCLO jclo = new JCLO(new Object());
        assertNotNull(jclo.getObject(jclo.getField("object")));
    }

    @Test(expected = NullPointerException.class)
    public void testJCLOConstructorObjectNull() {
        new JCLO(null);
    }

    @Test
    public void testJCLOConstructorObjectAliases() {
        String[][] aliases = new String[][]{{"a", "aliasA"}, {"b", "aliasB"}};
        JCLO jclo = new JCLO(new Object(), aliases);
        Field fieldA = jclo.getField("a");
        assertEquals("aliasA", fieldA.getName());
        Field fieldB = jclo.getField("b");
        assertEquals("aliasB", fieldB.getName());
    }

    @Test
    public void testJCLOConstructorStringObject() {
        String prefix = "myPrefix_";
        JCLO jclo = new JCLO(prefix, new Object());
        Field field = jclo.getField("object");
        assertEquals(prefix, field.getName().substring(0, prefix.length()));
    }

    @Test(expected = NullPointerException.class)
    public void testJCLOConstructorStringObjectNull() {
        new JCLO(null, new Object());
    }

    @Test
    public void testJCLOConstructorStringObjectAliases() {
        String prefix = "myPrefix_";
        String[][] aliases = new String[][]{{"a", "aliasA"}, {"b", "aliasB"}};
        JCLO jclo = new JCLO(prefix, new Object(), aliases);
        Field fieldA = jclo.getField("a");
        assertEquals("myPrefix_a", fieldA.getName());
        Field fieldB = jclo.getField("b");
        assertEquals("myPrefix_b", fieldB.getName());
    }

    @Test
    public void testJCLOGetValueNullKey() {
        JCLO jclo = new JCLO(new Object());
        assertNull(jclo.getValue(null));
    }

    @Test
    public void testJCLOGetValueUnknownKey() {
        JCLO jclo = new JCLO(new Object());
        assertNull(jclo.getValue("unknownKey"));
    }

    @Test
    public void testJCLOGetValueStringValue() {
        String value = "Hello World";
        JCLO jclo = new JCLO(new Object());
        Field field = jclo.getField("key");
        jclo.setObject(field, value);
        assertEquals(value, jclo.getValue("key"));
    }

    @Test
    public void testJCLOGetValueBooleanValue() {
        JCLO jclo = new JCLO(new Object());
        Field field = jclo.getField("key");
        jclo.setObject(field, true);
        assertTrue(jclo.getBoolean("key"));
    }

    @Test
    public void testJCLOGetValueByteValue() {
        byte value = 42;
        JCLO jclo = new JCLO(new Object());
        Field field = jclo.getField("key");
        jclo.setObject(field, value);
        assertEquals(value, jclo.getByte("key"));
    }

    @Test
    public void testJCLOGetValueCharValue() {
        char value = 'A';
        JCLO jclo = new JCLO(new Object());
        Field field = jclo.getField("key");
        jclo.setObject(field, value);
        assertEquals(value, jclo.getChar("key"));
    }

    @Test
    public void testJCLOGetValueShortValue() {
        short value = 42;
        JCLO jclo = new JCLO(new Object());
        Field field = jclo.getField("key");
        jclo.setObject(field, value);
        assertEquals(value, jclo.getShort("key"));
    }

    @Test
    public void testJCLOGetValueIntValue() {
        int value = 42;
        JCLO jclo = new JCLO(new Object());
        Field field = jclo.getField("key");
        jclo.setObject(field, value);
        assertEquals(value, jclo.getInt("key"));
    }

    @Test
    public void testJCLOGetValueFloatValue() {
        float value = 42.0f;
        JCLO jclo = new JCLO(new Object());
        Field field = jclo.getField("key");
        jclo.setObject(field, value);
        assertEquals(value, jclo.getFloat("key"), 0.001f);
    }

    @Test
    public void testJCLOGetValueDoubleValue() {
        double value = 42.0;
        JCLO jclo = new JCLO(new Object());
        Field field = jclo.getField("key");
        jclo.setObject(field, value);
        assertEquals(value, jclo.getDouble("key"), 0.001);
    }

    @Test
    public void testJCLOGetValueLongValue() {
        long value = 42L;
        JCLO jclo = new JCLO(new Object());
        Field field = jclo.getField("key");
        jclo.setObject(field, value);
        assertEquals(value, jclo.getLong("key"));
    }

    @Test
    public void testJCLOGetValueStringArrayValue() {
        String[] value = new String[]{"Hello", "World"};
        JCLO jclo = new JCLO(new Object());
        Field field = jclo.getField("key");
        jclo.setObject(field, value);
        assertArrayEquals(value, jclo.getStringArray("key"));
    }

    @Test
    public void testJCLOParseSingleValue() {
        Object obj = new Object();
        String[] args = new String[]{"-key=value"};
        JCLO jclo = new JCLO(obj);
        jclo.parse(args);
        assertEquals("value", jclo.getValue("key"));
    }

    @Test
    public void testJCLOParseBooleanValue() {
        Object obj = new Object();
        String[] args = new String[]{"--key"};
        JCLO jclo = new JCLO(obj);
        jclo.parse(args);
        assertTrue(jclo.getBoolean("key"));
    }

    @Test
    public void testJCLOParseValueWithArray() {
        Object obj = new Object();
        String[] args = new String[]{"-key=value1,value2"};
        JCLO jclo = new JCLO(obj);
        jclo.parse(args);
        Object[] expected = new Object[]{new String("value1"), new String("value2")};
        assertArrayEquals(expected, jclo.getValue("key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOParseUnknownKey() {
        Object obj = new Object();
        String[] args = new String[]{"-unknownKey=value"};
        new JCLO(obj).parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOParseUnknownKeyWithValue() {
        Object obj = new Object();
        String[] args = new String[]{"-unknownKey=value"};
        new JCLO(obj).parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOParseUnknownKeyWithoutValue() {
        Object obj = new Object();
        String[] args = new String[]{"-unknownKey"};
        new JCLO(obj).parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOParseUnknownKeyWithArrayValue() {
        Object obj = new Object();
        String[] args = new String[]{"-unknownKey=value1,value2"};
        new JCLO(obj).parse(args);
    }

    @Test
    public void testJCLOUsage() {
        String expectedUsage = "-key=VALUE\n" +
                "--key\n";
        JCLO jclo = new JCLO(new Object(), null, null);
        assertEquals(expectedUsage, jclo.usage());
    }

    @Test
    public void testJCLOUsageWithAliases() {
        String[][] aliases = new String[][]{{"key", "alias"}, {"key2", "alias2"}};
        JCLO jclo = new JCLO(new Object(), null, aliases);
        String expectedUsage = "-key=VALUE\n" +
                "--alias\n" +
                "-key2=VALUE\n" +
                "--alias2\n";
        assertEquals(expectedUsage, jclo.usage());
    }
}