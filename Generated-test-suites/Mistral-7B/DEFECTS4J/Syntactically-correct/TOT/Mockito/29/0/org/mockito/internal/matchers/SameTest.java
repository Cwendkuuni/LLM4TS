import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.JUnit4;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

@RunWith(JUnit4.class)
public class SameTest {

 // Test case for the constructor
 @Test
 public void testSameConstructor() {
     Object wanted = new Object();
     Same sameMatcher = new Same(wanted);
     // No need to check anything here, as there's no logic in the constructor
 }

 // Test case for the matches method with same objects
 @Test
 public void testSameMatchesSameObjects() {
     Object wanted = new Object();
     Same sameMatcher = new Same(wanted);
     Object actual = wanted;

     boolean result = sameMatcher.matches(actual);
     assertTrue(result);
 }

 // Test case for the matches method with different objects
 @Test(expected = AssertionError.class)
 public void testSameMatchesDifferentObjects() {
     Object wanted = new Object();
     Same sameMatcher = new Same(wanted);
     Object actual = new Object();

     boolean result = sameMatcher.matches(actual);
     fail("Should have thrown an AssertionError");
 }

 // Test case for the describeTo method with a String
 @Test
 public void testSameDescribeToString() {
     String wanted = "mockito";
     Same sameMatcher = new Same(wanted);

     Description description = new Description();
     sameMatcher.describeTo(description);

     String expectedDescription = "same(\"mockito\")";
     assertEquals(expectedDescription, description.toString());
 }

 // Test case for the describeTo method with a Character
 @Test
 public void testSameDescribeToCharacter() {
     char wanted = 'a';
     Same sameMatcher = new Same(wanted);

     Description description = new Description();
     sameMatcher.describeTo(description);

     String expectedDescription = "same('a')";
     assertEquals(expectedDescription, description.toString());
 }

 // Test case for the describeTo method with a complex object
 @Test
 public void testSameDescribeToComplexObject() {
     MyClass wanted = new MyClass();
     Same sameMatcher = new Same(wanted);

     Description description = new Description();
     sameMatcher.describeTo(description);

     String expectedDescription = "same(" + wanted.getClass().getName() + ".instance)";
     assertEquals(expectedDescription, description.toString());
 }

 // Helper class for testing equals
 private static class MyClass {
     private static final MyClass instance = new MyClass();

     private MyClass() {}

     public static MyClass getInstance() {
         return instance;
     }
 }
}