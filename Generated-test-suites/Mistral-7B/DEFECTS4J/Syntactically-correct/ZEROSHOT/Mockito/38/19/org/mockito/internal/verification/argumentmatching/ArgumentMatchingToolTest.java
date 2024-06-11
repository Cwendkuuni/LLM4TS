import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.verification.argumentmatching.ArgumentMatchingTool;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.internal.matchers.TypeMatches.typeMatches;
import static org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool argumentMatchingTool;

 @Before
 public void setUp() {
 argumentMatchingTool = new ArgumentMatchingTool();
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsAreSameSize() {
 List<Matcher> matchers = new ArrayList<>();
 Object[] arguments = new Object[]{"testString"};
 matchers.add(is("testString"));
 int[] expectedResult = new int[0];
 int[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertThat(result, is(expectedResult));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousMatches() {
 List<Matcher> matchers = new ArrayList<>();
 Object[] arguments = new Object[]{"testString1", "testString2"};
 matchers.add(is("testString1"));
 matchers.add(is("testString2"));
 int[] expectedResult = new int[0];
 int[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertThat(result, is(expectedResult));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_suspiciousMatch_diffTypes_toStringEquals() {
 List<Matcher> matchers = new ArrayList<>();
 Object[] arguments = new Object[]{"1", 1};
 matchers.add(is("1"));
 matchers.add(instanceOf(Integer.class));
 int[] expectedResult = new int[]{0};
 ContainsExtraTypeInformation containsExtraTypeInformation = new ContainsExtraTypeInformation(Integer.class);
 matchers.add(containsExtraTypeInformation);
 int[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertThat(result, arrayContaining(equalTo(0)));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_suspiciousMatch_diffTypes_toStringNotEquals() {
 List<Matcher> matchers = new ArrayList<>();
 Object[] arguments = new Object[]{"1", 1.0};
 matchers.add(is("1"));
 matchers.add(instanceOf(Integer.class));
 matchers.add(instanceOf(Number.class));
 int[] expectedResult = new int[]{1};
 int[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertThat(result, arrayContaining(equalTo(1)));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_suspiciousMatch_diffTypes_toStringEquals_typeDoesntMatch() {
 List<Matcher> matchers = new ArrayList<>();
 Object[] arguments = new Object[]{"1", '1'};
 matchers.add(is("1"));
 matchers.add(instanceOf(Integer.class));
 matchers.add(instanceOf(Character.class));
 int[] expectedResult = new int[]{1};
 ContainsExtraTypeInformation containsExtraTypeInformation = new ContainsExtraTypeInformation(Integer.class);
 matchers.add(containsExtraTypeInformation);
 int[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertThat(result, arrayContaining(equalTo(1)));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_suspiciousMatch_diffTypes_toStringEquals_typeMatches() {
 List<Matcher> matchers = new ArrayList<>();
 Object[] arguments = new Object[]{"1", 1L};
 matchers.add(is("1"));
 matchers.add(instanceOf(Integer.class));
 matchers.add(instanceOf(Long.class));
 int[] expectedResult = new int[]{1};
 ContainsExtraTypeInformation containsExtraTypeInformation = new ContainsExtraTypeInformation(Long.class);
 matchers.add(containsExtraTypeInformation);
 int[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertThat(result, arrayContaining(equalTo(1)));
 }
}