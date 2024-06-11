package com.jstevh.viewer;

import org.junit.Test;
import org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isAbstract());

        ClassInfo abstractClassInfo = new ClassInfo("java.lang.AbstractStringBuilder");
        assertTrue(abstractClassInfo.isAbstract());
    }

    @Test
    public void testIsInterface() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isInterface());

        ClassInfo interfaceClassInfo = new ClassInfo("java.util.List");
        assertTrue(interfaceClassInfo.isInterface());
    }

    @Test
    public void testGetClassName() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertEquals("java.lang.String", classInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertNull(classInfo.getClassPackage());

        ClassInfo packageClassInfo = new ClassInfo("com.jstevh.viewer.ClassInfo");
        assertEquals("com.jstevh.viewer", packageClassInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        assertNull(classInfo.getSuperClassName());

        ClassInfo classWithSuperClass = new ClassInfo("java.lang.String");
        assertEquals("java.lang.Object", classInfo.getSuperClassName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testConstructorWithInvalidClassName() throws Exception {
        new ClassInfo("InvalidClassName");
    }

    @Test
    public void testPrintMethods() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        assertFalse(Arrays.equals(new String[0], methods));

        for (String method : methods) {
            Method methodObj = ClassInfo.class.getDeclaredMethod(method);
            methodObj.setAccessible(true);
            Object result = methodObj.invoke(classInfo);
            assertTrue(result != null);
        }
    }

    @Test
    public void testPrintMethodsWithParam() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods(1);
        assertNotNull(methods);
        assertFalse(Arrays.equals(new String[0], methods));

        for (String method : methods) {
            Method methodObj = ClassInfo.class.getDeclaredMethod(method);
            methodObj.setAccessible(true);
            Object result = methodObj.invoke(classInfo);
            assertTrue(result != null);
        }
    }

    @Test
    public void testPrintFields() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] fields = classInfo.printFields();
        assertNotNull(fields);
        assertFalse(Arrays.equals(new String[0], fields));

        for (String field : fields) {
            Field fieldObj = ClassInfo.class.getDeclaredField(field);
            fieldObj.setAccessible(true);
            Object fieldValue = fieldObj.get(classInfo);
            assertTrue(fieldValue != null);
        }
    }
}