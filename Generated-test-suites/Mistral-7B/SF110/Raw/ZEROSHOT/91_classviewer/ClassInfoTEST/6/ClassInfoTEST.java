package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

	@Test
	public void testIsAbstract() throws Exception {
		ClassInfo classInfo = new ClassInfo("java.lang.String");
		assertFalse(classInfo.isAbstract());

		classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
		assertTrue(classInfo.isAbstract());
	}

	@Test
	public void testIsInterface() throws Exception {
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

	@Test
	public void testGetClassPackage() throws Exception {
		ClassInfo classInfo = new ClassInfo("java.lang.String");
		assertEquals("java.lang", classInfo.getClassPackage());
	}

	@Test
	public void testGetSuperClassName() throws Exception {
		ClassInfo classInfo = new ClassInfo("java.lang.Object");
		assertNull(classInfo.getSuperClassName());

		classInfo = new ClassInfo("java.lang.String");
		assertEquals("java.lang.Object", classInfo.getSuperClassName());
	}

	@Test
	public void testPrintMethods() throws Exception {
		ClassInfo classInfo = new ClassInfo("java.lang.String");
		String[] methods = classInfo.printMethods();
		assertNotNull(methods);
		assertEquals(34, methods.length);
	}

	@Test
	public void testPrintMethodsWithParam() throws Exception {
		ClassInfo classInfo = new ClassInfo("java.lang.String");
		String[] methods = classInfo.printMethods(ClassInfo.NO_INHERITED_METHODS);
		assertNotNull(methods);
		assertEquals(23, methods.length);
	}

	@Test
	public void testPrintConstructors() throws Exception {
		ClassInfo classInfo = new ClassInfo("java.lang.String");
		String[] constructors = classInfo.printConstructors();
		assertNotNull(constructors);
		assertEquals(0, constructors.length);
	}

	@Test
	public void testPrintFields() throws Exception {
		ClassInfo classInfo = new ClassInfo("java.lang.String");
		String[] fields = classInfo.printFields();
		assertNotNull(fields);
		assertEquals(0, fields.length);
	}

	@Test
	public void testPrintInterfaces() throws Exception {
		ClassInfo classInfo = new ClassInfo("java.util.List");
		String[] interfaces = classInfo.printInterfaces();
		assertNotNull(interfaces);
		assertEquals(1, interfaces.length);
		assertEquals("java.io.Serializable", interfaces[0]);
	}

	@Test
	public void testSrchMethods() throws Exception {
		ClassInfo classInfo = new ClassInfo("java.lang.String");
		String[] methods = classInfo.srchMethods("equals");
		assertNotNull(methods);
		assertEquals(1, methods.length);
		assertEquals("public boolean java.lang.Object.equals(java.lang.Object)", methods[0]);

		methods = classInfo.srchMethods("toUpperCase", new String[]{"java.lang.String"});
		assertNotNull(methods);
		assertEquals(1, methods.length);
		assertEquals("public java.lang.String java.lang.String.toUpperCase()", methods[0]);
	}

	@Test
	public void testGetFoundMethod() throws Exception {
		ClassInfo classInfo = new ClassInfo("java.lang.String");
		MethodData methodData = classInfo.getFoundMethod(0);
		assertNotNull(methodData);
		assertEquals("java.lang.Object.equals", methodData.getMethName());
		assertEquals("java.lang.Object", methodData.getMethClass());
		assertEquals(null, methodData.getMethPackage());
	}
}