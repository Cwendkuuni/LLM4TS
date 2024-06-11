import org.junit.Test;
import org.junit.Assert.*;
import org.jcvi.jillion.core.*;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import java.util.Date;
import java.util.Arrays;

public class DefaultPhdReadTagTest {

 private String type = "testType";
 private String source = "testSource";
 private Range ungappedRange = new Range(1, 10);
 private Date date = new Date();
 private String comment = "testComment";
 private String freeFormData = "testData";

 @Test
 public void testDefaultPhdReadTagConstruction() {
 DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertEquals(type, tag.getType());
 assertEquals(source, tag.getSource());
 assertEquals(ungappedRange, tag.getUngappedRange());
 assertEquals(date, tag.getDate());
 assertEquals(comment, tag.getComment());
 assertEquals(freeFormData, tag.getFreeFormData());
 }

 @Test
 public void testDefaultPhdReadTagHashCode() {
 DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 DefaultPhdReadTag tag2 = new DefaultPhdReadTag("testType2", source, ungappedRange, date, comment, freeFormData);
 DefaultPhdReadTag tag3 = new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);
 DefaultPhdReadTag tag4 = new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
 DefaultPhdReadTag tag5 = new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
 DefaultPhdReadTag tag6 = new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);
 DefaultPhdReadTag tag7 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);

 HashSet<DefaultPhdReadTag> tags = new HashSet<>(Arrays.asList(tag1, tag2, tag3, tag4, tag5, tag6, tag7));
 assertTrue(tags.size() == 7);
 }

 @Test
 public void testDefaultPhdReadTagEquals() {
 DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 DefaultPhdReadTag tag3 = new DefaultPhdReadTag("testType2", source, ungappedRange, date, comment, freeFormData);
 DefaultPhdReadTag tag4 = new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);
 DefaultPhdReadTag tag5 = new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
 DefaultPhdReadTag tag6 = new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
 DefaultPhdReadTag tag7 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);

 assertTrue(tag1.equals(tag1));
 assertTrue(tag1.equals(tag2));
 assertFalse(tag1.equals(tag3));
 assertFalse(tag1.equals(tag4));
 assertFalse(tag1.equals(tag5));
 assertFalse(tag1.equals(tag6));
 assertFalse(tag1.equals(tag7));
 }

 @Test(expected = NullPointerException.class)
 public void testDefaultPhdReadTagNullConstruction() {
 new DefaultPhdReadTag(null, null, null, null, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testDefaultPhdReadTagNullType() {
 new DefaultPhdReadTag(null, source, ungappedRange, date, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testDefaultPhdReadTagNullSource() {
 new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testDefaultPhdReadTagNullUngappedRange() {
 new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testDefaultPhdReadTagNullDate() {
 new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testDefaultPhdReadTagNullComment() {
 new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testDefaultPhdReadTagNullFreeFormData() {
 new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);
 }
}