import org.junit.Test;
import org.junit.Assert.*;
import org.jcvi.jillion.core.*;
import java.util.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstructor() {
        String type = "protein";
        String source = "CDS";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date("2022-01-01");
        String comment = "comment1";
        String freeFormData = "data1";

        DefaultPhdReadTag phdTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdTag.getType());
        assertEquals(source, phdTag.getSource());
        assertEquals(ungappedRange, phdTag.getUngappedRange());
        assertEquals(date, phdTag.getDate());
        assertEquals(comment, phdTag.getComment());
        assertEquals(freeFormData, phdTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagGetters() {
        String type = "protein";
        String source = "CDS";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date("2022-01-01");
        String comment = "comment1";
        String freeFormData = "data1";

        DefaultPhdReadTag phdTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdTag.getType());
        assertEquals(source, phdTag.getSource());
        assertEquals(ungappedRange, phdTag.getUngappedRange());
        assertEquals(date, phdTag.getDate());
        assertEquals(comment, phdTag.getComment());
        assertEquals(freeFormData, phdTag.getFreeFormData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullType() {
        String source = "CDS";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date("2022-01-01");
        String comment = "comment1";
        String freeFormData = "data1";

        new DefaultPhdReadTag(null, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullSource() {
        String type = "protein";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date("2022-01-01");
        String comment = "comment1";
        String freeFormData = "data1";

        new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullUngappedRange() {
        String type = "protein";
        String source = "CDS";
        Date date = new Date("2022-01-01");
        String comment = "comment1";
        String freeFormData = "data1";

        new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullDate() {
        String type = "protein";
        String source = "CDS";
        Range ungappedRange = new Range(1, 100);
        String comment = "comment1";
        String freeFormData = "data1";

        new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);
    }

    @Test
    public void testDefaultPhdReadTagHashCode() {
        String type1 = "protein1";
        String source1 = "CDS1";
        Range ungappedRange1 = new Range(1, 100);
        Date date1 = new Date("2022-01-01");
        String comment1 = "comment1";
        String freeFormData1 = "data1";

        String type2 = "protein2";
        String source2 = "CDS2";
        Range ungappedRange2 = new Range(1, 100);
        Date date2 = new Date("2022-01-02");
        String comment2 = "comment2";
        String freeFormData2 = "data2";

        DefaultPhdReadTag phdTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        int hashCode1 = phdTag1.hashCode();
        int hashCode2 = phdTag2.hashCode();

        assertNotSame(hashCode1, hashCode2);
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type1 = "protein1";
        String source1 = "CDS1";
        Range ungappedRange1 = new Range(1, 100);
        Date date1 = new Date("2022-01-01");
        String comment1 = "comment1";
        String freeFormData1 = "data1";

        String type2 = "protein2";
        String source2 = "CDS2";
        Range ungappedRange2 = new Range(1, 100);
        Date date2 = new Date("2022-01-01");
        String comment2 = "comment1";
        String freeFormData2 = "data1";

        DefaultPhdReadTag phdTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        DefaultPhdReadTag sameTag = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        assertTrue(phdTag1.equals(phdTag1));
        assertTrue(phdTag1.equals(sameTag));
        assertFalse(phdTag1.equals(phdTag2));
    }

    @Test
    public void testDefaultPhdReadTagNullObject() {
        String type = "protein";
        String source = "CDS";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date("2022-01-01");
        String comment = "comment1";
        String freeFormData = "data1";

        DefaultPhdReadTag phdTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertFalse(phdTag.equals(null));
    }

    @Test
    public void testDefaultPhdReadTagObjectNotInstance() {
        String type = "protein";
        String source = "CDS";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date("2022-01-01");
        String comment = "comment1";
        String freeFormData = "data1";

        DefaultPhdReadTag phdTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertFalse(phdTag.equals(new Object()));
    }

    @Test
    public void testDefaultPhdReadTagEmptyComment() {
        String type = "protein";
        String source = "CDS";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date("2022-01-01");
        String comment = "";
        String freeFormData = "data1";

        DefaultPhdReadTag phdTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals("", phdTag.getComment());
    }

    @Test
    public void testDefaultPhdReadTagNullComment() {
        String type = "protein";
        String source = "CDS";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date("2022-01-01");
        String comment = null;
        String freeFormData = "data1";

        DefaultPhdReadTag phdTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertNull(phdTag.getComment());
    }

    @Test
    public void testDefaultPhdReadTagEmptyFreeFormData() {
        String type = "protein";
        String source = "CDS";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date("2022-01-01");
        String comment = "comment1";
        String freeFormData = "";

        DefaultPhdReadTag phdTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals("", phdTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagNullFreeFormData() {
        String type = "protein";
        String source = "CDS";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date("2022-01-01");
        String comment = "comment1";
        String freeFormData = null;

        DefaultPhdReadTag phdTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertNull(phdTag.getFreeFormData());
    }
}