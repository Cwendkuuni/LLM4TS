package edu.mscd.cs.jclo;

import static org.junit.Assert.*;
import org.junit.Test;

public class JCLOTest {

    @Test
    public void testJCLO() {
        JCLO jclo = new JCLO(new Object());
        assertNotNull(jclo);

        JCLO jcloWithPrefix = new JCLO("prefix", new Object());
        assertNotNull(jcloWithPrefix);
        assertEquals("prefix", jcloWithPrefix.getPrefix());

        JCLO jcloWithAliases = new JCLO(new Object(), new String[][]{{"alias1", "alias2"}});
        assertNotNull(jcloWithAliases);
        assertEquals(new String[][]{{"alias1", "alias2"}}, jcloWithAliases.getAliases());

        JCLO jcloObject = new JCLO(new Object());
        assertNull(jcloObject.getPrefix());
        assertArrayEquals(new Field[0], jcloObject.getFields());

        JCLO jcloWithFields = new JCLO("prefix", new Object(), new Field[3]);
        assertNotNull(jcloWithFields);
        assertEquals("prefix", jcloWithFields.getPrefix());
        assertArrayEquals(new Field[3], jcloWithFields.getFields());

        JCLO jcloWithGetObject = new JCLO("prefix", new Object(), new Field[3]);
        Field field1 = new Field("field1", int.class);
        Field field2 = new Field("field2", String.class);
        Field field3 = new Field("field3", boolean.class);
        jcloWithGetObject.setFields(new Field[]{field1, field2, field3});
        assertEquals(field1, jcloWithGetObject.getField("field1"));
        assertEquals(field2, jcloWithGetObject.getField("field2"));
        assertEquals(field3, jcloWithGetObject.getField("field3"));
        assertEquals(0, jcloWithGetObject.getFields().length);

        JCLO jcloWithGetString = new JCLO("prefix", new Object(), new Field[3]);
        jcloWithGetString.setFields(new Field[]{field1, field2, field3});
        assertEquals("int", jcloWithGetString.getUsageType(field1.getType()));
        assertEquals("String", jcloWithGetString.getUsageType(field2.getType()));
        assertEquals("boolean", jcloWithGetString.getUsageType(field3.getType()));
    }

    @Test
    public void testGetValue() {
        JCLO jclo = new JCLO("prefix", new Object(), new Field[3]);
        jclo.setFields(new Field[]{new Field("field1", int.class), new Field("field2", String.class), new Field("field3", boolean.class)});

        int valueInt = 42;
        jclo.setObject(jclo.getField("field1"), valueInt);
        assertEquals(valueInt, jclo.getValue("field1"));

        String valueString = "test";
        jclo.setObject(jclo.getField("field2"), valueString);
        assertEquals(valueString, jclo.getValue("field2"));

        boolean valueBoolean = true;
        jclo.setObject(jclo.getField("field3"), valueBoolean);
        assertEquals(valueBoolean, jclo.getValue("field3"));

        jclo.setObject(jclo.getField("field1"), null);
        assertNull(jclo.getValue("field1"));
    }

    @Test
    public void testSetObject() {
        JCLO jclo = new JCLO("prefix", new Object(), new Field[3]);
        jclo.setFields(new Field[]{new Field("field1", int[].class), new Field("field2", String[].class), new Field("field3", boolean[].class)});

        int[] intArray = {1, 2, 3};
        jclo.setObject(jclo.getField("field1"), intArray);
        assertArrayEquals(intArray, (int[])(jclo.getValue("field1")));

        String[] stringArray = {"test1", "test2", "test3"};
        jclo.setObject(jclo.getField("field2"), stringArray);
        assertArrayEquals(stringArray, (String[])(jclo.getValue("field2")));

        boolean[] boolArray = {true, false, true};
        jclo.setObject(jclo.getField("field3"), boolArray);
        assertArrayEquals(boolArray, (boolean[])(jclo.getValue("field3")));

        jclo.setObject(jclo.getField("field1"), null);
        assertArrayEquals(new int[0], (int[])(jclo.getValue("field1")));
    }

    @Test
    public void testParse() {
        JCLO jclo = new JCLO("prefix", new Object(), new Field[3]);
        jclo.setFields(new Field[]{new Field("field1", int.class), new Field("field2", String.class), new Field("field3", boolean.class)});

        String[] args = {"--field1", "1", "--field2", "test"};
        jclo.parse(args);
        assertEquals(1, ((int[])jclo.getValue("field1"))[0]);
        assertEquals("test", (String)jclo.getValue("field2"));
        assertEquals(true, (boolean)jclo.getValue("field3"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidOption() {
        JCLO jclo = new JCLO("prefix", new Object(), new Field[3]);
        jclo.setFields(new Field[]{new Field("field1", int.class), new Field("field2", String.class), new Field("field3", boolean.class)});

        String[] args = {"--invalidoption"};
        jclo.parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingValue() {
        JCLO jclo = new JCLO("prefix", new Object(), new Field[3]);
        jclo.setFields(new Field[]{new Field("field1", int.class), new Field("field2", String.class), new Field("field3", boolean.class)});

        String[] args = {"--field1"};
        jclo.parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidValueType() {
        JCLO jclo = new JCLO("prefix", new Object(), new Field[3]);
        jclo.setFields(new Field[]{new Field("field1", int.class), new Field("field2", String.class), new Field("field3", boolean.class)});

        String[] args = {"--field1=invalidValue"};
        jclo.parse(args);
    }

    @Test
    public void testUsage() {
        JCLO jclo = new JCLO("prefix", new Object(), new Field[3]);
        jclo.setFields(new Field[]{new Field("field1", int.class), new Field("field2", String.class), new Field("field3", boolean.class)});

        String usage = jclo.usage();
        String prefixUsage = "--prefix--field1=";
        String intUsage = " int";
        String stringUsage = " String";
        String booleanUsage = " boolean";

        assertTrue(usage.contains(prefixUsage));
        assertTrue(usage.contains(intUsage));
        assertTrue(usage.contains(stringUsage));
        assertTrue(usage.contains(booleanUsage));
    }
}