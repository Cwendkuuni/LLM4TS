package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import com.jstevh.viewer.ClassInfo;
import java.lang.reflect.Method;
import org.junit.Test;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        assertFalse(classInfo.isAbstract());

        ClassInfo interfaceInfo = new ClassInfo("java.lang.Runnable", new DirManager() {});
        assertTrue(interfaceInfo.isAbstract());
    }

    @Test
    public void testIsInterface() {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        assertFalse(classInfo.isInterface());

        ClassInfo stringBufferInfo = new ClassInfo("java.lang.StringBuffer", new DirManager() {});
        assertFalse(stringBufferInfo.isInterface());

        ClassInfo runnableInfo = new ClassInfo("java.lang.Runnable", new DirManager() {});
        assertTrue(runnableInfo.isInterface());
    }

    @Test
    public void testGetClassName() {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        assertEquals("java.lang.String", classInfo.getClassName());

        ClassInfo stringBufferInfo = new ClassInfo("java.lang.StringBuffer", new DirManager() {});
        assertEquals("java.lang.StringBuffer", stringBufferInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        assertNull(classInfo.getClassPackage());

        ClassInfo stringBufferInfo = new ClassInfo("java.lang.StringBuffer", new DirManager() {});
        assertEquals("java.lang", stringBufferInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo(String.class.getName());
        assertNull(classInfo.getSuperClassName());

        ClassInfo objectInfo = new ClassInfo(Object.class.getName());
        assertNull(objectInfo.getSuperClassName());

        ClassInfo stringBufferInfo = new ClassInfo("java.lang.StringBuffer", new DirManager() {});
        assertEquals("java.lang.Object", stringBufferInfo.getSuperClassName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testClassNotFoundException() {
        new ClassInfo("com.jstevh.viewer.NonExistentClass");
    }

    @Test
    public void testPrintFields() {
        ClassInfo stringInfo = new ClassInfo(String.class.getName());
        String[] fields = stringInfo.printFields();
        assertEquals(0, fields.length);

        ClassInfo stringBufferInfo = new ClassInfo("java.lang.StringBuffer", new DirManager() {});
        String[] fields2 = stringBufferInfo.printFields();
        assertNotNull(fields2);
        assertEquals(10, fields2.length);
    }

    @Test
    public void testPrintMethods() {
        ClassInfo stringInfo = new ClassInfo(String.class.getName());
        String[] methods = stringInfo.printMethods();
        assertNotNull(methods);
        assertEquals(11, methods.length);

        ClassInfo stringBufferInfo = new ClassInfo("java.lang.StringBuffer", new DirManager() {});
        String[] methods2 = stringBufferInfo.printMethods();
        assertNotNull(methods2);
        assertEquals(15, methods2.length);

        String[] methods3 = stringBufferInfo.printMethods(1);
        assertNotNull(methods3);
        assertEquals(3, methods3.length);
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo stringInfo = new ClassInfo(String.class.getName());
        String[] constructors = stringInfo.printConstructors();
        assertEquals(0, constructors.length);

        ClassInfo stringBufferInfo = new ClassInfo("java.lang.StringBuffer", new DirManager() {});
        String[] constructors2 = stringBufferInfo.printConstructors();
        assertNotNull(constructors2);
        assertEquals(6, constructors2.length);
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo stringInfo = new ClassInfo(String.class.getName());
        String[] interfaces = stringInfo.printInterfaces();
        assertNull(interfaces);

        ClassInfo stringBufferInfo = new ClassInfo("java.lang.StringBuffer", new DirManager() {});
        String[] interfaces2 = stringBufferInfo.printInterfaces();
        assertNotNull(interfaces2);
        assertEquals(1, interfaces2.length);
        assertEquals("java.io.Serializable", interfaces2[0]);

        ClassInfo stringReaderInfo = new ClassInfo("java.io.StringReader", new DirManager() {});
        String[] interfaces3 = stringReaderInfo.printInterfaces();
        assertNotNull(interfaces3);
        assertEquals(2, interfaces3.length);
        assertEquals("java.io.Reader", interfaces3[0]);
        assertEquals("java.io.Closeable", interfaces3[1]);
    }

    @Test
    public void testSrchMethods() {
        ClassInfo stringInfo = new ClassInfo(String.class.getName());
        String[] methods = stringInfo.printMethods();
        String[] foundMethods = stringInfo.srchMethods("equals");
        assertNotNull(foundMethods);
        assertEquals(1, foundMethods.length);
        assertEquals(methods[0], foundMethods[0]);

        String[] emptyMethods = stringInfo.srchMethods("toString");
        assertNull(emptyMethods);
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo stringInfo = new ClassInfo(String.class.getName());
        String[] methods = stringInfo.printMethods();
        String methodStr = methods[0];
        MethodData methodData = stringInfo.getFoundMethod(0);
        assertEquals(methodStr, methodData.getMethName());
        assertEquals("java.lang.String", methodData.getMethClass());
        assertEquals("java.lang.Object", methodData.getMethPackage());
    }

    @Test
    public void testPrintArray() {
        String[] array = new String[]{"one", "two", "three"};
        ClassInfo classInfo = new ClassInfo();
        classInfo.printArray(array);

        ClassInfo stringInfo = new ClassInfo();
        String[] emptyArray = new String[0];
        classInfo.printArray(emptyArray);
    }

    @Test
    public void testGetClass() {
        ClassInfo classInfo = new ClassInfo();
        Class stringClass = classInfo.getClass("java.lang.String");
        assertEquals(String.class, stringClass);

        try {
            Class nonExistentClass = classInfo.getClass("com.jstevh.viewer.NonExistentClass");
            fail();
        } catch (ClassNotFoundException e) {
            // expected
        }
    }

    @Test
    public void testTryClass() {
        ClassInfo classInfo = new ClassInfo();
        Class stringClass = classInfo.tryClass("java.lang.String");
        assertEquals(String.class, stringClass);

        try {
            Class nonExistentClass = classInfo.tryClass("com.jstevh.viewer.NonExistentClass");
            fail();
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testMain() throws Exception {
        String[] args = new String[]{"new", "java.lang.String"};
        ClassInfo.main(args);
    }
}