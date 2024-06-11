import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

 @Test
 public void testDefaultPhdReadTagInstance() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date("2022-01-01"), "comment", "freeFormData");
 DefaultPhdReadTag anotherDefaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date("2022-01-01"), "comment", "freeFormData");

 assertEquals("type", defaultPhdReadTag.getType());
 assertEquals("source", defaultPhdReadTag.getSource());
 assertEquals(new Range(1, 10), defaultPhdReadTag.getUngappedRange());
 assertEquals(new Date("2022-01-01"), defaultPhdReadTag.getDate());
 assertEquals("comment", defaultPhdReadTag.getComment());
 assertEquals("freeFormData", defaultPhdReadTag.getFreeFormData());

 assertTrue(defaultPhdReadTag.equals(anotherDefaultPhdReadTag));
 assertNotSame(defaultPhdReadTag, anotherDefaultPhdReadTag);
 }

 @Test
 public void testDefaultPhdReadTagNullValues() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(null, null, null, null, null, null);

 assertNull(defaultPhdReadTag.getType());
 assertNull(defaultPhdReadTag.getSource());
 assertNull(defaultPhdReadTag.getUngappedRange());
 assertNull(defaultPhdReadTag.getDate());
 assertNull(defaultPhdReadTag.getComment());
 assertNull(defaultPhdReadTag.getFreeFormData());
 }

 @Test(expected = NullPointerException.class)
 public void testDefaultPhdReadTagNullParameters() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(null, null, null, null, null, "freeFormData");
 }

 @Test
 public void testDefaultPhdReadTagHashCode() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
 DefaultPhdReadTag anotherDefaultPhdReadTag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData2");
 DefaultPhdReadTag thirdDefaultPhdReadTag = new DefaultPhdReadTag("type2", "source2", new Range(2, 11), new Date("2022-01-02"), "comment2", "freeFormData3");

 int defaultPhdReadTagHashCode = defaultPhdReadTag.hashCode();
 int anotherDefaultPhdReadTagHashCode = anotherDefaultPhdReadTag.hashCode();
 int thirdDefaultPhdReadTagHashCode = thirdDefaultPhdReadTag.hashCode();

 assertNotEquals(defaultPhdReadTagHashCode, anotherDefaultPhdReadTagHashCode);
 assertNotEquals(defaultPhdReadTagHashCode, thirdDefaultPhdReadTagHashCode);
 assertNotEquals(anotherDefaultPhdReadTagHashCode, thirdDefaultPhdReadTagHashCode);
 }
}