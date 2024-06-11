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
			fail("Failed to load String class");
		}

		try {
			ClassInfo classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
			assertTrue(classInfo.isAbstract());
		} catch (ClassNotFoundException e) {
			fail("Failed to load AbstractStringBuilder class");
		}
	}

	@Test
	public void testIsInterface() {
		try {
			ClassInfo classInfo = new ClassInfo("java.util.List");
			assertFalse(classInfo.isInterface());
		} catch (ClassNotFoundException e) {
			fail("Failed to load List interface");
		}

		try {
			ClassInfo classInfo = new ClassInfo("java.util.Collections");
			assertTrue(classInfo.isInterface());
		} catch (ClassNotFoundException e) {
			fail("Failed to load Collections interface");
		}
	}

	@Test
	public void testGetClassName() {
		try {
			ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
			assertEquals("java.util.ArrayList", classInfo.getClassName());
		} catch (ClassNotFoundException e) {
			fail("Failed to load ArrayList class");
		}
	}

	@Test
	public void testGetClassPackage() {
		try {
			ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
			assertEquals("java.util", classInfo.getClassPackage());
		} catch (ClassNotFoundException e) {
			fail("Failed to load ArrayList class");
		}
	}

	@Test
	public void testGetSuperClassName() {
		try {
			ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
			assertNull(classInfo.getSuperClassName());
		} catch (ClassNotFoundException e) {
			fail("Failed to load ArrayList class");
		}

		try {
			ClassInfo classInfo = new ClassInfo("java.util.AbstractList");
			assertEquals("java.util.AbstractCollection", classInfo.getSuperClassName());
		} catch (ClassNotFoundException e) {
			fail("Failed to load AbstractList class");
		}
	}

	@Test
	public void testPrintMethods() {
		try {
			ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
			String[] methods = classInfo.printMethods();
			assertNotNull(methods);
			assertFalse(methods.length == 0);
			for (String method : methods) {
				assertTrue(method.contains("public "));
				Method methodObj = null;
				try {
					methodObj = ClassInfo.class.getDeclaredMethod(method.substring(0, method.indexOf("(")));
				} catch (SecurityException | NoSuchMethodException e) {
					fail("Method not found: " + method);
				}
				assertNotNull(methodObj);
			}
		} catch (ClassNotFoundException e) {
			fail("Failed to load ArrayList class");
		}
	}

	@Test
	public void testPrintMethodsWithParam() {
		try {
			ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
			String[] methods = classInfo.printMethods(ClassInfo.NO_OBJECT_METHODS);
			assertNotNull(methods);
			assertFalse(methods.length == 0);
			for (String method : methods) {
				assertTrue(method.contains("public "));
				Method methodObj = null;
				try {
					methodObj = ClassInfo.class.getDeclaredMethod(method.substring(0, method.indexOf("(")));
				} catch (SecurityException | NoSuchMethodException e) {
					fail("Method not found: " + method);
				}
				assertNotNull(methodObj);
				assertEquals(0, methodObj.getParameterCount());
			}
		} catch (ClassNotFoundException e) {
			fail("Failed to load ArrayList class");
		}
	}

	@Test
	public void testPrintConstructors() {
		try {
			ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
			String[] constructors = classInfo.printConstructors();
			assertNotNull(constructors);
			assertFalse(constructors.length == 0);
			for (String constructor : constructors) {
				assertTrue(constructor.contains("public "));
				Constructor constructorObj = null;
				try {
					constructorObj = ClassInfo.class.getConstructor(new Class[0]);
				} catch (NoSuchMethodException e) {
					try {
						constructorObj = ClassInfo.class.getConstructor(new Class[0], DirManager.class);
					} catch (NoSuchMethodException f) {
						fail("Constructor not found: " + constructor);
					}
				}
				assertNotNull(constructorObj);
				assertEquals(0, constructorObj.getParameterCount());
			}
		} catch (ClassNotFoundException e) {
			fail("Failed to load ArrayList class");
		}
	}

	@Test
	public void testPrintInterfaces() {
		try {
			ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
			String[] interfaces = classInfo.printInterfaces();
			assertNotNull(interfaces);
			assertFalse(interfaces.length == 0);
			assertEquals(1, interfaces.length);
			assertEquals("java.util.List", interfaces[0]);
		} catch (ClassNotFoundException e) {
			fail("Failed to load ArrayList class");
		}

		try {
			ClassInfo classInfo = new ClassInfo("java.util.Collections");
			String[] interfaces = classInfo.printInterfaces();
			assertNotNull(interfaces);
			assertFalse(interfaces.length == 0);
			assertEquals(1, interfaces.length);
			assertEquals("java.util.Collections", interfaces[0]);
		} catch (ClassNotFoundException e) {
			fail("Failed to load Collections class");
		}

		try {
			ClassInfo classInfo = new ClassInfo("java.util.AbstractCollection");
			String[] interfaces = classInfo.printInterfaces();
			assertNotNull(interfaces);
			assertTrue(interfaces.length > 0);
			assertEquals(1, interfaces.length);
			assertEquals("java.util.Collection", interfaces[0]);
		} catch (ClassNotFoundException e) {
			fail("Failed to load AbstractCollection class");
		}
	}

	@Test
	public void testSearchMethods() {
		try {
			ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
			String[] methods = classInfo.srchMethods("add");
			assertNotNull(methods);
			assertFalse(methods.length == 0);
			for (String method : methods) {
				assertTrue(method.contains("public "));
				Method methodObj = null;
				try {
					methodObj = ClassInfo.class.getDeclaredMethod(method.substring(0, method.indexOf("(")));
				} catch (NoSuchMethodException | SecurityException e) {
					fail("Method not found: " + method);
				}
				assertNotNull(methodObj);
				assertEquals("add", methodObj.getName());
			}
		} catch (ClassNotFoundException e) {
			fail("Failed to load ArrayList class");
		}

		try {
			ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
			String[] methods = classInfo.srchMethods("addAll", new String[]{"java.util.Collection"});
			assertNotNull(methods);
			assertFalse(methods.length == 0);
			for (String method : methods) {
				assertTrue(method.contains("public "));
				Method methodObj = null;
				try {
					methodObj = ClassInfo.class.getDeclaredMethod(method.substring(0, method.indexOf("(")));
				} catch (NoSuchMethodException | SecurityException e) {
					fail("Method not found: " + method);
				}
				assertNotNull(methodObj);
				assertEquals("addAll", methodObj.getName());
				assertEquals(1, methodObj.getParameterCount());
				assertTrue(methodObj.getParameterTypes()[0].getTypeName().equals("java.util.Collection"));
			}
		} catch (ClassNotFoundException e) {
			fail("Failed to load ArrayList class");
		}
	}

	@Test
	public void testGetFoundMethod() {
		try {
			ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
			MethodData methodData = classInfo.getFoundMethod(0);
			assertNotNull(methodData);
			assertEquals("java.util.ArrayList", methodData.getMethPackage());
			assertEquals("java.util.ArrayList", methodData.getMethClass());
			assertEquals("add", methodData.getMethName());
		} catch (Exception e) {
			fail("Failed to get method data");
		}
	}
}