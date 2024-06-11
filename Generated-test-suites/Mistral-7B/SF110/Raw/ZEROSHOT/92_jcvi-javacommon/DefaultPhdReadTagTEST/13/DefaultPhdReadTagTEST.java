import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

 @Test
 public void testDefaultPhdReadTagConstruction() {
 DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date("2022-01-01"), "comment", "freeFormData");
 assertEquals("type", tag.getType());
 assertEquals("source", tag.getSource());
 assertEquals(new Range(1, 10), tag.getUngappedRange());
 assertEquals(new Date("2022-01-01"), tag.getDate());
 assertEquals("comment", tag.getComment());
 assertEquals("freeFormData", tag.getFreeFormData());
 }

 @Test
 public void testDefaultPhdReadTagHashCode() {
 DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
 DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type2", "source2", new Range(1, 10), new Date("2022-01-01"), "comment2", "freeFormData2");
 assertNotEquals(tag1.hashCode(), tag2.hashCode());
 }

 @Test
 public void testDefaultPhdReadTagEquals() {
 DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
 DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
 assertTrue(tag1.equals(tag2));
 DefaultPhdReadTag tag3 = new DefaultPhdReadTag("type2", "source2", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
 assertFalse(tag1.equals(tag3));
 }
}