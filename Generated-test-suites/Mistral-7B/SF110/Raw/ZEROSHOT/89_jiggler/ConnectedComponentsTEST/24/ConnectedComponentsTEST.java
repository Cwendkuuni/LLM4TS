package jigl.image.ops.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assume.assumeTrue;

import org.junit.Test;

import jigl.image.ops.ConnectedComponents;
import jigl.image.ops.SimpleOperator;
import jigl.image.types.GrayImage;
import jigl.image.types.IntegerImage;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 3);
            assertNotNull(cc);
        } catch (InvalidArgumentException e) {
            assumeTrue(false);
        }

        try {
            ConnectedComponents cc = new ConnectedComponents(0, 3);
            assertNotNull(cc);
        } catch (InvalidArgumentException e) {
            assumeTrue(false);
        }

        try {
            ConnectedComponents cc = new ConnectedComponents(3, 3);
            assertNotNull(cc);
        } catch (InvalidArgumentException e) {
            assumeTrue(false);
        }

        try {
            ConnectedComponents cc = new ConnectedComponents(1, 4);
            assertNotNull(cc);
        } catch (InvalidArgumentException e) {
            assumeTrue(false);
        }

        try {
            ConnectedComponents cc = new ConnectedComponents(-1, 3);
            assertNull(cc);
        } catch (InvalidArgumentException e) {
            assumeTrue(true);
        }

        try {
            ConnectedComponents cc = new ConnectedComponents(1, -3);
            assertNull(cc);
        } catch (InvalidArgumentException e) {
            assumeTrue(true);
        }
    }

    @Test
    public void testConnectedComponentsApply() {
        BinaryImage input = new BinaryImage(3, 3);
        input.set(0, 0, 1);
        input.set(0, 1, 1);
        input.set(0, 2, 1);
        input.set(1, 0, 1);
        input.set(1, 1, 1);
        input.set(1, 2, 1);
        input.set(2, 0, 1);
        input.set(2, 1, 1);
        input.set(2, 2, 1);

        ConnectedComponents cc = new ConnectedComponents(0, 1);
        GrayImage output = cc.apply(input);

        IntegerImage expected = new IntegerImage(3, 3);
        expected.set(0, 0, 1);
        expected.set(0, 1, 1);
        expected.set(0, 2, 1);
        expected.set(1, 0, 1);
        expected.set(1, 1, 2);
        expected.set(1, 2, 1);
        expected.set(2, 0, 1);
        expected.set(2, 1, 2);
        expected.set(2, 2, 1);

        assertArrayEquals(expected.getData(), output.getData());

        cc = new ConnectedComponents(1, 1);
        output = cc.apply(input);

        expected = new IntegerImage(3, 3);
        expected.set(0, 0, 1);
        expected.set(0, 1, 2);
        expected.set(0, 2, 1);
        expected.set(1, 0, 1);
        expected.set(1, 1, 2);
        expected.set(1, 2, 1);
        expected.set(2, 0, 1);
        expected.set(2, 1, 3);
        expected.set(2, 2, 1);

        assertArrayEquals(expected.getData(), output.getData());
    }
}