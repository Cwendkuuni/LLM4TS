package com.jstevh.viewer;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        Assert.assertFalse(classInfo.isAbstract());

        ClassInfo abstractClassInfo = new ClassInfo("java.lang.AbstractStringBuilder");
        Assert.assertTrue(abstractClassInfo.isAbstract());
    }

    @Test
    public void testIsInterface() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        Assert.assertFalse(classInfo.isInterface());

        ClassInfo interfaceClassInfo = new ClassInfo("java.util.List");
        Assert.assertTrue(interfaceClassInfo.isInterface());
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

        ClassInfo classInfoWithPackage = new ClassInfo("com.jstevh.viewer.ClassInfo");
        assertEquals("com.jstevh.viewer", classInfoWithPackage.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        assertNull(classInfo.getSuperClassName());

        ClassInfo classInfoWithSuperClass = new ClassInfo("java.lang.String");
        assertEquals("java.lang.Object", classInfoWithSuperClass.getSuperClassName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testClassNotFoundException() throws Exception {
        new ClassInfo("com.jstevh.notfound.ClassNotFoundExceptionTest");
        fail("ClassNotFoundException should be thrown");
    }

    @Test
    public void testPrintFields() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] fields = classInfo.printFields();
        assertArrayEquals(new String[0], fields);

        ClassInfo stringBuilderClassInfo = new ClassInfo("java.lang.StringBuilder");
        fields = stringBuilderClassInfo.printFields();
        assertArrayEquals(new String[]{
                "this = StringBuilder@15db9742",
                "capacity = 16",
                "length = 0",
                "saveAll = false",
                "_append = null",
                "_appendp = null",
                "_capacityIncrement = 16",
                "_lengthIncrement = 1",
                "_sgr = 0",
                "_shrinkLock = false",
                "_size = 0",
                "_toString = [Ljava/lang/String;@1f36d6d9",
                "_trimmed = false",
                "_value = []"
        }, fields);
    }

    @Test
    public void testPrintMethods() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods();
        assertArrayEquals(new String[0], methods);

        ClassInfo stringBuilderClassInfo = new ClassInfo("java.lang.StringBuilder");
        methods = stringBuilderClassInfo.printMethods();
        assertArrayEquals(new String[]{
                "append(char[])",
                "append(char, int)",
                "append(String)",
                "append(String, int, int)",
                "capacity()",
                "charAt(int)",
                "delete(int, int)",
                "deleteCharAt(int)",
                "ensureCapacity(int)",
                "ensureCapacity(int, int)",
                "getLength()",
                "hashCode()",
                "insert(int, char)",
                "insert(int, String)",
                "length()",
                "replace(int, int, String)",
                "reverse()",
                "setCharAt(int, char)",
                "setLength(int)",
                "subSequence(int, int)",
                "toString()",
                "toString(int)"
        }, methods);
    }

    @Test
    public void testPrintMethodsWithParam() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.printMethods("java.lang.String");
        assertArrayEquals(new String[0], methods);

        ClassInfo stringBuilderClassInfo = new ClassInfo("java.lang.StringBuilder");
        methods = stringBuilderClassInfo.printMethods("java.lang.StringBuilder");
        assertArrayEquals(new String[]{
                "append(String)",
                "append(String, int, int)"
        }, methods);
    }

    @Test
    public void testPrintConstructors() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] constructors = classInfo.printConstructors();
        assertArrayEquals(new String[0], constructors);

        ClassInfo stringBuilderClassInfo = new ClassInfo("java.lang.StringBuilder");
        constructors = stringBuilderClassInfo.printConstructors();
        assertArrayEquals(new String[]{
                "StringBuilder()",
                "StringBuilder(int)"
        }, constructors);
    }

    @Test
    public void testPrintInterfaces() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] interfaces = classInfo.printInterfaces();
        assertArrayEquals(new String[0], interfaces);

        ClassInfo stringBuilderClassInfo = new ClassInfo("java.util.List");
        interfaces = stringBuilderClassInfo.printInterfaces();
        assertArrayEquals(new String[]{"java.io.Serializable", "java.util.Collection", "java.util.List"}, interfaces);
    }

    @Test
    public void testSrchMethods() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] methods = classInfo.srchMethods("toString");
        assertArrayEquals(new String[]{"public synchronized java.lang.String toString()"}, methods);

        methods = classInfo.srchMethods("toString", new String[]{"public"});
        assertArrayEquals(new String[0], methods);
    }

    @Test
    public void testGetFoundMethod() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        MethodData methodData = classInfo.getFoundMethod(0);
        assertEquals("java.lang.String", methodData.getMethClass());
        assertEquals("toString", methodData.getMethName());
    }

    @Test
    public void testPrintArray() throws Exception {
        Object[] array = new Object[10];
        new ClassInfo().printArray(array);

        for (Object obj : array) {
            System.out.println(obj);
        }
    }

    @Test
    public void testTryClass() throws Exception {
        Class tempClass = ClassInfo.tryClass("java.lang.String");
        assertEquals("java.lang.String", tempClass.getName());

        tempClass = ClassInfo.tryClass("com.jstevh.notfound.ClassNotFoundExceptionTest");
        assertNull(tempClass);
    }

    @Test
    public void testNewInstance() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
        Constructor constructor = classInfo.c.getConstructor(new Class[0]);
        Object instance = constructor.newInstance(new Object[0]);
        assertEquals("", (String) instance);

        constructor = classInfo.c.getConstructor(int.class);
        instance = constructor.newInstance(10);
        StringBuilder stringBuilder = (StringBuilder) instance;
        assertEquals(10, stringBuilder.capacity());
    }
}