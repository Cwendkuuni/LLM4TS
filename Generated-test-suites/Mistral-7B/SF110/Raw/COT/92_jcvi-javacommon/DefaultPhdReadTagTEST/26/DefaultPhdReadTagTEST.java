import org.jcvi.jillion.core.Date;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.assembly.consed.phd.PhdReadTag;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

 private String type = "DNA";
 private String source = "chr1:1-100";
 private Range ungappedRange = new Range(1, 50);
 private Date date = new Date("2021-01-01");
 private String comment = "this is a comment";
 private String freeFormData = "some data";

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
 public void testNullType() {
 DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, source, ungappedRange, date, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testNullSource() {
 DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testNullUngappedRange() {
 DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testNullDate() {
 DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testNullComment() {
 DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testNullFreeFormData() {
 DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);
 }

 @Test
 public void testHashCodeEquals() {
 DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag("DNA", null, ungappedRange, date, comment, freeFormData);
 DefaultPhdReadTag phdReadTag4 = new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);
 DefaultPhdReadTag phdReadTag5 = new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
 DefaultPhdReadTag phdReadTag6 = new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);
 DefaultPhdReadTag phdReadTag7 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);
 DefaultPhdReadTag phdReadTag8 = new DefaultPhdReadTag("DNA", "chr1:1-100", ungappedRange, date, comment, freeFormData);

 assertEquals(phdReadTag1.hashCode(), phdReadTag2.hashCode());
 assertFalse(phdReadTag1.equals(phdReadTag3));
 assertFalse(phdReadTag1.equals(phdReadTag4));
 assertFalse(phdReadTag1.equals(phdReadTag5));
 assertFalse(phdReadTag1.equals(phdReadTag6));
 assertFalse(phdReadTag1.equals(phdReadTag7));
 assertFalse(phdReadTag1.equals(phdReadTag8));
 assertFalse(phdReadTag1.equals(null));
 }
}