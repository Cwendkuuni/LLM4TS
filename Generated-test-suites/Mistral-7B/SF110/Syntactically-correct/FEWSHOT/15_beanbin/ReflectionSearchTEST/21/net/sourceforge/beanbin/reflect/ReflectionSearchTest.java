package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;
import java.lang.reflect.*;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Annotation.class);
        assertTrue(reflectionSearch.hasAnnotation("Annotation"));
        assertFalse(reflectionSearch.hasAnnotation("bar"));
        reflectionSearch = new ReflectionSearch(MyAnnotation.class);
        assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));
        String myAnnotationTerm = "Annotati*";
        ReflectionSearch reflectionSearchWithWildcard = new ReflectionSearch(MyAnnotation.class);
        assertTrue(reflectionSearchWithWildcard.hasAnnotation(myAnnotationTerm));
    }

    @Test
    public void testHasWildcard() {
        assertTrue(ReflectionSearch.getInstance().hasWildcard("Annotation*"));
        assertFalse(ReflectionSearch.getInstance().hasWildcard("Annotation"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(MyClass.class);
        reflectionSearch.methodsThatHave("methodName");
        List<Method> methods = reflectionSearch.getMethods();
        Method expectedMethod = MyClass.class.getMethod("methodName", new Class[0]);
        assertEquals(expectedMethod, methods.get(0));
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(MyClass.class);
        reflectionSearch.methodsThatDontHave("methodName");
        List<Method> methods = reflectionSearch.getMethods();
        assertTrue(methods.isEmpty());
    }

    @Test
    public void testAnd() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(MyClass.class);
        reflectionSearch.methodsThatHave("methodName1");
        reflectionSearch.and();
        reflectionSearch.methodsThatHave("methodName2");
        List<Method> methods = reflectionSearch.getMethods();
        List<Method> expectedMethods = new ArrayList<>();
        expectedMethods.add(MyClass.class.getMethod("methodName1", new Class[0]));
        expectedMethods.add(MyClass.class.getMethod("methodName2", new Class[0]));
        assertEquals(expectedMethods, methods);
    }

    @Test
    public void testOr() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(MyClass.class);
        reflectionSearch.methodsThatHave("methodName1");
        reflectionSearch.or();
        reflectionSearch.methodsThatHave("methodName2");
        List<Method> methods = reflectionSearch.getMethods();
        List<Method> expectedMethods = new ArrayList<>();
        expectedMethods.addAll(Arrays.asList(MyClass.class.getMethod("methodName1", new Class[0]), MyClass.class.getMethod("methodName2", new Class[0])));
        assertEquals(expectedMethods, methods);
    }
}

class MyAnnotation {
    @Retention(RetentionPolicy.RUNTIME)
    @InterfaceStability(InterfaceStability.UNCOMMITED)
    public @interface MyAnnotation {
        String value();
    }
}

class MyClass {
    public void methodName1() {
    }

    public void methodName2() {
    }
}