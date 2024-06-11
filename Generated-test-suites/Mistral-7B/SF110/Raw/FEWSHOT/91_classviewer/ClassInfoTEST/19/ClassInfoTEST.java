package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isAbstract());

        classInfo = new ClassInfo("java.lang.AbstractString");
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

        classInfo = new ClassInfo("com.jstevh.viewer.ClassInfo");
        assertEquals("com.jstevh.viewer", classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.lang.String");
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.Collection", classInfo.getSuperClassName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testConstructorWithInvalidClassName() {
        new ClassInfo("InvalidClassName");
    }

    @Test
    public void testPrintFields() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] fields = classInfo.printFields();
        assertEquals(0, fields.length);

        classInfo = new ClassInfo("java.util.Date");
        fields = classInfo.printFields();
        assertNotNull(fields);
        assertEquals(3, fields.length);
        assertEquals("time", fields[0]);
        assertEquals("date", fields[1]);
        assertEquals("classes", fields[2]);
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(0, methods.length);

        classInfo = new ClassInfo("java.lang.Math");
        methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(11, methods.length);
        assertEquals("abs", methods[0]);
        assertEquals("max", methods[1]);
        assertEquals("min", methods[2]);
    }

    @Test
    public void testPrintMethodsWithParam() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods(1);
        assertNull(methods);

        classInfo = new ClassInfo("java.lang.Math");
        methods = classInfo.printMethods(0);
        assertNotNull(methods);
        assertEquals("abs", methods[0]);
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] constructors = classInfo.printConstructors();
        assertNull(constructors);

        classInfo = new ClassInfo("java.util.ArrayList");
        constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(1, constructors.length);
        assertEquals("ArrayList", constructors[0]);
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] interfaces = classInfo.printInterfaces();
        assertNull(interfaces);

        classInfo = new ClassInfo("java.util.List");
        interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(1, interfaces.length);
        assertEquals("java.util.Collection", interfaces[0]);
    }

    @Test
    public void testSrchMethods() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.srchMethods("toString");
        assertNull(methods);

        classInfo = new ClassInfo("java.util.ArrayList");
        methods = classInfo.srchMethods("add");
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public void add(E e)", methods[0]);
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        MethodData methodData = classInfo.getFoundMethod(0);
        assertNull(methodData);

        classInfo = new ClassInfo("java.util.ArrayList");
        methodData = classInfo.getFoundMethod(0);
        assertEquals("public ArrayList()", methodData.getMethClass() + "." + methodData.getMethName());
    }

    @Test
    public void testMain() {
        assertThrows(ClassNotFoundException.class, () -> new ClassInfo("java.lang.InvalidClass"));

        String[] args = {"new", "java.util.ArrayList"};
        ClassInfo.main(args);
    }
}