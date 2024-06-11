package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;

import java.util.ArrayList;
import java.util.List;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagCreation() {
        String type = "DNA";
        String source = "Contig_1";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date("2022-01-01");
        String comment = "Sequencing run 1";
        String freeFormData = "Phred quality scores";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagHashCode() {
        String type = "DNA";
        String source = "Contig_1";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date("2022-01-01");
        String comment = "Sequencing run 1";
        String freeFormData = "Phred quality scores";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        String type2 = "RNA";
        String source2 = "Contig_2";
        Range ungappedRange2 = new Range(200, 300);
        Date date2 = new Date("2022-03-01");
        String comment2 = "Sequencing run 2";
        String freeFormData2 = "Adaptor sequences";

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        // Different objects should have different hash codes
        assertNotSame(hashCode1, hashCode2);

        // Same object should have the same hash code
        DefaultPhdReadTag phdReadTag3 = phdReadTag1;
        int hashCode3 = phdReadTag3.hashCode();
        assertEquals(hashCode1, hashCode3);
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type = "DNA";
        String source = "Contig_1";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date("2022-01-01");
        String comment = "Sequencing run 1";
        String freeFormData = "Phred quality scores";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        String type2 = "DNA";
        String source2 = "Contig_1";
        Range ungappedRange2 = new Range(1, 100);
        Date date2 = new Date("2022-01-01");
        String comment2 = "Sequencing run 1";
        String freeFormData2 = "Phred quality scores";

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        // Same objects should be equal
        assertTrue(phdReadTag1.equals(phdReadTag3));
        assertTrue(phdReadTag3.equals(phdReadTag1));

        // Different objects should not be equal
        assertFalse(phdReadTag1.equals(phdReadTag2));
        assertFalse(phdReadTag2.equals(phdReadTag1));
    }

    @Test
    public void testDefaultPhdReadTagEmptyValues() {
        String type = null;
        String source = null;
        Range ungappedRange = null;
        Date date = null;
        String comment = null;
        String freeFormData = null;

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertNull(phdReadTag.getType());
        assertNull(phdReadTag.getSource());
        assertNull(phdReadTag.getUngappedRange());
        assertNull(phdReadTag.getDate());
        assertNull(phdReadTag.getComment());
        assertNull(phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagEmptyValuesHashCode() {
        String type = null;
        String source = null;
        Range ungappedRange = null;
        Date date = null;
        String comment = null;
        String freeFormData = null;

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        int hashCode = phdReadTag.hashCode();

        // All null values should result in the same hash code
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(null, null, null, null, null, null);
        int hashCode2 = phdReadTag2.hashCode();

        assertEquals(hashCode, hashCode2);
    }

    @Test
    public void testDefaultPhdReadTagEmptyValuesEquals() {
        String type = null;
        String source = null;
        Range ungappedRange = null;
        Date date = null;
        String comment = null;
        String freeFormData = null;

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(null, null, null, null, null, null);

        // Null objects should be considered equal
        assertTrue(phdReadTag1.equals(phdReadTag2));
        assertTrue(phdReadTag2.equals(phdReadTag1));
    }

    @Test
    public void testDefaultPhdReadTagList() {
        List<DefaultPhdReadTag> phdReadTags = new ArrayList<>();

        String type1 = "DNA";
        String source1 = "Contig_1";
        Range ungappedRange1 = new Range(1, 100);
        Date date1 = new Date("2022-01-01");
        String comment1 = "Sequencing run 1";
        String freeFormData1 = "Phred quality scores";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        phdReadTags.add(phdReadTag1);

        String type2 = "RNA";
        String source2 = "Contig_2";
        Range ungappedRange2 = new Range(200, 300);
        Date date2 = new Date("2022-03-01");
        String comment2 = "Sequencing run 2";
        String freeFormData2 = "Adaptor sequences";

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);
        phdReadTags.add(phdReadTag2);

        assertEquals(2, phdReadTags.size());

        for (DefaultPhdReadTag tag : phdReadTags) {
            assertNotNull(tag);
        }
    }
}