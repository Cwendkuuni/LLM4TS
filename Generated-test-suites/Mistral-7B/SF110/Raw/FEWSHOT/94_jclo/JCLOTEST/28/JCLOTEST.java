package edu.mscd.cs.jclo;

import static org.junit.Assert.*;
import org.junit.Test;

public class JCLOTest {

    @Test
    public void testJCLOWithNoArguments() {
        JCLO jclo = new JCLO(new Object());
        assertEquals("", jclo.usage());
    }

    @Test
    public void testJCLOWithStringPrefix() {
        JCLO jclo = new JCLO("prefix_", new Object());
        String[][] aliases = new String[][]{{"p", "prefix"}, {"a", "additional"}};
        jclo = new JCLO(jclo.prefix, new Object(), aliases);
        String usage = jclo.usage();
        String expected = "--prefix=VALUE:\n" +
                "--prefix_additional:\n" +
                "--prefix_additional...:\n";
        assertEquals(expected, usage);
    }

    @Test
    public void testJCLOWithObject() {
        JCLO jclo = new JCLO(new Object());
        Field[] fields = jclo.fields;
        assertEquals(0, fields.length);
    }

    @Test
    public void testJCLOWithFieldArray() {
        JCLO jclo = new JCLO(new Object(), new String[][]{{"a", int[].class}, {"b", String[].class}});
        Field[] fields = jclo.fields;
        assertEquals(2, fields.length);
        Field aField = fields[0];
        assertEquals("a", aField.getName());
        assertEquals(int[].class, aField.getType());
        Field bField = fields[1];
        assertEquals("b", bField.getName());
        assertEquals(String[].class, bField.getType());
    }

    @Test
    public void testJCLOGetValue() {
        Object obj = new Object();
        Field field = new Field("field", int.class, new int[0]);
        JCLO jclo = new JCLO(obj);
        jclo.setObject(field, new int[]{1, 2, 3});
        Object value = jclo.getValue("field");
        assertArrayEquals(new int[]{1, 2, 3}, (int[])value);
    }

    @Test
    public void testJCLOGetValueWithNullField() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        assertNull(jclo.getValue("nonExistentField"));
    }

    @Test
    public void testJCLOParse() {
        String[] args = {"-a", "1", "--additional=value1,value2"};
        JCLO jclo = new JCLO(new Object());
        jclo.parse(args);
        Field aField = jclo.getField("a");
        assertNotNull(aField);
        assertEquals(1, ((int[])jclo.getObject(aField)).length);
        assertEquals(1, ((int[])jclo.getObject(aField))[0]);
        Field additionalField = jclo.getField("additional");
        assertNotNull(additionalField);
        Object additionalObj = jclo.getObject(additionalField);
        assertArrayEquals(new String[]{"value1", "value2"}, (String[])additionalObj);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOParseWithInvalidArgument() {
        String[] args = {"-a=", "--invalidField=value"};
        JCLO jclo = new JCLO(new Object());
        jclo.parse(args);
    }

    @Test
    public void testJCLOParseWithBooleanArgument() {
        String[] args = {"--boolean=true"};
        JCLO jclo = new JCLO(new Object());
        jclo.parse(args);
        assertTrue((boolean)jclo.getValue("boolean"));
    }

    @Test
    public void testJCLOParseWithArrayArgument() {
        String[] args = {"-a", "1", "--a=2,3,4"};
        JCLO jclo = new JCLO(new Object(), new String[][]{{"a", int[].class}});
        jclo.parse(args);
        Object aObj = jclo.getObject(jclo.getField("a"));
        assertArrayEquals(new int[]{1, 2, 3, 4}, (int[])aObj);
    }

    @Test
    public void testJCLOParseWithMultipleFields() {
        String[] args = {"--int=1", "--string=hello", "--boolean=true", "--boolean2=false"};
        JCLO jclo = new JCLO(new Object(), new String[][]{{"int", int.class}, {"string", String.class}, {"boolean", boolean.class}, {"boolean2", boolean.class}});
        jclo.parse(args);
        assertEquals(1, (int)jclo.getValue("int"));
        assertEquals("hello", jclo.getValue("string"));
        assertTrue((boolean)jclo.getValue("boolean"));
        assertFalse((boolean)jclo.getValue("boolean2"));
    }

    @Test
    public void testJCLOParseWithPrefix() {
        String[] args = {"--prefix_int=1", "--prefix_string=hello", "--prefix_boolean=true", "--prefix_boolean2=false"};
        String prefix = "prefix_";
        JCLO jclo = new JCLO(prefix, new Object(), new String[][]{{"int", int.class}, {"string", String.class}, {"boolean", boolean.class}, {"boolean2", boolean.class}});
        jclo.parse(args);
        assertEquals(1, (int)jclo.getValue("int"));
        assertEquals("hello", jclo.getValue("string"));
        assertTrue((boolean)jclo.getValue("boolean"));
        assertFalse((boolean)jclo.getValue("boolean2"));
    }

    @Test
    public void testJCLOParseWithAliases() {
        String[] args = {"--int=1", "--i=1", "--int2=2", "--i2=2"};
        String[][] aliases = new String[][]{{"i", "int"}, {"i2", "int2"}};
        JCLO jclo = new JCLO(new Object(), null, aliases);
        jclo.parse(args);
        assertEquals(1, (int)jclo.getValue("int"));
        assertEquals(1, (int)jclo.getValue("i"));
        assertEquals(2, (int)jclo.getValue("int2"));
        assertEquals(2, (int)jclo.getValue("i2"));
    }

    @Test
    public void testJCLOParseWithMixedArgs() {
        String[] args = {"--prefix_int=1", "--prefix_string=hello", "-a", "1", "--prefix_boolean=true", "--prefix_boolean2=false"};
        String prefix = "prefix_";
        JCLO jclo = new JCLO(prefix, new Object(), new String[][]{{"int", int.class}, {"string", String.class}, {"boolean", boolean.class}, {"boolean2", boolean.class}, {"a", int[].class}});
        jclo.parse(args);
        assertEquals(1, (int)jclo.getValue("int"));
        assertEquals("hello", jclo.getValue("string"));
        assertTrue((boolean)jclo.getValue("boolean"));
        assertFalse((boolean)jclo.getValue("boolean2"));
        Object aObj = jclo.getObject(jclo.getField("a"));
        assertArrayEquals(new int[]{1}, (int[])aObj);
    }

    @Test
    public void testJCLOToString() {
        Object obj = new Object() {
            int a;
            String b;
            boolean c;
            int[] d;
        };
        JCLO jclo = new JCLO(obj);
        String expected = "int: a = 0\n" +
                "String: b = null\n" +
                "boolean: c = false\n" +
                "int[]: d = []\n";
        assertEquals(expected, jclo.toString());
    }
}