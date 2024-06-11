
import org.junit.Test;
import org.junit.Assert.*;
import org.jcvi.jillion.core.Range;
import java.util.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstruction() {
        // Test default constructor
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag();
        assertNull(phdReadTag.getType());
        assertNull(phdReadTag.getSource());
        assertNull(phdReadTag.getUngappedRange());
        assertNull(phdReadTag.getDate());
        assertNull(phdReadTag.getComment());
        assertNull(phdReadTag.getFreeFormData());

        // Test parameterized constructor
        String type = "DNA";
        String source = "Chr1:100-200";
        Range ungappedRange = new Range(100, 200);
        Date date = new Date(2022, 1, 1);
        String comment = "Comment1";
        String freeFormData = "Data1";
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(type, phdReadTag1.getType());
        assertEquals(source, phdReadTag1.getSource());
        assertEquals(ungappedRange, phdReadTag1.getUngappedRange());
        assertEquals(date, phdReadTag1.getDate());
        assertEquals(comment, phdReadTag1.getComment());
        assertEquals(freeFormData, phdReadTag1.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagGetters() {
        String type = "DNA";
        String source = "Chr1:100-200";
        Range ungappedRange = new Range(100, 200);
        Date date = new Date(2022, 1, 1);
        String comment = "Comment1";
        String freeFormData = "Data1";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type1 = "DNA1";
        String type2 = "DNA2";
        String source1 = "Chr1:100-200";
        String source2 = "Chr2:100-200";
        Range range1 = new Range(100, 200);
        Range range2 = new Range(100, 200);
        Date date1 = new Date(2022, 1, 1);
        Date date2 = new Date(2022, 1, 2);
        String comment1 = "Comment1";
        String comment2 = "Comment2";
        String freeFormData1 = "Data1";
        String freeFormData2 = "Data2";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, range2, date2, comment2, freeFormData2);

        assertNotEquals(phdReadTag1, phdReadTag2);

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);
        assertEquals(phdReadTag1, phdReadTag3);
    }

    @Test
    public void testDefaultPhdReadTagHashCode() {
        String type1 = "DNA1";
        String type2 = "DNA2";
        String source1 = "Chr1:100-200";
        String source2 = "Chr2:100-200";
        Range range1 = new Range(100, 200);
        Range range2 = new Range(100, 200);
        Date date1 = new Date(2022, 1, 1);
        Date date2 = new Date(2022, 1, 2);
        String comment1 = "Comment1";
        String comment2 = "Comment2";
        String freeFormData1 = "Data1";
        String freeFormData2 = "Data2";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, range2, date2, comment2, freeFormData2);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        assertNotEquals(hashCode1, hashCode2);

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);
        int hashCode3 = phdReadTag3.hashCode();

        assertEquals(hashCode1, hashCode3);
    }

    @Test
    public void testDefaultPhdReadTagDefaultConstructor() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag();
        assertNull(phdReadTag.getType());
        assertNull(phdReadTag.getSource());
        assertNull(phdReadTag.getUngappedRange());
        assertNull(phdReadTag.getDate());
        assertNull(phdReadTag.getComment());
        assertNull(phdReadTag.getFreeFormData());
    }

    @Test(expected = NullPointerException.class)
    public void testDefaultPhdReadTagNullConstructor() {
        new DefaultPhdReadTag(null, null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagEmptySource() {
        String source = "";
        new DefaultPhdReadTag("Type", source, new Range(100, 200), new Date(2022, 1, 1), "Comment", "Data");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullType() {
        String type = null;
        new DefaultPhdReadTag(type, "Source", new Range(100, 200), new Date(2022, 1, 1), "Comment", "Data");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullDate() {
        Date date = null;
        new DefaultPhdReadTag("Type", "Source", new Range(100, 200), date, "Comment", "Data");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullComment() {
        String comment = null;
        new DefaultPhdReadTag("Type", "Source", new Range(100, 200), new Date(2022, 1, 1), comment, "Data");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullFreeFormData() {
        String freeFormData = null;
        new DefaultPhdReadTag("Type", "Source", new Range(100, 200), new Date(2022, 1, 1), "Comment", freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagInvalidRange() {
        Range invalidRange = new Range(-10, 10);
        assertThrows(IllegalArgumentException.class, () -> {
            new DefaultPhdReadTag("Type", "Source", invalidRange, new Date(2022, 1, 1), "Comment", "Data");
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagInvalidDateFormat() {
        Date invalidDate = new Date("InvalidFormat");
        assertThrows(IllegalArgumentException.class, () -> {
            new DefaultPhdReadTag("Type", "Source", new Range(100, 200), invalidDate, "Comment", "Data");
        });
    }
}
