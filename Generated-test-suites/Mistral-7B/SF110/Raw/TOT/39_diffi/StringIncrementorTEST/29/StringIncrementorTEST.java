import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import de.beiri22.stringincrementor.*;

public class StringIncrementorTest {

 private String a;
 private String b;
 private RelativeString expectedResult;

 @Before
 public void setUp() {
 a = "hello world";
 b = "hellohello worldworld";
 expectedResult = new RelativeString();
 expectedResult.addLink(new StringLink(5, 0, 5));
 expectedResult.addLink(new StringLink(12, 6, 5));
 expectedResult.setAbsolute("llworld");
 }

 @Test
 public void testDiff() {
 RelativeString result = StringIncrementor.diff(a, b);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testDiff_verbose() {
 System.out.println("Testing diff with verbose mode...");
 RelativeString result = StringIncrementor.diff(a, b, true);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiff_emptyStrings() {
 // This test case should throw an IllegalArgumentException as the method accepts only non-empty strings
 StringIncrementor.diff("", "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiff_nullStrings() {
 // This test case should throw an IllegalArgumentException as the method accepts only non-null strings
 StringIncrementor.diff(null, b);
 }

 @Test
 public void testPatch() {
 String patchedString = StringIncrementor.patch(a, expectedResult);
 assertEquals("hellollworld", patchedString);
 }

 @Test
 public void testPatch_verbose() {
 System.out.println("Testing patch with verbose mode...");
 String patchedString = StringIncrementor.patch(a, expectedResult, true);
 assertEquals("hellollworld", patchedString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatch_invalidRelativeString() {
 // This test case should throw an IllegalArgumentException as the method accepts only valid RelativeString objects
 StringIncrementor.patch(a, new RelativeString());
 }
}