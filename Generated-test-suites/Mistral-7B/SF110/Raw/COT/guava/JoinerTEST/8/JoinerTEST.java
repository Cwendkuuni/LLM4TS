import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JoinerTest {

    @Test
    public void testSimpleJoining() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, Lists.newArrayList("apple", "banana", "cherry"));
        Assert.assertEquals("apple,banana,cherry", stringWriter.toString());
    }

    @Test
    public void testJoiningWithNulls_NoNullText() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, Arrays.asList(null, "banana", "cherry"));
        Assert.assertEquals("banana,cherry", stringWriter.toString());
    }

    @Test
    public void testJoiningWithNulls_WithNullText() throws IOException {
        Joiner joiner = Joiner.on(",").useForNull("NA");
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, Arrays.asList(null, "banana", "cherry"));
        Assert.assertEquals("NA,banana,cherry", stringWriter.toString());
    }

    @Test
    public void testJoiningEmptyCollection() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, Collections.emptyList());
        Assert.assertEquals("", stringWriter.toString());
    }

    @Test
    public void testJoiningWithIterableAndAppendable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, Arrays.asList("apple", "banana", "cherry").iterator());
        Assert.assertEquals("apple,banana,cherry", stringWriter.toString());
    }

    @Test
    public void testJoiningWithIterableAndStringBuilder() {
        Joiner joiner = Joiner.on(",");
        StringBuilder stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, Arrays.asList("apple", "banana", "cherry").iterator());
        Assert.assertEquals("apple,banana,cherry", stringBuilder.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testNullWriter() throws IOException {
        Joiner joiner = Joiner.on(",");
        joiner.appendTo(null, Arrays.asList("apple", "banana", "cherry").iterator());
    }

    @Test(expected = NullPointerException.class)
    public void testNullIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        joiner.appendTo(new StringWriter(), null);
    }

    @Test
    public void testJoiningMapEntries() throws IOException {
        Joiner joiner = Joiner.on(" => ");
        StringWriter stringWriter = new StringWriter();
        Map<String, String> map = Maps.newHashMap();
        map.put("key1", "value1");
        map.put("key2", "value2");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(" => ");
        mapJoiner.appendTo(stringWriter, map.entrySet());
        Assert.assertEquals("key1 => value1, key2 => value2", stringWriter.toString());
    }

    @Test
    public void testJoiningMapEntriesWithNullValues() throws IOException {
        Joiner joiner = Joiner.on(" => ");
        StringWriter stringWriter = new StringWriter();
        Map<String, String> map = Maps.newHashMap();
        map.put("key1", null);
        map.put("key2", "value2");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(" => ").useForNull("NA");
        mapJoiner.appendTo(stringWriter, map.entrySet());
        Assert.assertEquals("key1 => NA, key2 => value2", stringWriter.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testNullMap() throws IOException {
        Joiner joiner = Joiner.on(" => ");
        StringWriter stringWriter = new StringWriter();
        mapJoiner.appendTo(stringWriter, null);
    }
}