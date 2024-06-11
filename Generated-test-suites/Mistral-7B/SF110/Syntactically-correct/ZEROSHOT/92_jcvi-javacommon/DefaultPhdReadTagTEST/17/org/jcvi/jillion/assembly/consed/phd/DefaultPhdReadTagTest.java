package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;

import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.util.DateUtil;
import org.junit.Test;

public class DefaultPhdReadTagTest {

 private static final String TYPE = "type";
 private static final String SOURCE = "source";
 private static final Range UNGAPPED_RANGE = new Range(1, 10);
 private static final Date DATE = DateUtil.parse("2022-01-01");
 private static final String COMMENT = "comment";
 private static final String FREE_FORM_DATA = "freeFormData";

 @Test
 public void testDefaultPhdReadTagConstruction() {
     DefaultPhdReadTag tag = new DefaultPhdReadTag(TYPE, SOURCE, UNGAPPED_RANGE, DATE, COMMENT, FREE_FORM_DATA);
     assertEquals(TYPE, tag.getType());
     assertEquals(SOURCE, tag.getSource());
     assertEquals(UNGAPPED_RANGE, tag.getUngappedRange());
     assertEquals(DATE, tag.getDate());
     assertEquals(COMMENT, tag.getComment());
     assertEquals(FREE_FORM_DATA, tag.getFreeFormData());
 }

 @Test
 public void testDefaultPhdReadTagHashCode() {
     DefaultPhdReadTag tag1 = new DefaultPhdReadTag(TYPE, SOURCE, UNGAPPED_RANGE, DATE, COMMENT, FREE_FORM_DATA);
     DefaultPhdReadTag tag2 = new DefaultPhdReadTag(TYPE, SOURCE, UNGAPPED_RANGE, DATE, COMMENT, FREE_FORM_DATA);
     DefaultPhdReadTag tag3 = new DefaultPhdReadTag("type2", SOURCE, UNGAPPED_RANGE, DATE, COMMENT, FREE_FORM_DATA);
     DefaultPhdReadTag tag4 = new DefaultPhdReadTag(TYPE, SOURCE, UNGAPPED_RANGE, new Date(1640995600000L), COMMENT, FREE_FORM_DATA);

     int tag1HashCode = tag1.hashCode();
     int tag2HashCode = tag2.hashCode();
     int tag3HashCode = tag3.hashCode();
     int tag4HashCode = tag4.hashCode();

     int expectedHashCode = Arrays.hashCode(new Object[] { TYPE, SOURCE, UNGAPPED_RANGE, DATE, COMMENT, FREE_FORM_DATA });

     assertEquals(expectedHashCode, tag1HashCode);
     assertEquals(expectedHashCode, tag2HashCode);
     assertNotEquals(tag1HashCode, tag3HashCode);
     assertNotEquals(tag1HashCode, tag4HashCode);
 }

 @Test
 public void testDefaultPhdReadTagEquals() {
     DefaultPhdReadTag tag1 = new DefaultPhdReadTag(TYPE, SOURCE, UNGAPPED_RANGE, DATE, COMMENT, FREE_FORM_DATA);
     DefaultPhdReadTag tag2 = new DefaultPhdReadTag(TYPE, SOURCE, UNGAPPED_RANGE, DATE, COMMENT, FREE_FORM_DATA);
     DefaultPhdReadTag tag3 = new DefaultPhdReadTag("type2", SOURCE, UNGAPPED_RANGE, DATE, COMMENT, FREE_FORM_DATA);
     DefaultPhdReadTag tag4 = new DefaultPhdReadTag(TYPE, SOURCE, UNGAPPED_RANGE, DATE, COMMENT, FREE_FORM_DATA);
     DefaultPhdReadTag tag5 = new DefaultPhdReadTag(TYPE, SOURCE, UNGAPPED_RANGE, DATE, COMMENT, "differentFreeFormData");

     assertTrue(tag1.equals(tag2));
     assertFalse(tag1.equals(tag3));
     assertTrue(tag1.equals(tag4));
     assertFalse(tag1.equals(tag5));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefaultPhdReadTagNullArguments() {
     String nullType = null;
     String nullSource = null;
     Range nullUngappedRange = null;
     Date nullDate = null;
     String nullComment = null;
     String nullFreeFormData = null;

     try {
         new DefaultPhdReadTag(nullType, nullSource, nullUngappedRange, nullDate, nullComment, nullFreeFormData);
         fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException ex) {
         // Expected
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefaultPhdReadTagEmptyArguments() {
     String emptyType = "";
     String emptySource = "";
     Range emptyUngappedRange = new Range();
     Date emptyDate = new Date();
     String emptyComment = "";
     String emptyFreeFormData = "";

     try {
         new DefaultPhdReadTag(emptyType, emptySource, emptyUngappedRange, emptyDate, emptyComment, emptyFreeFormData);
         fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException ex) {
         // Expected
     }
 }
}