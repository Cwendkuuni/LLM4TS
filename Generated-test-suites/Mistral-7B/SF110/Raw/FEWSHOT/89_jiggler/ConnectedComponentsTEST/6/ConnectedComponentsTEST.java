package jigl.image.ops;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

import org.junit.Test;

public class ConnectedComponentsTest {

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidArgumentException() {
        new ConnectedComponents(-2, 1);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidArgumentException2() {
        new ConnectedComponents(1, -2);
    }

    @Test
    public void testApplyWithZeroConnected() {
        BinaryImage image = new BinaryImage(5, 5);
        image.set(1, 1, 1);
        image.set(2, 1, 1);
        image.set(1, 2, 1);
        image.set(2, 2, 1);
        ConnectedComponents component = new ConnectedComponents(0, 0);
        GrayImage result = component.apply(image);

        assertArrayEquals(new Integer[]{Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1)}, result.getData());
    }

    @Test
    public void testApplyWithFourConnected() {
        BinaryImage image = new BinaryImage(5, 5);
        image.set(1, 1, 1);
        image.set(2, 1, 1);
        image.set(1, 2, 1);
        image.set(2, 2, 1);
        image.set(3, 3, 1);
        image.set(3, 4, 1);
        ConnectedComponents component = new ConnectedComponents(0, 0);
        GrayImage result = component.apply(image);

        assertArrayEquals(new Integer[]{Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1)}, result.getData());

        component = new ConnectedComponents(4, 0);
        result = component.apply(image);

        assertArrayEquals(new Integer[]{Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1)}, result.getData());
    }

    @Test
    public void testApplyWithEightConnected() {
        BinaryImage image = new BinaryImage(5, 5);
        image.set(1, 1, 1);
        image.set(2, 1, 1);
        image.set(1, 2, 1);
        image.set(2, 2, 1);
        image.set(3, 3, 1);
        image.set(3, 4, 1);
        image.set(4, 3, 1);
        image.set(4, 4, 1);
        ConnectedComponents component = new ConnectedComponents(0, 0);
        GrayImage result = component.apply(image);

        assertArrayEquals(new Integer[]{Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1)}, result.getData());

        component = new ConnectedComponents(8, 0);
        result = component.apply(image);

        assertArrayEquals(new Integer[]{Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1)}, result.getData());
    }

    @Test
    public void testApplyWithBackground() {
        BinaryImage image = new BinaryImage(5, 5);
        image.set(1, 1, 1);
        image.set(2, 1, 1);
        image.set(1, 2, 1);
        image.set(2, 2, 1);
        image.set(3, 3, 0);
        image.set(3, 4, 1);
        image.set(4, 3, 1);
        image.set(4, 4, 1);
        ConnectedComponents component = new ConnectedComponents(0, 0);
        GrayImage result = component.apply(image);

        assertArrayEquals(new Integer[]{Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1)}, result.getData());

        component = new ConnectedComponents(0, 255);
        result = component.apply(image);

        assertArrayEquals(new Integer[]{Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1)}, result.getData());
    }

    @Test
    public void testApplyWithFourConnectedAndBackground() {
        BinaryImage image = new BinaryImage(5, 5);
        image.set(1, 1, 1);
        image.set(2, 1, 1);
        image.set(1, 2, 1);
        image.set(2, 2, 1);
        image.set(3, 3, 0);
        image.set(3, 4, 1);
        image.set(4, 3, 1);
        image.set(4, 4, 1);
        ConnectedComponents component = new ConnectedComponents(4, 0);
        GrayImage result = component.apply(image);

        assertArrayEquals(new Integer[]{Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1)}, result.getData());
    }

    @Test
    public void testApplyWithEightConnectedAndBackground() {
        BinaryImage image = new BinaryImage(5, 5);
        image.set(1, 1, 1);
        image.set(2, 1, 1);
        image.set(1, 2, 1);
        image.set(2, 2, 1);
        image.set(3, 3, 0);
        image.set(3, 4, 1);
        image.set(4, 3, 1);
        image.set(4, 4, 1);
        ConnectedComponents component = new ConnectedComponents(8, 0);
        GrayImage result = component.apply(image);

        assertArrayEquals(new Integer[]{Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1)}, result.getData());
    }

    @Test
    public void testApplyWithZeroSizeImage() {
        BinaryImage image = new BinaryImage(0, 0);
        ConnectedComponents component = new ConnectedComponents(0, 0);

        assertNull(component.apply(image));
    }

}