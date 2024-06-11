import org.junit.Test;
import org.junit.Assert;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "data1");
        assertEquals("type1", tag.getType());
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "data1");
        assertEquals("source1", tag.getSource());
    }

    @Test
    public void testGetUngappedRangeNull() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", null, new Date("2022-01-01"), "comment1", "data1");
        assertNull(tag.getUngappedRange());
    }

    @Test
    public void testGetUngappedRangeEmpty() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(), new Date("2022-01-01"), "comment1", "data1");
        assertTrue(tag.getUngappedRange().isEmpty());
    }

    @Test
    public void testGetDateNull() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), null, "comment1", "data1");
        assertNull(tag.getDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDateInvalidFormat() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-32"), "comment1", "data1");
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type2", "source2", new Range(11, 20), new Date("2022-01-02"), "comment2", "data2");

        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "data1");
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-02"), "comment1", "data1");

        assertTrue(tag1.equals(tag2));
        assertFalse(tag1.equals(tag3));

        tag1 = new DefaultPhdReadTag(null, null, null, null, null, null);
        assertFalse(tag1.equals(tag2));

        tag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), null, null);
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEqualsNull() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "data1");
        assertFalse(tag.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "data1");
        Object other = new Object();
        assertFalse(tag.equals(other));
    }

}