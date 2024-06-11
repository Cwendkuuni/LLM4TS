package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import java.lang.annotation.*;
import java.lang.reflect.*;
import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.query.*;
import net.sourceforge.beanbin.reflect.ReflectionSearch;
import org.junit.Test;

public class ReflectionSearchTest {

 private Class clazz;

 @BeforeClass
 public static void setupClass() {
 clazz = TestClassWithAnnotations.class;
 }

 @Test
 public void testHasAnnotation_ExactName() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 assertTrue(reflectionSearch.hasAnnotation("Annotation1"));
 }

 @Test
 public void testHasAnnotation_ClassName() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 assertTrue(reflectionSearch.hasAnnotation("Annotation1Test"));
 }

 @Test
 public void testHasAnnotation_NoMatch() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 assertFalse(reflectionSearch.hasAnnotation("NoMatch"));
 }

 @Test
 public void testHasAnnotation_WildcardName() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 assertTrue(reflectionSearch.hasAnnotation("Annotation*"));
 }

 @Test
 public void testHasAnnotation_WildcardClassName() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 assertTrue(reflectionSearch.hasAnnotation("TestAnno*"));
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHave_NullTerm() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatHave(null);
 }

 @Test
 public void testMethodsThatHave_Match() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatHave("getAnnotatedField");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(1, methods.size());
 }

 @Test
 public void testMethodsThatHave_NoMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatHave("setAnotherField");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatDontHave_NullTerm() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatDontHave(null);
 }

 @Test
 public void testMethodsThatDontHave_Match() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatDontHave("getAnnotatedField");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatDontHave_MatchWithMethods() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatHave("getAnnotatedField");
 List<Method> methods = reflectionSearch.getMethods();
 ReflectionSearch reflectionSearch2 = new ReflectionSearch(clazz);
 reflectionSearch2.methodsThatDontHave("getAnnotatedField");
 List<Method> methods2 = reflectionSearch2.getMethods();
 assertEquals(methods.size(), methods2.size());
 for (Method method : methods) {
 assertFalse(methods2.contains(method));
 }
 }

 @Test
 public void testAndCondition() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatHave("getAnnotatedField");
 reflectionSearch.and();
 reflectionSearch.methodsThatDontHave("setAnotherField");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testOrCondition() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatHave("getAnnotatedField");
 reflectionSearch.or();
 reflectionSearch.methodsThatDontHave("setAnotherField");
 reflectionSearch.methodsThatHave("getAnotherField");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(2, methods.size());
 }

 @Test(expected = BeanBinException.class)
 public void testGetMethods_NullQuery() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.query = null;
 reflectionSearch.getMethods();
 }
}