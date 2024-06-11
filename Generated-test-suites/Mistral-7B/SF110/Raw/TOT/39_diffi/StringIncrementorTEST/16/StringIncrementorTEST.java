import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import de.beiri22.stringincrementor.*;
import static de.beiri22.stringincrementor.helper.StringHelper.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StringIncrementorTest
{
    @BeforeAll
    public void setup()
    {
        System.out.println("### Setting up test environment ###");
    }

    @Test
    public void testDiff_SameStrings()
    {
        String a = "hello";
        String b = "hello";
        RelativeString expectedResult = new RelativeString();
        String result = StringIncrementor.diff(a, b);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDiff_EmptyStrings()
    {
        String a = "";
        String b = "";
        RelativeString expectedResult = new RelativeString();
        String result = StringIncrementor.diff(a, b);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDiff_EmptyStringAndNonEmptyString()
    {
        String a = "";
        String b = "hello";
        RelativeString expectedResult = new RelativeString(new StringLink(0, 0, 5), "hello");
        String result = StringIncrementor.diff(a, b);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDiff_NullString()
    {
        String a = null;
        String b = "hello";
        assertThrows(NullPointerException.class, () -> StringIncrementor.diff(a, b));
    }

    @Test
    public void testDiff_NullStringB()
    {
        String a = "hello";
        String b = null;
        assertThrows(NullPointerException.class, () -> StringIncrementor.diff(a, b));
    }

    @Test
    public void testDiff_StringsWithDifferentLengthsButSameContent()
    {
        String a = "hello";
        String b = "hello world";
        RelativeString expectedResult = new RelativeString(new StringLink(6, 6, 5), "world");
        String result = StringIncrementor.diff(a, b);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDiff_StringsWithDifferentLengthsAndDifferentContent()
    {
        String a = "hello";
        String b = "world hello";
        RelativeString expectedResult = new RelativeString(new StringLink(0, 6, 6), "world", new StringLink(6, 12, 6), "hello");
        String result = StringIncrementor.diff(a, b);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDiff_Verbose_SameStrings()
    {
        String a = "hello";
        String b = "hello";
        RelativeString expectedResult = new RelativeString();
        String result = StringIncrementor.diff(a, b, true);
        String output = System.out.getStackTrace()[0].getClassName() + ":" + System.out.getStackTrace()[0].getLineNumber();
        assertTrue(result.toString().contains(output + ": Creating index..."));
        assertTrue(result.toString().contains(output + ": Searching links..."));
        assertTrue(result.toString().contains("@ 0%"));
        assertTrue(result.toString().contains("@ 50%"));
        assertTrue(result.toString().contains("@ 100%"));
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDiff_Verbose_EmptyStrings()
    {
        String a = "";
        String b = "";
        RelativeString expectedResult = new RelativeString();
        String result = StringIncrementor.diff(a, b, true);
        String output = System.out.getStackTrace()[0].getClassName() + ":" + System.out.getStackTrace()[0].getLineNumber();
        assertTrue(result.toString().contains(output + ": Creating index..."));
        assertTrue(result.toString().contains(output + ": Searching links..."));
        assertTrue(result.toString().contains("@ 0%"));
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDiff_Verbose_EmptyStringAndNonEmptyString()
    {
        String a = "";
        String b = "hello";
        RelativeString expectedResult = new RelativeString(new StringLink(0, 0, 5), "hello");
        String result = StringIncrementor.diff(a, b, true);
        String output = System.out.getStackTrace()[0].getClassName() + ":" + System.out.getStackTrace()[0].getLineNumber();
        assertTrue(result.toString().contains(output + ": Creating index..."));
        assertTrue(result.toString().contains(output + ": Searching links..."));
        assertTrue(result.toString().contains("@ 0%"));
        assertTrue(result.toString().contains("@ 50%"));
        assertTrue(result.toString().contains("@ 100%"));
        assertEquals(expectedResult, result);
    }

    @Test
    public void testPatch_SameStrings()
    {
        String a = "hello";
        String patch = "";
        String result = StringIncrementor.patch(a, new RelativeString());
        assertEquals(a, result);
    }

    @Test
    public void testPatch_EmptyString()
    {
        String a = "";
        String patch = "hello";
        String result = StringIncrementor.patch(a, new RelativeString(new StringLink(0, 0, 5), "hello"));
        assertEquals("hello", result);
    }

    @Test
    public void testPatch_NullString()
    {
        String a = "hello";
        String patch = null;
        assertThrows(NullPointerException.class, () -> StringIncrementor.patch(a, new RelativeString()));
    }

    @Test
    public void testPatch_RelativeStringWithEmptyAbsolutePart()
    {
        String a = "hello world";
        RelativeString patch = new RelativeString(new StringLink(6, 6, 5), "world");
        String result = StringIncrementor.patch(a, patch);
        assertEquals("helloworld", result);
    }

    @Test
    public void testPatch_StringsWithDifferentLengthsButSameContent()
    {
        String a = "hello";
        String patch = "hello world";
        String result = StringIncrementor.patch(a, new RelativeString(new StringLink(6, 6, 5), "world"));
        assertEquals("helloworld", result);
    }

    @Test
    public void testPatch_StringsWithDifferentLengthsAndDifferentContent()
    {
        String a = "hello world";
        String patch = new RelativeString(new StringLink(0, 6, 6), "world", new StringLink(6, 12, 6), "hello");
        String result = StringIncrementor.patch(a, patch);
        assertEquals("helloworld", result);
    }

    @AfterAll
    public void teardown()
    {
        System.out.println("### Tearing down test environment ###");
    }
}