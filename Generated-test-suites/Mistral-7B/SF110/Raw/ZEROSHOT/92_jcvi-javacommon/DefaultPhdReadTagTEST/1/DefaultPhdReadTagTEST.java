import org.jcvi.jillion.core.*;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.junit.Assert.*;
import org.junit.Test;

import java.util.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstructor() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(1632069320000L); // Jan 1, 2022
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
        Date date = new Date(1632069320000L); // Jan 1, 2022
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag2.hashCode();

        assertEquals(hashCode1, hashCode2);

        comment = "comment2";
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertNotEquals(hashCode1, tag3.hashCode());
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(1632069320000L); // Jan 1, 2022
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertTrue(tag1.equals(tag2));

        comment = "comment2";
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertFalse(tag1.equals(tag3));

        tag3.setComment(comment);

        assertTrue(tag1.equals(tag3));
    }

    @Test
    public void testDefaultPhdReadTagGetters() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(1632069320000L); // Jan 1, 2022
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

    @Test (expected = NullPointerException.class)
    public void testDefaultPhdReadTagNullConstructor() {
        new DefaultPhdReadTag(null, null, null, null, null, null);
    }

    @Test (expected = NullPointerException.class)
    public void testDefaultPhdReadTagNullType() {
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(1632069320000L); // Jan 1, 2022
        String comment = "comment";
        String freeFormData = "freeFormData";

        new DefaultPhdReadTag(null, source, ungappedRange, date, comment, freeFormData);
    }

    @Test (expected = NullPointerException.class)
    public void testDefaultPhdReadTagNullSource() {
        String type = "type";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(1632069320000L); // Jan 1, 2022
        String comment = "comment";
        String freeFormData = "freeFormData";

        new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
    }

    @Test (expected = NullPointerException.class)
    public void testDefaultPhdReadTagNullUngappedRange() {
        String type = "type";
        String source = "source";
        Date date = new Date(1632069320000L); // Jan 1, 2022
        String comment = "comment";
        String freeFormData = "freeFormData";

        new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
    }

    @Test (expected = NullPointerException.class)
    public void testDefaultPhdReadTagNullDate() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        String comment = "comment";
        String freeFormData = "freeFormData";

        new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);
    }

    @Test (expected = NullPointerException.class)
    public void testDefaultPhdReadTagNullComment() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(1632069320000L); // Jan 1, 2022
        String freeFormData = "freeFormData";

        new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);
    }

    @Test (expected = NullPointerException.class)
    public void testDefaultPhdReadTagNullFreeFormData() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(1632069320000L); // Jan 1, 2022
        String comment = "comment";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);
    }
}