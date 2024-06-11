package edu.mscd.cs.jclo;

import static org.junit.Assert.*;
import org.junit.Test;

public class JCLOTest {

    @Test
    public void testJCLOInstance() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        assertNotNull(jclo.object);
        assertEquals(obj.getClass(), jclo.object.getClass());
    }

    @Test
    public void testJCLOInstanceWithPrefix() {
        Object obj = new Object();
        String prefix = "prefix_";
        JCLO jclo = new JCLO(prefix, obj);
        assertNotNull(jclo.prefix);
        assertEquals(prefix, jclo.prefix);
    }

    @Test
    public void testJCLOInstanceWithPrefixAndAliases() {
        Object obj = new Object();
        String prefix = "prefix_";
        String[][] aliases = {{ "alias1", "alias2" }, { "alias3", "alias4" }};
        JCLO jclo = new JCLO(prefix, obj, aliases);
        assertNotNull(jclo.prefix);
        assertEquals(prefix, jclo.prefix);
        assertArrayEquals(aliases, jclo.aliases);
    }

    @Test
    public void testGetObject() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        Field field = new Field("field", int.class);
        jclo.fields = new Field[] { field };
        jclo.setAccessible(true);
        jclo.setObject(field, 5);
        assertEquals(5, jclo.getObject(field));
    }

    @Test
    public void testGetField() {
        Object obj = new Object();
        Field field = new Field("field", int.class);
        JCLO jclo = new JCLO(obj, new String[][] { { "field", "alias" } });
        assertEquals(field, jclo.getField("field"));
        assertEquals(field, jclo.getField("alias"));
        assertNull(jclo.getField("unknown"));
    }

    @Test
    public void testGetValue() {
        Object obj = new Object();
        Field field = new Field("field", int.class);
        Object value = 5;
        JCLO jclo = new JCLO(obj, new String[][] { { "field", "alias" } });
        jclo.fields = new Field[] { field };
        jclo.setObject(field, value);
        assertEquals(value, jclo.getValue("field"));
        assertEquals(value, jclo.getValue("alias"));
    }

    @Test
    public void testToString() {
        Object obj = new Object();
        Field[] fields = new Field[2];
        fields[0] = new Field("field1", int.class);
        fields[1] = new Field("field2", String[].class);
        JCLO jclo = new JCLO(obj, fields);
        jclo.setObject(fields[0], 5);
        jclo.setObject(fields[1], new String[] { "value1", "value2" });
        String expected = "int: field1 = 5\n" + "class [Ljava.lang.String;: field2 = [value1, value2]";
        assertEquals(expected, jclo.toString());
    }

    @Test
    public void testUsage() {
        Object obj = new Object();
        Field[] fields = new Field[2];
        fields[0] = new Field("field1", boolean.class, true);
        fields[1] = new Field("field2", int[].class);
        JCLO jclo = new JCLO("", obj, fields);
        String expected = "--field1\n" + "--field2=[int]\n";
        assertEquals(expected, jclo.usage());
    }

    @Test
    public void testParseAdditional() {
        Object obj = new Object();
        Field field = new Field("additional", String[].class);
        JCLO jclo = new JCLO(obj);
        String[] args = { "--additional", "value1", "value2" };
        jclo.parseAdditional(args, 0);
        Object additional = jclo.getObject(field);
        String[] expected = { "value1", "value2" };
        assertArrayEquals(expected, (Object[])additional);
    }

    @Test
    public void testParse() {
        Object obj = new Object();
        Field[] fields = new Field[2];
        fields[0] = new Field("field1", boolean.class);
        fields[1] = new Field("field2", int[].class);
        JCLO jclo = new JCLO("", obj, fields);
        String[] args = { "--field1=false", "--field2=1,2,3" };
        jclo.parse(args);
        assertEquals(false, jclo.getBoolean("field1"));
        Object field2 = jclo.getObject(fields[1]);
        assertArrayEquals(new int[] { 1, 2, 3 }, (int[])field2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidKey() {
        Object obj = new Object();
        Field[] fields = new Field[1];
        fields[0] = new Field("field1", int.class);
        JCLO jclo = new JCLO("", obj, fields);
        String[] args = { "--unknown" };
        jclo.parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidValue() {
        Object obj = new Object();
        Field[] fields = new Field[1];
        fields[0] = new Field("field1", int.class);
        JCLO jclo = new JCLO("", obj, fields);
        String[] args = { "--field1=unknown" };
        jclo.parse(args);
    }
}