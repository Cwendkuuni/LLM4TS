import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.JUnit4;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;

@RunWith(JUnit4.class)
public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool;

 @Before
 public void setup() {
 tool = new ArgumentMatchingTool();
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyList() {
 List<Matcher> matchers = new ArrayList<>();
 Object[] arguments = new Object[0];
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertNull(result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_InvalidSize() {
 List<Matcher> matchers = new ArrayList<>();
 matchers.add(Mockito.mock(Matcher.class));
 Object[] arguments = new Object[0];
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertNull(result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspiciousMatch() {
 List<Matcher> matchers = new ArrayList<>();
 matchers.add(Mockito.mock(Matcher.class));
 Object[] arguments = new Object[]{1, 2, "three"};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousMatch() {
 List<Matcher> matchers = new ArrayList<>();
 matchers.add(Mockito.mock(ContainsExtraTypeInformation.class));
 matchers.add(Mockito.mock(Matcher.class));
 Object[] arguments = new Object[]{1, "one", 2.0};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[]{1}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousMatch_MultipleMatches() {
 List<Matcher> matchers = new ArrayList<>();
 matchers.add(Mockito.mock(ContainsExtraTypeInformation.class));
 matchers.add(Mockito.mock(Matcher.class));
 matchers.add(Mockito.mock(Matcher.class));
 Object[] arguments = new Object[]{1, "one", 2.0, "two"};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 List<Integer> expected = Arrays.asList(0, 3);
 assertArrayEquals(expected.toArray(new Integer[0]), result);
 }

 @Test
 public void testToStringEquals_SameStringRepresentation_DifferentTypes() {
 Matcher m1 = Mockito.mock(Matcher.class);
 Matcher m2 = Mockito.mock(ContainsExtraTypeInformation.class);
 when(m1.toString()).thenReturn("Matcher1");
 when(m2.toString()).thenReturn("Matcher1");
 StringDescription description1 = new StringDescription();
 StringDescription description2 = new StringDescription();
 description1.appendValue("Matcher1");
 description2.appendValue("Object1");
 when(m1.matches(Mockito.any())).thenReturn(true);
 when(m2.matches(Mockito.any())).thenReturn(false);
 when(m2.typeMatches(Mockito.any())).thenReturn(false);

 boolean result = tool.toStringEquals(m1, new Object() {});
 assertFalse(result);
 }

 @Test
 public void testToStringEquals_SameStringRepresentation_SameType() {
 Matcher m1 = Mockito.mock(Matcher.class);
 Matcher m2 = Mockito.mock(Matcher.class);
 when(m1.toString()).thenReturn("Matcher1");
 when(m2.toString()).thenReturn("Matcher1");
 when(m1.matches(Mockito.any())).thenReturn(true);
 when(m2.matches(Mockito.any())).thenReturn(true);

 boolean result = tool.toStringEquals(m1, m2);
 assertTrue(result);
 }

 @Test
 public void testSafelyMatches_Matches() {
 Matcher m = Mockito.mock(Matcher.class);
 Object arg = "test";
 when(m.matches(Mockito.any())).thenReturn(true);

 boolean result = tool.safelyMatches(m, arg);
 assertTrue(result);
 }

 @Test
 public void testSafelyMatches_DoesNotMatch() {
 Matcher m = Mockito.mock(Matcher.class);
 Object arg = new Object();
 when(m.matches(Mockito.any())).thenReturn(false);

 boolean result = tool.safelyMatches(m, arg);
 assertFalse(result);
 }
}