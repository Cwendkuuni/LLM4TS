package net.sourceforge.beanbin.reflect;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.*;
import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;

 @BeforeAll
 public void setup() throws Exception {
 testClass = YourClassUnderTest.class;
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotation_AnnotationPresent() {
 Annotation annotation = testClass.getAnnotation(YourAnnotation.class);
 assertTrue(reflectionSearch.hasAnnotation(YourAnnotation.class.getName()));
 }

 @Test
 public void testHasAnnotation_AnnotationNotPresent() {
 assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
 }

 @Test
 public void testHasAnnotation_WildcardAnnotation() {
 assertTrue(reflectionSearch.hasAnnotation("net.sourceforge.beanbin.annotation.YourBaseAnnotation*"));
 }

 @Test
 public void testMethodsThatHave_MethodPresent() throws BeanBinException {
 reflectionSearch.methodsThatHave("methodName");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(1, methods.size());
 assertEquals(MethodType.METHOD, methods.get(0).getType());
 // Perform assertions on the method
 }

 @Test
 public void testMethodsThatHave_MethodNotPresent() throws BeanBinException {
 reflectionSearch.methodsThatHave("NonExistentMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatDontHave_MethodPresent() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("methodName");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatDontHave_MethodNotPresent() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("NonExistentMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(YourClassUnderTest.class.getDeclaredMethods().length, methods.size());
 }

 @Test
 public void testAndCondition() throws BeanBinException {
 reflectionSearch.methodsThatHave("methodName");
 reflectionSearch.methodsThatDontHave("excludedMethodName");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(YourClassUnderTest.class.getDeclaredMethods().length - 1, methods.size());
 }

 @Test
 public void testOrCondition() throws BeanBinException {
 reflectionSearch.methodsThatHave("methodName1");
 reflectionSearch.methodsThatHave("methodName2");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(YourClassUnderTest.class.getDeclaredMethods().length, methods.size());
 }

 @AfterAll
 public void teardown() throws Exception {
 reflectionSearch = null;
 testClass = null;
 }

}