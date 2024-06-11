import org.junit.jupiter.params.JUnitParams;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.matchers.Arguments;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.verification.argumentmatching.ContainsExtraTypeInformation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ArgumentMatchingToolTest {

 private final ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

 @ParameterizedTest(name = "{index}: getSuspiciouslyNotMatchingArgsIndexes(matchers={0}, arguments={1})")
 @ArgumentsSource(SuspiciouslyNotMatchingArgsDataProvider.class)
 void testGetSuspiciouslyNotMatchingArgsIndexes(List<Matcher> matchers, Object[] arguments, int[] expected) {
     int[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(expected, actual);
 }

 static Stream<Arguments> suspiciousNotMatchingArgsDataProvider() {
     Object[] intArray = new Object[]{1, 2, 3};
     Object[] stringArray = new Object[]{"one", "two", "three"};

     List<Matcher> matchers = Arrays.asList(
         ArgumentMatchers.eq(1),
         ArgumentMatchers.instanceOf(String.class),
         ArgumentMatchers.not(ArgumentMatchers.instanceOf(Integer.class)),
         ContainsExtraTypeInformation.not(ContainsExtraTypeInformation.typeEquals(Integer[].class))
     );

     return Stream.of(
         new Object[][]{matchers, intArray, new int[]{0, 2}},
         new Object[][]{matchers, stringArray, new int[]{1, 2}},
         new Object[][]{matchers, intArray, new int[]{}},
         new Object[][]{matchers, stringArray, new int[]{}}
     );
 }

 static class SuspiciouslyNotMatchingArgsDataProvider implements ArgumentsProvider {
     @Override
     public Iterable<?> provideArguments() {
         return JUnitParams.iterables();
     }
 }

}