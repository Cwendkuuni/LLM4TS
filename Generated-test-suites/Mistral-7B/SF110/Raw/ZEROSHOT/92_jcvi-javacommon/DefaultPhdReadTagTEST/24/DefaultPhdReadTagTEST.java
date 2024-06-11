import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DefaultPhdReadTagTest {

 @Test
 public void testDefaultPhdReadTagConstruction() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date("2022-01-01"), "comment", "freeFormData");
 assertEquals("type", defaultPhdReadTag.getType());
 assertEquals("source", defaultPhdReadTag.getSource());
 assertEquals(new Range(1, 10), defaultPhdReadTag.getUngappedRange());
 assertEquals(new Date("2022-01-01"), defaultPhdReadTag.getDate());
 assertEquals("comment", defaultPhdReadTag.getComment());
 assertEquals("freeFormData", defaultPhdReadTag.getFreeFormData());
 }

 @Test
 public void testDefaultPhdReadTagGetters() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date("2022-01-01"), "comment", "freeFormData");
 assertEquals("type", defaultPhdReadTag.getType());
 assertEquals("source", defaultPhdReadTag.getSource());
 assertEquals(new Range(1, 10), defaultPhdReadTag.getUngappedRange());
 assertEquals(new Date("2022-01-01"), defaultPhdReadTag.getDate());
 assertEquals("comment", defaultPhdReadTag.getComment());
 assertEquals("freeFormData", defaultPhdReadTag.getFreeFormData());
 }

 @Test
 public void testDefaultPhdReadTagHashCode() {
 DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("type2", "source2", new Range(1, 10), new Date("2022-01-01"), "comment2", "freeFormData2");
 assertNotEquals(defaultPhdReadTag1.hashCode(), defaultPhdReadTag2.hashCode());
 }

 @Test
 public void testDefaultPhdReadTagEquals() {
 DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
 DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
 assertTrue(defaultPhdReadTag1.equals(defaultPhdReadTag2));

 DefaultPhdReadTag defaultPhdReadTag3 = new DefaultPhdReadTag("type2", "source2", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
 assertFalse(defaultPhdReadTag1.equals(defaultPhdReadTag3));
 }

 @Test
 public void testDefaultPhdReadTagEqualsNull() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date("2022-01-01"), "comment", "freeFormData");
 assertFalse(defaultPhdReadTag.equals(null));
 }

 @Test
 public void testDefaultPhdReadTagEqualsDifferentClass() {
 DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date("2022-01-01"), "comment", "freeFormData");
 assertFalse(defaultPhdReadTag.equals("string"));
 }
}