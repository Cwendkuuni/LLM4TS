import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.decorator.TDoubleShortMapDecorator;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testEmptyConstructor() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Assert.assertTrue(decorator.isEmpty());
        Assert.assertNull(decorator.getMap());
    }

    @Test
    public void testEmptyMap() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        Assert.assertEquals(decorator.size(), 0);
        Assert.assertTrue(decorator.isEmpty());
    }
}