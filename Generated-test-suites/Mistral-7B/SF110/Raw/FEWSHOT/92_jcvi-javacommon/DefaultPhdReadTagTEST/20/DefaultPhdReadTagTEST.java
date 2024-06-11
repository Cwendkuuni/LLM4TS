package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstruction() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, tag.getType());
        assertEquals(source, tag.getSource());
        assertEquals(ungappedRange, tag.getUngappedRange());
        assertEquals(date, tag.getDate());
        assertEquals(comment, tag.getComment());
        assertEquals(freeFormData, tag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagHashCode() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type1 = "type1";
        String type2 = "type2";
        String source1 = "source1";
        String source2 = "source2";
        Range ungappedRange1 = new Range(1, 10);
        Range ungappedRange2 = new Range(1, 10);
        Date date1 = new Date("2022-01-01");
        Date date2 = new Date("2022-01-01");
        String comment1 = "comment1";
        String comment2 = "comment2";
        String freeFormData1 = "freeFormData1";
        String freeFormData2 = "freeFormData2";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        DefaultPhdReadTag tag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        DefaultPhdReadTag tag4 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, null);

        assertTrue(tag1.equals(tag3));
        assertFalse(tag1.equals(tag2));
        assertFalse(tag1.equals(null));
        assertFalse(tag1.equals(""));
    }

    @Test
    public void testDefaultPhdReadTagEmptyFields() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag(null, null, null, null, null, null);

        assertEquals(null, tag.getType());
        assertEquals(null, tag.getSource());
        assertNull(tag.getUngappedRange());
        assertNull(tag.getDate());
        assertEquals(null, tag.getComment());
        assertEquals(null, tag.getFreeFormData());

        assertEquals(0, tag.hashCode());
    }

    @Test
    public void testDefaultPhdReadTagHashCodeWithNullFields() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag(null, null, null, null, null, null);

        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(null, null, null, null, null, null);

        int hashCode1 = tag.hashCode();
        int hashCode2 = tag2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testDefaultPhdReadTagEqualsWithNullFields() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(null, null, null, null, null, null);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(null, null, null, null, null, null);

        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testDefaultPhdReadTagEqualsWithDifferentObjects() {
        String type1 = "type1";
        String source1 = "source1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date("2022-01-01");
        String comment1 = "comment1";
        String freeFormData1 = "freeFormData1";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "type2";
        String source2 = "source2";
        Range ungappedRange2 = new Range(1, 11);
        Date date2 = new Date("2022-01-02");
        String comment2 = "comment2";
        String freeFormData2 = "freeFormData2";

        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testDefaultPhdReadTagEqualsWithSameObjectsButDifferentOrders() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "comment";
        String freeFormData = "freeFormData";

        List<Object> fields1 = new ArrayList<>();
        fields1.add(type);
        fields1.add(source);
        fields1.add(ungappedRange);
        fields1.add(date);
        fields1.add(comment);
        fields1.add(freeFormData);

        List<Object> fields2 = new ArrayList<>();
        fields2.add(source);
        fields2.add(type);
        fields2.add(date);
        fields2.add(comment);
        fields2.add(freeFormData);
        fields2.add(ungappedRange);

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(fields1.toArray());
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(fields2.toArray());

        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testDefaultPhdReadTagEqualsWithSameObjectsButNullFields() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "comment";
        String freeFormData = "freeFormData";

        List<Object> fields1 = new ArrayList<>();
        fields1.add(type);
        fields1.add(source);
        fields1.add(ungappedRange);
        fields1.add(date);
        fields1.add(comment);
        fields1.add(freeFormData);

        List<Object> fields2 = new ArrayList<>();
        fields2.add(type);
        fields2.add(source);
        fields2.add(date);
        fields2.add(comment);
        fields2.add(null);
        fields2.add(freeFormData);

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(fields1.toArray());
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(fields2.toArray());

        assertFalse(tag1.equals(tag2));
    }
}