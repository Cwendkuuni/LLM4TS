import static org.junit.Assert.*;
import bible.obj.Verse;

public class VerseTest {
    @Test
    public void testNew_ValidId() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
        assertEquals(1, verse.getId());
    }

    @Test
    public void testNew_InvalidId() {
        Verse verse = Verse.New(-1);
        assertNull(verse);
    }
}