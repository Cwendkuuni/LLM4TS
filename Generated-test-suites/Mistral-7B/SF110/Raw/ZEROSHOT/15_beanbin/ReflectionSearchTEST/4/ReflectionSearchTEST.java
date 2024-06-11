package net.sourceforge.beanbin.reflect.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.beanbin.query.SearchType;
import net.sourceforge.beanbin.reflect.ReflectionCriteria;
import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.reflect.ReflectionShelf;

import org.junit.Test;

public class ReflectionSearchTest {

	@Test
	public void testHasAnnotation() throws Exception {
		Class clazz = Object.class.forName("java.util.ArrayList");
		ReflectionSearch search = new ReflectionSearch(clazz);

		// Test exact match
		assertTrue(search.hasAnnotation("ArrayList"));
		assertTrue(search.hasAnnotation("java.util.ArrayList"));

		// Test wildcard match
		assertTrue(search.hasAnnotation("List*"));
		assertTrue(search.hasAnnotation("ArrayList"));
		assertTrue(search.hasAnnotation("LinkedList"));

		// Test negative case
		assertFalse(search.hasAnnotation("AnotherClass"));
	}

	@Test
	public void testMethodsThatHave() throws Exception {
		Class clazz = Object.class.forName("java.util.ArrayList");
		ReflectionSearch search = new ReflectionSearch(clazz);

		// Test exact match
		search.methodsThatHave("add");
		List<Method> methods = search.getMethods();
		assertEquals(2, methods.size());
		assertTrue(methods.get(0).getName().equals("add"));
		assertTrue(methods.get(1).getName().equals("add"));

		// Test wildcard match
		search.methodsThatHave("a*");
		methods = search.getMethods();
		assertEquals(3, methods.size());
		assertTrue(methods.get(0).getName().equals("add"));
		assertTrue(methods.get(1).getName().equals("add"));
		assertTrue(methods.get(2).getName().equals("remove"));

		// Test negative case
		search.methodsThatHave("remove");
		methods = search.getMethods();
		assertEquals(2, methods.size());
		assertFalse(methods.get(0).getName().equals("remove"));
		assertTrue(methods.get(1).getName().equals("remove"));
	}

	@Test
	public void testMethodsThatDontHave() throws Exception {
		Class clazz = Object.class.forName("java.util.ArrayList");
		ReflectionSearch search = new ReflectionSearch(clazz);

		// Test exact match
		search.methodsThatDontHave("add");
		List<Method> methods = search.getMethods();
		assertEquals(12, methods.size());
		assertFalse(methods.get(0).getName().equals("add"));
		assertFalse(methods.get(1).getName().equals("add"));

		// Test wildcard match
		search.methodsThatDontHave("a*");
		methods = search.getMethods();
		assertEquals(11, methods.size());
		assertFalse(methods.get(0).getName().equals("add"));
		assertFalse(methods.get(1).getName().equals("add"));
		assertTrue(methods.get(2).getName().equals("clone"));
		assertTrue(methods.get(3).getName().equals("getClass"));
		assertTrue(methods.get(4).getName().equals("finalize"));
		assertTrue(methods.get(5).getName().equals("hashCode"));
		assertTrue(methods.get(6).getName().equals("notify"));
		assertTrue(methods.get(7).getName().equals("notifyAll"));
		assertTrue(methods.get(8).getName().equals("toString"));
		assertTrue(methods.get(9).getName().equals("wait"));
		assertTrue(methods.get(10).getName().equals("wait"));

		// Test negative case
		search.methodsThatDontHave("add");
		methods = search.getMethods();
		assertEquals(11, methods.size());
		assertTrue(methods.get(0).getName().equals("add"));
		assertFalse(methods.get(1).getName().equals("add"));
	}

	@Test
	public void testAnd() throws Exception {
		Class clazz = Object.class.forName("java.util.ArrayList");
		ReflectionSearch search = new ReflectionSearch(clazz);

		search.methodsThatHave("get");
		search.and();
		search.methodsThatDontHave("toString");

		List<Method> methods = search.getMethods();
		assertEquals(1, methods.size());
		assertTrue(methods.get(0).getName().equals("get"));
	}

	@Test
	public void testOr() throws Exception {
		Class clazz = Object.class.forName("java.util.ArrayList");
		ReflectionSearch search = new ReflectionSearch(clazz);

		search.methodsThatHave("get");
		search.or();
		search.methodsThatHave("set");

		List<Method> methods = search.getMethods();
		assertEquals(3, methods.size());
		assertTrue(methods.get(0).getName().equals("get"));
		assertTrue(methods.get(1).getName().equals("get"));
		assertTrue(methods.get(2).getName().equals("set"));
	}

	@Test(expected = BeanBinException.class)
	public void testException() throws Exception {
		Class clazz = Object.class.forName("java.util.ArrayList");
		ReflectionSearch search = new ReflectionSearch(clazz);
		search.methodsThatHave("removeElement");
		search.getMethods();
	}

	@After
	public void tearDown() {
		ReflectionShelf.getInstance().clear();
	}

}