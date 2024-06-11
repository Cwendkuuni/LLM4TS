import org.junit.Test;
import org.junit.Assert.*;
import edu.mscd.cs.jclo.*;

public class JCLOTest {

    @Test
    public void testJCLOConstructorWithObject() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNotNull(jclo.fields);
        assertNotNull(jclo.object);
        assertEquals(1, jclo.fields.length);
    }

    @Test
    public void testJCLOConstructorWithObjectAndAliases() {
        Object object = new Object();
        String[][] aliases = new String[][]{{"a", "aliasA"}, {"b", "aliasB"}};
        JCLO jclo = new JCLO(object, aliases);
        assertNotNull(jclo.fields);
        assertNotNull(jclo.object);
        assertNotNull(jclo.aliases);
        assertEquals(2, jclo.fields.length);
    }

    @Test
    public void testJCLOConstructorWithPrefixAndObject() {
        Object object = new Object();
        String prefix = "prefix_";
        JCLO jclo = new JCLO(prefix, object);
        assertNotNull(jclo.prefix);
        assertNotNull(jclo.object);
        assertEquals("prefix_", jclo.prefix);
    }

    @Test
    public void testJCLOConstructorWithPrefixAndObjectAndAliases() {
        Object object = new Object();
        String prefix = "prefix_";
        String[][] aliases = new String[][]{{"a", "aliasA"}, {"b", "aliasB"}};
        JCLO jclo = new JCLO(prefix, object, aliases);
        assertNotNull(jclo.prefix);
        assertNotNull(jclo.object);
        assertNotNull(jclo.aliases);
        assertEquals("prefix_", jclo.prefix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOGetValueWithNullKey() {
        JCLO jclo = new JCLO(new Object());
        jclo.getValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOGetValueWithUnknownKey() {
        JCLO jclo = new JCLO(new Object());
        jclo.setValue("unknownKey", "unknownValue");
        jclo.getValue("unknownKey");
    }

    @Test
    public void testJCLOGetValueWithBooleanKey() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.setValue("booleanKey", true);
        assertTrue(jclo.getBoolean("booleanKey"));
    }

    @Test
    public void testJCLOGetValueWithByteKey() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.setValue("byteKey", (byte) 1);
        assertEquals((byte) 1, jclo.getByte("byteKey"));
    }

    @Test
    public void testJCLOGetValueWithCharKey() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.setValue("charKey", 'A');
        assertEquals('A', jclo.getChar("charKey"));
    }

    @Test
    public void testJCLOGetValueWithShortKey() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.setValue("shortKey", (short) 1);
        assertEquals((short) 1, jclo.getShort("shortKey"));
    }

    @Test
    public void testJCLOGetValueWithIntKey() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.setValue("intKey", 1);
        assertEquals(1, jclo.getInt("intKey"));
    }

    @Test
    public void testJCLOGetValueWithFloatKey() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.setValue("floatKey", 1.0f);
        assertEquals(1.0f, jclo.getFloat("floatKey"), 0.001f);
    }

    @Test
    public void testJCLOGetValueWithDoubleKey() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.setValue("doubleKey", 1.0);
        assertEquals(1.0, jclo.getDouble("doubleKey"), 0.001);
    }

    @Test
    public void testJCLOGetValueWithLongKey() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.setValue("longKey", 1L);
        assertEquals(1L, jclo.getLong("longKey"));
    }

    @Test
    public void testJCLOGetValueWithStringKey() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.setValue("stringKey", "test");
        assertEquals("test", jclo.getString("stringKey"));
    }

    @Test(expected = IllegalAccessException.class)
    public void testJCLOGetValueWithInaccessibleField() {
        Object obj = new Object() {
            private int inaccessibleValue = 1;
        };
        JCLO jclo = new JCLO(obj);
        jclo.getValue("inaccessibleValue");
    }

    @Test
    public void testJCLOSetValueWithBooleanKey() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.setValue("booleanKey", true);
        assertTrue(jclo.getBoolean("booleanKey"));
        jclo.setValue("booleanKey", false);
        assertFalse(jclo.getBoolean("booleanKey"));
    }

    @Test
    public void testJCLOSetValueWithByteKey() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.setValue("byteKey", (byte) 1);
        assertEquals((byte) 1, jclo.getByte("byteKey"));
        jclo.setValue("byteKey", (byte) -1);
        assertEquals((byte) -1, jclo.getByte("byteKey"));
    }

    @Test
    public void testJCLOSetValueWithCharKey() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.setValue("charKey", 'A');
        assertEquals('A', jclo.getChar("charKey"));
        jclo.setValue("charKey", 'Z');
        assertEquals('Z', jclo.getChar("charKey"));
    }

    @Test
    public void testJCLOSetValueWithShortKey() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.setValue("shortKey", (short) 1);
        assertEquals((short) 1, jclo.getShort("shortKey"));
        jclo.setValue("shortKey", Short.MAX_VALUE);
        assertEquals(Short.MAX_VALUE, jclo.getShort("shortKey"));
    }

    @Test
    public void testJCLOSetValueWithIntKey() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.setValue("intKey", 1);
        assertEquals(1, jclo.getInt("intKey"));
        jclo.setValue("intKey", Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, jclo.getInt("intKey"));
    }

    @Test
    public void testJCLOSetValueWithFloatKey() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.setValue("floatKey", 1.0f);
        assertEquals(1.0f, jclo.getFloat("floatKey"), 0.001f);
        jclo.setValue("floatKey", Float.POSITIVE_INFINITY);
        assertEquals(Float.POSITIVE_INFINITY, jclo.getFloat("floatKey"), 0.001f);
    }

    @Test
    public void testJCLOSetValueWithDoubleKey() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.setValue("doubleKey", 1.0);
        assertEquals(1.0, jclo.getDouble("doubleKey"), 0.001);
        jclo.setValue("doubleKey", Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, jclo.getDouble("doubleKey"), 0.001);
    }

    @Test
    public void testJCLOSetValueWithLongKey() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.setValue("longKey", 1L);
        assertEquals(1L, jclo.getLong("longKey"));
        jclo.setValue("longKey", Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, jclo.getLong("longKey"));
    }

    @Test
    public void testJCLOSetValueWithStringKey() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.setValue("stringKey", "test");
        assertEquals("test", jclo.getString("stringKey"));
        jclo.setValue("stringKey", "new test");
        assertEquals("new test", jclo.getString("stringKey"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOSetValueWithNullKey() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.setValue(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOSetValueWithUnknownKey() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.setValue("unknownKey", "value");
    }

    @Test(expected = IllegalAccessException.class)
    public void testJCLOSetValueWithInaccessibleField() {
        Object obj = new Object() {
            private int inaccessibleValue = 1;
        };
        JCLO jclo = new JCLO(obj);
        jclo.setValue("inaccessibleValue", "value");
    }

    @Test
    public void testJCLOUsage() {
        JCLO jclo = new JCLO(new Object());
        jclo.parse(new String[]{"--booleanKey=true", "--byteKey=1", "--charKey=A", "--shortKey=1", "--intKey=1", "--floatKey=1.0", "--doubleKey=1.0", "--longKey=1"});
        String usage = jclo.usage();
        String expectedUsage = "--booleanKey: boolean\n" +
                "--byteKey: byte\n" +
                "--charKey: char\n" +
                "--shortKey: short\n" +
                "--intKey: int\n" +
                "--floatKey: float\n" +
                "--doubleKey: double\n" +
                "--longKey: long\n";
        assertEquals(expectedUsage, usage);
    }

    @Test
    public void testJCLOParseWithUnknownOption() {
        JCLO jclo = new JCLO(new Object());
        jclo.parse(new String[]{"--unknownOption=value"});
        String usage = jclo.usage();
        String expectedUsage = "--booleanKey: boolean\n" +
                "--byteKey: byte\n" +
                "--charKey: char\n" +
                "--shortKey: short\n" +
                "--intKey: int\n" +
                "--floatKey: float\n" +
                "--doubleKey: double\n" +
                "--longKey: long\n";
        assertEquals(expectedUsage, usage);
        assertFalse(jclo.getBoolean("unknownOption"));
    }

    @Test
    public void testJCLOParseWithAlias() {
        JCLO jclo = new JCLO(new Object(), new String[][]{"--a=aliasA", "--b=aliasB"});
        jclo.parse(new String[]{"--aliasA=valueA", "--aliasB=valueB"});
        String usage = jclo.usage();
        String expectedUsage = "--booleanKey: boolean\n" +
                "--byteKey: byte\n" +
                "--charKey: char\n" +
                "--shortKey: short\n" +
                "--intKey: int\n" +
                "--floatKey: float\n" +
                "--doubleKey: double\n" +
                "--longKey: long\n" +
                "--a: aliasA\n" +
                "--b: aliasB\n";
        assertEquals(expectedUsage, usage);
        assertTrue(jclo.getBoolean("aliasA"));
        assertEquals("valueA", jclo.getString("aliasA"));
        assertTrue(jclo.getBoolean("aliasB"));
        assertEquals("valueB", jclo.getString("aliasB"));
    }

    @Test
    public void testJCLOParseWithMultipleValuesForArray() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.parse(new String[]{"--arrayKey=[1,2,3]"});
        Object value = jclo.getValue("arrayKey");
        assertArrayEquals(new int[]{1, 2, 3}, (int[])value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOParseWithInvalidArrayValue() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.parse(new String[]{"--arrayKey=[1,2,3,invalid]"});
    }

    @Test
    public void testJCLOParseWithBooleanValues() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        jclo.parse(new String[]{"--booleanKey=true", "--booleanKey=false"});
        assertTrue(jclo.getBoolean("booleanKey"));
        assertFalse(jclo.getBoolean("booleanKey"));
    }
}