package edu.mscd.cs.jclo;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class JCLOTest {

    private Object testObject;

    @Before
    public void setup() {
        testObject = new Object() {
            int i = 1;
            String s = "test";
            double d = 1.2;
            boolean b = true;
            char c = 'a';
            byte[] bytes = {1, 2, 3};
            short[] shorts = {1, 2, 3};
            int[] ints = {1, 2, 3};
            float[] floats = {1.2f, 2.3f, 3.4f};
            double[] doubles = {1.2, 2.3, 3.4};
            long[] longs = {1, 2, 3};
            String[] strings = {"test1", "test2", "test3"};
        };
    }

    @Test
    public void testNewInstance() {
        new JCLO(testObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithInvalidPrefix() {
        new JCLO("invalidPrefix", testObject);
    }

    @Test(expected = NullPointerException.class)
    public void testGetObjectNullField() {
        JCLO jclo = new JCLO("prefix", testObject);
        Field nullField = null;
        jclo.getObject(nullField);
    }

    @Test
    public void testGetField() {
        JCLO jclo = new JCLO("prefix", testObject);
        Field fieldI = jclo.getField("i");
        Field fieldS = jclo.getField("s");
        Field fieldD = jclo.getField("d");
        Field fieldB = jclo.getField("b");
        Field fieldC = jclo.getField("c");
        Field fieldBytes = jclo.getField("bytes");
        Field fieldShorts = jclo.getField("shorts");
        Field fieldInts = jclo.getField("ints");
        Field fieldFloats = jclo.getField("floats");
        Field fieldDoubles = jclo.getField("doubles");
        Field fieldLongs = jclo.getField("longs");
        Field fieldStrings = jclo.getField("strings");

        assertSame(testObject.getClass().getDeclaredField("i"), fieldI);
        assertSame(testObject.getClass().getDeclaredField("s"), fieldS);
        assertSame(testObject.getClass().getDeclaredField("d"), fieldD);
        assertSame(testObject.getClass().getDeclaredField("b"), fieldB);
        assertSame(testObject.getClass().getDeclaredField("c"), fieldC);
        assertSame(testObject.getClass().getDeclaredField("bytes"), fieldBytes);
        assertSame(testObject.getClass().getDeclaredField("shorts"), fieldShorts);
        assertSame(testObject.getClass().getDeclaredField("ints"), fieldInts);
        assertSame(testObject.getClass().getDeclaredField("floats"), fieldFloats);
        assertSame(testObject.getClass().getDeclaredField("doubles"), fieldDoubles);
        assertSame(testObject.getClass().getDeclaredField("longs"), fieldLongs);
        assertSame(testObject.getClass().getDeclaredField("strings"), fieldStrings);
    }

    @Test
    public void testGetValue() {
        JCLO jclo = new JCLO("prefix", testObject);
        Object valueI = jclo.getValue("i");
        Object valueS = jclo.getValue("s");
        Object valueD = jclo.getValue("d");
        Object valueB = jclo.getValue("b");
        Object valueC = jclo.getValue("c");
        Object valueBytes = jclo.getValue("bytes");
        Object valueShorts = jclo.getValue("shorts");
        Object valueInts = jclo.getValue("ints");
        Object valueFloats = jclo.getValue("floats");
        Object valueDoubles = jclo.getValue("doubles");
        Object valueLongs = jclo.getValue("longs");
        Object valueStrings = jclo.getValue("strings");

        assertEquals(1, valueI);
        assertEquals("test", valueS);
        assertEquals(1.2, valueD, 0.001);
        assertEquals(true, valueB);
        assertEquals('a', valueC);
        assertArrayEquals(new byte[] {1, 2, 3}, valueBytes);
        assertArrayEquals(new short[] {1, 2, 3}, valueShorts);
        assertArrayEquals(new int[] {1, 2, 3}, valueInts);
        assertArrayEquals(new float[] {1.2f, 2.3f, 3.4f}, valueFloats, 0.001f);
        assertArrayEquals(new double[] {1.2, 2.3, 3.4}, valueDoubles, 0.001);
        assertArrayEquals(new long[] {1, 2, 3}, valueLongs);
        assertArrayEquals(new String[] {"test1", "test2", "test3"}, valueStrings);
    }

    @Test
    public void testSetObject() {
        JCLO jclo = new JCLO("prefix", testObject);
        Object newValueI = new Integer(42);
        jclo.setObject(jclo.getField("i"), newValueI);
        assertEquals(42, jclo.getValue("i"));
    }

    @Test
    public void testToString() {
        JCLO jclo = new JCLO("prefix", testObject);
        String expected = "byte[]: i = [1, 2, 3]\n"
                + "char: c = a\n"
                + "double: d = 1.2\n"
                + "float: ints = [1, 2, 3]\n"
                + "int: i = 1\n"
                + "java.lang.String: s = test\n"
                + "short: shorts = [1, 2, 3]\n"
                + "boolean: b = true\n";
        assertEquals(expected, jclo.toString());
    }

    @Test
    public void testUsage() {
        JCLO jclo = new JCLO("prefix", testObject);
        String expected = "--i = int\n"
                + "--s = java.lang.String\n"
                + "--d = double\n"
                + "--b = boolean\n"
                + "--c = char\n"
                + "--bytes = byte[]...\n"
                + "--shorts = short[]...\n"
                + "--ints = int[]...\n"
                + "--floats = float[]...\n"
                + "--doubles = double[]...\n"
                + "--longs = long[]...\n"
                + "--strings = java.lang.String[]...\n";
        assertEquals(expected, jclo.usage());
    }

    @Test
    public void testParse() {
        JCLO jclo = new JCLO("prefix", testObject);
        String[] args = {"--i", "2", "--s", "test2", "--d", "1.5", "--b", "false", "--c", "b", "--bytes", "1,2,3",
                "--shorts", "1,2,3", "--ints", "1,2,3", "--floats", "1.2,2.3,3.4", "--doubles", "1.2,2.3,3.4",
                "--longs", "1,2,3", "--strings", "test1,test2,test3"};
        jclo.parse(args);
        Object valueI = jclo.getValue("i");
        assertEquals(2, valueI);
        assertEquals("test2", jclo.getValue("s"));
        assertEquals(1.5, jclo.getValue("d"), 0.001);
        assertEquals(false, jclo.getValue("b"));
        assertEquals('b', jclo.getValue("c"));
        assertArrayEquals(new byte[] {1, 2, 3}, jclo.getValue("bytes"));
        assertArrayEquals(new short[] {1, 2, 3}, jclo.getValue("shorts"));
        assertArrayEquals(new int[] {1, 2, 3}, jclo.getValue("ints"));
        assertArrayEquals(new float[] {1.2f, 2.3f, 3.4f}, jclo.getValue("floats"), 0.001f);
        assertArrayEquals(new double[] {1.2, 2.3, 3.4}, jclo.getValue("doubles"), 0.001);
        assertArrayEquals(new long[] {1, 2, 3}, jclo.getValue("longs"));
        assertArrayEquals(new String[] {"test1", "test2", "test3"}, jclo.getValue("strings"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidOption() {
        JCLO jclo = new JCLO("prefix", testObject);
        String[] args = {"--invalidOption"};
        jclo.parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingValue() {
        JCLO jclo = new JCLO("prefix", testObject);
        String[] args = {"--i"};
        jclo.parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidValueType() {
        JCLO jclo = new JCLO("prefix", testObject);
        String[] args = {"--i", "abc"};
        jclo.parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingValueType() {
        JCLO jclo = new JCLO("prefix", testObject);
        String[] args = {"--i", "1,2,3"};
        jclo.parse(args);
    }

    @Test
    public void testParseAdditional() {
        JCLO jclo = new JCLO("prefix", testObject);
        String[] args = {"--i", "2", "--additional", "elem1", "elem2", "elem3"};
        jclo.parse(args);
        Object valueAdditional = jclo.getValue("additional");
        assertArrayEquals(new String[] {"elem1", "elem2", "elem3"}, valueAdditional);
    }

}