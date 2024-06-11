package edu.mscd.cs.jclo;

import static org.junit.Assert.*;
import org.junit.Test;

public class JCLOTest {

    @Test
    public void testJCLO() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNotNull(jclo.fields);
        assertNotNull(jclo.object);
        assertEquals(0, jclo.fields.length);

        Object additionalObject = new Object()[1];
        String[][] aliases = {{ "alias1", "alias2" }, { "alias3", "alias4" }};
        JCLO jcloWithAliases = new JCLO("prefix", object, aliases);
        assertNotNull(jcloWithAliases.prefix);
        assertNotNull(jcloWithAliases.aliases);
        assertEquals(2, jcloWithAliases.aliases.length);
        assertEquals(2, jcloWithAliases.fields.length);

        JCLO jcloWithPrefix = new JCLO("prefix", object);
        assertEquals("prefix", jcloWithPrefix.prefix);

        JCLO jcloWithFields = new JCLO(object, new String[][] {{"field1", "alias1"}, {"field2", "alias2"}});
        Field field1 = jcloWithFields.getField("field1");
        Field field2 = jcloWithFields.getField("field2");
        Field alias1 = jcloWithFields.getField("alias1");
        Field alias2 = jcloWithFields.getField("alias2");
        assertNotNull(field1);
        assertNotNull(field2);
        assertNotNull(alias1);
        assertNotNull(alias2);
        assertEquals("field1", field1.getName());
        assertEquals("alias1", alias1.getName());
        assertEquals("field2", field2.getName());
        assertEquals("alias2", alias2.getName());
    }

    @Test
    public void testGetValue() {
        Object object = new Object() {{
            int field1 = 1;
            String field2 = "test";
        }};
        JCLO jclo = new JCLO(object);
        assertEquals(1, jclo.getValue("field1"));
        assertEquals("test", jclo.getValue("field2"));
    }

    @Test
    public void testSetValue() {
        Object object = new Object() {{
            int field1 = 1;
        }};
        JCLO jclo = new JCLO(object);
        jclo.setObject(jclo.getField("field1"), 2);
        assertEquals(2, ((int) object.getClass().getDeclaredField("field1").get(object)));
    }

    @Test
    public void testUsage() {
        Object object = new Object() {{
            int field1 = 1;
            String field2 = "test";
        }};
        JCLO jclo = new JCLO(object);
        String usage = jclo.usage();
        String expectedUsage = "-field1 int:\n" +
                "--field2=string:\n";
        assertEquals(expectedUsage, usage);
    }

    @Test
    public void testParse() {
        String[] args = {"-field1", "1", "--field2=test"};
        Object object = new Object() {{
            int field1 = 0;
            String field2 = null;
        }};
        JCLO jclo = new JCLO(object);
        jclo.parse(args);
        assertEquals(1, ((int) object.getClass().getDeclaredField("field1").get(object)));
        assertEquals("test", ((String) object.getClass().getDeclaredField("field2").get(object)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidOption() {
        String[] args = {"-invalidOption"};
        Object object = new Object() {{
            int field1 = 0;
        }};
        JCLO jclo = new JCLO(object);
        jclo.parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingValue() {
        String[] args = {"--field1"};
        Object object = new Object() {{
            int field1 = 0;
        }};
        JCLO jclo = new JCLO(object);
        jclo.parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingEquals() {
        String[] args = {"--field1=", "1"};
        Object object = new Object() {{
            int field1 = 0;
        }};
        JCLO jclo = new JCLO(object);
        jclo.parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidType() {
        String[] args = {"--field1=invalidType:value"};
        Object object = new Object() {{
            int field1 = 0;
        }};
        JCLO jclo = new JCLO(object);
        jclo.parse(args);
    }

    @Test
    public void testParseArray() {
        String[] args = {"-field1", "1", "-field1", "2", "-field1", "3"};
        Object object = new Object() {{
            int[] field1 = new int[0];
        }};
        JCLO jclo = new JCLO(object);
        jclo.parse(args);
        int[] field1 = (int[]) object.getClass().getDeclaredField("field1").get(object);
        assertEquals(3, field1.length);
        assertEquals(1, field1[0]);
        assertEquals(2, field1[1]);
        assertEquals(3, field1[2]);
    }
}