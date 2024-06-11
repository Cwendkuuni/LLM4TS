package jigl.image.ops;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import jigl.image.types.GrayImage;
import jigl.image.ImageFactory;
import jigl.image.exceptions.InvalidArgumentException;

import org.junit.Before;
import org.junit.Test;

public class ConnectedComponentsTest {

    private ConnectedComponents cc;
    private BinaryImage image;
    private GrayImage expectedResult;

    @Before
    public void setup() {
        image = (BinaryImage) ImageFactory.binaryImage(3, 3, new byte[][] { { 1, 1, 1 }, { 1, 0, 1 }, { 1, 1, 1 } });
        cc = new ConnectedComponents(0, 0);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidArguments() {
        new ConnectedComponents(2, 2);
    }

    @Test
    public void testConnectedComponents() {
        expectedResult = (GrayImage) ImageFactory.grayImage(3, 3, new Integer[][] { { Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1) }, { Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(1) }, { Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1) } });
        assertEquals(expectedResult, cc.apply(image));
    }

    @Test
    public void testConnectedComponents1Neighbor() {
        cc = new ConnectedComponents(1, 0);
        expectedResult = (GrayImage) ImageFactory.grayImage(3, 3, new Integer[][] { { Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1) }, { Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(1) }, { Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1) } });
        assertEquals(expectedResult, cc.apply(image));
    }

    @Test
    public void testConnectedComponents2Neighbors() {
        cc = new ConnectedComponents(2, 0);
        expectedResult = (GrayImage) ImageFactory.grayImage(3, 3, new Integer[][] { { Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1) }, { Integer.valueOf(1), Integer.valueOf(3), Integer.valueOf(1) }, { Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1) } });
        assertEquals(expectedResult, cc.apply(image));
    }

    @Test
    public void testConnectedComponentsBackground() {
        image = (BinaryImage) ImageFactory.binaryImage(3, 3, new byte[][] { { 0, 1, 1 }, { 1, 0, 1 }, { 1, 1, 1 } });
        cc = new ConnectedComponents(0, 1);
        expectedResult = (GrayImage) ImageFactory.grayImage(3, 3, new Integer[][] { { Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1) }, { Integer.valueOf(0), Integer.valueOf(2), Integer.valueOf(1) }, { Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1) } });
        assertEquals(expectedResult, cc.apply(image));
    }

    @Test
    public void testConnectedComponentsEdgeCases() {
        image = (BinaryImage) ImageFactory.binaryImage(1, 1, new byte[][] { { 1 } });
        cc = new ConnectedComponents(0, 0);
        expectedResult = (GrayImage) ImageFactory.grayImage(1, 1, new Integer[][] { { Integer.valueOf(1) } });
        assertEquals(expectedResult, cc.apply(image));

        image = (BinaryImage) ImageFactory.binaryImage(1, 1, new byte[][] { { 0 } });
        cc = new ConnectedComponents(0, 0);
        expectedResult = (GrayImage) ImageFactory.grayImage(1, 1, new Integer[][] { { Integer.valueOf(0) } });
        assertEquals(expectedResult, cc.apply(image));
    }

    @Test
    public void testConnectedComponentsAllZero() {
        image = (BinaryImage) ImageFactory.binaryImage(1, 1, new byte[][] { { 0 } });
        cc = new ConnectedComponents(0, 0);
        expectedResult = (GrayImage) ImageFactory.grayImage(1, 1, new Integer[][] { { Integer.valueOf(0) } });
        assertEquals(expectedResult, cc.apply(image));
    }

    @Test
    public void testConnectedComponentsAllOne() {
        image = (BinaryImage) ImageFactory.binaryImage(1, 1, new byte[][] { { 1 } });
        cc = new ConnectedComponents(0, 0);
        expectedResult = (GrayImage) ImageFactory.grayImage(1, 1, new Integer[][] { { Integer.valueOf(1) } });
        assertEquals(expectedResult, cc.apply(image));
    }
}