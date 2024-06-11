package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.ClassCastException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.jstevh.tools.StringTools;
import com.jstevh.viewer.ClassInfo;
import com.jstevh.viewer.DirManager;

public class ClassInfoTest {

    private static final String CLASS_NAME = "com.jstevh.viewer.ClassInfo";
    private static final String DIR_MANAGER_PACKAGE = "com.jstevh.viewer";
    private static final String TEST_DIR = "src/test/resources";

    private ClassInfo classInfo;
    private DirManager dirManager;

    @Before
    public void setUp() throws Exception {
        classInfo = new ClassInfo(CLASS_NAME);
        dirManager = new DirManager(new File(TEST_DIR));
    }

    @Test
    public void testIsAbstract() {
        assertFalse(classInfo.isAbstract());
    }

    @Test
    public void testIsAbstractWithAbstractClass() throws Exception {
        classInfo = new ClassInfo(AbstractClass.class.getName());
        assertTrue(classInfo.isAbstract());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testClassNotFoundException() throws Exception {
        classInfo = new ClassInfo("NonExistentClass");
    }

    @Test
    public void testGetClassName() {
        assertEquals(CLASS_NAME, classInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() {
        String expectedPackageName = "com.jstevh.viewer";
        assertEquals(expectedPackageName, classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        String expectedSuperClassName = "java.lang.Object";
        assertEquals(expectedSuperClassName, classInfo.getSuperClassName());
    }

    @Test
    public void testPrintFields() {
        String[] expectedFields = {"field1=value1", "field2=value2"};
        assertArrayEquals(expectedFields, classInfo.printFields());
    }

    @Test
    public void testPrintMethods() {
        classInfo = new ClassInfo(CLASS_NAME, dirManager);
        String[] expectedMethods = {"public void method1()", "public void method2()", "public static void method3(int param)"};
        assertArrayEquals(expectedMethods, classInfo.printMethods());
    }

    @Test
    public void testPrintMethodsWithFilter() {
        classInfo = new ClassInfo(CLASS_NAME, dirManager);
        String[] expectedMethods = {"public static void method3(int param)"};
        assertArrayEquals(expectedMethods, classInfo.printMethods(ClassInfo.NO_OBJECT_METHODS));
    }

    @Test
    public void testPrintConstructors() {
        classInfo = new ClassInfo(CLASS_NAME);
        String[] expectedConstructors = {"public com.jstevh.viewer.ClassInfo()", "public com.jstevh.viewer.ClassInfo(java.lang.String)"};
        assertArrayEquals(expectedConstructors, classInfo.printConstructors());
    }

    @Test
    public void testPrintInterfaces() {
        classInfo = new ClassInfo(InterfaceClass.class.getName());
        String[] expectedInterfaces = {"java.io.Serializable"};
        assertArrayEquals(expectedInterfaces, classInfo.printInterfaces());
    }

    @Test
    public void testSrchMethods() {
        classInfo = new ClassInfo(CLASS_NAME, dirManager);
        String searchString = "method";
        String[] expectedMethods = {"public void method1()", "public static void method3(int param)"};
        assertArrayEquals(expectedMethods, classInfo.srchMethods(searchString));
    }

    @Test
    public void testSrchMethodsWithIndex() {
        classInfo = new ClassInfo(CLASS_NAME, dirManager);
        String searchString = "method";
        String[] index = {"public"};
        String[] expectedMethods = {"public void method1()"};
        assertArrayEquals(expectedMethods, classInfo.srchMethods(searchString, index));
    }

    @Test
    public void testGetFoundMethod() {
        classInfo = new ClassInfo(CLASS_NAME, dirManager);
        int index = 1;
        MethodData expectedMethodData = new MethodData() {
            @Override
            public String getMethPackage() {
                return "com.jstevh.viewer";
            }

            @Override
            public String getMethClass() {
                return CLASS_NAME;
            }

            @Override
            public String getMethName() {
                return "method3";
            }
        };
        MethodData actualMethodData = classInfo.getFoundMethod(index);
        assertNotNull(actualMethodData);
        assertEquals(expectedMethodData.getMethPackage(), actualMethodData.getMethPackage());
        assertEquals(expectedMethodData.getMethClass(), actualMethodData.getMethClass());
        assertEquals(expectedMethodData.getMethName(), actualMethodData.getMethName());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetFoundMethodOutOfBounds() {
        classInfo = new ClassInfo(CLASS_NAME, dirManager);
        int index = 10;
        classInfo.getFoundMethod(index);
    }

    @Test
    public void testTryClass() {
        String className = "java.lang.String";
        Class expectedClass = String.class;
        Class actualClass = ClassInfo.tryClass(className);
        assertEquals(expectedClass, actualClass);
    }

    @Test(expected = NoClassDefFoundError.class)
    public void testTryClassNoClassDefFoundError() {
        String className = "NonExistentClass";
        ClassInfo.tryClass(className);
    }

    @Test(expected = ClassCastException.class)
    public void testTryClassClassCastException() {
        String className = "java.lang.Integer";
        Class actualClass = ClassInfo.tryClass(className);
        assertNotSame(Integer.class, actualClass);
    }

    @Test
    public void testGetClass() {
        String className = "com.jstevh.viewer.ClassInfo";
        Class expectedClass = ClassInfo.class;
        Class actualClass = classInfo.getClass(className);
        assertEquals(expectedClass, actualClass);
    }

    @Test
    public void testGetClassWithPackage() {
        String className = "com.jstevh.viewer.ClassInfo";
        Class expectedClass = ClassInfo.class;
        Class actualClass = classInfo.getClass(className, dirManager);
        assertEquals(expectedClass, actualClass);
    }

    @Test
    public void testGetClassNotFoundException() {
        String className = "NonExistentClass";
        try {
            classInfo.getClass(className);
            fail();
        } catch (ClassNotFoundException e) {
            // expected
        }
    }

    @Test
    public void testPrintArray() {
        String[] array = {"element1", "element2", "element3"};
        classInfo = new ClassInfo();
        classInfo.printArray(array);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintArrayWithNullArray() {
        classInfo = new ClassInfo();
        classInfo.printArray(null);
    }

    static class MethodData {
        String tempStr;
        int begin;
        int end;
        String className;
        String methodName;

        public String getMethPackage() {
            return className.substring(0, className.lastIndexOf("."));
        }

        public String getMethClass() {
            return className.substring(className.lastIndexOf(".") + 1);
        }

        public String getMethName() {
            return methodName;
        }
    }

    static class AbstractClass {}

    static class InterfaceClass implements Serializable {}
}