package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import com.jstevh.viewer.ClassInfo;

import java.lang.reflect.Method;

import org.junit.Test;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertFalse(classInfo.isAbstract());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
            assertTrue(classInfo.isAbstract());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }

    @Test
    public void testIsInterface() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertFalse(classInfo.isInterface());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.util.List");
            assertTrue(classInfo.isInterface());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }

    @Test
    public void testGetClassName() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertEquals("java.lang.String", classInfo.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }

    @Test
    public void testGetClassPackage() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertNull(classInfo.getClassPackage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }

        try {
            ClassInfo classInfo = new ClassInfo("com.jstevh.viewer.ClassInfo");
            assertEquals("com.jstevh.viewer", classInfo.getClassPackage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }

    @Test
    public void testGetSuperClassName() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            assertNull(classInfo.getSuperClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
            assertEquals("java.util.AbstractList", classInfo.getSuperClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }

    @Test(expected = ClassNotFoundException.class)
    public void testGetClassThrowsClassNotFoundException() {
        new ClassInfo("java.lang.Xyzzy");
    }

    @Test
    public void testPrintMethods() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] methods = classInfo.printMethods();
            assertNotNull(methods);
            assertTrue(methods.length > 0);
            for (String method : methods) {
                System.out.println(method);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }

    @Test
    public void testPrintConstructors() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] constructors = classInfo.printConstructors();
            assertNotNull(constructors);
            assertTrue(constructors.length > 0);
            for (String constructor : constructors) {
                System.out.println(constructor);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }

    @Test
    public void testPrintInterfaces() {
        try {
            ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
            String[] interfaces = classInfo.printInterfaces();
            assertNotNull(interfaces);
            assertTrue(interfaces.length > 0);
            for (String interfaceName : interfaces) {
                System.out.println(interfaceName);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }

    @Test
    public void testSrchMethods() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] methods = classInfo.srchMethods("equals");
            assertNotNull(methods);
            if (methods.length > 0) {
                String method = methods[0];
                Method methodObj = null;
                try {
                    methodObj = classInfo.c.getMethod(method);
                } catch (NoSuchMethodException e) {
                    fail("Method not found: " + method);
                }
                assertEquals("(Ljava/lang/Object;)Z", methodObj.toGenericString());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }

        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            String[] methods = classInfo.srchMethods("equals", new String[]{"String"});
            assertNotNull(methods);
            if (methods.length > 0) {
                String method = methods[0];
                Method methodObj = null;
                try {
                    methodObj = classInfo.c.getMethod(method, String.class);
                } catch (NoSuchMethodException e) {
                    fail("Method not found: " + method);
                }
                assertEquals("(Ljava/lang/String;)Z", methodObj.toGenericString());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }

    @Test
    public void testGetFoundMethod() {
        try {
            ClassInfo classInfo = new ClassInfo("java.lang.String");
            MethodData methodData = classInfo.getFoundMethod(0);
            assertNotNull(methodData);
            assertEquals("java.lang.String", methodData.getMethPackage());
            assertEquals("java.lang.String", methodData.getMethClass());
            assertEquals("equals", methodData.getMethName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException should not be thrown");
        }
    }
}