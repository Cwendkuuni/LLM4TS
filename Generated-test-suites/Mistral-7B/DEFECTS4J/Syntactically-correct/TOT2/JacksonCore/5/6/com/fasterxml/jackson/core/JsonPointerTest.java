import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {
    @Test
    public void testJsonPointerConstructor_Empty() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertNull(jsonPointer._nextSegment);
        Assert.assertEquals("", jsonPointer._asString);
        Assert.assertEquals("", jsonPointer._matchingPropertyName);
        Assert.assertEquals(-1, jsonPointer._matchingElementIndex);
    }

    @Test
    public void testJsonPointerConstructor_NonEmpty() {
        String fullString = "/foo/bar";
        String segment = "bar";
        JsonPointer next = new JsonPointer("/baz");
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, next);
        Assert.assertEquals(fullString, jsonPointer._asString);
        Assert.assertEquals(next, jsonPointer._nextSegment);
        Assert.assertEquals("bar", jsonPointer._matchingPropertyName);
        Assert.assertEquals(-1, jsonPointer._matchingElementIndex);
    }

    // Test other methods here...
}