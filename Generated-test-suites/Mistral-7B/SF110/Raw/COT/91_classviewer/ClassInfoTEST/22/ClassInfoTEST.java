package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;

import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isAbstract());

        classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
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

        classInfo = new ClassInfo("com.jstevh.viewer.ClassInfo");
        assertEquals("com.jstevh.viewer", classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.lang.String");
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.Collection", classInfo.getSuperClassName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testConstructorWithInvalidClassName() throws ClassNotFoundException {
        new ClassInfo("InvalidClass");
    }

    @Test
    public void testPrintFields() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] fields = classInfo.printFields();

        assertNotNull(fields);
        assertEquals(10, fields.length);

        Class<?> clazz = Class.forName("java.util.ArrayList");
        Field[] declaredFields = clazz.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            String[] parts = field.split("=");
            String fieldName = parts[0];
            Field declaredField = Arrays.stream(declaredFields).filter(f -> f.getName().equals(fieldName)).findFirst().orElse(null);
            String fieldValue = parts[1].substring(1, parts[1].length() - 1).replaceAll("\\[", "").replaceAll("\\]", "");

            assertEquals(declaredField.getType().getName(), getClass(fieldValue));
        }
    }

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] methods = classInfo.printMethods();

        assertNotNull(methods);
        assertEquals(26, methods.length);

        Class<?> clazz = Class.forName("java.util.ArrayList");
        Method[] declaredMethods = clazz.getDeclaredMethods();

        for (int i = 0; i < methods.length; i++) {
            String method = methods[i];
            String[] parts = method.split("(");
            String methodName = parts[0];
            Method declaredMethod = Arrays.stream(declaredMethods).filter(m -> m.getName().equals(methodName)).findFirst().orElse(null);

            if (declaredMethod == null) {
                continue;
            }

            Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
            String[] parameterNames = declaredMethod.getParameterNames();
            Class<?> returnType = declaredMethod.getReturnType();

            String expectedMethodSignature = methodName + "(";
            for (int j = 0; j < parameterTypes.length; j++) {
                expectedMethodSignature += parameterTypes[j].getName();
                if (j < parameterTypes.length - 1) {
                    expectedMethodSignature += ", ";
                }
            }
            expectedMethodSignature += ")";

            assertEquals(expectedMethodSignature, method);
        }
    }

    @Test
    public void testPrintMethodsWithParam() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] methods = classInfo.printMethods("ArrayList");

        assertNotNull(methods);
        assertEquals(2, methods.length);

        Method method = Arrays.stream(classInfo.c.getDeclaredMethods())
                .filter(m -> m.getName().equals("ensureCapacity") && m.isAccessible())
                .findFirst().orElse(null);

        assertEquals(method.toString(), methods[0]);
    }

    @Test
    public void testPrintConstructors() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] constructors = classInfo.printConstructors();

        assertNotNull(constructors);
        assertEquals(1, constructors.length);

        Class<?> clazz = Class.forName("java.util.ArrayList");
        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();

        String expectedConstructorSignature = "(" + getTypeName("int") + ", " + getTypeName("java.util.Collection<capture#1-of ? extends java.util.Collection<capture#2-of ? extends java.lang.Object>>") + ")";

        assertEquals(expectedConstructorSignature, constructors[0]);
    }

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] interfaces = classInfo.printInterfaces();

        assertNotNull(interfaces);
        assertEquals(1, interfaces.length);
        assertEquals("java.util.Collection", interfaces[0]);
    }

    @Test
    public void testSrchMethods() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");

        String[] methods = classInfo.c.getDeclaredMethods();
        Method method = Arrays.stream(methods).filter(m -> m.getName().equals("add")).findFirst().orElse(null);

        String[] foundMethods = classInfo.srchMethods(method.getName());
        assertNotNull(foundMethods);
        assertEquals(1, foundMethods.length);
        assertEquals(method.toString(), foundMethods[0]);
    }

    @Test
    public void testSrchMethodsWithIndex() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");

        String[] methods = classInfo.c.getDeclaredMethods();
        Method method = Arrays.stream(methods).filter(m -> m.getName().equals("add")).findFirst().orElse(null);

        String[] indexes = new String[]{"1"};
        String[] foundMethods = classInfo.srchMethods(method.getName(), indexes);
        assertNotNull(foundMethods);
        assertEquals(1, foundMethods.length);
        assertEquals(method.toString(), foundMethods[0]);
    }

    private Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private String getTypeName(String className) {
        return "class [" + className + "]";
    }
}