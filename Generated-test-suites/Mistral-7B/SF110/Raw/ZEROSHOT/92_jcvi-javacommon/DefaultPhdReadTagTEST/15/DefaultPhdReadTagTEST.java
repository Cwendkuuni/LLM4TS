import org.jcvi.jillion.core.Date;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.StringUtil;
import org.jcvi.jillion.exceptions.JillionRuntimeException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class DefaultPhdReadTagTest {

private DefaultPhdReadTag tag;
private String type;
 private String source;
 private Range ungappedRange;
 private Date date;
 private String comment;
 private String freeFormData;

 @Parameters
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][]{
 {"type1", "source1", new Range(1, 10), new Date(2021, 1, 1), "comment1", "freeFormData1"},
 {"type2", null, new Range(15, 20), new Date(2022, 2, 2), null, "freeFormData2"},
 {"type3", "", new Range(25, 30), new Date(2023, 3, 3), "comment3", ""}
 });
 }

 @Before
 public void setUp() {
 this.type = StringUtil.createSafeString(this.getType());
 this.source = StringUtil.createSafeString(this.getSource());
 this.ungappedRange = this.getUngappedRange();
 this.date = this.getDate();
 this.comment = StringUtil.createSafeString(this.getComment());
 this.freeFormData = StringUtil.createSafeString(this.getFreeFormData());
 this.tag = new DefaultPhdReadTag(this.type, this.source, this.ungappedRange, this.date, this.comment, this.freeFormData);
 }

 @Test
 public void testGetType() {
 assertEquals(this.type, this.tag.getType());
 }

 @Test
 public void testGetSource() {
 assertEquals(this.source, this.tag.getSource());
 }

 @Test
 public void testGetUngappedRange() {
 assertEquals(this.ungappedRange, this.tag.getUngappedRange());
 }

 @Test
 public void testGetDate() {
 assertEquals(this.date, this.tag.getDate());
 }

 @Test
 public void testGetComment() {
 assertEquals(this.comment, this.tag.getComment());
 }

 @Test
 public void testGetFreeFormData() {
 assertEquals(this.freeFormData, this.tag.getFreeFormData());
 }

 @Test(expected = JillionRuntimeException.class)
 public void testNullTypeThrowsException() {
 this.type = null;
 try {
 new DefaultPhdReadTag(this.type, this.source, this.ungappedRange, this.date, this.comment, this.freeFormData);
 } catch (JillionRuntimeException e) {
 assertEquals("PhdReadTag type cannot be null.", e.getMessage());
 throw e;
 }
 }

 @Test(expected = JillionRuntimeException.class)
 public void testNullSourceThrowsException() {
 this.source = null;
 try {
 new DefaultPhdReadTag(this.type, this.source, this.ungappedRange, this.date, this.comment, this.freeFormData);
 } catch (JillionRuntimeException e) {
 assertEquals("PhdReadTag source cannot be null.", e.getMessage());
 throw e;
 }
 }

 @Test(expected = JillionRuntimeException.class)
 public void testNullUngappedRangeThrowsException() {
 this.ungappedRange = null;
 try {
 new DefaultPhdReadTag(this.type, this.source, this.ungappedRange, this.date, this.comment, this.freeFormData);
 } catch (JillionRuntimeException e) {
 assertEquals("PhdReadTag ungapped range cannot be null.", e.getMessage());
 throw e;
 }
 }

 @Test(expected = JillionRuntimeException.class)
 public void testNullDateThrowsException() {
 this.date = null;
 try {
 new DefaultPhdReadTag(this.type, this.source, this.ungappedRange, this.date, this.comment, this.freeFormData);
 } catch (JillionRuntimeException e) {
 assertEquals("PhdReadTag date cannot be null.", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testHashCode() {
 DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2021, 1, 1), "comment1", "freeFormData1");
 DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type2", null, new Range(15, 20), new Date(2022, 2, 2), null, "freeFormData2");
 DefaultPhdReadTag tag3 = new DefaultPhdReadTag("type3", "", new Range(25, 30), new Date(2023, 3, 3), "comment3", "");
 int tag1HashCode = tag1.hashCode();
 int tag2HashCode = tag2.hashCode();
 int tag3HashCode = tag3.hashCode();
 int expected = 31 * (31 * ((tag1.getType() == null) ? 0 : tag1.getType().hashCode()) + tag1.getSource().hashCode())
 + (31 * ((tag1.getUngappedRange() == null) ? 0 : tag1.getUngappedRange().hashCode()) + tag1.getDate().hashCode())
 + (31 * ((tag1.getComment() == null) ? 0 : tag1.getComment().hashCode()) + tag1.getFreeFormData().hashCode());
 assertEquals(expected, tag1HashCode);
 assertNotSame(tag1HashCode, tag2HashCode);
 assertNotSame(tag1HashCode, tag3HashCode);
 }

 @Test
 public void testEquals() {
 DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2021, 1, 1), "comment1", "freeFormData1");
 DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2021, 1, 1), "comment1", "freeFormData1");
 DefaultPhdReadTag tag3 = new DefaultPhdReadTag("type2", "source1", new Range(1, 10), new Date(2021, 1, 1), "comment1", "freeFormData1");
 DefaultPhdReadTag tag4 = new DefaultPhdReadTag("type1", "source1", new Range(1, 11), new Date(2021, 1, 1), "comment1", "freeFormData1");
 DefaultPhdReadTag tag5 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2021, 1, 1), null, "freeFormData1");
 DefaultPhdReadTag tag6 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), null, "comment1", "freeFormData1");
 DefaultPhdReadTag tag7 = new DefaultPhdReadTag("type1", null, new Range(1, 10), new Date(2021, 1, 1), "comment1", "freeFormData1");
 DefaultPhdReadTag tag8 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2021, 1, 1), "comment1", null);
 DefaultPhdReadTag tag9 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2021, 1, 1), null, null);
 DefaultPhdReadTag tag10 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2022, 2, 2), "comment1", "freeFormData1");

 assertTrue(tag1.equals(tag1));
 assertTrue(tag1.equals(tag2));
 assertFalse(tag1.equals(tag3));
 assertFalse(tag1.equals(tag4));
 assertFalse(tag1.equals(tag5));
 assertFalse(tag1.equals(tag6));
 assertFalse(tag1.equals(tag7));
 assertFalse(tag1.equals(tag8));
 assertFalse(tag1.equals(tag9));
 assertFalse(tag1.equals(tag10));
 }

 private String getType() {
 return type;
 }

 private String getSource() {
 return source;
 }

 private Range getUngappedRange() {
 return ungappedRange;
 }

 private Date getDate() {
 return date;
 }

 private String getComment() {
 return comment;
 }

 private String getFreeFormData() {
 return freeFormData;
 }
}