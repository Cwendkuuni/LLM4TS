package edu.mscd.cs.jclo;

import static org.junit.Assert.*;
import org.junit.Test;

public class JCLOTest {

    @Test
    public void testJCLO() {
        Object object = new Object() {
            int field1;
            String field2;
            char field3;
            boolean field4;
            int[] field5;
            String[] field6;
        };

        JCLO jclo1 = new JCLO(object);
        JCLO jclo2 = new JCLO(object, new String[][]{{"field2", "alias1"}, {"field5", "alias2"}});
        JCLO jclo3 = new JCLO("prefix", object);
        JCLO jclo4 = new JCLO("prefix", object, new String[][]{{"field2", "alias1"}, {"field5", "alias2"}});

        assertEquals(object, jclo1.getObject());
        assertEquals(object, jclo2.getObject());
        assertEquals(object, jclo3.getObject());
        assertEquals(object, jclo4.getObject());

        Field[] fields1 = jclo1.getFields();
        Field[] fields2 = jclo2.getFields();
        Field[] fields3 = jclo3.getFields();
        Field[] fields4 = jclo4.getFields();

        assertEquals(fields1.length, fields2.length);
        assertEquals(fields1.length, fields3.length);
        assertEquals(fields1.length, fields4.length);

        for (int i = 0; i < fields1.length; i++) {
            assertEquals(fields1[i].getName(), fields2[i].getName());
            assertEquals(fields1[i].getName(), fields3[i].getName());
            assertEquals(fields1[i].getName(), fields4[i].getName());
        }

        Object value1 = jclo1.getValue("field1");
        Object value2 = jclo2.getValue("field1");
        Object value3 = jclo3.getValue("field1");
        Object value4 = jclo4.getValue("field1");

        assertEquals(value1, value2);
        assertEquals(value1, value3);
        assertEquals(value1, value4);
        assertEquals(int.class, value1.getClass());

        value1 = jclo1.getValue("field2");
        value2 = jclo2.getValue("alias1");
        value3 = jclo3.getValue("field2");
        value4 = jclo4.getValue("alias1");

        assertEquals(value1, value3);
        assertEquals(value3, value4);
        assertEquals(String.class, value2.getClass());

        value1 = jclo1.getValue("field3");
        value2 = jclo2.getValue("field5");
        value3 = jclo3.getValue("field3");
        value4 = jclo4.getValue("alias2");

        assertEquals(value1, value3);
        assertNotEquals(value1, value2);
        assertNotEquals(value1, value4);
        assertEquals(char.class, value1.getClass());

        value1 = jclo1.getValue("field4");
        value2 = jclo2.getValue("field6");
        value3 = jclo3.getValue("field4");
        value4 = jclo4.getValue("alias1");

        assertFalse((Boolean) value1);
        assertArrayEquals(new String[]{}, (String[]) value2);
        assertTrue((Boolean) value3);
        assertNull(value4);

        Object[] array1 = jclo1.getValue("field5");
        Object[] array2 = jclo2.getValue("alias2");

        assertNotNull(array1);
        assertNotNull(array2);
        assertEquals(int.class, array1.getClass().getComponentType());
        assertEquals(array1.length, array2.length);

        for (int i = 0; i < array1.length; i++) {
            assertEquals(Integer.valueOf(i), array1[i]);
            assertEquals(Integer.valueOf(i), array2[i]);
        }

        jclo1.parse(new String[]{"--field1", "1", "--field2", "test"});
        jclo2.parse(new String[]{"--alias1", "test", "--field5", "1", "2"});
        jclo3.parse(new String[]{"prefix--field1", "1", "prefix--field2", "test"});
        jclo4.parse(new String[]{"prefix--alias1", "test", "prefix--field5", "1", "2"});

        assertEquals(1, jclo1.getInt("field1"));
        assertEquals("test", jclo1.getString("field2"));
        assertArrayEquals(new int[]{1}, (int[]) jclo1.getInts("field5"));

        assertEquals(1, jclo2.getInt("alias1"));
        assertEquals("test", jclo2.getString("field5"));
        assertArrayEquals(new int[]{1, 2}, (int[]) jclo2.getInts("field5"));

        assertEquals(1, jclo3.getInt("field1"));
        assertEquals("test", jclo3.getString("field2"));
        assertArrayEquals(new int[]{1}, (int[]) jclo3.getInts("field5"));

        assertEquals(1, jclo4.getInt("alias1"));
        assertEquals("test", jclo4.getString("field5"));
        assertArrayEquals(new int[]{1, 2}, (int[]) jclo4.getInts("field5"));
    }

    private Field[] getFields(JCLO jclo) {
        return jclo.getFields();
    }

    private void assertArrayEquals(Object expected, Object actual) {
        if (expected == null) {
            assertEquals(null, actual);
        } else if (expected.getClass().isArray()) {
            Assert.assertArrayEquals((Object[]) expected, (Object[]) actual);
        } else {
            assertEquals(expected.toString(), actual.toString());
        }
    }
}