package edu.mscd.cs.jclo;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.Test;

public class JCLOTest {

    

    @Test
    public void testJCLOConstructorWithObject() {
        JCLO jclo = new JCLO(new Object());
        assertNotNull(jclo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOConstructorWithObjectAndAliasesNull() {
        new JCLO(new Object(), null);
    }

    @Test
    public void testJCLOConstructorWithPrefixAndObject() {
        JCLO jclo = new JCLO("prefix", new Object());
        assertNotNull(jclo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOConstructorWithPrefixAndObjectAndAliasesNull() {
        new JCLO("prefix", new Object(), null);
    }

    @Test
    public void testJCLOConstructorWithPrefixObjectAndAliases() {
        JCLO jclo = new JCLO("prefix", new Object(), new String[][] {{"alias1", "alias2"}});
        assertNotNull(jclo);
    }

    @Test
    public void testGetObject() {
        JCLO jclo = new JCLO(new Object(), new String[][] {{"alias1", "alias2"}});
        Field field = jclo.getClass().getDeclaredFields()[0];
        Object obj = jclo.getObject(field);
        assertNotNull(obj);
    }

    @Test(expected = IllegalAccessException.class)
    public void testGetObjectWithIllegalAccess() {
        JCLO jclo = new JCLO(new Object(), new String[][] {{"alias1", "alias2"}});
        Field field = JCLO.class.getDeclaredField("object");
        jclo.setAccessible(true);
        Object obj = jclo.getObject(field);
    }

    @Test
    public void testGetField() {
        JCLO jclo = new JCLO(new Object(), new String[][] {{"alias1", "alias2"}});
        Field field = jclo.getField("field1");
        assertNotNull(field);
    }

    @Test
    public void testGetValue() {
        JCLO jclo = new JCLO(new Object(), new String[][] {{"alias1", "alias2"}});
        Field field = jclo.getClass().getDeclaredFields()[0];
        Object value = jclo.getValue("field1");
        assertEquals(jclo.getObject(field), value);
    }

    @Test
    public void testSetObject() {
        JCLO jclo = new JCLO(new Object(), new String[][] {{"alias1", "alias2"}});
        Field field = jclo.getClass().getDeclaredFields()[0];
        Object oldValue = jclo.getObject(field);
        Object newValue = new Object();
        jclo.setObject(field, newValue);
        assertNotSame(oldValue, jclo.getObject(field));
        assertSame(newValue, jclo.getObject(field));
    }

    @Test
    public void testGetArrayType() {
        Class type = String[].class;
        assertEquals("String[]", JCLO.getArrayType(type));
    }

    @Test
    public void testToString() {
        JCLO jclo = new JCLO(new Object(), new String[][] {{"alias1", "alias2"}});
        String expected = "String[]: field1 = [Ljava.lang.String;@1b4d1ac\n";
        String actual = jclo.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testUsage() {
        JCLO jclo = new JCLO(new Object(), new String[][] {{"alias1", "alias2"}});
        String expected = "-field1: String[]\n";
        String actual = jclo.usage();
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAdditional() {
        JCLO jclo = new JCLO(new Object(), new String[][] {{"alias1", "alias2"}});
        String[] args = {"--additional", "arg1", "arg2"};
        jclo.parseAdditional(args, 0);
        Object additional = jclo.getObject(jclo.getField("additional"));
        assertArrayEquals(new String[] {"arg1", "arg2"}, (String[]) additional);
    }

    @Test
    public void testAddToArray() {
        JCLO jclo = new JCLO(new Object(), new String[][] {{"alias1", "alias2"}});
        Field field = jclo.getClass().getDeclaredFields()[0];
        Object original = jclo.getObject(field);
        Object newElement = new Object();
        Object result = jclo.addToArray(field, newElement);
        assertArrayEquals(new Object[] {newElement}, (Object[]) result);
        assertArrayEquals(new Object[] {newElement}, (Object[]) jclo.getObject(field));
    }

    @Test
    public void testParse() {
        JCLO jclo = new JCLO(new Object(), new String[][] {{"alias1", "alias2"}});
        String[] args = {"--boolean", "true", "--byte", "1", "--char", "a", "--short", "123", "--int", "456", "--float", "1.23", "--double", "3.14", "--long", "789", "--string", "hello", "--bytes", "abc", "--chars", "ABC", "--shorts", "[1, 2]", "--ints", "[3, 4]", "--floats", "[5.5, 6.6]", "--doubles", "[7.7, 8.8]", "--longs", "[9, 10]", "--strings", "[\"hello\", \"world\"]"};
        jclo.parse(args);
        assertTrue(jclo.getBoolean("boolean"));
        assertEquals((Byte) 1, jclo.getByte("byte"));
        assertEquals((Char) 'a', jclo.getChar("char"));
        assertEquals((Short) 123, jclo.getShort("short"));
        assertEquals((Integer) 456, jclo.getInt("int"));
        assertEquals((Float) 1.23f, jclo.getFloat("float"));
        assertEquals((Double) 3.14, jclo.getDouble("double"));
        assertEquals((Long) 789, jclo.getLong("long"));
        assertEquals("hello", jclo.getString("string"));
        assertArrayEquals(new byte[] {'a', 'b', 'c'}, (byte[]) jclo.getBytes("bytes"));
        assertArrayEquals(new char[] {'A', 'B', 'C'}, (char[]) jclo.getChars("chars"));
        assertArrayEquals(new short[] {1, 2}, (short[]) jclo.getShorts("shorts"));
        assertArrayEquals(new int[] {3, 4}, (int[]) jclo.getInts("ints"));
        assertArrayEquals(new float[] {5.5f, 6.6f}, (float[]) jclo.getFloats("floats"));
        assertArrayEquals(new double[] {7.7, 8.8}, (double[]) jclo.getDoubles("doubles"));
        assertArrayEquals(new long[] {9, 10}, (long[]) jclo.getLongs("longs"));
        assertArrayEquals(new String[] {"hello", "world"}, (String[]) jclo.getStrings("strings"));
    }

    
}