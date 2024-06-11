package org.junit.juporter.test.sequencesComparator;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.sequence.EditScript;
import org.apache.commons.collections4.sequence.SequencesComparator;

public class SequencesComparatorTest {

@Test
public void testGetScriptMethod() {
// Given
List<Integer> sequence1 = ListUtils.nCopies(5, 1);
List<Integer> sequence2 = ListUtils.nCopies(5, 2);
SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

// When
EditScript<Integer> script = comparator.getScript();

// Then
assertEquals(25, script.size());
for (int i = 0; i < script.size(); i++) {
if (i < 5) {
assertTrue(script.getCommand(i) instanceof InsertCommand<Integer>);
assertEquals(2, ((InsertCommand<Integer>) script.getCommand(i)).getObject());
} else {
assertTrue(script.getCommand(i) instanceof DeleteCommand<Integer>);
assertEquals(1, ((DeleteCommand<Integer>) script.getCommand(i)).getObject());
}
}
}

@Test
public void testConstructors() {
// Given
List<Integer> sequence1 = ListUtils.nCopies(5, 1);
List<Integer> sequence2 = ListUtils.nCopies(5, 2);

// When
SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

// Then
assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, comparator.vDown);
assertArrayEquals(new int[]{15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1}, comparator.vUp);
}

@Test
public void testGetMiddleSnakeMethod() {
// Given
List<Integer> sequence1 = ListUtils.nCopies(8, 1);
List<Integer> sequence2 = ListUtils.nCopies(8, 2);

// When
SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
Snake middleSnake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());

// Then
assertNotNull(middleSnake);
assertEquals(3, middleSnake.getDiag());
assertEquals(5, middleSnake.getStart());
assertEquals(7, middleSnake.getEnd());
}
}