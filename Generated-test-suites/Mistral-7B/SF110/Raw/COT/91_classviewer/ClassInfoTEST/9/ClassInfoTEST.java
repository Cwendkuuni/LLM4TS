package com.jstevh.viewer;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isAbstract());

        ClassInfo interfaceClassInfo = new ClassInfo("java.lang.Runnable");
        assertTrue(interfaceClassInfo.isAbstract());
    }

    @Test
    public void testIsInterface() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isInterface());

        ClassInfo interfaceClassInfo = new ClassInfo("java.lang.Runnable");
        assertTrue(interfaceClassInfo.isInterface());
    }

    @Test
    public void testGetClassName() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertEquals("java.lang.String", classInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertEquals("java.lang", classInfo.getClassPackage());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testGetClassNameThrowsException() {
        new ClassInfo("nonexistentclass");
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.lang.Object", classInfo.getSuperClassName());

        ClassInfo classInfo2 = new ClassInfo("java.util.LinkedList");
        assertEquals("java.util.AbstractSequentialList", classInfo2.getSuperClassName());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullName() {
        new ClassInfo(null);
    }

    @Test
    public void testPrintFields() {
        try {
            new ClassInfo("java.util.ArrayList");
            String[] expectedFields = new String[]{"private static final int serialVersionUID = 8683452581122892189L",
                    "private transient Object elementData", "private int size", "private int modCount"};
            String[] actualFields = new ClassInfo("java.util.ArrayList").printFields();
            assertArrayEquals(expectedFields, actualFields);
        } catch (ClassNotFoundException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testPrintMethods() {
        try {
            new ClassInfo("java.util.ArrayList");
            String[] expectedMethods = new String[]{"public synchronized boolean add(E e)", "public synchronized boolean addAll(Collection<? extends E> c)", "public synchronized boolean addAll(int index, Collection<? extends E> c)", "public synchronized void clear()", "public synchronized boolean contains(Object o)", "public synchronized boolean containsAll(Collection<?> c)", "public synchronized boolean equals(Object obj)", "public synchronized int hashCode()", "public synchronized int indexOf(Object o)", "public synchronized boolean isEmpty()", "public synchronized int lastIndexOf(Object o)", "public synchronized ListIterator<E> listIterator()", "public synchronized ListIterator<E> listIterator(int index)", "public synchronized boolean remove(Object o)", "public synchronized boolean removeAll(Collection<?> c)", "public synchronized boolean retainAll(Collection<?> c)", "public synchronized void trimToSize()", "public synchronized void add(int index, E element)", "public synchronized void add(int index, E element) throws IndexOutOfBoundsException", "public synchronized void set(int index, E element)", "public synchronized E get(int index)", "public synchronized E set(int index, E element)", "public synchronized void add(E e)", "public synchronized void add(int index, E element)", "public synchronized void remove(int index)", "public synchronized E remove(int index)", "public synchronized E get(int index)", "public synchronized E set(int index, E element)", "public synchronized int size()", "public synchronized boolean isEmpty()", "public synchronized boolean contains(Object o)", "public synchronized Iterator<E> iterator()", "public synchronized List<E> subList(int fromIndex, int toIndex)", "public synchronized Spliterator<E> spliterator()"};
            String[] actualMethods = new ClassInfo("java.util.ArrayList").printMethods();
            assertArrayEquals(expectedMethods, actualMethods);
        } catch (ClassNotFoundException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testPrintMethodsWithParam() {
        try {
            new ClassInfo("java.util.ArrayList");
            String[] expectedMethods = new String[]{"public synchronized void add(int index, E element)"};
            String[] actualMethods = new ClassInfo("java.util.ArrayList").printMethods(1);
            assertArrayEquals(expectedMethods, actualMethods);
        } catch (ClassNotFoundException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testPrintConstructors() {
        try {
            new ClassInfo("java.util.ArrayList");
            String[] expectedConstructors = new String[]{"public ArrayList()", "public ArrayList(int initialCapacity)", "public ArrayList(int initialCapacity, float loadFactor)", "public ArrayList(Collection<? extends E> c)"};
            String[] actualConstructors = new ClassInfo("java.util.ArrayList").printConstructors();
            assertArrayEquals(expectedConstructors, actualConstructors);
        } catch (ClassNotFoundException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testPrintInterfaces() {
        try {
            new ClassInfo("java.util.ArrayList");
            String[] expectedInterfaces = new String[]{"java.io.Serializable"};
            String[] actualInterfaces = new ClassInfo("java.util.ArrayList").printInterfaces();
            assertArrayEquals(expectedInterfaces, actualInterfaces);
        } catch (ClassNotFoundException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSrchMethods() {
        try {
            new ClassInfo("java.util.ArrayList");
            String[] expectedMethods = new String[]{"public synchronized void add(int index, E element)"};
            String[] actualMethods = new ClassInfo("java.util.ArrayList").srchMethods("add", null)[0];
            assertArrayEquals(expectedMethods, actualMethods);
        } catch (ClassNotFoundException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSrchMethodsWithIndex() {
        try {
            new ClassInfo("java.util.ArrayList");
            String[] expectedMethods = new String[]{"public synchronized void add(int index, E element)"};
            String[] actualMethods = new ClassInfo("java.util.ArrayList").srchMethods("add", new String[]{"int"})[0];
            assertArrayEquals(expectedMethods, actualMethods);
        } catch (ClassNotFoundException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetFoundMethod() {
        try {
            new ClassInfo("java.util.ArrayList");
            MethodData actualMethod = new ClassInfo("java.util.ArrayList").getFoundMethod(0);
            String[] expectedMethodParts = new String[]{"java.util.ArrayList", "add", "(Ljava/lang/Object;)V"};
            assertEquals(expectedMethodParts[0], actualMethod.getMethPackage());
            assertEquals(expectedMethodParts[1], actualMethod.getMethClass());
            assertEquals(expectedMethodParts[2], actualMethod.getMethName());
        } catch (ClassNotFoundException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetFoundMethodWithInvalidIndex() {
        new ClassInfo("java.util.ArrayList");
        new ClassInfo("java.util.ArrayList").getFoundMethod(10);
    }
}