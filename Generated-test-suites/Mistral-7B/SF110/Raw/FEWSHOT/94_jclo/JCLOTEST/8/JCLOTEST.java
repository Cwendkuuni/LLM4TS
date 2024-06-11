package edu.mscd.cs.jclo;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

public class JCLOTest {

    private JCLO cliObj;

    @Before
    public void setup() {
        Object testObj = new TestClass();
        cliObj = new JCLO(testObj);
    }

    @Test
    public void testJCLOConstructorWithObject() {
        JCLO cliObj = new JCLO(new TestClass());
        assertNull(cliObj.getPrefix());
        assertNull(cliObj.getAliases());
        assertEquals(TestClass.class, cliObj.object.getClass());
    }

    @Test
    public void testJCLOConstructorWithObjectAndAliases() {
        String[][] aliases = {{"long", "l"}, {"string", "s"}};
        JCLO cliObj = new JCLO(new TestClass(), aliases);
        assertEquals("", cliObj.getPrefix());
        assertArrayEquals(aliases, cliObj.aliases);
        assertEquals(TestClass.class, cliObj.object.getClass());
    }

    @Test
    public void testJCLOConstructorWithPrefixAndObject() {
        String prefix = "prefix_";
        JCLO cliObj = new JCLO(prefix, new TestClass());
        assertEquals(prefix, cliObj.prefix);
        assertNull(cliObj.aliases);
        assertEquals(TestClass.class, cliObj.object.getClass());
    }

    @Test
    public void testJCLOConstructorWithPrefixAndObjectAndAliases() {
        String prefix = "prefix_";
        String[][] aliases = {{"long", "l"}, {"string", "s"}};
        JCLO cliObj = new JCLO(prefix, new TestClass(), aliases);
        assertEquals(prefix, cliObj.prefix);
        assertArrayEquals(aliases, cliObj.aliases);
        assertEquals(TestClass.class, cliObj.object.getClass());
    }

    @Test
    public void testGetObject() {
        Field field = null;
        try {
            field = cliObj.getClass().getDeclaredField("field1");
            field.setAccessible(true);
            Object fieldValue = cliObj.getObject(field);
            assertEquals(1, fieldValue.hashCode());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetField() {
        Field field = cliObj.getField("field1");
        assertSame(field, cliObj.getClass().getDeclaredField("field1"));
    }

    @Test
    public void testGetValue() {
        Field field = null;
        try {
            field = cliObj.getClass().getDeclaredField("field1");
            field.setAccessible(true);
            Object fieldValue = cliObj.getObject(field);
            String value = (String) cliObj.getValue("field1");
            assertEquals("field1Value", value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetArrayType() {
        assertEquals("int[]", cliObj.getArrayType(int[].class));
    }

    @Test
    public void testToString() {
        String expected = "int: field1 = field1Value\nboolean: flag = false\n";
        assertEquals(expected, cliObj.toString());
    }

    @Test
    public void testUsage() {
        String expected = "--flag=boolean\n--field1=int\n";
        assertEquals(expected, cliObj.usage());
    }

    @Test
    public void testParseAdditional() {
        String[] args = {"--additional", "arg1", "arg2"};
        cliObj.parseAdditional(args, 0);
        Object additional = cliObj.getObject(cliObj.getField("additional"));
        assertArrayEquals(new String[]{"arg1", "arg2"}, (String[]) additional);
    }

    @Test
    public void testAddToArray() {
        Field field = null;
        try {
            field = cliObj.getClass().getDeclaredField("field1");
            field.setAccessible(true);
            Object fieldValue = cliObj.getObject(field);
            Object newValue = new Integer(2);
            Object newArray = cliObj.addToArray(field, newValue);
            assertNotNull(newArray);
            assertEquals(3, newArray.getClass().getComponentCount());
            assertArrayEquals(new Integer[]{1, 2}, (Object[]) newArray);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetKey() {
        String expected = "field1";
        assertEquals(expected, cliObj.getKey("field1"));
    }

    @Test
    public void testGetBooleanValue() {
        assertTrue(cliObj.getBooleanValue("true"));
        assertFalse(cliObj.getBooleanValue("false"));
    }

    @Test
    public void testMakeObject() {
        String booleanValue = "true";
        Object booleanObj = cliObj.makeObject("boolean", booleanValue);
        assertTrue(booleanObj instanceof Boolean);
        assertEquals(true, ((Boolean) booleanObj).booleanValue());

        String intValue = "123";
        Object intObj = cliObj.makeObject("int", intValue);
        assertTrue(intObj instanceof Integer);
        assertEquals(123, ((Integer) intObj).intValue());

        String stringValue = "testValue";
        Object stringObj = cliObj.makeObject("java.lang.String", stringValue);
        assertTrue(stringObj instanceof String);
        assertEquals("testValue", (String) stringObj);

        String charValue = "A";
        Object charObj = cliObj.makeObject("char", charValue);
        assertTrue(charObj instanceof Character);
        assertEquals('A', ((Character) charObj).charValue());

        String shortValue = "127";
        Object shortObj = cliObj.makeObject("short", shortValue);
        assertTrue(shortObj instanceof Short);
        assertEquals(127, ((Short) shortObj).shortValue());

        String intValue2 = "123456";
        Object intObj2 = cliObj.makeObject("int", intValue2);
        assertTrue(intObj2 instanceof Integer);
        assertEquals(123456, ((Integer) intObj2).intValue());

        String floatValue = "123.45";
        Object floatObj = cliObj.makeObject("float", floatValue);
        assertTrue(floatObj instanceof Float);
        assertEquals(123.45f, ((Float) floatObj).floatValue());

        String doubleValue = "123.456";
        Object doubleObj = cliObj.makeObject("double", doubleValue);
        assertTrue(doubleObj instanceof Double);
        assertEquals(123.456, ((Double) doubleObj).doubleValue());

        String longValue = "1234567890";
        Object longObj = cliObj.makeObject("long", longValue);
        assertTrue(longObj instanceof Long);
        assertEquals(1234567890L, ((Long) longObj).longValue());

        String longValue2 = "9876543210";
        Object longObj2 = cliObj.makeObject("long", longValue2);
        assertTrue(longObj2 instanceof Long);
        assertEquals(-9876543210L, ((Long) longObj2).longValue());
    }

    @Test
    public void testGetEqualsValue() {
        String equalsValue = "someValue";
        assertEquals("someValue", cliObj.getEqualsValue("--some-option=someValue"));
    }

    @Test
    public void testParse() {
        String[] args = {"--flag=false", "--field1=1", "--field2=2", "--field3=3.5", "--field4=testValue"};
        cliObj.parse(args);

        assertEquals(false, cliObj.getBoolean("flag"));
        assertEquals(1, cliObj.getInt("field1"));
        assertEquals(2, cliObj.getInt("field2"));
        assertEquals(3.5, cliObj.getFloat("field3"));
        assertEquals("testValue", cliObj.getString("field4"));

        // Test for optional 'additional' field
        args = new String[]{"--additional=arg1", "--additional=arg2"};
        cliObj.parse(args);
        Object additional = cliObj.getObject(cliObj.getField("additional"));
        assertArrayEquals(new String[]{"arg1", "arg2"}, (String[]) additional);
    }

    @Test
    public void testGetBoolean() {
        assertEquals(false, cliObj.getBoolean("flag"));
    }

    @Test
    public void testGetByte() {
        assertThrows(IllegalArgumentException.class, () -> cliObj.getByte("nonExistentField"));

        cliObj.setObject(cliObj.getField("field1"), (byte) 1);
        assertEquals((byte) 1, cliObj.getByte("field1"));
    }

    @Test
    public void testGetChar() {
        assertThrows(IllegalArgumentException.class, () -> cliObj.getChar("nonExistentField"));

        cliObj.setObject(cliObj.getField("field1"), (char) 'A');
        assertEquals('A', cliObj.getChar("field1"));
    }

    @Test
    public void testGetShort() {
        assertThrows(IllegalArgumentException.class, () -> cliObj.getShort("nonExistentField"));

        cliObj.setObject(cliObj.getField("field1"), (short) 1);
        assertEquals((short) 1, cliObj.getShort("field1"));
    }

    @Test
    public void testGetInt() {
        assertThrows(IllegalArgumentException.class, () -> cliObj.getInt("nonExistentField"));

        cliObj.setObject(cliObj.getField("field1"), 1);
        assertEquals(1, cliObj.getInt("field1"));
    }

    @Test
    public void testGetFloat() {
        assertThrows(IllegalArgumentException.class, () -> cliObj.getFloat("nonExistentField"));

        cliObj.setObject(cliObj.getField("field1"), 1.0f);
        assertEquals(1.0f, cliObj.getFloat("field1"));
    }

    @Test
    public void testGetDouble() {
        assertThrows(IllegalArgumentException.class, () -> cliObj.getDouble("nonExistentField"));

        cliObj.setObject(cliObj.getField("field1"), 1.0);
        assertEquals(1.0, cliObj.getDouble("field1"));
    }

    @Test
    public void testGetLong() {
        assertThrows(IllegalArgumentException.class, () -> cliObj.getLong("nonExistentField"));

        cliObj.setObject(cliObj.getField("field1"), 1L);
        assertEquals(1L, cliObj.getLong("field1"));
    }

    @Test
    public void testGetString() {
        assertThrows(IllegalArgumentException.class, () -> cliObj.getString("nonExistentField"));

        cliObj.setObject(cliObj.getField("field1"), "testString");
        assertEquals("testString", cliObj.getString("field1"));
    }

    @Test
    public void testGetBytes() {
        assertThrows(IllegalArgumentException.class, () -> cliObj.getBytes("nonExistentField"));

        byte[] testBytes = new byte[]{(byte) 1, (byte) 2, (byte) 3};
        cliObj.setObject(cliObj.getField("field1"), testBytes);
        assertArrayEquals(testBytes, cliObj.getBytes("field1"));
    }

    @Test
    public void testGetChars() {
        assertThrows(IllegalArgumentException.class, () -> cliObj.getChars("nonExistentField"));

        char[] testChars = new char[]{'A', 'B', 'C'};
        cliObj.setObject(cliObj.getField("field1"), testChars);
        assertArrayEquals(testChars, (char[]) cliObj.getChars("field1"));
    }

    @Test
    public void testGetShorts() {
        assertThrows(IllegalArgumentException.class, () -> cliObj.getShorts("nonExistentField"));

        short[] testShorts = new short[]{(short) 1, (short) 2, (short) 3};
        cliObj.setObject(cliObj.getField("field1"), testShorts);
        assertArrayEquals(testShorts, (short[]) cliObj.getShorts("field1"));
    }

    @Test
    public void testgetInts() {
        assertThrows(IllegalArgumentException.class, () -> cliObj.getInts("nonExistentField"));

        int[] testInts = new int[]{1, 2, 3};
        cliObj.setObject(cliObj.getField("field1"), testInts);
        assertArrayEquals(testInts, (int[]) cliObj.getInts("field1"));
    }

    @Test
    public void testGetFloats() {
        assertThrows(IllegalArgumentException.class, () -> cliObj.getFloats("nonExistentField"));

        float[] testFloats = new float[]{1.0f, 2.0f, 3.0f};
        cliObj.setObject(cliObj.getField("field1"), testFloats);
        assertArrayEquals(testFloats, (float[]) cliObj.getFloats("field1"));
    }

    @Test
    public void testGetDoubles() {
        assertThrows(IllegalArgumentException.class, () -> cliObj.getDoubles("nonExistentField"));

        double[] testDoubles = new double[]{1.0, 2.0, 3.0};
        cliObj.setObject(cliObj.getField("field1"), testDoubles);
        assertArrayEquals(testDoubles, (double[]) cliObj.getDoubles("field1"));
    }

    @Test
    public void testGetLongs() {
        assertThrows(IllegalArgumentException.class, () -> cliObj.getLongs("nonExistentField"));

        long[] testLongs = new long[]{1L, 2L, 3L};
        cliObj.setObject(cliObj.getField("field1"), testLongs);
        assertArrayEquals(testLongs, (long[]) cliObj.getLongs("field1"));
    }

    @Test
    public void testGetStringArray() {
        assertThrows(IllegalArgumentException.class, () -> cliObj.getStringArray("nonExistentField"));

        String[] testStrings = new String[]{"testString1", "testString2"};
        cliObj.setObject(cliObj.getField("field1"), testStrings);
        assertArrayEquals(testStrings, (String[]) cliObj.getStringArray("field1"));
    }
}

class TestClass {
    int field1 = 1;
    boolean flag = false;
}