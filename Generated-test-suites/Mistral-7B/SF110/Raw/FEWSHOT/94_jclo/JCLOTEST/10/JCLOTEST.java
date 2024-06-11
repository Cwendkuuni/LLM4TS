package edu.mscd.cs.jclo;

import static org.junit.Assert.*;
import org.junit.Test;

public class JCLOTest {

    @Test
    public void testJCLOConstruction() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        assertEquals(obj.getClass(), jclo.object.getClass());
        assertNull(jclo.prefix);
        assertNull(jclo.aliases);
        assertEquals(obj.getClass().getDeclaredFields().length, jclo.fields.length);
    }

    @Test
    public void testJCLOConstructionWithPrefix() {
        Object obj = new Object();
        String prefix = "test";
        JCLO jclo = new JCLO(prefix, obj);
        assertEquals(prefix, jclo.prefix);
        assertNull(jclo.aliases);
        assertEquals(obj.getClass().getDeclaredFields().length, jclo.fields.length);
    }

    @Test
    public void testJCLOConstructionWithAliases() {
        Object obj = new Object();
        String[][] aliases = {{ "alias1", "alias2" }, { "alias3", "alias4" }};
        JCLO jclo = new JCLO(null, obj, aliases);
        assertNull(jclo.prefix);
        assertEquals(aliases, jclo.aliases);
        assertEquals(obj.getClass().getDeclaredFields().length, jclo.fields.length);
    }

    @Test
    public void testJCLOConstructionWithPrefixAndAliases() {
        Object obj = new Object();
        String prefix = "test";
        String[][] aliases = {{ "alias1", "alias2" }, { "alias3", "alias4" }};
        JCLO jclo = new JCLO(prefix, obj, aliases);
        assertEquals(prefix, jclo.prefix);
        assertEquals(aliases, jclo.aliases);
        assertEquals(obj.getClass().getDeclaredFields().length, jclo.fields.length);
    }

    @Test
    public void testGetObject() {
        Object obj = new Object();
        Field field = new Field("testField", int.class, new Modifier(0));
        JCLO jclo = new JCLO(null, obj);
        jclo.setObject(field, 1);
        assertEquals(1, jclo.getObject(field));
    }

    @Test
    public void testGetField() {
        Object obj = new Object();
        Field field = new Field("testField", int.class, new Modifier(0));
        JCLO jclo = new JCLO(null, obj);
        assertEquals(field, jclo.getField("testField"));
    }

    @Test
    public void testGetValue() {
        Object obj = new Object();
        Field field = new Field("testField", int.class, new Modifier(0));
        JCLO jclo = new JCLO(null, obj);
        jclo.setObject(field, 1);
        assertEquals(1, jclo.getValue("testField"));
    }

    @Test
    public void testSetObject() {
        Object obj = new Object();
        Field field = new Field("testField", int.class, new Modifier(0));
        JCLO jclo = new JCLO(null, obj);
        jclo.setObject(field, 1);
        assertEquals(1, field.get(obj));
    }

    @Test
    public void testGetArrayType() {
        Class arrayType = int[].class;
        assertEquals("int[]", JCLO.getArrayType(arrayType));
    }

    @Test
    public void testToString() {
        Object obj = new Object();
        Field field1 = new Field("field1", int.class, new Modifier(0));
        Field field2 = new Field("field2", int[].class, new Modifier(0));
        JCLO jclo = new JCLO(null, obj);
        jclo.setObject(field1, 1);
        jclo.setObject(field2, new int[]{1, 2, 3});
        assertEquals("int: field1 = 1\nint[]: field2 = [1, 2, 3]", jclo.toString());
    }

    @Test
    public void testUsage() {
        Object obj = new Object();
        Field field1 = new Field("field1", boolean.class, new Modifier(0));
        Field field2 = new Field("field2", int.class, new Modifier(0));
        JCLO jclo = new JCLO(null, obj);
        assertEquals("--field1\n-field2=\n", jclo.usage());
    }

    @Test
    public void testParseAdditional() {
        Object obj = new Object();
        String[] args = {"--additional", "arg1", "arg2"};
        JCLO jclo = new JCLO(null, obj);
        jclo.parseAdditional(args, 0);
        Object additional = jclo.getObject(jclo.getField("additional"));
        assertEquals(new String[]{"arg1", "arg2"}, (String[])additional);
    }

    @Test
    public void testParse() {
        Object obj = new Object();
        String[] args = {"--boolean", "true", "--intField", "5", "--intArrayField=1,2,3"};
        JCLO jclo = new JCLO(null, obj);
        jclo.parse(args);
        assertEquals(true, jclo.getBoolean("boolean"));
        assertEquals(5, jclo.getInt("intField"));
        Object intArrayField = jclo.getObject(jclo.getField("intArrayField"));
        assertArrayEquals(new int[]{1, 2, 3}, (int[])intArrayField);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidOption() {
        Object obj = new Object();
        String[] args = {"--invalidOption"};
        JCLO jclo = new JCLO(null, obj);
        jclo.parse(args);
    }

    @Test
    public void testParseWithAliases() {
        Object obj = new Object();
        String[][] aliases = {{ "alias1", "alias2" }, { "alias3", "alias4" }};
        String[] args = {"--alias1", "value1", "--alias2", "value2"};
        JCLO jclo = new JCLO(null, obj, aliases);
        jclo.parse(args);
        assertEquals("value1", jclo.getValue("alias1"));
        assertEquals("value2", jclo.getValue("alias2"));
    }

    @Test
    public void testParseWithPrefix() {
        Object obj = new Object();
        String prefix = "test";
        String[] args = {"--testboolean", "true", "--testintField", "5", "--testintArrayField=1,2,3"};
        JCLO jclo = new JCLO(prefix, obj);
        jclo.parse(args);
        assertEquals("true", jclo.getValue("boolean"));
        assertEquals(5, jclo.getInt("intField"));
        Object intArrayField = jclo.getObject(jclo.getField("intArrayField"));
        assertArrayEquals(new int[]{1, 2, 3}, (int[])intArrayField);
        assertEquals("testboolean", jclo.getValue("testboolean").toString());
        assertEquals("testintField", jclo.getValue("testintField").toString());
        assertEquals("testintArrayField", jclo.getValue("testintArrayField").toString());
    }

}