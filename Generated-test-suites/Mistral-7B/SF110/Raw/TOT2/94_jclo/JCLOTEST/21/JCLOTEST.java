
package edu.mscd.cs.jclo;

import org.junit.Test;
import static org.junit.Assert.*;

public class JCLOTest {
    // Test cases for constructors
    @Test
    public void testJCLOWithNoArguments() {
        JCLO jclo = new JCLO(null);
        assertNotNull(jclo.object);
        assertNotNull(jclo.fields);
    }

    @Test
    public void testJCLOWithObjectArgument() {
        JCLO jclo = new JCLO(new Object());
        assertNotNull(jclo.object);
        assertNotNull(jclo.fields);
    }

    @Test
    public void testJCLOWithStringPrefixArgument() {
        JCLO jclo = new JCLO("prefix", new Object());
        assertNotNull(jclo.prefix);
        assertNotNull(jclo.object);
        assertNotNull(jclo.fields);
    }

    // Test cases for methods
    @Test
    public void testGetValue() throws NoSuchFieldException, IllegalAccessException {
        JCLO jclo = new JCLO("prefix", new Object()) {{
            fields = new Field[] { new Field(this, "field1", Integer.TYPE), new Field(this, "field2", String.class) };
            setAccessible(true);
        }};
        setObject(jclo.fields[0], 1);
        setObject(jclo.fields[1], "test");
        assertEquals(1, jclo.getValue("field1"));
        assertEquals("test", jclo.getValue("field2"));
    }

    @Test
    public void testGetUsageType() {
        assertEquals("boolean", JCLO.getUsageType(boolean.class));
        assertEquals("int...", JCLO.getUsageType(int[].class));
        assertEquals("java.lang.String", JCLO.getUsageType(String.class));
    }

    @Test
    public void testUsage() {
        JCLO jclo = new JCLO("prefix", new Object()) {{
            fields = new Field[] { new Field(this, "field1", Integer.TYPE), new Field(this, "field2", String.class) };
            setAccessible(true);
        }};
        assertEquals("prefixfield1=" + JCLO.getUsageType(Integer.TYPE) + "\n"
                + "prefixfield2=" + JCLO.getUsageType(String.class) + "\n", jclo.usage());
    }

    // Test cases for error handling and edge cases
    @Test(expected = IllegalAccessException.class)
    public void testGetValueWithIllegalAccessException() throws NoSuchFieldException, IllegalAccessException {
        JCLO jclo = new JCLO("prefix", new Object()) {{
            fields = new Field[] { new Field(this, "field1", Integer.TYPE), new Field(this, "field2", String.class) };
            setAccessible(false);
        }};
        jclo.getValue("field1");
    }

    @Test(expected = NoSuchFieldException.class)
    public void testGetValueWithNoSuchFieldException() {
        JCLO jclo = new JCLO("prefix", new Object()) {{
            fields = new Field[] { new Field(this, "field1", Integer.TYPE), new Field(this, "field2", String.class) };
        }};
        jclo.getValue("nonExistentField");
    }

    @Test
    public void testGetValueWithNullKey() {
        JCLO jclo = new JCLO("prefix", new Object()) {{
            fields = new Field[] { new Field(this, "field1", Integer.TYPE), new Field(this, "field2", String.class) };
        }};
        assertNull(jclo.getValue(null));
    }
}