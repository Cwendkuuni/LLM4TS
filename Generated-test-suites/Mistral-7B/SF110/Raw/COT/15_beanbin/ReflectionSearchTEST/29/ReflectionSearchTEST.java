package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import java.lang.reflect.*;
import java.util.*;

import org.junit.Test;

import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.query.*;
import net.sourceforge.beanbin.search.*;

public class ReflectionSearchTest {

 private static final Class<ReflectionSearch> REFLECTION_SEARCH_CLASS = ReflectionSearch.class;

 @Test
 public void testHasAnnotation_EmptyAnnotation_ReturnsFalse() throws Exception {
     ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
     assertFalse(reflectionSearch.hasAnnotation(""));
 }

 @Test
 public void testHasAnnotation_EmptyTerm_ThrowsException() {
     expectThrows(IllegalArgumentException.class, () -> new ReflectionSearch(ReflectionSearchTest.class).hasAnnotation(null));
 }

 @Test
 public void testHasAnnotation_NoAnnotation_ReturnsFalse() throws Exception {
     ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
     assertFalse(reflectionSearch.hasAnnotation("Foo"));
 }

 @Test
 public void testHasAnnotation_AnnotationPresent_ReturnsTrue() throws Exception {
     ReflectionSearch reflectionSearch = new ReflectionSearch(AnnotationTest.class);
     assertTrue(reflectionSearch.hasAnnotation("AnnotationTest"));
 }

 @Test
 public void testHasAnnotation_AnnotationPresentInSuperclass_ReturnsTrue() throws Exception {
     ReflectionSearch reflectionSearch = new ReflectionSearch(SuperAnnotationTest.class);
     assertTrue(reflectionSearch.hasAnnotation("AnnotationTest"));
 }

 @Test
 public void testHasAnnotation_AnnotationWithWildcard_ReturnsTrue() throws Exception {
     ReflectionSearch reflectionSearch = new ReflectionSearch(WildcardAnnotationTest.class);
     assertTrue(reflectionSearch.hasAnnotation("A*"));
     assertTrue(reflectionSearch.hasAnnotation("Annotation*"));
 }

 @Test
 public void testHasAnnotation_AnnotationWithWildcardInSuperclass_ReturnsTrue() throws Exception {
     ReflectionSearch reflectionSearch = new ReflectionSearch(SuperWildcardAnnotationTest.class);
     assertTrue(reflectionSearch.hasAnnotation("A*"));
     assertTrue(reflectionSearch.hasAnnotation("Annotation*"));
 }

 @Test
 public void testMethodsThatHave_EmptyTerm_ThrowsException() throws Exception {
     ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
     expectThrows(IllegalArgumentException.class, () -> reflectionSearch.methodsThatHave(""));
 }

 @Test
 public void testMethodsThatHave_MethodsMatchingTerm_ReturnsReflctionSearchInstance() throws Exception {
     ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTestWithMethods.class);
     reflectionSearch.methodsThatHave("test");
     assertTrue(reflectionSearch instanceof ReflectionSearch);
 }

 @Test
 public void testMethodsThatHave_NoMatchingMethods_ThrowsBeanBinException() throws Exception {
     ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTestWithoutMethods.class);
     expectThrows(BeanBinException.class, () -> reflectionSearch.methodsThatHave("test"));
 }

 @Test
 public void testMethodsThatDontHave_EmptyTerm_ThrowsException() throws Exception {
     ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
     expectThrows(IllegalArgumentException.class, () -> reflectionSearch.methodsThatDontHave(""));
 }

 @Test
 public void testMethodsThatDontHave_MethodsNotMatchingTerm_ReturnsReflctionSearchInstance() throws Exception {
     ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTestWithMethods.class);
     reflectionSearch.methodsThatDontHave("bar");
     assertTrue(reflectionSearch instanceof ReflectionSearch);
 }

 @Test
 public void testMethodsThatDontHave_AllMethodsMatchingTerm_ThrowsBeanBinException() throws Exception {
     ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTestWithMethods.class);
     reflectionSearch.methodsThatHave("test");
     reflectionSearch.methodsThatDontHave("test");
     expectThrows(BeanBinException.class, () -> reflectionSearch.getMethods());
 }

 @Test
 public void testAnd_EmptyQuery_ThrowsBeanBinException() throws Exception {
     ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
     reflectionSearch.methodsThatHave("test");
     expectThrows(BeanBinException.class, () -> reflectionSearch.and().getMethods());
 }

 @Test
 public void testAnd_MethodsMatchingBothCriteria_ReturnsMethods() throws Exception {
     ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchAndTest.class);
     reflectionSearch.methodsThatHave("test1");
     reflectionSearch.and();
     reflectionSearch.methodsThatHave("test2");
     List<Method> methods = reflectionSearch.getMethods();
     assertTrue(methods.size() > 0);
 }

 @Test
 public void testOr_EmptyQuery_ThrowsBeanBinException() throws Exception {
     ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
     reflectionSearch.methodsThatHave("test");
     expectThrows(BeanBinException.class, () -> reflectionSearch.or().getMethods());
 }

 @Test
 public void testOr_MethodsMatchingEitherCriteria_ReturnsMethods() throws Exception {
     ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchOrTest.class);
     reflectionSearch.methodsThatHave("test1");
     reflectionSearch.or();
     reflectionSearch.methodsThatHave("test2");
     List<Method> methods = reflectionSearch.getMethods();
     assertTrue(methods.size() > 0);
 }

 @Test
 public void testOr_MethodsMatchingBothCriteria_ReturnsMethods() throws Exception {
     ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchOrTest.class);
     reflectionSearch.methodsThatHave("test1");
     reflectionSearch.or();
     reflectionSearch.methodsThatHave("test1");
     List<Method> methods = reflectionSearch.getMethods();
     assertTrue(methods.size() > 0);
 }

 @Test
 public void testGetMethods_EmptyQuery_ReturnsEmptyList() throws Exception {
     ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
     List<Method> methods = reflectionSearch.getMethods();
     assertTrue(methods.isEmpty());
 }

 @Test
 public void testGetMethods_MethodsMatchingQuery_ReturnsMethods() throws Exception {
     ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTestWithMethods.class);
     reflectionSearch.methodsThatHave("test");
     List<Method> methods = reflectionSearch.getMethods();
     assertTrue(methods.size() > 0);
 }

 @Test
 public void testGetMethods_MultipleMethodsMatchingQuery_ReturnsMethods() throws Exception {
     ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTestWithMultipleMethods.class);
     reflectionSearch.methodsThatHave("test");
     List<Method> methods = reflectionSearch.getMethods();
     assertTrue(methods.size() > 0);
 }

 @Test
 public void testIntersect_EmptyMasterList_ThrowsIllegalArgumentException() {
     List<Method> masterList = new ArrayList<>();
     List<Method> toaddList = Arrays.asList(new Method() {});
     expectThrows(IllegalArgumentException.class, () -> ReflectionSearch.intersect(masterList, toaddList));
 }

 @Test
 public void testIntersect_EmptyToaddList_DoesNotModifyMasterList() throws Exception {
     List<Method> masterList = new ArrayList<>();
     List<Method> toaddList = new ArrayList<>();
     ReflectionSearch.intersect(masterList, toaddList);
     assertTrue(masterList.isEmpty());
 }

 @Test
 public void testIntersect_MasterListWithMethodsToRemove_RemovesMethods() throws Exception {
     List<Method> masterList = Arrays.asList(new Method() {}, new Method() {}, new Method() {});
     List<Method> toaddList = Arrays.asList(new Method() {});
     ReflectionSearch.intersect(masterList, toaddList);
     assertTrue(masterList.size() == 2);
 }

 @Test
 public void testUnion_EmptyMasterList_ReturnsEmptyList() throws Exception {
     List<Method> masterList = new ArrayList<>();
     List<Method> toaddList = Arrays.asList(new Method() {});
     List<Method> resultList = ReflectionSearch.union(masterList, toaddList);
     assertTrue(resultList.isEmpty());
 }

 @Test
 public void testUnion_MasterListWithMethodsToAdd_ReturnsMergedList() throws Exception {
     List<Method> masterList = Arrays.asList(new Method() {});
     List<Method> toaddList = Arrays.asList(new Method() {}, new Method() {});
     List<Method> resultList = ReflectionSearch.union(masterList, toaddList);
     assertTrue(resultList.size() == 2);
 }

 @Test
 public void testUnion_MasterListWithMasterMethodsToAdd_ReturnsMergedList() throws Exception {
     List<Method> masterList = Arrays.asList(new Method() {}, new Method() {});
     List<Method> toaddList = Arrays.asList(new Method() {});
     List<Method> resultList = ReflectionSearch.union(masterList, masterList);
     assertTrue(resultList.size() == 3);
 }
}