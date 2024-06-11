package net.sourceforge.beanbin.reflect;

import net.sourceforge.beanbin.search.*;
import net.sourceforge.beanbin.reflect.annotation.*;
import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {
    
    @Test
    public void testHasAnnotation_AnnotationPresent() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(AnnotationClass.class);
        assertTrue(reflectionSearch.hasAnnotation("Annotation"));
    }
    
    @Test
    public void testHasAnnotation_WildcardCharacter() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(AnnotationInterfaceWithClass.class);
        assertTrue(reflectionSearch.hasAnnotation("An*notation"));
    }
    
    @Test(expected = BeanBinException.class)
    public void testHasAnnotation_Exception() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(NullClass.class);
        reflectionSearch.hasAnnotation("Annotation");
    }
    
    @Test
    public void testMethodsThatHave_MethodsPresent() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClassWithMethods.class);
        reflectionSearch.methodsThatHave("methodName");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size()); // Assuming there are 2 methods with the given name
    }
    
    @Test
    public void testMethodsThatHave_NoMatchingMethods() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClassWithNoMatchingMethods.class);
        reflectionSearch.methodsThatHave("nonExistentMethod");
        assertEquals(0, reflectionSearch.getMethods().size());
    }
    
    @Test(expected = BeanBinException.class)
    public void testMethodsThatHave_Exception() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(NullClass.class);
        reflectionSearch.methodsThatHave("methodName");
    }
    
    // Add other test cases for other methods (methodsThatDontHave, and, or) as needed
}