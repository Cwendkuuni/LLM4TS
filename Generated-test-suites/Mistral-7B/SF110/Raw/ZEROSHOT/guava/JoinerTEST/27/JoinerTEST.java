import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JoinerTest {

 private Joiner joiner;
 private StringBuilder stringBuilder;
 private Iterator<String> stringIterator;

 @Before
 public void setUp() {
 joiner = Joiner.on(",");
 stringBuilder = new StringBuilder();
 stringIterator = Arrays.asList("apple", "banana", "orange").iterator();
 }

 @Test
 public void testAppendTo() throws IOException {
 // Test appendTo with Appendable (PrintWriter)
 PrintWriter printWriter = new PrintWriter(System.out);
 joiner.appendTo(printWriter, Arrays.asList("apple", "banana", "orange"));
 printWriter.flush();
 assertEquals("apple,banana,orange", System.out.getBuffer().toString());

 // Test appendTo with StringBuilder
 joiner.appendTo(stringBuilder, Arrays.asList("apple", "banana", "orange"));
 assertEquals("apple,banana,orange", stringBuilder.toString());

 // Test appendTo with Object array
 joiner.appendTo(stringBuilder, new Object[]{"apple", "banana", "orange"});
 assertEquals("apple,banana,orange", stringBuilder.toString());

 // Test appendTo with individual Objects
 joiner.appendTo(stringBuilder, "apple", "banana", "orange");
 assertEquals("apple,banana,orange", stringBuilder.toString());
 }

 @Test
 public void testJoin() {
 assertEquals("apple,banana,orange", joiner.join(Arrays.asList("apple", "banana", "orange")));
 assertEquals("apple,banana,orange", joiner.join(new Object[]{"apple", "banana", "orange"}));
 assertEquals("apple:banana:orange", joiner.useForNull(":").join(Arrays.asList("apple", null, "orange")));
 }

 @Test(expected = NullPointerException.class)
 public void testNullIterator() {
 joiner.appendTo(stringBuilder, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyIterable() {
 joiner.appendTo(stringBuilder, Collections.emptyList());
 }

 @Test
 public void testAppendToWithIterators() throws IOException {
 // Test appendTo with Iterator (StringBuilder)
 StringBuilder stringBuilder1 = new StringBuilder();
 joiner.appendTo(stringBuilder1, stringIterator);
 assertEquals("apple,banana,orange", stringBuilder1.toString());

 // Test appendTo with Iterator (PrintWriter)
 PrintWriter printWriter1 = new PrintWriter(System.out);
 joiner.appendTo(printWriter1, stringIterator);
 printWriter1.flush();
 assertEquals("apple,banana,orange", System.out.getBuffer().toString());
 }
}