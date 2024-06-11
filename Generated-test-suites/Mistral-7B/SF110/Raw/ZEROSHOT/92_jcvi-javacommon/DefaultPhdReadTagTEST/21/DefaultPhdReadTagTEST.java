package org.jcvi.jillion.assembly.consed.phd;

import org.junit.Assert;
import org.junit.Test;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.util.DateRange;
import org.jcvi.jillion.core.util.DateUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstruction() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(0, 10);
        Date date = new DateRange(DateUtils.parseLocalDate("2022-01-01"), DateUtils.parseLocalDate("2022-01-02")).getDate();
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(type, phdReadTag.getType());
        Assert.assertEquals(source, phdReadTag.getSource());
        Assert.assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        Assert.assertEquals(date, phdReadTag.getDate());
        Assert.assertEquals(comment, phdReadTag.getComment());
        Assert.assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagHashCodeEquals() {
        String type1 = "type1";
        String source1 = "source1";
        Range ungappedRange1 = new Range(0, 10);
        Date date1 = new DateRange(DateUtils.parseLocalDate("2022-01-01"), DateUtils.parseLocalDate("2022-01-02")).getDate();
        String comment1 = "comment1";
        String freeFormData1 = "freeFormData1";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "type2";
        String source2 = "source2";
        Range ungappedRange2 = new Range(0, 10);
        Date date2 = new DateRange(DateUtils.parseLocalDate("2022-01-03"), DateUtils.parseLocalDate("2022-01-04")).getDate();
        String comment2 = "comment2";
        String freeFormData2 = "freeFormData2";

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();
        int hashCode3 = phdReadTag3.hashCode();

        boolean equals12 = phdReadTag1.equals(phdReadTag2);
        boolean equals13 = phdReadTag1.equals(phdReadTag3);
        boolean equals23 = phdReadTag2.equals(phdReadTag3);

        // Different objects should have different hash codes and not equal
        Assert.assertNotEquals(hashCode1, hashCode2);
        Assert.assertFalse(equals12);

        // Same object should have the same hash code and equal
        Assert.assertEquals(hashCode1, hashCode3);
        Assert.assertTrue(equals13);
        Assert.assertTrue(equals23);

        // Equal objects with different order of fields should still be equal
        List<Object> fields1 = new ArrayList<>();
        fields1.add(source1);
        fields1.add(type1);
        fields1.add(date1);
        fields1.add(comment1);
        fields1.add(ungappedRange1);
        fields1.add(freeFormData1);

        List<Object> fields2 = new ArrayList<>();
        fields2.add(type1);
        fields2.add(comment1);
        fields2.add(date1);
        fields2.add(source1);
        fields2.add(freeFormData1);
        fields2.add(ungappedRange1);

        DefaultPhdReadTag phdReadTag4 = new DefaultPhdReadTag();
        phdReadTag4.setType(type1);
        phdReadTag4.setComment(comment1);
        phdReadTag4.setDate(date1);
        phdReadTag4.setSource(source1);
        phdReadTag4.setFreeFormData(freeFormData1);
        phdReadTag4.setUngappedRange(ungappedRange1);

        int hashCode4 = phdReadTag4.hashCode();
        boolean equals41 = phdReadTag4.equals(phdReadTag1);
        boolean equals42 = phdReadTag4.equals(phdReadTag2);

        // Different objects should have different hash codes and not equal
        Assert.assertNotEquals(hashCode1, hashCode4);
        Assert.assertFalse(equals41);
        Assert.assertFalse(equals42);

        // Setting the fields in the same order as in the constructor should make the objects equal
        phdReadTag4.setUngappedRange(ungappedRange1);
        phdReadTag4.setFreeFormData(freeFormData1);
        phdReadTag4.setSource(source1);
        phdReadTag4.setType(type1);
        phdReadTag4.setComment(comment1);
        phdReadTag4.setDate(date1);

        int hashCode5 = phdReadTag4.hashCode();
        boolean equals51 = phdReadTag4.equals(phdReadTag1);
        boolean equals52 = phdReadTag4.equals(phdReadTag2);

        // Equal objects should have the same hash code and be equal
        Assert.assertEquals(hashCode1, hashCode5);
        Assert.assertTrue(equals51);
        Assert.assertTrue(equals52);
    }
}