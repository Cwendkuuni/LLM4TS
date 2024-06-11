package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;
import org.jcvi.jillion.core.*;
import org.jcvi.jillion.core.range.*;
import org.jcvi.jillion.core.util.*;
import java.time.*;
import java.util.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstruction() {
        String type = "protein";
        String source = "contig123";
        Range ungappedRange = new Range(10, 20);
        Date date = Date.parse("2022-01-01T12:00:00Z");
        String comment = "this is a comment";
        String freeFormData = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

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
        String type = "protein";
        String source = "contig123";
        Range ungappedRange = new Range(10, 20);
        Date date = Date.parse("2022-01-01T12:00:00Z");
        String comment = "this is a comment";
        String freeFormData = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        int phdReadTag1HashCode = phdReadTag1.hashCode();
        int phdReadTag2HashCode = phdReadTag2.hashCode();

        assertEquals(phdReadTag1HashCode, phdReadTag2HashCode);
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type1 = "protein";
        String source1 = "contig123";
        Range ungappedRange1 = new Range(10, 20);
        Date date1 = Date.parse("2022-01-01T12:00:00Z");
        String comment1 = "this is a comment";
        String freeFormData1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "protein";
        String source2 = "contig123";
        Range ungappedRange2 = new Range(10, 20);
        Date date2 = Date.parse("2022-01-01T12:00:00Z");
        String comment2 = "this is a comment";
        String freeFormData2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        assertTrue(phdReadTag1.equals(phdReadTag2));

        String type3 = "DNA";
        String source3 = "contig456";
        Range ungappedRange3 = new Range(30, 40);
        Date date3 = Date.parse("2022-01-02T13:00:00Z");
        String comment3 = "another comment";
        String freeFormData3 = "1234567890";

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type3, source3, ungappedRange3, date3, comment3, freeFormData3);

        assertFalse(phdReadTag1.equals(phdReadTag3));
    }

    @Test
    public void testDefaultPhdReadTagEqualsNull() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("protein", "contig123", null, null, null, null);

        assertFalse(phdReadTag.equals(null));
    }

    @Test
    public void testDefaultPhdReadTagEqualsDifferentTypes() {
        String proteinType = "protein";
        String proteinSource = "contig123";
        Range proteinUngappedRange = new Range(10, 20);
        Date proteinDate = Date.parse("2022-01-01T12:00:00Z");
        String proteinComment = "this is a comment";
        String proteinFreeFormData = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        DefaultPhdReadTag proteinPhdReadTag = new DefaultPhdReadTag(proteinType, proteinSource, proteinUngappedRange, proteinDate, proteinComment, proteinFreeFormData);

        String dnaType = "DNA";
        String dnaSource = "contig456";
        Range dnaUngappedRange = new Range(30, 40);
        Date dnaDate = Date.parse("2022-01-02T13:00:00Z");
        String dnaComment = "another comment";
        String dnaFreeFormData = "1234567890";

        DefaultPhdReadTag dnaPhdReadTag = new DefaultPhdReadTag(dnaType, dnaSource, dnaUngappedRange, dnaDate, dnaComment, dnaFreeFormData);

        assertFalse(proteinPhdReadTag.equals(dnaPhdReadTag));
    }

    @Test
    public void testDefaultPhdReadTagEqualsSameInstances() {
        String type = "protein";
        String source = "contig123";
        Range ungappedRange = new Range(10, 20);
        Date date = Date.parse("2022-01-01T12:00:00Z");
        String comment = "this is a comment";
        String freeFormData = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        DefaultPhdReadTag phdReadTag2 = phdReadTag1;

        assertTrue(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testDefaultPhdReadTagEqualsWithSameValuesButDifferentInstances() {
        String type = "protein";
        String source = "contig123";
        Range ungappedRange = new Range(10, 20);
        Date date = Date.parse("2022-01-01T12:00:00Z");
        String comment = "this is a comment";
        String freeFormData = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertTrue(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testDefaultPhdReadTagEqualsWithNullSource() {
        String type = "protein";
        String source = null;
        Range ungappedRange = new Range(10, 20);
        Date date = Date.parse("2022-01-01T12:00:00Z");
        String comment = "this is a comment";
        String freeFormData = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testDefaultPhdReadTagEqualsWithNullType() {
        String type = null;
        String source = "contig123";
        Range ungappedRange = new Range(10, 20);
        Date date = Date.parse("2022-01-01T12:00:00Z");
        String comment = "this is a comment";
        String freeFormData = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("", source, ungappedRange, date, comment, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testDefaultPhdReadTagEqualsWithNullComment() {
        String type = "protein";
        String source = "contig123";
        Range ungappedRange = new Range(10, 20);
        Date date = Date.parse("2022-01-01T12:00:00Z");
        String comment = null;
        String freeFormData = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testDefaultPhdReadTagEqualsWithNullFreeFormData() {
        String type = "protein";
        String source = "contig123";
        Range ungappedRange = new Range(10, 20);
        Date date = Date.parse("2022-01-01T12:00:00Z");
        String comment = "this is a comment";
        String freeFormData = null;

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);

        assertFalse(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testDefaultPhdReadTagEqualsWithDifferentUngappedRanges() {
        String type = "protein";
        String source = "contig123";
        Range ungappedRange1 = new Range(10, 20);
        Date date = Date.parse("2022-01-01T12:00:00Z");
        String comment = "this is a comment";
        String freeFormData = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange1, date, comment, freeFormData);

        Range ungappedRange2 = new Range(30, 40);

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange2, date, comment, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testDefaultPhdReadTagEqualsWithDifferentDates() {
        String type = "protein";
        String source = "contig123";
        Range ungappedRange = new Range(10, 20);
        Date date1 = Date.parse("2022-01-01T12:00:00Z");
        String comment = "this is a comment";
        String freeFormData = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date1, comment, freeFormData);

        Date date2 = Date.parse("2022-01-02T13:00:00Z");

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date2, comment, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag2));
    }
}