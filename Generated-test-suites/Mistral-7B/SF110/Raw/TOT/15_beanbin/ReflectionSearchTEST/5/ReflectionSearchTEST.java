package net.sourceforge.beanbin.reflect;

import org.junit.Assert;
import org.junit.Test;
import net.sourceforge.beanbin.query.Criteria;
import net.sourceforge.beanbin.query.Conditional;
import net.sourceforge.beanbin.query.ReflectionCriteria;
import net.sourceforge.beanbin.query.ReflectionShelf;
import net.sourceforge.beanbin.search.SearchType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);

        // Test when the term matches an exact class or interface name
        Assert.assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));

        // Test when the term matches a superclass name
        Class superclass = AnnotationTestClass.class.getSuperclass();
        Assert.assertTrue(reflectionSearch.hasAnnotation(superclass.getName()));

        // Test when the term matches an interface name
        Assert.assertTrue(reflectionSearch.hasAnnotation(AnnotationTestInterface.class.getName()));

        // Test when the term matches an annotation with a wildcard
        Assert.assertTrue(reflectionSearch.hasAnnotation("*Annotation"));

        // Test when the term does not match any annotation
        Assert.assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));

        // Test when the term matches an annotation with a wildcard character in its name
        Assert.assertTrue(reflectionSearch.hasAnnotation("MyAnnotation*"));

        // Test when the term matches an inner class or interface
        Assert.assertTrue(reflectionSearch.hasAnnotation(AnnotationTestClass.InnerClass.class.getName()));
        Assert.assertTrue(reflectionSearch.hasAnnotation(AnnotationTestClass.InnerInterface.class.getName()));
    }

    @Test
    public void testMethodsThatHaveAndMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);

        // Test methods that have the specified name
        List<Method> methodsWithName = reflectionSearch.methodsThatHave("getMyAnnotation").getMethods();
        Assert.assertEquals(1, methodsWithName.size());
        Assert.assertEquals("getMyAnnotation", methodsWithName.get(0).getName());

        // Test methods that do not have the specified name
        List<Method> methodsWithoutName = reflectionSearch.methodsThatDontHave("getMyAnnotation").getMethods();
        Assert.assertEquals(2, methodsWithoutName.size());
        Assert.assertFalse(methodsWithoutName.contains(methodsWithName.get(0)));
    }

    @Test
    public void testAndOrConditions() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);

        // Test methods that have the specified name and return type
        List<Method> methodsWithNameAndReturnType = reflectionSearch.methodsThatHave("getName").and().methodsThatHave(void.class)
                                                                                            .getMethods();
        Assert.assertEquals(1, methodsWithNameAndReturnType.size());
        Assert.assertEquals("getName", methodsWithNameAndReturnType.get(0).getName());

        // Test methods that have the specified name or return type
        List<Method> methodsWithNameOrReturnType = reflectionSearch.methodsThatHave("getName").or()
                                                                                          .methodsThatHave(int.class)
                                                                                          .getMethods();
        Assert.assertEquals(2, methodsWithNameOrReturnType.size());
        Assert.assertEquals("getName", methodsWithNameOrReturnType.get(0).getName());
        Assert.assertEquals("getNameInt", methodsWithNameOrReturnType.get(1).getName());
    }

    @Test
    public void testGetMethods() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);

        // Test getting methods that match the specified criteria
        List<Method> methodsWithName = reflectionSearch.methodsThatHave("getName").getMethods();
        Assert.assertEquals(3, methodsWithName.size());
        Assert.assertTrue(methodsWithName.contains(AnnotationTestClass.getNameMethod()));
        Assert.assertTrue(methodsWithName.contains(AnnotationTestClass.getNameAnnotationMethod()));
        Assert.assertTrue(methodsWithName.contains(AnnotationTestClass.getNameAnnotationMethodWithArgs()));

        // Test getting methods that match the specified criteria with AND condition
        List<Method> methodsWithNameAndReturnType = reflectionSearch.methodsThatHave("getName").and().methodsThatHave(void.class)
                                                                                            .getMethods();
        Assert.assertEquals(1, methodsWithNameAndReturnType.size());
        Assert.assertEquals("getName", methodsWithNameAndReturnType.get(0).getName());

        // Test getting methods that match the specified criteria with OR condition
        List<Method> methodsWithNameOrReturnType = reflectionSearch.methodsThatHave("getName").or()
                                                                                          .methodsThatHave(int.class)
                                                                                          .getMethods();
        Assert.assertEquals(2, methodsWithNameOrReturnType.size());
        Assert.assertTrue(methodsWithNameOrReturnType.contains(AnnotationTestClass.getNameMethod()));
        Assert.assertTrue(methodsWithNameOrReturnType.contains(AnnotationTestClass.getNameIntMethod()));
    }

    @Test
    public void testCache() throws BeanBinException {
        ReflectionSearch reflectionSearch1 = new ReflectionSearch(AnnotationTestClass.class);
        List<Method> methods1 = reflectionSearch1.getMethods();

        ReflectionSearch reflectionSearch2 = new ReflectionSearch(AnnotationTestClass.class);
        List<Method> methods2 = reflectionSearch2.getMethods();

        Assert.assertEquals(methods1, methods2);
    }

    static class AnnotationTestClass {
        @MyAnnotation
        public String getName() {
            return "Name";
        }

        @MyAnnotation
        public String getNameAnnotation() {
            return "Annotation";
        }

        @MyAnnotation
        public String getNameAnnotationMethod() {
            return "AnnotationMethod";
        }

        @MyAnnotation
        public String getNameAnnotationMethodWithArgs(int arg) {
            return "AnnotationMethodWithArgs " + arg;
        }

        @MyAnnotation
        public void getNameMethod() {
        }

        @MyAnnotation
        public int getNameInt() {
            return 42;
        }

        @MyAnnotation
        public int getNameIntMethod() {
            return 43;
        }

        static class InnerClass {
            @MyAnnotation
            public String getName() {
                return "InnerClass";
            }
        }

        public interface InnerInterface {
            @MyAnnotation
            String getName();
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @InterfaceStability(stable = false)
    @Target(ElementType.METHOD)
    @Target(ElementType.TYPE)
    @MyAnnotation
    public @interface MyAnnotation {
    }
}