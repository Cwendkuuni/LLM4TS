package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.functors.Equator;
import org.apache.commons.collections4.seq.EditScript;
import org.apache.commons.collections4.seq.SequenceComparator;
import org.apache.commons.collections4.seq.EditScript.Command;

public class SequencesComparatorTest {

	@SuppressWarnings("unchecked")
	@org.junit.Test
	public void testSequencesComparator() {
		// Test case 1: Sequences are equal
		List<Integer> sequence1 = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
		List<Integer> sequence2 = new ArrayList<Integer>(sequence1);
		SequenceComparator<Integer> comparator = new SequenceComparator<Integer>(sequence1, sequence2);
		EditScript<Integer> script = comparator.getScript();
		assertTrue(script.isEmpty());

		// Test case 2: Sequences are reverse of each other
		List<String> sequence1_reverse = new ArrayList<String>(Arrays.asList("abc", "def"));
		List<String> sequence2 = new ArrayList<String>(Arrays.asList("def", "abc"));
		SequenceComparator<String> comparator_reverse = new SequenceComparator<String>(sequence1_reverse, sequence2);
		EditScript<String> script_reverse = comparator_reverse.getScript();
		List<Command<String>> commands_reverse = script_reverse.getCommands();
		assertEquals(commands_reverse.size(), 2);
		assertEquals(commands_reverse.get(0).getClass(), InsertCommand.class);
		assertEquals(commands_reverse.get(1).getClass(), DeleteCommand.class);
		assertEquals(commands_reverse.get(0).getObject(), "def");
		assertEquals(commands_reverse.get(1).getObject(), "abc");

		// Test case 3: Sequences are not equal and have common elements
		List<Integer> sequence1_common = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
		List<Integer> sequence2_common = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 5, 6));
		SequenceComparator<Integer> comparator_common = new SequenceComparator<Integer>(sequence1_common, sequence2_common);
		EditScript<Integer> script_common = comparator_common.getScript();
		List<Command<Integer>> commands_common = script_common.getCommands();
		assertFalse(script_common.isEmpty());
		assertEquals(commands_common.size(), 3);
		assertEquals(commands_common.get(0).getClass(), DeleteCommand.class);
		assertEquals(commands_common.get(0).getObject(), 3);
		assertEquals(commands_common.get(1).getClass(), InsertCommand.class);
		assertEquals(commands_common.get(1).getObject(), 5);
		assertEquals(commands_common.get(2).getClass(), InsertCommand.class);
		assertEquals(commands_common.get(2).getObject(), 6);

		// Test case 4: Sequences are not equal and have no common elements
		List<Integer> sequence1_noCommon = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
		List<Integer> sequence2_noCommon = new ArrayList<Integer>(Arrays.asList(6, 7, 8, 9, 10));
		SequenceComparator<Integer> comparator_noCommon = new SequenceComparator<Integer>(sequence1_noCommon, sequence2_noCommon);
		EditScript<Integer> script_noCommon = comparator_noCommon.getScript();
		List<Command<Integer>> commands_noCommon = script_noCommon.getCommands();
		assertFalse(script_noCommon.isEmpty());
		assertEquals(commands_noCommon.size(), 5);
		assertEquals(commands_noCommon.get(0).getClass(), DeleteCommand.class);
		assertEquals(commands_noCommon.get(0).getObject(), 1);
		assertEquals(commands_noCommon.get(1).getClass(), InsertCommand.class);
		assertEquals(commands_noCommon.get(1).getObject(), 6);
		assertEquals(commands_noCommon.get(2).getClass(), InsertCommand.class);
		assertEquals(commands_noCommon.get(2).getObject(), 7);
		assertEquals(commands_noCommon.get(3).getClass(), InsertCommand.class);
		assertEquals(commands_noCommon.get(3).getObject(), 8);
		assertEquals(commands_noCommon.get(4).getClass(), InsertCommand.class);
		assertEquals(commands_noCommon.get(4).getObject(), 9);

		// Test case 5: Sequences are not equal and have different length
		List<Integer> sequence1_length = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
		List<Integer> sequence2_length = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
		SequenceComparator<Integer> comparator_length = new SequenceComparator<Integer>(sequence1_length, sequence2_length);
		EditScript<Integer> script_length = comparator_length.getScript();
		List<Command<Integer>> commands_length = script_length.getCommands();
		assertFalse(script_length.isEmpty());
		assertEquals(commands_length.size(), 2);
		assertEquals(commands_length.get(0).getClass(), DeleteCommand.class);
		assertEquals(commands_length.get(0).getObject(), 5);
		assertEquals(commands_length.get(1).getClass(), InsertCommand.class);
		assertEquals(commands_length.get(1).getObject(), 5);

		// Test equals and hashCode methods
		List<Integer> sequence1_equalHashCode = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
		List<Integer> sequence2_equalHashCode = new ArrayList<Integer>(sequence1_equalHashCode);
		SequenceComparator<Integer> comparator_equalHashCode = new SequenceComparator<Integer>(sequence1_equalHashCode, sequence2_equalHashCode);
		List<Integer> sequence3_equalHashCode = new ArrayList<Integer>(sequence1_equalHashCode);
		SequenceComparator<Integer> comparator_equalHashCode2 = new SequenceComparator<Integer>(sequence3_equalHashCode, sequence2_equalHashCode);
		assertTrue(comparator_equalHashCode.equals(comparator_equalHashCode2));
		assertTrue(comparator_equalHashCode.hashCode() == comparator_equalHashCode2.hashCode());

		List<Integer> sequence1_notEqualHashCode = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
		List<Integer> sequence2_notEqualHashCode = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 5));
		SequenceComparator<Integer> comparator_notEqualHashCode = new SequenceComparator<Integer>(sequence1_notEqualHashCode, sequence2_notEqualHashCode);
		List<Integer> sequence3_notEqualHashCode = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
		SequenceComparator<Integer> comparator_notEqualHashCode2 = new SequenceComparator<Integer>(sequence3_notEqualHashCode, sequence2_notEqualHashCode);
		assertFalse(comparator_notEqualHashCode.equals(comparator_notEqualHashCode2));
		assertFalse(comparator_notEqualHashCode.hashCode() == comparator_notEqualHashCode2.hashCode());

		// Test case for empty sequences
		List<Integer> sequence1_empty = Collections.emptyList();
		List<Integer> sequence2_empty = Collections.emptyList();
		SequenceComparator<Integer> comparator_empty = new SequenceComparator<Integer>(sequence1_empty, sequence2_empty);
		EditScript<Integer> script_empty = comparator_empty.getScript();
		assertTrue(script_empty.isEmpty());

		// Test case for null sequences
		List<Integer> sequence1_null = null;
		List<Integer> sequence2_null = null;
		try {
			new SequenceComparator<Integer>(sequence1_null, sequence2_null);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@SuppressWarnings("unchecked")
	@org.junit.Test
	public void testDefaultEquator() {
		Equator<Integer> equator = DefaultEquator.defaultEquator();
		assertTrue(equator.equate(1, 1));
		assertTrue(equator.equate(null, null));
		assertFalse(equator.equate(1, 2));
		assertFalse(equator.equate("a", 1));
		assertFalse(equator.equate(1, "a"));
	}
}