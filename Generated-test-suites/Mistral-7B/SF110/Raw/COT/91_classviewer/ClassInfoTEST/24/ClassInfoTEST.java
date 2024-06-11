package com.jstevh.viewer;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class ClassInfoTest {

    private ClassInfo classInfo;
    private String className;
    private boolean isInterfaceExpected;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                { "java.lang.String", false },
                { "java.util.ArrayList", false },
                { "java.util.Hashtable", true },
                { "com.jstevh.viewer.ClassInfoTest", false }
        });
    }

    public ClassInfoTest(String className, boolean isInterfaceExpected) {
        this.className = className;
        this.isInterfaceExpected = isInterfaceExpected;
    }

    @Before
    public void setUp() throws Exception {
        classInfo = new ClassInfo(className);
    }

    @Test
    public void testIsAbstract() {
        assertFalse(classInfo.isAbstract());
    }

    @Test
    public void testIsInterface() throws Exception {
        assertEquals(isInterfaceExpected, classInfo.isInterface());
    }

    @Test
    public void testGetClassName() throws Exception {
        assertEquals(className, classInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() throws Exception {
        if (className.indexOf(".") > 0) {
            String expected = className.substring(0, className.lastIndexOf("."));
            assertEquals(expected, classInfo.getClassPackage());
        } else {
            assertEquals(null, classInfo.getClassPackage());
        }
    }

    @Test
    public void testGetSuperClassName() throws Exception {
        if (!className.equals(Object.class.getName())) {
            String superClassName = classInfo.getSuperClassName();
            if (superClassName != null) {
                assertEquals(Class.forName(superClassName).getName(), getSuperclassName(className));
            } else {
                assertEquals(null, classInfo.getSuperClassName());
            }
        }
    }

    @Test(expected = ClassNotFoundException.class)
    public void testConstructorWithInvalidClassName() throws Exception {
        new ClassInfo("InvalidClassName");
    }

    @Test
    public void testPrintFields() throws Exception {
        String[] fields = classInfo.printFields();
        if (fields != null) {
            assertNotNull(fields);
            assertTrue(fields.length > 0);
            for (String field : fields) {
                assertTrue(field.contains("="));
            }
        } else {
            assertEquals(0, classInfo.printFields().length);
        }
    }

    @Test
    public void testPrintMethods() throws Exception {
        String[] methods = classInfo.printMethods();
        if (methods != null) {
            assertNotNull(methods);
            assertTrue(methods.length > 0);
            for (String method : methods) {
                assertTrue(method.contains("("));
                assertTrue(method.contains(")"));
            }
        } else {
            assertEquals(0, classInfo.printMethods().length);
        }
    }

    @Test
    public void testPrintMethodsWithParam() throws Exception {
        String[] methods = classInfo.printMethods(className);
        if (methods != null) {
            assertNotNull(methods);
            assertTrue(methods.length > 0);
            for (String method : methods) {
                assertTrue(method.contains(className + "."));
            }
        } else {
            assertEquals(0, classInfo.printMethods(className).length);
        }
    }

    @Test
    public void testPrintConstructors() throws Exception {
        String[] constructors = classInfo.printConstructors();
        if (constructors != null) {
            assertNotNull(constructors);
            assertTrue(constructors.length > 0);
            for (String constructor : constructors) {
                assertTrue(constructor.contains("("));
                assertTrue(constructor.contains(")"));
            }
        } else {
            assertEquals(0, classInfo.printConstructors().length);
        }
    }

    @Test
    public void testPrintInterfaces() throws Exception {
        String[] interfaces = classInfo.printInterfaces();
        if (interfaces != null) {
            assertNotNull(interfaces);
            assertTrue(interfaces.length > 0);
            for (String interfaceName : interfaces) {
                assertEquals(Class.forName(interfaceName).getName(), interfaceName);
            }
        } else {
            assertEquals(0, classInfo.printInterfaces().length);
        }
    }

    @Test(expected = NoSuchMethodException.class)
    public void testSrchMethodsWithInvalidString() throws Exception {
        classInfo.srchMethods(null);
    }

    @Test
    public void testSrchMethods() throws Exception {
        String[] methods = classInfo.srchMethods("toString");
        if (methods != null) {
            assertNotNull(methods);
            assertTrue(methods.length > 0);
            for (String method : methods) {
                Method methodObj = classInfo.c.getMethod(method);
                assertEquals(methodObj.getName(), method);
                assertEquals(methodObj.getReturnType().getName(), "java.lang.String");
            }
        } else {
            assertEquals(0, classInfo.srchMethods("toString").length);
        }
    }

    @Test
    public void testGetFoundMethod() throws Exception {
        MethodData methodData = classInfo.getFoundMethod(0);
        assertNotNull(methodData);
        assertEquals(className, methodData.getMethPackage());
        assertEquals(className, methodData.getMethClass());
        assertEquals("toString", methodData.getMethName());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFoundMethodWithInvalidIndex() throws Exception {
        classInfo.getFoundMethod(classInfo.cMethods.length);
    }

    @Test
    public void testConstructorWithDirManager() throws Exception {
        ClassInfo classInfoWithDirManager = new ClassInfo(className, new DirManager());
        assertNotNull(classInfoWithDirManager);
    }

    private static String getSuperclassName(String className) {
        try {
            return Class.forName(className).getSuperclass().getName();
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}