package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import com.jstevh.viewer.ClassInfo;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isAbstract());

        classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
        assertTrue(classInfo.isAbstract());
    }

    @Test
    public void testIsInterface() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.List");
        assertTrue(classInfo.isInterface());
    }

    @Test
    public void testGetClassName() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertEquals("java.lang.String", classInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertNull(classInfo.getClassPackage());

        classInfo = new ClassInfo("com.jstevh.MyClass");
        assertEquals("com.jstevh", classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.lang.Object");
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.lang.Number");
        assertEquals("java.lang.Object", classInfo.getSuperClassName());
    }

    @Test
    public void testConstructorWithNoArguments() {
        ClassInfo classInfo = new ClassInfo();
        assertEquals(WindowConstants.class, classInfo.c);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testConstructorWithInvalidClassName() {
        ClassInfo classInfo = new ClassInfo("com.jstevh.InvalidClass");
    }

    @Test
    public void testPrintFields() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] fields = classInfo.printFields();
        assertNull(fields);

        classInfo = new ClassInfo("java.lang.Object");
        fields = classInfo.printFields();
        assertArrayEquals(new String[0], fields);

        classInfo = new ClassInfo("java.util.Date");
        fields = classInfo.printFields();
        String[] expectedFields = {
                "time=1606331352145",
                "year=1970",
                "month=7",
                "dayOfMonth=16",
                "dayOfWeek=1",
                "weekYear=1970",
                "dayOfYear=316",
                "hour=13",
                "minute=35",
                "second=25",
                "millisecond=1452"
        };
        assertArrayEquals(expectedFields, fields);
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods();
        assertNull(methods);

        classInfo = new ClassInfo("java.lang.Object");
        methods = classInfo.printMethods();
        assertArrayEquals(new String[0], methods);

        classInfo = new ClassInfo("java.lang.String");
        methods = classInfo.printMethods();
        String[] expectedMethods = {
                "toLowerCase()Ljava/lang/String;",
                "toUpperCase()Ljava/lang/String;",
                "equals(Ljava/lang/Object;)Z",
                "hashCode()I",
                "length()I",
                "charAt(I)C",
                "substring(II)Ljava/lang/String;",
                "indexOf(Ljava/lang/String;)I"
        };
        assertArrayEquals(expectedMethods, methods);
    }

    @Test
    public void testPrintMethodsWithParam() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods("equals");
        String[] expectedMethods = {
                "equals(Ljava/lang/Object;)Z"
        };
        assertArrayEquals(expectedMethods, methods);
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] constructors = classInfo.printConstructors();
        assertNull(constructors);

        classInfo = new ClassInfo("java.lang.Object");
        constructors = classInfo.printConstructors();
        assertArrayEquals(new String[0], constructors);

        classInfo = new ClassInfo("java.util.Date");
        constructors = classInfo.printConstructors();
        String[] expectedConstructors = {
                "<init>()V",
                "<init>(J)V",
                "<init>(Ljava/util/Date;)V"
        };
        assertArrayEquals(expectedConstructors, constructors);
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] interfaces = classInfo.printInterfaces();
        assertArrayEquals(new String[0], interfaces);

        classInfo = new ClassInfo("java.util.List");
        interfaces = classInfo.printInterfaces();
        String[] expectedInterfaces = {
                "java.io.Serializable",
                "java.lang.Cloneable"
        };
        assertArrayEquals(expectedInterfaces, interfaces);
    }

    @Test
    public void testSrchMethods() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.srchMethods("equals");
        assertArrayEquals(new String[0], methods);

        methods = classInfo.srchMethods("length");
        String[] expectedMethods = {
                "length()I"
        };
        assertArrayEquals(expectedMethods, methods);
    }

    @Test
    public void testSrchMethodsWithIndex() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.srchMethods("toLowerCase", new String[]{"String"});
        assertArrayEquals(new String[0], methods);

        methods = classInfo.srchMethods("toUpperCase", new String[]{"String"});
        String[] expectedMethods = {
                "toUpperCase()Ljava/lang/String;"
        };
        assertArrayEquals(expectedMethods, methods);
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        MethodData methodData = classInfo.getFoundMethod(0);
        assertNull(methodData);

        methodData = classInfo.getFoundMethod(1);
        String expectedClassName = "java.lang.String";
        String expectedMethodName = "length";
        String expectedMethodSignature = "()I";
        assertEquals(expectedClassName, methodData.getMethClass());
        assertEquals(expectedMethodName, methodData.getMethName());
        assertEquals(expectedMethodSignature, methodData.getMethodSignature());

        methodData = classInfo.getFoundMethod(2);
        expectedClassName = "java.lang.Object";
        expectedMethodName = "equals";
        String expectedMethodSignature2 = "(Ljava/lang/Object;)Z";
        assertEquals(expectedClassName, methodData.getMethClass());
        assertEquals(expectedMethodName, methodData.getMethName());
        assertEquals(expectedMethodSignature2, methodData.getMethodSignature());
    }

    @Test
    public void testPrintArray() {
        String[] array = {"hello", "world"};
        ClassInfo classInfo = new ClassInfo();
        classInfo.printArray(array);
        String expectedOutput = "[Ljava.lang.String;@22b7f2e\nhello\nworld]";
        assertEquals(expectedOutput, System.out.getHistory(1)[0].toString());
    }

    @Test
    public void testMain() throws Exception {
        String[] args = {"new", "java.util.Date"};
        ClassInfo.main(args);
    }
}