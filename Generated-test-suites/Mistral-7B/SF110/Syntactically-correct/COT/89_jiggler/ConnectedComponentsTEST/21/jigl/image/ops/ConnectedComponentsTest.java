package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.ops.ConnectedComponents;
import jigl.image.ops.InvalidArgumentException;
import jigl.image.types.GrayImage;

import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructorWithValidArguments() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        assertNotNull(cc);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConnectedComponentsConstructorWithInvalidArguments() {
        new ConnectedComponents(2, 2);
    }

    @Test
    public void testApplyWithEmptyImage() {
        BinaryImage input = new EmptyImage(1, 1);
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage output = cc.apply(input);
        assertNotNull(output);
        assertTrue(output.isZero());
    }

    @Test
    public void testApplyWithSinglePixelConnectedComponent() {
        BinaryImage input = new ByteImage(new byte[]{1}, new int[]{0}, new int[]{0}, new int[]{1});
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage output = cc.apply(input);
        assertEquals(1, output.get(0, 0).intValue());
    }

    @Test
    public void testApplyWithMultipleConnectedComponents() {
        BinaryImage input = new ByteImage(new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 1}, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{0, 0, 1, 1, 0, 0, 1, 1, 0}, new int[]{1, 1, 0, 0, 1, 1, 0, 0, 1});
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage output = cc.apply(input);
        assertEquals(3, output.get(0, 0).intValue());
        assertEquals(4, output.get(1, 1).intValue());
        assertEquals(3, output.get(2, 2).intValue());
    }

    @Test
    public void testApplyWithConnectedComponentsTouchingEdges() {
        BinaryImage input = new ByteImage(new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 0}, new int[]{0, 0, 1, 1, 0, 0, 1, 1, 1, 0}, new int[]{1, 1, 0, 0, 1, 1, 0, 0, 1, 1});
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage output = cc.apply(input);
        assertEquals(5, output.get(0, 0).intValue());
        assertEquals(6, output.get(1, 1).intValue());
        assertEquals(5, output.get(2, 2).intValue());
    }

    @Test
    public void testApplyWithConnectedComponentsTouchingCorners() {
        BinaryImage input = new ByteImage(new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1}, new int[]{1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage output = cc.apply(input);
        assertEquals(11, output.get(0, 0).intValue());
        assertEquals(12, output.get(2, 2).intValue());
    }

    @Test
    public void testApplyWithConnectedComponentsTouchingDiagonally() {
        BinaryImage input = new ByteImage(new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new int[]{1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage output = cc.apply(input);
        assertEquals(12, output.get(0, 0).intValue());
        assertEquals(13, output.get(2, 2).intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyWithNullImage() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        cc.apply(null);
    }
}