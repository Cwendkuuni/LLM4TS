package edu.mscd.cs.jclo; // make sure to import necessary libraries at the beginning of the file

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class JCLOTest {

    private JCLO jclo;
    private Object testObject;
    
    @Before
    public void setUp() {
        testObject = new Object() {
            Field name = new Field("name", String.class) { {} };
            Field age = new Field("age", int.class) { {} };
            Field booleanField = new Field("booleanField", boolean.class) { {} };
        };
        jclo = new JCLO(testObject);
    }

    
    
    // Test constructor with no arguments
    @Test
    public void testJCLOWithNoArguments() {
        JCLO jclo = new JCLO(null);
        assertNull(jclo.getObject());
        assertNull(jclo.prefix);
        assertNull(jclo.aliases);
    }
    
    // Test constructor with Object argument
    @Test
    public void testJCLOWithObjectArgument() {
        JCLO jclo = new JCLO(testObject);
        assertNotNull(jclo.getObject());
        assertEquals("", jclo.prefix);
        assertNull(jclo.aliases);
    }
    
    // Test constructor with String prefix and Object argument
    @Test
    public void testJCLOWithStringPrefixAndObjectArgument() {
        JCLO jclo = new JCLO("prefix", testObject);
        assertEquals("prefix", jclo.prefix);
        assertNull(jclo.aliases);
    }
    
    // Test constructor with String prefix, Object argument, and String[][] aliases
    @Test
    public void testJCLOWithStringPrefixObjectArgumentAndAliases() {
        String[][] aliases = {{"name", "n"}, {"age", "a"}};
        JCLO jclo = new JCLO("prefix", testObject, aliases);
        assertEquals("prefix", jclo.prefix);
        assertEquals(aliases, jclo.aliases);
    }
    
    // Test getValue method with valid key
    @Test
    public void testGetValueWithValidKey() {
        String key = "name";
        String expectedValue = "test";
        setValue(testObject, "name", expectedValue);
        Object actualValue = jclo.getValue(key);
        assertEquals(expectedValue, actualValue);
    }
    
    // Test getValue method with invalid key
    @Test(expected = IllegalArgumentException.class)
    public void testGetValueWithInvalidKey() {
        String key = "invalidKey";
        jclo.getValue(key);
    }
    
    // Test parse method with valid arguments
    @Test
    public void testParseWithValidArguments() {
        String[] args = {"--double-dash", "--has-equals=booleanField=true", "--name=testName", "--age=25"};
        jclo.parse(args);
        assertTrue(jclo.doubleDashes);
        assertTrue(jclo.hasEquals);
        assertEquals("booleanField", jclo.getField("booleanField").getName());
        assertEquals(true, jclo.getBoolean("booleanField"));
        assertEquals("testName", jclo.getString("name"));
        assertEquals(25, jclo.getInt("age"));
    }
    
    // Test parse method with invalid arguments
    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidArguments() {
        String[] args = {"--invalidOption=invalidValue"};
        jclo.parse(args);
    }
    
    // Test getters and setters for fields and aliases
    @Test
    public void testGettersAndSetters() {
        String[][] aliases = {{"name", "n"}, {"age", "a"}};
        jclo = new JCLO("prefix", testObject, aliases);
        String key = "prefix_name";
        String value = "testValue";
        Field field = jclo.getField(key);
        assertNotNull(field);
        Object originalValue = field.get(jclo.object);
        jclo.setObject(field, value);
        assertEquals(value, field.get(jclo.object));
        assertEquals(value, jclo.getValue(key));
    }

    // Test edge cases and exceptions handling
    @Test(expected = IllegalAccessException.class)
    public void testGetObjectWithIllegalAccessException() {
        Field illegalField = new Field("illegalField", byte.class) { {} };
        JCLO jclo = new JCLO(testObject);
        jclo.setObject(illegalField, new Byte("test IllegalAccessException"));
        try {
            jclo.getObject(illegalField);
        } catch (IllegalAccessException e) {
            throw new AssertionError("Unexpected IllegalAccessException", e);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFieldWithInvalidKey() {
        JCLO jclo = new JCLO(testObject);
        assertNull(jclo.getField("invalidKey"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetObjectWithNullField() {
        Field nullField = null;
        JCLO jclo = new JCLO(testObject);
        try {
            jclo.getObject(nullField);
        } catch (NullPointerException e) {
            // expected exception
        }
    }

    @Test
    public void testToString() {
        String expectedOutput = "java.lang.Object[]: name = test, age = 25";
        setValue(testObject, "name", "test");
        setValue(testObject, "age", 25);
        assertEquals(expectedOutput, jclo.toString());
    }

    private void setValue(Object object, String key, Object value) {
        Field field = object.getClass().getDeclaredField(key);
        field.setAccessible(true);
        field.set(object, value);
    }

    
}