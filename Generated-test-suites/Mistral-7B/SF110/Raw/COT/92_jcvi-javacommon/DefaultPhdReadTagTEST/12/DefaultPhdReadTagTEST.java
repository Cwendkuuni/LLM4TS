import org.junit.Test;
import static org.junit.Assert.*;
import org.jcvi.jillion.core.*;
import java.util.*;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.assembly.consed.phd.PhdReadTag;

public class DefaultPhdReadTagTest {

 private DefaultPhdReadTag createDefaultPhdReadTag(String type, String source, Range ungappedRange, Date date, String comment, String freeFormData) {
 return new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 }

 @Test
 public void testGetType() {
 DefaultPhdReadTag phdReadTag = createDefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(2022, 1, 1), "Comment1", "FreeFormData1");
 String actualType = phdReadTag.getType();
 String expectedType = "Type1";
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetSource() {
 DefaultPhdReadTag phdReadTag = createDefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(2022, 1, 1), "Comment1", "FreeFormData1");
 String actualSource = phdReadTag.getSource();
 String expectedSource = "Source1";
 assertEquals(expectedSource, actualSource);
 }

 @Test
 public void testGetUngappedRange() {
 DefaultPhdReadTag phdReadTag = createDefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(2022, 1, 1), "Comment1", "FreeFormData1");
 Range actualUngappedRange = phdReadTag.getUngappedRange();
 Range expectedUngappedRange = new Range(1, 10);
 assertEquals(expectedUngappedRange, actualUngappedRange);
 }

 @Test
 public void testGetDate() {
 DefaultPhdReadTag phdReadTag = createDefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(2022, 1, 1), "Comment1", "FreeFormData1");
 Date actualDate = phdReadTag.getDate();
 Date expectedDate = new Date(2022, 1, 1);
 assertEquals(expectedDate, actualDate);
 }

 @Test
 public void testGetComment() {
 DefaultPhdReadTag phdReadTag = createDefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(2022, 1, 1), "Comment1", "FreeFormData1");
 String actualComment = phdReadTag.getComment();
 String expectedComment = "Comment1";
 assertEquals(expectedComment, actualComment);
 }

 @Test
 public void testGetFreeFormData() {
 DefaultPhdReadTag phdReadTag = createDefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(2022, 1, 1), "Comment1", "FreeFormData1");
 String actualFreeFormData = phdReadTag.getFreeFormData();
 String expectedFreeFormData = "FreeFormData1";
 assertEquals(expectedFreeFormData, actualFreeFormData);
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeWithNullComment() {
 DefaultPhdReadTag phdReadTag = createDefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(2022, 1, 1), null, "FreeFormData1");
 int actualHashCode = phdReadTag.hashCode();
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeWithNullDate() {
 DefaultPhdReadTag phdReadTag = createDefaultPhdReadTag("Type1", "Source1", new Range(1, 10), null, "Comment1", "FreeFormData1");
 int actualHashCode = phdReadTag.hashCode();
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeWithNullFreeFormData() {
 DefaultPhdReadTag phdReadTag = createDefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(2022, 1, 1), "Comment1", null);
 int actualHashCode = phdReadTag.hashCode();
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeWithNullSource() {
 DefaultPhdReadTag phdReadTag = createDefaultPhdReadTag("Type1", null, new Range(1, 10), new Date(2022, 1, 1), "Comment1", "FreeFormData1");
 int actualHashCode = phdReadTag.hashCode();
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeWithNullType() {
 DefaultPhdReadTag phdReadTag = createDefaultPhdReadTag(null, "Source1", new Range(1, 10), new Date(2022, 1, 1), "Comment1", "FreeFormData1");
 int actualHashCode = phdReadTag.hashCode();
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeWithNullUngappedRange() {
 DefaultPhdReadTag phdReadTag = createDefaultPhdReadTag("Type1", "Source1", null, new Date(2022, 1, 1), "Comment1", "FreeFormData1");
 int actualHashCode = phdReadTag.hashCode();
 }

 @Test
 public void testEqualsWithSameObject() {
 DefaultPhdReadTag phdReadTag1 = createDefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(2022, 1, 1), "Comment1", "FreeFormData1");
 DefaultPhdReadTag phdReadTag2 = phdReadTag1;
 boolean actualEquals = phdReadTag1.equals(phdReadTag2);
 boolean expectedEquals = true;
 assertEquals(expectedEquals, actualEquals);
 }

 @Test
 public void testEqualsWithDifferentType() {
 DefaultPhdReadTag phdReadTag1 = createDefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(2022, 1, 1), "Comment1", "FreeFormData1");
 Object obj = new Object();
 boolean actualEquals = phdReadTag1.equals(obj);
 boolean expectedEquals = false;
 assertEquals(expectedEquals, actualEquals);
 }

 @Test
 public void testEqualsWithDifferentSource() {
 DefaultPhdReadTag phdReadTag1 = createDefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(2022, 1, 1), "Comment1", "FreeFormData1");
 DefaultPhdReadTag phdReadTag2 = createDefaultPhdReadTag("Type1", "Source2", new Range(1, 10), new Date(2022, 1, 1), "Comment1", "FreeFormData1");
 boolean actualEquals = phdReadTag1.equals(phdReadTag2);
 boolean expectedEquals = false;
 assertEquals(expectedEquals, actualEquals);
 }

 @Test
 public void testEqualsWithDifferentUngappedRange() {
 DefaultPhdReadTag phdReadTag1 = createDefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(2022, 1, 1), "Comment1", "FreeFormData1");
 DefaultPhdReadTag phdReadTag2 = createDefaultPhdReadTag("Type1", "Source1", new Range(2, 11), new Date(2022, 1, 1), "Comment1", "FreeFormData1");
 boolean actualEquals = phdReadTag1.equals(phdReadTag2);
 boolean expectedEquals = false;
 assertEquals(expectedEquals, actualEquals);
 }

 @Test
 public void testEqualsWithDifferentDate() {
 DefaultPhdReadTag phdReadTag1 = createDefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(2022, 1, 1), "Comment1", "FreeFormData1");
 DefaultPhdReadTag phdReadTag2 = createDefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(2022, 2, 1), "Comment1", "FreeFormData1");
 boolean actualEquals = phdReadTag1.equals(phdReadTag2);
 boolean expectedEquals = false;
 assertEquals(expectedEquals, actualEquals);
 }

 @Test
 public void testEqualsWithDifferentComment() {
 DefaultPhdReadTag phdReadTag1 = createDefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(2022, 1, 1), "Comment1", "FreeFormData1");
 DefaultPhdReadTag phdReadTag2 = createDefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(2022, 1, 1), "Comment2", "FreeFormData1");
 boolean actualEquals = phdReadTag1.equals(phdReadTag2);
 boolean expectedEquals = false;
 assertEquals(expectedEquals, actualEquals);
 }

 @Test
 public void testEqualsWithDifferentFreeFormData() {
 DefaultPhdReadTag phdReadTag1 = createDefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(2022, 1, 1), "Comment1", "FreeFormData1");
 DefaultPhdReadTag phdReadTag2 = createDefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(2022, 1, 1), "Comment1", "FreeFormData2");
 boolean actualEquals = phdReadTag1.equals(phdReadTag2);
 boolean expectedEquals = false;
 assertEquals(expectedEquals, actualEquals);
 }

 @Test
 public void testEqualsWithSameValues() {
 DefaultPhdReadTag phdReadTag1 = createDefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(2022, 1, 1), "Comment1", "FreeFormData1");
 DefaultPhdReadTag phdReadTag2 = createDefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(2022, 1, 1), "Comment1", "FreeFormData1");
 boolean actualEquals = phdReadTag1.equals(phdReadTag2);
 boolean expectedEquals = true;
 assertEquals(expectedEquals, actualEquals);
 }

}