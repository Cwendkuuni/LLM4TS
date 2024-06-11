package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ReflectionSearchTest {

	private ReflectionSearch reflectionSearch;
	private Class testClass;

	@Before
	public void setUp() throws Exception {
		testClass = Class.forName("net.sourceforge.beanbin.test.TestClass");
		reflectionSearch = new ReflectionSearch(testClass);
	}

	@Test
	public void testHasAnnotation() throws Exception {
		assertTrue(reflectionSearch.hasAnnotation("Test"));
		assertTrue(reflectionSearch.hasAnnotation("net.sourceforge.beanbin.annot.TestAnnotation"));
		assertFalse(reflectionSearch.hasAnnotation("NotANnotation"));
		assertFalse(reflectionSearch.hasAnnotation(""));
		assertFalse(reflectionSearch.hasAnnotation(null));
	}

	@Test
	public void testMethodsThatHave() throws Exception {
		reflectionSearch.methodsThatHave("test");
		List<Method> testMethods = reflectionSearch.getMethods();
		assertEquals(3, testMethods.size());
		assertTrue(testMethods.get(0).getName().equals("testMethod1"));
		assertTrue(testMethods.get(1).getName().equals("testMethod2"));
		assertTrue(testMethods.get(2).getName().equals("testMethod3"));
	}

	@Test
	public void testMethodsThatDontHave() throws Exception {
		reflectionSearch.methodsThatDontHave("test");
		List<Method> noTestMethods = reflectionSearch.getMethods();
		assertEquals(0, noTestMethods.size());
	}

	@Test
	public void testAnd() throws Exception {
		reflectionSearch.methodsThatHave("test");
		reflectionSearch.and();
		reflectionSearch.methodsThatHave("void");
		List<Method> testAndVoidMethods = reflectionSearch.getMethods();
		assertEquals(1, testAndVoidMethods.size());
		assertTrue(testAndVoidMethods.get(0).getName().equals("testMethod2"));
	}

	@Test
	public void testOr() throws Exception {
		reflectionSearch.methodsThatHave("test");
		reflectionSearch.or();
		reflectionSearch.methodsThatDontHave("void");
		List<Method> testOrNoVoidMethods = reflectionSearch.getMethods();
		assertEquals(2, testOrNoVoidMethods.size());
		assertTrue(testOrNoVoidMethods.get(0).getName().equals("testMethod1"));
		assertTrue(testOrNoVoidMethods.get(1).getName().equals("testMethod3"));
	}

	@Test(expected = BeanBinException.class)
	public void testInvalidQuery() throws Exception {
		reflectionSearch.methodsThatHave("");
		reflectionSearch.getMethods();
	}
}

class TestClass {
	@Test
	public void testMethod1() {
	}

	@Test
	@TestAnnotation
	public void testMethod2() {
	}

	public void testMethod3() {
	}
}