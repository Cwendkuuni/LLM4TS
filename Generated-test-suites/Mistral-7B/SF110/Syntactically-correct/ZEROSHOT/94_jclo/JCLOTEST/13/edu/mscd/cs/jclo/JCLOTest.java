package edu.mscd.cs.jclo;

import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;

public class JCLOTest {

    @Test
    public void testJCLOInstance() {
        Object object = new Object();
        JCLO jclo1 = new JCLO(object);
        JCLO jclo2 = new JCLO(object, new String[][]{{"alias1", "value1"}});
        JCLO jclo3 = new JCLO("prefix", object);
        JCLO jclo4 = new JCLO("prefix", object, new String[][]{{"alias1", "value1"}});

        assertEquals(jclo1.getClass(), JCLO.class);
        assertEquals(jclo2.getClass(), JCLO.class);
        assertEquals(jclo3.getClass(), JCLO.class);
        assertEquals(jclo4.getClass(), JCLO.class);
    }

    @Test
    public void testGetFields() {
        Object object = new Object() {
            private int a = 1;
            private String b = "test";
            private boolean c = true;
            private char d = 'd';
            private double e = 3.14;
            private float f = 2.718f;
            private long g = 42L;
            private short h = 127;
            private int[] i = {1, 2, 3};
            private String[] j = {"one", "two", "three"};
            private byte[] k = {1, 2, 3};
            private char[] l = {'a', 'b', 'c'};
            private short[] m = {1, 2, 3};
            private int[][] n = {{1, 2}, {3, 4}, {5, 6}};
        };

        JCLO jclo = new JCLO(object);

        Field[] fields = jclo.getFields();
        assertEquals(fields.length, 17);

        Field aField = jclo.getField("a");
        assertEquals(aField.getName(), "a");
        assertEquals(aField.getType().getTypeName(), "int");

        Field bField = jclo.getField("b");
        assertEquals(bField.getName(), "b");
        assertEquals(bField.getType().getTypeName(), "java.lang.String");

        Field cField = jclo.getField("c");
        assertEquals(cField.getName(), "c");
        assertEquals(cField.getType().getTypeName(), "boolean");

        Field dField = jclo.getField("d");
        assertEquals(dField.getName(), "d");
        assertEquals(dField.getType().getTypeName(), "char");

        Field eField = jclo.getField("e");
        assertEquals(eField.getName(), "e");
        assertEquals(eField.getType().getTypeName(), "double");

        Field fField = jclo.getField("f");
        assertEquals(fField.getName(), "f");
        assertEquals(fField.getType().getTypeName(), "float");

        Field gField = jclo.getField("g");
        assertEquals(gField.getName(), "g");
        assertEquals(gField.getType().getTypeName(), "long");

        Field hField = jclo.getField("h");
        assertEquals(hField.getName(), "h");
        assertEquals(hField.getType().getTypeName(), "short");

        Field iField = jclo.getField("i");
        assertEquals(iField.getName(), "i");
        assertEquals(iField.getType().getTypeName(), "int[]");

        Field jField = jclo.getField("j");
        assertEquals(jField.getName(), "j");
        assertEquals(jField.getType().getTypeName(), "java.lang.String[]");

        Field kField = jclo.getField("k");
        assertEquals(kField.getName(), "k");
        assertEquals(kField.getType().getTypeName(), "byte[]");

        Field lField = jclo.getField("l");
        assertEquals(lField.getName(), "l");
        assertEquals(lField.getType().getTypeName(), "char[]");

        Field mField = jclo.getField("m");
        assertEquals(mField.getName(), "m");
        assertEquals(mField.getType().getTypeName(), "short[]");

        Field nField = jclo.getField("n");
        assertEquals(nField.getName(), "n");
        assertEquals(nField.getType().getTypeName(), "int[][]");
    }

    @Test
    public void testGetValue() {
        Object object = new Object() {
            private int a = 1;
            private String b = "test";
            private boolean c = true;
            private char d = 'd';
            private double e = 3.14;
            private float f = 2.718f;
            private long g = 42L;
            private short h = 127;
            private int[] i = {1, 2, 3};
            private String[] j = {"one", "two", "three"};
            private byte[] k = {1, 2, 3};
            private char[] l = {'a', 'b', 'c'};
            private short[] m = {1, 2, 3};
            private int[][] n = {{1, 2}, {3, 4}, {5, 6}};
        };

        JCLO jclo = new JCLO(object);

        assertEquals(jclo.getValue("a"), 1);
        assertEquals(jclo.getValue("b"), "test");
        assertTrue(jclo.getValue("c"));
        assertEquals(jclo.getValue("d"), 'd');
        assertEquals(jclo.getValue("e"), 3.14, 0.001);
        assertEquals(jclo.getValue("f"), 2.718f, 0.0001);
        assertEquals(jclo.getValue("g"), 42L);
        assertEquals(jclo.getValue("h"), 127);
        assertArrayEquals(jclo.getValue("i"), new int[]{1, 2, 3});
        assertArrayEquals(jclo.getValue("j"), new String[]{"one", "two", "three"});
        assertArrayEquals(jclo.getValue("k"), new byte[]{1, 2, 3});
        assertArrayEquals(jclo.getValue("l"), new char[]{'a', 'b', 'c'});
        assertArrayEquals(jclo.getValue("m"), new short[]{1, 2, 3});
        assertArrayEquals(jclo.getValue("n"), new int[][]{{1, 2}, {3, 4}, {5, 6}});
    }

    @Test
    public void testSetValue() {
        Object object = new Object() {
            private int a = 1;
            private String b = "test";
            private boolean c = true;
            private char d = 'd';
            private double e = 3.14;
            private float f = 2.718f;
            private long g = 42L;
            private short h = 127;
            private int[] i = {1, 2, 3};
            private String[] j = {"one", "two", "three"};
            private byte[] k = {1, 2, 3};
            private char[] l = {'a', 'b', 'c'};
            private short[] m = {1, 2, 3};
            private int[][] n = {{1, 2}, {3, 4}, {5, 6}};
        };

        JCLO jclo = new JCLO(object);

        jclo.setValue("a", 42);
        assertEquals(jclo.getValue("a"), 42);

        jclo.setValue("b", "new test");
        assertEquals(jclo.getValue("b"), "new test");

        jclo.setValue("c", false);
        assertFalse(jclo.getValue("c"));

        jclo.setValue("d", 'e');
        assertEquals(jclo.getValue("d"), 'e');

        jclo.setValue("e", 5.12);
        assertEquals(jclo.getValue("e"), 5.12, 0.001);

        jclo.setValue("f", 2.71);
        assertEquals(jclo.getValue("f"), 2.71f, 0.0001);

        jclo.setValue("g", 43L);
        assertEquals(jclo.getValue("g"), 43L);

        jclo.setValue("h", (short)128);
        assertEquals(jclo.getValue("h"), 127); // should not allow setting h to 128 due to its short data type

        int[] newI = {5, 6, 7};
        jclo.setValue("i", newI);
        assertArrayEquals(jclo.getValue("i"), newI);

        String[] newJ = {"new one", "new two", "new three"};
        jclo.setValue("j", newJ);
        assertArrayEquals(jclo.getValue("j"), newJ);

        byte[] newK = {4, 5, 6};
        jclo.setValue("k", newK);
        assertArrayEquals(jclo.getValue("k"), newK);

        char[] newL = {'d', 'e', 'f'};
        jclo.setValue("l", newL);
        assertArrayEquals(jclo.getValue("l"), newL);

        short[] newM = {4, 5, 6};
        jclo.setValue("m", newM);
        assertArrayEquals(jclo.getValue("m"), newM);

        int[][] newN = {{5, 6}, {7, 8}, {9, 10}};
        jclo.setValue("n", newN);
        assertArrayEquals(jclo.getValue("n"), newN);
    }

    @Test
    public void testToString() {
        Object object = new Object() {
            private int a = 1;
            private String b = "test";
            private boolean c = true;
            private char d = 'd';
            private double e = 3.14;
            private float f = 2.718f;
            private long g = 42L;
            private short h = 127;
            private int[] i = {1, 2, 3};
            private String[] j = {"one", "two", "three"};
            private byte[] k = {1, 2, 3};
            private char[] l = {'a', 'b', 'c'};
            private short[] m = {1, 2, 3};
            private int[][] n = {{1, 2}, {3, 4}, {5, 6}};
        };

        JCLO jclo = new JCLO(object);

        String expectedOutput = "int: a = 1\n" +
                "java.lang.String: b = test\n" +
                "boolean: c = true\n" +
                "char: d = d\n" +
                "double: e = 3.14\n" +
                "float: f = 2.718\n" +
                "long: g = 42\n" +
                "short: h = 127\n" +
                "int[]: i = [1, 2, 3]\n" +
                "java.lang.String[]: j = [one, two, three]\n" +
                "byte[]: k = [1, 2, 3]\n" +
                "char[]: l = [a, b, c]\n" +
                "short[]: m = [1, 2, 3]\n" +
                "int[][]: n = [[1, 2], [3, 4], [5, 6]]\n";

        assertEquals(jclo.toString(), expectedOutput);
    }

    @Test
    public void testUsage() {
        Object object = new Object() {
            private int a = 1;
            private String b = "test";
            private boolean c = true;
            private char d = 'd';
            private double e = 3.14;
            private float f = 2.718f;
            private long g = 42L;
            private short h = 127;
            private int[] i = {1, 2, 3};
            private String[] j = {"one", "two", "three"};
            private byte[] k = {1, 2, 3};
            private char[] l = {'a', 'b', 'c'};
            private short[] m = {1, 2, 3};
            private int[][] n = {{1, 2}, {3, 4}, {5, 6}};
        };

        JCLO jclo = new JCLO(object);

        String expectedOutput = "--a= --int\n" +
                "--b= --java.lang.String\n" +
                "--c= --boolean\n" +
                "--d= --char\n" +
                "--e= --double\n" +
                "--f= --float\n" +
                "--g= --long\n" +
                "--h= --short\n" +
                "--i= --int[]\n" +
                "--j= --java.lang.String[]\n" +
                "--k= --byte[]\n" +
                "--l= --char[]\n" +
                "--m= --short[]\n" +
                "--n= --int[][]\n";

        assertEquals(jclo.usage(), expectedOutput);
    }

    @Test
    public void testParse() {
        String[] args = {"--a=1", "--b=test", "--c=false", "--d=e", "--e=5.12", "--f=2.71", "--g=43", "--h=128", "--i=5,6,7", "--j=new one,new two,new three", "--k=4,5,6", "--l=d,e,f", "--m=4,5,6", "--n=5,6,9,10,11,12"};

        Object object = new Object();
        JCLO jclo = new JCLO(object);
        jclo.parse(args);

        assertEquals(jclo.getValue("a"), 1);
        assertEquals(jclo.getValue("b"), "test");
        assertFalse(jclo.getValue("c"));
        assertEquals(jclo.getValue("d"), 'e');
        assertEquals(jclo.getValue("e"), 5.12, 0.001);
        assertEquals(jclo.getValue("f"), 2.71f, 0.0001);
        assertEquals(jclo.getValue("g"), 43L);
        assertEquals(jclo.getValue("h"), 128); // should have been set to 127 but 128 was given as an argument
        assertArrayEquals(jclo.getValue("i"), new int[]{5, 6, 7});
        String[] jValues = (String[]) jclo.getValue("j");
        assertArrayEquals(jValues, new String[]{"new one", "new two", "new three"});
        assertArrayEquals(jclo.getValue("k"), new byte[]{4, 5, 6});
        char[] lValues = (char[]) jclo.getValue("l");
        assertArrayEquals(lValues, new char[]{'d', 'e', 'f'});
        assertArrayEquals(jclo.getValue("m"), new short[]{4, 5, 6});
        int[][] nValues = (int[][]) jclo.getValue("n");
        assertArrayEquals(nValues, new int[][]{{5, 6}, {7, 8}, {9, 10, 11, 12}});
    }
}