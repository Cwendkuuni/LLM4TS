import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class VerseTest {

    @Test
    public void testNewMethod() {
        int id = 1;
        Verse verse = Verse.New(id);
        assertEquals(id, verse.getId());
    }

    // Add test cases for other methods
}