package com.jstevh.viewer.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.jstevh.viewer.ClassInfo;
import com.jstevh.viewer.ClassInfo.MethodData;
import com.jstevh.tools.StringTools;

public class ClassInfoTest {
    private ClassInfo classInfo;

    @Before
    public void setUp() {
        classInfo = new ClassInfo("java.lang.String");
    }

    // Test isAbstract() method
    @Test
    public void testIsAbstract() {
        assertTrue(classInfo.isInterface());
        assertFalse(classInfo.isAbstract());

        classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
        assertTrue(classInfo.isAbstract());
    }

    // Test getClassName() method
    @Test
    public void testGetClassName() {
        assertEquals("java.lang.String", classInfo.getClassName());
    }

    // Test getClassPackage() method
    @Test
    public void testGetClassPackage() {
        assertNull(classInfo.getClassPackage());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util", classInfo.getClassPackage());
    }

    // Test getSuperClassName() method
    @Test
    public void testGetSuperClassName() {
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.AbstractCollection", classInfo.getSuperClassName());
    }

    // Test printFields() method
    @Test
    public void testPrintFields() {
        String[] fields = classInfo.printFields();
        assertNotNull(fields);
        assertEquals(0, fields.length);

        classInfo = new ClassInfo("java.util.ArrayList");
        fields = classInfo.printFields();
        assertEquals(7, fields.length);
    }

    // Test printMethods() method
    @Test
    public void testPrintMethods() {
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
        assertEquals(0, methods.length);

        classInfo = new ClassInfo("java.util.ArrayList");
        methods = classInfo.printMethods();
        assertEquals(37, methods.length);
    }

    // Test printMethods(int param) method
    @Test
    public void testPrintMethods_intParam() {
        String[] methods = classInfo.printMethods(0);
        assertNotNull(methods);
        assertEquals(0, methods.length);

        classInfo = new ClassInfo("java.util.ArrayList");
        methods = classInfo.printMethods(1);
        assertEquals(1, methods.length);
        assertEquals("public static <E> java.util.ArrayList<E> java.util.Collections.emptyList()", methods[0]);

        methods = classInfo.printMethods(37);
        assertNotNull(methods);
        assertEquals(0, methods.length);
    }

    // Test printConstructors() method
    @Test
    public void testPrintConstructors() {
        String[] constructors = classInfo.printConstructors();
        assertNotNull(constructors);
        assertEquals(0, constructors.length);

        classInfo = new ClassInfo("java.util.ArrayList");
        constructors = classInfo.printConstructors();
        assertEquals(2, constructors.length);
    }

    // Test printInterfaces() method
    @Test
    public void testPrintInterfaces() {
        String[] interfaces = classInfo.printInterfaces();
        assertNotNull(interfaces);
        assertEquals(0, interfaces.length);

        classInfo = new ClassInfo("java.util.ArrayList");
        interfaces = classInfo.printInterfaces();
        assertEquals(1, interfaces.length);
        assertEquals("java.util.Collection", interfaces[0]);
    }

    // Test getData(Member[] m) method
    @Test
    public void testGetData_MemberArray() {
        Member[] members = new Member[0];
        String[] data = ClassInfo.getData(members);
        assertNull(data);

        Class<?> clazz = String[].class;
        Member[] members2 = clazz.getMethods();
        data = ClassInfo.getData(members2);
        assertEquals(11, data.length);
    }

    // Test getData(Class[] m) method
    @Test
    public void testGetData_ClassArray() {
        Class<?>[] classes = new Class[0];
        String[] data = ClassInfo.getData(classes);
        assertNull(data);

        Class<?> clazz = String[].class;
        Class<?>[] classes2 = new Class[] { clazz };
        data = ClassInfo.getData(classes2);
        assertEquals(1, data.length);
        assertEquals(clazz.getName(), data[0]);
    }

    // Test srchMethods(String tempStr) method
    @Test
    public void testSrchMethods_String() {
        String tempStr = null;
        String[] methods = classInfo.srchMethods(tempStr);
        assertNull(methods);

        tempStr = "toString";
        methods = classInfo.srchMethods(tempStr);
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public synchronized java.lang.String java.lang.Object.toString()", methods[0]);
    }

    // Test srchMethods(String tempStr, String[] index) method
    @Test
    public void testSrchMethods_StringArray() {
        String tempStr = null;
        String[] index = new String[0];
        String[] methods = classInfo.srchMethods(tempStr, index);
        assertNull(methods);

        tempStr = "toString";
        index = new String[] { "0" };
        methods = classInfo.srchMethods(tempStr, index);
        assertNotNull(methods);
        assertEquals(1, methods.length);
        assertEquals("public synchronized java.lang.String java.lang.Object.toString()", methods[0]);
    }

    // Test getFoundMethod(int pos) method
    @Test
    public void testGetFoundMethod_int() {
        String[] methods = classInfo.printMethods();
        ClassInfo.MethodData methodData = classInfo.getFoundMethod(0);
        assertNull(methodData);

        methodData = classInfo.getFoundMethod(1);
        assertEquals("public static <E> java.util.ArrayList<E> java.util.Collections.emptyList()", methodData.getMethName());
    }

    // Test printArray(Object[] array) method
    @Test(expected = AssertionError.class)
    public void testPrintArray_ObjectArray_debugTrue() {
        ClassInfo.debug = true;
        String[] array = null;
        classInfo.printArray(array);
    }

    // Test printArray(Object[] array) method with debug false
    @Test
    public void testPrintArray_ObjectArray_debugFalse() {
        String[] array = null;
        classInfo = new ClassInfo();
        classInfo.printArray(array);
    }

    // Test getClass(String name) method
    @Test
    public void testGetClass_String() {
        assertNull(classInfo.getClass(null));

        classInfo = new ClassInfo();
        classInfo.getClass("java.lang.String");
        assertSame(String.class, classInfo.c);
    }

    // Test tryClass(String name) method
    @Test
    public void testTryClass_String() {
        assertNull(classInfo.tryClass(null));

        Class<?> expectedClass = String[].class;
        assertSame(expectedClass, classInfo.tryClass("java.lang.String[]"));
    }
}