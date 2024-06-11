package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.*;
import org.jcvi.jillion.test.RangeTest;
import org.jcvi.jillion.test.TestHelper;
import org.jcvi.jillion.test.date.DateTest;
import org.junit.*;

public class DefaultPhdReadTagTest {

 private DefaultPhdReadTag tag;

 @Before
 public void setUp() {
 String type = "testType";
 String source = "testSource";
 Range ungappedRange = new Range(1, 10);
 Date date = DateTest.parse("2022-01-01");
 String comment = "testComment";
 String freeFormData = "testFreeFormData";
 tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
 }

 @Test
 public void testGetType() {
 String expectedType = "testType";
 String actualType = tag.getType();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetSource() {
 String expectedSource = "testSource";
 String actualSource = tag.getSource();
 assertEquals(expectedSource, actualSource);
 }

 @Test
 public void testGetUngappedRange() {
 Range expectedUngappedRange = new Range(1, 10);
 Range actualUngappedRange = tag.getUngappedRange();
 RangeTest.assertEquals(expectedUngappedRange, actualUngappedRange);
 }

 @Test
 public void testGetDate() {
 Date expectedDate = DateTest.parse("2022-01-01");
 Date actualDate = tag.getDate();
 DateTest.assertEquals(expectedDate, actualDate);
 }

 @Test
 public void testGetComment() {
 String expectedComment = "testComment";
 String actualComment = tag.getComment();
 assertEquals(expectedComment, actualComment);
 }

 @Test
 public void testGetFreeFormData() {
 String expectedFreeFormData = "testFreeFormData";
 String actualFreeFormData = tag.getFreeFormData();
 assertEquals(expectedFreeFormData, actualFreeFormData);
 }

 @Test
 public void testHashcode() {
 DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", null, null, null, null);
 DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type2", "source2", null, null, null, null);
 int expectedHashcode1 = 1;
 int expectedHashcode2 = 2;
 int actualHashcode1 = tag1.hashCode();
 int actualHashcode2 = tag2.hashCode();
 assertNotEquals(expectedHashcode1, expectedHashcode2);
 tag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 1), null, null, null);
 tag2 = new DefaultPhdReadTag("type1", "source1", new Range(2, 2), null, null, null);
 int actualHashcode1_1 = tag1.hashCode();
 int actualHashcode2_1 = tag2.hashCode();
 assertNotEquals(actualHashcode1_1, actualHashcode2_1);
 }

 @Test
 public void testEquals() {
 DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", null, null, null, null);
 DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", null, null, null, null);
 assertTrue(tag1.equals(tag2));
 DefaultPhdReadTag tag3 = new DefaultPhdReadTag("type1", "source2", null, null, null, null);
 assertFalse(tag1.equals(tag3));
 DefaultPhdReadTag tag4 = new DefaultPhdReadTag("type1", "source1", new Range(1, 1), null, null, null);
 tag3 = new DefaultPhdReadTag("type1", "source1", new Range(1, 1), null, null, null);
 assertTrue(tag4.equals(tag3));
 tag4 = new DefaultPhdReadTag("type1", "source1", new Range(2, 2), null, null, null);
 assertFalse(tag4.equals(tag3));
 }

 @Test
 public void testEquals_null() {
 DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", null, null, null, null);
 assertFalse(tag.equals(null));
 }

 @Test
 public void testEquals_otherClass() {
 DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", null, null, null, null);
 assertFalse(tag.equals(new Object()));
 }
}