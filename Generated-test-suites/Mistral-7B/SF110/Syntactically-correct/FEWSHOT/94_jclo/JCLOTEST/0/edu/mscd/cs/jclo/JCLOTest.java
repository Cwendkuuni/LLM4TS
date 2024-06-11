package edu.mscd.cs.jclo;

import static org.junit.Assert.*;
import org.junit.Test;

public class JCLOTest {

    @Test
    public void testJCLOInstance() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNotNull(jclo.fields);
        assertNotNull(jclo.object);
        assertEquals(object, jclo.object);
    }

    @Test
    public void testJCLOInstanceWithPrefix() {
        Object object = new Object();
        String prefix = "TEST_";
        JCLO jclo = new JCLO(prefix, object);
        assertNotNull(jclo.prefix);
        assertEquals(prefix, jclo.prefix);
        assertEquals(object, jclo.object);
    }

    @Test
    public void testJCLOInstanceWithPrefixAndAliases() {
        Object object = new Object();
        String prefix = "TEST_";
        String[][] aliases = {{ "alias1", "Alias1" }, { "alias2", "Alias2" }};
        JCLO jclo = new JCLO(prefix, object, aliases);
        assertNotNull(jclo.prefix);
        assertEquals(prefix, jclo.prefix);
        assertNotNull(jclo.aliases);
        assertEquals(2, jclo.aliases.length);
        assertEquals("alias1", jclo.aliases[0][0]);
        assertEquals("Alias1", jclo.aliases[0][1]);
        assertEquals("alias2", jclo.aliases[1][0]);
        assertEquals("Alias2", jclo.aliases[1][1]);
        assertEquals(object, jclo.object);
    }

    @Test
    public void testGetObject() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        Field field = jclo.fields[0];
        Object fieldValue = new Object();
        try {
            jclo.setObject(field, fieldValue);
            Object retrievedValue = jclo.getObject(field);
            assertEquals(fieldValue, retrievedValue);
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException should not be thrown");
        }
    }

    @Test
    public void testGetField() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        String key = "testField";
        Field field = jclo.getField(key);
        assertNull(field);

        jclo = new JCLO("prefix_", object);
        key = "prefix_testField";
        field = jclo.getField(key);
        assertNotNull(field);
        assertEquals(key, field.getName());
    }

    @Test
    public void testGetValue() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);

        Class dummyClass = new Class() {};
        Field dummyField = dummyClass.getDeclaredField("testField");
        dummyField.setAccessible(true);
        dummyField.set(object, "testValue");

        String key = "testField";
        Object value = jclo.getValue(key);
        assertEquals("testValue", value);
    }

    @Test
    public void testUsage() {
        Object object = new Object();
        String[][] aliases = {{ "alias1", "Alias1" }, { "alias2", "Alias2" }};
        JCLO jclo = new JCLO("", object, aliases);
        String usage = jclo.usage();
        assertNotNull(usage);
        assertTrue(usage.isEmpty());

        jclo = new JCLO("prefix_", object, aliases);
        usage = jclo.usage();
        assertNotNull(usage);
        assertTrue(usage.contains("prefix_testField"));
    }

    @Test
    public void testParseAdditional() {
        Object object = new Object();
        String[] args = {"--additional", "arg1", "arg2"};
        JCLO jclo = new JCLO(object);
        jclo.parseAdditional(args, 0);
        Object additional = jclo.getObject(jclo.getField("additional"));
        assertArrayEquals(new String[]{"arg1", "arg2"}, (String[])additional);
    }

    @Test
    public void testAddToArray() {
        Class StringClass = String[].class;
        Object originalArray = Array.newInstance(StringClass, 0);
        Object newElement = "newElement";
        JCLO jclo = new JCLO(null, null);
        Object extendedArray = jclo.addToArray(jclo.getField("someField"), newElement);
        assertArrayEquals(new Object[]{newElement}, extendedArray);

        Object[] original = (Object[])originalArray;
        Object[] extended = (Object[])extendedArray;
        assertEquals(1, extended.length - original.length);
        assertEquals(newElement, extended[extended.length - 1]);
    }

    @Test
    public void testParse() {
        Object object = new Object();
        String[] args = {"--boolean", "true", "--string", "testString", "--int", "123"};
        JCLO jclo = new JCLO("", object);
        jclo.parse(args);

        Object booleanValue = jclo.getValue("boolean");
        assertTrue(booleanValue instanceof Boolean);
        assertEquals(true, ((Boolean)booleanValue).booleanValue());

        Object stringValue = jclo.getValue("string");
        assertTrue(stringValue instanceof String);
        assertEquals("testString", stringValue);

        Object intValue = jclo.getValue("int");
        assertTrue(intValue instanceof Integer);
        assertEquals(123, ((Integer)intValue).intValue());
    }

    @Test
    public void testParseWithAliases() {
        Object object = new Object();
        String prefix = "prefix_";
        String[][] aliases = {{ "alias1", "Alias1" }, { "alias2", "Alias2" }};
        JCLO jclo = new JCLO(prefix, object, aliases);
        String[] args = {"--Alias1", "testValue"};
        jclo.parse(args);
        Object aliasValue = jclo.getValue("alias1");
        assertTrue(aliasValue instanceof String);
        assertEquals("testValue", aliasValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidArgument() {
        Object object = new Object();
        String[] args = {"invalidOption"};
        JCLO jclo = new JCLO("", object);
        jclo.parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithMissingValue() {
        Object object = new Object();
        String[] args = {"--boolean"};
        JCLO jclo = new JCLO("", object);
        jclo.parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidType() {
        Object object = new Object();
        String[] args = {"--invalidType", "testValue"};
        JCLO jclo = new JCLO("", object);
        jclo.parse(args);
    }
}