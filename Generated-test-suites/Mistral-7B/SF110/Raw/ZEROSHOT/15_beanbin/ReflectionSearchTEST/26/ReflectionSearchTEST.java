package net.sourceforge.beanbin.reflect.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.beanbin.query.Conditional;
import net.sourceforge.beanbin.query.Criteria;
import net.sourceforge.beanbin.query.ReflectionCriteria;
import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.reflect.ReflectionShelf;

import org.junit.Test;

public class ReflectionSearchTest {

	@Test
	public void testHasAnnotation() throws Exception {
		Class clazz = Class.forName("net.sourceforge.beanbin.reflect.ReflectionSearch");
		ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

		assertTrue(reflectionSearch.hasAnnotation("ReflectionSearch"));
		assertTrue(reflectionSearch.hasAnnotation("Search"));
		assertFalse(reflectionSearch.hasAnnotation("nonExistentAnnotation"));
	}

	@Test
	public void testHasWildcard() {
		assertTrue(reflectionSearch.hasWildcard("*Search"));
		assertTrue(reflectionSearch.hasWildcard("Reflection*"));
		assertFalse(reflectionSearch.hasWildcard("Reflection"));
	}

	@Test
	public void testMethodsThatHave() throws Exception {
		Class clazz = Class.forName("net.sourceforge.beanbin.reflect.ReflectionSearch");
		ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

		reflectionSearch.methodsThatHave("hasAnnotation");
		List<Method> methods = reflectionSearch.getMethods();
		Method method = clazz.getMethod("hasAnnotation", String.class);
		assertTrue(methods.contains(method));
		assertEquals(1, methods.size());
	}

	@Test
	public void testMethodsThatDontHave() throws Exception {
		Class clazz = Class.forName("net.sourceforge.beanbin.reflect.ReflectionSearch");
		ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

		reflectionSearch.methodsThatDontHave("hasAnnotation");
		List<Method> methods = reflectionSearch.getMethods();
		assertEquals(0, methods.size());
	}

	@Test
	public void testAnd() throws Exception {
		Class clazz = Class.forName("net.sourceforge.beanbin.reflect.ReflectionSearch");
		ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

		reflectionSearch.methodsThatHave("getMethods");
		reflectionSearch.and();
		reflectionSearch.methodsThatHave("equals");
		List<Method> methods = reflectionSearch.getMethods();
		Method getMethods = clazz.getMethod("getMethods", (Class[])null);
		Method equals = clazz.getMethod("equals", Object.class);
		assertTrue(methods.contains(getMethods));
		assertTrue(methods.contains(equals));
		assertEquals(2, methods.size());
	}

	@Test
	public void testOr() throws Exception {
		Class clazz = Class.forName("net.sourceforge.beanbin.reflect.ReflectionSearch");
		ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

		reflectionSearch.methodsThatHave("getMethods");
		reflectionSearch.or();
		reflectionSearch.methodsThatHave("equals");
		reflectionSearch.methodsThatHave("hashCode");
		List<Method> methods = reflectionSearch.getMethods();
		Method getMethods = clazz.getMethod("getMethods", (Class[])null);
		Method equals = clazz.getMethod("equals", Object.class);
		Method hashCode = clazz.getMethod("hashCode", (Class[])null);
		assertTrue(methods.contains(getMethods));
		assertTrue(methods.contains(equals));
		assertTrue(methods.contains(hashCode));
		assertEquals(3, methods.size());
	}

	@Test(expected = BeanBinException.class)
	public void testInvalidSearch() throws Exception {
		Class clazz = Class.forName("net.sourceforge.beanbin.reflect.ReflectionSearch");
		ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

		reflectionSearch.methodsThatHave("nonExistentMethod");
		reflectionSearch.getMethods();
	}

	@Test
	public void testReflectionShelf() throws Exception {
		Class clazz = Class.forName("net.sourceforge.beanbin.reflect.ReflectionSearch");
		ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

		reflectionSearch.methodsThatHave("getMethods");
		List<Method> methods = reflectionSearch.getMethods();
		assertEquals(1, methods.size());

		ReflectionSearch reflectionSearch2 = new ReflectionSearch(clazz);
		List<Method> methods2 = reflectionSearch2.getMethods();
		assertEquals(1, methods2.size());

		assertTrue(methods.equals(methods2));
	}

}