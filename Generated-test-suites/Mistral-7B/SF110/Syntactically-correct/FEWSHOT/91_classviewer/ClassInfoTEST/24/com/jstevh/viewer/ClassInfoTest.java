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

            ClassInfo interfaceClassInfo = new ClassInfo("java.lang.Runnable");
            assertTrue(interfaceClassInfo.isAbstract());

            ClassInfo abstractClassInfo = new ClassInfo("java.util.AbstractCollection");
            assertTrue(abstractClassInfo.isAbstract());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Class not found exception: " + e.getMessage());
        }
    }

    @Test
    public void testIsInterface() {
        try {
            ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
            assertFalse(stringClassInfo.isInterface());

            ClassInfo runnableClassInfo = new ClassInfo("java.lang.Runnable");
            assertTrue(runnableClassInfo.isInterface());

            ClassInfo stringBufferClassInfo = new ClassInfo("java.lang.StringBuffer");
            assertFalse(stringBufferClassInfo.isInterface());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Class not found exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetClassName() throws Exception {
        try {
            ClassInfo classInfo = new ClassInfo("com.jstevh.viewer.ClassInfo");
            assertEquals("com.jstevh.viewer.ClassInfo", classInfo.getClassName());

            ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
            assertEquals("java.lang.String", stringClassInfo.getClassName());

            ClassInfo intClassInfo = new ClassInfo("int");
            assertEquals("int", intClassInfo.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Class not found exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetClassPackage() throws Exception {
        try {
            ClassInfo classInfo = new ClassInfo("com.jstevh.viewer.ClassInfo");
            assertEquals("com.jstevh.viewer", classInfo.getClassPackage());

            ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
            assertNull(stringClassInfo.getClassPackage());

            ClassInfo intClassInfo = new ClassInfo("int");
            assertNull(intClassInfo.getClassPackage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Class not found exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetSuperClassName() throws Exception {
        try {
            ClassInfo objectClassInfo = new ClassInfo("java.lang.Object");
            assertNull(objectClassInfo.getSuperClassName());

            ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
            assertNull(stringClassInfo.getSuperClassName());

            ClassInfo stringBufferClassInfo = new ClassInfo("java.lang.StringBuffer");
            assertEquals("java.lang.Object", stringBufferClassInfo.getSuperClassName());

            ClassInfo ArrayListClassInfo = new ClassInfo("java.util.ArrayList");
            assertEquals("java.util.AbstractList", ArrayListClassInfo.getSuperClassName());

            ClassInfo AbstractCollectionClassInfo = new ClassInfo("java.util.AbstractCollection");
            assertEquals("java.util.Collection", AbstractCollectionClassInfo.getSuperClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Class not found exception: " + e.getMessage());
        }
    }

    @Test
    public void testPrintMethods() throws Exception {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.Object");
            String[] methods = classInfo.printMethods();
            assertNotNull(methods);
            assertEquals(0, methods.length);

            ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
            methods = stringClassInfo.printMethods();
            assertNotNull(methods);
            assertEquals(1, methods.length);
            assertEquals("length()", methods[0]);

            ClassInfo stringBufferClassInfo = new ClassInfo("java.lang.StringBuffer");
            methods = stringBufferClassInfo.printMethods();
            assertNotNull(methods);
            assertEquals(14, methods.length);
            assertTrue(methods[0].startsWith("append("));
            assertTrue(methods[1].startsWith("capacity()"));
            assertTrue(methods[12].startsWith("setLength("));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Class not found exception: " + e.getMessage());
        }
    }

    @Test
    public void testPrintConstructors() throws Exception {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.Object");
            String[] constructors = classInfo.printConstructors();
            assertNotNull(constructors);
            assertEquals(0, constructors.length);

            ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
            constructors = stringClassInfo.printConstructors();
            assertNotNull(constructors);
            assertEquals(0, constructors.length);

            ClassInfo stringBufferClassInfo = new ClassInfo("java.lang.StringBuffer");
            constructors = stringBufferClassInfo.printConstructors();
            assertNotNull(constructors);
            assertEquals(2, constructors.length);
            assertTrue(constructors[0].startsWith("StringBuffer("));
            assertTrue(constructors[1].startsWith("StringBuffer(int)"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Class not found exception: " + e.getMessage());
        }
    }

    @Test
    public void testPrintInterfaces() throws Exception {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.Object");
            String[] interfaces = classInfo.printInterfaces();
            assertNotNull(interfaces);
            assertEquals(0, interfaces.length);

            ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
            interfaces = stringClassInfo.printInterfaces();
            assertNotNull(interfaces);
            assertEquals(0, interfaces.length);

            ClassInfo stringBufferClassInfo = new ClassInfo("java.lang.StringBuffer");
            interfaces = stringBufferClassInfo.printInterfaces();
            assertNotNull(interfaces);
            assertEquals(1, interfaces.length);
            assertEquals("java.io.Serializable", interfaces[0]);

            ClassInfo ArrayListClassInfo = new ClassInfo("java.util.ArrayList");
            interfaces = ArrayListClassInfo.printInterfaces();
            assertNotNull(interfaces);
            assertEquals(1, interfaces.length);
            assertEquals("java.util.List", interfaces[0]);

            ClassInfo AbstractCollectionClassInfo = new ClassInfo("java.util.AbstractCollection");
            interfaces = AbstractCollectionClassInfo.printInterfaces();
            assertNotNull(interfaces);
            assertEquals(1, interfaces.length);
            assertEquals("java.util.Collection", interfaces[0]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Class not found exception: " + e.getMessage());
        }
    }

    @Test
    public void testSrchMethods() throws Exception {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.Object");
            String[] methods = classInfo.srchMethods(null);
            assertNotNull(methods);
            assertEquals(0, methods.length);

            ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
            methods = stringClassInfo.srchMethods(null);
            assertNotNull(methods);
            assertEquals(1, methods.length);
            assertEquals("length()", methods[0]);

            ClassInfo stringBufferClassInfo = new ClassInfo("java.lang.StringBuffer");
            methods = stringBufferClassInfo.srchMethods("append");
            assertNotNull(methods);
            assertEquals(1, methods.length);
            assertEquals("append(char)", methods[0]);

            methods = stringBufferClassInfo.srchMethods("capacity", new String[]{"int"});
            assertNotNull(methods);
            assertEquals(1, methods.length);
            assertEquals("capacity()", methods[0]);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetFoundMethod() throws Exception {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.Object");
            MethodData methodData = classInfo.getFoundMethod(0);
            assertNull(methodData);

            ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
            methodData = stringClassInfo.getFoundMethod(0);
            assertNotNull(methodData);
            assertEquals("length()", methodData.getMethName());

            String[] methods = stringClassInfo.printMethods();
            methodData = stringClassInfo.getFoundMethod(Arrays.asList(methods).indexOf("length()"));
            assertNotNull(methodData);
            assertEquals("length()", methodData.getMethName());

            ClassInfo stringBufferClassInfo = new ClassInfo("java.lang.StringBuffer");
            methods = stringBufferClassInfo.printMethods();
            methodData = stringBufferClassInfo.getFoundMethod(Arrays.asList(methods).indexOf("append(char)"));
            assertNotNull(methodData);
            assertEquals("append(char)", methodData.getMethName());

            methodData = stringBufferClassInfo.getFoundMethod(Arrays.asList(methods).indexOf("capacity()"));
            assertNotNull(methodData);
            assertEquals("capacity()", methodData.getMethName());
        } catch (ClassNotFoundException | NoSuchMethodException | IndexOutOfBoundsException e) {
            e.printStackTrace();
            fail("Exception: " + e.getMessage());
        }
    }

    @Test
    public void testMain() {
        try {
            String[] args = new String[]{"new", "java.lang.String"};
            ClassInfo.main(args);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception: " + e.getMessage());
        }
    }
}