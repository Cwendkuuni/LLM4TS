package edu.mscd.cs.jclo;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JCLOTest {

    private JCLO jclo;
    private static final String TEST_OBJECT = "testObject";
    private static final String TEST_FIELD_1 = "testField1";
    private static final String TEST_FIELD_2 = "testField2";
    private static final String TEST_PREFIX = "testPrefix_";
    private static final String TEST_ALIAS = new String[][] { { "alias1", "actualName1" }, { "alias2", "actualName2" } };

    @Before
    public void setUp() {
        try {
            Class<?> testClass = Class.forName("edu.mscd.cs.jclo.JCLO");
            Constructor<?> constructor = testClass.getConstructor(Object.class);
            jclo = (JCLO) constructor.newInstance(TEST_OBJECT);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @After
    public void tearDown() {
        jclo = null;
    }

    @Test
    public void testJCLOConstructorWithObject() {
        assertEquals("testObject", jclo.getObject());
    }

    @Test
    public void testJCLOConstructorWithObjectAndAliases() {
        JCLO jcloWithAliases = new JCLO(TEST_OBJECT, TEST_ALIAS);
        assertEquals(TEST_ALIAS, jcloWithAliases.getAliases());
    }

    @Test
    public void testJCLOConstructorWithPrefixAndObject() {
        JCLO jcloWithPrefix = new JCLO(TEST_PREFIX, TEST_OBJECT);
        assertEquals(TEST_PREFIX, jcloWithPrefix.getPrefix());
    }

    @Test
    public void testJCLOConstructorWithPrefixObjectAndAliases() {
        JCLO jcloWithPrefixAndAliases = new JCLO(TEST_PREFIX, TEST_OBJECT, TEST_ALIAS);
        assertEquals(TEST_PREFIX, jcloWithPrefixAndAliases.getPrefix());
        assertEquals(TEST_ALIAS, jcloWithPrefixAndAliases.getAliases());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOParseWithInvalidArgument() {
        jclo.parse(new String[] { "--invalidOption" });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOParseWithMissingValue() {
        jclo.parse(new String[] { "--boolean=true" });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOParseWithInvalidBooleanValue() {
        jclo.parse(new String[] { "--boolean=invalid" });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOParseWithMissingEqualsValue() {
        jclo.parse(new String[] { "--boolean=true--" });
    }

    @Test
    public void testJCLOParseWithBooleanValue() {
        jclo.parse(new String[] { "--boolean=true" });
        assertTrue(jclo.getBoolean("boolean"));
    }

    @Test
    public void testJCLOParseWithByteValue() {
        jclo.parse(new String[] { "--byte=127" });
        assertEquals((byte) 127, jclo.getByte("byte"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOParseWithInvalidCharValue() {
        jclo.parse(new String[] { "--char=invalid" });
    }

    @Test
    public void testJCLOParseWithCharValue() {
        jclo.parse(new String[] { "--char=a" });
        assertEquals((char) 'a', jclo.getChar("char"));
    }

    @Test
    public void testJCLOParseWithShortValue() {
        jclo.parse(new String[] { "--short=32767" });
        assertEquals((short) 32767, jclo.getShort("short"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOParseWithInvalidIntValue() {
        jclo.parse(new String[] { "--int=-2147483649" });
    }

    @Test
    public void testJCLOParseWithIntValue() {
        jclo.parse(new String[] { "--int=2147483647" });
        assertEquals(2147483647, jclo.getInt("int"));
    }

    @Test
    public void testJCLOParseWithFloatValue() {
        jclo.parse(new String[] { "--float=3.14159265" });
        assertEquals(3.14159265f, jclo.getFloat("float"), 0.001f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOParseWithInvalidDoubleValue() {
        jclo.parse(new String[] { "--double=invalid" });
    }

    @Test
    public void testJCLOParseWithDoubleValue() {
        jclo.parse(new String[] { "--double=3.14159265" });
        assertEquals(3.14159265d, jclo.getDouble("double"), 0.001d);
    }

    @Test
    public void testJCLOParseWithLongValue() {
        jclo.parse(new String[] { "--long=9223372036854775807" });
        assertEquals(9223372036854775807L, jclo.getLong("long"));
    }

    @Test
    public void testJCLOParseWithStringValue() {
        jclo.parse(new String[] { "--string=Test String" });
        assertEquals("Test String", jclo.getString("string"));
    }

    @Test
    public void testJCLOParseWithArrayValue() {
        jclo.parse(new String[] { "--ints=1,2,3" });
        int[] ints = (int[]) jclo.getInts("ints");
        assertArrayEquals(new int[] { 1, 2, 3 }, ints);
    }

    @Test
    public void testJCLOParseWithArrayValueWithPrefix() {
        JCLO jcloWithPrefix = new JCLO(TEST_PREFIX, TEST_OBJECT);
        jcloWithPrefix.parse(new String[] { "--ints=1,2,3" });
        int[] ints = (int[]) jcloWithPrefix.getInts("ints");
        assertArrayEquals(new int[] { 1, 2, 3 }, ints);
    }

    @Test
    public void testJCLOUsage() {
        assertEquals("", jclo.usage());
        jclo.parse(new String[] { "--boolean=true" });
        assertEquals("-boolean--\n--boolean=true\n", jclo.usage());

        jclo.parse(new String[] { "--boolean=true", "--byte=127" });
        assertEquals("-boolean--\n--boolean=true\n--byte=127\n", jclo.usage());

        jclo.parse(new String[] { "--byte=127", "--boolean=true" });
        assertEquals("-byte--\n--byte=127\n--boolean--\n--boolean=true\n", jclo.usage());

        jclo.parse(new String[] { "--boolean=true", "--byte=127", "--short=-32768" });
        assertEquals("-boolean--\n--boolean=true\n--byte=127\n--short=-32768\n", jclo.usage());

        jclo.parse(new String[] { "--boolean=true", "--byte=127", "--short=-32768", "--int=-2147483649" });
        assertEquals("-boolean--\n--boolean=true\n--byte=127\n--short=-32768\n--int=-2147483649\n", jclo.usage());

        jclo.parse(new String[] { "--boolean=true", "--byte=127", "--short=-32768", "--int=-2147483649", "--float=3.14159265" });
        assertEquals("-boolean--\n--boolean=true\n--byte=127\n--short=-32768\n--int=-2147483649\n--float=3.14159265\n", jclo.usage());

        jclo.parse(new String[] { "--boolean=true", "--byte=127", "--short=-32768", "--int=-2147483649", "--float=3.14159265", "--double=3.14159265" });
        assertEquals("-boolean--\n--boolean=true\n--byte=127\n--short=-32768\n--int=-2147483649\n--float=3.14159265\n--double=3.14159265\n", jclo.usage());

        jclo.parse(new String[] { "--boolean=true", "--byte=127", "--short=-32768", "--int=-2147483649", "--float=3.14159265", "--double=3.14159265", "--long=9223372036854775807" });
        assertEquals("-boolean--\n--boolean=true\n--byte=127\n--short=-32768\n--int=-2147483649\n--float=3.14159265\n--double=3.14159265\n--long=9223372036854775807\n", jclo.usage());

        jclo.parse(new String[] { "--boolean=true", "--byte=127", "--short=-32768", "--int=-2147483649", "--float=3.14159265", "--double=3.14159265", "--long=9223372036854775807", "--string=Test String" });
        assertEquals("-boolean--\n--boolean=true\n--byte=127\n--short=-32768\n--int=-2147483649\n--float=3.14159265\n--double=3.14159265\n--long=9223372036854775807\n--string=Test String\n", jclo.usage());
    }

    @Test
    public void testJCLOParseWithAliases() {
        JCLO jcloWithAliases = new JCLO(TEST_OBJECT, TEST_ALIAS);
        jcloWithAliases.parse(new String[] { "--alias1=value1" });
        assertEquals("value1", jcloWithAliases.getValue("alias1"));
    }

}