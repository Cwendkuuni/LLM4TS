import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.verification.ArgumentInfo;
import org.mockito.verification.ArgumentTypeInfo;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.internal.matchers.ContainsExtraTypeInformation.contains;
import static org.mockito.internal.matchers.TypeMatchers.typeEq;

@RunWith(org.mockito.runners.JUnit4.class)
public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsAreNotEqualInSize_thenReturnEmptyArray() {
     List<Matcher> matchers = new ArrayList<>();
     matchers.add(contains(is(String.class)));
     Object[] arguments = new Object[]{new Integer(1)};

     Integer[] expected = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[0], expected);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsAreEqualInSize_thenReturnSuspiciousIndexes() {
     List<Matcher> matchers = new ArrayList<>();
     matchers.add(contains(is("expectedString")));
     matchers.add(contains(is(Integer.class)));
     Object[] arguments = new Object[]{"actualString", new Integer(1)};

     Integer[] expected = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[]{0}, expected);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenStringMatcherDoesNotMatch_thenReturnSuspiciousIndex() {
     List<Matcher> matchers = new ArrayList<>();
     matchers.add(contains(is(String.class)));
     Object[] arguments = new Object[]{new Integer(1)};

     Integer[] expected = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[0], expected);

     matchers.clear();
     matchers.add(contains(is("expectedString")));
     matchers.add(contains(is(Integer.class)));
     arguments = new Object[]{"actualString", new Integer(1)};

     expected = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[]{0}, expected);

     matchers.clear();
     matchers.add(contains(is("expectedString")));
     matchers.add(contains(is(String.class)));
     arguments = new Object[]{"actualString", "actualString"};

     expected = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[0], expected);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenStringMatcherToStringEqualsButTypeDoesNotMatch_thenReturnSuspiciousIndex() {
     List<Matcher> matchers = new ArrayList<>();
     matchers.add(contains(is("expectedString")));
     matchers.add(contains(is(Integer.class)));
     Object[] arguments = new Object[]{"expectedString", new Double(1.0)};

     Integer[] expected = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[]{0}, expected);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenStringMatcherToStringEqualsAndTypeMatches_thenReturnEmptyArray() {
     List<Matcher> matchers = new ArrayList<>();
     matchers.add(contains(is("expectedString")));
     matchers.add(contains(is(String.class)));
     Object[] arguments = new Object[]{"expectedString", "expectedString"};

     Integer[] expected = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[0], expected);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenContainersMatcherToStringEqualsButTypesDontMatch_thenReturnSuspiciousIndex() {
     List<Matcher> matchers = new ArrayList<>();
     matchers.add(contains(contains(is("expectedString"))));
     matchers.add(contains(is(Integer.class)));
     Object[] arguments = new Object[]{"expectedString", new Double(1.0)};

     Integer[] expected = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[]{0}, expected);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenContainersMatcherToStringEqualsAndTypesMatch_thenReturnEmptyArray() {
     List<Matcher> matchers = new ArrayList<>();
     matchers.add(contains(contains(is("expectedString"))));
     matchers.add(contains(contains(is(String.class))));
     Object[] arguments = new Object[]{"expectedString", "expectedString"};

     Integer[] expected = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[0], expected);
 }

 private void assertArrayEquals(Integer[] expected, Integer[] actual) {
     if (!java.util.Arrays.deepEquals(expected, actual)) {
         fail("Expected [" + java.util.Arrays.toString(expected) + "], but was [" + java.util.Arrays.toString(actual) + "]");
     }
 }

 @Test
 public void testToStringEquals_whenTwoObjectsAreEqual_thenReturnTrue() {
     String string1 = "expectedString";
     String string2 = "expectedString";

     boolean result = argumentMatchingTool.toStringEquals(contains(is(String.class)), string1);
     assertTrue(result);

     result = argumentMatchingTool.toStringEquals(contains(is(String.class)), string2);
     assertTrue(result);
 }

 @Test
 public void testToStringEquals_whenTwoObjectsAreNotEqual_thenReturnFalse() {
     String string1 = "expectedString";
     String string2 = "actualString";

     boolean result = argumentMatchingTool.toStringEquals(contains(is(String.class)), string1);
     assertTrue(result);

     result = argumentMatchingTool.toStringEquals(contains(is(String.class)), string2);
     assertFalse(result);
 }

 @Test
 public void testToStringEquals_whenMatcherIsAnInstanceOfTypeInfo_thenReturnFalse() {
     ArgumentInfo argumentInfo = new ArgumentInfo(new ArgumentTypeInfo(String.class), 0);
     Matcher matcher = argumentInfo;

     boolean result = argumentMatchingTool.toStringEquals(contains(is(String.class)), matcher);
     assertFalse(result);
 }

 @Test
 public void testToStringEquals_whenTwoObjectsHaveDifferentTypes_thenReturnFalse() {
     Object object1 = "expectedString";
     Object object2 = new Integer(1);

     boolean result = argumentMatchingTool.toStringEquals(contains(is(String.class)), object1);
     assertTrue(result);

     result = argumentMatchingTool.toStringEquals(contains(is(String.class)), object2);
     assertFalse(result);
 }

 @Test
 public void testToStringEquals_whenTwoObjectsHaveSameTypesButDifferentValues_thenReturnFalse() {
     String string1 = "expectedString";
     String string2 = "actualString";

     boolean result = argumentMatchingTool.toStringEquals(contains(is(String.class)), string1);
     assertTrue(result);

     result = argumentMatchingTool.toStringEquals(contains(is(String.class)), string2);
     assertFalse(result);
 }

 @Test
 public void testSafelyMatches_whenMatcherMatchesArgument_thenReturnTrue() {
     Matcher matcher = contains(is(String.class));
     String argument = "expectedString";

     boolean result = argumentMatchingTool.safelyMatches(matcher, argument);
     assertTrue(result);
 }

 @Test
 public void testSafelyMatches_whenMatcherDoesNotMatchArgument_thenReturnFalse() {
     Matcher matcher = contains(is(String.class));
     Object argument = new Integer(1);

     boolean result = argumentMatchingTool.safelyMatches(matcher, argument);
     assertFalse(result);
 }
}