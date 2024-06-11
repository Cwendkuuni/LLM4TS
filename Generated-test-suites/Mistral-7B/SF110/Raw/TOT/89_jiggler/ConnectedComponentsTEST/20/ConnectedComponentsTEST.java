import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;

import jigl.image.ops.ConnectedComponents;
import jigl.image.ops.SimpleOperator;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

import org.junit.Test;

public class ConnectedComponentsTest {
    @Test
    public void testConnectedComponentsFourNeighbors() {
        try {
            SimpleOperator op = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            GrayImage image = new GrayImage(5, 5);
            image.set(1, 1, 1);
            image.set(2, 1, 1);
            image.set(1, 2, 1);
            image.set(2, 2, 1);
            image.set(3, 1, 1);
            image.set(3, 2, 1);
            image.set(1, 3, 1);
            image.set(2, 3, 1);
            image.set(3, 3, 1);
            image.set(4, 4, 1);
            image.set(5, 4, 1);
            image.set(4, 5, 1);
            image.set(5, 5, 1);

            GrayImage result = op.apply(image);

            // Check the result has the correct dimensions
            assertThat(result.X(), is(5));
            assertThat(result.Y(), is(5));

            // Check all connected components have unique labels
            for (int x = 0; x < result.X(); ++x) {
                for (int y = 0; y < result.Y(); ++y) {
                    int label = result.get(x, y).intValue();
                    int prevLabel = (x > 0 && y > 0) ? result.get(x - 1, y - 1).intValue() : 0;
                    assertThat(label, is(not(is(prevLabel))));
                }
            }

            // Check all background pixels are labeled 0
            for (int x = 0; x < image.X(); ++x) {
                for (int y = 0; y < image.Y(); ++y) {
                    if (image.get(x, y) == 0) {
                        int label = result.get(x, y).intValue();
                        assertThat(label, is(is(0)));
                    }
                }
            }

            // Check all non-background pixels have labels greater than 0
            for (int x = 0; x < image.X(); ++x) {
                for (int y = 0; y < image.Y(); ++y) {
                    if (image.get(x, y) != 0) {
                        int label = result.get(x, y).intValue();
                        assertThat(label, is(greaterThan(0)));
                    }
                }
            }

            // Check invalid arguments
            try {
                new ConnectedComponents(-1, 0);
                fail("Expected InvalidArgumentException");
            } catch (InvalidArgumentException e) {
            }

            try {
                new ConnectedComponents(0, -1);
                fail("Expected InvalidArgumentException");
            } catch (InvalidArgumentException e) {
            }

            try {
                new ConnectedComponents(3, 0);
                fail("Expected InvalidArgumentException");
            } catch (InvalidArgumentException e) {
            }

            try {
                new ConnectedComponents(0, 3);
                fail("Expected InvalidArgumentException");
            } catch (InvalidArgumentException e) {
            }
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}