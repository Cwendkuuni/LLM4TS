package edu.mscd.cs.jclo;

import static org.junit.Assert.*;
import org.junit.Test;

public class JCLOTest {

    @Test
    public void testJCLOConstruction() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        assertEquals(obj.getClass(), jclo.object.getClass());
        assertEquals(0, jclo.fields.length);

        String[][] aliases = new String[1][2];
        aliases[0][0] = "a";
        aliases[0][1] = "b";
        JCLO jcloWithAliases = new JCLO("prefix", obj, aliases);
        assertEquals("prefix", jcloWithAliases.prefix);
        assertEquals(aliases, jcloWithAliases.aliases);
        assertEquals(obj.getClass(), jcloWithAliases.object.getClass());
        assertEquals(aliases.length + obj.getClass().getDeclaredFields().length, jcloWithAliases.fields.length);
    }

    @Test
    public void testGetString() {
        String className = String.class.getName();
        Object obj = new Object() {
            String field = "test";
        };
        JCLO jclo = new JCLO(obj);
        assertEquals("test", jclo.getValue("field"));

        JCLO jcloWithPrefix = new JCLO("prefix", obj);
        assertEquals("prefixfield", jcloWithPrefix.getValue("field"));
    }

    @Test
    public void testGetBoolean() {
        Object obj = new Object() {
            boolean field = true;
        };
        JCLO jclo = new JCLO(obj);
        assertTrue(jclo.getBoolean("field"));

        JCLO jcloWithPrefix = new JCLO("prefix", obj);
        assertTrue(jcloWithPrefix.getBoolean("field"));
        assertTrue(jcloWithPrefix.getBoolean("prefixfield"));
    }

    @Test
    public void testGetByte() {
        Object obj = new Object() {
            byte field = 5;
        };
        JCLO jclo = new JCLO(obj);
        assertEquals((byte) 5, jclo.getByte("field"));

        JCLO jcloWithPrefix = new JCLO("prefix", obj);
        assertEquals((byte) 5, jcloWithPrefix.getByte("field"));
        assertEquals((byte) 5, jcloWithPrefix.getByte("prefixfield"));
    }

    @Test
    public void testGetShort() {
        Object obj = new Object() {
            short field = 10;
        };
        JCLO jclo = new JCLO(obj);
        assertEquals((short) 10, jclo.getShort("field"));

        JCLO jcloWithPrefix = new JCLO("prefix", obj);
        assertEquals((short) 10, jcloWithPrefix.getShort("field"));
        assertEquals((short) 10, jcloWithPrefix.getShort("prefixfield"));
    }

    @Test
    public void testGetInt() {
        Object obj = new Object() {
            int field = 15;
        };
        JCLO jclo = new JCLO(obj);
        assertEquals(15, jclo.getInt("field"));

        JCLO jcloWithPrefix = new JCLO("prefix", obj);
        assertEquals(15, jcloWithPrefix.getInt("field"));
        assertEquals(15, jcloWithPrefix.getInt("prefixfield"));
    }

    @Test
    public void testGetFloat() {
        Object obj = new Object() {
            float field = 3.14f;
        };
        JCLO jclo = new JCLO(obj);
        assertEquals(3.14f, jclo.getFloat("field"));

        JCLO jcloWithPrefix = new JCLO("prefix", obj);
        assertEquals(3.14f, jcloWithPrefix.getFloat("field"));
        assertEquals(3.14f, jcloWithPrefix.getFloat("prefixfield"));
    }

    @Test
    public void testGetDouble() {
        Object obj = new Object() {
            double field = 6.28;
        };
        JCLO jclo = new JCLO(obj);
        assertEquals(6.28, jclo.getDouble("field"));

        JCLO jcloWithPrefix = new JCLO("prefix", obj);
        assertEquals(6.28, jcloWithPrefix.getDouble("field"));
        assertEquals(6.28, jcloWithPrefix.getDouble("prefixfield"));
    }

    @Test
    public void testGetLong() {
        Object obj = new Object() {
            long field = 20;
        };
        JCLO jclo = new JCLO(obj);
        assertEquals(20, jclo.getLong("field"));

        JCLO jcloWithPrefix = new JCLO("prefix", obj);
        assertEquals(20, jcloWithPrefix.getLong("field"));
        assertEquals(20, jcloWithPrefix.getLong("prefixfield"));
    }

    @Test
    public void testGetStringArray() {
        Object obj = new Object() {
            String[] field = {"test1", "test2"};
        };
        JCLO jclo = new JCLO(obj);
        assertArrayEquals(new String[]{"test1", "test2"}, (String[]) jclo.getValue("field"));

        JCLO jcloWithPrefix = new JCLO("prefix", obj);
        assertArrayEquals(new String[]{"test1", "test2"}, (String[]) jcloWithPrefix.getValue("field"));
        assertArrayEquals(new String[]{"test1", "test2"}, (String[]) jcloWithPrefix.getValue("prefixfield"));
    }

    @Test
    public void testParse() {
        String[] args = {"--test", "1", "--test2", "2"};
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.parse(args);
        assertEquals(1, jclo.getInt("test"));
        assertEquals(2, jclo.getInt("test2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidOption() {
        String[] args = {"--invalid"};
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingValue() {
        String[] args = {"--test"};
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.parse(args);
    }

    @Test
    public void testParseWithAliases() {
        String[] args = {"--test-alias", "1", "--test", "2"};
        Object obj = new Object();
        String[][] aliases = new String[1][2];
        aliases[0][0] = "test-alias";
        aliases[0][1] = "test";
        JCLO jclo = new JCLO(obj, aliases);
        assertEquals(1, jclo.getInt("test-alias"));
        assertEquals(2, jclo.getInt("test"));
    }

    @Test
    public void testUsage() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        assertEquals("", jclo.usage());

        String[][] aliases = new String[1][2];
        aliases[0][0] = "test";
        aliases[0][1] = "t";
        JCLO jcloWithAliases = new JCLO(obj, aliases);
        assertEquals("--test (=boolean)\n--t (=boolean)\n", jcloWithAliases.usage());
    }
}