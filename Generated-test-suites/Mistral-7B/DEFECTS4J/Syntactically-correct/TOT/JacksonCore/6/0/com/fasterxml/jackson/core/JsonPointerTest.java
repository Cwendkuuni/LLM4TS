import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertTrue(emptyPointer.matches());
        assertNull(emptyPointer.getMatchingProperty());
        assertEquals(-1, emptyPointer.getMatchingIndex());
        assertFalse(emptyPointer.mayMatchProperty());
        assertFalse(emptyPointer.mayMatchElement());
        assertNull(emptyPointer.tail());
        assertEquals("", emptyPointer.toString());
    }

    @Test
    public void testSimplePointer() {
        JsonPointer pointer = new JsonPointer("/foo");
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/foo", pointer.toString());
    }

    @Test
    public void testPointerWithProperty() {
        String jsonString = "{\"foo\": {\"bar\": [1, 2, 3]}}";
        JsonPointer pointer = JsonPointer.compile("/foo/bar/1");
        JsonNode jsonNode = new ObjectMapper().readTree(jsonString);
        TreeNode treeNode = TreeNodeFactory.instance.constructTree(jsonNode);
        TreeNode currentNode = treeNode;
        while (pointer.matches() || pointer.mayMatchProperty()) {
            if (pointer.mayMatchProperty()) {
                currentNode = currentNode.at(pointer.getMatchingProperty());
                pointer = pointer.tail();
            }
            pointer = pointer.tail();
        }
        TreeNode expectedNode = (ArrayNode) currentNode;
        assertEquals(1, expectedNode.size());
        JsonPointer expectedPointer = new JsonPointer("/foo/bar/1");
        assertEquals(expectedPointer, pointer);
        assertEquals(1, expectedNode.get(0).asInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer pointer = JsonPointer.compile("~/foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer2() {
        JsonPointer pointer = JsonPointer.compile("foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer3() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar~1");
    }
}