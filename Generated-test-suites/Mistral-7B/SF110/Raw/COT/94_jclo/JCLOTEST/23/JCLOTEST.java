import org.junit.Test;
import org.junit.Assert.*;
import edu.mscd.cs.jclo.*;

public class JCLOTest {

    @Test
    public void testJCLOConstructor1() {
        JCLO jclo = new JCLO(new Object());
        assertNotNull(jclo.fields);
        assertNotNull(jclo.object);
        assertNull(jclo.prefix);
        assertNull(jclo.aliases);
    }

    @Test
    public void testJCLOConstructor2() {
        String[][] aliases = new String[][]{{"a", "b"}, {"c", "d"}};
        JCLO jclo = new JCLO(new Object(), aliases);
        assertNotNull(jclo.fields);
        assertNotNull(jclo.object);
        assertNotNull(jclo.prefix);
        assertEquals(aliases, jclo.aliases);
    }

    @Test
    public void testJCLOConstructor3() {
        String prefix = "prefix";
        JCLO jclo = new JCLO(prefix, new Object());
        assertNotNull(jclo.fields);
        assertNotNull(jclo.object);
        assertEquals(prefix, jclo.prefix);
        assertNull(jclo.aliases);
    }

    @Test
    public void testJCLOConstructor4() {
        String prefix = "prefix";
        Object object = new Object();
        String[][] aliases = new String[][]{{"a", "b"}, {"c", "d"}};
        JCLO jclo = new JCLO(prefix, object, aliases);
        assertNotNull(jclo.fields);
        assertNotNull(jclo.object);
        assertEquals(prefix, jclo.prefix);
        assertEquals(aliases, jclo.aliases);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueNullField() {
        JCLO jclo = new JCLO(new Object());
        String key = "nonExistentField";
        jclo.getValue(key);
    }

    @Test(expected = IllegalAccessException.class)
    public void testGetObjectIllegalAccess() {
        JCLO jclo = new JCLO(new Object());
        Field field = new Field("nonAccessibleField", int.class, null);
        jclo.setField(field);
        jclo.getObject(field);
    }

    @Test
    public void testUsage() {
        JCLO jclo = new JCLO(new Object());
        String expectedUsage = "-boolean--boolean:\n" +
                "-byte--byte:\n" +
                "-char--char:\n" +
                "-short--short:\n" +
                "-int--int:\n" +
                "-float--float:\n" +
                "-double--double:\n" +
                "-long--long:\n" +
                "-java.lang.String--:\n" +
                "--additional=arg1 arg2 arg3";
        String actualUsage = jclo.usage();
        assertEquals(expectedUsage, actualUsage);
    }

    @Test
    public void testParse() {
        String[] args = {"--boolean", "true", "--byte", "42", "--char", "a", "--short", "32767", "--int", "1234567890",
                "--float", "3.1415926536", "--double", "4.1415926536", "--long", "123456789012345", "--java.lang.String", "Hello"};
        JCLO jclo = new JCLO(new Object());
        jclo.parse(args);
        assertTrue(jclo.getBoolean("boolean"));
        assertEquals((byte) 42, jclo.getByte("byte"));
        assertEquals((char) 'a', jclo.getChar("char"));
        assertEquals((short) 32767, jclo.getShort("short"));
        assertEquals(1234567890, jclo.getInt("int"));
        assertEquals(3.1415926536f, jclo.getFloat("float"), 0.001f);
        assertEquals(4.1415926536d, jclo.getDouble("double"), 0.001d);
        assertEquals(123456789012345L, jclo.getLong("long"));
        assertEquals("Hello", jclo.getString("java.lang.String"));
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, (String[]) jclo.getObject("additional"));

        String[] args2 = {"--boolean=false", "--byte=-128", "--char=z", "--short=-32768", "--int=-2147483648", "--float=-3.1415926536",
                "--double=-4.1415926536", "--long=-9223372036854775808", "--java.lang.String=-This is a test string"};
        jclo.parse(args2);
        assertFalse(jclo.getBoolean("boolean"));
        assertEquals((byte) -128, jclo.getByte("byte"));
        assertEquals((char) 'z', jclo.getChar("char"));
        assertEquals((short) -32768, jclo.getShort("short"));
        assertEquals(-2147483648, jclo.getInt("int"));
        assertEquals(-3.1415926536f, jclo.getFloat("float"), 0.001f);
        assertEquals(-4.1415926536d, jclo.getDouble("double"), 0.001d);
        assertEquals(-9223372036854775808L, jclo.getLong("long"));
        assertEquals("-This is a test string", jclo.getString("java.lang.String"));
    }

    @Test
    public void testGetArrayType() {
        Class type = int[].class;
        assertEquals("int[]", JCLO.getArrayType(type));
    }
}