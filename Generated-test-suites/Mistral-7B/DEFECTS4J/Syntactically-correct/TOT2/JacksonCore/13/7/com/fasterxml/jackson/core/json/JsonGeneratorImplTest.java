import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    @Test
    public void testJsonGeneratorImplConstructor() throws IOException {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, Feature.STANDARD_FEATURES.asInt(), null);
        assertTrue(jsonGeneratorImpl instanceof JsonGeneratorImpl);
        assertEquals(Feature.STANDARD_FEATURES, jsonGeneratorImpl.getFeatures());
    }
}