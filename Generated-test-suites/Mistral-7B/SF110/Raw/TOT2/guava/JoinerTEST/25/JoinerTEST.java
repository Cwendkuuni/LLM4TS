import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class JoinerTest {
    private Joiner joiner;
    private Appendable appendable;
    private Iterator<String> iterator;

    @Before
    public void setUp() {
        joiner = Joiner.on(":");
        appendable = mock(Appendable.class);
        iterator = Lists.newArrayList("one", "two", "three").iterator();
    }

    @Test
    public void testAppendToWithIterableAndIterator() throws IOException {
        when(appendable.append(joiner.toString(iterator.next()))).thenReturn(appendable);
        when(appendable.append(joiner.separator)).thenReturn(appendable);

        joiner.appendTo(appendable, iterator);

        assertEquals("one:two:three", appendable.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendToWithNullAppendable() {
        joiner.appendTo(null, iterator);
    }

    @Test(expected = NullPointerException.class)
    public void testAppendToWithNullIterator() {
        joiner.appendTo(appendable, null);
    }

    @Test
    public void testAppendToWithEmptyIterable() throws IOException {
        joiner.appendTo(appendable, Lists.newArrayList().iterator());
        assertEquals("", appendable.toString());
    }

    @Test(expected = IOException.class)
    public void testAppendToWithIOException() throws IOException {
        when(appendable.append(anyString())).thenThrow(new IOException());
        joiner.appendTo(appendable, iterator);
    }

    @Test
    public void testAppendToWithLargeInputData() throws IOException {
        long maxLength = 10000;
        List<String> list = Lists.newArrayList();
        for (int i = 0; i < maxLength; i++) {
            list.add("large" + i);
        }

        joiner.appendTo(appendable, list.iterator());
        assertEquals(maxLength * (joiner.separator.length() + 1) + 1, appendable.toString().length());
    }
}