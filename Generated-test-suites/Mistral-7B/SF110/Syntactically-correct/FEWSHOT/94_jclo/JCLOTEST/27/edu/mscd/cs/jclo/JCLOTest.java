package edu.mscd.cs.jclo;

import static org.junit.Assert.*;
import org.junit.Test;

public class JCLOTest {

    @Test
    public void testJCLOInstance() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        assertNotNull(jclo.fields);
        assertEquals(obj.getClass(), jclo.object.getClass());
        assertEquals(0, jclo.fields.length);
    }

    @Test
    public void testJCLOInstanceWithPrefix() {
        Object obj = new Object();
        String prefix = "prefix_";
        JCLO jclo = new JCLO(prefix, obj);
        assertEquals(prefix, jclo.prefix);
        assertEquals(obj.getClass(), jclo.object.getClass());
        assertEquals(0, jclo.fields.length);
    }

    @Test
    public void testJCLOInstanceWithAliases() {
        Object obj = new Object() {{
            String[][] aliases = new String[1][2];
            aliases[0][0] = "alias1";
            aliases[0][1] = "alias2";
        }};
        JCLO jclo = new JCLO(obj, aliases);
        assertNotNull(jclo.aliases);
        assertEquals("alias1", jclo.aliases[0][0]);
        assertEquals("alias2", jclo.aliases[0][1]);
        assertEquals(obj.getClass(), jclo.object.getClass());
        assertEquals(0, jclo.fields.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOInstanceWithNullObject() {
        new JCLO(null);
    }

    @Test
    public void testGetObject() {
        Object obj = new Object();
        String key = "field1";
        Field field = new Field("field1", Integer.TYPE, null) {{
            setAccessible(true);
        }};
        Object instance = new JCLO(obj) {{
            fields = new Field[]{field};
        }};
        Object value = instance.getObject(field);
        assertEquals(obj, instance.object);
        assertNull(value);

        field.setInt(obj, 1);
        value = instance.getObject(field);
        assertEquals(Integer.valueOf(1), value);
    }

    @Test
    public void testGetField() {
        Object obj = new Object() {{
            int field1;
        }};
        String key = "field1";
        Field field = new Field("field1", Integer.TYPE, null) {{
            setAccessible(true);
            set(obj, Integer.valueOf(1));
        }};
        Object instance = new JCLO(obj) {{
            fields = new Field[]{field};
        }};
        Field returnedField = instance.getField(key);
        assertEquals(field, returnedField);
    }

    @Test
    public void testGetValue() {
        Object obj = new Object() {{
            int field1 = 1;
        }};
        String key = "field1";
        Field field = new Field("field1", Integer.TYPE, null) {{
            setAccessible(true);
        }};
        Object instance = new JCLO(obj) {{
            fields = new Field[]{field};
        }};
        Object value = instance.getValue(key);
        assertEquals(Integer.valueOf(1), value);
    }

    @Test
    public void testSetObject() {
        Object obj = new Object() {{
            int field1 = 1;
        }};
        String key = "field1";
        Field field = new Field("field1", Integer.TYPE, null) {{
            setAccessible(true);
        }};
        Object value = Integer.valueOf(2);
        Object instance = new JCLO(obj) {{
            fields = new Field[]{field};
        }};
        instance.setObject(field, value);
        assertEquals(Integer.valueOf(2), field.get(obj));
    }

    @Test
    public void testGetArrayType() {
        Class intType = Integer.TYPE;
        assertEquals("int[]", JCLO.getArrayType(intType));
    }

    @Test
    public void testToString() {
        Object obj = new Object() {{
            int field1 = 1;
            int[] field2 = {1, 2, 3};
        }};
        String prefix = "";
        String[][] aliases = null;
        Object instance = new JCLO(prefix, obj, aliases) {{
            fields = new Field[]{
                    new Field("field1", Integer.TYPE, null) {{
                        setAccessible(true);
                    }},
                    new Field("field2", int[].class, null) {{
                        setAccessible(true);
                    }}
            };
        }};
        String expectedOutput = "int: field1 = 1\nint[]: field2 = [1, 2, 3]";
        assertEquals(expectedOutput, instance.toString());
    }

    @Test
    public void testGetUsageType() {
        Class intType = Integer.TYPE;
        assertEquals(" int", JCLO.getUsageType(intType));
        assertEquals("int[]", JCLO.getUsageType(int[].class));
    }

    @Test
    public void testUsage() {
        Object obj = new Object() {{
            int field1 = 1;
            int field2 = 2;
        }};
        String prefix = "";
        Object instance = new JCLO(prefix, obj) {{
            fields = new Field[]{
                    new Field("field1", Integer.TYPE, null) {{
                        setAccessible(true);
                    }},
                    new Field("field2", Integer.TYPE, null) {{
                        setAccessible(true);
                    }}
            };
        }};
        String expectedOutput = "--field1\n--field2\n";
        assertEquals(expectedOutput, instance.usage());
    }

    @Test
    public void testParseAdditional() {
        Object obj = new Object() {{
            String[] additional;
        }};
        String[] args = {"--additional", "arg1", "arg2"};
        int i = 0;
        JCLO instance = new JCLO(obj) {{
            fields = new Field[]{
                    new Field("additional", String[].class, null) {{
                        setAccessible(true);
                    }}
            };
        }};
        instance.parseAdditional(args, i);
        String[] expectedAdditional = {"arg1", "arg2"};
        assertArrayEquals(expectedAdditional, ((String[])instance.getObject(instance.getField("additional"))));
    }

    @Test
    public void testAddToArray() {
        Object obj = new Object() {{
            int[] field1 = new int[0];
        }};
        Object newElement = Integer.valueOf(1);
        Field field = new Field("field1", int[].class, null) {{
            setAccessible(true);
        }};
        Object instance = new JCLO(obj) {{
            fields = new Field[]{field};
        }};
        Object result = instance.addToArray(field, newElement);
        assertEquals(Integer.TYPE.getComponentType(), result.getClass().getComponentType());
        assertEquals(1, Array.getLength(result));
        assertEquals(Integer.valueOf(1), Array.get(result, 0));
    }

    @Test
    public void testParse() throws Exception {
        Object obj = new Object() {{
            int field1 = 0;
            int field2 = 0;
            int[] field3 = new int[0];
        }};
        String[] args = {
                "--field1=1",
                "--field2=2",
                "--field3=3",
                "--field3=4",
                "--field3=5"
        };
        JCLO instance = new JCLO(obj) {{
            fields = new Field[]{
                    new Field("field1", Integer.TYPE, null) {{
                        setAccessible(true);
                    }},
                    new Field("field2", Integer.TYPE, null) {{
                        setAccessible(true);
                    }},
                    new Field("field3", int[].class, null) {{
                        setAccessible(true);
                    }}
            };
        }};
        instance.parse(args);
        assertEquals(1, instance.getField("field1").get(obj).intValue());
        assertEquals(2, instance.getField("field2").get(obj).intValue());
        int[] expectedField3 = {3, 4, 5};
        assertArrayEquals(expectedField3, ((int[])instance.getObject(instance.getField("field3"))));
    }

    @Test
    public void testGetters() {
        Object obj = new Object() {{
            int field1 = 1;
        }};
        String key = "field1";
        JCLO instance = new JCLO(obj) {{
            fields = new Field[]{
                    new Field("field1", Integer.TYPE, null) {{
                        setAccessible(true);
                    }}
            };
        }};
        assertEquals(1, instance.getInt(key));
    }

    @Test
    public void testGettersForArrays() {
        Object obj = new Object() {{
            int[] field1 = {1, 2, 3};
        }};
        String key = "field1";
        JCLO instance = new JCLO(obj) {{
            fields = new Field[]{
                    new Field("field1", int[].class, null) {{
                        setAccessible(true);
                    }}
            };
        }};
        int[] expected = {1, 2, 3};
        assertArrayEquals(expected, instance.getInts(key));
    }
}