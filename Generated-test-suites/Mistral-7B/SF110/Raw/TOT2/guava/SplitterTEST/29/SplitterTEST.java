import static org.junit.Assert.*;
import com.google.common.base.Splitter;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SplitterTest {
  @Test
  public void testOnCharSeparator() {
    Splitter splitter = Splitter.on('-');
    String input = "apple-banana-orange-grape";
    List<String> expectedOutput = Arrays.asList("apple", "banana", "orange", "grape");
    List<String> output = new ArrayList<>();
    for (String token : splitter.split(input)) {
      output.add(token);
    }
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testEmptyString() {
    Splitter splitter = Splitter.on('-');
    List<String> output = new ArrayList<>();
    List<String> expectedOutput = Collections.emptyList();
    for (String token : splitter.split("")) {
      output.add(token);
    }
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testNullInput() {
    Splitter splitter = Splitter.on('-');
    splitter.split(null);
  }

  @Test
  public void testSeparatorNotPresent() {
    Splitter splitter = Splitter.on('-');
    String input = "applebananaorangegrape";
    List<String> output = new ArrayList<>();
    List<String> expectedOutput = Arrays.asList("apple", "banana", "orange", "grape");
    for (String token : splitter.split(input)) {
      output.add(token);
    }
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testOnStringSeparator() {
    // Test for on(String separator) method here
  }

  @Test
  public void testOnPatternSeparator() {
    // Test for on(Pattern separatorPattern) method here
  }

  @Test
  public void testFixedLength() {
    // Test for fixedLength(int length) method here
  }

  @Test
  public void testOmitEmptyStrings() {
    // Test for omitEmptyStrings() method here
  }

  @Test
  public void testLimit() {
    // Test for limit(int limit) method here
  }

  @Test
  public void testTrimResults() {
    // Test for trimResults() method here
  }

  @Test
  public void testTrimResultsWithCustomTrimmer() {
    // Test for trimResults(CharMatcher trimmer) method here
  }

  @Test
  public void testSplit() {
    // Test for split(CharSequence sequence) method here
  }

  @Test
  public void testSplitToList() {
    // Test for splitToList(CharSequence sequence) method here
  }

  @Test
  public void testWithKeyValueSeparator() {
    // Test for withKeyValueSeparator(String separator) method here
  }

  @Test
  public void testWithKeyValueSeparatorWithCustomSeparator() {
    // Test for withKeyValueSeparator(char separator) method here
  }

  @Test
  public void testWithKeyValueSeparatorWithCustomSplitter() {
    // Test for withKeyValueSeparator(Splitter keyValueSplitter) method here
  }
}