import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class StdDelegatingSerializerTest {
    @Test
    public void testStdDelegatingSerializerConstructor() {
        Converter<Object,?> converter = new Converter<Object, Object>() {
            @Override
            public Object convert(Object source) {
                return source;
            }
        };
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        assertNotNull(serializer);
    }
}