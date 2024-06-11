import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

    @Test
    public void testNumberSerializerInstance() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        assertNotNull(serializer);
    }

    @Test
    public void testNumberSerializerWithCustomType() {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        assertNotNull(serializer);
    }
}