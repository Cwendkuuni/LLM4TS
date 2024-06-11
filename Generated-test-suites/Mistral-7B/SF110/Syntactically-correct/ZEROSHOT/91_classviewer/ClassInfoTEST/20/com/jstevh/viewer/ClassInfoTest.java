package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.jstevh.viewer.ClassInfo;
import com.jstevh.viewer.ClassInfo.MethodData;
import com.jstevh.viewer.StringTools;
import com.jstevh.viewer.Tools;
import com.jstevh.viewer.DirManager;

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
		assertEquals("java.lang", classInfo.getClassPackage());
	}

	@Test
	public void testGetSuperClassName() {
		ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
		assertEquals("java.util.AbstractList", classInfo.getSuperClassName());
		classInfo = new ClassInfo("java.lang.Object");
		assertEquals(null, classInfo.getSuperClassName());
	}

	@Test(expected = ClassNotFoundException.class)
	public void testClassNotFoundException() throws ClassNotFoundException {
		new ClassInfo("com.jstevh.viewer.NonExistentClass");
	}

	@Test
	public void testPrintMethods() {
		ClassInfo classInfo = new ClassInfo("java.lang.String");
		String[] methods = classInfo.printMethods();
		assertNotNull(methods);
		assertEquals(17, methods.length);
		List<String> methodList = Arrays.asList(methods);
		assertTrue(methodList.contains("charAt(int)"));
		assertTrue(methodList.contains("equals(Object)"));
		assertTrue(methodList.contains("hashCode()"));
		// ... add more assertions for other methods
	}

	@Test
	public void testPrintMethodsWithParam() {
		ClassInfo classInfo = new ClassInfo("java.util.List");
		String[] methods = classInfo.printMethods(ClassInfo.NO_INHERITED_METHODS);
		assertNotNull(methods);
		assertEquals(8, methods.length);
		List<String> methodList = Arrays.asList(methods);
		assertTrue(methodList.contains("add(E)"));
		assertTrue(methodList.contains("clear()"));
		// ... add more assertions for other methods
	}

	@Test
	public void testPrintConstructors() {
		ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
		String[] constructors = classInfo.printConstructors();
		assertNotNull(constructors);
		assertEquals(5, constructors.length);
		List<String> constructorList = Arrays.asList(constructors);
		assertTrue(constructorList.contains("ArrayList()"));
		assertTrue(constructorList.contains("ArrayList(int)"));
		// ... add more assertions for other constructors
	}

	@Test
	public void testPrintInterfaces() {
		ClassInfo classInfo = new ClassInfo("java.util.List");
		String[] interfaces = classInfo.printInterfaces();
		assertNotNull(interfaces);
		assertEquals(1, interfaces.length);
		assertEquals("java.lang.Iterable", interfaces[0]);
	}

	@Test
	public void testSrchMethods() {
		ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
		String[] methods = classInfo.srchMethods("remove");
		assertNotNull(methods);
		assertEquals(1, methods.length);
		assertEquals("remove(int)", methods[0]);
		methods = classInfo.srchMethods("remove", new String[]{"int", "Object"});
		assertNotNull(methods);
		assertEquals(2, methods.length);
		assertTrue(Arrays.asList(methods).containsAll(Arrays.asList(new String[]{"remove(int)", "remove(Object)"})));
	}

	@Test
	public void testGetFoundMethod() {
		ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
		MethodData methodData = classInfo.getFoundMethod(0);
		assertNotNull(methodData);
		assertEquals("ArrayList", methodData.getMethPackage());
		assertEquals("ArrayList", methodData.getMethClass());
		assertEquals("add", methodData.getMethName());
	}

	@Test
	public void testPrintArray() {
		Object[] array = new Object[5];
		ClassInfo classInfo = new ClassInfo();
		classInfo.printArray(array);
		// Uncomment the following lines to test the printArray method with debug mode
		// File file = new File("test.txt");
		// PrintWriter writer = new PrintWriter(file);
		// classInfo.printArray(new String[]{"one", "two", "three", "four", "five"});
		// writer.close();
	}

	@Test
	public void testGetClass() {
		ClassInfo classInfo = new ClassInfo();
		Class clazz = classInfo.getClass("java.util.ArrayList");
		assertNotNull(clazz);
		assertEquals("java.util.ArrayList", clazz.getName());
		assertFalse(clazz.isInterface());
		clazz = classInfo.getClass("java.util.List");
		assertNotNull(clazz);
		assertEquals("java.util.List", clazz.getName());
		assertTrue(clazz.isInterface());
		try {
			clazz = classInfo.getClass("NonExistentClass");
			fail();
		} catch (ClassNotFoundException e) {
			// Expected
		}
	}

	@Test
	public void testTryClass() {
		ClassInfo classInfo = new ClassInfo();
		Class clazz = classInfo.tryClass("java.util.ArrayList");
		assertNotNull(clazz);
		assertEquals("java.util.ArrayList", clazz.getName());
		assertFalse(clazz.isInterface());
		clazz = classInfo.tryClass("java.util.List");
		assertNotNull(clazz);
		assertEquals("java.util.List", clazz.getName());
		assertTrue(clazz.isInterface());
		clazz = classInfo.tryClass("NonExistentClass");
		assertEquals(null, clazz);
	}

	@Test
	public void testMain() throws Exception {
		String[] args = {"new", "java.util.ArrayList"};
		ClassInfo.main(args);
	}
}