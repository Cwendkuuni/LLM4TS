package com.jstevh.viewer.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jstevh.viewer.ClassInfo;
import com.jstevh.viewer.ClassInfo.MethodData;
import com.jstevh.viewer.ClassInfoTestUtils;
import com.jstevh.viewer.StringTools;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class ClassInfoTest {

    private static final boolean DEBUG = false;
    private static final String PACKAGE = "com.jstevh.viewer.test";
    private static final String CLASS_NAME = "ClassInfoTest";
    private static final String INTERFACE_CLASS_NAME = "MockInterface";
    private static final String ABSTRACT_CLASS_NAME = "MockAbstractClass";
    private static final String NORMAL_CLASS_NAME = "MockClass";

    @Parameters(name = "{index}: testClassInfo-{0}")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {NORMAL_CLASS_NAME},
                {INTERFACE_CLASS_NAME},
                {ABSTRACT_CLASS_NAME}
        });
    }

    private ClassInfo classInfo;
    private ClassInfo classInfoWithDirManager;

    @Before
    public void setUp() throws Exception {
        classInfo = new ClassInfo(CLASS_NAME);
        classInfoWithDirManager = new ClassInfo(CLASS_NAME, new DirManager());
    }

    @Test
    public void testIsAbstract() throws Exception {
        final boolean isAbstract = new ClassInfo(ABSTRACT_CLASS_NAME).isAbstract();
        assertTrue(isAbstract);

        final boolean notAbstract = new ClassInfo(NORMAL_CLASS_NAME).isAbstract();
        assertFalse(notAbstract);
    }

    @Test
    public void testIsInterface() throws Exception {
        final boolean isInterface = new ClassInfo(INTERFACE_CLASS_NAME).isInterface();
        assertTrue(isInterface);

        final boolean notInterface = new ClassInfo(NORMAL_CLASS_NAME).isInterface();
        assertFalse(notInterface);
    }

    @Test
    public void testGetClassName() throws Exception {
        String className = classInfo.getClassName();
        assertEquals(CLASS_NAME, className);

        className = classInfoWithDirManager.getClassName();
        assertEquals(CLASS_NAME, className);
    }

    @Test
    public void testGetClassPackage() throws Exception {
        String packageName = classInfo.getClassPackage();
        assertNull(packageName);

        packageName = classInfoWithDirManager.getClassPackage();
        assertEquals(PACKAGE, packageName);
    }

    @Test
    public void testGetSuperClassName() throws Exception {
        Class superClass = classInfo.getSuperClassName();
        assertNull(superClass);

        superClass = classInfoWithDirManager.getSuperClassName();
        assertNull(superClass);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testConstructorWithClassNotFoundException() throws Exception {
        new ClassInfo("NonExistentClass");
    }

    @Test
    public void testConstructor() throws Exception {
        ClassInfo classInfo = new ClassInfo(CLASS_NAME);
        Class expectedClass = Class.forName(CLASS_NAME);
        assertSame(expectedClass, classInfo.c);

        String[] methods = classInfo.printMethods();
        assertArrayEquals(new String[]{}, methods);

        String[] constructors = classInfo.printConstructors();
        assertArrayEquals(new String[]{}, constructors);

        String[] interfaces = classInfo.printInterfaces();
        assertArrayEquals(new String[]{}, interfaces);
    }

    @Test
    public void testConstructorWithDirManager() throws Exception {
        String[] methods = classInfoWithDirManager.printMethods();
        assertArrayEquals(new String[]{}, methods);

        String[] constructors = classInfoWithDirManager.printConstructors();
        assertArrayEquals(new String[]{}, constructors);

        String[] interfaces = classInfoWithDirManager.printInterfaces();
        assertArrayEquals(new String[]{}, interfaces);

        String[] classPackage = classInfoWithDirManager.getClassPackage();
        assertEquals(PACKAGE, classPackage);
    }

    @Test
    public void testPrintFields() throws Exception {
        String[] fields = classInfoWithDirManager.printFields();
        assertArrayEquals(new String[]{}, fields);
    }

    @Test
    public void testPrintMethods() throws Exception {
        String[] methods = classInfo.printMethods();
        assertArrayEquals(new String[]{}, methods);

        methods = classInfoWithDirManager.printMethods();
        assertArrayEquals(new String[]{}, methods);
    }

    @Test
    public void testPrintMethodsWithParam() throws Exception {
        String[] methods = classInfo.printMethods("MethodName");
        assertArrayEquals(new String[]{}, methods);

        methods = classInfoWithDirManager.printMethods("MethodName");
        assertArrayEquals(new String[]{}, methods);
    }

    @Test
    public void testPrintInterfaces() throws Exception {
        String[] interfaces = classInfo.printInterfaces();
        assertArrayEquals(new String[]{}, interfaces);

        interfaces = classInfoWithDirManager.printInterfaces();
        assertArrayEquals(new String[]{}, interfaces);
    }

    @Test
    public void testSrchMethods() throws Exception {
        String[] methods = classInfoWithDirManager.srchMethods("MethodName");
        assertArrayEquals(new String[]{}, methods);
    }

    @Test
    public void testSrchMethodsWithIndex() throws Exception {
        String[] methods = classInfoWithDirManager.srchMethods("MethodName", new String[]{"void", "testMethod"});
        assertArrayEquals(new String[]{}, methods);
    }

    @Test
    public void testGetFoundMethod() throws Exception {
        int pos = 0;
        MethodData methodData = classInfoWithDirManager.getFoundMethod(pos);
        assertNull(methodData);

        String[] methods = classInfoWithDirManager.printMethods();
        if (methods.length > pos) {
            methodData = classInfoWithDirManager.getFoundMethod(pos);
            assertEquals(METHOD_NAME, methodData.getMethName());
        }
    }

    @Test
    public void testPrintArray() throws Exception {
        Object[] array = new Object[0];
        classInfo.printArray(array);
    }

    @Test
    public void testGetClass() throws Exception {
        Class expectedClass = Class.forName(CLASS_NAME);
        Class resultClass = classInfo.getClass(CLASS_NAME);
        assertSame(expectedClass, resultClass);

        expectedClass = Class.forName(PACKAGE + "." + INTERFACE_CLASS_NAME);
        resultClass = classInfo.getClass(INTERFACE_CLASS_NAME);
        assertSame(expectedClass, resultClass);

        resultClass = classInfo.getClass(ABSTRACT_CLASS_NAME);
        assertNull(resultClass);
    }

    @Test
    public void testTryClass() throws Exception {
        Class expectedClass = Class.forName(CLASS_NAME);
        Class resultClass = ClassInfo.tryClass(CLASS_NAME);
        assertSame(expectedClass, resultClass);

        resultClass = ClassInfo.tryClass(PACKAGE + "." + INTERFACE_CLASS_NAME);
        assertSame(expectedClass, resultClass);

        resultClass = ClassInfo.tryClass(ABSTRACT_CLASS_NAME);
        assertNull(resultClass);
    }

    @Test
    public void testPrintClassInfo() throws Exception {
        classInfoWithDirManager.printClassInfo();
    }

    @Test
    public void testMain() throws Exception {
        ClassInfoTestUtils.runMain(ClassInfo.class, new String[]{"new", CLASS_NAME});
    }

    private static String METHOD_NAME = "testMethod";
}