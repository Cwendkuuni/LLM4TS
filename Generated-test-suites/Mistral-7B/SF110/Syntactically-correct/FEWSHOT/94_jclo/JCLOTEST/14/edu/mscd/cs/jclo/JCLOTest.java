package edu.mscd.cs.jclo;

import static org.junit.Assert.*;
import org.junit.Test;

public class JCLOTest {

    @Test
    public void testJCLO() {
        // Test constructor with no arguments
        JCLO jclo = new JCLO(new Object());
        assertNotNull(jclo);

        // Test constructor with aliases
        String[][] aliases = new String[][]{{"a", "aliasA"}, {"b", "aliasB"}};
        JCLO jcloAliases = new JCLO("prefix", new Object(), aliases);
        assertNotNull(jcloAliases);

        // Test getting value for existing field
        JCLO jcloTest = new JCLO("prefix", new Object()) {
            private Object testValue = "Test Value";
        };
        assertEquals("Test Value", jcloTest.getValue("testValue"));

        // Test getting value for non-existent field
        assertNull(jcloTest.getValue("nonExistentField"));

        // Test parsing arguments with no equals sign
        String[] args1 = new String[]{"--prefix", "TestPrefix", "-testValue", "TestValue"};
        JCLO jcloArgs1 = new JCLO("TestPrefix", new Object());
        jcloArgs1.parse(args1);
        assertEquals("TestValue", jcloArgs1.getValue("testValue"));

        // Test parsing arguments with equals sign
        String[] args2 = new String[]{"--prefix", "TestPrefix", "--testValue=TestValue"};
        JCLO jcloArgs2 = new JCLO("TestPrefix", new Object());
        jcloArgs2.parse(args2);
        assertEquals("TestValue", jcloArgs2.getValue("testValue"));

        // Test parsing arguments with double dashes
        String[] args3 = new String[]{"--prefix", "TestPrefix", "--test-value=TestValue"};
        JCLO jcloArgs3 = new JCLO("TestPrefix", new Object());
        jcloArgs3.parse(args3);
        assertEquals("TestValue", jcloArgs3.getValue("testValue"));

        // Test parsing multiple values for array field
        JCLO jcloArray = new JCLO("prefix", new Object()) {
            private Object[] arrayValue = new Object[2];
        };
        String[] argsArray = new String[]{"--prefix", "TestPrefix", "--arrayValue", "Value1", "Value2"};
        jcloArray.parse(argsArray);
        assertArrayEquals(new Object[]{new String("Value1"), new String("Value2")}, jcloArray.getValue("arrayValue"));

        // Test parsing additional arguments
        String[] argsAdditional = new String[]{"--prefix", "TestPrefix", "--additional", "Value1", "Value2"};
        JCLO jcloAdditional = new JCLO("prefix", new Object()) {
            private Object[] additional = new Object[2];
        };
        jcloAdditional.parse(argsAdditional);
        assertArrayEquals(new Object[]{new String("Value1"), new String("Value2")}, jcloAdditional.getValue("additional"));

        // Test parsing non-existent option
        String[] argsNonExistent = new String[]{"--prefix", "TestPrefix", "--nonExistent"};
        JCLO jcloNonExistent = new JCLO("prefix", new Object());
        try {
            jcloNonExistent.parse(argsNonExistent);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testToString() {
        JCLO jclo = new JCLO("prefix", new Object()) {
            private int intValue = 42;
            private char charValue = 'A';
            private boolean booleanValue = true;
            private String stringValue = "Test String";
            private byte[] byteArrayValue = new byte[]{1, 2, 3};
            private char[] charArrayValue = new char[]{'A', 'B', 'C'};
            private short[] shortArrayValue = new short[]{1, 2, 3};
            private int[] intArrayValue = new int[]{1, 2, 3};
            private float[] floatArrayValue = new float[]{1.0f, 2.0f, 3.0f};
            private double[] doubleArrayValue = new double[]{1.0, 2.0, 3.0};
            private long[] longArrayValue = new long[]{1, 2, 3};
            private String[] stringArrayValue = new String[]{"Test1", "Test2", "Test3"};
        };
        assertEquals("int: intValue = 42,\n" +
                     "char: charValue = A,\n" +
                     "boolean: booleanValue = true,\n" +
                     "String: stringValue = Test String,\n" +
                     "byte[]: byteArrayValue = [1, 2, 3],\n" +
                     "char[]: charArrayValue = [A, B, C],\n" +
                     "short[]: shortArrayValue = [1, 2, 3],\n" +
                     "int[]: intArrayValue = [1, 2, 3],\n" +
                     "float[]: floatArrayValue = [1.0, 2.0, 3.0],\n" +
                     "double[]: doubleArrayValue = [1.0, 2.0, 3.0],\n" +
                     "long[]: longArrayValue = [1, 2, 3],\n" +
                     "String[]: stringArrayValue = [Test1, Test2, Test3]", jclo.toString());
    }

    @Test
    public void testUsage() {
        JCLO jclo = new JCLO("prefix", new Object());
        assertEquals("", jclo.usage());

        JCLO jcloBool = new JCLO("prefix", new Object(), null, true);
        assertEquals("--prefix:\n" +
                     "  type: boolean\n" +
                     "", jcloBool.usage());

        JCLO jcloString = new JCLO("prefix", new Object(), null, false, "str");
        assertEquals("--prefix--str:\n" +
                     "  type: java.lang.String\n" +
                     "", jcloString.usage());

        JCLO jcloArray = new JCLO("prefix", new Object(), null, false, null, new Class[]{int[].class});
        assertEquals("--prefix--int-array:\n" +
                     "  type: int[]\n" +
                     "", jcloArray.usage());

        JCLO jcloAliases = new JCLO("prefix", new Object(), new String[][]{{"a", "aliasA"}, {"b", "aliasB"}});
        assertEquals("--prefix--a:  # aliasA for b\n" +
                     "--prefix--b:  # aliasB for b\n" +
                     "--prefix--b=VALUE:  # aliasB for b with value VALUE\n", jcloAliases.usage());

        JCLO jcloDoubleDashes = new JCLO("prefix", new Object(), null, false, null, null, true);
        assertEquals("--prefix=\n" +
                     "--prefix=VALUE:\n", jcloDoubleDashes.usage());

        JCLO jcloHasEquals = new JCLO("prefix", new Object(), null, false, null, null, false, true);
        assertEquals("--prefix=VALUE:\n", jcloHasEquals.usage());

        JCLO jcloMultipleFields = new JCLO("prefix", new Object()) {
            private int intValue = 42;
            private boolean booleanValue = true;
            private String stringValue = "Test String";
        };
        assertEquals("--prefix--int:\n" +
                     "  type: int\n" +
                     "--prefix--boolean:\n" +
                     "  type: boolean\n" +
                     "--prefix--string:\n" +
                     "  type: java.lang.String\n", jcloMultipleFields.usage());

        JCLO jcloPrefix = new JCLO("prefix1", new Object());
        assertEquals("--prefix1:\n", jcloPrefix.usage());

        JCLO jcloPrefixMultipleFields = new JCLO("prefix1", new Object()) {
            private int intValue = 42;
            private boolean booleanValue = true;
            private String stringValue = "Test String";
        };
        assertEquals("--prefix1--int:\n" +
                     "  type: int\n" +
                     "--prefix1--boolean:\n" +
                     "  type: boolean\n" +
                     "--prefix1--string:\n" +
                     "  type: java.lang.String\n", jcloPrefixMultipleFields.usage());

        JCLO jcloPrefixWithArray = new JCLO("prefix1", new Object(), null, false, null, new Class[]{int[].class});
        assertEquals("--prefix1--int-array:\n" +
                     "  type: int[]\n", jcloPrefixWithArray.usage());

        JCLO jcloPrefixWithAliases = new JCLO("prefix1", new Object(), new String[][]{{"a", "aliasA"}, {"b", "aliasB"}});
        assertEquals("--prefix1--a:  # aliasA for b\n" +
                     "--prefix1--b:  # aliasB for b\n" +
                     "--prefix1--b=VALUE:  # aliasB for b with value VALUE\n", jcloPrefixWithAliases.usage());

        JCLO jcloPrefixDoubleDashes = new JCLO("prefix1", new Object(), null, false, null, null, true);
        assertEquals("--prefix1=\n" +
                     "--prefix1=VALUE:\n", jcloPrefixDoubleDashes.usage());

        JCLO jcloPrefixHasEquals = new JCLO("prefix1", new Object(), null, false, null, null, false, true);
        assertEquals("--prefix1=VALUE:\n", jcloPrefixHasEquals.usage());

        JCLO jcloMultiplePrefixes = new JCLO("prefix1", new Object()) {
            private int intValue = 42;
            private boolean booleanValue = true;
            private String stringValue = "Test String";
        };
        JCLO jcloMultiplePrefixes2 = new JCLO("prefix2", new Object()) {
            private int intValue = 43;
            private boolean booleanValue = false;
            private String stringValue = "Test Other String";
        };
        assertEquals("--prefix1--int:\n" +
                     "  type: int\n" +
                     "--prefix1--boolean:\n" +
                     "  type: boolean\n" +
                     "--prefix1--string:\n" +
                     "  type: java.lang.String\n" +
                     "--prefix2--int:\n" +
                     "  type: int\n" +
                     "--prefix2--boolean:\n" +
                     "  type: boolean\n" +
                     "--prefix2--string:\n" +
                     "  type: java.lang.String\n", jcloMultiplePrefixes.usage() + jcloMultiplePrefixes2.usage());

        JCLO jcloMultiplePrefixesWithArray = new JCLO("prefix1", new Object()) {
            private int intValue = 42;
        };
        JCLO jcloMultiplePrefixesWithArray2 = new JCLO("prefix2", new Object(), null, false, null, new Class[]{int[].class});
        assertEquals("--prefix1--int:\n" +
                     "  type: int\n" +
                     "--prefix1--int-array:\n" +
                     "  type: int[]\n" +
                     "--prefix2--int:\n" +
                     "  type: int\n" +
                     "--prefix2--int-array:\n" +
                     "  type: int[]\n", jcloMultiplePrefixes.usage() + jcloMultiplePrefixesWithArray2.usage());

        JCLO jcloMultiplePrefixesWithAliases = new JCLO("prefix1", new Object()) {
            private int intValue = 42;
        };
        JCLO jcloMultiplePrefixesWithAliases2 = new JCLO("prefix2", new Object(), new String[][]{{"a", "aliasA"}, {"b", "aliasB"}});
        assertEquals("--prefix1--int:\n" +
                     "  type: int\n" +
                     "--prefix1--a:  # aliasA for b\n" +
                     "--prefix1--b:  # aliasB for b\n" +
                     "--prefix1--b=VALUE:  # aliasB for b with value VALUE\n" +
                     "--prefix2--int:\n" +
                     "  type: int\n" +
                     "--prefix2--a:  # aliasA for b\n" +
                     "--prefix2--b:  # aliasB for b\n" +
                     "--prefix2--b=VALUE:  # aliasB for b with value VALUE\n", jcloMultiplePrefixes.usage() + jcloMultiplePrefixesWithAliases2.usage());

        JCLO jcloMultiplePrefixesDoubleDashes = new JCLO("prefix1", new Object()) {
            private int intValue = 42;
        };
        JCLO jcloMultiplePrefixesDoubleDashes2 = new JCLO("prefix2", new Object(), null, true);
        assertEquals("--prefix1=\n" +
                     "--prefix1=\n" +
                     "--prefix2=\n" +
                     "--prefix2=\n", jcloMultiplePrefixes.usage() + jcloMultiplePrefixesDoubleDashes2.usage());

        JCLO jcloMultiplePrefixesHasEquals = new JCLO("prefix1", new Object()) {
            private int intValue = 42;
        };
        JCLO jcloMultiplePrefixesHasEquals2 = new JCLO("prefix2", new Object(), null, false, true);
        assertEquals("--prefix1=\n" +
                     "--prefix1=VALUE:\n" +
                     "--prefix2=\n" +
                     "--prefix2=VALUE:\n", jcloMultiplePrefixes.usage() + jcloMultiplePrefixesHasEquals2.usage());
    }
}