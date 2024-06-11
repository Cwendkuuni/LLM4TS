package com.jstevh.viewer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.StringList;

import org.junit.Test;

import com.jstevh.viewer.ClassInfo;
import com.jstevh.viewer.ClassInfo.MethodData;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isAbstract());

        classInfo = new ClassInfo("java.lang.AbstractString");
        assertTrue(classInfo.isAbstract());
    }

    @Test
    public void testIsInterface() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.List");
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertFalse(classInfo.isInterface());

        classInfo = new ClassInfo("java.util.Iterator");
        assertTrue(classInfo.isInterface());
    }

    @Test
    public void testGetClassName() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertEquals("java.lang.String", classInfo.getClassName());
    }

    @Test
    public void testGetClassPackage() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertNull(classInfo.getClassPackage());

        classInfo = new ClassInfo("com.jstevh.viewer.ClassInfo");
        assertEquals("com.jstevh.viewer", classInfo.getClassPackage());
    }

    @Test
    public void testGetSuperClassName() throws Exception {
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.lang.String");
        assertNull(classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.lang.Number");
        assertEquals("java.lang.Object", classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.AbstractList", classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.util.AbstractList");
        assertEquals("java.util.Collection", classInfo.getSuperClassName());

        classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals("java.util.AbstractList", classInfo.getSuperClassName());
        assertEquals("java.util.Collection", classInfo.getSuperClassName(1));
    }

    @Test
    public void testPrintFields() throws Exception {
        String[] expected = { "private static final int serialVersionUID = 6849628353562662662L;",
                "private int size;", "private transient Object elementData;",
                "private static final Object[] EMPTY_ELEMENTDATA = new Object[0];" };
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals(expected.length, classInfo.printFields().length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], classInfo.printFields()[i]);
        }
    }

    @Test
    public void testPrintMethods() throws Exception {
        String[] expected = { "public int size()", "public boolean isEmpty()",
                "public boolean contains(Object o)", "public Object get(int index)",
                "public Object set(int index, Object element)", "public void add(int index, Object element)",
                "public Object remove(int index)", "public boolean remove(Object o)",
                "public boolean add(Object element)", "public boolean removeAll(Collection<?> c)",
                "public boolean retainAll(Collection<?> c)", "public void clear()",
                "public Object[] toArray()", "public Object[] toArray(Object[] a)",
                "public String toString()", "public boolean equals(Object o)",
                "public int hashCode()", "public Class<?> getClass()",
                "public boolean addAll(Collection<? extends E> c)", "public boolean addAll(int index, Collection<? extends E> c)",
                "public int indexOf(Object o)", "public int lastIndexOf(Object o)",
                "public ListIterator<E> listIterator()", "public ListIterator<E> listIterator(int index)",
                "public List<E> subList(int fromIndex, int toIndex)", "public Spliterator<E> spliterator()" };
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals(expected.length, classInfo.printMethods().length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], classInfo.printMethods()[i]);
        }
    }

    @Test
    public void testPrintMethodsWithParameter() throws Exception {
        String[] expected = { "public void add(int index, E element)" };
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals(1, classInfo.printMethods(1).length);
        assertEquals(expected[0], classInfo.printMethods(1)[0]);
    }

    @Test
    public void testPrintConstructors() throws Exception {
        String[] expected = { "public ArrayList()", "public ArrayList(int initialCapacity)",
                "public ArrayList(Collection<? extends E> c)",
                "public ArrayList(int initialCapacity, Collection<? extends E> c)" };
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals(expected.length, classInfo.printConstructors().length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], classInfo.printConstructors()[i]);
        }
    }

    @Test
    public void testPrintInterfaces() throws Exception {
        String[] expected = { "java.util.Collection", "java.util.List",
                "java.io.Serializable" };
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        assertEquals(expected.length, classInfo.printInterfaces().length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], classInfo.printInterfaces()[i]);
        }
    }

    @Test
    public void testSrchMethods() throws Exception {
        String text = "public java.util.List getList()";
        String[] expected = { "public java.util.List getList()" };
        ClassInfo classInfo = new ClassInfo("com.jstevh.viewer.ClassInfo");
        Method[] methods = classInfo.getClass().getMethods();
        String[] methodsNames = getData(methods);
        MethodData methodData = classInfo.getFoundMethod(StringTools.searchStrings(methodsNames, text)[0]);
        assertEquals(expected[0], methodData.getMethName());
        assertEquals("com.jstevh.viewer.ClassInfo", methodData.getMethClass().getName());
    }

    private static String[] getData(Method[] methods) {
        return ClassInfo.getData(methods);
    }

    @Test
    public void testGetFoundMethod() throws Exception {
        String name = "getList";
        int position = 0;
        String className = "com.jstevh.viewer.ClassInfo";
        String methodName = "getList";
        String methodPackage = "com.jstevh.viewer";
        MethodData methodData = new MethodData() {
            @Override
            public String getMethPackage() {
                return methodPackage;
            }

            @Override
            public String getMethClass() {
                return className;
            }

            @Override
            public String getMethName() {
                return methodName;
            }
        };
        ClassInfo classInfo = new ClassInfo();
        MethodData actual = classInfo.getFoundMethod(position);
        assertEquals(methodData, actual);
    }
}