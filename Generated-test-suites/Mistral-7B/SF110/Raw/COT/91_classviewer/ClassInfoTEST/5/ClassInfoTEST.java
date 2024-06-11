package com.jstevh.viewer;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ClassInfoTest {

    private static final String TEST_PACKAGE = "com.jstevh.viewer.test";
    private static final String TEST_CLASS_NAME = "TestClass";
    private static final String TEST_INTERFACE_NAME = "TestInterface";
    private static final String TEST_SUPERCLASS_NAME = "TestSuperClass";
    private static final String TEST_DIR = "src/test/java";
    private static final ClassInfo testClassInfo;
    private static final DirManager testDirManager = new DirManager(TEST_DIR);

    static {
        try {
            testClassInfo = new ClassInfo(TEST_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Before
    public void setUp() {
        testClassInfo.setDebug(true);
        testClassInfo.setDirManager(testDirManager);
    }

    @Test
    public void testIsAbstract() {
        assertFalse(testClassInfo.isAbstract());

        Class<AbstractTestClass> abstractTestClass = AbstractTestClass.class;
        testClassInfo = new ClassInfo(abstractTestClass.getName());
        assertTrue(testClassInfo.isAbstract());
    }

    @Test
    public void testIsInterface() {
        assertFalse(testClassInfo.isInterface());

        Class<TestInterface> testInterface = TestInterface.class;
        testClassInfo = new ClassInfo(testInterface.getName());
        assertTrue(testClassInfo.isInterface());
    }

    @Test
    public void testGetClassName() {
        assertEquals(WindowConstants.class.getName(), testClassInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() {
        assertEquals(ClassInfo.class.getPackage().getName(), testClassInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        assertNull(testClassInfo.getSuperClassName());

        Class<TestSuperClass> testSuperClass = TestSuperClass.class;
        testClassInfo = new ClassInfo(testSuperClass.getName());
        assertEquals(TestSuperClass.class.getName(), testClassInfo.getSuperClassName());
    }

    @Test
    public void testConstructor() throws Exception {
        Class<TestClass> testClass = TestClass.class;
        testClassInfo = new ClassInfo(testClass.getName());

        assertNull(testClassInfo.cMethods);
        assertFalse(testClassInfo.isAbstract());

        Class<TestInterface> testInterface = TestInterface.class;
        testClassInfo = new ClassInfo(testInterface.getName());
        assertNull(testClassInfo.cMethods);
        assertTrue(testClassInfo.isAbstract());

        Class<?> testSuperClass = Class.forName(TEST_SUPERCLASS_NAME);
        testClassInfo = new ClassInfo(testSuperClass.getName());
        assertNull(testClassInfo.cMethods);
        assertFalse(testClassInfo.isAbstract());

        Class<?> unknownClass = Class.forName("UnknownClass");
        testClassInfo = new ClassInfo(unknownClass.getName());
        assertNull(testClassInfo.cMethods);
        assertFalse(testClassInfo.isAbstract());

        String invalidClassName = "123";
        testClassInfo = new ClassInfo(invalidClassName);
        assertNull(testClassInfo.cMethods);
        assertFalse(testClassInfo.isAbstract());
        assertThrows(ClassNotFoundException.class, () -> testClassInfo.c = testClassInfo.getClass(invalidClassName));
    }

    @Test
    public void testPrintFields() {
        Class<TestClassWithFields> testClassWithFields = TestClassWithFields.class;
        testClassInfo = new ClassInfo(testClassWithFields.getName(), testDirManager);

        String[] expectedFields = new String[] {"field1=int value=1", "field2=double value=3.14", "field3=String value=test"};
        assertArrayEquals(expectedFields, testClassInfo.printFields());
    }

    @Test
    public void testPrintMethods() {
        Class<TestClassWithMethods> testClassWithMethods = TestClassWithMethods.class;
        testClassInfo = new ClassInfo(testClassWithMethods.getName(), testDirManager);

        String[] expectedMethods = new String[] {"method1=public void method1()", "method2=public void method2(int arg1)",
                "method3=public static void method3()", "method4=public void method4(String arg1)"};
        assertArrayEquals(expectedMethods, testClassInfo.printMethods());
    }

    @Test
    public void testPrintMethods_filter() {
        Class<TestClassWithMethods> testClassWithMethods = TestClassWithMethods.class;
        testClassInfo = new ClassInfo(testClassWithMethods.getName(), testDirManager);

        String[] expectedMethods = new String[] {"method3=public static void method3()"};
        assertArrayEquals(expectedMethods, testClassInfo.printMethods(Modifier.STATIC));
    }

    @Test
    public void testPrintConstructors() {
        Class<TestClassWithConstructors> testClassWithConstructors = TestClassWithConstructors.class;
        testClassInfo = new ClassInfo(testClassWithConstructors.getName(), testDirManager);

        String[] expectedConstructors = new String[] {"TestClassWithConstructors.<init>()", "TestClassWithConstructors.<init>(int arg1)",
                "TestClassWithConstructors.<init>(String arg1)"};
        assertArrayEquals(expectedConstructors, testClassInfo.printConstructors());
    }

    @Test
    public void testPrintInterfaces() {
        Class<TestClassWithInterfaces> testClassWithInterfaces = TestClassWithInterfaces.class;
        testClassInfo = new ClassInfo(testClassWithInterfaces.getName());

        String[] expectedInterfaces = new String[] {"TestInterface1.class", "TestInterface2.class"};
        assertArrayEquals(expectedInterfaces, testClassInfo.printInterfaces());
    }

    @Test
    public void testSrchMethods() {
        Class<TestClassWithMethods> testClassWithMethods = TestClassWithMethods.class;
        testClassInfo = new ClassInfo(testClassWithMethods.getName(), testDirManager);

        String searchString = "method";
        String[] expectedMethods = testClassInfo.printMethods();
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < expectedMethods.length; ++i) {
            if (expectedMethods[i].contains(searchString)) {
                positions.add(i);
            }
        }
        String[] foundMethods = testClassInfo.srchMethods(searchString);
        assertArrayEquals(foundMethods, Arrays.copyOfRange(expectedMethods, 0, positions.size()));
    }

    @Test
    public void testSrchMethods_withIndex() {
        Class<TestClassWithMethods> testClassWithMethods = TestClassWithMethods.class;
        testClassInfo = new ClassInfo(testClassWithMethods.getName(), testDirManager);

        String searchString = "method";
        String[] expectedMethods = testClassInfo.printMethods();
        int index = 1;
        String[] foundMethods = testClassInfo.srchMethods(searchString, new String[] {"method1"});
        assertArrayEquals(foundMethods, new String[] {expectedMethods[index]});
    }

    @Test(expected = NullPointerException.class)
    public void testGetFoundMethod_invalidPosition() {
        int invalidPosition = testClassInfo.cMethods.length + 1;
        MethodData methodData = testClassInfo.getFoundMethod(invalidPosition);
        fail("Expected NullPointerException");
    }
}

class MethodData {
    String tempStr;
    int begin;
    int end;

    public String getMethPackage() {
        return "";
    }

    public String getMethClass() {
        return "";
    }

    public String getMethName() {
        return "";
    }
}

class AbstractTestClass {
    // An abstract class
}

class TestClass {
    // A regular class
}

class TestInterface {
    // An interface
}

class TestSuperClass {
    // A superclass
}

class TestClassWithFields {
    int field1;
    double field2;
    String field3;
}

class TestClassWithMethods {
    public void method1() {}
    public void method2(int arg1) {}
    public static void method3() {}
    public void method4(String arg1) {}
}

class TestClassWithConstructors {
    public TestClassWithConstructors() {}
    public TestClassWithConstructors(int arg1) {}
    public TestClassWithConstructors(String arg1) {}
}

class TestClassWithInterfaces implements TestInterface1, TestInterface2 {
    // A class that implements multiple interfaces
}