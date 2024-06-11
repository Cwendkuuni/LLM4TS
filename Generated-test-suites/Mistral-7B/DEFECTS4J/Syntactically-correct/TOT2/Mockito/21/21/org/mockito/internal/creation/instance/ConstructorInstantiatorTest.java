import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import static org.junit.Assert.assertEquals;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance() throws Exception {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Class<String> stringClass = String.class;
        String instance = constructorInstantiator.newInstance(stringClass);
        assertEquals("", instance);
    }
}