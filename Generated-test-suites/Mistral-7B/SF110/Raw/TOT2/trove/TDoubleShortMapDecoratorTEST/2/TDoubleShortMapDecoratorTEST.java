import org.junit.Assert;
import org.junit.Test;
import gnu.trove.decorator.TDoubleShortMapDecorator;
import gnu.trove.map.TDoubleShortMap;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testTDoubleShortMapDecoratorEmpty() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        Assert.assertEquals(0, decorator.size());
        Assert.assertNull(decorator.get(null));
        Assert.assertNull(decorator.get(2.0));
    }

    // ...

    @Test
    public void testTDoubleShortMapDecoratorWithMap() {
        TDoubleShortMap map = new TDoubleShortMap();
        map.put(1.0, (short)1);
        map.put(2.0, (short)2);
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        Assert.assertEquals(2, decorator.size());
        Assert.assertEquals((short)1, decorator.get(1.0));
        Assert.assertEquals((short)2, decorator.get(2.0));
        Assert.assertNull(decorator.get(null));
        Assert.assertNull(decorator.get(3.0));
    }

    // Edge cases and exception handling test cases will be added here
}