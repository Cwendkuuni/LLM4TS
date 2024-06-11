package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.junit.Test;

public class ClassInfoTest {

 private ClassInfo classInfo;

 @Test
 public void testIsAbstract() {
 ClassInfo ci = new ClassInfo(String.class.getName());
 assertFalse(ci.isAbstract());

 ci = new ClassInfo("java.lang.AbstractStringBuilder", new DirManager());
 assertTrue(ci.isAbstract());
 }

 @Test
 public void testIsInterface() {
 ClassInfo ci = new ClassInfo(String.class.getName());
 assertFalse(ci.isInterface());

 ci = new ClassInfo("java.util.List", new DirManager());
 assertTrue(ci.isInterface());
 }

 @Test
 public void testGetClassName() {
 ClassInfo ci = new ClassInfo(String.class.getName());
 assertEquals(String.class.getName(), ci.getClassName());
 }

 @Test
 public void testGetClassPackage() {
 ClassInfo ci = new ClassInfo(String.class.getName());
 assertNull(ci.getClassPackage());

 ci = new ClassInfo("java.util.ArrayList", new DirManager());
 assertEquals("java.util", ci.getClassPackage());
 }

 @Test
 public void testGetSuperClassName() {
 ClassInfo ci = new ClassInfo(String.class.getName());
 assertNull(ci.getSuperClassName());

 ci = new ClassInfo("java.util.ArrayList", new DirManager());
 assertEquals("java.util.AbstractList", ci.getSuperClassName());
 }

 @Test
 public void testPrintFields() {
 ClassInfo ci = new ClassInfo("java.util.ArrayList", new DirManager());
 String[] fields = ci.printFields();
 assertArrayEquals(new String[]{"size=17", "elementData=org.objectweb.asm.Type[]@3b2479dd", "toArray=(Ljava/lang/Object;)Ljava/lang/Object;", "trimToSize=()V", "ensureCapacity=(I)V", "modCount=0"}, fields);
 }

 @Test
 public void testPrintMethods() {
 ClassInfo ci = new ClassInfo("java.util.ArrayList", new DirManager());
 String[] methods = ci.printMethods();
 assertArrayEquals(new String[]{"add=(Ljava/lang/Object;)Z", "clone=()Ljava/lang/Object;", "clear=()V", "contains=(Ljava/lang/Object;)Z", "ensureCapacity=(I)V", "ensureCapacityInternal=(I)V", "get=([I)Ljava/lang/Object;", "indexOf=(Ljava/lang/Object;)I", "isEmpty=()Z", "iterator=()Ljava/util/Iterator;", "listIterator=()Ljava/util/ListIterator;", "remove=(Ljava/lang/Object;)Z", "removeRange=(II)V", "replaceAll=(Ljava/util/function/BiConsumer;)V", "retainAll=(Ljava/util/Collection;)Z", "set=([IILjava/lang/Object;)V", "size=()I", "toArray=()[Ljava/lang/Object;", "trimToSize=()V"}, methods);
 }

 @Test
 public void testPrintMethodsInt() {
 ClassInfo ci = new ClassInfo("java.util.ArrayList", new DirManager());
 String[] methods = ci.printMethods(1);
 assertArrayEquals(new String[]{"add=([Ljava/lang/Object;)Z"}, methods);
 }

 @Test
 public void testPrintConstructors() {
 ClassInfo ci = new ClassInfo("java.util.ArrayList", new DirManager());
 String[] constructors = ci.printConstructors();
 assertArrayEquals(new String[]{"public <E> java.util.ArrayList<E> (int)", "public java.util.ArrayList()"}, constructors);
 }

 @Test
 public void testPrintInterfaces() {
 ClassInfo ci = new ClassInfo("java.util.ArrayList", new DirManager());
 String[] interfaces = ci.printInterfaces();
 assertArrayEquals(new String[]{"java.io.Serializable", "java.util.RandomAccess", "java.util.Collection", "java.lang.Iterable"}, interfaces);
 }

 @Test
 public void testSrchMethods() {
 ClassInfo ci = new ClassInfo("java.util.ArrayList", new DirManager());
 String[] methods = ci.srchMethods("add");
 assertArrayEquals(new String[]{"public synchronized void add(E)", "public synchronized boolean addAll(Collection<? extends E>)", "public synchronized boolean addAll(int, Collection<? extends E>)", "public synchronized void add(int, E)"}, methods);
 }

 @Test
 public void testSrchMethodsStringArray() {
 ClassInfo ci = new ClassInfo("java.util.ArrayList", new DirManager());
 String[] methods = ci.srchMethods("iterator", new String[]{"java.util."});
 assertArrayEquals(new String[]{"public java.util.ListIterator<E> listIterator()", "public java.util.Iterator<E> iterator()"}, methods);
 }

 @Test
 public void testGetFoundMethod() {
 ClassInfo ci = new ClassInfo("java.util.ArrayList", new DirManager());
 MethodData method = ci.getFoundMethod(0);
 assertEquals("public java.util.ArrayList<E> <init>(int)", method.getMethPackage());
 assertEquals("java.util.ArrayList", method.getMethClass());
 assertEquals("(I)V", method.getMethName());
 }

 @Test
 public void testPrintArray() {
 ClassInfo ci = new ClassInfo("java.util.ArrayList", new DirManager());
 String[] array = {"public java.util.ArrayList<E> <init>(int)", "public java.util.ArrayList<E> <init>()", "public void add(E)", "public synchronized void add(E)", "public boolean addAll(Collection<? extends E>)", "public synchronized boolean addAll(int, Collection<? extends E>)", "public void add(int, E)", "public E get(int)", "public int size()", "public Object[] toArray()", "public void trimToSize()"};
 ClassInfo.debug = true;
 ci.printArray(array);
 }
}