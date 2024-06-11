package com.jstevh.viewer;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClassInfoTest {

 @Test
 public void testIsAbstractMethod() {
 ClassInfo classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
 assertTrue(classInfo.isAbstract());
 }

 @Test(expected = ClassNotFoundException.class)
 public void testGetClassThrowsException() {
 ClassInfo classInfo = new ClassInfo("com.jstevh.nonExistentClass");
 fail("ClassNotFoundException not thrown");
 }

 @Test
 public void testGetClassExistingClass() {
 ClassInfo classInfo = new ClassInfo("java.lang.String");
 assertNotNull(classInfo.getC());
 }

 @Test
 public void testPrintMethodsWithParam() {
 String className = "java.util.ArrayList";
 ClassInfo classInfo = new ClassInfo(className);
 String[] expectedMethods = new String[]{"public int size()", "public boolean isEmpty()", "public boolean contains(Object o)", "public String toString()", "public void clear()", "public boolean add(E e)", "public boolean remove(Object o)", "public boolean containsAll(Collection<?> c)", "public boolean addAll(Collection<? extends E> c)", "public boolean removeAll(Collection<?> c)", "public boolean retainAll(Collection<?> c)", "public boolean addAll(int index, Collection<? extends E> c)", "public E get(int index)", "public E set(int index, E element)", "public boolean add(E e)", "public boolean remove(int index)", "public void add(int index, E element)", "public E remove(int index)", "public int indexOf(Object o)", "public int lastIndexOf(Object o)", "public ListIterator<E> listIterator()", "public ListIterator<E> listIterator(int index)", "public List subList(int fromIndex, int toIndex)", "public boolean equals(Object o)", "public int hashCode()", "public boolean equals(Object obj)", "public String toString()", "public Class<?> getClass()", "public int size()", "public boolean isEmpty()", "public boolean add(E e)", "public boolean remove(Object o)", "public boolean contains(Object o)", "public boolean addAll(Collection<? extends E> c)", "public boolean removeAll(Collection<?> c)", "public boolean retainAll(Collection<?> c)", "public int size()", "public boolean isEmpty()", "public boolean add(E e)", "public boolean remove(int index)", "public E get(int index)", "public E set(int index, E element)", "public void add(int index, E element)", "public E remove(int index)", "public ListIterator<E> listIterator()", "public ListIterator<E> listIterator(int index)", "public List subList(int fromIndex, int toIndex)"};
 String[] actualMethods = classInfo.printMethods(NO_INHERITED_METHODS);
 assertArrayEquals(expectedMethods, actualMethods);
 }

}