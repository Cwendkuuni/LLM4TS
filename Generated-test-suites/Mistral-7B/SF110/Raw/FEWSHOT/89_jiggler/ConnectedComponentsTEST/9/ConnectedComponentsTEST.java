package jigl.image.ops;

import static org.junit.Assert.*;
import jigl.image.types.*;
import org.junit.Test;

import static jigl.image.ops.ConnectedComponents.FOUR_CONNECTED;
import static jigl.image.ops.ConnectedComponents.EIGHT_CONNECTED;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsWithFourNeighborsAndBackgroundZero() throws Exception {
        BinaryImage inputImage = new ByteBinaryImage(new int[][]{
                {0, 0, 0, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0},
                {0, 1, 1, 1, 1, 1, 1, 0},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {0, 1, 1, 1, 1, 1, 1, 0},
                {0, 0, 0, 1, 1, 1, 0, 0}
        });

        ConnectedComponents connectedComponents = new ConnectedComponents(FOUR_CONNECTED, 0);
        GrayImage outputImage = connectedComponents.apply(inputImage);

        int[] expectedOutput = new int[]{
                1, 1, 1, 2, 2, 2, 1, 1,
                1, 1, 1, 2, 2, 2, 2, 1,
                1, 1, 2, 2, 2, 2, 2, 1,
                2, 2, 2, 3, 3, 3, 2, 2,
                2, 2, 3, 3, 3, 3, 3, 2,
                2, 2, 3, 3, 3, 3, 3, 2,
                1, 1, 2, 3, 3, 3, 2, 1,
                0, 0, 0, 3, 3, 3, 0, 0
        };

        for (int i = 0; i < outputImage.X(); i++) {
            for (int j = 0; j < outputImage.Y(); j++) {
                assertEquals(expectedOutput[i * outputImage.X() + j], outputImage.get(i, j).intValue());
            }
        }
    }

    @Test
    public void testConnectedComponentsWithFourNeighborsAndBackgroundOne() throws Exception {
        BinaryImage inputImage = new ByteBinaryImage(new int[][]{
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1}
        });

        ConnectedComponents connectedComponents = new ConnectedComponents(FOUR_CONNECTED, 1);
        GrayImage outputImage = connectedComponents.apply(inputImage);

        int[] expectedOutput = new int[]{
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1
        };

        for (int i = 0; i < outputImage.X(); i++) {
            for (int j = 0; j < outputImage.Y(); j++) {
                assertEquals(expectedOutput[i * outputImage.X() + j], outputImage.get(i, j).intValue());
            }
        }
    }

    @Test
    public void testConnectedComponentsWithEightNeighborsAndBackgroundZero() throws Exception {
        BinaryImage inputImage = new ByteBinaryImage(new int[][]{
                {0, 0, 0, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0},
                {0, 1, 1, 1, 1, 1, 1, 0},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {0, 1, 1, 1, 1, 1, 1, 0},
                {0, 0, 0, 1, 1, 1, 0, 0}
        });

        ConnectedComponents connectedComponents = new ConnectedComponents(EIGHT_CONNECTED, 0);
        GrayImage outputImage = connectedComponents.apply(inputImage);

        int[] expectedOutput = new int[]{
                1, 1, 1, 2, 2, 2, 1, 1,
                1, 1, 1, 2, 2, 2, 2, 1,
                1, 1, 2, 2, 2, 2, 2, 1,
                2, 2, 2, 3, 3, 3, 2, 2,
                2, 2, 3, 3, 3, 3, 3, 2,
                2, 2, 3, 3, 3, 3, 3, 2,
                1, 1, 2, 3, 3, 3, 2, 1,
                0, 0, 0, 3, 3, 3, 0, 0
        };

        for (int i = 0; i < outputImage.X(); i++) {
            for (int j = 0; j < outputImage.Y(); j++) {
                assertEquals(expectedOutput[i * outputImage.X() + j], outputImage.get(i, j).intValue());
            }
        }
    }

    @Test
    public void testConnectedComponentsWithEightNeighborsAndBackgroundOne() throws Exception {
        BinaryImage inputImage = new ByteBinaryImage(new int[][]{
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1}
        });

        ConnectedComponents connectedComponents = new ConnectedComponents(EIGHT_CONNECTED, 1);
        GrayImage outputImage = connectedComponents.apply(inputImage);

        int[] expectedOutput = new int[]{
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1
        };

        for (int i = 0; i < outputImage.X(); i++) {
            for (int j = 0; j < outputImage.Y(); j++) {
                assertEquals(expectedOutput[i * outputImage.X() + j], outputImage.get(i, j).intValue());
            }
        }
    }
}