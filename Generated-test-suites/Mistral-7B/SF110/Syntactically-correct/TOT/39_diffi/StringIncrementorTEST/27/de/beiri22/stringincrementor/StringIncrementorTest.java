import org.junit.Test;
import static org.junit.Assert.*;
import de.beiri22.stringincrementor.*;

public class StringIncrementorTest {

 // Test for diff method with empty strings
 @Test
 public void testDiffEmptyStrings() {
 String a = "";
 String b = "";
 RelativeString expectedResult = new RelativeString();
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals(expectedResult, result);
 }

 // Test for diff method with identical strings
 @Test
 public void testDiffIdenticalStrings() {
 String a = "HelloWorld";
 String b = "HelloWorld";
 RelativeString expectedResult = new RelativeString();
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals(expectedResult, result);
 }

 // Test for diff method with one character difference
 @Test
 public void testDiffOneCharacterDifference() {
 String a = "HelloWorld";
 String b = "HelloWorld";
 RelativeString expectedResult = new StringLink(4, 4, 1);
 expectedResult.setAbsolute("ld");
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals(expectedResult, result);
 }

 // Test for diff method with multiple character differences
 @Test
 public void testDiffMultipleCharacterDifferences() {
 String a = "HelloWorld";
 String b = "HeLloWorLd";
 RelativeString expectedResult = new StringLink(0, 1, 1)
 .addLink(new StringLink(3, 4, 3))
 .addLink(new StringLink(6, 7, 1));
 expectedResult.setAbsolute("HeLldWorLd");
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals(expectedResult, result);
 }

 // Test for diff method with verbose output
 @Test
 public void testDiffVerbose() {
 String a = "HelloWorld";
 String b = "HeLloWorLd";
 String verboseOutput = "Link: @ 25 % (4, 4, 1) ld\n";
 System.out.println("Verbose output:");
 System.out.println("----------------------------------");
 StringIncrementor.diff(a, b, true);
 String expectedVerboseOutput = System.out.getByteArray();
 String resultVerboseOutput = new String(expectedVerboseOutput);
 assertTrue(resultVerboseOutput.contains(verboseOutput));
 RelativeString expectedResult = new StringLink(3, 4, 1);
 expectedResult.setAbsolute("ld");
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals(expectedResult, result);
 }

 // Test for patch method with empty strings
 @Test
 public void testPatchEmptyStrings() {
 String a = "";
 RelativeString r = new RelativeString();
 String expectedResult = "";
 String result = StringIncrementor.patch(a, r, false);
 assertEquals(expectedResult, result);
 }

 // Test for patch method with no links
 @Test
 public void testPatchNoLinks() {
 String a = "HelloWorld";
 RelativeString r = new RelativeString();
 String expectedResult = "HelloWorld";
 String result = StringIncrementor.patch(a, r, false);
 assertEquals(expectedResult, result);
 }

 // Test for patch method with single link
 @Test
 public void testPatchSingleLink() {
 String a = "HelloWorld";
 RelativeString r = new StringLink(4, 4, 1);
 r.setAbsolute("ld");
 String expectedResult = "HellolWorld";
 String result = StringIncrementor.patch(a, r, false);
 assertEquals(expectedResult, result);
 }

 // Test for patch method with multiple links
 @Test
 public void testPatchMultipleLinks() {
 String a = "HelloWorld";
 RelativeString r = new RelativeString()
 .addLink(new StringLink(3, 4, 3))
 .addLink(new StringLink(6, 7, 1));
 r.setAbsolute("HeLldWorLd");
 String expectedResult = "HeLloWorLd";
 String result = StringIncrementor.patch(a, r, false);
 assertEquals(expectedResult, result);
 }

 // Test for patch method with verbose output
 @Test
 public void testPatchVerbose() {
 String a = "HelloWorld";
 String b = "HeLloWorLd";
 RelativeString r = new StringLink(4, 4, 1);
 r.setAbsolute("ld");
 String verboseOutput = "Adding 3 absolute Bytes from pos#0\n";
 System.out.println("Verbose output:");
 System.out.println("----------------------------------");
 StringIncrementor.patch(a, r, true);
 String expectedVerboseOutput = System.out.getByteArray();
 String resultVerboseOutput = new String(expectedVerboseOutput);
 assertTrue(resultVerboseOutput.contains(verboseOutput));
 String expectedResult = "HeLloWorLd";
 String result = StringIncrementor.patch(a, r, false);
 assertEquals(expectedResult, result);
 }
}