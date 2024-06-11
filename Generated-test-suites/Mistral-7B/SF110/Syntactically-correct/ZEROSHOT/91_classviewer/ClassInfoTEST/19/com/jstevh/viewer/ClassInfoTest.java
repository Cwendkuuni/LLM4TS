package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

	@Test
	public void testIsAbstract() {
		try {
			ClassInfo classInfo = new ClassInfo("java.lang.String");
			assertFalse(classInfo.isAbstract());
		} catch (ClassNotFoundException e) {
			fail("Failed to load class: java.lang.String");
		}

		try {
			ClassInfo classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
			assertTrue(classInfo.isAbstract());
		} catch (ClassNotFoundException e) {
			fail("Failed to load class: java.lang.AbstractStringBuilder");
		}
	}

	@Test
	public void testIsInterface() {
		try {
			ClassInfo classInfo = new ClassInfo("java.lang.String");
			assertFalse(classInfo.isInterface());
		} catch (ClassNotFoundException e) {
			fail("Failed to load class: java.lang.String");
		}

		try {
			ClassInfo classInfo = new ClassInfo("java.util.List");
			assertTrue(classInfo.isInterface());
		} catch (ClassNotFoundException e) {
			fail("Failed to load class: java.util.List");
		}
	}

	@Test
	public void testGetClassName() {
		try {
			ClassInfo classInfo = new ClassInfo("java.lang.String");
			assertEquals("java.lang.String", classInfo.getClassName());
		} catch (ClassNotFoundException e) {
			fail("Failed to load class: java.lang.String");
		}
	}

	@Test
	public void testGetClassPackage() {
		try {
			ClassInfo classInfo = new ClassInfo("java.lang.String");
			assertEquals("java.lang", classInfo.getClassPackage());
		} catch (ClassNotFoundException e) {
			fail("Failed to load class: java.lang.String");
		}
	}

	@Test
	public void testGetSuperClassName() {
		try {
			ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
			assertEquals("java.util.AbstractList", classInfo.getSuperClassName());
		} catch (ClassNotFoundException e) {
			fail("Failed to load class: java.util.ArrayList");
		}

		try {
			ClassInfo classInfo = new ClassInfo("java.lang.Object");
			assertNull(classInfo.getSuperClassName());
		} catch (ClassNotFoundException e) {
			fail("Failed to load class: java.lang.Object");
		}
	}

	@Test
	public void testPrintMethods() throws Exception {
		try {
			ClassInfo classInfo = new ClassInfo("java.lang.String");
			String[] methods = classInfo.printMethods();
			assertNotNull(methods);
			assertEquals(138, methods.length);
			for (String method : methods) {
				assertTrue(method.contains("public "));
			}
		} catch (ClassNotFoundException e) {
			fail("Failed to load class: java.lang.String");
		}
	}

	@Test
	public void testPrintMethodsWithParam() throws Exception {
		try {
			ClassInfo classInfo = new ClassInfo("java.lang.StringBuilder");
			String[] methods = classInfo.printMethods(ClassInfo.NO_OBJECT_METHODS);
			assertNotNull(methods);
			assertEquals(17, methods.length);
			for (String method : methods) {
				assertTrue(method.contains("public "));
				assertFalse(method.contains("Object."));
			}
		} catch (ClassNotFoundException e) {
			fail("Failed to load class: java.lang.StringBuilder");
		}
	}

	@Test
	public void testPrintConstructors() throws Exception {
		try {
			ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
			String[] constructors = classInfo.printConstructors();
			assertNotNull(constructors);
			assertEquals(5, constructors.length);
			for (String constructor : constructors) {
				assertTrue(constructor.contains("public "));
			}
		} catch (ClassNotFoundException e) {
			fail("Failed to load class: java.util.ArrayList");
		}
	}

	@Test
	public void testPrintInterfaces() throws Exception {
		try {
			ClassInfo classInfo = new ClassInfo("java.util.List");
			String[] interfaces = classInfo.printInterfaces();
			assertNotNull(interfaces);
			assertEquals(1, interfaces.length);
			assertEquals("java.io.Serializable", interfaces[0]);
		} catch (ClassNotFoundException e) {
			fail("Failed to load class: java.util.List");
		}
	}

	@Test
	public void testSrchMethods() throws Exception {
		try {
			ClassInfo classInfo = new ClassInfo("java.lang.String");
			StringSearch search = new StringSearch("length");
			String[] methods = classInfo.srchMethods(search.getSearchString());
			assertNotNull(methods);
			assertEquals(1, methods.length);
			assertEquals("public int length()", methods[0]);
		} catch (ClassNotFoundException e) {
			fail("Failed to load class: java.lang.String");
		}
	}

	@Test
	public void testSrchMethodsWithIndex() throws Exception {
		try {
			ClassInfo classInfo = new ClassInfo("java.lang.String");
			StringSearch search = new StringSearch("length");
			int index = 0;
			String[] methods = classInfo.srchMethods(search.getSearchString(), new String[] { "int" });
			assertNotNull(methods);
			assertEquals(1, methods.length);
			assertEquals("public int length()", methods[0]);
		} catch (ClassNotFoundException e) {
			fail("Failed to load class: java.lang.String");
		}
	}

	@Test
	public void testGetFoundMethod() throws Exception {
		try {
			ClassInfo classInfo = new ClassInfo("java.lang.String");
			StringSearch search = new StringSearch("value");
			Method method = classInfo.getFoundMethod(classInfo.srchMethods(search.getSearchString())[0].indexOf("(") + 1);
			assertNotNull(method);
			assertEquals("public char charAt(int)", method.toString());
		} catch (ClassNotFoundException e) {
			fail("Failed to load class: java.lang.String");
		}
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testGetFoundMethodInvalidIndex() throws Exception {
		try {
			ClassInfo classInfo = new ClassInfo("java.lang.String");
			StringSearch search = new StringSearch("value");
			Method method = classInfo.getFoundMethod(classInfo.srchMethods(search.getSearchString()).length);
		} catch (ClassNotFoundException e) {
			fail("Failed to load class: java.lang.String");
		}
	}

	private static class StringSearch {
		private String searchString;
		private String[] paramTypes;

		public StringSearch(String searchString) {
			this.searchString = searchString;
			this.paramTypes = new String[0];
		}

		public StringSearch(String searchString, String[] paramTypes) {
			this.searchString = searchString;
			this.paramTypes = paramTypes;
		}

		public String getSearchString() {
			return searchString;
		}

		public String[] getParamTypes() {
			return paramTypes;
		}
	}
}