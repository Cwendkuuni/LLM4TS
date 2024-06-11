import org.junit.Test;
import static org.junit.Assert.assertEquals;
import de.beiri22.stringincrementor.StringIncrementor;
import de.beiri22.stringincrementor.helper.IndexedString;
import de.beiri22.stringincrementor.helper.StringBuilder;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.relativestring.StringLink;

public class StringIncrementorTest {

 @Test
 public void testDiffEmptyStrings() {
 String a = "";
 String b = "";
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals("", result.getAbsolute());
 }

 @Test
 public void testDiffIdenticalStrings() {
 String a = "hello";
 String b = "hello";
 RelativeString result = StringIncrementor.diff(a, b, false);
 assertEquals("", result.getAbsolute());
 }

 @Test
 public void testDiffSingleCharacterDifference() {
 String a = "hello";
 String b = "helium";
 RelativeString result = StringIncrementor.diff(a, b, false);
 String expected = new StringLink(5, 5, 1).toString() + " @ 50%";
 System.out.println(result);
 assertEquals(expected, result.toString());
 }

 @Test
 public void testDiffMultipleCharacterDifference() {
 String a = "hello";
 String b = "heliumon";
 RelativeString result = StringIncrementor.diff(a, b, false);
 String expected = new StringLink(5, 5, 1).toString() + " @ 50%\n" +
 new StringLink(6, 6, 1).toString() + " @ 60%\n" +
 new StringLink(7, 7, 3).toString() + " @ 70%";
 System.out.println(result);
 assertEquals(expected, result.toString());
 }

 @Test
 public void testDiffEdgeCaseLongStrings() {
 String a = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras porttitor metus justo, ut faucibus urna hendrerit et. Ut fringilla consectetur mi in aliquam. Donec vitae ante luctus, commodo tortor sit amet, bibendum mi. Aliquam ac justo eget mauris dapibus auctor. Fusce rhoncus orci ut mauris ultrices, id consequat justo pulvinar. Donec auctor, tellus eu aliquet varius, velit augue rhoncus nibh, a interdum lectus quam eu nibh.";
 String b = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras porttitor metus justo, ut faucibus urna hendrerit et. Ut fringilla consectetur mi in aliquam. Donec vitae ante luctus, commodo tortor sit amet, bibendum mi. Aliquam ac justo eget mauris dapibus auctor. Fusce rhoncus orci ut mauris ultrices, id consequat justo pulvinar. Donec auctor, tellus eu aliquet varius, velit augue rhoncus nibh, a interdum lectus quam eu nibh.";
 // This test case may take a long time to execute due to the large string comparison
 RelativeString result = StringIncrementor.diff(a, b, false);
 String expected = "";
 // The output of this test case is not important as it's just a large string comparison
 assertEquals(expected, result.getAbsolute());
 }

 @Test
 public void testDiffErrorCaseNullString() {
 String a = null;
 String b = "hello";
 try {
 RelativeString result = StringIncrementor.diff(a, b, false);
 fail("Should have thrown NullPointerException");
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test
 public void testPatchEmptyStrings() {
 String a = "";
 RelativeString r = new RelativeString();
 String result = StringIncrementor.patch(a, r, false);
 assertEquals("", result);
 }

 @Test
 public void testPatchIdenticalStrings() {
 String a = "hello";
 RelativeString r = new RelativeString();
 String result = StringIncrementor.patch(a, r, false);
 assertEquals("hello", result);
 }

 @Test
 public void testPatchSingleCharacterDifference() {
 String a = "hello";
 RelativeString r = new StringLink(5, 5, 1).toString();
 String result = StringIncrementor.patch(a, r, false);
 assertEquals("helium", result);
 }

 @Test
 public void testPatchMultipleCharacterDifference() {
 String a = "hello";
 RelativeString r = new StringLink(5, 5, 1).toString() + "\n" +
 new StringLink(6, 6, 1).toString() + "\n" +
 new StringLink(7, 7, 3).toString();
 String result = StringIncrementor.patch(a, r, false);
 assertEquals("heliumon", result);
 }

 @Test
 public void testPatchEdgeCaseLongStrings() {
 String a = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras porttitor metus justo, ut faucibus urna hendrerit et. Ut fringilla consectetur mi in aliquam. Donec vitae ante luctus, commodo tortor sit amet, bibendum mi. Aliquam ac justo eget mauris dapibus auctor. Fusce rhoncus orci ut mauris ultrices, id consequat justo pulvinar. Donec auctor, tellus eu aliquet varius, velit augue rhoncus nibh, a interdum lectus quam eu nibh.";
 RelativeString r = new RelativeString();
 r.addLink(new StringLink(5, 5, 1));
 r.addLink(new StringLink(6, 6, 1));
 r.addLink(new StringLink(7, 7, 3));
 String result = StringIncrementor.patch(a, r, false);
 String expected = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras porttitor metus justo, ut faucibus urna hendrerit et. Ut fringilla consectetur mi in aliquam. Donec vitae ante luctus, commodo tortor sit amet, bibendum mi. Aliquam ac justo eget mauris dapibus auctor. Fusce rhoncus orci ut mauris ultrices, id consequat justo pulvinar. Donec auctor, tellus eu aliquet varius, velit augue rhoncus nibh, a interdum lectus quam eu nibh.";
 assertEquals(expected, result);
 }

 @Test
 public void testPatchErrorCaseNullString() {
 String a = null;
 RelativeString r = new RelativeString();
 try {
 String result = StringIncrementor.patch(a, r, false);
 fail("Should have thrown NullPointerException");
 } catch (NullPointerException e) {
 // Expected exception
 }
 }
}