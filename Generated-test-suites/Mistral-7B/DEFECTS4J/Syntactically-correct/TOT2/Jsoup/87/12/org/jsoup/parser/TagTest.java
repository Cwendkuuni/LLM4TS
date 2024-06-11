import org.junit.Test;
import org.jsoup.parser.Tag;
import static org.junit.Assert.assertEquals;

public class TagTest {
    @Test
    public void testTagValueOf() {
        Tag pTag = Tag.valueOf("p");
        String expected = "p";
        assertEquals(expected, pTag.getName());

        Tag unknownTag = Tag.valueOf("unknownTag");
        assertEquals("unknownTag", unknownTag.getName()); // unknown tags are not registered and will only .equals()
    }

    @Test
    public void testTagIsBlock() {
        Tag pTag = Tag.valueOf("p");
        boolean expected = true;
        assertEquals(expected, pTag.isBlock());

        Tag inputTag = Tag.valueOf("input");
        expected = false;
        assertEquals(expected, inputTag.isBlock());
    }

    @Test
    public void testTagIsEmpty() {
        Tag imgTag = Tag.valueOf("img");
        boolean expected = true;
        assertEquals(expected, imgTag.isEmpty());

        Tag divTag = Tag.valueOf("div");
        expected = false;
        assertEquals(expected, divTag.isEmpty());
    }
}