import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testCompileValidInput() {
        String input1 = "/abc/123";
        JsonPointer expected1 = new JsonPointer("/abc/123");
        JsonPointer actual1 = JsonPointer.compile(input1);
        assertEquals(expected1, actual1);

        String input2 = "/";
        JsonPointer expected2 = JsonPointer.EMPTY;
        JsonPointer actual2 = JsonPointer.compile(input2);
        assertEquals(expected2, actual2);

        String input3 = "/a/b/c/d/e/0";
        JsonPointer expected3 = new JsonPointer("/a/b/c/d/e/0");
        JsonPointer actual3 = JsonPointer.compile(input3);
        assertEquals(expected3, actual3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        String input = "invalid";
        JsonPointer.compile(input);
    }
}