import org.junit.Assert;
import org.junit.Test;
import de.beiri22.stringincrementor.*;

public class StringIncrementorTest {

    @Test
    public void testDiffEmptyStrings() {
        String a = "";
        String b = "";
        RelativeString expected = new RelativeString();
        RelativeString result = StringIncrementor.diff(a, b, false);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDiffSameStrings() {
        String a = "hello";
        String b = "hello";
        RelativeString expected = new RelativeString();
        RelativeString result = StringIncrementor.diff(a, b, false);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDiffDifferentStrings() {
        String a = "hello";
        String b = "world";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(4, 0, 5, "hello"));
        expected.addLink(new StringLink(5, 5, 1, "world"));
        RelativeString result = StringIncrementor.diff(a, b, false);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDiffVerbose() {
        String a = "hello";
        String b = "world";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(4, 0, 5, "hello"));
        expected.addLink(new StringLink(5, 5, 1, "world"));
        RelativeString result = StringIncrementor.diff(a, b, true);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiffNullStrings() {
        String a = null;
        String b = "world";
        StringIncrementor.diff(a, b, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiffNullRelativeString() {
        String a = "hello";
        RelativeString b = null;
        StringIncrementor.diff(a, b, false);
    }

    @Test
    public void testPatchEmptyString() {
        String a = "";
        RelativeString r = new RelativeString();
        String expected = "";
        String result = StringIncrementor.patch(a, r, false);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPatchSingleLink() {
        String a = "hello world";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(5, 5, 1, "world"));
        String expected = "hello worldworld";
        String result = StringIncrementor.patch(a, r, false);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPatchMultipleLinks() {
        String a = "hello world hello world";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(5, 5, 1, "world"));
        r.addLink(new StringLink(13, 13, 1, "world"));
        String expected = "hello worldworldhello worldworld";
        String result = StringIncrementor.patch(a, r, false);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPatchVerbose() {
        String a = "hello world hello world";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(5, 5, 1, "world"));
        r.addLink(new StringLink(13, 13, 1, "world"));
        String expected = "hello worldworldhello worldworld";
        String result = StringIncrementor.patch(a, r, true);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatchNullString() {
        String a = null;
        RelativeString r = new RelativeString();
        StringIncrementor.patch(a, r, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatchNullStringRelativeString() {
        String a = "hello";
        RelativeString r = null;
        StringIncrementor.patch(a, r, false);
    }
}