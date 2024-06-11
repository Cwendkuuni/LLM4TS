package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Method;

import org.junit.Test;

import com.jstevh.tools.StringTools;
import com.jstevh.viewer.ClassInfo;
import com.jstevh.viewer.DirManager;

public class ClassInfoTest {

	@Test
	public void testIsAbstract() {
		try {
			ClassInfo classInfo = new ClassInfo("java.lang.String");
			assertFalse(classInfo.isAbstract());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}

		try {
			ClassInfo classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
			assertTrue(classInfo.isAbstract());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}
	}

	@Test
	public void testIsInterface() {
		try {
			ClassInfo classInfo = new ClassInfo("java.lang.String");
			assertFalse(classInfo.isInterface());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}

		try {
			ClassInfo classInfo = new ClassInfo("java.util.List");
			assertTrue(classInfo.isInterface());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}
	}

	@Test
	public void testGetClassName() {
		try {
			ClassInfo classInfo = new ClassInfo("java.lang.String");
			assertEquals("java.lang.String", classInfo.getClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}
	}

	@Test
	public void testGetClassPackage() {
		try {
			ClassInfo classInfo = new ClassInfo("java.lang.String");
			assertEquals("java.lang", classInfo.getClassPackage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}

		try {
			ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
			assertEquals("java.util", classInfo.getClassPackage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}
	}

	@Test
	public void testGetSuperClassName() {
		try {
			ClassInfo classInfo = new ClassInfo("java.lang.String");
			assertEquals(null, classInfo.getSuperClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}

		try {
			ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
			assertEquals("java.util.AbstractCollection", classInfo.getSuperClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}
	}

	@Test
	public void testConstructorWithoutException() {
		try {
			ClassInfo classInfo = new ClassInfo("java.lang.String");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}
	}

	@Test(expected = ClassNotFoundException.class)
	public void testConstructorWithException() {
		try {
			ClassInfo classInfo = new ClassInfo("java.nonExistentClass");
		} catch (IllegalArgumentException e) {
			fail("IllegalArgumentException was expected instead of ClassNotFoundException");
		}
	}

	@Test
	public void testPrintFields() {
		try {
			ClassInfo classInfo = new ClassInfo("java.lang.String");
			String[] fields = classInfo.printFields();
			assertEquals(0, fields.length);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}

		try {
			ClassInfo classInfo = new ClassInfo("java.lang.Object");
			String[] fields = classInfo.printFields();
			assertEquals(0, fields.length);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}

		try {
			ClassInfo classInfo = new ClassInfo("java.util.Date");
			String[] fields = classInfo.printFields();
			assertEquals(11, fields.length);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}
	}

	@Test
	public void testPrintMethods() {
		try {
			ClassInfo classInfo = new ClassInfo("java.lang.String");
			String[] methods = classInfo.printMethods();
			assertEquals(0, methods.length);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}

		try {
			ClassInfo classInfo = new ClassInfo("java.lang.Object");
			String[] methods = classInfo.printMethods();
			assertEquals(0, methods.length);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}

		try {
			ClassInfo classInfo = new ClassInfo("java.util.Date");
			String[] methods = classInfo.printMethods();
			assertEquals(28, methods.length);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}
	}

	@Test
	public void testPrintMethodsWithParam() {
		try {
			ClassInfo classInfo = new ClassInfo("java.util.Date");
			String[] methods = classInfo.printMethods(ClassInfo.NO_INHERITED_METHODS);
			assertEquals(15, methods.length);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}
	}

	@Test
	public void testPrintConstructors() {
		try {
			ClassInfo classInfo = new ClassInfo("java.lang.String");
			String[] constructors = classInfo.printConstructors();
			assertEquals(0, constructors.length);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}

		try {
			ClassInfo classInfo = new ClassInfo("java.lang.Object");
			String[] constructors = classInfo.printConstructors();
			assertEquals(0, constructors.length);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}

		try {
			ClassInfo classInfo = new ClassInfo("java.util.Date");
			String[] constructors = classInfo.printConstructors();
			assertEquals(11, constructors.length);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}
	}

	@Test
	public void testPrintInterfaces() {
		try {
			ClassInfo classInfo = new ClassInfo("java.lang.String");
			String[] interfaces = classInfo.printInterfaces();
			assertEquals(0, interfaces.length);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}

		try {
			ClassInfo classInfo = new ClassInfo("java.lang.Object");
			String[] interfaces = classInfo.printInterfaces();
			assertEquals(0, interfaces.length);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}

		try {
			ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
			String[] interfaces = classInfo.printInterfaces();
			assertEquals(1, interfaces.length);
			assertEquals("java.util.Collection", interfaces[0]);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}
	}

	@Test
	public void testSrchMethods() {
		try {
			ClassInfo classInfo = new ClassInfo("java.util.Date");
			String[] methods = classInfo.srchMethods("toString");
			assertEquals(1, methods.length);
			assertEquals("public java.lang.String toString()", methods[0]);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}

		try {
			ClassInfo classInfo = new ClassInfo("java.util.Date");
			String[] methods = classInfo.srchMethods("setYear");
			assertEquals(1, methods.length);
			assertEquals("public void setYear(int year)", methods[0]);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}

		try {
			ClassInfo classInfo = new ClassInfo("java.util.Date");
			String[] methods = classInfo.srchMethods("setMonth");
			assertEquals(0, methods.length);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		}
	}

	@Test
	public void testGetFoundMethod() {
		try {
			ClassInfo classInfo = new ClassInfo("java.util.Date");
			MethodData methodData = classInfo.getFoundMethod(0);
			assertEquals("public java.lang.String toString()", methodData.getMethString());
			assertEquals("java.lang.String", methodData.getMethReturnType());
			assertEquals("java.util.Date", methodData.getMethClass());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("ClassNotFoundException was thrown");
		} catch (IndexOutOfBoundsException e) {
			fail("IndexOutOfBoundsException was expected instead of NullPointerException");
		}
	}

	@Test
	public void testMain() {
		File classFile = new File("target/test-classes/ClassInfoTest.class");
		Runtime rt = Runtime.getRuntime();
		String[] args = new String[] {"new", "ClassInfoTest"};
		Process process = rt.exec("java", "-cp", ".", "ClassInfo", "-Xmx128m", "-Xms128m", "-verbose:class", "-Djava.class.path=" + classFile.getAbsolutePath(), "ClassInfoTest");
		try {
			process.waitFor();
			int exitCode = process.exitValue();
			assertEquals(0, exitCode);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("InterruptedException was thrown");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException was thrown");
		}
	}
}