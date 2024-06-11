package edu.mscd.cs.jclo;

import static org.junit.Assert.*;
import org.junit.Test;

public class JCLOTest {

    @Test
    public void testJCLOConstruction() {
        JCLO jclo = new JCLO(new Object());
        assertNotNull(jclo.object);
        JCLO jcloWithPrefix = new JCLO("prefix", new Object());
        assertEquals("prefix", jcloWithPrefix.prefix);
        JCLO jcloWithAliases = new JCLO(new Object(), new String[][]{{"a", "b"}, {"c", "d"}});
        assertEquals(new String[][]{{"a", "b"}, {"c", "d"}}, jcloWithAliases.aliases);
    }

    @Test
    public void testGetValue() {
        JCLO jclo = new JCLO(new Object());
        Field field = jclo.getClass().getDeclaredField("object");
        jclo.setObject(field, new String("test"));
        assertEquals("test", jclo.getValue("object"));
    }

    @Test
    public void testParse() {
        JCLO jclo = new JCLO(new Object());
        String[] args = {"-a", "1", "--boolean=true", "--char=x", "--short=123", "--int=456", "--float=7.8", "--double=9.123", "--long=12345678901234567890", "--string=test", "--byte=1", "--boolean=false", "--additional=1,2,3"};
        jclo.parse(args);
        assertEquals(true, jclo.getBoolean("boolean"));
        assertEquals('x', jclo.getChar("char"));
        assertEquals((short)123, jclo.getShort("short"));
        assertEquals(456, jclo.getInt("int"));
        assertEquals(7.8f, jclo.getFloat("float"));
        assertEquals(9.123d, jclo.getDouble("double"));
        assertEquals(12345678901234567890L, jclo.getLong("long"));
        assertArrayEquals(new String[]{"test"}, jclo.getString("string"));
        assertEquals((byte)1, jclo.getByte("byte"));
        assertFalse(jclo.getBoolean("boolean2"));
        assertArrayEquals(new String[]{"1", "2", "3"}, jclo.getString("additional"));
    }

    @Test
    public void testUsage() {
        JCLO jclo = new JCLO(new Object());
        assertEquals("", jclo.usage());
        JCLO jcloWithFields = new JCLO(new Object(), new String[][]{{"a", "b"}, {"c", "d"}});
        String usage = jcloWithFields.usage();
        assertTrue(usage.contains("--a="));
        assertTrue(usage.contains("--c="));
    }
}