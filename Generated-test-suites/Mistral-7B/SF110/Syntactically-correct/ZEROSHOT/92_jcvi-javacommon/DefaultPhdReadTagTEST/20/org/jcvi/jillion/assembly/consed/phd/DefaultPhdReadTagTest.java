import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.junit.Test;
import org.junit.Assert.*;

public class DefaultPhdReadTagTest {

 private String type = "type";
 private String source = "source";
 private Range ungappedRange = new Range(0, 10);
 private Date date = new Date(2022, 1, 1);
 private String comment = "comment";
 private String freeFormData = "freeFormData";

 @Test
 public void testDefaultPhdReadTagConstruction() {
 DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertEquals(type, phdReadTag.getType());
 assertEquals(source, phdReadTag.getSource());
 assertEquals(ungappedRange, phdReadTag.getUngappedRange());
 assertEquals(date, phdReadTag.getDate());
 assertEquals(comment, phdReadTag.getComment());
 assertEquals(freeFormData, phdReadTag.getFreeFormData());
 }

 @Test
 public void testDefaultPhdReadTagHashCode() {
 DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertEquals(phdReadTag1.hashCode(), phdReadTag2.hashCode());

 DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag("type2", source, ungappedRange, date, comment, freeFormData);
 assertNotSame(phdReadTag1.hashCode(), phdReadTag3.hashCode());
 }

 @Test
 public void testDefaultPhdReadTagEquals() {
 DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertTrue(phdReadTag1.equals(phdReadTag2));

 DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag("type2", source, ungappedRange, date, comment, freeFormData);
 assertFalse(phdReadTag1.equals(phdReadTag3));
 }

 @Test
 public void testDefaultPhdReadTagEmptyConstructors() {
 DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag();
 assertNull(phdReadTag.getType());
 assertNull(phdReadTag.getSource());
 assertNull(phdReadTag.getUngappedRange());
 assertNull(phdReadTag.getDate());
 assertNull(phdReadTag.getComment());
 assertNull(phdReadTag.getFreeFormData());
 }
}