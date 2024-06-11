import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.util.DefaultIndentationOutput;
import com.fasterxml.jackson.core.util.DupDetector;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonWriteContextTest {

    private JsonWriteContext rootContext;
    private JsonWriteContext arrayContext;
    private JsonWriteContext objectContext;
    private DupDetector dupDetector;

    @Before
    public void setUp() throws JsonProcessingException {
        dupDetector = new DupDetector();
        rootContext = JsonWriteContext.createRootContext(dupDetector);
        arrayContext = rootContext.createChildArrayContext();
        objectContext = rootContext.createChildObjectContext();
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        String name = "testField";
        int status = objectContext.writeFieldName(name);
        assertEquals("Unexpected status code", JsonWriteContext.STATUS_OK_AFTER_COLON, status);
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteDuplicateFieldName() throws JsonProcessingException {
        String name = "testField";
        objectContext.writeFieldName(name);
        objectContext.writeFieldName(name);
    }

    @Test
    public void testWriteValueInRootContext() throws JsonProcessingException {
        objectContext.setCurrentValue("testValue");
        int status = rootContext.writeValue();
        assertEquals("Unexpected status code", JsonWriteContext.STATUS_OK_AFTER_SPACE, status);
    }

    @Test
    public void testWriteValueInArrayContext() throws JsonProcessingException {
        int[] values = new int[]{1, 2, 3};
        arrayContext.setCurrentValue(values);
        int status = arrayContext.writeValue();
        assertEquals("Unexpected status code", JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
    }

    @Test
    public void testWriteValueInObjectContext() throws JsonProcessingException {
        String key = "testKey";
        Object value = new Object(){};
        int status = objectContext.writeFieldName(key);
        objectContext.setCurrentValue(value);
        status = objectContext.writeValue();
        assertEquals("Unexpected status code", JsonWriteContext.STATUS_OK_AFTER_COLON, status);
    }

    @Test
    public void testGetParentContext() throws JsonProcessingException {
        JsonWriteContext grandChildContext = objectContext.createChildObjectContext();
        assertEquals("Unexpected parent context", objectContext, grandChildContext.getParent());
    }

    @Test
    public void testToString() throws IOException {
        rootContext.appendDesc(new DefaultIndentationOutput(new StringBuilder(64), 0, 2));
        String expectedRootContext = "[root]";
        assertEquals("Unexpected root context representation", expectedRootContext, rootContext.toString());

        arrayContext.appendDesc(new DefaultIndentationOutput(new StringBuilder(64), 2, 2));
        String expectedArrayContext = "[0]";
        assertEquals("Unexpected array context representation", expectedArrayContext, arrayContext.toString());

        objectContext.appendDesc(new DefaultIndentationOutput(new StringBuilder(64), 4, 2));
        String expectedObjectContext = "{testField: ?}";
        assertEquals("Unexpected object context representation", expectedObjectContext, objectContext.toString());
    }
}