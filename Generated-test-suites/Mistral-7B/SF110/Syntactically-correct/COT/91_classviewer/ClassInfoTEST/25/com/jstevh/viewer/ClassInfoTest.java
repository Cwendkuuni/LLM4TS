package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import com.jstevh.viewer.ClassInfo;
import java.io.File;
import java.lang.Class;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.Test;

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

        classInfo = new ClassInfo("java.util.ArrayList");
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

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.AbstractCollection", classInfo.getSuperClassName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testClassNotFoundException() {
        new ClassInfo("com.jstevh.viewer.UnknownClass");
    }

    @Test
    public void testPrintFields() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] fields = classInfo.printFields();
        assertNotNull(fields);

        classInfo = new ClassInfo("java.lang.String");
        fields = classInfo.printFields();
        assertArrayEquals(new String[]{}, fields);
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);

        classInfo = new ClassInfo("java.lang.String");
        methods = classInfo.printMethods();
        assertArrayEquals(new String[0], methods);
    }

    @Test
    public void testPrintMethodsWithParam() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] methods = classInfo.printMethods(ClassInfo.NO_OBJECT_METHODS);
        assertNotNull(methods);

        classInfo = new ClassInfo("java.lang.String");
        methods = classInfo.printMethods(ClassInfo.NO_OBJECT_METHODS);
        assertArrayEquals(new String[0], methods);
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] constructors = classInfo.printConstructors();
        assertNotNull(constructors);

        classInfo = new ClassInfo("java.lang.String");
        constructors = classInfo.printConstructors();
        assertArrayEquals(new String[0], constructors);
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(1, interfaces.length);
        assertEquals("java.util.Collection", interfaces[0]);

        classInfo = new ClassInfo("java.lang.String");
        interfaces = classInfo.printInterfaces();
        assertNull(interfaces);
    }

    @Test
    public void testSrchMethods() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String tempStr = "add";
        String[] methods = classInfo.srchMethods(tempStr);
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public void add(E e)", methods[0]);

        tempStr = "remove";
        methods = classInfo.srchMethods(tempStr);
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public boolean remove(Object o)", methods[0]);

        tempStr = "toString";
        methods = classInfo.srchMethods(tempStr);
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public String toString()", methods[0]);

        tempStr = "getSuperClassName";
        methods = classInfo.srchMethods(tempStr);
        assertNull(methods);
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] methods = classInfo.printMethods();
        MethodData methodData = classInfo.getFoundMethod(0);
        assertNotNull(methodData);
        assertEquals("public void add(E e)", methodData.getMethName());
        assertEquals("java.util.ArrayList", methodData.getMethClass());
        assertEquals("java.util.ArrayList", methodData.getMethPackage());

        methodData = classInfo.getFoundMethod(methods.length - 1);
        assertNotNull(methodData);
        assertEquals("private static native void removeRange(int,int)", methodData.getMethName());
        assertEquals("java.util.ArrayList", methodData.getMethClass());
        assertEquals("java.util.ArrayList", methodData.getMethPackage());
    }

    @Test
    public void testPrintArray() throws Exception {
        Object[] array = new Object[10];
        new ClassInfo().printArray(array);

        File file = new File("test.txt");
        try (PrintWriter writer = new PrintWriter(file)) {
            new ClassInfo().printArray(new String[]{"test"});
        }
        new ClassInfo().printArray(new String[]{"test"});

        File emptyFile = new File("empty.txt");
        try (PrintWriter writer = new PrintWriter(emptyFile)) {
            new ClassInfo().printArray(new Object[0]);
        }
        new ClassInfo().printArray(new Object[0]);
    }

    @Test
    public void testGetClass() {
        ClassInfo classInfo = new ClassInfo();
        Class clazz = classInfo.getClass("java.util.ArrayList");
        assertNotNull(clazz);
        assertEquals(ArrayList.class, clazz);

        try {
            classInfo.getClass("java.lang.String$$HashCode");
            fail();
        } catch (ClassNotFoundException e) {
            // Expected
        }

        try {
            classInfo.getClass("com.jstevh.viewer.UnknownClass");
            fail();
        } catch (ClassNotFoundException e) {
            // Expected
        }

        String packageName = "com.jstevh.viewer";
        String className = "ClassInfoTest";
        ClassInfo classInfo2 = new ClassInfo(packageName);
        Class clazz2 = classInfo2.getClass(className);
        assertNotNull(clazz2);
        assertEquals(ClassInfoTest.class, clazz2);
    }

    
}