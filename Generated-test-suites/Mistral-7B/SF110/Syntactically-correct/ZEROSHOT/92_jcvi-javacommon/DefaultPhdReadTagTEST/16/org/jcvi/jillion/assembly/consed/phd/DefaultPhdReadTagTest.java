import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

 @Test
 public void testDefaultPhdReadTagInstance() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("type2", "source2", new Range(5, 15), new Date("2023-02-02"), "comment2", "freeFormData2");

 assertEquals("type1", defaultPhdReadTag.getType());
 assertEquals("source1", defaultPhdReadTag.getSource());
 assertEquals(new Range(1, 10), defaultPhdReadTag.getUngappedRange());
 assertEquals(new Date("2022-01-01"), defaultPhdReadTag.getDate());
 assertEquals("comment1", defaultPhdReadTag.getComment());
 assertEquals("freeFormData1", defaultPhdReadTag.getFreeFormData());

 assertEquals("type2", defaultPhdReadTag2.getType());
 assertEquals("source2", defaultPhdReadTag2.getSource());
 assertEquals(new Range(5, 15), defaultPhdReadTag2.getUngappedRange());
 assertEquals(new Date("2023-02-02"), defaultPhdReadTag2.getDate());
 assertEquals("comment2", defaultPhdReadTag2.getComment());
 assertEquals("freeFormData2", defaultPhdReadTag2.getFreeFormData());
 }

 @Test
 public void testDefaultPhdReadTagEquals() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
 DefaultPhdReadTag defaultPhdReadTag3 = new DefaultPhdReadTag("type2", "source2", new Range(5, 15), new Date("2023-02-02"), "comment2", "freeFormData2");

 assertTrue(defaultPhdReadTag.equals(defaultPhdReadTag));
 assertTrue(defaultPhdReadTag2.equals(defaultPhdReadTag2));
 assertFalse(defaultPhdReadTag.equals(defaultPhdReadTag3));
 }

 @Test
 public void testDefaultPhdReadTagHashCode() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
 DefaultPhdReadTag defaultPhdReadTag3 = new DefaultPhdReadTag("type2", "source2", new Range(5, 15), new Date("2023-02-02"), "comment2", "freeFormData2");

 int defaultPhdReadTagHashCode = defaultPhdReadTag.hashCode();
 int defaultPhdReadTag2HashCode = defaultPhdReadTag2.hashCode();
 int defaultPhdReadTag3HashCode = defaultPhdReadTag3.hashCode();

 assertEquals(defaultPhdReadTagHashCode, defaultPhdReadTag2HashCode);
 assertNotSame(defaultPhdReadTagHashCode, defaultPhdReadTag3HashCode);
 }
}