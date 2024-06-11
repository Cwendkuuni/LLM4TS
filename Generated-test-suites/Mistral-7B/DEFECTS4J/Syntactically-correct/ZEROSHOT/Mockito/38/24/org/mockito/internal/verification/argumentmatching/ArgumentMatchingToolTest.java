import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.internal.matchers.Arguments.argThat;
import static org.mockito.internal.matchers.Arguments.isA;

@RunWith(MockitoJUnitRunner.class)
public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyList_returnsEmptyArray() {
 List<Matcher> matchers = Arrays.asList();
 Object[] arguments = new Object[0];
 Integer[] expected = new Integer[0];
 assertArrayEquals(expected, argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_differentSizes_returnsEmptyArray() {
 List<Matcher> matchers = Arrays.asList(isA(Integer.class));
 Object[] arguments = new Object[0];
 Integer[] expected = new Integer[0];
 assertArrayEquals(expected, argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));

 matchers = Arrays.asList(isA(Integer.class), isA(String.class));
 arguments = new Object[]{1, "test"};
 expected = new Integer[0];
 assertArrayEquals(expected, argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousMatches_returnsEmptyArray() {
 List<Matcher> matchers = Arrays.asList(
 isA(Integer.class),
 isA(Integer.class),
 isA(Integer.class)
 );
 Object[] arguments = new Object[]{1, 1, 1};
 Integer[] expected = new Integer[0];
 assertArrayEquals(expected, argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_suspiciousMatch_returnsIndex() {
 List<Matcher> matchers = Arrays.asList(
 new TypeSafeMatcher<Object>() {
 @Override
 public boolean matchesSafely(Object item) {
 return item instanceof String;
 }

 @Override
 public void describeTo(StringDescription description) {
 description.appendText("a String");
 }

 @Override
 public String toString() {
 return "isA(String)";
 }
 },
 isA(Integer.class),
 isA(Integer.class)
 );
 Object[] arguments = new Object[]{"test", 1, 1};
 Integer[] expected = new Integer[]{1};
 assertArrayEquals(expected, argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_multipleSuspiciousMatches_returnsAllIndices() {
 List<Matcher> matchers = Arrays.asList(
 new ContainsExtraTypeInformation("String") {
 @Override
 public boolean matchesSafely(Object item) {
 return item.getClass().getName().startsWith("java.lang.String");
 }
 },
 new ContainsExtraTypeInformation("Integer") {
 @Override
 public boolean matchesSafely(Object item) {
 return item instanceof Number && item.getClass().isPrimitive();
 }
 },
 isA(Integer.class),
 isA(Integer.class)
 );
 Object[] arguments = new Object[]{"test", 1, 1, 1, new String("test"), 1, (byte) 1};
 Integer[] expected = new Integer[]{0, 6};
 assertArrayEquals(expected, argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }
}