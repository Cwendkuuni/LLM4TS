import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.junit.Assert.assertThrows;

public class StringIncrementorTest {

    @Test
    public void testDiff_SameStrings() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcdefghijklmnopqrstuvwxyz";

        RelativeString expectedResult = new RelativeString();

        StringIncrementor.diff(a, b, false); //Invoke method without storing the result

        RelativeString actualResult = new RelativeString();
        actualResult = StringIncrementor.diff(a, b, false); //Store the result this time

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDiff_Verbose_SameStrings() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcdefghijklmnopqrstuvwxyz";

        String systemOutCaptured = System.out.getOutputStream().getText(); //Capture the System.out

        StringIncrementor.diff(a, b, true); //Invoke method with verbose flag

        String expectedSystemOut = "Creating index...\nSearching links...\n@ 50.0%\nLink: StringLink{startPosNew=5, endPosNew=13, length=8, startPosOrig=-1, endPosOrig=-1}\n@ 100.0%"; //Expected System.out output

        String actualSystemOut = systemOutCaptured; //Get the captured System.out

        assertEquals(expectedSystemOut, actualSystemOut);

        RelativeString expectedResult = new RelativeString();
        expectedResult = StringIncrementor.diff(a, b, false); //Invoke method without storing the result
        expectedResult.addLink(new StringLink(0, 0, 8));

        RelativeString actualResult = new RelativeString();
        actualResult = StringIncrementor.diff(a, b, false); //Store the result this time

        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiff_ThrowsIllegalArgumentException_EmptyStrings() {
        String a = "";
        String b = "";

        assertThrows(IllegalArgumentException.class, () -> StringIncrementor.diff(a, b, false));
    }

    @Test
    public void testDiff_NoLinksFound() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcdabcdabcdabcdabcd";

        RelativeString expectedResult = new RelativeString();
        expectedResult.setAbsolute("");

        RelativeString actualResult = StringIncrementor.diff(a, b, false);

        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiff_ThrowsIllegalArgumentException_NullStrings() {
        String a = null;
        String b = null;

        assertThrows(IllegalArgumentException.class, () -> StringIncrementor.diff(a, b, false));
    }

    @Test
    public void testDiff_DifferentStrings() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcdabcdabcdabcdabcdabcefghijklmnopqrstuvwxyz";

        RelativeString expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(18, 30, 12));

        RelativeString actualResult = StringIncrementor.diff(a, b, false);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDiff_SpecialCharacters() {
        String a = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+-=[]{}|;:,.<>?/";
        String b = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+-=[]{}|;:,.<>?/abcdefghijklmnopqrstuvwxyz";

        RelativeString expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(0, 26, 26));

        RelativeString actualResult = StringIncrementor.diff(a, b, false);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDiff_LeadingTrailingWhiteSpaces() {
        String a = " abcdefghijklmnopqrstuvwxyz ";
        String b = "abcdabcdabcdabcdabcd";

        RelativeString expectedResult = new RelativeString();
        expectedResult.setAbsolute("");

        RelativeString actualResult = StringIncrementor.diff(a, b, false);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDiff_Verbose_DifferentStrings() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcdabcdabcdabcdabcdabcefghijklmnopqrstuvwxyz";

        String systemOutCaptured = System.out.getOutputStream().getText(); //Capture the System.out

        StringIncrementor.diff(a, b, true); //Invoke method with verbose flag

        String expectedSystemOut = "Creating index...\nSearching links...\n@ 21.18%\nLink: StringLink{startPosNew=21, endPosNew=33, length=12, startPosOrig=18, endPosOrig=30}\n@ 100.0%"; //Expected System.out output

        String actualSystemOut = systemOutCaptured; //Get the captured System.out

        assertEquals(expectedSystemOut, actualSystemOut);

        RelativeString expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(18, 30, 12));

        RelativeString actualResult = StringIncrementor.diff(a, b, false);

        assertEquals(expectedResult, actualResult);
    }
}