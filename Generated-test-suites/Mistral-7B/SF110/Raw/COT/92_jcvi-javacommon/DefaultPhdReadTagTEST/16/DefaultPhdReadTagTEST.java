package org.jcvi.jillion.assembly.consed.phd.test;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.*;
import org.jcvi.jillion.assembly.consed.phd.*;
import org.junit.Test;

import java.util.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTag_AllArgumentsProvided_ShouldCreateAnInstance() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2020, 1, 1);
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test(expected = NullPointerException.class)
    public void testDefaultPhdReadTag_NullType_ShouldThrowNPE() {
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2020, 1, 1);
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        new DefaultPhdReadTag(null, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testDefaultPhdReadTag_NullSource_ShouldThrowNPE() {
        String type = "testType";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2020, 1, 1);
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testDefaultPhdReadTag_NullUngappedRange_ShouldThrowNPE() {
        String type = "testType";
        String source = "testSource";
        Date date = new Date(2020, 1, 1);
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testDefaultPhdReadTag_NullComment_ShouldThrowNPE() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2020, 1, 1);
        String freeFormData = "testFreeFormData";

        new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testDefaultPhdReadTag_NullFreeFormData_ShouldThrowNPE() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2020, 1, 1);
        String comment = "testComment";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);
    }

    @Test
    public void testDefaultPhdReadTag_EqualObjects_ShouldReturnTrue() {
        String type1 = "testType";
        String source1 = "testSource";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date(2020, 1, 1);
        String comment1 = "testComment";
        String freeFormData1 = "testFreeFormData";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "testType";
        String source2 = "testSource";
        Range ungappedRange2 = new Range(1, 10);
        Date date2 = new Date(2020, 1, 1);
        String comment2 = "testComment";
        String freeFormData2 = "testFreeFormData";

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        assertTrue(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testDefaultPhdReadTag_NotEqualObjects_ShouldReturnFalse() {
        String type1 = "testType";
        String source1 = "testSource";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date(2020, 1, 1);
        String comment1 = "testComment";
        String freeFormData1 = "testFreeFormData";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "testType2";
        String source2 = "testSource";
        Range ungappedRange2 = new Range(1, 10);
        Date date2 = new Date(2020, 1, 1);
        String comment2 = "testComment";
        String freeFormData2 = "testFreeFormData2";

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        assertFalse(phdReadTag1.equals(phdReadTag2));
    }
}