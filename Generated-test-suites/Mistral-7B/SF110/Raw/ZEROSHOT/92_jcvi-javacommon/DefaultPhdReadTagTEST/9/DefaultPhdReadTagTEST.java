package org.jcvi.jillion.assembly.consed.phd.test;

import org.junit.Test;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.util.DateUtil;
import org.jcvi.jillion.test.TestUtil;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.assembly.consed.phd.PhdReadTag;

import java.util.Date;

import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstruction() {
        String type = "Protein";
        String source = "Contig1";
        Range ungappedRange = new Range(1, 100);
        Date date = DateUtil.parse("2022-01-01");
        String comment = "This is a test comment";
        String freeFormData = "Test data";

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
        String type = "Protein";
        String source = "Contig1";
        Range ungappedRange = new Range(1, 100);
        Date date = DateUtil.parse("2022-01-01");
        String comment = "This is a test comment";
        String freeFormData = "Test data";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(phdReadTag1.hashCode(), phdReadTag2.hashCode());
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type = "Protein";
        String source = "Contig1";
        Range ungappedRange = new Range(1, 100);
        Date date = DateUtil.parse("2022-01-01");
        String comment = "This is a test comment";
        String freeFormData = "Test data";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertTrue(phdReadTag1.equals(phdReadTag2));

        PhdReadTag phdReadTag3 = new DefaultPhdReadTag("RNA", source, ungappedRange, date, comment, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag3));

        assertFalse(phdReadTag1.equals(null));
    }

    @Test
    public void testDefaultPhdReadTagClone() {
        String type = "Protein";
        String source = "Contig1";
        Range ungappedRange = new Range(1, 100);
        Date date = DateUtil.parse("2022-01-01");
        String comment = "This is a test comment";
        String freeFormData = "Test data";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        DefaultPhdReadTag clonedPhdReadTag = (DefaultPhdReadTag) phdReadTag.clone();

        assertEquals(phdReadTag, clonedPhdReadTag);
    }

    @Test
    public void testDefaultPhdReadTagToString() {
        String type = "Protein";
        String source = "Contig1";
        Range ungappedRange = new Range(1, 100);
        Date date = DateUtil.parse("2022-01-01");
        String comment = "This is a test comment";
        String freeFormData = "Test data";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        String expectedOutput = String.format("DefaultPhdReadTag{type='%s', source='%s', ungappedRange=%s, date=%s, comment='%s', freeFormData='%s'}", type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(expectedOutput, phdReadTag.toString());
    }
}