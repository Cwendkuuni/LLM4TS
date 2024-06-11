import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {
    @Test
    public void testTaggetName() {
        Tag tag = new Tag("div");
        String expectedName = "div";
        String actualName = tag.getName();
        assertEquals(expectedName, actualName);
    }
}