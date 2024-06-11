import org.junit.Assert;import org.junit.Test;import org.jcvi.jillion.core.Range;import org.jcvi.jillion.core.Date;import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;

public class DefaultPhdReadTagTest {

 private DefaultPhdReadTag validDefaultPhdReadTag;

 @Test
 public void testDefaultPhdReadTagConstructor() {
 String type = "protein";
 String source = "R1";
 Range ungappedRange = new Range(1, 100);
 Date date = new Date(2022, 1, 1);
 String comment = "Comment1";
 String freeFormData = "FreeFormData1";

 validDefaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

 // Basic functionality test for getType() method
 Assert.assertEquals("protein", validDefaultPhdReadTag.getType());

 // Basic functionality test for getSource() method
 Assert.assertEquals("R1", validDefaultPhdReadTag.getSource());

 // Basic functionality test for getUngappedRange() method
 Assert.assertEquals(ungappedRange, validDefaultPhdReadTag.getUngappedRange());

 // Basic functionality test for getDate() method
 Assert.assertEquals(date, validDefaultPhdReadTag.getDate());

 // Basic functionality test for getComment() method
 Assert.assertEquals("Comment1", validDefaultPhdReadTag.getComment());

 // Basic functionality test for getFreeFormData() method
 Assert.assertEquals("FreeFormData1", validDefaultPhdReadTag.getFreeFormData());
 }

 @Test(expected = NullPointerException.class)
 public void testDefaultPhdReadTagConstructorWithNullType() {
 String source = "R1";
 Range ungappedRange = new Range(1, 100);
 Date date = new Date(2022, 1, 1);
 String comment = "Comment1";
 String freeFormData = "FreeFormData1";

 new DefaultPhdReadTag(null, source, ungappedRange, date, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testDefaultPhdReadTagConstructorWithNullSource() {
 String type = "protein";
 Range ungappedRange = new Range(1, 100);
 Date date = new Date(2022, 1, 1);
 String comment = "Comment1";
 String freeFormData = "FreeFormData1";

 new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testDefaultPhdReadTagConstructorWithNullUngappedRange() {
 String type = "protein";
 String source = "R1";
 Date date = new Date(2022, 1, 1);
 String comment = "Comment1";
 String freeFormData = "FreeFormData1";

 new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testDefaultPhdReadTagConstructorWithNullDate() {
 String type = "protein";
 String source = "R1";
 Range ungappedRange = new Range(1, 100);
 String comment = "Comment1";
 String freeFormData = "FreeFormData1";

 new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testDefaultPhdReadTagConstructorWithNullComment() {
 String type = "protein";
 String source = "R1";
 Range ungappedRange = new Range(1, 100);
 Date date = new Date(2022, 1, 1);
 String freeFormData = "FreeFormData1";

 new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testDefaultPhdReadTagConstructorWithNullFreeFormData() {
 String type = "protein";
 String source = "R1";
 Range ungappedRange = new Range(1, 100);
 Date date = new Date(2022, 1, 1);
 String comment = "Comment1";

 new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);
 }

 @Test
 public void testDefaultPhdReadTagHashCode() {
 DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("protein1", "R1", new Range(1, 100), new Date(2022, 1, 1), "Comment1", "FreeFormData1");
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("protein2", "R2", new Range(2, 200), new Date(2022, 2, 2), "Comment2", "FreeFormData2");

 int hashCodeDefaultPhdReadTag1 = defaultPhdReadTag1.hashCode();
 int hashCodeDefaultPhdReadTag2 = defaultPhdReadTag2.hashCode();

 Assert.assertNotEquals(hashCodeDefaultPhdReadTag1, hashCodeDefaultPhdReadTag2);
 }

 @Test
 public void testDefaultPhdReadTagEquality() {
 DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("protein1", "R1", new Range(1, 100), new Date(2022, 1, 1), "Comment1", "FreeFormData1");
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("protein1", "R1", new Range(1, 100), new Date(2022, 1, 1), "Comment1", "FreeFormData1");

 Assert.assertTrue(defaultPhdReadTag1.equals(defaultPhdReadTag2));
 }

 @Test
 public void testDefaultPhdReadTagInequality() {
 DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("protein1", "R1", new Range(1, 100), new Date(2022, 1, 1), "Comment1", "FreeFormData1");
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("protein2", "R2", new Range(2, 200), new Date(2022, 2, 2), "Comment2", "FreeFormData2");

 Assert.assertFalse(defaultPhdReadTag1.equals(defaultPhdReadTag2));
 }
}