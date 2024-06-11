import org.junit.Test;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.factory.DateFactory;
import org.jcvi.jillion.test.TestUtils;
import org.jcvi.jillion.core.util.StringUtils;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

 private DefaultPhdReadTag createDefaultPhdReadTag() {
 return new DefaultPhdReadTag("type1", "source1", new Range(1, 10), DateFactory.makeDate(2022, 1, 1), "comment1", "freeFormData1");
 }

 @Test
 public void testDefaultPhdReadTag_validConstruction() {
 DefaultPhdReadTag tag = createDefaultPhdReadTag();
 assertEquals("type1", tag.getType());
 assertEquals("source1", tag.getSource());
 assertEquals(Range.make(1, 10), tag.getUngappedRange());
 assertNotNull(tag.getDate());
 assertEquals("comment1", tag.getComment());
 assertEquals("freeFormData1", tag.getFreeFormData());
 }

 @Test
 public void testDefaultPhdReadTag_nullValues() {
 DefaultPhdReadTag tag = new DefaultPhdReadTag(null, null, null, null, null, null);
 assertNull(tag.getType());
 assertNull(tag.getSource());
 assertNull(tag.getUngappedRange());
 assertNull(tag.getDate());
 assertNull(tag.getComment());
 assertNull(tag.getFreeFormData());
 }

 @Test
 public void testDefaultPhdReadTag_emptyValues() {
 DefaultPhdReadTag tag = new DefaultPhdReadTag("", "", Range.makeEmpty(), DateFactory.makeDate(2022, 1, 1), "", "");
 assertEquals("", tag.getType());
 assertEquals("", tag.getSource());
 assertFalse(tag.getUngappedRange().isDefined());
 assertNotNull(tag.getDate());
 assertEquals("", tag.getComment());
 assertEquals("", tag.getFreeFormData());
 }

 @Test
 public void testDefaultPhdReadTag_hashCode_equals() {
 DefaultPhdReadTag tag1 = createDefaultPhdReadTag();
 DefaultPhdReadTag tag2 = createDefaultPhdReadTag();
 assertEquals(tag1.hashCode(), tag2.hashCode());
 assertEquals(tag1, tag2);

 tag1.setComment("comment2");
 assertNotEquals(tag1.hashCode(), tag2.hashCode());
 assertNotEquals(tag1, tag2);
 }
}