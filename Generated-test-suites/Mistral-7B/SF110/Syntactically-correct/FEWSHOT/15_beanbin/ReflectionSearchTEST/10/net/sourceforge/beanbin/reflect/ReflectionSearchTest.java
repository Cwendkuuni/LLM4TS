package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch search = new ReflectionSearch(String.class);

        assertTrue(search.hasAnnotation("ToString"));
        assertTrue(search.hasAnnotation("Equals"));
        assertTrue(search.hasAnnotation("HashCode"));
        assertFalse(search.hasAnnotation("NonExistentAnnotation"));

        Class enumClass = Enum.class;
        ReflectionSearch searchForEnum = new ReflectionSearch(enumClass);
        assertTrue(searchForEnum.hasAnnotation("EnumConstant"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch search = new ReflectionSearch(String.class);

        search.methodsThatHave("equals");
        List<Method> methods = search.getMethods();
        assertEquals(2, methods.size());
        assertTrue(methods.get(0).getName().equals("equals"));
        assertTrue(methods.get(1).getName().equals("equals"));

        search = new ReflectionSearch(Object.class);
        search.methodsThatHave("toString");
        methods = search.getMethods();
        assertEquals(1, methods.size());
        assertTrue(methods.get(0).getName().equals("toString"));
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch search = new ReflectionSearch(String.class);

        search.methodsThatDontHave("equals");
        List<Method> methods = search.getMethods();
        assertEquals(11, methods.size());

        search = new ReflectionSearch(Object.class);
        search.methodsThatDontHave("toString");
        methods = search.getMethods();
        assertEquals(25, methods.size());
    }

    @Test
    public void testAnd() throws BeanBinException {
        ReflectionSearch search = new ReflectionSearch(String.class);

        search.methodsThatHave("equals");
        search.and();
        search.methodsThatDontHave("hashCode");
        List<Method> methods = search.getMethods();
        assertEquals(0, methods.size());

        search = new ReflectionSearch(Object.class);
        search.methodsThatHave("equals");
        search.and();
        search.methodsThatHave("hashCode");
        methods = search.getMethods();
        assertEquals(1, methods.size());
        assertTrue(methods.get(0).getName().equals("equals"));
    }

    @Test
    public void testOr() throws BeanBinException {
        ReflectionSearch search = new ReflectionSearch(String.class);

        search.methodsThatHave("equals");
        search.or();
        search.methodsThatHave("toString");
        List<Method> methods = search.getMethods();
        assertEquals(3, methods.size());
        assertTrue(methods.get(0).getName().equals("equals"));
        assertTrue(methods.get(1).getName().equals("equals"));
        assertTrue(methods.get(2).getName().equals("toString"));

        search = new ReflectionSearch(Object.class);
        search.methodsThatHave("equals");
        search.or();
        search.methodsThatHave("toString");
        search.or();
        search.methodsThatHave("hashCode");
        methods = search.getMethods();
        assertEquals(3, methods.size());
        assertTrue(methods.get(0).getName().equals("equals"));
        assertTrue(methods.get(1).getName().equals("toString"));
        assertTrue(methods.get(2).getName().equals("hashCode"));
    }
}