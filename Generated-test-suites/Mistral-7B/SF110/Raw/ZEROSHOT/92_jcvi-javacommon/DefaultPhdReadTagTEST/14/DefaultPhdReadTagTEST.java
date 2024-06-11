package org.jcvi.jillion.assembly.consed.phd.test;

import org.jcvi.jillion.core.*;
import org.jcvi.jillion.assembly.consed.phd.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

 @Test
 public void testDefaultPhdReadTagInstance() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2022, 1, 1), "comment1", "freeFormData1");
 PhdReadTag phdReadTag = defaultPhdReadTag;

 assertEquals("type1", phdReadTag.getType());
 assertEquals("source1", phdReadTag.getSource());
 assertEquals(new Range(1, 10), phdReadTag.getUngappedRange());
 assertEquals(new Date(2022, 1, 1), phdReadTag.getDate());
 assertEquals("comment1", phdReadTag.getComment());
 assertEquals("freeFormData1", phdReadTag.getFreeFormData());
 }

 @Test
 public void testDefaultPhdReadTagHashCode() {
 DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2022, 1, 1), "comment1", "freeFormData1");
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("type2", "source2", new Range(2, 20), new Date(2023, 2, 2), "comment2", "freeFormData2");

 int hashCode1 = defaultPhdReadTag1.hashCode();
 int hashCode2 = defaultPhdReadTag2.hashCode();

 assertNotEquals(hashCode1, hashCode2);
 }

 @Test
 public void testDefaultPhdReadTagEquals() {
 DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2022, 1, 1), "comment1", "freeFormData1");
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2022, 1, 1), "comment1", "freeFormData1");
 DefaultPhdReadTag defaultPhdReadTag3 = new DefaultPhdReadTag("type1", "source2", new Range(1, 10), new Date(2022, 1, 1), "comment1", "freeFormData1");

 assertTrue(defaultPhdReadTag1.equals(defaultPhdReadTag2));
 assertFalse(defaultPhdReadTag1.equals(defaultPhdReadTag3));
 }

 @Test
 public void testDefaultPhdReadTagEqualsWithNull() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2022, 1, 1), "comment1", "freeFormData1");

 assertFalse(defaultPhdReadTag.equals(null));
 }

 @Test
 public void testDefaultPhdReadTagEqualsWithDifferentClass() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2022, 1, 1), "comment1", "freeFormData1");
 String testString = "test";

 assertFalse(defaultPhdReadTag.equals(testString));
 }
}