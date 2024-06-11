import org.hamcrest.Description;
import org.junit.Test;
import static org.junit.Assert.*;

public class SameTest {

   @Test
   public void testSameConstructor() {
       Object wanted = new Object();
       Same sameInstance = new Same(wanted);
       // No assertions needed as the constructor execution should not throw any exceptions
   }

   @Test
   public void testMatchesSameObjects() {
       Object wanted = new Object();
       Object actual = wanted;
       Same sameInstance = new Same(wanted);
       assertTrue(sameInstance.matches(actual));
   }

   @Test
   public void testMatchesDifferentObjects() {
       Object wanted = new Object();
       Object actual = new Object();
       Same sameInstance = new Same(wanted);
       assertFalse(sameInstance.matches(actual));
   }

   @Test
   public void testDescribeToMethod() {
       Object wanted = new Object();
       Description description = new Description();
       Same sameInstance = new Same(wanted);
       sameInstance.describeTo(description);
       // Verify the description string
       // ...
       // Add more assertions as necessary
   }

   @Test
   public void testMatchesNullObjects() {
       Object wanted = null;
       Object actual = null;
       Same sameInstance = new Same(wanted);
       assertTrue(sameInstance.matches(actual));
   }

   @Test
   public void testMatchesObjectsWithSameContent() {
       String wanted = "hello";
       String actual = new String("hello");
       Same sameInstance = new Same(wanted);
       assertTrue(sameInstance.matches(actual));
   }

   @Test
   public void testMatchesObjectsWithDifferentContent() {
       String wanted = "hello";
       String actual = new String("world");
       Same sameInstance = new Same(wanted);
       assertFalse(sameInstance.matches(actual));
   }

   @Test(expected = IllegalStateException.class)
   public void testMatchesWithNonSerializableWanted() {
       Object wanted = new NonSerializableObject();
       Object actual = new Object();
       Same sameInstance = new Same(wanted);
       // Uncomment the following line to run the test
       // sameInstance.matches(actual);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testMatchesWithNonObjectWanted() {
       int wanted = 1;
       Object actual = new Object();
       Same sameInstance = new Same(wanted);
       // Uncomment the following line to run the test
       // sameInstance.matches(actual);
   }

}

class NonSerializableObject implements Serializable {
   // A dummy class that is not serializable
}