import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class JoinerTest {

    private Joiner joiner;
    private Joiner joinerWithNull;
    private Joiner joinerSkippingNulls;
    private MapJoiner mapJoiner;

    @Before
    public void setup() {
        joiner = Joiner.on(",");
        joinerWithNull = joiner.useForNull("NaN");
        joinerSkippingNulls = joiner.skipNulls();
        mapJoiner = joiner.withKeyValueSeparator(":");
    }

    @Test
    public void testJoinerOnString() throws IOException {
        String[] parts = {"apple", "banana", "cherry"};
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, Arrays.asList(parts));
        String expected = "apple,banana,cherry";
        String actual = stringWriter.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinerOnCharacter() throws IOException {
        String[] parts = {"apple", "banana", "cherry"};
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, parts);
        String expected = "apple,banana,cherry";
        String actual = stringWriter.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinWithEmptyList() throws IOException {
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, Collections.emptyIterator());
        String actual = stringWriter.toString();
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testJoinWithNullIterator() throws IOException {
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, null);
    }

    @Test
    public void testJoinWithNullPart() {
        String actual = joiner.join(new Object[] {null, "apple", "banana"});
        String expected = "NaN,apple,banana";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinWithNullFirstAndSecond() {
        String actual = joiner.join(null, null, "apple", "banana");
        String expected = "NaN,apple,banana";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinWithVarargs() {
        String actual = joiner.join("apple", "banana", "cherry");
        String expected = "apple,banana,cherry";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinerWithNullText() {
        String actual = joinerWithNull.join("apple", "banana", "cherry");
        String expected = "apple,NaN,banana,NaN,cherry";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinerSkippingNulls() throws IOException {
        StringWriter stringWriter = new StringWriter();
        Object[] parts = {null, "apple", null, "banana", null, "cherry"};
        joinerSkippingNulls.appendTo(stringWriter, parts);
        String actual = stringWriter.toString();
        String expected = "apple,banana,cherry";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMapJoiner() {
        Map<String, String> map = ImmutableMap.of("apple", "red", "banana", "yellow");
        StringWriter stringWriter = new StringWriter();
        mapJoiner.appendTo(stringWriter, map.entrySet());
        String expected = "apple:red,banana:yellow";
        String actual = stringWriter.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMapJoinerWithNullValue() {
        Map<String, Object> map = ImmutableMap.of("apple", "red", "banana", null);
        String expected = "apple:red,banana:NaN";
        String actual = mapJoiner.join(map.entrySet());
        Assert.assertEquals(expected, actual);
    }
}