import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstructor() {
        String type = "DNA";
        String source = "Contig1:100-200";
        Range ungappedRange = new Range(100, 200);
        Date date = new Date(2022, 11, 1);
        String comment = "Initial read";
        String freeFormData = "ABCDEFG";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testGettersHashCode() {
        String type = "DNA";
        String source = "Contig1:100-200";
        Range ungappedRange = new Range(100, 200);
        Date date = new Date(2022, 11, 1);
        String comment = "Initial read";
        String freeFormData = "ABCDEFG";
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(phdReadTag1, phdReadTag2);
        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag("RNA", source, ungappedRange, date, comment, freeFormData);
        assertNotEquals(phdReadTag1, phdReadTag3);
        DefaultPhdReadTag phdReadTag4 = new DefaultPhdReadTag(type, "Contig1:90-190", ungappedRange, date, comment, freeFormData);
        assertNotEquals(phdReadTag1, phdReadTag4);
        DefaultPhdReadTag phdReadTag5 = new DefaultPhdReadTag(type, source, ungappedRange, new Date(2023, 11, 1), comment, freeFormData);
        assertNotEquals(phdReadTag1, phdReadTag5);
        DefaultPhdReadTag phdReadTag6 = new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
        assertNotEquals(phdReadTag1, phdReadTag6);
        DefaultPhdReadTag phdReadTag7 = new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);
        assertNotEquals(phdReadTag1, phdReadTag7);
        DefaultPhdReadTag phdReadTag8 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);
        assertNotEquals(phdReadTag1, phdReadTag8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagConstructorNullType() {
        String type = null;
        String source = "Contig1:100-200";
        Range ungappedRange = new Range(100, 200);
        Date date = new Date(2022, 11, 1);
        String comment = "Initial read";
        String freeFormData = "ABCDEFG";
        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagConstructorNullSource() {
        String type = "DNA";
        String source = null;
        Range ungappedRange = new Range(100, 200);
        Date date = new Date(2022, 11, 1);
        String comment = "Initial read";
        String freeFormData = "ABCDEFG";
        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagConstructorNullUngappedRange() {
        String type = "DNA";
        String source = "Contig1:100-200";
        Range ungappedRange = null;
        Date date = new Date(2022, 11, 1);
        String comment = "Initial read";
        String freeFormData = "ABCDEFG";
        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagConstructorNullDate() {
        String type = "DNA";
        String source = "Contig1:100-200";
        Range ungappedRange = new Range(100, 200);
        Date date = null;
        String comment = "Initial read";
        String freeFormData = "ABCDEFG";
        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagConstructorNullComment() {
        String type = "DNA";
        String source = "Contig1:100-200";
        Range ungappedRange = new Range(100, 200);
        Date date = new Date(2022, 11, 1);
        String comment = null;
        String freeFormData = "ABCDEFG";
        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagConstructorNullFreeFormData() {
        String type = "DNA";
        String source = "Contig1:100-200";
        Range ungappedRange = new Range(100, 200);
        Date date = new Date(2022, 11, 1);
        String comment = "Initial read";
        String freeFormData = null;
        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }
}