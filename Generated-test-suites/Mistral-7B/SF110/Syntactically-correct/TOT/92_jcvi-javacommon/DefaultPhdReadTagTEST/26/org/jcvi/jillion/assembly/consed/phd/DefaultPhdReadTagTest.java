import org.junit.Test;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.util.DateTime;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

 @Test
 public void testDefaultPhdReadTagConstruction() {
 DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new DateTime("2022-01-01"), "comment1", "freeFormData1");
 assertEquals("type1", tag.getType());
 assertEquals("source1", tag.getSource());
 assertEquals(new Range(1, 10), tag.getUngappedRange());
 assertEquals("2022-01-01", tag.getDate().toString());
 assertEquals("comment1", tag.getComment());
 assertEquals("freeFormData1", tag.getFreeFormData());
 }

 @Test
 public void testDefaultPhdReadTagGetters() {
 DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new DateTime("2022-01-01"), "comment1", "freeFormData1");
 assertNull(tag.getType());
 assertNull(tag.getSource());
 assertNull(tag.getUngappedRange());
 assertNull(tag.getDate());
 assertNull(tag.getComment());
 assertNull(tag.getFreeFormData());
 tag.setType("type1");
 tag.setSource("source1");
 tag.setUngappedRange(new Range(1, 10));
 tag.setDate(new DateTime("2022-01-01"));
 tag.setComment("comment1");
 tag.setFreeFormData("freeFormData1");
 assertEquals("type1", tag.getType());
 assertEquals("source1", tag.getSource());
 assertEquals(new Range(1, 10), tag.getUngappedRange());
 assertEquals("2022-01-01", tag.getDate().toString());
 assertEquals("comment1", tag.getComment());
 assertEquals("freeFormData1", tag.getFreeFormData());
 }

 @Test
 public void testDefaultPhdReadTagHashCodeAndEquals() {
 DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new DateTime("2022-01-01"), "comment1", "freeFormData1");
 DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new DateTime("2022-01-01"), "comment1", "freeFormData1");
 DefaultPhdReadTag tag3 = new DefaultPhdReadTag("type1", "source2", new Range(1, 10), new DateTime("2022-01-01"), "comment1", "freeFormData1");
 DefaultPhdReadTag tag4 = new DefaultPhdReadTag("type2", "source1", new Range(1, 10), new DateTime("2022-01-01"), "comment1", "freeFormData1");
 DefaultPhdReadTag tag5 = new DefaultPhdReadTag("type1", "source1", new Range(1, 11), new DateTime("2022-01-01"), "comment1", "freeFormData1");

 assertEquals(tag1.hashCode(), tag2.hashCode());
 assertSame(tag1, tag2);
 assertNotSame(tag1, tag3);
 assertNotSame(tag1, tag4);
 assertNotSame(tag1, tag5);
 }
}