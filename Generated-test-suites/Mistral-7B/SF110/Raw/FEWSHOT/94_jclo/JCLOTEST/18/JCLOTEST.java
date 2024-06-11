package edu.mscd.cs.jclo;

import static org.junit.Assert.*;
import org.junit.Test;

public class JCLOTest {

    @Test
    public void testJCLOConstructorWithObject() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        assertNotNull(jclo.object);
        assertEquals(obj.getClass(), jclo.object.getClass());
        assertNull(jclo.prefix);
        assertNull(jclo.aliases);
        assertEquals(obj.getClass().getDeclaredFields().length, jclo.fields.length);
    }

    @Test
    public void testJCLOConstructorWithObjectAndAliases() {
        Object obj = new Object();
        String[][] aliases = new String[1][2];
        aliases[0][0] = "alias1";
        aliases[0][1] = "alias2";
        JCLO jclo = new JCLO(obj, aliases);
        assertNotNull(jclo.object);
        assertEquals(obj.getClass(), jclo.object.getClass());
        assertEquals(aliases, jclo.aliases);
        assertNull(jclo.prefix);
        assertEquals(obj.getClass().getDeclaredFields().length, jclo.fields.length);
    }

    @Test
    public void testJCLOConstructorWithPrefixAndObject() {
        Object obj = new Object();
        String prefix = "prefix";
        JCLO jclo = new JCLO(prefix, obj);
        assertNotNull(jclo.object);
        assertEquals(obj.getClass(), jclo.object.getClass());
        assertEquals(prefix, jclo.prefix);
        assertNull(jclo.aliases);
        assertEquals(obj.getClass().getDeclaredFields().length, jclo.fields.length);
    }

    @Test
    public void testJCLOConstructorWithPrefixAndObjectAndAliases() {
        Object obj = new Object();
        String prefix = "prefix";
        String[][] aliases = new String[1][2];
        aliases[0][0] = "alias1";
        aliases[0][1] = "alias2";
        JCLO jclo = new JCLO(prefix, obj, aliases);
        assertNotNull(jclo.object);
        assertEquals(obj.getClass(), jclo.object.getClass());
        assertEquals(prefix, jclo.prefix);
        assertEquals(aliases, jclo.aliases);
        assertEquals(obj.getClass().getDeclaredFields().length, jclo.fields.length);
    }

    @Test
    public void testGetObject() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        Field field = jclo.fields[0];
        Object value = jclo.getObject(field);
        assertEquals(obj, value);
    }

    @Test(expected = IllegalAccessException.class)
    public void testGetObjectWithIllegalAccess() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        Field field = JCLO.class.getDeclaredField("object");
        Object value = jclo.getObject(field);
    }

    @Test
    public void testGetField() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        Field field = jclo.getField("field1");
        assertNotNull(field);
        assertEquals("field1", field.getName());
    }

    @Test
    public void testGetValue() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        Field field = jclo.fields[0];
        Object value = jclo.getValue("field1");
        assertEquals(jclo.getObject(field), value);
    }

    @Test
    public void testSetObject() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        Field field = jclo.fields[0];
        Object newValue = new Object();
        jclo.setObject(field, newValue);
        assertEquals(newValue, jclo.getObject(field));
    }

    @Test(expected = IllegalAccessException.class)
    public void testSetObjectWithIllegalAccess() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        Field field = JCLO.class.getDeclaredField("object");
        Object newValue = new Object();
        jclo.setObject(field, newValue);
    }

    @Test
    public void testGetArrayType() {
        Class type = String[].class;
        assertEquals("String[]", JCLO.getArrayType(type));
    }

    @Test
    public void testToString() {
        Object obj = new Object() {
            String field1 = "value1";
            String[] field2 = {"value2", "value3"};
        };
        JCLO jclo = new JCLO(obj);
        String result = jclo.toString();
        assertTrue(result.contains("String: field1 = value1"));
        assertTrue(result.contains("String[]: field2 = [value2, value3]"));
    }

    @Test
    public void testGetUsageType() {
        Class type = boolean.class;
        assertEquals("boolean", JCLO.getUsageType(type));
        assertEquals("[=boolean]", JCLO.getUsageType(type), JCLO.doubleDashes);

        type = String[].class;
        assertEquals("String...", JCLO.getUsageType(type));
        assertEquals("=String...", JCLO.getUsageType(type), JCLO.doubleDashes);

        type = Integer.TYPE;
        assertEquals("int", JCLO.getUsageType(type));
        assertEquals(" int", JCLO.getUsageType(type), JCLO.doubleDashes);
    }

    @Test
    public void testUsage() {
        Object obj = new Object() {
            String field1 = "value1";
            String[] field2 = {"value2", "value3"};
        };
        JCLO jclo = new JCLO(obj);
        String result = jclo.usage();
        assertTrue(result.contains("--field1="));
        assertTrue(result.contains("--field2="));
        assertTrue(result.contains("--field2="));
        assertTrue(result.contains("--field2=value2"));
        assertTrue(result.contains("--field2=value3"));
    }

    @Test
    public void testParseAdditional() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        String[] args = {"--additional", "arg1", "arg2"};
        jclo.parseAdditional(args, 1);
        String[] expected = {"arg1", "arg2"};
        assertArrayEquals(expected, (String[])jclo.getValue("additional"));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testParseAdditionalWithInvalidIndex() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        String[] args = {"--additional", "arg1", "arg2"};
        jclo.parseAdditional(args, 3);
    }

    @Test
    public void testAddToArray() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        Field field = jclo.getField("field");
        Object[] initialValue = new Object[0];
        Object newValue = new Object();
        Object result = jclo.addToArray(field, newValue);
        assertArrayEquals(new Object[]{newValue}, (Object[])jclo.getValue(field));
        assertEquals(newValue, result);
    }

    @Test
    public void testParse() {
        Object obj = new Object() {
            String field1 = "value1";
            String[] field2 = {"value2", "value3"};
        };
        JCLO jclo = new JCLO(obj);
        String[] args = {"--field1=value4", "--field2=value5", "--field2=value6"};
        jclo.parse(args);
        assertEquals("value4", jclo.getValue("field1"));
        String[] expected = {"value5", "value6"};
        assertArrayEquals(expected, (String[])jclo.getValue("field2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidArgument() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        String[] args = {"--invalid"};
        jclo.parse(args);
    }

    @Test
    public void testGetBoolean() {
        Object obj = new Object() {
            boolean field = true;
        };
        JCLO jclo = new JCLO(obj);
        assertTrue(jclo.getBoolean("field"));
    }

    @Test
    public void testGetByte() {
        Object obj = new Object() {
            byte field = 1;
        };
        JCLO jclo = new JCLO(obj);
        assertEquals(1, jclo.getByte("field"));
    }

    @Test
    public void testGetChar() {
        Object obj = new Object() {
            char field = 'a';
        };
        JCLO jclo = new JCLO(obj);
        assertEquals('a', jclo.getChar("field"));
    }

    @Test
    public void testGetShort() {
        Object obj = new Object() {
            short field = 1;
        };
        JCLO jclo = new JCLO(obj);
        assertEquals(1, jclo.getShort("field"));
    }

    @Test
    public void testgetInt() {
        Object obj = new Object() {
            int field = 1;
        };
        JCLO jclo = new JCLO(obj);
        assertEquals(1, jclo.getInt("field"));
    }

    @Test
    public void testGetFloat() {
        Object obj = new Object() {
            float field = 1.0f;
        };
        JCLO jclo = new JCLO(obj);
        assertEquals(1.0f, jclo.getFloat("field"), 0.001f);
    }

    @Test
    public void testGetDouble() {
        Object obj = new Object() {
            double field = 1.0;
        };
        JCLO jclo = new JCLO(obj);
        assertEquals(1.0, jclo.getDouble("field"), 0.001);
    }

    @Test
    public void testGetLong() {
        Object obj = new Object() {
            long field = 1L;
        };
        JCLO jclo = new JCLO(obj);
        assertEquals(1L, jclo.getLong("field"));
    }

    @Test
    public void testGetString() {
        Object obj = new Object() {
            String field = "value";
        };
        JCLO jclo = new JCLO(obj);
        assertEquals("value", jclo.getString("field"));
    }

    @Test
    public void testGetBytes() {
        Object obj = new Object() {
            byte[] field = new byte[]{1, 2, 3};
        };
        JCLO jclo = new JCLO(obj);
        assertArrayEquals(new byte[]{1, 2, 3}, (byte[])jclo.getBytes("field"));
    }

    @Test
    public void testGetChars() {
        Object obj = new Object() {
            char[] field = new char[]{'a', 'b'};
        };
        JCLO jclo = new JCLO(obj);
        assertArrayEquals(new char[]{'a', 'b'}, (char[])jclo.getChars("field"));
    }

    @Test
    public void testGetShorts() {
        Object obj = new Object() {
            short[] field = new short[]{1, 2, 3};
        };
        JCLO jclo = new JCLO(obj);
        assertArrayEquals(new short[]{1, 2, 3}, (short[])jclo.getShorts("field"));
    }

    @Test
    public void testgetInts() {
        Object obj = new Object() {
            int[] field = new int[]{1, 2, 3};
        };
        JCLO jclo = new JCLO(obj);
        assertArrayEquals(new int[]{1, 2, 3}, (int[])jclo.getInts("field"));
    }

    @Test
    public void testGetFloats() {
        Object obj = new Object() {
            float[] field = new float[]{1.0f, 2.0f, 3.0f};
        };
        JCLO jclo = new JCLO(obj);
        assertArrayEquals(new float[]{1.0f, 2.0f, 3.0f}, (float[])jclo.getFloats("field"), 0.001f);
    }

    @Test
    public void testGetDoubles() {
        Object obj = new Object() {
            double[] field = new double[]{1.0, 2.0, 3.0};
        };
        JCLO jclo = new JCLO(obj);
        assertArrayEquals(new double[]{1.0, 2.0, 3.0}, (double[])jclo.getDoubles("field"), 0.001);
    }

    @Test
    public void testGetLongs() {
        Object obj = new Object() {
            long[] field = new long[]{1L, 2L, 3L};
        };
        JCLO jclo = new JCLO(obj);
        assertArrayEquals(new long[]{1L, 2L, 3L}, (long[])jclo.getLongs("field"));
    }

    @Test
    public void testGetStringArray() {
        Object obj = new Object() {
            String[] field = new String[]{"value1", "value2"};
        };
        JCLO jclo = new JCLO(obj);
        assertArrayEquals(new String[]{"value1", "value2"}, (String[])jclo.getStrings("field"));
    }
}