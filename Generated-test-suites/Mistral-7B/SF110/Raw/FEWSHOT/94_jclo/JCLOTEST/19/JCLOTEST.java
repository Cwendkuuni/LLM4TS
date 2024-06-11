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
        assertEquals(0, jclo.fields.length);
    }

    @Test
    public void testJCLOWithFields() {
        class TestObj {
            int a;
            String b;
            char c;
            boolean d;
        }

        TestObj obj = new TestObj();
        JCLO jclo = new JCLO(obj);
        Field[] fields = jclo.fields;

        assertEquals(4, fields.length);
        assertEquals("a", fields[0].getName());
        assertEquals("int", fields[0].getType().getName());

        assertEquals("b", fields[1].getName());
        assertEquals("class java.lang.String", fields[1].getType().getName());

        assertEquals("c", fields[2].getName());
        assertEquals("char", fields[2].getType().getName());

        assertEquals("d", fields[3].getName());
        assertEquals("boolean", fields[3].getType().getName());
    }

    @Test
    public void testGetValue() {
        class TestObj {
            int a;
            String b;
            char c;
            boolean d;
        }

        TestObj obj = new TestObj();
        obj.a = 1;
        obj.b = "test";
        obj.c = 't';
        obj.d = true;

        JCLO jclo = new JCLO(obj);
        assertEquals(1, jclo.getValue("a"));
        assertEquals("test", jclo.getValue("b"));
        assertEquals('t', jclo.getValue("c"));
        assertEquals(true, jclo.getValue("d"));
    }

    @Test
    public void testGetValueUnknownField() {
        class TestObj {
            int a;
            String b;
            char c;
            boolean d;
        }

        TestObj obj = new TestObj();
        JCLO jclo = new JCLO(obj);
        assertNull(jclo.getValue("unknown"));
    }

    @Test
    public void testToString() {
        class TestObj {
            int a;
            String b;
            char c;
            boolean d;
        }

        TestObj obj = new TestObj();
        obj.a = 1;
        obj.b = "test";
        obj.c = 't';
        obj.d = true;

        JCLO jclo = new JCLO(obj);
        String expected = "int: a = 1\n" +
                "class java.lang.String: b = test\n" +
                "char: c = t\n" +
                "boolean: d = true";
        assertEquals(expected, jclo.toString());
    }

    @Test
    public void testUsage() {
        class TestObj {
            int a;
            String b;
            char c;
            boolean d;
        }

        TestObj obj = new TestObj();
        JCLO jclo = new JCLO(obj, new String[][]{{"a", "num"}, {"b", "string"}});
        String expected = "-num\n" +
                "-string\n";
        assertEquals(expected, jclo.usage());
    }

    @Test
    public void testUsageWithPrefix() {
        class TestObj {
            int a;
            String b;
            char c;
            boolean d;
        }

        TestObj obj = new TestObj();
        JCLO jclo = new JCLO(obj, null, "opt_");
        String expected = "-opt_num\n" +
                "-opt_string\n";
        assertEquals(expected, jclo.usage());
    }

    @Test
    public void testParse() {
        class TestObj {
            int a[];
            String b;
            boolean d;
        }

        TestObj obj = new TestObj();

        String[] args = {"--num", "1", "--string", "test", "--boolean", "true"};
        JCLO jclo = new JCLO(obj);
        jclo.parse(args);

        assertEquals(new int[]{1}, obj.a);
        assertEquals("test", obj.b);
        assertEquals(true, obj.d);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseUnknownOption() {
        class TestObj {
            int a[];
            String b;
            boolean d;
        }

        TestObj obj = new TestObj();
        JCLO jclo = new JCLO(obj);
        jclo.parse(new String[]{"--unknown"});
    }

    @Test
    public void testParseUnknownOptionWithValue() {
        class TestObj {
            int a[];
            String b;
            boolean d;
        }

        TestObj obj = new TestObj();
        JCLO jclo = new JCLO(obj);
        try {
            jclo.parse(new String[]{"--unknown=value"});
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("No such option: 'unknown'", e.getMessage());
        }
    }

    @Test
    public void testParseMultipleValues() {
        class TestObj {
            int a[];
            String b;
            boolean d;
        }

        TestObj obj = new TestObj();
        JCLO jclo = new JCLO(obj);
        jclo.parse(new String[]{"--num", "1", "2", "--string", "test1", "test2"});

        assertEquals(new int[]{1, 2}, obj.a);
        assertEquals("test1test2", obj.b);
    }
}