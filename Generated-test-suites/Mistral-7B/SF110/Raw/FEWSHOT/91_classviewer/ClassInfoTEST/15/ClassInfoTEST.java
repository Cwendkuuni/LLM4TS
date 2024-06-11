package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.StringJoiner;

import org.junit.Test;

import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo ci = new ClassInfo(String.class.getName());
        assertTrue(ci.isAbstract() == false);

        ci = new ClassInfo(AbstractClass.class.getName());
        assertTrue(ci.isAbstract() == true);
    }

    @Test
    public void testIsInterface() {
        ClassInfo ci = new ClassInfo(String.class.getName());
        assertFalse(ci.isInterface());

        ci = new ClassInfo(Interface.class.getName());
        assertTrue(ci.isInterface());
    }

    @Test
    public void testGetClassName() throws Exception {
        ClassInfo ci = new ClassInfo(String.class.getName());
        assertEquals(String.class.getName(), ci.getClassName());

        ci = new ClassInfo(ClassInfo.class.getName());
        assertEquals(ClassInfo.class.getName(), ci.getClassName());
    }

    @Test
    public void testGetClassPackage() throws Exception {
        ClassInfo ci = new ClassInfo(String.class.getName());
        assertNull(ci.getClassPackage());

        ci = new ClassInfo(StringBuilder.class.getName());
        assertEquals(StringBuilder.class.getPackage().getName(), ci.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() throws Exception {
        ClassInfo ci = new ClassInfo(String.class.getName());
        assertNull(ci.getSuperClassName());

        ci = new ClassInfo(ArrayList.class.getName());
        assertEquals(ArrayList.class.getSuperclass().getName(), ci.getSuperClassName());
    }

    @Test
    public void testPrintFields() throws Exception {
        ClassInfo ci = new ClassInfo(String.class.getName());
        assertArrayEquals(new String[0], ci.printFields());

        ci = new ClassInfo(ComplexNumber.class.getName());
        StringJoiner joiner = new StringJoiner(", ", "\n", "\n");
        String[] expected = { "real=0.0", "imaginary=0.0" };
        assertArrayEquals(expected, ci.printFields());
    }

    @Test
    public void testPrintMethods() throws Exception {
        ClassInfo ci = new ClassInfo(String.class.getName());
        assertArrayEquals(new String[0], ci.printMethods());

        ci = new ClassInfo(ComplexNumber.class.getName());
        StringJoiner joiner = new StringJoiner("\n", "\n", "");
        String[] expected = { "add(ComplexNumber)", "subtract(ComplexNumber)", "multiply(ComplexNumber)", "divide(ComplexNumber)", "toString()" };
        assertArrayEquals(expected, ci.printMethods());
    }

    @Test
    public void testPrintMethodsWithParam() throws Exception {
        ClassInfo ci = new ClassInfo(ComplexNumber.class.getName());
        assertArrayEquals(new String[0], ci.printMethods(1));

        ci = new ClassInfo(ComplexNumber.class.getName());
        StringJoiner joiner = new StringJoiner("\n", "\n", "");
        String[] expected = { "add(ComplexNumber)", "subtract(ComplexNumber)", "multiply(ComplexNumber)", "divide(ComplexNumber)", "toString()" };
        assertArrayEquals(expected, ci.printMethods(0));
    }

    @Test
    public void testPrintConstructors() throws Exception {
        ClassInfo ci = new ClassInfo(String.class.getName());
        assertArrayEquals(new String[0], ci.printConstructors());

        ci = new ClassInfo(ComplexNumber.class.getName());
        StringJoiner joiner = new StringJoiner("\n", "\n", "");
        String[] expected = { "ComplexNumber()", "ComplexNumber(double, double)" };
        assertArrayEquals(expected, ci.printConstructors());
    }

    @Test
    public void testPrintInterfaces() throws Exception {
        ClassInfo ci = new ClassInfo(String.class.getName());
        assertArrayEquals(new String[0], ci.printInterfaces());

        ci = new ClassInfo(MyList.class.getName());
        StringJoiner joiner = new StringJoiner("\n", "\n", "");
        String[] expected = { "List", "RandomAccess" };
        assertArrayEquals(expected, ci.printInterfaces());
    }

    private static class AbstractClass {}

    private static interface Interface {}

    private static class ComplexNumber {
        double real, imaginary;

        ComplexNumber add(ComplexNumber c) {
            real += c.real;
            imaginary += c.imaginary;
            return this;
        }

        ComplexNumber subtract(ComplexNumber c) {
            real -= c.real;
            imaginary -= c.imaginary;
            return this;
        }

        ComplexNumber multiply(ComplexNumber c) {
            real *= c.real;
            imaginary *= c.imaginary;
            return this;
        }

        ComplexNumber divide(ComplexNumber c) {
            double divisor = c.real * c.real + c.imaginary * c.imaginary;
            real /= divisor;
            imaginary /= divisor;
            return this;
        }

        @Override
        public String toString() {
            return String.format("(%f, %f)", real, imaginary);
        }
    }

    private static class MyList extends ArrayList<Object> {}
}