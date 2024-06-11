import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

 private String type = "testType";
 private String source = "testSource";
 private Range ungappedRange = new Range(1, 10);
 private Date date = new Date(2021, 1, 1);
 private String comment = "testComment";
 private String freeFormData = "testData";

 @Test
 public void testGetType() {
 DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertEquals(type, phdReadTag.getType());
 }

 @Test
 public void testGetSource() {
 DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertEquals(source, phdReadTag.getSource());
 }

 @Test
 public void testGetUngappedRange() {
 DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertEquals(ungappedRange, phdReadTag.getUngappedRange());
 }

 @Test
 public void testGetDate() {
 DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertEquals(date, phdReadTag.getDate());
 }

 @Test
 public void testGetComment() {
 DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertEquals(comment, phdReadTag.getComment());
 }

 @Test
 public void testGetFreeFormData() {
 DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertEquals(freeFormData, phdReadTag.getFreeFormData());
 }

 @Test(expected = NullPointerException.class)
 public void testConstructWithNullType() {
 new DefaultPhdReadTag(null, source, ungappedRange, date, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructWithNullSource() {
 new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructWithNullUngappedRange() {
 new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructWithNullDate() {
 new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructWithNullComment() {
 new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructWithNullFreeFormData() {
 new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);
 }

 @Test
 public void testHashCode() {
 DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertEquals(phdReadTag1.hashCode(), phdReadTag2.hashCode());

 DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag("testType2", source, ungappedRange, date, comment, freeFormData);
 assertNotEquals(phdReadTag1.hashCode(), phdReadTag3.hashCode());
 }

 @Test
 public void testEquals() {
 DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertTrue(phdReadTag1.equals(phdReadTag2));

 DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag("testType2", source, ungappedRange, date, comment, freeFormData);
 assertFalse(phdReadTag1.equals(phdReadTag3));

 DefaultPhdReadTag phdReadTag4 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);
 assertFalse(phdReadTag1.equals(phdReadTag4));
 }

 @Test
 public void testEqualsWithSameObject() {
 DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertTrue(phdReadTag.equals(phdReadTag));
 }
}