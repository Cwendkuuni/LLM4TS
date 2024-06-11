import org.junit.Test;
import org.jcvi.jillion.core.Range;
import org.junit.Assert.*;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import java.util.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        String type = "protein";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, null, null, null, null, null);
        String returnedType = tag.getType();
        assertEquals(type, returnedType);
    }

    @Test
    public void testGetUngappedRange() {
        int start = 10;
        int end = 20;
        DefaultPhdReadTag tag = new DefaultPhdReadTag(null, null, new Range(start, end), null, null, null);
        Range returnedRange = tag.getUngappedRange();
        assertEquals(start, returnedRange.getStart());
        assertEquals(end, returnedRange.getEnd());
    }

    @Test
    public void testHashCodeAndEquals() {
        String type1 = "protein";
        String type2 = "DNA";
        String source1 = "contig1";
        String source2 = "contig2";
        Date date1 = new Date(1576800000000L);
        Date date2 = new Date(1576800000001L);
        String comment1 = "comment1";
        String comment2 = "comment1";
        String freeFormData1 = "data1";
        String freeFormData2 = "data2";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type1, source1, null, date1, comment1, freeFormData1);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type2, source2, null, date2, comment2, freeFormData2);
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag(type1, source1, null, date1, comment1, freeFormData1);

        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag2.hashCode();
        int hashCode3 = tag3.hashCode();

        assertNotEquals(hashCode1, hashCode2);
        assertEquals(hashCode1, hashCode3);

        assertTrue(tag1.equals(tag3));
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testOtherMethods() {
        // Add test cases for other methods here
    }

    @Test
    public void testErrorScenarios() {
        // Add test cases for error scenarios here
    }

    @Test
    public void testEdgeCases() {
        // Add test cases for edge cases here
    }
}