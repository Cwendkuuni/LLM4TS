import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import jigl.image.ops.ConnectedComponents;
import jigl.image.types.GrayImage;
import jigl.image.types.BinaryImage;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructor() {
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        assertNotNull(cc);

        ConnectedComponents cc1 = new ConnectedComponents(2, 1);
        assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(2, 2));
        assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(-1, 0));
        assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(0, -1));
    }

    @Test
    public void testApplyMethodZeroConnected() {
        BinaryImage binaryImage = new BinaryImage(3, 3, new byte[][]{{0, 1, 1}, {1, 1, 0}, {1, 0, 1}});
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage expectedGrayImage = new GrayImage(3, 3, new Integer[]{0, 1, 1, 1, 1, 1, 1, 1, 1});
        GrayImage actualGrayImage = cc.apply(binaryImage);
        assertNotNull(actualGrayImage);
        assertArrayEquals(expectedGrayImage.getData(), actualGrayImage.getData());
    }

    @Test
    public void testApplyMethodFourConnected() {
        BinaryImage binaryImage = new BinaryImage(3, 3, new byte[][]{{0, 1, 1}, {1, 1, 0}, {1, 0, 1}});
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage expectedGrayImage = new GrayImage(3, 3, new Integer[]{0, 1, 2, 1, 2, 2, 1, 2, 2});
        GrayImage actualGrayImage = cc.apply(binaryImage);
        assertNotNull(actualGrayImage);
        assertArrayEquals(expectedGrayImage.getData(), actualGrayImage.getData());
    }

    @Test
    public void testApplyMethodEightConnected() {
        BinaryImage binaryImage = new BinaryImage(3, 3, new byte[][]{{0, 1, 1}, {1, 1, 0}, {1, 0, 1}});
        ConnectedComponents cc = new ConnectedComponents(1, 0);
        GrayImage expectedGrayImage = new GrayImage(3, 3, new Integer[]{0, 1, 1, 1, 2, 2, 1, 2, 2});
        GrayImage actualGrayImage = cc.apply(binaryImage);
        assertNotNull(actualGrayImage);
        assertArrayEquals(expectedGrayImage.getData(), actualGrayImage.getData());
    }

    @Test(expected = InvalidArgumentException.class)
    public void testApplyMethodInvalidArguments() {
        BinaryImage binaryImage = new BinaryImage(3, 3, new byte[][]{{0, 1, 1}, {1, 1, 0}, {1, 0, 1}});
        ConnectedComponents cc = new ConnectedComponents(3, 3);
        GrayImage actualGrayImage = cc.apply(binaryImage);
    }

    @Test
    public void testApplyMethodEmptyImage() {
        BinaryImage binaryImage = new BinaryImage(0, 0);
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage expectedGrayImage = new GrayImage(0, 0);
        GrayImage actualGrayImage = cc.apply(binaryImage);
        assertNotNull(actualGrayImage);
        assertArrayEquals(expectedGrayImage.getData(), actualGrayImage.getData());
    }

}