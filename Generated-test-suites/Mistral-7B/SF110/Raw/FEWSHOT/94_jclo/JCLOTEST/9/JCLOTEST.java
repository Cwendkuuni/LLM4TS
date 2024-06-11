package edu.mscd.cs.jclo;

import static org.junit.Assert.*;
import org.junit.Test;

public class JCLOTest {

    @Test
    public void testJCLOConstruction() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        assertEquals(obj.getClass().getDeclaredFields().length, jclo.fields.length);

        Object objWithAliases = new Object() {
            String[][] aliases = {{ "alias1", "alias2" }, { "alias3", "alias4" }};
        };
        JCLO jcloWithAliases = new JCLO(objWithAliases, objWithAliases.aliases);
        assertEquals(objWithAliases.getClass().getDeclaredFields().length, jcloWithAliases.fields.length);
        assertEquals(objWithAliases.aliases.length, jcloWithAliases.aliases.length);

        String prefix = "prefix_";
        Object objWithPrefix = new Object() {
            String prefix = prefix;
        };
        JCLO jcloWithPrefix = new JCLO(prefix, objWithPrefix);
        assertEquals(prefix, jcloWithPrefix.prefix);
    }

    @Test
    public void testGetObject() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);

        Field field = obj.getClass().getDeclaredField("field1");
        field.setAccessible(true);
        Object fieldValue = new Object();
        try {
            field.set(obj, fieldValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        assertEquals(fieldValue, jclo.getObject(jclo.getField("field1")));
    }

    @Test
    public void testGetField() {
        Object obj = new Object() {
            String field1 = "value1";
            String field2 = "value2";
        };
        JCLO jclo = new JCLO(obj);

        assertEquals("field1", jclo.getField("field1").getName());
        assertEquals("field2", jclo.getField("field2").getName());
        assertNull(jclo.getField("nonExistentField"));
    }

    @Test
    public void testgetValue() {
        Object obj = new Object() {
            String field1 = "value1";
            int field2 = 1;
        };
        JCLO jclo = new JCLO(obj);

        assertEquals("value1", jclo.getValue("field1"));
        assertEquals(1, jclo.getValue("field2").intValue());
        assertNull(jclo.getValue("nonExistentField"));
    }

    @Test
    public void testSetObject() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);

        Field field = obj.getClass().getDeclaredField("field1");
        field.setAccessible(true);
        Object fieldValue = new Object();

        jclo.setObject(jclo.getField("field1"), fieldValue);

        try {
            assertEquals(fieldValue, field.get(obj));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUsage() {
        Object obj = new Object() {
            String field1 = "value1";
            int field2 = 1;
            String[][] aliases = {{ "alias1", "alias2" }, { "alias3", "alias4" }};
        };
        JCLO jclo = new JCLO("", obj, obj.aliases);

        String expectedUsage = "--field1 VALUE1\n" +
                "--field2 = INT_VALUE\n" +
                "--alias1 ALias2\n" +
                "--alias3 ALias4\n";

        assertEquals(expectedUsage, jclo.usage());
    }

    @Test
    public void testParseAdditional() {
        Object obj = new Object() {
            String[] additional;
        };
        String[] args = {"--additional", "arg1", "arg2"};
        JCLO jclo = new JCLO(obj);
        jclo.parseAdditional(args, 0);

        assertEquals(2, obj.getClass().getDeclaredField("additional").get(obj).length);
        assertArrayEquals(new String[]{"arg1", "arg2"}, (String[]) obj.getClass().getDeclaredField("additional").get(obj));
    }

    @Test
    public void testAddToArray() {
        Object obj = new Object();
        Field field = obj.getClass().getDeclaredField("arrayField");
        field.setAccessible(true);

        Object jclo = new JCLO(obj);
        jclo.parseAdditional(new String[]{"--arrayField", "newValue"}, 0);

        Object original = field.get(obj);
        Object added = jclo.makeObject("String", "newValue");

        Object result = jclo.addToArray(field, added);

        assertEquals(1, Array.getLength(original));
        assertEquals("newValue", Array.get(original, 0).toString());
        assertEquals(2, Array.getLength(result));
        assertEquals("newValue", Array.get(result, 1).toString());
    }

    @Test
    public void testParse() {
        Object obj = new Object() {
            String field1 = "defaultValue";
            int field2 = 0;
            String[] arrayField = new String[0];
            String[][] aliases = {{ "alias1", "alias2" }, { "alias3", "alias4" }};
        };
        String[] args = {"--field1=newValue",
                "--field2=5",
                "--arrayField=value1",
                "--arrayField=value2",
                "--alias1=aliasValue1",
                "--alias2=aliasValue2"};

        JCLO jclo = new JCLO(obj);
        jclo.parse(args);

        assertEquals("newValue", jclo.getValue("field1"));
        assertEquals(5, jclo.getInt("field2"));
        assertArrayEquals(new String[]{"value1", "value2"}, (String[]) jclo.getValue("arrayField"));
        assertEquals("aliasValue1", jclo.getValue("alias1"));
        assertEquals("aliasValue2", jclo.getValue("alias2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidArgument() {
        Object obj = new Object();
        String[] args = {"--invalidField=value"};
        JCLO jclo = new JCLO(obj);
        jclo.parse(args);
    }
}