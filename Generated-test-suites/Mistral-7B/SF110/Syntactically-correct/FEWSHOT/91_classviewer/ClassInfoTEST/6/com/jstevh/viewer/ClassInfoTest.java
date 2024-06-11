package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import com.jstevh.viewer.*;
import java.lang.reflect.Method;

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

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util", classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.lang.String");
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.lang.Number");
        assertEquals("java.lang.Object", classInfo.getSuperClassName());
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(45, methods.length);

        for (String method : methods) {
            System.out.println(method);
        }
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(1, constructors.length);

        for (String constructor : constructors) {
            System.out.println(constructor);
        }
    }

    @Test
    public void testPrintFields() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] fields = classInfo.printFields();
        assertNotNull(fields);
        assertEquals(0, fields.length);
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo("java.util.List");
        String[] interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(1, interfaces.length);
        assertEquals("java.io.Serializable", interfaces[0]);

        classInfo = new ClassInfo("java.util.ArrayList");
        interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(1, interfaces.length);
        assertEquals("java.util.List", interfaces[0]);

        classInfo = new ClassInfo("java.util.LinkedList");
        interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(2, interfaces.length);
        assertEquals("java.io.Serializable", interfaces[0]);
        assertEquals("java.util.List", interfaces[1]);
    }

    @Test
    public void testSrchMethods() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = {"public static java.lang.String valueOf(java.lang.String)",
                            "public java.lang.String charAt(int)",
                            "public int length()",
                            "public char[] toCharArray()"};
        classInfo.cMethods = methods;

        String[] foundMethods = classInfo.srchMethods("charAt");
        assertNotNull(foundMethods);
        assertEquals(1, foundMethods.length);
        assertEquals("public java.lang.String charAt(int)", foundMethods[0]);

        foundMethods = classInfo.srchMethods("length");
        assertNotNull(foundMethods);
        assertEquals(1, foundMethods.length);
        assertEquals("public int length()", foundMethods[0]);

        foundMethods = classInfo.srchMethods("valueOf", new String[]{"java.lang.String"});
        assertNotNull(foundMethods);
        assertEquals(1, foundMethods.length);
        assertEquals("public static java.lang.String valueOf(java.lang.String)", foundMethods[0]);

        foundMethods = classInfo.srchMethods("toString");
        assertNull(foundMethods);
    }

    @Test
    public void testGetFoundMethod() {
        String className = "java.util.ArrayList";
        String methodName = "add";
        String methodSignature = "(Ljava/lang/Object;)Z";

        ClassInfo classInfo = new ClassInfo();
        Method method = new Method(methodName, new Class[] { Object.class }, new AccessibleObject[0]);
        classInfo.cMethods = new String[] { method.toString() };

        MethodData methodData = classInfo.getFoundMethod(0);
        assertEquals(className, methodData.getMethPackage());
        assertEquals(className, methodData.getMethClass());
        assertEquals(methodName, methodData.getMethName());
    }
}