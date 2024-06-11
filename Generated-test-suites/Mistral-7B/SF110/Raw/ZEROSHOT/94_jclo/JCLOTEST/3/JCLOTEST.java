package edu.mscd.cs.jclo;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.Test;

public class JCLOTest {

    @Test
    public void testJCLOConstructorWithObject() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNotNull(jclo.getObject(object.getClass().getDeclaredField("field1")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOConstructorWithInvalidKey() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        jclo.getValue("invalidKey");
    }

    @Test
    public void testJCLOConstructorWithPrefixAndObject() {
        Object object = new Object() {
            private String prefix = "test_";
            private int field1;

            public int getField1() {
                return field1;
            }

            public void setField1(int field1) {
                this.field1 = field1;
            }
        };
        JCLO jclo = new JCLO("prefix", object);
        assertEquals("prefix_field1", jclo.getValue("field1").getClass().getName());
    }

    @Test
    public void testJCLOConstructorWithPrefixObjectAndAliases() {
        Object object = new Object() {
            private String prefix = "test_";
            private int field1;
            private String[][] aliases = { { "f1", "field1" }, { "f2", "field2" } };

            public int getField1() {
                return field1;
            }

            public void setField1(int field1) {
                this.field1 = field1;
            }
        };
        JCLO jclo = new JCLO("prefix", object, object.aliases);
        assertEquals("prefix_field1", jclo.getValue("f1").getClass().getName());
    }

    @Test
    public void testJCLOGetters() {
        Object object = new Object() {
            private String prefix = "test_";
            private int field1;

            public int getField1() {
                return field1;
            }

            public void setField1(int field1) {
                this.field1 = field1;
            }
        };
        JCLO jclo = new JCLO("prefix", object);
        Field field1 = object.getClass().getDeclaredField("field1");
        assertEquals(field1.getName(), jclo.getField("field1").getName());
    }

    @Test
    public void testJCLOValueGetters() {
        Object object = new Object() {
            private String prefix = "test_";
            private int field1 = 10;
            private String field2 = "test";

            public int getField1() {
                return field1;
            }

            public void setField1(int field1) {
                this.field1 = field1;
            }

            public String getField2() {
                return field2;
            }

            public void setField2(String field2) {
                this.field2 = field2;
            }
        };
        JCLO jclo = new JCLO("prefix", object);
        assertEquals(10, jclo.getInt("field1"));
        assertEquals("test", jclo.getString("field2"));
    }

    @Test
    public void testJCLOSetters() {
        Object object = new Object() {
            private String prefix = "test_";
            private int field1 = 10;

            public int getField1() {
                return field1;
            }

            public void setField1(int field1) {
                this.field1 = field1;
            }
        };
        JCLO jclo = new JCLO("prefix", object);
        Field field1 = object.getClass().getDeclaredField("field1");
        jclo.setObject(field1, 20);
        assertEquals(20, jclo.getInt("field1"));
    }

    @Test
    public void testJCLOUsage() {
        Object object = new Object() {
            private String prefix = "test_";
            private int field1 = 10;
            private String field2 = "test";

            public int getField1() {
                return field1;
            }

            public void setField1(int field1) {
                this.field1 = field1;
            }

            public String getField2() {
                return field2;            }

            public void setField2(String field2) {
                this.field2 = field2;
            }
        };
        JCLO jclo = new JCLO("prefix", object);
        String usage = jclo.usage();
        assertTrue(usage.contains("--prefix="));
        assertTrue(usage.contains("--field1="));
        assertTrue(usage.contains("--field1=" + getUsageType(int[].class)));
        assertTrue(usage.contains("--field2="));
        assertTrue(usage.contains("--field2=" + getUsageType(String.class)));
    }

    @Test
    public void testJCLOParse() {
        Object object = new Object() {
            private String prefix = "test_";
            private int field1 = 10;
            private String field2 = "test";

            public int getField1() {
                return field1;
            }

            public void setField1(int field1) {
                this.field1 = field1;
            }

            public String getField2() {
                return field2;
            }

            public void setField2(String field2) {
                this.field2 = field2;
            }
        };
        JCLO jclo = new JCLO("prefix", object);
        String[] args = { "--prefix=test", "--field1=20", "--field2=test2" };
        jclo.parse(args);
        assertEquals(20, jclo.getInt("field1"));
        assertEquals("test2", jclo.getString("field2"));
    }

    @Test
    public void testJCLOParseWithArray() {
        Object object = new Object() {
            private String prefix = "test_";
            private int[] field1 = { 1, 2, 3 };

            public int[] getField1() {
                return field1;
            }

            public void setField1(int[] field1) {
                this.field1 = field1;
            }
        };
        JCLO jclo = new JCLO("prefix", object);
        String[] args = { "--prefix=test", "--field1=1,2,3" };
        jclo.parse(args);
        assertArrayEquals(new int[] { 1, 2, 3 }, jclo.getInts("field1"));
    }

    @Test
    public void testJCLOParseWithUnknownOption() {
        Object object = new Object() {
            private String prefix = "test_";
            private int field1 = 10;

            public int getField1() {
                return field1;
            }

            public void setField1(int field1) {
                this.field1 = field1;
            }
        };
        JCLO jclo = new JCLO("prefix", object);
        String[] args = { "--prefix=test", "--unknown=unknown" };
        try {
            jclo.parse(args);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("No such option: \"unknown\"", e.getMessage());
        }
    }

    private String getUsageType(Class type) {
        return type.isPrimitive() ? type.getName() : type.getName().replaceFirst("class.*\\.", "");
    }
}