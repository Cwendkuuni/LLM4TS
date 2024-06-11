import org.junit.Test;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;

public class DefaultPhdReadTagTest {
    // 
    
    @Test
    public void testDefaultPhdReadTagConstruction() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertNotNull(phdReadTag);
        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    // 
}