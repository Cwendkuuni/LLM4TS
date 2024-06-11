import org.junit.Assert;
import org.junit.Test;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.assembly.consed.phd.PhdReadTag;

public class DefaultPhdReadTagTest {

 private String type = "testType";
 private String source = "testSource";
 private Range ungappedRange = new Range(0, 10);
 private Date date = new Date();
 private String comment = "testComment";
 private String freeFormData = "testFreeFormData";

 @Test
 public void testDefaultPhdReadTagConstructor() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

 Assert.assertEquals(type, defaultPhdReadTag.getType());
 Assert.assertEquals(source, defaultPhdReadTag.getSource());
 Assert.assertEquals(ungappedRange, defaultPhdReadTag.getUngappedRange());
 Assert.assertEquals(date, defaultPhdReadTag.getDate());
 Assert.assertEquals(comment, defaultPhdReadTag.getComment());
 Assert.assertEquals(freeFormData, defaultPhdReadTag.getFreeFormData());
 }

 @Test
 public void testGetHashCode() {
 DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

 Assert.assertEquals(defaultPhdReadTag1.hashCode(), defaultPhdReadTag2.hashCode());

 DefaultPhdReadTag defaultPhdReadTag3 = new DefaultPhdReadTag("testType2", source, ungappedRange, date, comment, freeFormData);

 Assert.assertNotEquals(defaultPhdReadTag1.hashCode(), defaultPhdReadTag3.hashCode());
 }

 @Test
 public void testEquals() {
 DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 DefaultPhdReadTag defaultPhdReadTag3 = new DefaultPhdReadTag("testType2", source, ungappedRange, date, comment, freeFormData);
 PhdReadTag phdReadTag = new DefaultPhdReadTag("testType2", source, null, date, comment, freeFormData);

 Assert.assertTrue(defaultPhdReadTag1.equals(defaultPhdReadTag2));
 Assert.assertFalse(defaultPhdReadTag1.equals(defaultPhdReadTag3));
 Assert.assertFalse(defaultPhdReadTag1.equals(phdReadTag));
 }

 @Test(expected = NullPointerException.class)
 public void testNullSource() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testNullType() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(null, source, ungappedRange, date, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testNullComment() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testNullFreeFormData() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUngappedRangeNull() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUngappedRangeInvalidStart() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, new Range(-1, 10), date, comment, freeFormData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUngappedRangeInvalidEnd() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, new Range(0, -1), date, comment, freeFormData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUngappedRangeInvalidLength() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, new Range(0, 10), date, comment, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDateNull() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);
 }
}