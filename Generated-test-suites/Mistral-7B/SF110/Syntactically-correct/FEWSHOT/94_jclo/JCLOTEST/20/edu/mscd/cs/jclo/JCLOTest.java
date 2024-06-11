package edu.mscd.cs.jclo;

import static org.junit.Assert.*;
import org.junit.Test;

public class JCLOTest {

    @Test
    public void testJCLOCreation() {
        JCLO jclo = new JCLO(new Object());
        assertNotNull(jclo);

        JCLO jcloWithPrefix = new JCLO("prefix", new Object());
        assertNotNull(jcloWithPrefix);
        assertEquals("prefix", jcloWithPrefix.prefix);

        JCLO jcloWithAliases = new JCLO(new Object(), new String[][]{{"a", "aliasA"}, {"b", "aliasB"}});
        assertNotNull(jcloWithAliases);
        assertEquals("aliasA", jcloWithAliases.getField("a").getName());
        assertEquals("aliasB", jcloWithAliases.getField("b").getName());
    }

    @Test
    public void testGetValue() {
        Object testObject = new Object();
        Field field = new Field("testField", Integer.TYPE);
        JCLO jclo = new JCLO(testObject, new Field[]{field});

        jclo.setObject(field, 42);
        assertEquals(42, jclo.getValue("testField"));

        jclo = new JCLO("prefix", testObject, new Field[]{field});
        assertEquals("prefix.testField", jclo.getValue("testField").toString());
    }

    @Test
    public void testUsage() {
        Object testObject = new Object();
        Field[] fields = new Field[]{
                new Field("booleanField", Boolean.TYPE),
                new Field("byteField", Byte.TYPE),
                new Field("shortField", Short.TYPE),
                new Field("intField", Integer.TYPE),
                new Field("floatField", Float.TYPE),
                new Field("doubleField", Double.TYPE),
                new Field("longField", Long.TYPE),
                new Field("stringField", String.class),
                new Field("charField", Character.TYPE),
                new Field("shortsField", Short[].class),
                new Field("intsField", Int[].class),
                new Field("floatsField", Float[].class),
                new Field("doublesField", Double[].class),
                new Field("longsField", Long[].class),
                new Field("stringsField", String[].class),
                new Field("additional", Object[].class),
                new Field("hiddenField", Integer.TYPE, Modifier.FINAL)
        };
        JCLO jclo = new JCLO(testObject, fields);

        String usage = jclo.usage();
        String expectedUsage = "" +
                "--booleanField=<boolean>\n" +
                "--byteField=<byte>\n" +
                "--shortField=<short>\n" +
                "--intField=<int>\n" +
                "--floatField=<float>\n" +
                "--doubleField=<double>\n" +
                "--longField=<long>\n" +
                "--stringField=<java.lang.String>\n" +
                "--charField=<char>\n" +
                "--shortsField=<short...>\n" +
                "--intsField=<int...>\n" +
                "--floatsField=<float...>\n" +
                "--doublesField=<double...>\n" +
                "--longsField=<long...>\n" +
                "--stringsField=<java.lang.String...>\n" +
                "--additional=<java.lang.Object...>\n";
        assertEquals(expectedUsage, usage);
    }

    @Test
    public void testParse() {
        Object testObject = new Object();
        Field[] fields = new Field[]{
                new Field("booleanField", Boolean.TYPE),
                new Field("byteField", Byte.TYPE),
                new Field("shortField", Short.TYPE),
                new Field("intField", Integer.TYPE),
                new Field("floatField", Float.TYPE),
                new Field("doubleField", Double.TYPE),
                new Field("longField", Long.TYPE),
                new Field("stringField", String.class),
                new Field("charField", Character.TYPE),
                new Field("shortsField", Short[].class, Modifier.FINAL),
                new Field("intsField", Int[].class),
                new Field("floatsField", Float[].class),
                new Field("doublesField", Double[].class),
                new Field("longsField", Long[].class),
                new Field("stringsField", String[].class),
                new Field("additional", Object[].class),
                new Field("hiddenField", Integer.TYPE, Modifier.FINAL)
        };
        JCLO jclo = new JCLO(testObject, fields);

        String[] args = {"--booleanField=true", "--byteField=127", "--shortField=32767",
                "--intField=2147483647", "--floatField=3.14159265", "--doubleField=4.14159265E+09",
                "--longField=9223372036854775807", "--stringField=\"string value\"",
                "--charField=z", "--shortsField=-128,0,127", "--intsField=0,1,2,3,4,5,6,7,8,9",
                "--floatsField=1.5,2.5,3.5,4.5,5.5,6.5,7.5,8.5,9.5", "--doublesField=1.6E-324,2.6E-324,3.6E-324,4.6E-324,5.6E-324,6.6E-324,7.6E-324,8.6E-324,9.6E-324",
                "--longsField=1L,2L,3L,4L,5L,6L,7L,8L,9L", "--stringsField=\"string1,string2,string3,string4,string5,string6,string7,string8,string9\"",
                "--additional=object1,object2,object3"};
        jclo.parse(args);

        assertTrue(jclo.getBoolean("booleanField"));
        assertEquals((byte)127, jclo.getByte("byteField"));
        assertEquals((short)32767, jclo.getShort("shortField"));
        assertEquals(2147483647, jclo.getInt("intField"));
        assertEquals(3.14159265F, jclo.getFloat("floatField"));
        assertEquals(4.14159265E+09D, jclo.getDouble("doubleField"));
        assertEquals(9223372036854775807L, jclo.getLong("longField"));
        assertEquals("string value", jclo.getString("stringField"));
        assertEquals('z', jclo.getChar("charField"));
        Short[] shortsArray = (Short[])jclo.getShorts("shortsField");
        assertArrayEquals(new Short[]{-128, 0, 127}, shortsArray);
        Int[] intsArray = (Int[])jclo.getInts("intsField");
        assertArrayEquals(new Int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, intsArray);
        Float[] floatsArray = (Float[])jclo.getFloats("floatsField");
        assertArrayEquals(new Float[]{1.5F, 2.5F, 3.5F, 4.5F, 5.5F, 6.5F, 7.5F, 8.5F, 9.5F}, floatsArray);
        Double[] doublesArray = (Double[])jclo.getDoubles("doublesField");
        assertArrayEquals(new Double[]{1.6E-324D, 2.6E-324D, 3.6E-324D, 4.6E-324D, 5.6E-324D, 6.6E-324D, 7.6E-324D, 8.6E-324D, 9.6E-324D}, doublesArray);
        Long[] longsArray = (Long[])jclo.getLongs("longsField");
        assertArrayEquals(new Long[]{1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L}, longsArray);
        String[] stringsArray = (String[])jclo.getStrings("stringsField");
        assertArrayEquals(new String[]{"string1", "string2", "string3", "string4", "string5", "string6", "string7", "string8", "string9"}, stringsArray);
        Object[] additionalObjects = (Object[])jclo.getValues("additional");
        assertEquals(3, additionalObjects.length);
        assertNotNull(additionalObjects[0]);
        assertNotNull(additionalObjects[1]);
        assertNotNull(additionalObjects[2]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidArgument() {
        Object testObject = new Object();
        Field[] fields = new Field[]{
                new Field("booleanField", Boolean.TYPE),
                new Field("byteField", Byte.TYPE),
                new Field("shortField", Short.TYPE),
                new Field("intField", Integer.TYPE),
                new Field("floatField", Float.TYPE),
                new Field("doubleField", Double.TYPE),
                new Field("longField", Long.TYPE),
                new Field("stringField", String.class),
                new Field("charField", Character.TYPE),
                new Field("shortsField", Short[].class),
                new Field("intsField", Int[].class),
                new Field("floatsField", Float[].class),
                new Field("doublesField", Double[].class),
                new Field("longsField", Long[].class),
                new Field("stringsField", String[].class),
                new Field("additional", Object[].class),
                new Field("hiddenField", Integer.TYPE, Modifier.FINAL)
        };
        JCLO jclo = new JCLO(testObject, fields);

        String[] args = {"--invalid", "--booleanField=true"};
        jclo.parse(args);
        fail("Error not thrown");
    }
}