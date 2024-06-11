package edu.mscd.cs.jclo;

import static org.junit.Assert.*;
import org.junit.Test;

public class JCLOTest {

    @Test
    public void testJCLO() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        Field[] fields = jclo.fields;
        assertEquals(fields.length, 0);

        String prefix = "prefix_";
        obj = new MyClass(prefix);
        jclo = new JCLO(prefix, obj);
        fields = jclo.fields;
        assertEquals(fields.length, 3);

        String[][] aliases = new String[][]{{"a", "aliasA"}, {"b", "aliasB"}};
        jclo = new JCLO(prefix, obj, aliases);
        fields = jclo.fields;
        assertEquals(fields.length, 3);

        Field field = jclo.getField("field1");
        assertNull(field);

        field = jclo.getField("prefix_field1");
        assertNotNull(field);
    }

    @Test
    public void testGetValue() {
        Object obj = new MyClass("prefix");
        JCLO jclo = new JCLO("prefix", obj);

        Field field1 = jclo.fields[0];
        field1.set(jclo.object, "value1");

        assertEquals("value1", jclo.getValue("field1"));

        field1 = jclo.fields[1];
        field1.set(jclo.object, new int[]{1, 2, 3});

        Object[] ints = (Object[]) jclo.getValue("field2");
        assertEquals(ints[0], 1);
        assertEquals(ints[1], 2);
        assertEquals(ints[2], 3);
    }

    @Test
    public void testUsage() {
        Object obj = new MyClass("prefix");
        JCLO jclo = new JCLO("prefix", obj);

        String usage = jclo.usage();

        String expectedUsage = "--prefix_field1=VALUE1\n" +
                "--prefix_field2=[=INT...]\n" +
                "--prefix_field3=VALUE3\n";

        assertEquals(expectedUsage, usage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidOption() {
        Object obj = new MyClass("prefix");
        JCLO jclo = new JCLO("prefix", obj);
        String[] args = {"--invalid"};
        jclo.parse(args);
    }

    @Test
    public void testParseBooleanOption() {
        Object obj = new MyClass("prefix");
        JCLO jclo = new JCLO("prefix", obj);
        String[] args = {"--prefix_boolean=true"};
        jclo.parse(args);
        assertTrue(jclo.getBoolean("boolean"));
    }

    @Test
    public void testParseIntArrayOption() {
        Object obj = new MyClass("prefix");
        JCLO jclo = new JCLO("prefix", obj);
        String[] args = {"--prefix_ints=1,2,3"};
        jclo.parse(args);
        int[] ints = (int[]) jclo.getValue("ints");
        assertEquals(ints[0], 1);
        assertEquals(ints[1], 2);
        assertEquals(ints[2], 3);
    }

    @Test
    public void testParseStringOption() {
        Object obj = new MyClass("prefix");
        JCLO jclo = new JCLO("prefix", obj);
        String[] args = {"--prefix_string=value"};
        jclo.parse(args);
        assertEquals("value", jclo.getString("string"));
    }

    @Test
    public void testParseStringAliasOption() {
        Object obj = new MyClass("prefix");
        JCLO jclo = new JCLO("prefix", obj, new String[][]{{"a", "aliasA"}, {"b", "aliasB"}});
        String[] args = {"--aliasA=value"};
        jclo.parse(args);
        assertEquals("value", jclo.getString("a"));
        assertEquals("value", jclo.getString("aliasA"));
    }

    private static class MyClass {
        private String field1;
        private int field2[];
        private String field3;

        public MyClass(String prefix) {
            this.field1 = "";
            this.field2 = new int[0];
            this.field3 = "";
        }

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }

        public int[] getField2() {
            return field2;
        }

        public void setField2(int[] field2) {
            this.field2 = field2;
        }

        public String getField3() {
            return field3;
        }

        public void setField3(String field3) {
            this.field3 = field3;
        }
    }
}