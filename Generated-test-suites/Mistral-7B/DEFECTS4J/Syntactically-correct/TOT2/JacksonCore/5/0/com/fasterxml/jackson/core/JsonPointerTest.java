import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JsonPointerTest {

    @Test
    public void testJsonPointerCompile() {
        String input = "/foo/bar/baz";
        JsonPointer expected = new JsonPointer("/foo/bar/baz");
        JsonPointer actual = JsonPointer.compile(input);
        assertEquals(expected, actual);
    }
}