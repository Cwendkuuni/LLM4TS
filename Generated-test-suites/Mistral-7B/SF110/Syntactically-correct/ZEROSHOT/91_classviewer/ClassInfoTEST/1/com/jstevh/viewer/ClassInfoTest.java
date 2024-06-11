package com.jstevh.viewer.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
	public void testGetClassName() throws Exception {
		ClassInfo classInfo = new ClassInfo("java.lang.String");
		assertEquals("java.lang.String", classInfo.getClassName());
	}

	@Test(expected = ClassNotFoundException.class)
	public void testGetClassNameWithInvalidClass() throws Exception {
		new ClassInfo("java.nonexistent.Class");
	}

	@Test
	public void testGetClassPackage() throws Exception {
		ClassInfo classInfo = new ClassInfo("java.lang.String");
		assertNull(classInfo.getClassPackage());

		classInfo = new ClassInfo("java.util.List");
		assertEquals("java.util", classInfo.getClassPackage());
	}

	@Test
	public void testGetSuperClassName() throws Exception {
		ClassInfo classInfo = new ClassInfo("java.lang.Object");
		assertNull(classInfo.getSuperClassName());

		classInfo = new ClassInfo("java.lang.String");
		assertNull(classInfo.getSuperClassName());

		classInfo = new ClassInfo("java.lang.Number");
		assertEquals("java.lang.Object", classInfo.getSuperClassName());
	}

	@Test
	public void testGetMethods() throws Exception {
		ClassInfo classInfo = new ClassInfo("java.lang.String");
		String[] methods = classInfo.printMethods();
		assertArrayEquals(new String[] { "toString", "equals", "hashCode", "charAt", "length", "substring", "regionMatches", "regionMatches", "startsWith", "endsWith", "trim", "indexOf", "lastIndexOf", "valueOf", "valueOf", "valueOf" }, methods);
	}

	@Test
	public void testPrintFields() throws Exception {
		ClassInfo classInfo = new ClassInfo("java.lang.String");
		String[] fields = classInfo.printFields();
		assertArrayEquals(new String[] { "value=value", "offset=offset" }, fields);
	}

	@Test
	public void testPrintMethods() throws Exception {
		ClassInfo classInfo = new ClassInfo("java.lang.String");
		String[] methods = classInfo.printMethods();
		assertArrayEquals(new String[] { "toString", "equals", "hashCode", "charAt", "length", "substring", "regionMatches", "regionMatches", "startsWith", "endsWith", "trim", "indexOf", "lastIndexOf", "valueOf", "valueOf", "valueOf" }, methods);
	}

	@Test
	public void testPrintMethodsParam() throws Exception {
		ClassInfo classInfo = new ClassInfo("java.lang.String");
		String[] methods = classInfo.printMethods(ClassInfo.NO_OBJECT_METHODS);
		assertArrayEquals(new String[] { "substring", "regionMatches", "regionMatches", "startsWith", "endsWith", "trim", "indexOf", "lastIndexOf", "valueOf" }, methods);
	}

	@Test
	public void testPrintConstructors() throws Exception {
		ClassInfo classInfo = new ClassInfo("java.lang.String");
		String[] constructors = classInfo.printConstructors();
		assertArrayEquals(new String[] { "String()", "String(byte[])", "String(byte[], int, int)", "String(char[], int, int)", "String(char[], int)", "String(String)", "String(String, int)", "String(String, int, int)", "String(String, int, int, int)", "String(StringBuilder)" }, constructors);
	}

	@Test
	public void testPrintInterfaces() throws Exception {
		ClassInfo classInfo = new ClassInfo("java.util.List");
		String[] interfaces = classInfo.printInterfaces();
		assertArrayEquals(new String[] { "java.io.Serializable", "java.util.Collection", "java.util.RandomAccess" }, interfaces);
	}

	@Test
	public void testSrchMethods() throws Exception {
		ClassInfo classInfo = new ClassInfo("java.lang.String");
		String[] methods = classInfo.srchMethods("substring");
		assertArrayEquals(new String[] { "substring(int, int)", "substring(int)", "substring(int, int)", "substring(int, int)", "substring(int, int)", "substring(int, int)", "substring(int, int)", "substring(int, int)", "substring(int, int)", "substring(int, int)", "substring(int, int)", "substring(int, int)", "substring(int, int)", "substring(int, int)", "substring(int, int)", "substring(int, int)", "substring(int, int)", "substring(int, int)", "substring(int, int)", "substring(int, int)", "substring(int, int)", "substring(int, int)", "substring(int, int)", "substring(int, int)" }, methods);
	}

	@Test
	public void testGetFoundMethod() throws Exception {
		ClassInfo classInfo = new ClassInfo("java.lang.String");
		MethodData methodData = classInfo.getFoundMethod(13);
		assertEquals("substring", methodData.getMethName());
		assertEquals("java.lang.String", methodData.getMethClass());
		assertEquals("java.lang.String", methodData.getMethPackage());
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testGetFoundMethodInvalidIndex() throws Exception {
		ClassInfo classInfo = new ClassInfo("java.lang.String");
		methodData = classInfo.getFoundMethod(100);
	}

	@Test
	public void testTryClass() throws Exception {
		Class tempClass = ClassInfo.tryClass("java.lang.String");
		assertSame(String.class, tempClass);
	}

	@Test(expected = ClassNotFoundException.class)
	public void testTryClassInvalidClass() throws Exception {
		ClassInfo.tryClass("java.nonexistent.Class");
	}

	@Test
	public void testDataGetDataMembers() {
		Member[] members = Class.forName("java.lang.String").getMembers();
		String[] data = ClassInfo.getData(members);
		assertArrayEquals(new String[] { "private static final native long serialVersionUID=0L", "private transient native Object[] value", "private final native int hash", "private transient native int offset", "private int length", "private transient native char[] value", "private native long serializedLength", "private transient native int[] serializedHash", "private native int[] serializedOffsets", "private native long[] serializedValues" }, data);
	}

	@Test
	public void testDataGetDataConstructors() {
		Constructor<?>[] constructors = Class.forName("java.lang.String").getConstructors();
		String[] data = ClassInfo.getData(constructors);
		assertArrayEquals(new String[] { "public java.lang.String()", "public java.lang.String(byte[]) throws java.io.UnsupportedEncodingException", "public java.lang.String(byte[],int,int)", "public java.lang.String(char[],int,int)", "public java.lang.String(char[],int)", "public java.lang.String(java.lang.String)", "public java.lang.String(java.lang.String,int)", "public java.lang.String(java.lang.String,int,int)", "public java.lang.String(java.lang.String,int,int,int)", "public java.lang.String(java.lang.StringBuilder)" }, data);
	}

}