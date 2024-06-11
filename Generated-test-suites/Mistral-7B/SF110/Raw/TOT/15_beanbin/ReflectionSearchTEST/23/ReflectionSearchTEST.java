import org.junit.Before;
import org.junit.Test;
import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.query.Criteria;
import net.sourceforge.beanbin.query.SearchType;
import net.sourceforge.beanbin.query.ReflectionCriteria;
import net.sourceforge.beanbin.query.Conditional;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;

 @Before
 public void setup() {
 testClass = TestClassWithAnnotations.class;
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotation_ExactMatch() {
 reflectionSearch.hasAnnotation("Annotation1");
 assertTrue(reflectionSearch.hasAnnotation("Annotation1"));
 }

 @Test
 public void testHasAnnotation_ClassMatch() {
 reflectionSearch.hasAnnotation("Annotation1");
 reflectionSearch = new ReflectionSearch(testClass); // Reset instance
 reflectionSearch.hasAnnotation("Annotation1");
 assertTrue(reflectionSearch.hasAnnotation("Annotation1"));
 }

 @Test
 public void testHasAnnotation_NoMatch() {
 reflectionSearch.hasAnnotation("NonExistingAnnotation");
 assertFalse(reflectionSearch.hasAnnotation("NonExistingAnnotation"));
 }

 @Test
 public void testHasAnnotation_WildcardMatch() {
 reflectionSearch.hasAnnotation("Annot*ion");
 reflectionSearch = new ReflectionSearch(testClass); // Reset instance
 reflectionSearch.hasAnnotation("Annot*ion");
 assertTrue(reflectionSearch.hasAnnotation("Annotation1"));
 assertTrue(reflectionSearch.hasAnnotation("Annotation2"));
 }

 @Test
 public void testMethodsThatHave_NoMatch() throws BeanBinException {
 reflectionSearch.methodsThatHave("NonExistingMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertFalse(methods.isEmpty());
 assertTrue(methods.size() == 0);
 }

 @Test
 public void testMethodsThatHave_Match() throws BeanBinException {
 reflectionSearch.methodsThatHave("testMethod");
 methods = reflectionSearch.getMethods();
 List<Method> expectedMethods = Arrays.asList(testClass.getMethod("testMethod"), testClass.getMethod("testMethod2"));
 assertTrue(methods.size() >= expectedMethods.size());
 for (Method method : methods) {
 assertTrue(expectedMethods.contains(method));
 }
 }

 @Test
 public void testMethodsThatHave_MultipleMatches() throws BeanBinException {
 reflectionSearch.methodsThatHave("testMeth*");
 methods = reflectionSearch.getMethods();
 expectedMethods = Arrays.asList(testClass.getMethod("testMethod"), testClass.getMethod("testMethod2"), testClass.getMethod("testMethod3"));
 assertTrue(methods.size() >= expectedMethods.size());
 for (Method method : methods) {
 assertTrue(expectedMethods.contains(method));
 }
 }

 @Test
 public void testMethodsThatDontHave_NoMatch() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("NonExistingMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test
 public void testMethodsThatDontHave_Match() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("privateMethod");
 methods = reflectionSearch.getMethods();
 List<Method> expectedMethods = Arrays.asList(testClass.getMethod("testMethod"), testClass.getMethod("testMethod2"), testClass.getMethod("testMethod3"), testClass.getMethod("privateMethod1"), testClass.getMethod("privateMethod2"));
 List<Method> filteredMethods = new ArrayList<>();
 for (Method method : methods) {
 if (!method.getName().equals("privateMethod")) {
 filteredMethods.add(method);
 }
 }
 assertTrue(methods.size() > 0);
 assertTrue(methods.size() == filteredMethods.size());
 for (Method method : methods) {
 if (!method.getName().equals("privateMethod")) {
 assertTrue(filteredMethods.contains(method));
 }
 }
 }

 @Test
 public void testAndConditional() throws BeanBinException {
 reflectionSearch.methodsThatHave("testMeth*");
 reflectionSearch.methodsThatDontHave("testMethod3");
 methods = reflectionSearch.getMethods();
 List<Method> expectedMethods = Arrays.asList(testClass.getMethod("testMethod"), testClass.getMethod("testMethod2"));
 assertTrue(methods.size() >= expectedMethods.size());
 for (Method method : methods) {
 assertTrue(expectedMethods.contains(method));
 assertTrue(!method.getName().equals("testMethod3"));
 }
 }

 @Test
 public void testOrConditional() throws BeanBinException {
 reflectionSearch.methodsThatHave("testMeth*");
 reflectionSearch.methodsThatDontHave("testMethod2");
 methods = reflectionSearch.getMethods();
 List<Method> expectedMethods = Arrays.asList(testClass.getMethod("testMethod"), testClass.getMethod("testMethod3"));
 assertTrue(methods.size() >= expectedMethods.size());
 for (Method method : methods) {
 assertTrue(expectedMethods.contains(method));
 assertTrue(!method.getName().equals("testMethod2"));
 }
 }
}

class TestClassWithAnnotations {
 @Annotation1
 public void testMethod() {
 // Test method implementation
 }

 @Annotation2
 public void testMethod2() {
 // Test method implementation
 }

 private void testMethod3() {
 // Test method implementation
 }
}