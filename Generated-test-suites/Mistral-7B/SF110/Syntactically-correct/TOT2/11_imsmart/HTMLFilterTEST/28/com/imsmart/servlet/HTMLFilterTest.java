import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilterNullInput() {
        String actual = HTMLFilter.filter(null);
        assertNull(actual);
    }

    @Test
    public void testFilterEmptyInput() {
        String actual = HTMLFilter.filter("");
        assertEquals("", actual);
    }

    @Test
    public void testFilterSimpleInput() {
        String actual = HTMLFilter.filter("Hello World!");
        String expected = "Hello World!";
        expected = expected.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("&", "&amp;").replaceAll("\"", "&quot;");
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterScenario1() {
        String actual = HTMLFilter.filter("This is a <test> case with & special characters and \"quotes\".");
        String expected = "This is a &lt;test&gt; case with &amp; special characters and &quot;quotes&quot;.";
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterNullInputException() {
        String message = null;
        String actual = HTMLFilter.filter(message);
        // NullPointerException is expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterIllegalArgumentException() {
        String message = new String(new byte[0]);
        String actual = HTMLFilter.filter(message);
        // IllegalArgumentException is expected
    }

    @Test
    public void testFilterScenario2() {
        String actual = HTMLFilter.filter("This is a <script> tag <img src=\"image.jpg\" onerror=\"alert('XSS')\"> case.");
        String expected = "This is a &lt;script&gt; tag &lt;img src=&quot;image.jpg&quot; onerror=&quot;alert('XSS')&quot;&gt; case.";
        assertEquals(expected, actual);
    }

    // ...
}