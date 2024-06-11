import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

 private String type = "DNA";
 private String source = "contig1";
 private Range ungappedRange = new Range(1, 10);
 private Date date = new Date(2022, 1, 1);
 private String comment = "example comment";
 private String freeFormData = "example data";

 @Test
 public void testgetType() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertEquals("DNA", defaultPhdReadTag.getType());
 }

 @Test
 public void testGetSource() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertEquals("contig1", defaultPhdReadTag.getSource());
 }

 @Test
 public void testGetUngappedRange() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertEquals(new Range(1, 10), defaultPhdReadTag.getUngappedRange());
 }

 @Test
 public void testGetDate() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertEquals(date, defaultPhdReadTag.getDate());
 }

 @Test
 public void testGetComment() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertEquals("example comment", defaultPhdReadTag.getComment());
 }

 @Test
 public void testGetFreeFormData() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 assertEquals("example data", defaultPhdReadTag.getFreeFormData());
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullType() {
 new DefaultPhdReadTag(null, source, ungappedRange, date, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullSource() {
 new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullUngappedRange() {
 new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullDate() {
 new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullComment() {
 new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullFreeFormData() {
 new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);
 }

 @Test
 public void testHashCode() {
 DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("DNA", "contig1", new Range(1, 10), new Date(2022, 1, 1), "example comment", "example data");
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("DNA", "contig1", new Range(1, 10), new Date(2022, 1, 1), "example comment", "example data");
 DefaultPhdReadTag defaultPhdReadTag3 = new DefaultPhdReadTag("RNA", "contig2", new Range(1, 10), new Date(2022, 1, 2), "example comment", "example data");
 assertEquals(defaultPhdReadTag1.hashCode(), defaultPhdReadTag2.hashCode());
 assertNotEquals(defaultPhdReadTag1.hashCode(), defaultPhdReadTag3.hashCode());
 }

 @Test
 public void testEquals() {
 DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("DNA", "contig1", new Range(1, 10), new Date(2022, 1, 1), "example comment", "example data");
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("DNA", "contig1", new Range(1, 10), new Date(2022, 1, 1), "example comment", "example data");
 DefaultPhdReadTag defaultPhdReadTag3 = new DefaultPhdReadTag("RNA", "contig2", new Range(1, 10), new Date(2022, 1, 2), "example comment", "example data");
 DefaultPhdReadTag defaultPhdReadTag4 = new DefaultPhdReadTag("DNA", "contig1", new Range(1, 10), new Date(2022, 1, 1), "example comment", "example data");
 assertTrue(defaultPhdReadTag1.equals(defaultPhdReadTag2));
 assertTrue(defaultPhdReadTag1.equals(defaultPhdReadTag4));
 assertFalse(defaultPhdReadTag1.equals(defaultPhdReadTag3));
 }
}