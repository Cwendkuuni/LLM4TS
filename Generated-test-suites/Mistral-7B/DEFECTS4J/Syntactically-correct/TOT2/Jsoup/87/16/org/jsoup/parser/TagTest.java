import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.getName());
    }
}