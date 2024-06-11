import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JsonTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TestCreatorPropertyName extends JsonTest {

    @Test
    public void testName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        CreatorProperty property = new CreatorProperty(
            PropertyName.construct("testPropertyName"),
            JavaType.construct(String.class),
            PropertyName.construct("testWrapperName"),
            null,
            null,
            null,
            0,
            null,
            null
        );
        String expectedName = "testPropertyName";
        String resultName = property.getName();
        assertEquals(expectedName, resultName);
    }
}