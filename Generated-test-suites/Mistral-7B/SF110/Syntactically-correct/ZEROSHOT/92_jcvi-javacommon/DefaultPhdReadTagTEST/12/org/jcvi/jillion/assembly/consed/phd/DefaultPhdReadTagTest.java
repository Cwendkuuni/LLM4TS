import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.jcvi.jillion.core.StringUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class DefaultPhdReadTagTest {

 @Test
 public void testDefaultPhdReadTagConstructor() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2021, 1, 1), "comment1", "freeFormData1");

 assertEquals("type1", defaultPhdReadTag.getType());
 assertEquals("source1", defaultPhdReadTag.getSource());
 assertEquals(1, defaultPhdReadTag.getUngappedRange().getStart());
 assertEquals(10, defaultPhdReadTag.getUngappedRange().getEnd());
 assertEquals(new Date(2021, 1, 1), defaultPhdReadTag.getDate());
 assertEquals("comment1", defaultPhdReadTag.getComment());
 assertEquals("freeFormData1", defaultPhdReadTag.getFreeFormData());
 }

 @Test
 public void testDefaultPhdReadTagGetters() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2021, 1, 1), "comment1", "freeFormData1");

 assertEquals("type1", defaultPhdReadTag.getType());
 assertEquals("source1", defaultPhdReadTag.getSource());
 assertEquals(1, defaultPhdReadTag.getUngappedRange().getStart());
 assertEquals(10, defaultPhdReadTag.getUngappedRange().getEnd());
 assertEquals(new Date(2021, 1, 1), defaultPhdReadTag.getDate());
 assertEquals("comment1", defaultPhdReadTag.getComment());
 assertEquals("freeFormData1", defaultPhdReadTag.getFreeFormData());
 }

 @Test
 public void testDefaultPhdReadTagHashCode() {
 DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2021, 1, 1), "comment1", "freeFormData1");
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("type2", "source2", new Range(2, 20), new Date(2021, 2, 2), "comment2", "freeFormData2");

 int hashCode1 = defaultPhdReadTag1.hashCode();
 int hashCode2 = defaultPhdReadTag2.hashCode();

 assertNotEquals(hashCode1, hashCode2);
 }

 @Test
 public void testDefaultPhdReadTagEquals() {
 DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2021, 1, 1), "comment1", "freeFormData1");
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2021, 1, 1), "comment1", "freeFormData1");
 DefaultPhdReadTag defaultPhdReadTag3 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2021, 1, 2), "comment1", "freeFormData1");
 DefaultPhdReadTag defaultPhdReadTag4 = new DefaultPhdReadTag("type2", "source2", new Range(2, 20), new Date(2021, 2, 2), "comment2", "freeFormData2");

 assertTrue(defaultPhdReadTag1.equals(defaultPhdReadTag1));
 assertTrue(defaultPhdReadTag1.equals(defaultPhdReadTag2));
 assertFalse(defaultPhdReadTag1.equals(defaultPhdReadTag3));
 assertFalse(defaultPhdReadTag1.equals(defaultPhdReadTag4));
 }

 @Test
 public void testDefaultPhdReadTagEqualsWithNull() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2021, 1, 1), "comment1", "freeFormData1");

 assertFalse(defaultPhdReadTag.equals(null));
 }

 @Test
 public void testDefaultPhdReadTagEqualsWithDifferentClass() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2021, 1, 1), "comment1", "freeFormData1");
 Object obj = new Object();

 assertFalse(defaultPhdReadTag.equals(obj));
 }

 @Test
 public void testDefaultPhdReadTagToString() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2021, 1, 1), "comment1", "freeFormData1");

 String toString = defaultPhdReadTag.toString();
 String expected = String.format("DefaultPhdReadTag{type='%s', source='%s', ungappedRange=Range{start=1, end=10}, date=%s, comment='%s', freeFormData='%s'}", defaultPhdReadTag.getType(), defaultPhdReadTag.getSource(), defaultPhdReadTag.getDate(), defaultPhdReadTag.getComment(), defaultPhdReadTag.getFreeFormData());

 assertEquals(expected, toString);
 }
}