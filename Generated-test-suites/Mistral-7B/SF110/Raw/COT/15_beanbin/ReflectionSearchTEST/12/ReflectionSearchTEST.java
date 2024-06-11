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

 private ReflectionSearch reflectionSearch;

 @Test
 public void testReflectionSearchConstructor() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 assertNotNull(reflectionSearch);
 }

 @Test
 public void testHasAnnotation_NoAnnotation_False() {
 reflectionSearch = new ReflectionSearch(Object.class);
 assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
 }

 @Test
 public void testHasAnnotation_HasAnnotation_True() {
 Class annotatedClass = MyAnnotation.class;
 reflectionSearch = new ReflectionSearch(annotatedClass);
 assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));
 }

 @Test
 public void testHasAnnotation_WildcardAnnotation_True() {
 Class annotatedClass = MyAnnotation.class;
 reflectionSearch = new ReflectionSearch(annotatedClass);
 assertTrue(reflectionSearch.hasAnnotation("*Annotation"));
 }

 @Test
 public void testHasAnnotation_HasAnnotationWithDifferentCase_True() {
 Class annotatedClass = MyAnnotation.class;
 reflectionSearch = new ReflectionSearch(annotatedClass);
 assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));
 assertTrue(reflectionSearch.hasAnnotation("MYANNOTATION"));
 }

 @Test
 public void testHasAnnotation_HasAnnotationWithDifferentPackage_False() {
 Class annotatedClass = MyAnnotation.class;
 reflectionSearch = new ReflectionSearch(OtherPackageAnnotation.class);
 assertFalse(reflectionSearch.hasAnnotation("MyAnnotation"));
 }

 @Test
 public void testMethodsThatHave_EmptyQuery_EmptyList() {
 reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("").getMethods();
 assertTrue(reflectionSearch.getMethods().isEmpty());
 }

 @Test
 public void testMethodsThatHave_MethodsWithTermInName_Present() {
 Class clazzWithMethod = HasTermInNameClass.class;
 reflectionSearch = new ReflectionSearch(clazzWithMethod);
 reflectionSearch.methodsThatHave("methodWithTerm").getMethods();
 List<Method> methods = reflectionSearch.getMethods();
 Method method = null;
 for (Method methodInList : methods) {
 if (methodInList.getName().equals("methodWithTerm")) {
 method = methodInList;
 break;
 }
 }
 assertNotNull(method);
 }

 @Test
 public void testMethodsThatHave_MethodsWithTermInClass_Present() {
 Class clazzWithTermInClass = TermInClassPackage.class;
 reflectionSearch = new ReflectionSearch(clazzWithTermInClass);
 reflectionSearch.methodsThatHave("TermInClass").getMethods();
 List<Method> methods = reflectionSearch.getMethods();
 Method method = null;
 for (Method methodInList : methods) {
 if (methodInList.isMember() && methodInList.getDeclaringClass().getName().equals("TermInClassPackage")) {
 method = methodInList;
 break;
 }
 }
 assertNotNull(method);
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHave_InvalidTerm_Exception() {
 reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("invalidTerm").getMethods();
 fail("BeanBinException expected");
 }

 @Test
 public void testMethodsThatDontHave_EmptyQuery_EmptyList() {
 reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatDontHave("").getMethods();
 assertTrue(reflectionSearch.getMethods().isEmpty());
 }

 @Test
 public void testMethodsThatDontHave_MethodsWithoutTermInName_Absent() {
 Class clazzWithoutTermInName = NoTermInNameClass.class;
 reflectionSearch = new ReflectionSearch(clazzWithoutTermInName);
 reflectionSearch.methodsThatDontHave("Term").getMethods();
 List<Method> methods = reflectionSearch.getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test
 public void testMethodsThatDontHave_MethodsWithoutTermInClass_Absent() {
 Class clazzWithoutTermInClass = NoTermInClassPackage.class;
 reflectionSearch = new ReflectionSearch(clazzWithoutTermInClass);
 reflectionSearch.methodsThatDontHave("Term").getMethods();
 List<Method> methods = reflectionSearch.getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatDontHave_InvalidTerm_Exception() {
 reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatDontHave("invalidTerm").getMethods();
 fail("BeanBinException expected");
 }

 @Test
 public void testAndOperator() {
 Class clazzWithTermInNameAndTermInClass = TermInNameAndTermInClassPackage.class;
 reflectionSearch = new ReflectionSearch(clazzWithTermInNameAndTermInClass);
 reflectionSearch.methodsThatHave("methodWithTerm").methodsThatHave("TermInClass");
 List<Method> methods = reflectionSearch.getMethods();
 Method method = null;
 for (Method methodInList : methods) {
 if (methodInList.getName().equals("methodWithTermThatHasTermInClass")) {
 method = methodInList;
 break;
 }
 }
 assertNotNull(method);
 }

 @Test
 public void testOrOperator() {
 Class clazzWithTermInNameOrTermInClass = TermInNameOrTermInClassPackage.class;
 reflectionSearch = new ReflectionSearch(clazzWithTermInNameOrTermInClass);
 reflectionSearch.methodsThatHave("methodWithTerm").or();
 reflectionSearch.methodsThatDontHave("TermInClass").getMethods();
 List<Method> methods = reflectionSearch.getMethods();
 Method method = null;
 for (Method methodInList : methods) {
 if (methodInList.getName().equals("methodWithTermThatDoesNotHaveTermInClass")) {
 method = methodInList;
 break;
 }
 }
 assertNotNull(method);
 }

 @Test
 public void testGetMethods_EmptyQuery_EmptyList() {
 reflectionSearch = new ReflectionSearch(Object.class);
 assertTrue(reflectionSearch.getMethods().isEmpty());
 }

 @Test
 public void testGetMethods_MethodsWithTermInName_Present() {
 Class clazzWithMethod = HasTermInNameClass.class;
 reflectionSearch = new ReflectionSearch(clazzWithMethod);
 reflectionSearch.methodsThatHave("methodWithTerm");
 List<Method> methods = reflectionSearch.getMethods();
 Method method = null;
 for (Method methodInList : methods) {
 if (methodInList.getName().equals("methodWithTerm")) {
 method = methodInList;
 break;
 }
 }
 assertNotNull(method);
 }

 @Test
 public void testGetMethods_MethodsWithTermInClass_Present() {
 Class clazzWithTermInClass = TermInClassPackage.class;
 reflectionSearch = new ReflectionSearch(clazzWithTermInClass);
 reflectionSearch.methodsThatHave("TermInClass");
 List<Method> methods = reflectionSearch.getMethods();
 Method method = null;
 for (Method methodInList : methods) {
 if (methodInList.isMember() && methodInList.getDeclaringClass().getName().equals("TermInClassPackage")) {
 method = methodInList;
 break;
 }
 }
 assertNotNull(method);
 }

 @Test(expected = BeanBinException.class)
 public void testGetMethods_InvalidQuery_Exception() {
 reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.query = null;
 reflectionSearch.getMethods();
 fail("BeanBinException expected");
 }

 @Test
 public void testIntersect() {
 List<Method> list1 = new ArrayList<Method>();
 list1.add(new Method() {});
 list1.add(new Method() {});
 List<Method> list2 = new ArrayList<Method>();
 list2.add(new Method() {});
 reflectionSearch = new ReflectionSearch(new Object() {});
 reflectionSearch.intersect(list1, list2);
 assertTrue(list1.size() == 0);
 }

 @Test
 public void testUnion() {
 List<Method> list1 = new ArrayList<Method>();
 list1.add(new Method() {});
 List<Method> list2 = new ArrayList<Method>();
 list2.add(new Method() {});
 reflectionSearch = new ReflectionSearch(new Object() {});
 reflectionSearch.union(list1, list2);
 assertEquals(list1.size() + list2.size(), reflectionSearch.getMethods().size());
 }
}