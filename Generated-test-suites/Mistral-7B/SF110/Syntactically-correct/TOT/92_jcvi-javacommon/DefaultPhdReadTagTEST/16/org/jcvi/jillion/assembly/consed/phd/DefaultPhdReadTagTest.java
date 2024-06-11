
package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;
import org.jcvi.jillion.core.*;
import java.util.*;
import org.junit.*;

public class DefaultPhdReadTagTest {

    @Before
    public void setUp() {
    }

    @Test
    public void testDefaultPhdReadTagConstruction() {
        String type = "quality";
        String source = "Sanger";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "this is a comment";
        String freeFormData = "JUnit test data";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, tag.getType());
        assertEquals(source, tag.getSource());
        assertEquals(ungappedRange, tag.getUngappedRange());
        assertEquals(date, tag.getDate());
        assertEquals(comment, tag.getComment());
        assertEquals(freeFormData, tag.getFreeFormData());
    }

    @Test
    public void testGetType() {
        String type = "quality";
        String source = "Sanger";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "this is a comment";
        String freeFormData = "JUnit test data";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, tag.getType());
        DefaultPhdReadTag tagWithNullType = new DefaultPhdReadTag("", source, ungappedRange, date, comment, freeFormData);
        assertEquals(null, tagWithNullType.getType());
    }

    @Test
    public void testEquals() {
        String type1 = "quality";
        String source1 = "Sanger";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date("2022-01-01");
        String comment1 = "this is a comment";
        String freeFormData1 = "JUnit test data";

        String type2 = "coverage";
        String source2 = "Illumina";
        Range ungappedRange2 = new Range(5, 20);
        Date date2 = new Date("2022-01-02");
        String comment2 = "this is another comment";
        String freeFormData2 = "JUnit test data 2";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        assertTrue(tag1.equals(tag3));
        assertFalse(tag1.equals(tag2));

        tag1.setComment("this is a different comment");
        assertFalse(tag1.equals(tag3));
    }

    @Test
    public void testHashCode() {
        String type = "quality";
        String source = "Sanger";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "this is a comment";
        String freeFormData = "JUnit test data";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test(expected = AssertionError.class)
    public void testNullObject() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag(null, "Sanger", new Range(1, 10), new Date("2022-01-01"), "this is a comment", "JUnit test data");
        fail("Expected AssertionError");
    }

    @Test(expected = AssertionError.class)
    public void testNullSource() {
        String type = "quality";
        String source = null;
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "this is a comment";
        String freeFormData = "JUnit test data";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        fail("Expected AssertionError");
    }

    @Test(expected = AssertionError.class)
    public void testNullType() {
        String type = null;
        String source = "Sanger";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "this is a comment";
        String freeFormData = "JUnit test data";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        fail("Expected AssertionError");
    }

    @Test(expected = AssertionError.class)
    public void testNullUngappedRange() {
        String type = "quality";
        String source = "Sanger";
        Range ungappedRange = null;
        Date date = new Date("2022-01-01");
        String comment = "this is a comment";
        String freeFormData = "JUnit test data";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        fail("Expected AssertionError");
    }

    @Test(expected = AssertionError.class)
    public void testNullDate() {
        String type = "quality";
        String source = "Sanger";
        Range ungappedRange = new Range(1, 10);
        Date date = null;
        String comment = "this is a comment";
        String freeFormData = "JUnit test data";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        fail("Expected AssertionError");
    }

    @Test(expected = AssertionError.class)
    public void testNullComment() {
        String type = "quality";
        String source = "Sanger";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = null;
        String freeFormData = "JUnit test data";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        fail("Expected AssertionError");
    }

    @Test(expected = AssertionError.class)
    public void testNullFreeFormData() {
        String type = "quality";
        String source = "Sanger";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "this is a comment";
        String freeFormData = null;

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        fail("Expected AssertionError");
    }
}