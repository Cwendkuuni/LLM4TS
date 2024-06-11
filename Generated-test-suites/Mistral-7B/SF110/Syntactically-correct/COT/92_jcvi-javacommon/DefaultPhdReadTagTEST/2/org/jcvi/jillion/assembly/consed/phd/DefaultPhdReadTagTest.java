import org.junit.Assert;
import org.junit.Test;
import org.jcvi.jillion.core.*;
import java.util.*;

public class DefaultPhdReadTagTest {

 private DefaultPhdReadTag defaultPhdReadTag;

 @Test
 public void testDefaultPhdReadTagConstructor() {
     String type = "protein";
     String source = "NC_001234.1:123-456";
     Range ungappedRange = new Range(123, 456);
     Date date = new Date("2022-01-01");
     String comment = "comment1";
     String freeFormData = "data1";

     defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

     Assert.assertEquals(type, defaultPhdReadTag.getType());
     Assert.assertEquals(source, defaultPhdReadTag.getSource());
     Assert.assertEquals(ungappedRange, defaultPhdReadTag.getUngappedRange());
     Assert.assertEquals(date, defaultPhdReadTag.getDate());
     Assert.assertEquals(comment, defaultPhdReadTag.getComment());
     Assert.assertEquals(freeFormData, defaultPhdReadTag.getFreeFormData());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefaultPhdReadTagConstructorWithNullType() {
     String source = "NC_001234.1:123-456";
     Range ungappedRange = new Range(123, 456);
     Date date = new Date("2022-01-01");
     String comment = "comment1";
     String freeFormData = "data1";

     try {
         new DefaultPhdReadTag(null, source, ungappedRange, date, comment, freeFormData);
     } catch (IllegalArgumentException e) {
         Assert.assertEquals("type cannot be null", e.getMessage());
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefaultPhdReadTagConstructorWithNullSource() {
     String type = "protein";
     Range ungappedRange = new Range(123, 456);
     Date date = new Date("2022-01-01");
     String comment = "comment1";
     String freeFormData = "data1";

     try {
         new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
     } catch (IllegalArgumentException e) {
         Assert.assertEquals("source cannot be null", e.getMessage());
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefaultPhdReadTagConstructorWithNullUngappedRange() {
     String type = "protein";
     String source = "NC_001234.1:123-456";
     Date date = new Date("2022-01-01");
     String comment = "comment1";
     String freeFormData = "data1";

     try {
         new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
     } catch (IllegalArgumentException e) {
         Assert.assertEquals("ungappedRange cannot be null", e.getMessage());
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefaultPhdReadTagConstructorWithNullDate() {
     String type = "protein";
     String source = "NC_001234.1:123-456";
     Range ungappedRange = new Range(123, 456);
     String comment = "comment1";
     String freeFormData = "data1";

     try {
         new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);
     } catch (IllegalArgumentException e) {
         Assert.assertEquals("date cannot be null", e.getMessage());
     }
 }

 @Test
 public void testDefaultPhdReadTagHashCode() {
     String type1 = "protein";
     String source1 = "NC_001234.1:123-456";
     Range ungappedRange1 = new Range(123, 456);
     Date date1 = new Date("2022-01-01");
     String comment1 = "comment1";
     String freeFormData1 = "data1";

     String type2 = "RNA";
     String source2 = "NC_001234.1:123-456";
     Range ungappedRange2 = new Range(123, 456);
     Date date2 = new Date("2022-01-01");
     String comment2 = "comment2";
     String freeFormData2 = "data2";

     DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
     DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

     int hashCode1 = defaultPhdReadTag1.hashCode();
     int hashCode2 = defaultPhdReadTag2.hashCode();

     Assert.assertNotEquals(hashCode1, hashCode2);
 }

 @Test
 public void testDefaultPhdReadTagEquals() {
     String type1 = "protein";
     String source1 = "NC_001234.1:123-456";
     Range ungappedRange1 = new Range(123, 456);
     Date date1 = new Date("2022-01-01");
     String comment1 = "comment1";
     String freeFormData1 = "data1";

     String type2 = "protein";
     String source2 = "NC_001234.1:123-456";
     Range ungappedRange2 = new Range(123, 456);
     Date date2 = new Date("2022-01-01");
     String comment2 = "comment1";
     String freeFormData2 = "data1";

     DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
     DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

     DefaultPhdReadTag defaultPhdReadTag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

     Assert.assertTrue(defaultPhdReadTag1.equals(defaultPhdReadTag3));
     Assert.assertFalse(defaultPhdReadTag1.equals(defaultPhdReadTag2));
 }

 @Test
 public void testDefaultPhdReadTagEqualsWithNull() {
     String type = "protein";
     String source = "NC_001234.1:123-456";
     Range ungappedRange = new Range(123, 456);
     Date date = new Date("2022-01-01");
     String comment = "comment1";
     String freeFormData = "data1";

     DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

     Assert.assertFalse(defaultPhdReadTag.equals(null));
 }

 @Test
 public void testDefaultPhdReadTagEqualsWithDifferentType() {
     String type1 = "protein";
     String source = "NC_001234.1:123-456";
     Range ungappedRange = new Range(123, 456);
     Date date = new Date("2022-01-01");
     String comment = "comment1";
     String freeFormData = "data1";

     String type2 = "RNA";

     DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type1, source, ungappedRange, date, comment, freeFormData);

     Assert.assertFalse(defaultPhdReadTag.equals(type2));
 }

 @Test
 public void testDefaultPhdReadTagEqualsWithDifferentSource() {
     String type = "protein";
     String source1 = "NC_001234.1:123-456";
     String source2 = "NC_001235.1:123-456";
     Range ungappedRange = new Range(123, 456);
     Date date = new Date("2022-01-01");
     String comment = "comment1";
     String freeFormData = "data1";

     DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source1, ungappedRange, date, comment, freeFormData);

     Assert.assertFalse(defaultPhdReadTag.equals(source2));
 }

 @Test
 public void testDefaultPhdReadTagEqualsWithDifferentUngappedRange() {
     String type = "protein";
     String source = "NC_001234.1:123-456";
     Range ungappedRange1 = new Range(123, 456);
     Range ungappedRange2 = new Range(122, 457);
     Date date = new Date("2022-01-01");
     String comment = "comment1";
     String freeFormData = "data1";

     DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange1, date, comment, freeFormData);

     Assert.assertFalse(defaultPhdReadTag.equals(ungappedRange2));
 }

 @Test
 public void testDefaultPhdReadTagEqualsWithDifferentDate() {
     String type = "protein";
     String source = "NC_001234.1:123-456";
     Range ungappedRange = new Range(123, 456);
     Date date1 = new Date("2022-01-01");
     Date date2 = new Date("2022-01-02");
     String comment = "comment1";
     String freeFormData = "data1";

     DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date1, comment, freeFormData);

     Assert.assertFalse(defaultPhdReadTag.equals(date2));
 }

 @Test
 public void testDefaultPhdReadTagEqualsWithDifferentComment() {
     String type = "protein";
     String source = "NC_001234.1:123-456";
     Range ungappedRange = new Range(123, 456);
     Date date = new Date("2022-01-01");
     String comment1 = "comment1";
     String comment2 = "comment2";
     String freeFormData = "data1";

     DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment1, freeFormData);

     Assert.assertFalse(defaultPhdReadTag.equals(comment2));
 }

 @Test
 public void testDefaultPhdReadTagEqualsWithDifferentFreeFormData() {
     String type = "protein";
     String source = "NC_001234.1:123-456";
     Range ungappedRange = new Range(123, 456);
     Date date = new Date("2022-01-01");
     String comment = "comment1";
     String freeFormData1 = "data1";
     String freeFormData2 = "data2";

     DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData1);

     Assert.assertFalse(defaultPhdReadTag.equals(freeFormData2));
 }
}