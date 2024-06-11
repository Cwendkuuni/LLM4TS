import org.junit.Test;
import static org.junit.Assert.assertNull;

public class StdTypeResolverBuilderTest {

    @Test
    public void testEmptyConstructor() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getDefaultImpl());
    }
}