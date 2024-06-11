package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstruction() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "comment";
        String freeFormData = "freeFormData";

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
        String type1 = "type1";
        String source1 = "source1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date(2022, 1, 1);
        String comment1 = "comment1";
        String freeFormData1 = "freeFormData1";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "type2";
        String source2 = "source2";
        Range ungappedRange2 = new Range(2, 20);
        Date date2 = new Date(2023, 2, 2);
        String comment2 = "comment2";
        String freeFormData2 = "freeFormData2";

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        assertNotSame(hashCode1, hashCode2);

        // test when all fields are equal
        String type3 = type1;
        String source3 = source1;
        Range ungappedRange3 = ungappedRange1;
        Date date3 = date1;
        String comment3 = comment1;
        String freeFormData3 = freeFormData1;

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type3, source3, ungappedRange3, date3, comment3, freeFormData3);

        int hashCode3 = phdReadTag3.hashCode();
        assertEquals(hashCode1, hashCode3);

        // test when some fields are null
        DefaultPhdReadTag phdReadTagNullComment = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, null, freeFormData1);
        DefaultPhdReadTag phdReadTagNullFreeFormData = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, null);
        DefaultPhdReadTag phdReadTagNullAll = new DefaultPhdReadTag(type1, source1, null, date1, comment1, freeFormData1);

        int hashCodeNullComment = phdReadTagNullComment.hashCode();
        int hashCodeNullFreeFormData = phdReadTagNullFreeFormData.hashCode();
        int hashCodeNullAll = phdReadTagNullAll.hashCode();

        List<Object> list = new ArrayList<>();
        list.add(phdReadTag1);
        list.add(phdReadTag2);
        list.add(phdReadTagNullComment);
        list.add(phdReadTagNullFreeFormData);
        list.add(phdReadTagNullAll);

        Collections.sort(list); // this sorts based on hashcode

        DefaultPhdReadTag phdReadTagSorted = (DefaultPhdReadTag)list.get(0);
        assertEquals(phdReadTag1.hashCode(), phdReadTagSorted.hashCode());
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type1 = "type1";
        String source1 = "source1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date(2022, 1, 1);
        String comment1 = "comment1";
        String freeFormData1 = "freeFormData1";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "type2";
        String source2 = "source1";
        Range ungappedRange2 = new Range(1, 10);
        Date date2 = new Date(2022, 1, 1);
        String comment2 = "comment1";
        String freeFormData2 = "freeFormData1";

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        assertTrue(phdReadTag1.equals(phdReadTag3));
        assertFalse(phdReadTag1.equals(phdReadTag2));

        DefaultPhdReadTag phdReadTag4 = new DefaultPhdReadTag(type1, source1, null, date1, comment1, freeFormData1);

        assertFalse(phdReadTag1.equals(phdReadTag4));

        DefaultPhdReadTag phdReadTag5 = new DefaultPhdReadTag(type1, null, ungappedRange1, date1, comment1, freeFormData1);

        assertFalse(phdReadTag1.equals(phdReadTag5));

        DefaultPhdReadTag phdReadTag6 = new DefaultPhdReadTag(type1, source1, ungappedRange1, null, comment1, freeFormData1);

        assertFalse(phdReadTag1.equals(phdReadTag6));

        DefaultPhdReadTag phdReadTag7 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, null, freeFormData1);

        assertFalse(phdReadTag1.equals(phdReadTag7));

        DefaultPhdReadTag phdReadTag8 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, null);

        assertFalse(phdReadTag1.equals(phdReadTag8));
    }
}