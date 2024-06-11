package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.junit.Test;
import org.jcvi.jillion.core.*;
import java.util.*;

public class DefaultPhdReadTagTest {
    @Test
    public void testgetType() {
        String type = "DNA";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, null, null, null, null, null);
        String actualType = phdReadTag.getType();
        assert(actualType.equals(type));
    }

    @Test
    public void testGetSource() {
        String source = "Contig123";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, source, null, null, null, null);
        String actualSource = phdReadTag.getSource();
        assert(actualSource.equals(source));
    }

    @Test
    public void testGetUngappedRange() {
        int start = 1;
        int end = 10;
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, new Range(start, end), null, null, null);
        Range actualUngappedRange = phdReadTag.getUngappedRange();
        assert(actualUngappedRange.equals(new Range(start, end)));
    }

    @Test
    public void testGetDate() {
        Date date = new Date("2022-12-31");
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, date, null, null);
        Date actualDate = phdReadTag.getDate();
        assert(actualDate.equals(date));
    }

    @Test
    public void testGetComment() {
        String comment = "This is a test comment";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, null, comment, null);
        String actualComment = phdReadTag.getComment();
        assert(actualComment.equals(comment));
    }

    @Test
    public void testGetFreeFormData() {
        String freeFormData = "Test Data";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, null, null, freeFormData);
        String actualFreeFormData = phdReadTag.getFreeFormData();
        assert(actualFreeFormData.equals(freeFormData));
    }

    @Test(expected = NullPointerException.class)
    public void testNullValuesInConstructor() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, null, null, null);
    }

    @Test
    public void testEmptyStrings() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("", "", null, null, "", "");
    }

    @Test
    public void testEmptyRange() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, null, null, null);
    }

    @Test
    public void testNullDate() {
        Date date = null;
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, date, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullGetters() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, null, null, null);
        String actualType = phdReadTag.getType();
        assert(actualType == null);
    }
}