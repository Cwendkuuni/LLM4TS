package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import javax.swing.JOptionPane;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.StringJoiner;

import org.junit.Test;

import com.jstevh.tools.StringTools;
import com.jstevh.viewer.ClassInfo;
import com.jstevh.viewer.DirManager;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        boolean actual = classInfo.isAbstract();
        assertFalse(actual);
    }

    @Test
    public void testIsInterface() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        boolean actual = classInfo.isInterface();
        assertFalse(actual);
    }

    @Test
    public void testGetClassName() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String actual = classInfo.getClassName();
        String expected = "java.lang.String";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetClassPackage() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String actual = classInfo.getClassPackage();
        String expected = "java.lang";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        String actual = classInfo.getSuperClassName();
        String expected = null;
        assertEquals(expected, actual);

        classInfo = new ClassInfo("java.lang.String");
        actual = classInfo.getSuperClassName();
        expected = "java.lang.Object";
        assertEquals(expected, actual);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testConstructorWithInvalidClassNameThrowsClassNotFoundException() throws Exception {
        new ClassInfo("java.lang.XString");
    }

    @Test(expected = ClassNotFoundException.class)
    public void testConstructorWithNullNameThrowsClassNotFoundException() throws Exception {
        new ClassInfo(null);
    }

    @Test
    public void testConstructorWithValidClassNameInitializesClassInfo() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
    }

    @Test
    public void testConstructorWithValidClassNameAndDirManagerInitializesClassInfo() throws Exception {
        DirManager dirManager = new DirManager();
        ClassInfo classInfo = new ClassInfo("java.lang.String", dirManager);
    }

    @Test
    public void testPrintFields() {
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        String[] actual = classInfo.printFields();
        assertNull(actual);

        classInfo = new ClassInfo("java.lang.String");
        StringJoiner joiner = new StringJoiner("\n", "\n", "");
        Field[] fields = String.class.getFields();
        for (Field field : fields) {
            joiner.add(field.getName() + "=" + field.get(new Object()));
        }
        String[] expected = joiner.toString().split("\n");
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < actual.length; ++i) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        String[] actual = classInfo.printMethods();
        assertNull(actual);

        classInfo = new ClassInfo("java.lang.String");
        Method[] methods = String.class.getMethods();
        StringJoiner joiner = new StringJoiner("\n", "\n", "");
        for (Method method : methods) {
            joiner.add(method.toGenericString());
        }
        String[] expected = joiner.toString().split("\n");
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < actual.length; ++i) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testPrintMethodsWithParam() {
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        String[] actual = classInfo.printMethods("println");
        assertNull(actual);

        classInfo = new ClassInfo("java.lang.String");
        Method[] methods = String.class.getMethods();
        StringJoiner joiner = new StringJoiner("\n", "\n", "");
        for (Method method : methods) {
            if (method.getName().equals("println")) {
                joiner.add(method.toGenericString());
                break;
            }
        }
        String[] expected = joiner.toString().split("\n");
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < actual.length; ++i) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        String[] actual = classInfo.printConstructors();
        assertNull(actual);

        classInfo = new ClassInfo("java.lang.String");
        Constructor[] constructors = String.class.getConstructors();
        StringJoiner joiner = new StringJoiner("\n", "\n", "");
        for (Constructor constructor : constructors) {
            joiner.add(constructor.toGenericString());
        }
        String[] expected = joiner.toString().split("\n");
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < actual.length; ++i) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        String[] actual = classInfo.printInterfaces();
        assertNull(actual);

        classInfo = new ClassInfo("java.lang.String");
        Class[] interfaces = String.class.getInterfaces();
        StringJoiner joiner = new StringJoiner("\n", "\n", "");
        for (Class interfaceClass : interfaces) {
            joiner.add(interfaceClass.getName());
        }
        String[] expected = joiner.toString().split("\n");
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < actual.length; ++i) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testSrchMethods() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] actual = classInfo.srchMethods("toString");
        assertNull(actual);

        String[] expected = new String[] {"public java.lang.String toString()"};
        String[] actual2 = classInfo.srchMethods("toString", new String[] {"java.lang.StringBuilder"});
        assertNull(actual2);

        String[] actual3 = classInfo.srchMethods("toString", new String[] {"java.lang.StringBuilder", "java.lang.StringBuffer"});
        assertEquals(expected.length, actual3.length);
        for (int i = 0; i < actual3.length; ++i) {
            assertEquals(expected[i], actual3[i]);
        }
    }

    @Test
    public void testGetFoundMethod() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        MethodData actual = classInfo.getFoundMethod(0);
        assertNull(actual);

        Method[] methods = String.class.getMethods();
        MethodData expected = new MethodData() {
            String tempStr = "public java.lang.String toString()";
            @Override
            public String getMethPackage() {
                return "java.lang";
            }

            @Override
            public String getMethClass() {
                return "java.lang.String";
            }

            @Override
            public String getMethName() {
                return "toString";
            }
        };
        MethodData actual2 = classInfo.getFoundMethod(0);
        assertEquals(expected, actual2);
    }
}

class MethodData {
    String tempStr;
    int begin;
    int end;

    public interface MethodData {
        String getMethPackage();
        String getMethClass();
        String getMethName();
    }
}