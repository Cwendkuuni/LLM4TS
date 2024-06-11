import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

 @Test
 public void testDefaultPhdReadTagConstruction() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date("2022-01-01"), "comment", "freeFormData");
 assertEquals("type", defaultPhdReadTag.getType());
 assertEquals("source", defaultPhdReadTag.getSource());
 assertEquals(1, defaultPhdReadTag.getUngappedRange().getStart());
 assertEquals(10, defaultPhdReadTag.getUngappedRange().getEnd());
 assertEquals("2022-01-01", defaultPhdReadTag.getDate().toString());
 assertEquals("comment", defaultPhdReadTag.getComment());
 assertEquals("freeFormData", defaultPhdReadTag.getFreeFormData());
 }

 @Test
 public void testDefaultPhdReadTagHashCode() {
 DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("type2", "source2", new Range(2, 20), new Date("2022-02-01"), "comment2", "freeFormData2");
 int hashCode1 = defaultPhdReadTag1.hashCode();
 int hashCode2 = defaultPhdReadTag2.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 }

 @Test
 public void testDefaultPhdReadTagEquals() {
 DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
 assertTrue(defaultPhdReadTag1.equals(defaultPhdReadTag2));
 DefaultPhdReadTag defaultPhdReadTag3 = new DefaultPhdReadTag("type2", "source2", new Range(2, 20), new Date("2022-02-01"), "comment2", "freeFormData2");
 assertFalse(defaultPhdReadTag1.equals(defaultPhdReadTag3));
 }

 @Test(expected = NullPointerException.class)
 public void testDefaultPhdReadTagNullConstructor() {
 new DefaultPhdReadTag(null, null, null, null, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testDefaultPhdReadTagNullType() {
 new DefaultPhdReadTag(null, "source", new Range(1, 10), new Date("2022-01-01"), "comment", "freeFormData");
 }

 @Test(expected = NullPointerException.class)
 public void testDefaultPhdReadTagNullSource() {
 new DefaultPhdReadTag("type", null, new Range(1, 10), new Date("2022-01-01"), "comment", "freeFormData");
 }

 @Test(expected = NullPointerException.class)
 public void testDefaultPhdReadTagNullUngappedRange() {
 new DefaultPhdReadTag("type", "source", null, new Date("2022-01-01"), "comment", "freeFormData");
 }

 @Test(expected = NullPointerException.class)
 public void testDefaultPhdReadTagNullDate() {
 new DefaultPhdReadTag("type", "source", new Range(1, 10), null, "comment", "freeFormData");
 }

 @Test(expected = NullPointerException.class)
 public void testDefaultPhdReadTagNullComment() {
 new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date("2022-01-01"), null, "freeFormData");
 }

 @Test(expected = NullPointerException.class)
 public void testDefaultPhdReadTagNullFreeFormData() {
 new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date("2022-01-01"), "comment", null);
 }
}