import org.junit.Test;
import org.junit.Assert.*;
import org.jcvi.jillion.core.*;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.assembly.consed.phd.PhdReadTag;
import java.util.*;
import java.util.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstructor() {
        String type = "protein";
        String source = "contig1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "comment1";
        String freeFormData = "data1";

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, defaultPhdReadTag.getType());
        assertEquals(source, defaultPhdReadTag.getSource());
        assertEquals(ungappedRange, defaultPhdReadTag.getUngappedRange());
        assertEquals(date, defaultPhdReadTag.getDate());
        assertEquals(comment, defaultPhdReadTag.getComment());
        assertEquals(freeFormData, defaultPhdReadTag.getFreeFormData());
    }

    @Test
    public void testGetters() {
        String type = "protein";
        String source = "contig1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "comment1";
        String freeFormData = "data1";

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, defaultPhdReadTag.getType());
        assertEquals(source, defaultPhdReadTag.getSource());
        assertEquals(ungappedRange, defaultPhdReadTag.getUngappedRange());
        assertEquals(date, defaultPhdReadTag.getDate());
        assertEquals(comment, defaultPhdReadTag.getComment());
        assertEquals(freeFormData, defaultPhdReadTag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        String type1 = "protein";
        String source1 = "contig1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date();
        String comment1 = "comment1";
        String freeFormData1 = "data1";

        String type2 = "mRNA";
        String source2 = "contig2";
        Range ungappedRange2 = new Range(2, 11);
        Date date2 = new Date();
        String comment2 = "comment2";
        String freeFormData2 = "data2";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        assertNotEquals(hashCode1, hashCode2);

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        assertEquals(hashCode1, phdReadTag3.hashCode());
    }

    @Test
    public void testEquals() {
        String type1 = "protein";
        String source1 = "contig1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date();
        String comment1 = "comment1";
        String freeFormData1 = "data1";

        String type2 = "mRNA";
        String source2 = "contig2";
        Range ungappedRange2 = new Range(2, 11);
        Date date2 = new Date();
        String comment2 = "comment1";
        String freeFormData2 = "data1";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment1, freeFormData1);

        assertFalse(phdReadTag1.equals(phdReadTag2));

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        assertTrue(phdReadTag1.equals(phdReadTag3));
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructor() {
        new DefaultPhdReadTag(null, null, null, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullType() {
        String source = "contig1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "comment1";
        String freeFormData = "data1";

        new DefaultPhdReadTag(null, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullSource() {
        String type = "protein";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "comment1";
        String freeFormData = "data1";

        new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullUngappedRange() {
        String type = "protein";
        String source = "contig1";
        Date date = new Date();
        String comment = "comment1";
        String freeFormData = "data1";

        new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullDate() {
        String type = "protein";
        String source = "contig1";
        Range ungappedRange = new Range(1, 10);
        String comment = "comment1";
        String freeFormData = "data1";

        new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullComment() {
        String type = "protein";
        String source = "contig1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String freeFormData = "data1";

        new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullFreeFormData() {
        String type = "protein";
        String source = "contig1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "comment1";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);
    }
}