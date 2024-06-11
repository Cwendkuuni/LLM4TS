import org.junit.Assert;
import org.junit.Test;
import de.beiri22.stringincrementor.*;
import static de.beiri22.stringincrementor.helper.StringHelper.createString;

public class StringIncrementorTest {

 private final String baseline = "baselineString";
 private final String modifiedString1 = "modifiedString1";
 private final String modifiedString2 = "modifiedString2";
 private final String erroneousString = "erroneousString";

 @Test
 public void testDiffWithVerboseFalse() {
 String a = createString("aStringA");
 String b = createString("aStringB");
 RelativeString result = StringIncrementor.diff(a, b);
 RelativeString expectedResult = new RelativeString();
 expectedResult.addLink(new StringLink(1, 4, 3, "link1"));
 expectedResult.setAbsolute("expectedString1");
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testDiffWithVerboseTrue() {
 String a = createString("aStringA");
 String b = createString("aStringB");
 RelativeString result = StringIncrementor.diff(a, b, true);
 RelativeString expectedResult = new RelativeString();
 expectedResult.addLink(new StringLink(1, 4, 3, "link1"));
 expectedResult.setAbsolute("expectedString1");
 System.out.println("Expected output:");
 System.out.println(expectedResult);
 StringIncrementor.main(new String[]{"-v", "-a", a, "-b", b});
 RelativeString actualResult = new RelativeString();
 actualResult.parse(System.in);
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDiffWithInvalidStrings() {
 String a = null;
 String b = createString("aStringB");
 RelativeString result = StringIncrementor.diff(a, b);
 }

 @Test
 public void testDiffWithEmptyStrings() {
 String a = createString("");
 String b = createString("");
 RelativeString result = StringIncrementor.diff(a, b);
 RelativeString expectedResult = new RelativeString();
 expectedResult.setAbsolute("");
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testPatchWithVerboseFalse() {
 String a = createString("aStringA");
 String patch = createString("link1: 3, link2: 5-7");
 String expectedResult = createString("resultString");
 StringIncrementor.patch(a, StringIncrementor.parseRelativeString(patch), false);
 Assert.assertEquals(expectedResult, a);
 }

 @Test
 public void testPatchWithVerboseTrue() {
 String a = createString("aStringA");
 String patch = createString("link1: 3, link2: 5-7");
 String expectedResult = createString("resultString");
 StringIncrementor.patch(a, StringIncrementor.parseRelativeString(patch), true);
 StringIncrementor.main(new String[]{"-v", "-p", a, "-r", patch});
 String actualResult = System.console().readLine();
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatchWithInvalidPatch() {
 String a = createString("aStringA");
 String patch = createString("link1: 3, link2: 5-7, link3: 3asd");
 StringIncrementor.patch(a, StringIncrementor.parseRelativeString(patch), false);
 }

 @Test
 public void testPatchWithEmptyPatch() {
 String a = createString("aStringA");
 String patch = createString("");
 String expectedResult = createString("aStringA");
 StringIncrementor.patch(a, StringIncrementor.parseRelativeString(patch), false);
 Assert.assertEquals(expectedResult, a);
 }
}