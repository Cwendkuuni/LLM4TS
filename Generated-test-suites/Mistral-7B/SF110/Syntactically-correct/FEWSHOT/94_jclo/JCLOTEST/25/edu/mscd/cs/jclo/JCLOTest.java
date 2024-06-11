package edu.mscd.cs.jclo;

import static org.junit.Assert.*;
import org.junit.Test;

public class JCLOTest {

    @Test
    public void testJCLO() {
        // Test default constructor
        JCLO jclo1 = new JCLO(null);
        assertNull(jclo1.getObject());
        assertNull(jclo1.getPrefix());
        assertNull(jclo1.getAliases());

        // Test constructor with object
        Object testObject = new Object() {
            private String testField = "testValue";
        };
        JCLO jclo2 = new JCLO(testObject);
        Field[] fields = testObject.getClass().getDeclaredFields();
        Field testField = null;
        for (Field field : fields) {
            if (field.getName().equals("testField")) {
                testField = field;
                break;
            }
        }
        assertEquals(testObject, jclo2.getObject());
        assertEquals(null, jclo2.getPrefix());
        assertNull(jclo2.getAliases());

        // Test constructor with object and aliases
        String[][] aliases = {{ "test1", "alias1" }, { "test2", "alias2" }};
        JCLO jclo3 = new JCLO(testObject, aliases);
        assertEquals(testObject, jclo3.getObject());
        assertEquals(null, jclo3.getPrefix());
        assertEquals(aliases, jclo3.getAliases());

        // Test constructor with prefix and object
        JCLO jclo4 = new JCLO("prefix", testObject);
        assertEquals(testObject, jclo4.getObject());
        assertEquals("prefix", jclo4.getPrefix());
        assertNull(jclo4.getAliases());

        // Test constructor with prefix, object, and aliases
        JCLO jclo5 = new JCLO("prefix", testObject, aliases);
        assertEquals(testObject, jclo5.getObject());
        assertEquals("prefix", jclo5.getPrefix());
        assertEquals(aliases, jclo5.getAliases());
    }

    @Test
    public void testGetField() {
        Object testObject = new Object() {
            private String testField1 = "testValue1";
            private int testField2 = 1;
            private boolean testField3 = true;
        };
        JCLO jclo = new JCLO(testObject);

        Field testField1 = jclo.getField("testField1");
        assertEquals("testField1", testField1.getName());
        assertEquals(String.class, testField1.getType());
        assertEquals("testValue1", (String) jclo.getValue("testField1"));

        Field testField2 = jclo.getField("testField2");
        assertEquals("testField2", testField2.getName());
        assertEquals(int.class, testField2.getType());
        assertEquals(1, (int) jclo.getValue("testField2"));

        Field testField3 = jclo.getField("testField3");
        assertEquals("testField3", testField3.getName());
        assertEquals(boolean.class, testField3.getType());
        assertEquals(true, jclo.getBoolean("testField3"));
    }

    @Test
    public void testGetValue() {
        Object testObject = new Object() {
            private String testField1 = "testValue1";
            private int testField2 = 1;
            private boolean testField3 = true;
        };
        JCLO jclo = new JCLO(testObject);

        assertEquals("testValue1", jclo.getValue("testField1"));
        assertEquals(1, jclo.getInt("testField2"));
        assertEquals(true, jclo.getBoolean("testField3"));
    }

    @Test
    public void testSetObject() {
        Object testObject = new Object();
        JCLO jclo = new JCLO(null);

        jclo.setObject(jclo.getField("testField1"), "newValue");
        assertEquals("newValue", jclo.getValue("testField1"));

        jclo.setObject(jclo.getField("testField2"), 42);
        assertEquals(42, jclo.getInt("testField2"));

        jclo.setObject(jclo.getField("testField3"), false);
        assertEquals(false, jclo.getBoolean("testField3"));
    }

    @Test
    public void testGetArrayType() {
        assertEquals("int[]", JCLO.getArrayType(int[].class));
        assertEquals("String", JCLO.getArrayType(String[].class));
    }

    @Test
    public void testToString() {
        Object testObject = new Object() {
            private String testField1 = "testValue1";
            private int testField2 = 1;
            private boolean testField3 = true;
            private String[] testField4 = {"testValue4_1", "testValue4_2"};
            private int[] testField5 = {1, 2, 3};
        };
        JCLO jclo = new JCLO(testObject);

        assertEquals("int: testField2 = 1\nString: testField1 = testValue1\nboolean: testField3 = true\nString[]: testField4 = [testValue4_1, testValue4_2]\nint: testField5[] = [1, 2, 3]", jclo.toString());
    }

    @Test
    public void testUsage() {
        Object testObject = new Object() {
            private String testField1 = "testValue1";
            private int testField2 = 1;
            private boolean testField3 = true;
            private String[] testField4 = {"testValue4_1", "testValue4_2"};
            private int[] testField5 = {1, 2, 3};
        };
        JCLO jclo = new JCLO(testObject);

        String usage = jclo.usage();
        String expectedUsage = "--testField1=<String>\n--testField2=<int>\n--testField3=<boolean>\n--testField4=[<String>...]\n--testField5=[<int>...]";
        assertEquals(expectedUsage, usage);
    }

    @Test
    public void testParseAdditional() {
        Object testObject = new Object() {
            private String[] testField = null;
        };
        String[] args = new String[]{"--additional", "value1", "value2"};

        JCLO jclo = new JCLO(testObject);
        jclo.parseAdditional(args, 0);

        assertEquals(2, testObject.getClass().getDeclaredField("testField").get(testObject).length);
        assertEquals("value1", testObject.getClass().getDeclaredField("testField")[0]);
        assertEquals("value2", testObject.getClass().getDeclaredField("testField")[1]);
    }

    @Test
    public void testAddToArray() {
        Object testObject = new Object() {
            private int[] testField = {};
        };
        JCLO jclo = new JCLO(testObject);

        jclo.addToArray(testObject.getClass().getDeclaredField("testField"), 42);
        assertEquals(1, testObject.getClass().getDeclaredField("testField").get(testObject).length);
        assertEquals(42, testObject.getClass().getDeclaredField("testField")[0]);

        jclo.addToArray(testObject.getClass().getDeclaredField("testField"), 43);
        assertEquals(2, testObject.getClass().getDeclaredField("testField").get(testObject).length);
        assertEquals(42, testObject.getClass().getDeclaredField("testField")[0]);
        assertEquals(43, testObject.getClass().getDeclaredField("testField")[1]);
    }

    @Test
    public void testParse() {
        Object testObject = new Object() {
            private String testField1 = null;
            private int testField2 = 0;
            private boolean testField3 = false;
            private String[] testField4 = {};
            private int[] testField5 = {};
        };
        String[] args = new String[]{"--testField1=value1", "--testField2=2", "--testField3", "--testField4=value4_1", "--testField4=value4_2", "--testField5=3", "--testField5=4"};

        JCLO jclo = new JCLO(testObject);
        jclo.parse(args);

        assertEquals("value1", testObject.getClass().getDeclaredField("testField1").get(testObject));
        assertEquals(2, testObject.getClass().getDeclaredField("testField2").get(testObject));
        assertEquals(true, testObject.getClass().getDeclaredField("testField3").get(testObject));
        assertEquals(2, testObject.getClass().getDeclaredField("testField4").length);
        assertEquals("value4_1", testObject.getClass().getDeclaredField("testField4")[0]);
        assertEquals("value4_2", testObject.getClass().getDeclaredField("testField4")[1]);
        assertEquals(2, testObject.getClass().getDeclaredField("testField5").length);
        assertEquals(3, testObject.getClass().getDeclaredField("testField5")[0]);
        assertEquals(4, testObject.getClass().getDeclaredField("testField5")[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidOption() {
        Object testObject = new Object() {
            private String testField = null;
        };
        String[] args = new String[]{"--testField=value"};

        JCLO jclo = new JCLO(testObject);
        jclo.parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingValue() {
        Object testObject = new Object() {
            private String testField = null;
        };
        String[] args = new String[]{"--testField"};

        JCLO jclo = new JCLO(testObject);
        jclo.parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidValueType() {
        Object testObject = new Object() {
            private int testField = 0;
        };
        String[] args = new String[]{"--testField=value"};

        JCLO jclo = new JCLO(testObject);
        jclo.parse(args);
    }
}