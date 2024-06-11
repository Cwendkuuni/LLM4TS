import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.decorator.TShortByteMapDecorator;

public class TShortByteMapDecoratorTest {

    @Test
    public void testDefaultConstructor() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Assert.assertNotNull(decorator);
        Assert.assertNull(decorator.getMap());
    }

    @Test
    public void testConstructingWithMap() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        Assert.assertNotNull(decorator);
        Assert.assertEquals(map, decorator.getMap());
    }

    @Test(expected = NullPointerException.class)
    public void testPutWithNullKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) null, (Byte) null);
    }

    @Test(expected = NullPointerException.class)
    public void testPutWithNullValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetWithNullKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);
        decorator.get(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructingWithNullMap() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(null);
    }

    @Test(expected = IOException.class)
    public void testReadExternalWithNullInput() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        decorator.writeExternal(objectOutputStream);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        decorator.readExternal(byteArrayInputStream);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testReadExternalWithInvalidData() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject("Invalid data.");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        decorator.readExternal(byteArrayInputStream);
    }
}