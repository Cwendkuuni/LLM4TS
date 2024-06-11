import static org.junit.Assert.assertEquals;
import bible.obj.Verse;

public class VerseTest {

    @Test
    public void testNewMethod() {
        int id = 1;
        Verse verse = Verse.New(id);
        int expectedId = id;
        assertEquals(expectedId, verse.getId());
    }
}