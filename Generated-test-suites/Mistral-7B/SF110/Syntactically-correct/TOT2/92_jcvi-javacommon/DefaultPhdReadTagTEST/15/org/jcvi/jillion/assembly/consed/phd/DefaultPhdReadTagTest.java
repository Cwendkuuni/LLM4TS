import org.jcvi.jillion.core.*;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("1990-01-01"), "comment1", "freeFormData1");
        assertEquals("type1", phdReadTag.getType());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullArguments() {
        new DefaultPhdReadTag(null, null, null, null, null, null);
    }

    @Test
    public void testConstructorWithEmptyStrings() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("", "", new Range(1, 10), new Date("1990-01-01"), "", "");
        assertEquals("", phdReadTag.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidDateFormat() {
        Date date = new Date("Invalid date format");
        new DefaultPhdReadTag("type1", "source1", new Range(1, 10), date, "comment1", "freeFormData1");
    }

    @Test
    public void testEqualsWithNullObject() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("1990-01-01"), "comment1", "freeFormData1");
        assertFalse(phdReadTag1.equals(null));
    }

    @Test
    public void testEqualsWithDifferentObjectTypes() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("1990-01-01"), "comment1", "freeFormData1");
        assertFalse(phdReadTag1.equals(new Object()));
    }

    @Test
    public void testHashCodeWithSameObjects() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("1990-01-01"), "comment1", "freeFormData1");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("1990-01-01"), "comment1", "freeFormData1");
        assertEquals(phdReadTag1.hashCode(), phdReadTag2.hashCode());
    }

    @Test
    public void testHashCodeWithDifferentObjects() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("1990-01-01"), "comment1", "freeFormData1");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("type2", "source2", new Range(1, 10), new Date("1990-01-01"), "comment2", "freeFormData2");
        assertNotSame(phdReadTag1.hashCode(), phdReadTag2.hashCode());
    }
}