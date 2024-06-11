import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

 private String type = "testType";
 private String source = "testSource";
 private Range ungappedRange = new Range(0, 10);
 private Date date = new Date(2022, 1, 1);
 private String comment = "testComment";
 private String freeFormData = "testFreeFormData";

 @Test
 public void testGetType() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertEquals(type, defaultPhdReadTag.getType());
 }

 @Test
 public void testGetSource() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertEquals(source, defaultPhdReadTag.getSource());
 }

 @Test
 public void testGetUngappedRange() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertEquals(ungappedRange, defaultPhdReadTag.getUngappedRange());
 }

 @Test
 public void testGetDate() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertEquals(date, defaultPhdReadTag.getDate());
 }

 @Test
 public void testGetComment() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertEquals(comment, defaultPhdReadTag.getComment());
 }

 @Test
 public void testGetFreeFormData() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertEquals(freeFormData, defaultPhdReadTag.getFreeFormData());
 }

 @Test(expected = NullPointerException.class)
 public void testConstructNullType() {
 new DefaultPhdReadTag(null, source, ungappedRange, date, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructNullSource() {
 new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructNullUngappedRange() {
 new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructNullDate() {
 new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructNullComment() {
 new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructNullFreeFormData() {
 new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);
 }

 @Test
 public void testHashCode() {
 DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertEquals(defaultPhdReadTag1.hashCode(), defaultPhdReadTag2.hashCode());
 }

 @Test
 public void testHashCodeDifferentObjects() {
 DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("differentType", source, ungappedRange, date, comment, freeFormData);
 assertNotEquals(defaultPhdReadTag1.hashCode(), defaultPhdReadTag2.hashCode());
 }

 @Test
 public void testEqualsSameObjects() {
 DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertTrue(defaultPhdReadTag1.equals(defaultPhdReadTag2));
 }

 @Test
 public void testEqualsDifferentObjects() {
 DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("differentType", source, ungappedRange, date, comment, freeFormData);
 assertFalse(defaultPhdReadTag1.equals(defaultPhdReadTag2));
 }

 @Test
 public void testEqualsSelf() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertTrue(defaultPhdReadTag.equals(defaultPhdReadTag));
 }

 @Test
 public void testEqualsNull() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertFalse(defaultPhdReadTag.equals(null));
 }

 @Test
 public void testEqualsDifferentClass() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertFalse(defaultPhdReadTag.equals(new Object()));
 }

 @Test
 public void testEqualsNullSource() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertFalse(defaultPhdReadTag.equals(defaultPhdReadTag2));
 }

 @Test
 public void testEqualsNullComment() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertFalse(defaultPhdReadTag.equals(defaultPhdReadTag2));
 }

 @Test
 public void testEqualsDifferentUngappedRange() {
 DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag(type, source, new Range(0, 5), date, comment, freeFormData);
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag(type, source, new Range(1, 10), date, comment, freeFormData);
 assertFalse(defaultPhdReadTag1.equals(defaultPhdReadTag2));
 }

 @Test
 public void testEqualsDifferentDate() {
 DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, new Date(2021, 1, 1), comment, freeFormData);
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertFalse(defaultPhdReadTag1.equals(defaultPhdReadTag2));
 }

 @Test
 public void testEqualsDifferentType() {
 DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("testType1", source, ungappedRange, date, comment, freeFormData);
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertFalse(defaultPhdReadTag1.equals(defaultPhdReadTag2));
 }

 @Test
 public void testEqualsDifferentFreeFormData() {
 DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, "testFreeFormData1");
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, "testFreeFormData2");
 assertFalse(defaultPhdReadTag1.equals(defaultPhdReadTag2));
 }

}