import org.junit.Test;
import org.junit.Assert;

public class ConstructorInstantiatorTest {

    @Test
    public void testConstructor() {
        Object outerInstance = new Object();
        ConstructorInstantiator constructorInst = new ConstructorInstantiator(outerInstance);
        Assert.assertNotNull(constructorInst.outerClassInstance);
        Assert.assertEquals(outerInstance, constructorInst.outerClassInstance);
    }
}