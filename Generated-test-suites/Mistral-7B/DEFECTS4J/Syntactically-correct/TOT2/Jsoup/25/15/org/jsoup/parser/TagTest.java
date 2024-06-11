import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TagTest {

    @Test
    public void testName() {
        Tag tag = new Tag("div");
        String expected = "div";
        String actual = tag.getName();
        assertEquals(expected, actual);
    }
}