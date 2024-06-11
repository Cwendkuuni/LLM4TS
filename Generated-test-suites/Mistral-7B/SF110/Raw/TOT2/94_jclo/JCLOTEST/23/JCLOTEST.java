package edu.mscd.cs.jclo;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import java.lang.reflect.Field;
import java.util.Arrays;

public class JCLOTest {

    @Test
    public void testEmptyConstructor() {
        JCLO jclo = new JCLO(new Object());
        assertEquals(0, jclo.fields.length);
        assertEquals(null, jclo.object);
        assertEquals(null, jclo.prefix);
        assertEquals(null, jclo.aliases);
    }

    @Test
    public void testConstructorWithObject() {
        String prefix = "test";
        Object testObject = new Object() {};
        JCLO jclo = new JCLO(prefix, testObject);
        assertEquals(prefix, jclo.prefix);
        assertEquals(testObject, jclo.object);
        assertEquals(null, jclo.aliases);
        assertEquals(testObject.getClass().getDeclaredFields().length, jclo.fields.length);
        for (Field field : testObject.getClass().getDeclaredFields()) {
            assertEquals(field.getName(), jclo.getField(field.getName()).getName());
        }
    }

    @Test
    public void testConstructorWithObjectAndAliases() {
        String prefix = "test";
        Object testObject = new Object() {};
        String[][] aliases = {{ "alias1", "alias2" }, { "alias3", "alias4" }};
        JCLO jclo = new JCLO(prefix, testObject, aliases);
        assertEquals(prefix, jclo.prefix);
        assertEquals(testObject, jclo.object);
        assertEquals(aliases, jclo.aliases);
        assertEquals(testObject.getClass().getDeclaredFields().length, jclo.fields.length);
        for (Field field : testObject.getClass().getDeclaredFields()) {
            String key = jclo.getKey(field.getName());
            String alias = null;
            for (String[] aliasEntry : aliases) {
                if (aliasEntry[0].equals(key)) {
                    alias = aliasEntry[1];
                    break;
                }
            }
            assertEquals(field.getName(), alias != null ? alias : key);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidPrefix() {
        JCLO jclo = new JCLO("", new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullObject() {
        JCLO jclo = new JCLO("test", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyAliases() {
        String prefix = "test";
        Object testObject = new Object() {};
        JCLO jclo = new JCLO(prefix, testObject, (String[][]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidAlias() {
        String prefix = "test";
        Object testObject = new Object() {};
        String[][] aliases = {{ "alias1", "alias2" }, { "alias3", "alias4" }, { "invalid", "invalid" }};
        JCLO jclo = new JCLO(prefix, testObject, aliases);
    }

    @Test(expected = IllegalAccessException.class)
    public void testGetObjectWithIllegalAccess() {
        String prefix = "test";
        Object testObject = new Object() {};
        JCLO jclo = new JCLO(prefix, testObject);
        Field invalidField = new Field("invalidField", int.class, TestClass.class) {
            {
                setAccessible(true);
            }
        };
        jclo.setObject(invalidField, 42);
        assertNull(jclo.getObject(invalidField));
    }

    @Test
    public void testGetValue() {
        String prefix = "test";
        Object testObject = new TestClass();
        JCLO jclo = new JCLO(prefix, testObject);
        assertEquals(42, jclo.getValue("intField").intValue());
    }

    @Test
    public void testToString() {
        String prefix = "test";
        Object testObject = new TestClass();
        JCLO jclo = new JCLO(prefix, testObject);
        assertEquals(
            "char: charField = test.charField = 'A'\n" +
            "int: intField = test.intField = 42\n" +
            "TestClass: testObject = test.testObject = [Ltest.TestClass;@67e72d62\n",
            jclo.toString()
        );
    }
}

class TestClass {
    private char charField = 'A';
    private int intField = 42;
}