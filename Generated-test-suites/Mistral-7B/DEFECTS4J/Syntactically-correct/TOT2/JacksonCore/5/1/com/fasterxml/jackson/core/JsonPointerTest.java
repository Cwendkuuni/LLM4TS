import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testCompileValidJsonPointer() {
        String jsonPointer1 = "/example/propertyA";
        JsonPointer expectedJsonPointer1 = new JsonPointer("/example/propertyA", null);
        JsonPointer jsonPointer1Result = JsonPointer.compile(jsonPointer1);
        assertEquals(expectedJsonPointer1._asString, jsonPointer1Result._asString);

        String jsonPointer2 = "/example/propertyA/propertyB";
        JsonPointer expectedJsonPointer2 = new JsonPointer("/example/propertyA/propertyB", null, null);
        JsonPointer jsonPointer2Result = JsonPointer.compile(jsonPointer2);
        assertEquals(expectedJsonPointer2._asString, jsonPointer2Result._asString);
    }
}