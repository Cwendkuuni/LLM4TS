import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class JoinerTest {

    private Joiner joiner;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        joiner = Joiner.on("/");
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    public void testAppendTo_EmptyIterable() throws IOException {
        Joiner.Appendable appendable = Mockito.mock(Joiner.Appendable.class);
        joiner.appendTo(appendable, Collections.emptyList());
        Mockito.verify(appendable, never()).append(anyString());
    }

    @Test
    public void testAppendTo_SingleElementIterable() throws IOException {
        Joiner.Appendable appendable = Mockito.mock(Joiner.Appendable.class);
        joiner.appendTo(appendable, Arrays.asList("test"));
        Mockito.verify(appendable).append("test");
    }

    @Test
    public void testAppendTo_MultipleElementsIterable() throws IOException {
        Joiner.Appendable appendable = Mockito.mock(Joiner.Appendable.class);
        joiner.appendTo(appendable, Arrays.asList("test1", "test2", "test3"));
        Mockito.verify(appendable).append("test1");
        Mockito.verify(appendable).append("/");
        Mockito.verify(appendable).append("test2");
        Mockito.verify(appendable).append("/");
        Mockito.verify(appendable).append("test3");
        Mockito.verify(appendable, times(3)).append("/");
    }

    @Test
    public void testAppendTo_IOException() throws IOException {
        Joiner.Appendable appendable = Mockito.mock(Joiner.Appendable.class);
        Mockito.doThrow(new IOException()).when(appendable).append(anyString());
        joiner.appendTo(appendable, Arrays.asList("test"));
        Mockito.verify(appendable).append("test");
        Mockito.verify(appendable).append("/");
        try {
            joiner.appendTo(appendable, Arrays.asList("test2"));
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testAppendTo_NullIterable() {
        Joiner.Appendable appendable = Mockito.mock(Joiner.Appendable.class);
        try {
            joiner.appendTo(appendable, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testAppendTo_NullElementsIterable() throws IOException {
        Joiner.Appendable appendable = Mockito.mock(Joiner.Appendable.class);
        joiner.appendTo(appendable, Arrays.asList((Object) null));
        Mockito.verify(appendable, never()).append(anyString());
    }

    @Test
    public void testAppendTo_MixedNullAndNonNullElementsIterable() throws IOException {
        Joiner.Appendable appendable = Mockito.mock(Joiner.Appendable.class);
        joiner.appendTo(appendable, Arrays.asList("test1", null, "test3"));
        Mockito.verify(appendable).append("test1");
        Mockito.verify(appendable).append("/");
        Mockito.verify(appendable).append(outputStream.toString());
        Mockito.verify(appendable).append("/");
        Mockito.verify(appendable).append("test3");
        Mockito.verify(appendable, times(2)).append("/");
    }

    @Test
    public void testAppendTo_NullAppendable() {
        try {
            joiner.appendTo(null, Arrays.asList("test"));
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testAppendTo_NullParts() {
        Joiner.Appendable appendable = Mockito.mock(Joiner.Appendable.class);
        try {
            joiner.appendTo(appendable, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testToString_NullObject() {
        Joiner joiner = new Joiner(null);
        try {
            String result = joiner.toString(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}