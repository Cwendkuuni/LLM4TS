package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import java.util.Date;

import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.util.test.MockDate;
import org.jcvi.jillion.core.util.test.TestUtils;
import org.junit.Test;

public class DefaultPhdReadTagTest {

	@Test
	public void testDefaultPhdReadTagCreation() {
		String type = "Type1";
		String source = "source1";
		Range ungappedRange = new Range(1, 10);
		Date date = new MockDate("2022-01-01").getDate();
		String comment = "comment1";
		String freeFormData = "freeFormData1";

		DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

		assertEquals(type, tag.getType());
		assertEquals(source, tag.getSource());
		assertEquals(ungappedRange, tag.getUngappedRange());
		assertEquals(date, tag.getDate());
		assertEquals(comment, tag.getComment());
		assertEquals(freeFormData, tag.getFreeFormData());
	}

	@Test
	public void testDefaultPhdReadTagHashCode() {
		String type = "Type1";
		String source = "source1";
		Range ungappedRange = new Range(1, 10);
		Date date = new MockDate("2022-01-01").getDate();
		String comment = "comment1";
		String freeFormData = "freeFormData1";

		DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
		DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

		assertEquals(tag1.hashCode(), tag2.hashCode());
	}

	@Test
	public void testDefaultPhdReadTagEquals() {
		String type = "Type1";
		String source = "source1";
		Range ungappedRange = new Range(1, 10);
		Date date = new MockDate("2022-01-01").getDate();
		String comment = "comment1";
		String freeFormData = "freeFormData1";

		DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
		DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

		assertEquals(tag1, tag2);
	}

	@Test(expected = AssertionError.class)
	public void testDefaultPhdReadTagNullComment() {
		String type = "Type1";
		String source = "source1";
		Range ungappedRange = new Range(1, 10);
		Date date = new MockDate("2022-01-01").getDate();
		String comment = null; // Change this to null
		String freeFormData = "freeFormData1";

		DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
	}

	@Test(expected = AssertionError.class)
	public void testDefaultPhdReadTagNullSource() {
		String type = "Type1";
		String source = null; // Change this to null
		Range ungappedRange = new Range(1, 10);
		Date date = new MockDate("2022-01-01").getDate();
		String comment = "comment1";
		String freeFormData = "freeFormData1";

		DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
	}

	@Test(expected = AssertionError.class)
	public void testDefaultPhdReadTagNullUngappedRange() {
		String type = "Type1";
		String source = "source1";
		Range ungappedRange = null; // Change this to null
		Date date = new MockDate("2022-01-01").getDate();
		String comment = "comment1";
		String freeFormData = "freeFormData1";

		DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
	}
}