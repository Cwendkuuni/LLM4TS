package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.search.BeanBinException;
import net.sourceforge.beanbin.search.Criteria;
import net.sourceforge.beanbin.search.Conditional;
import net.sourceforge.beanbin.search.MethodReflectionCriteria;
import net.sourceforge.beanbin.search.ReflectionCriteria;
import net.sourceforge.beanbin.search.SearchType;

public class ReflectionSearchTest {

	@Test
	public void testReflectionSearchInstance() {
		Class<ReflectionSearch> clazz = ReflectionSearch.class;
		ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
		assertNotNull(reflectionSearch);
	}

	@Test
	public void testHasAnnotation() {
		Class<ReflectionSearch> clazz = ReflectionSearch.class;
		ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

		assertTrue(reflectionSearch.hasAnnotation("Annotation1"));
		assertTrue(reflectionSearch.hasAnnotation("java.lang.annotation.Annotation"));
		assertFalse(reflectionSearch.hasAnnotation("Annotation2"));
		assertFalse(reflectionSearch.hasAnnotation("NonAnnotationClass"));
	}

	@Test
	public void testHasWildcard() {
		ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
		assertTrue(reflectionSearch.hasWildcard("Annotation*"));
		assertFalse(reflectionSearch.hasWildcard("Annotation"));
		assertFalse(reflectionSearch.hasWildcard("NonAnnotation"));
	}

	@Test
	public void testMethodsThatHave() throws BeanBinException {
		Class<ReflectionSearch> clazz = ReflectionSearch.class;
		ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
		List<Method> methods = Arrays.asList(clazz.getMethods());

		reflectionSearch.methodsThatHave("test*");
		List<Method> result = reflectionSearch.getMethods();

		for (Method method : methods) {
			String methodName = method.getName();
			assertTrue(methodName.startsWith("test"));
			assertFalse(result.contains(method));
		}

		for (Method method : result) {
			assertTrue(method.getName().startsWith("test"));
		}
	}

	@Test
	public void testMethodsThatDontHave() throws BeanBinException {
		Class<ReflectionSearch> clazz = ReflectionSearch.class;
		ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
		List<Method> methods = Arrays.asList(clazz.getMethods());

		reflectionSearch.methodsThatDontHave("test*");
		List<Method> result = reflectionSearch.getMethods();

		for (Method method : methods) {
			String methodName = method.getName();
			assertFalse(methodName.startsWith("test"));
			assertFalse(result.contains(method));
		}

		for (Method method : result) {
			assertFalse(method.getName().startsWith("test"));
		}
	}

	@Test
	public void testAndOperator() throws BeanBinException {
		Class<ReflectionSearch> clazz = ReflectionSearch.class;
		ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
		List<Method> methods = Arrays.asList(clazz.getMethods());

		reflectionSearch.methodsThatHave("test");
		reflectionSearch.and();
		reflectionSearch.methodsThatHave("Method");
		List<Method> result = reflectionSearch.getMethods();

		for (Method method : methods) {
			String methodName = method.getName();
			assertFalse(result.contains(method));
		}

		List<Method> andResult = new ArrayList<>();
		andResult.add(clazz.getMethod("testMethod1"));
		andResult.add(clazz.getMethod("testMethod2"));

		for (Method method : result) {
			assertTrue(andResult.contains(method));
		}
	}

	@Test
	public void testOrOperator() throws BeanBinException {
		Class<ReflectionSearch> clazz = ReflectionSearch.class;
		ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
		List<Method> methods = Arrays.asList(clazz.getMethods());

		reflectionSearch.methodsThatHave("test");
		reflectionSearch.or();
		reflectionSearch.methodsThatHave("Method");
		List<Method> result = reflectionSearch.getMethods();

		for (Method method : methods) {
			String methodName = method.getName();
			assertFalse(result.contains(method));
		}

		List<Method> orResult = new ArrayList<>();
		orResult.add(clazz.getMethod("testMethod1"));
		orResult.add(clazz.getMethod("MethodTest1"));
		orResult.add(clazz.getMethod("testMethod2"));
		orResult.add(clazz.getMethod("MethodTest2"));

		for (Method method : result) {
			assertTrue(orResult.contains(method));
		}
	}

	@Test
	public void testIntersect() {
		List<Method> master = Arrays.asList(new Method[] { new Method() {}, new Method() {}, new Method() {} });
		List<Method> toadd = Arrays.asList(new Method[] { new Method() {}, new Method() {} });

		ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
		reflectionSearch.intersect(master, toadd);

		assertEquals(1, master.size());
		assertEquals(0, toadd.size());
	}

	@Test
	public void testUnion() {
		List<Method> master = Arrays.asList(new Method[] { new Method() {}, new Method() {} });
		List<Method> toadd = Arrays.asList(new Method[] { new Method() {}, new Method() {}, new Method() {} });

		ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
		reflectionSearch.union(master, toadd);

		assertEquals(3, master.size());
		assertEquals(0, toadd.size());
	}

	private void assertEquals(int expected, int actual) {
		if (expected != actual) {
			fail("Expected: " + expected + " Actual: " + actual);
		}
	}
}