package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;

import java.util.Arrays;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstruction() {
        String type = "protein";
        String source = "contig123";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date("2022-01-01");
        String comment = "this is a comment";
        String freeFormData = "A:B:C:D";

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
        String type1 = "protein1";
        String source1 = "contig456";
        Range ungappedRange1 = new Range(1, 200);
        Date date1 = new Date("2021-12-31");
        String comment1 = "this is a comment1";
        String freeFormData1 = "A:B:C:D1";

        String type2 = "protein2";
        String source2 = "contig789";
        Range ungappedRange2 = new Range(3, 400);
        Date date2 = new Date("2022-02-01");
        String comment2 = "this is a comment2";
        String freeFormData2 = "A:B:C:D2";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        assertNotEquals(hashCode1, hashCode2);

        // Test hashcode with null values
        DefaultPhdReadTag phdReadTagNullComment = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, null, freeFormData1);
        DefaultPhdReadTag phdReadTagNullDate = new DefaultPhdReadTag(type1, source1, ungappedRange1, null, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTagNullFreeFormData = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, null);
        DefaultPhdReadTag phdReadTagNullSource = new DefaultPhdReadTag(type1, null, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTagNullType = new DefaultPhdReadTag(null, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTagNullUngappedRange = new DefaultPhdReadTag(type1, source1, null, date1, comment1, freeFormData1);

        int hashCodeNullComment = phdReadTagNullComment.hashCode();
        int hashCodeNullDate = phdReadTagNullDate.hashCode();
        int hashCodeNullFreeFormData = phdReadTagNullFreeFormData.hashCode();
        int hashCodeNullSource = phdReadTagNullSource.hashCode();
        int hashCodeNullType = phdReadTagNullType.hashCode();
        int hashCodeNullUngappedRange = phdReadTagNullUngappedRange.hashCode();

        ArrayList<Integer> hashCodeList = new ArrayList<>(Arrays.asList(hashCode1, hashCode2, hashCodeNullComment, hashCodeNullDate, hashCodeNullFreeFormData, hashCodeNullSource, hashCodeNullType, hashCodeNullUngappedRange));

        // Ensure that the hashcode is different for each instance
        for (int i = 0; i < hashCodeList.size() - 1; i++) {
            int currentHashCode = hashCodeList.get(i);
            int nextHashCode = hashCodeList.get(i + 1);
            assertNotEquals(currentHashCode, nextHashCode);
        }
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type1 = "protein1";
        String source1 = "contig456";
        Range ungappedRange1 = new Range(1, 200);
        Date date1 = new Date("2021-12-31");
        String comment1 = "this is a comment1";
        String freeFormData1 = "A:B:C:D1";

        String type2 = "protein2";
        String source2 = "contig789";
        Range ungappedRange2 = new Range(3, 400);
        Date date2 = new Date("2022-02-01");
        String comment2 = "this is a comment2";
        String freeFormData2 = "A:B:C:D2";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        assertNotEquals(phdReadTag1, phdReadTag2);

        // Test equals with null values
        DefaultPhdReadTag phdReadTagNullComment = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, null, freeFormData1);
        DefaultPhdReadTag phdReadTagNullDate = new DefaultPhdReadTag(type1, source1, ungappedRange1, null, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTagNullFreeFormData = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, null);
        DefaultPhdReadTag phdReadTagNullSource = new DefaultPhdReadTag(type1, null, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTagNullType = new DefaultPhdReadTag(null, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTagNullUngappedRange = new DefaultPhdReadTag(type1, source1, null, date1, comment1, freeFormData1);

        assertNotEquals(phdReadTag1, phdReadTagNullComment);
        assertNotEquals(phdReadTag1, phdReadTagNullDate);
        assertNotEquals(phdReadTag1, phdReadTagNullFreeFormData);
        assertNotEquals(phdReadTag1, phdReadTagNullSource);
        assertNotEquals(phdReadTag1, phdReadTagNullType);
        assertNotEquals(phdReadTag1, phdReadTagNullUngappedRange);

        // Test equals with same values
        DefaultPhdReadTag phdReadTag1Copy = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        assertTrue(phdReadTag1.equals(phdReadTag1Copy));
        assertTrue(phdReadTag1Copy.equals(phdReadTag1));
    }
}