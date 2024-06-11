import static org.junit.Assert.*;
import de.beiri22.stringincrementor.RelativeString;
import de.beiri22.stringincrementor.StringIncrementor;
import org.junit.Test;

public class StringIncrementorTest {
    @Test
    public void testDiff() {
        String a = "";
        String b = "";
        RelativeString expected = new RelativeString();
        StringIncrementor.diff(a, b);
        assertEquals(expected, StringIncrementor.diff(a, b));

        a = "";
        b = "abc";
        expected = new RelativeString();
        StringIncrementor.diff(a, b);
        assertEquals(expected, StringIncrementor.diff(a, b));

        a = "abc";
        b = "";
        expected = new RelativeString();
        StringIncrementor.diff(a, b);
        assertEquals(expected, StringIncrementor.diff(a, b));

        a = "abc";
        b = "abc";
        RelativeString expected1 = new RelativeString();
        expected1.addLink(new StringLink(0, 0, 3));
        StringIncrementor.diff(a, b);
        assertEquals(expected1, StringIncrementor.diff(a, b));

        a = "abc";
        b = "abcd";
        RelativeString expected2 = new RelativeString();
        expected2.addLink(new StringLink(0, 0, 3));
        expected2.addLink(new StringLink(3, 3, 1));
        StringIncrementor.diff(a, b);
        assertEquals(expected2, StringIncrementor.diff(a, b));

        a = "abc";
        b = "HelloWorld";
        RelativeString expected3 = new RelativeString();
        StringIncrementor.diff(a, b);
        assertEquals(expected3, StringIncrementor.diff(a, b));

        a = "HelloWorld";
        b = "abc";
        RelativeString expected4 = new RelativeString();
        StringIncrementor.diff(a, b);
        assertEquals(expected4, StringIncrementor.diff(a, b));

        a = "HelloWorld";
        b = "HelloWorld";
        RelativeString expected5 = new RelativeString();
        StringIncrementor.diff(a, b, true);
        assertEquals(expected5, StringIncrementor.diff(a, b));

        a = "HelloWorld";
        b = "HelloWorldHelloWorld";
        RelativeString expected6 = new RelativeString();
        expected6.addLink(new StringLink(0, 0, 6));
        expected6.addLink(new StringLink(12, 12, 6));
        StringIncrementor.diff(a, b);
        assertEquals(expected6, StringIncrementor.diff(a, b));
    }

    @Test
    public void testDiffWithVerbose() {
        String a = "";
        String b = "";
        StringIncrementor.diff(a, b, true);

        a = "";
        b = "abc";
        StringIncrementor.diff(a, b, true);

        a = "abc";
        b = "";
        StringIncrementor.diff(a, b, true);

        a = "abc";
        b = "abc";
        StringIncrementor.diff(a, b, true);

        a = "abc";
        b = "abcd";
        StringIncrementor.diff(a, b, true);

        a = "abc";
        b = "HelloWorld";
        StringIncrementor.diff(a, b, true);

        a = "HelloWorld";
        b = "abc";
        StringIncrementor.diff(a, b, true);

        a = "HelloWorld";
        b = "HelloWorld";
        StringIncrementor.diff(a, b, true);

        a = "HelloWorld";
        b = "HelloWorldHelloWorld";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 6));
        expected.addLink(new StringLink(12, 12, 6));
        System.out = new PrintStream(new ByteArrayOutputStream()) {
            @Override
            public void println(String message) {
                System.out.println("Test: " + message);
            }
        };
        StringIncrementor.diff(a, b, true);
        assertEquals(expected, StringIncrementor.diff(a, b));
        System.out = System.out;
    }

    @Test
    public void testPatch() {
        String a = "abc";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 3));
        String expected = "abc";
        String result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);

        a = "abc";
        r = new RelativeString();
        r.addLink(new StringLink(3, 3, 1));
        expected = "abcd";
        result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);

        a = "abc";
        r = new RelativeString();
        r.addLink(new StringLink(0, 0, 6));
        r.addLink(new StringLink(12, 12, 6));
        expected = "HelloWorldHelloWorld";
        result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);

        a = "HelloWorld";
        r = new RelativeString();
        r.addLink(new StringLink(0, 0, 6));
        r.addLink(new StringLink(12, 12, 6));
        expected = "HelloWorldHelloWorldHelloWorld";
        result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);
    }

    @Test
    public void testPatchWithVerbose() {
        String a = "abc";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 3));
        System.out = new PrintStream(new ByteArrayOutputStream()) {
            @Override
            public void println(String message) {
                System.out.println("Test: " + message);
            }
        };
        StringIncrementor.patch(a, r, true);
        String expected = "abc";
        assertEquals(expected, StringIncrementor.patch(a, r));
        System.out = System.out;

        a = "abc";
        r = new RelativeString();
        r.addLink(new StringLink(3, 3, 1));
        System.out = new PrintStream(new ByteArrayOutputStream()) {
            @Override
            public void println(String message) {
                System.out.println("Test: " + message);
            }
        };
        StringIncrementor.patch(a, r, true);
        expected = "abcd";
        assertEquals(expected, StringIncrementor.patch(a, r));
        System.out = System.out;

        a = "abc";
        r = new RelativeString();
        r.addLink(new StringLink(0, 0, 6));
        r.addLink(new StringLink(12, 12, 6));
        System.out = new PrintStream(new ByteArrayOutputStream()) {
            @Override
            public void println(String message) {
                System.out.println("Test: " + message);
            }
        };
        StringIncrementor.patch(a, r, true);
        expected = "HelloWorldHelloWorld";
        assertEquals(expected, StringIncrementor.patch(a, r));
        System.out = System.out;

        a = "HelloWorld";
        r = new RelativeString();
        r.addLink(new StringLink(0, 0, 6));
        r.addLink(new StringLink(12, 12, 6));
        System.out = new PrintStream(new ByteArrayOutputStream()) {
            @Override
            public void println(String message) {
                System.out.println("Test: " + message);
            }
        };
        StringIncrementor.patch(a, r, true);
        expected = "HelloWorldHelloWorldHelloWorld";
        assertEquals(expected, StringIncrementor.patch(a, r));
        System.out = System.out;
    }
}