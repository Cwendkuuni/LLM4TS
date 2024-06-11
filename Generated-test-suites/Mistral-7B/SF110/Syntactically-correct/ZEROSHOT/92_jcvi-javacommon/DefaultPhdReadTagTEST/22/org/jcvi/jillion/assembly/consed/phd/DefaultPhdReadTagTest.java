import org.jcvi.jillion.core.*;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

 @Test
 public void testDefaultPhdReadTagConstruction() {
 DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date("2022-01-01"), "comment", "freeFormData");
 assertEquals("type", phdReadTag.getType());
 assertEquals("source", phdReadTag.getSource());
 assertNotNull(phdReadTag.getUngappedRange());
 assertEquals(1, phdReadTag.getUngappedRange().getStart());
 assertEquals(10, phdReadTag.getUngappedRange().getEnd());
 assertNotNull(phdReadTag.getDate());
 assertEquals("2022-01-01", phdReadTag.getDate().toString());
 assertEquals("comment", phdReadTag.getComment());
 assertEquals("freeFormData", phdReadTag.getFreeFormData());
 }

 @Test
 public void testDefaultPhdReadTagHashCode() {
 DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 1), new Date("2022-01-01"), "comment1", "freeFormData1");
 DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("type2", "source2", new Range(2, 2), new Date("2022-02-02"), "comment2", "freeFormData2");
 int hashCode1 = phdReadTag1.hashCode();
 int hashCode2 = phdReadTag2.hashCode();
 assertNotSame(hashCode1, hashCode2);
 }

 @Test
 public void testDefaultPhdReadTagEquals() {
 DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 1), new Date("2022-01-01"), "comment1", "freeFormData1");
 DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 1), new Date("2022-01-01"), "comment1", "freeFormData1");
 DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag("type2", "source2", new Range(2, 2), new Date("2022-02-02"), "comment2", "freeFormData2");
 assertTrue(phdReadTag1.equals(phdReadTag1));
 assertTrue(phdReadTag1.equals(phdReadTag2));
 assertFalse(phdReadTag1.equals(phdReadTag3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefaultPhdReadTagNullType() {
 try {
 new DefaultPhdReadTag(null, "source", new Range(1, 1), new Date("2022-01-01"), "comment", "freeFormData");
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefaultPhdReadTagNullSource() {
 try {
 new DefaultPhdReadTag("type", null, new Range(1, 1), new Date("2022-01-01"), "comment", "freeFormData");
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefaultPhdReadTagNullUngappedRange() {
 try {
 new DefaultPhdReadTag("type", "source", null, new Date("2022-01-01"), "comment", "freeFormData");
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefaultPhdReadTagNullDate() {
 try {
 new DefaultPhdReadTag("type", "source", new Range(1, 1), null, "comment", "freeFormData");
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }
}