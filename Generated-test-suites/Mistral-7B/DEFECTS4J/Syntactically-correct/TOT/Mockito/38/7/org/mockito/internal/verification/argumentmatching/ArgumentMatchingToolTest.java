import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.verification.argumentmatching.ContainsExtraTypeInformation;

import java.util.ArrayList;
import java.util.List;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenArgumentsAndMatchersHaveDifferentTypesButSameToString_thenReturnIndexes() {
 List<Matcher> matchers = new ArrayList<>();
 matchers.add(new ContainsExtraTypeInformation(Integer.class, "10") {
 @Override
 public String description() {
 return "contains 10";
 }
 });
 matchers.add(new ContainsExtraTypeInformation(String.class, "mockito") {
 @Override
 public String description() {
 return "contains mockito";
 }
 });
 Object[] arguments = new Object[] {1, "mockito"};
 Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[] {1}, indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenArgumentsAndMatchersHaveSameTypesButDifferentValues_thenReturnEmptyArray() {
 List<Matcher> matchers = new ArrayList<>();
 matchers.add(new ContainsExtraTypeInformation(Integer.class, 10) {
 @Override
 public String description() {
 return "contains 10";
 }
 });
 matchers.add(new ContainsExtraTypeInformation(Integer.class, 20) {
 @Override
 public String description() {
 return "contains 20";
 }
 });
 Object[] arguments = new Object[] {1, 2};
 Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[0], indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenArgumentsAndMatchersHaveSameTypesAndValues_thenReturnEmptyArray() {
 List<Matcher> matchers = new ArrayList<>();
 matchers.add(new ContainsExtraTypeInformation(Integer.class, 10) {
 @Override
 public String description() {
 return "contains 10";
 }
 });
 matchers.add(new ContainsExtraTypeInformation(Integer.class, 10) {
 @Override
 public String description() {
 return "contains 10";
 }
 });
 Object[] arguments = new Object[] {10, 10};
 Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[0], indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenArgumentsIsEmpty_thenReturnEmptyArray() {
 List<Matcher> matchers = new ArrayList<>();
 matchers.add(new ContainsExtraTypeInformation(Integer.class, 10) {
 @Override
 public String description() {
 return "contains 10";
 }
 });
 Object[] arguments = new Object[0];
 Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[0], indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersIsEmpty_thenReturnEmptyArray() {
 List<Matcher> matchers = new ArrayList<>();
 Object[] arguments = new Object[] {1, 2};
 Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[0], indexes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenArgumentsAndMatchersSizeMismatch_thenThrowIllegalArgumentException() {
 List<Matcher> matchers = new ArrayList<>();
 matchers.add(new ContainsExtraTypeInformation(Integer.class, 10) {
 @Override
 public String description() {
 return "contains 10";
 }
 });
 Object[] arguments = new Object[] {1, 2, 3};
 Assert.assertThrows(IllegalArgumentException.class, () -> tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }
}