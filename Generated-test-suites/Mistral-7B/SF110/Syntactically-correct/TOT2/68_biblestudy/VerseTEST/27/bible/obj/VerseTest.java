import org.junit.Test;
import static org.junit.Assert.*;
import bible.obj.Verse;

public class VerseTest {
    @Test
    public void testGetId() {
        int id = 1;
        Verse verse = Verse.New(id);
        int expected = id;
        int actual = verse.getId();
        assertEquals(expected, actual);
    }
}