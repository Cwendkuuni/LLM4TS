import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.util.SimpleDate;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstruction() {
        String type = "type1";
        String source = "source1";
        Range ungappedRange = new Range(1, 10);
        SimpleDate date = new SimpleDate("2022-01-01");
        String comment = "comment1";
        String freeFormData = "freeFormData1";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals("type1", phdReadTag.getType());
        assertEquals("source1", phdReadTag.getSource());
        assertEquals(new Range(1, 10), phdReadTag.getUngappedRange());
        assertEquals(new SimpleDate("2022-01-01"), phdReadTag.getDate());
        assertEquals("comment1", phdReadTag.getComment());
        assertEquals("freeFormData1", phdReadTag.getFreeFormData());

        // hashCode test
        DefaultPhdReadTag samePhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        int thisHashCode = phdReadTag.hashCode();
        int otherHashCode = samePhdReadTag.hashCode();
        assertEquals(thisHashCode, otherHashCode);
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type1 = "type1";
        String source1 = "source1";
        Range ungappedRange1 = new Range(1, 10);
        SimpleDate date1 = new SimpleDate("2022-01-01");
        String comment1 = "comment1";
        String freeFormData1 = "freeFormData1";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "type2";
        String source2 = "source1";
        Range ungappedRange2 = new Range(1, 11);
        SimpleDate date2 = new SimpleDate("2022-01-01");
        String comment2 = "comment1";
        String freeFormData2 = "freeFormData1";

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        assertFalse(phdReadTag1.equals(phdReadTag2));

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        assertTrue(phdReadTag1.equals(phdReadTag3));
    }

    @Test
    public void testDefaultPhdReadTagHashCodeAndEqualsWithNulls() {
        String type = null;
        String source = null;
        Range ungappedRange = null;
        SimpleDate date = null;
        String comment = null;
        String freeFormData = null;

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        // hashCode
        int thisHashCode = phdReadTag.hashCode();
        DefaultPhdReadTag samePhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        int otherHashCode = samePhdReadTag.hashCode();
        assertEquals(thisHashCode, otherHashCode);

        // equals
        assertEquals(phdReadTag, samePhdReadTag);

        // equals with different objects
        Object obj1 = new Object();
        assertFalse(phdReadTag.equals(obj1));

        Object obj2 = new DefaultPhdReadTag("", "", null, null, null, null);
        assertFalse(phdReadTag.equals(obj2));
    }
}