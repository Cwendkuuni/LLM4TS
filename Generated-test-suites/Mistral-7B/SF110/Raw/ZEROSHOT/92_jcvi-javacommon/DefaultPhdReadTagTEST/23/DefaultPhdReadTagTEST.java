package org.jcvi.jillion.assembly.consed.phd.test;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.*;
import org.jcvi.jillion.assembly.consed.phd.*;
import java.util.*;
import java.util.Date;

public class DefaultPhdReadTagTest {

 @ org.junit.BeforeClass
 static void setup() {
 System.out.println("### Testing DefaultPhdReadTag class ###");
 }

 @ org.junit.AfterClass
 static void teardown() {
 System.out.println("### Testing of DefaultPhdReadTag class completed ###");
 }

 @ org.junit.Test
 public void testDefaultPhdReadTagConstruction() {
 // Create an instance of DefaultPhdReadTag using valid arguments
 String type = "protein";
 String source = "contig_1";
 Range ungappedRange = new Range(0, 100);
 Date date = new Date(System.currentTimeMillis());
 String comment = "this is a comment";
 String freeFormData = "free data";
 DefaultPhdReadTag phdTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

 // Test if the instance was created successfully
 assertEquals("protein", phdTag.getType());
 assertEquals("contig_1", phdTag.getSource());
 assertEquals(ungappedRange, phdTag.getUngappedRange());
 assertEquals(date, phdTag.getDate());
 assertEquals("this is a comment", phdTag.getComment());
 assertEquals("free data", phdTag.getFreeFormData());
 }

 @ org.junit.Test
 public void testDefaultPhdReadTagHashCode() {
 // Create instances of DefaultPhdReadTag using the same and different values
 DefaultPhdReadTag phdTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(1L), "comment1", "data1");
 DefaultPhdReadTag phdTag2 = new DefaultPhdReadTag("type2", "source2", new Range(2, 20), new Date(2L), "comment2", "data2");
 DefaultPhdReadTag phdTag3 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(1L), "comment1", "data1");

 // Test if equal objects have the same hash code
 assertEquals(phdTag1.hashCode(), phdTag3.hashCode());

 // Test if non-equal objects have different hash codes
 assertNotEquals(phdTag1.hashCode(), phdTag2.hashCode());
 }

 @ org.junit.Test
 public void testDefaultPhdReadTagEquals() {
 // Create instances of DefaultPhdReadTag using the same and different values
 DefaultPhdReadTag phdTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(1L), "comment1", "data1");
 DefaultPhdReadTag phdTag2 = new DefaultPhdReadTag("type2", "source2", new Range(2, 20), new Date(2L), "comment2", "data2");
 DefaultPhdReadTag phdTag3 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(1L), "comment1", "data1");

 // Test if equal objects are equal
 assertTrue(phdTag1.equals(phdTag3));

 // Test if non-equal objects are not equal
 assertFalse(phdTag1.equals(phdTag2));
 }

 @ org.junit.Test
 public void testDefaultPhdReadTagToString() {
 // Create an instance of DefaultPhdReadTag
 DefaultPhdReadTag phdTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(1L), "comment", "data");

 // Test if the toString method returns the expected string representation
 String expected = "[DefaultPhdReadTag type=type source=source ungappedRange=[1, 10] date=" + phdTag.getDate() + " comment=comment freeFormData=data]";
 assertEquals(expected, phdTag.toString());
 }
}