import static org.junit.Assert.*;

import java.lang.reflect.Field;

import edu.mscd.cs.jclo.JCLO;

public class JCLOTest {

    @Test
    public void testJCLOConstructorWithNoArguments() {
        JCLO jclo = new JCLO(null);
        assertNotNull(jclo.object);
        assertNull(jclo.prefix);
        assertNull(jclo.aliases);
        assertNotNull(jclo.fields);
        for (Field field : jclo.fields) {
            assertFalse(Modifier.isFinal(field.getModifiers()));
            assertFalse(field.isAccessible(jclo));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOConstructorWithNullObject() {
        new JCLO(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOConstructorWithNullAliases() {
        new JCLO(new Object(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOConstructorWithNullPrefix() {
        new JCLO(null, new Object(), null);
    }

    @Test
    public void testJCLOConstructorWithPrefixAndKeyClash() {
        String prefix = "myPrefix";
        JCLO jclo = new JCLO(prefix, new Object(), new String[][]{{"prefix", "newValue"}, {"name", "John Doe"}});
        Field prefixField = jclo.getField("prefix");
        Field nameField = jclo.getField("name");
        assertNotNull(prefixField);
        assertEquals(prefix, prefixField.getName());
        assertNotNull(nameField);
        assertEquals("name", nameField.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testgetValueWithNonExistentKey() {
        JCLO jclo = new JCLO(new Object(), new String[][]{{"name", "John Doe"}});
        jclo.getValue("nonExistentKey");
    }

    @Test
    public void testgetValueWithArrayFieldAndIndexOutOfBounds() {
        JCLO jclo = new JCLO(new Object(), new String[][]{{"name", "John Doe"}, {"array", new int[2]}});
        Field arrayField = jclo.getField("array");
        Object array = jclo.getValue("array");
        assertArrayEquals(new int[2], array);
        try {
            jclo.setObject(arrayField, new int[3]);
            fail("Should have thrown an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testgetValueWithNonExistentField() {
        JCLO jclo = new JCLO(new Object(), new String[][]{{"name", "John Doe"}});
        jclo.getValue("nonExistentField");
    }

    @Test
    public void testgetValueWithBooleanField() {
        JCLO jclo = new JCLO(new Object(), new String[][]{{"booleanField", "true"}});
        assertTrue(jclo.getBoolean("booleanField"));
    }

    @Test
    public void testgetValueWithByteField() {
        JCLO jclo = new JCLO(new Object(), new String[][]{{"byteField", "5"}});
        assertEquals((byte) 5, jclo.getByte("byteField"));
    }

    @Test
    public void testgetValueWithCharField() {
        JCLO jclo = new JCLO(new Object(), new String[][]{{"charField", "A"}});
        assertEquals('A', jclo.getChar("charField"));
    }

    @Test
    public void testgetValueWithShortField() {
        JCLO jclo = new JCLO(new Object(), new String[][]{{"shortField", "10"}});
        assertEquals((short) 10, jclo.getShort("shortField"));
    }

    @Test
    public void testgetValueWithIntField() {
        JCLO jclo = new JCLO(new Object(), new String[][]{{"intField", "20"}});
        assertEquals(20, jclo.getInt("intField"));
    }

    @Test
    public void testgetValueWithFloatField() {
        JCLO jclo = new JCLO(new Object(), new String[][]{{"floatField", "3.14"}});
        assertEquals(3.14f, jclo.getFloat("floatField"), 0.01f);
    }

    @Test
    public void testgetValueWithDoubleField() {
        JCLO jclo = new JCLO(new Object(), new String[][]{{"doubleField", "4.56"}});
        assertEquals(4.56, jclo.getDouble("doubleField"), 0.01);
    }

    @Test
    public void testgetValueWithLongField() {
        JCLO jclo = new JCLO(new Object(), new String[][]{{"longField", "5L"}});
        assertEquals(5L, jclo.getLong("longField"));
    }

    @Test
    public void testgetValueWithStringField() {
        JCLO jclo = new JCLO(new Object(), new String[][]{{"stringField", "Hello World"}});
        assertEquals("Hello World", jclo.getString("stringField"));
    }

    @Test
    public void testgetValueWithArrayField() {
        JCLO jclo = new JCLO(new Object(), new String[][]{{"arrayField", "[1, 2, 3]"}});
        Object array = jclo.getValue("arrayField");
        assertArrayEquals(new int[]{1, 2, 3}, array);
    }

    @Test
    public void testgetValueWithObjectField() {
        JCLO jclo = new JCLO(new Object(), new String[][]{{"objectField", "new MyClass()"}});
        Object obj = jclo.getValue("objectField");
        assertTrue(obj instanceof MyClass);
    }

    @Test
    public void testparse() {
        String[] args = new String[]{"--doubleDashes", "--hasEquals", "--name", "John Doe", "--intField=3", "--floatField=4.5", "--booleanField=true"};
        JCLO jclo = new JCLO(null);
        jclo.parse(args);
        assertEquals("John Doe", jclo.getString("name"));
        assertEquals(3, jclo.getInt("intField"));
        assertEquals(4.5, jclo.getFloat("floatField"), 0.01);
        assertTrue(jclo.getBoolean("booleanField"));
    }
}