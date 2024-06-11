import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Comment;

public class CommentTest {

    @Test
    public void testCommentCreation() {
        Comment comment = new Comment("This is a comment");
        String expected = "#comment";
        String actual = comment.nodeName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("This is a comment");
        String expected = "This is a comment";
        String actual = comment.getData();
        assertEquals(expected, actual);
    }

    // Test cases for other methods can be added here.
}