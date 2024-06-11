import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.junit.Test;
import org.junit.Assert.*;

import java.time.LocalDate;
import java.util.Collections;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstructor() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(0, 100);
        Date date = new Date(LocalDate.of(2022, 1, 1));
        String comment = "testComment";
        String freeFormData = "testData";

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, defaultPhdReadTag.getType());
        assertEquals(source, defaultPhdReadTag.getSource());
        assertEquals(ungappedRange, defaultPhdReadTag.getUngappedRange());
        assertEquals(date, defaultPhdReadTag.getDate());
        assertEquals(comment, defaultPhdReadTag.getComment());
        assertEquals(freeFormData, defaultPhdReadTag.getFreeFormData());
    }

    @Test
    public void testGetters() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(0, 100);
        Date date = new Date(LocalDate.of(2022, 1, 1));
        String comment = "testComment";
        String freeFormData = "testData";

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, defaultPhdReadTag.getType());
        assertEquals(source, defaultPhdReadTag.getSource());
        assertEquals(ungappedRange, defaultPhdReadTag.getUngappedRange());
        assertEquals(date, defaultPhdReadTag.getDate());
        assertEquals(comment, defaultPhdReadTag.getComment());
        assertEquals(freeFormData, defaultPhdReadTag.getFreeFormData());
    }

    @Test(expected = NullPointerException.class)
    public void testEqualsNullObject() {
        DefaultPhdReadTag testPhdReadTag = new DefaultPhdReadTag("testType", "testSource", new Range(0, 100), new Date(LocalDate.of(2022, 1, 1)), "testComment", "testData");
        assertEquals(testPhdReadTag, null);
    }

    @Test
    public void testEqualsSameObject() {
        DefaultPhdReadTag testPhdReadTag1 = new DefaultPhdReadTag("testType", "testSource", new Range(0, 100), new Date(LocalDate.of(2022, 1, 1)), "testComment", "testData");
        DefaultPhdReadTag testPhdReadTag2 = testPhdReadTag1;

        assertEquals(testPhdReadTag1, testPhdReadTag2);
    }

    @Test
    public void testEqualsDifferentObjects() {
        DefaultPhdReadTag testPhdReadTag1 = new DefaultPhdReadTag("testType1", "testSource1", new Range(0, 100), new Date(LocalDate.of(2022, 1, 1)), "testComment1", "testData1");
        DefaultPhdReadTag testPhdReadTag2 = new DefaultPhdReadTag("testType2", "testSource2", new Range(0, 100), new Date(LocalDate.of(2022, 1, 1)), "testComment2", "testData2");

        assertNotEquals(testPhdReadTag1, testPhdReadTag2);
    }

    @Test
    public void testHashCodeEquals() {
        DefaultPhdReadTag testPhdReadTag1 = new DefaultPhdReadTag("testType", "testSource", new Range(0, 100), new Date(LocalDate.of(2022, 1, 1)), "testComment", "testData");
        DefaultPhdReadTag testPhdReadTag2 = new DefaultPhdReadTag("testType", "testSource", new Range(0, 100), new Date(LocalDate.of(2022, 1, 1)), "testComment", "testData");

        int hashCode1 = testPhdReadTag1.hashCode();
        int hashCode2 = testPhdReadTag2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCodeDifferentObjects() {
        DefaultPhdReadTag testPhdReadTag1 = new DefaultPhdReadTag("testType1", "testSource1", new Range(0, 100), new Date(LocalDate.of(2022, 1, 1)), "testComment1", "testData1");
        DefaultPhdReadTag testPhdReadTag2 = new DefaultPhdReadTag("testType2", "testSource2", new Range(0, 100), new Date(LocalDate.of(2022, 1, 1)), "testComment2", "testData2");

        int hashCode1 = testPhdReadTag1.hashCode();
        int hashCode2 = testPhdReadTag2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagConstructorNullType() {
        String type = null;
        String source = "testSource";
        Range ungappedRange = new Range(0, 100);
        Date date = new Date(LocalDate.of(2022, 1, 1));
        String comment = "testComment";
        String freeFormData = "testData";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagConstructorNullSource() {
        String type = "testType";
        String source = null;
        Range ungappedRange = new Range(0, 100);
        Date date = new Date(LocalDate.of(2022, 1, 1));
        String comment = "testComment";
        String freeFormData = "testData";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagConstructorNullUngappedRange() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = null;
        Date date = new Date(LocalDate.of(2022, 1, 1));
        String comment = "testComment";
        String freeFormData = "testData";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagConstructorNullDate() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(0, 100);
        Date date = null;
        String comment = "testComment";
        String freeFormData = "testData";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagConstructorNullComment() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(0, 100);
        Date date = new Date(LocalDate.of(2022, 1, 1));
        String comment = null;
        String freeFormData = "testData";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagConstructorNullFreeFormData() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(0, 100);
        Date date = new Date(LocalDate.of(2022, 1, 1));
        String comment = "testComment";
        String freeFormData = null;

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test
    public void testDefaultPhdReadTagConstructorEmptyString() {
        String type = "";
        String source = "";
        Range ungappedRange = new Range(0, 100);
        Date date = new Date(LocalDate.of(2022, 1, 1));
        String comment = "";
        String freeFormData = "";

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals("", defaultPhdReadTag.getType());
        assertEquals("", defaultPhdReadTag.getSource());
        assertEquals(ungappedRange, defaultPhdReadTag.getUngappedRange());
        assertEquals(date, defaultPhdReadTag.getDate());
        assertEquals("", defaultPhdReadTag.getComment());
        assertEquals("", defaultPhdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagConstructorEmptyRange() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range();
        Date date = new Date(LocalDate.of(2022, 1, 1));
        String comment = "testComment";
        String freeFormData = "testData";

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, defaultPhdReadTag.getType());
        assertEquals(source, defaultPhdReadTag.getSource());
        assertEquals(new Range(0, 0), defaultPhdReadTag.getUngappedRange());
        assertEquals(date, defaultPhdReadTag.getDate());
        assertEquals(comment, defaultPhdReadTag.getComment());
        assertEquals(freeFormData, defaultPhdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagConstructorEmptyDate() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(0, 100);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testData";

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, defaultPhdReadTag.getType());
        assertEquals(source, defaultPhdReadTag.getSource());
        assertEquals(ungappedRange, defaultPhdReadTag.getUngappedRange());
        assertNull(defaultPhdReadTag.getDate());
        assertEquals(comment, defaultPhdReadTag.getComment());
        assertEquals(freeFormData, defaultPhdReadTag.getFreeFormData());
    }
}