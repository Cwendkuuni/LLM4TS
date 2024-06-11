package edu.mscd.cs.jclo;

import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

public class JCLOTest {

    @Test
    public void testJCLO() {
        // Test default constructor
        JCLO jclo = new JCLO(new Object());
        assertNotNull(jclo);

        // Test constructor with object
        JCLO jcloWithObject = new JCLO(new Object());
        assertEquals(jcloWithObject.getClass(), JCLO.class);
        assertEquals(jcloWithObject.object, new Object());
        assertEquals(jcloWithObject.prefix, "");
        assertEquals(jcloWithObject.fields.length, 0);

        // Test constructor with object and aliases
        String[][] aliases = new String[][]{{"a", "aliasA"}, {"b", "aliasB"}};
        JCLO jcloWithObjectAndAliases = new JCLO(new Object(), aliases);
        assertEquals(jcloWithObjectAndAliases.getClass(), JCLO.class);
        assertEquals(jcloWithObjectAndAliases.object, new Object());
        assertEquals(jcloWithObjectAndAliases.prefix, "");
        assertEquals(jcloWithObjectAndAliases.aliases, aliases);
        assertEquals(jcloWithObjectAndAliases.fields.length, 0);

        // Test constructor with prefix and object
        String prefix = "testPrefix_";
        JCLO jcloWithPrefix = new JCLO(prefix, new Object());
        assertEquals(jcloWithPrefix.getClass(), JCLO.class);
        assertEquals(jcloWithPrefix.prefix, prefix);
        assertEquals(jcloWithPrefix.object, new Object());
        assertEquals(jcloWithPrefix.fields.length, 0);

        // Test constructor with prefix, object and aliases
        JCLO jcloWithPrefixAndAliases = new JCLO(prefix, new Object(), aliases);
        assertEquals(jcloWithPrefixAndAliases.getClass(), JCLO.class);
        assertEquals(jcloWithPrefixAndAliases.prefix, prefix);
        assertEquals(jcloWithPrefixAndAliases.object, new Object());
        assertEquals(jcloWithPrefixAndAliases.aliases, aliases);
        assertEquals(jcloWithPrefixAndAliases.fields.length, 0);

        // Test getValue method
        Field testField = new Field() {{
            setAccessible(true);
            setName("testField");
            setType(int.class);
            set(new Object(), 42);
        }};
        JCLO jcloWithTestField = new JCLO(prefix, new Object(), aliases) {
            {
                setField(testField, new Object());
            }
        };
        assertEquals(jcloWithPrefixAndAliases.getClass(), JCLO.class);
        assertEquals(jcloWithPrefixAndAliases.prefix, prefix);
        assertEquals(jcloWithPrefixAndAliases.object, new Object());
        assertEquals(jcloWithPrefixAndAliases.aliases, aliases);
        assertEquals(jcloWithPrefixAndAliases.fields.length, 1);
        assertEquals(jcloWithPrefixAndAliases.getValue("testField"), 42);

        // Test setValue method
        int testValue = 7;
        jcloWithPrefixAndAliases.setValue("testField", testValue);
        assertEquals(jcloWithPrefixAndAliases.getValue("testField"), testValue);

        // Test getObject method
        assertEquals(jcloWithPrefixAndAliases.getObject(testField), testValue);

        // Test toString method
        String expectedToString = "int: testField = 7";
        String toString = jcloWithPrefixAndAliases.toString();
        assertEquals(toString, expectedToString);
    }

    @Test
    public void testGetUsageType() {
        Class booleanType = boolean.class;
        Class intArrayType = int[].class;
        Class stringType = String.class;

        assertEquals(JCLO.getUsageType(booleanType), "[=boolean]");
        assertEquals(JCLO.getUsageType(intArrayType), "int...\n");
        assertEquals(JCLO.getUsageType(stringType), "string\n");
        assertEquals(JCLO.getUsageType(null), "");
    }

    @Test
    public void testUsage() {
        Field[] fields = new Field[]{{}, {}, {}, {}};
        Object object = new Object();
        JCLO jclo = new JCLO(fields, object);

        String[] expectedUsage = new String[]{"int: a = ",
                "int: b = ",
                "int: c = ",
                "int: d = "};
        String usage = jclo.usage();

        assertTrue(Arrays.deepEquals(expectedUsage, jclo.usage().split("\n")));
    }

    @Test
    public void testParseAdditional() {
        String[] args = new String[]{"--additional", "arg1", "arg2"};
        JCLO jclo = new JCLO(new Object(), new String[][]{{}, {}});
        jclo.parseAdditional(args, 0);

        Object additional = jclo.getValue("additional");
        assertArrayEquals(new Object[]{"arg1", "arg2"}, additional);
    }

    @Test
    public void testParseAndGetValues() {
        String[] args = new String[]{"-a", "1", "-b", "2", "--c", "3", "--d=4", "--e"};
        String prefix = "";
        JCLO jclo = new JCLO(prefix, new Object(), new String[][]{{"a", "aAlias"}, {"b", "bAlias"}, {"c", "cAlias"}, {"d", "dAlias"}});
        jclo.parse(args);

        assertEquals(jclo.getByte("a"), (byte) 1);
        assertEquals(jclo.getInt("b"), 2);
        assertEquals(jclo.getDouble("c"), 3.0);
        assertEquals(jclo.getString("d"), "4");
        assertNull(jclo.getValue("e"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidOption() {
        String[] args = new String[]{"--invalid"};
        new JCLO(new Object(), new String[][]{{}, {}}).parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingArgument() {
        String[] args = new String[]{"-a"};
        new JCLO(new Object(), new String[][]{{"a", "aAlias"}, {}}).parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingEquals() {
        String[] args = new String[]{"--a=", "1"};
        new JCLO(new Object(), new String[][]{{"a", "aAlias"}}).parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingHyphen() {
        String[] args = new String[]{"a="};
        new JCLO(new Object(), new String[][]{{"a", "aAlias"}}).parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingDoubleHyphen() {
        String[] args = new String[]{"--a", "="};
        new JCLO(new Object(), new String[][]{{"a", "aAlias"}}).parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingPrefix() {
        String[] args = new String[]{"--a", "="};
        new JCLO(new Object(), new String[][]{{"a", "aAlias"}}).parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingAlias() {
        String[] args = new String[]{"--a", "foo"};
        new JCLO(new Object(), new String[][]{{}, {}}).parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseUnsupportedType() {
        String[] args = new String[]{"--a", "boolean"};
        new JCLO(new Object(), new String[][]{{}, {}}).parse(args);
    }
}