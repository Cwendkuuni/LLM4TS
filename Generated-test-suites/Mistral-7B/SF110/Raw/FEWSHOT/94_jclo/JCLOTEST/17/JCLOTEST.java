package edu.mscd.cs.jclo;

import static org.junit.Assert.*;
import org.junit.Test;

public class JCLOTest {

    @Test
    public void testJCLO() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        Field[] fields = jclo.getFields();
        assertEquals(fields.length, 0);

        String prefix = "prefix_";
        obj = new MyClass(prefix);
        jclo = new JCLO(prefix, obj);
        fields = jclo.getFields();
        assertEquals(fields.length, 3);
        String key1 = "field1";
        String key2 = "field2";
        String key3 = "field3";
        Class<?> type1 = Integer.TYPE;
        Class<?> type2 = String.class;
        Class<?> type3 = Boolean.TYPE;
        Object value1 = 1;
        Object value2 = "test";
        Object value3 = true;
        Field field1 = jclo.getField(key1);
        Field field2 = jclo.getField(key2);
        Field field3 = jclo.getField(key3);
        assertNotNull(field1);
        assertNotNull(field2);
        assertNotNull(field3);
        assertEquals(field1.getType(), type1);
        assertEquals(field2.getType(), type2);
        assertEquals(field3.getType(), type3);
        assertEquals(jclo.getValue(key1), value1);
        assertEquals(jclo.getValue(key2), value2);
        assertEquals(jclo.getValue(key3), value3);
    }

    @Test
    public void testUsage() {
        Object obj = new MyClass();
        JCLO jclo = new JCLO(obj);
        String usage = jclo.usage();
        String expectedUsage = "--field1 <int>\n" +
                "--field2 <string>\n" +
                "--field3 <boolean>\n";
        assertEquals(usage, expectedUsage);
    }

    @Test
    public void testParse() {
        String[] args = {"--field1", "10", "--field2", "test", "--field3", "true"};
        Object obj = new MyClass();
        JCLO jclo = new JCLO(obj);
        jclo.parse(args);
        String key1 = "field1";
        String key2 = "field2";
        String key3 = "field3";
        Field field1 = jclo.getField(key1);
        Field field2 = jclo.getField(key2);
        Field field3 = jclo.getField(key3);
        Object value1 = 10;
        Object value2 = "test";
        Object value3 = true;
        assertEquals(jclo.getValue(key1), value1);
        assertEquals(jclo.getValue(key2), value2);
        assertEquals(jclo.getValue(key3), value3);
    }

    @Test
    public void testParseWithAliases() {
        String[] args = {"--f1", "10", "--option2", "test", "--flag", "true"};
        Object obj = new MyClass();
        String[][] aliases = {{"f1", "field1"}, {"option2", "field2"}, {"flag", "field3"}};
        JCLO jclo = new JCLO(obj, aliases);
        jclo.parse(args);
        String key1 = "field1";
        String key2 = "field2";
        String key3 = "field3";
        Field field1 = jclo.getField(key1);
        Field field2 = jclo.getField(key2);
        Field field3 = jclo.getField(key3);
        Object value1 = 10;
        Object value2 = "test";
        Object value3 = true;
        assertEquals(jclo.getValue(key1), value1);
        assertEquals(jclo.getValue(key2), value2);
        assertEquals(jclo.getValue(key3), value3);
    }

    @Test
    public void testParseWithAdditional() {
        String[] args = {"--field1", "10", "--field2", "test", "--additional", "value1", "value2"};
        Object obj = new MyClass();
        JCLO jclo = new JCLO(obj);
        jclo.parse(args);
        String keyAdditional = "additional";
        Field fieldAdditional = jclo.getField(keyAdditional);
        Object[] additionalValues = (Object[]) jclo.getValue(keyAdditional);
        String expectedValue1 = "value1";
        String expectedValue2 = "value2";
        assertEquals(additionalValues.length, 2);
        assertEquals(additionalValues[0], expectedValue1);
        assertEquals(additionalValues[1], expectedValue2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidOption() {
        String[] args = {"--invalid", "value"};
        Object obj = new MyClass();
        JCLO jclo = new JCLO(obj);
        jclo.parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithMissingValue() {
        String[] args = {"--field1"};
        Object obj = new MyClass();
        JCLO jclo = new JCLO(obj);
        jclo.parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithMissingEquals() {
        String[] args = {"--field1=value1"};
        Object obj = new MyClass();
        JCLO jclo = new JCLO(obj, new String[][] {{"field1", "f"}});
        jclo.parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithMissingDoubleDashes() {
        String[] args = {"--field1 value1"};
        Object obj = new MyClass();
        JCLO jclo = new JCLO(obj, new String[][] {{"field1", "f"}});
        jclo.parse(args);
    }

    private static class MyClass {
        int field1;
        String field2;
        boolean field3;
    }
}