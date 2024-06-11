package jigl.image.ops;

import static org.junit.Assert.*;

import jigl.image.types.*;
import jigl.image.*;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponents() {
        BinaryImage binaryImage = new BinaryImage(5, 5);
        binaryImage.set(1, 1, 1);
        binaryImage.set(1, 2, 1);
        binaryImage.set(1, 3, 1);
        binaryImage.set(1, 4, 1);
        binaryImage.set(2, 1, 1);
        binaryImage.set(2, 2, 1);
        binaryImage.set(2, 3, 1);
        binaryImage.set(2, 4, 1);
        binaryImage.set(3, 1, 1);
        binaryImage.set(3, 2, 1);
        binaryImage.set(3, 3, 1);
        binaryImage.set(3, 4, 1);
        binaryImage.set(4, 1, 1);
        binaryImage.set(4, 2, 1);
        binaryImage.set(4, 3, 1);
        binaryImage.set(4, 4, 1);
        binaryImage.set(5, 1, 1);
        binaryImage.set(5, 2, 1);
        binaryImage.set(5, 3, 1);
        binaryImage.set(5, 4, 1);
        binaryImage.set(5, 5, 1);

        ConnectedComponents cc = new ConnectedComponents(0, 1);
        GrayImage result = cc.apply(binaryImage);

        assertEquals(11, result.X());
        assertEquals(11, result.Y());

        for (int i = 0; i < 11; ++i) {
            for (int j = 0; j < 11; ++j) {
                int expected = i + 1;
                int actual = result.get(i, j).intValue();
                assertEquals(expected, actual);
            }
        }

        ConnectedComponents cc1 = new ConnectedComponents(1, 1);
        GrayImage result1 = cc1.apply(binaryImage);

        assertEquals(11, result1.X());
        assertEquals(11, result1.Y());

        for (int i = 0; i < 11; ++i) {
            for (int j = 0; j < 11; ++j) {
                int expected = findComponent(binaryImage, i, j);
                int actual = result1.get(i, j).intValue();
                assertEquals(expected, actual);
            }
        }
    }

    private int findComponent(BinaryImage image, int x, int y) {
        if (image.get(x, y) == 1) {
            return 1;
        }

        int label = 0;
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{x, y, label});

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int cx = current[0];
            int cy = current[1];
            int cl = current[2];

            image.set(cx, cy, cl);

            for (int dx = -1; dx <= 1; ++dx) {
                for (int dy = -1; dy <= 1; ++dy) {
                    if (dx == 0 && dy == 0) continue;
                    int nx = cx + dx;
                    int ny = cy + dy;

                    if (isValidPixel(image, nx, ny)) {
                        int nlabel = image.get(nx, ny).intValue();
                        if (nlabel == 0) {
                            image.set(nx, ny, cl);
                            stack.push(new int[]{nx, ny, cl});
                            label = cl;
                        } else if (nlabel != cl) {
                            return nlabel;
                        }
                    }
                }
            }
        }

        return label;
    }

    private boolean isValidPixel(BinaryImage image, int x, int y) {
        return x >= 0 && x < image.X() && y >= 0 && y < image.Y();
    }
}