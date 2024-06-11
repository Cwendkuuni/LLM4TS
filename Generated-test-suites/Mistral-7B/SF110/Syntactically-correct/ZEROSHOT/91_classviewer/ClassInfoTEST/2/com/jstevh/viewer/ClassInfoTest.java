package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertFalse(classInfo.isAbstract());

            ClassInfo interfaceInfo = new ClassInfo("java.lang.Runnable");
            assertTrue(interfaceInfo.isAbstract());

            ClassInfo abstractClassInfo = new ClassInfo("java.util.AbstractList");
            assertTrue(abstractClassInfo.isAbstract());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class.");
        }
    }

    @Test
    public void testIsInterface() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertFalse(classInfo.isInterface());

            ClassInfo interfaceInfo = new ClassInfo("java.lang.Runnable");
            assertTrue(interfaceInfo.isInterface());

            ClassInfo abstractClassInfo = new ClassInfo("java.util.AbstractList");
            assertFalse(abstractClassInfo.isInterface());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class.");
        }
    }

    @Test
    public void testGetClassName() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertEquals("java.lang.String", classInfo.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class.");
        }
    }

    @Test
    public void testGetClassPackage() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertNull(classInfo.getClassPackage());

            ClassInfo packageClassInfo = new ClassInfo("com.jstevh.viewer.ClassInfo", new DirManager());
            assertEquals("com.jstevh.viewer", packageClassInfo.getClassPackage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class.");
        }
    }

    @Test
    public void testGetSuperClassName() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertNull(classInfo.getSuperClassName());

            ClassInfo objectClassInfo = new ClassInfo("java.lang.Object");
            assertNull(objectClassInfo.getSuperClassName());

            ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
            assertNull(stringClassInfo.getSuperClassName());

            ClassInfo vectorClassInfo = new ClassInfo("java.util.Vector", new DirManager());
            assertEquals("java.util.Collection", vectorClassInfo.getSuperClassName());

            ClassInfo arrayListClassInfo = new ClassInfo("java.util.ArrayList", new DirManager());
            assertEquals("java.util.AbstractList", arrayListClassInfo.getSuperClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class.");
        }
    }

    @Test
    public void testConstructor() throws ClassNotFoundException {
        ClassInfo classInfo = new ClassInfo("java.lang.String");

        Method method = classInfo.getClass().getDeclaredMethod("printClassInfo");
        method.setAccessible(true);
        method.invoke(classInfo);
    }

    @Test
    public void testPrintFields() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] fields = classInfo.printFields();
            assertEquals(0, fields.length);

            ClassInfo stringBuilderClassInfo = new ClassInfo("java.lang.StringBuilder");
            fields = stringBuilderClassInfo.printFields();
            assertEquals(1, fields.length);
            assertEquals("this = new StringBuilder()", fields[0]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class.");
        }
    }

    @Test
    public void testPrintMethods() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] methods = classInfo.printMethods();
            assertEquals(0, methods.length);

            ClassInfo stringBuilderClassInfo = new ClassInfo("java.lang.StringBuilder");
            methods = stringBuilderClassInfo.printMethods();
            assertEquals(11, methods.length);
            assertEquals("toString = class java.lang.String", methods[0]);
            assertEquals("append = class java.lang.StringBuilder", methods[1]);
            assertEquals("charAt = class java.lang.StringBuilder", methods[2]);
            assertEquals("codePointAt = class java.lang.StringBuilder", methods[3]);
            assertEquals("codePointBefore = class java.lang.StringBuilder", methods[4]);
            assertEquals("codePointCount = class java.lang.StringBuilder", methods[5]);
            assertEquals("getChars = class java.lang.StringBuilder", methods[6]);
            assertEquals("length = class java.lang.StringBuilder", methods[7]);
            assertEquals("replace = class java.lang.StringBuilder", methods[8]);
            assertEquals("setCharAt = class java.lang.StringBuilder", methods[9]);
            assertEquals("subSequence = class java.lang.StringBuilder", methods[10]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class.");
        }
    }

    @Test
    public void testPrintMethodsWithParam() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] methods = classInfo.printMethods(ClassInfo.NO_OBJECT_METHODS);
            assertEquals(0, methods.length);

            ClassInfo stringBuilderClassInfo = new ClassInfo("java.lang.StringBuilder");
            methods = stringBuilderClassInfo.printMethods(ClassInfo.NO_OBJECT_METHODS);
            assertEquals(1, methods.length);
            assertEquals("toString = class java.lang.StringBuilder", methods[0]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class.");
        }
    }

    @Test
    public void testPrintConstructors() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] constructors = classInfo.printConstructors();
            assertEquals(0, constructors.length);

            ClassInfo stringBuilderClassInfo = new ClassInfo("java.lang.StringBuilder");
            constructors = stringBuilderClassInfo.printConstructors();
            assertEquals(7, constructors.length);
            assertEquals("StringBuilder() = class java.lang.StringBuilder", constructors[0]);
            assertEquals("StringBuilder(int capacity) = class java.lang.StringBuilder", constructors[1]);
            assertEquals("StringBuilder(String str) = class java.lang.StringBuilder", constructors[2]);
            assertEquals("StringBuilder(String str, int capacity) = class java.lang.StringBuilder", constructors[3]);
            assertEquals("StringBuilder(CharSequence seq) = class java.lang.StringBuilder", constructors[4]);
            assertEquals("StringBuilder(CharSequence seq, int capacity) = class java.lang.StringBuilder", constructors[5]);
            assertEquals("StringBuilder(StringBuilder sb) = class java.lang.StringBuilder", constructors[6]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class.");
        }
    }

    @Test
    public void testPrintInterfaces() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] interfaces = classInfo.printInterfaces();
            assertEquals(0, interfaces.length);

            ClassInfo stringBuilderClassInfo = new ClassInfo("java.lang.StringBuilder");
            interfaces = stringBuilderClassInfo.printInterfaces();
            assertEquals(0, interfaces.length);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class.");
        }
    }

    @Test
    public void testSrchMethods() {
        try {
            String tempStr = "toString";

            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] methods = classInfo.srchMethods(tempStr);
            assertEquals(1, methods.length);
            assertEquals("toString = class java.lang.String", methods[0]);

            methods = classInfo.srchMethods(tempStr, new String[]{"java.lang.Object", "String"});
            assertEquals(1, methods.length);
            assertEquals("toString = class java.lang.String", methods[0]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class.");
        }
    }

    @Test
    public void testGetFoundMethod() {
        try {
            String className = "java.lang.String";
            String methodName = "toString";

            ClassInfo classInfo = new ClassInfo(className);
            MethodData methodData = classInfo.getFoundMethod(0);

            assertEquals(methodName, methodData.getMethName());
            assertEquals(className, methodData.getMethClass().getName());
            assertEquals(className, methodData.getMethPackage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to load class.");
        }
    }

    @Test
    public void testMain() {
        String[] args = new String[]{"new", "java.lang.String"};
        ClassInfo.main(args);
    }
}