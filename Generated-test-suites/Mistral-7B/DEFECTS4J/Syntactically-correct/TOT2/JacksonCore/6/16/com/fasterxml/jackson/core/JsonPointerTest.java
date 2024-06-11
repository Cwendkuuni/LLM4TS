// JsonPointerTest.java (Expert 1)

import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest_Expert1 {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        Assert.assertTrue(jsonPointer.matches());
        Assert.assertNull(jsonPointer.getMatchingProperty());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        Assert.assertNull(jsonPointer.tail());
        Assert.assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithProperty() {
        JsonPointer jsonPointer = new JsonPointer("/propertyName", null);
        Assert.assertFalse(jsonPointer.matches());
        Assert.assertEquals("propertyName", jsonPointer.getMatchingProperty());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        Assert.assertEquals(jsonPointer, jsonPointer.tail());
        Assert.assertEquals("/propertyName", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithIndex() {
        JsonPointer jsonPointer = new JsonPointer("/array/0", null);
        Assert.assertFalse(jsonPointer.matches());
        Assert.assertNull(jsonPointer.getMatchingProperty());
        Assert.assertEquals(0, jsonPointer.getMatchingIndex());
        Assert.assertEquals(jsonPointer, jsonPointer.tail());
        Assert.assertEquals("/array/0", jsonPointer.toString());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidJsonPointer() {
        JsonPointer jsonPointer = new JsonPointer("invalidJsonPointer", null);
    }
}