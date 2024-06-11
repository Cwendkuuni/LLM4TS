package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.*;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.util.DateUtil;

import java.util.*;

public class DefaultPhdReadTagTest {
    @Test
    public void testDefaultPhdReadTag() {
        String type = "protein";
        String source = "NC_001234.1:c.100-200";
        Range ungappedRange = new Range(100, 201);
        Date date = DateUtil.parseDate("2022-01-01");
        String comment = "comment1";
        String freeFormData = "free-form-data1";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());

        assertEquals(phdReadTag.hashCode(), phdReadTag.hashCode());
        assertTrue(phdReadTag.equals(phdReadTag));

        String differentType = "DNA";
        DefaultPhdReadTag differentPhdReadTag = new DefaultPhdReadTag(differentType, source, ungappedRange, date, comment, freeFormData);

        assertNotEquals(phdReadTag.hashCode(), differentPhdReadTag.hashCode());
        assertFalse(phdReadTag.equals(differentPhdReadTag));

        String differentSource = "NC_001234.1:c.100-200:r.-50";
        DefaultPhdReadTag differentSourcePhdReadTag = new DefaultPhdReadTag(type, differentSource, ungappedRange, date, comment, freeFormData);

        assertNotEquals(phdReadTag.hashCode(), differentSourcePhdReadTag.hashCode());
        assertFalse(phdReadTag.equals(differentSourcePhdReadTag));

        Range differentUngappedRange = new Range(150, 251);
        DefaultPhdReadTag differentUngappedRangePhdReadTag = new DefaultPhdReadTag(type, source, differentUngappedRange, date, comment, freeFormData);

        assertNotEquals(phdReadTag.hashCode(), differentUngappedRangePhdReadTag.hashCode());
        assertFalse(phdReadTag.equals(differentUngappedRangePhdReadTag));

        Date differentDate = DateUtil.parseDate("2022-01-02");
        DefaultPhdReadTag differentDatePhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, differentDate, comment, freeFormData);

        assertNotEquals(phdReadTag.hashCode(), differentDatePhdReadTag.hashCode());
        assertFalse(phdReadTag.equals(differentDatePhdReadTag));

        String differentComment = "comment2";
        DefaultPhdReadTag differentCommentPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, differentComment, freeFormData);

        assertNotEquals(phdReadTag.hashCode(), differentCommentPhdReadTag.hashCode());
        assertFalse(phdReadTag.equals(differentCommentPhdReadTag));

        String differentFreeFormData = "free-form-data2";
        DefaultPhdReadTag differentFreeFormDataPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, differentFreeFormData);

        assertNotEquals(phdReadTag.hashCode(), differentFreeFormDataPhdReadTag.hashCode());
        assertFalse(phdReadTag.equals(differentFreeFormDataPhdReadTag));
    }

    @Test
    public void testGetters() {
        String type = "protein";
        String source = "NC_001234.1:c.100-200";
        Range ungappedRange = new Range(100, 201);
        Date date = DateUtil.parseDate("2022-01-01");
        String comment = "comment1";
        String freeFormData = "free-form-data1";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }
}