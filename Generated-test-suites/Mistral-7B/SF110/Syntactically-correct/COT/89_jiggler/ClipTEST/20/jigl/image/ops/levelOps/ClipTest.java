import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.SimpleOperator;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

    @Test
    public void testConstructorWithIntMinAndIntMax() {
        Clip clip = new Clip(5, 10);
        assertEquals(5, clip.int_min);
        assertEquals(10, clip.int_max);
    }

    @Test
    public void testConstructorWithFloatMinAndFloatMax() {
        Clip clip = new Clip(2.5f, 7.5f);
        assertEquals(2.5f, clip.float_min, 0.01f);
        assertEquals(7.5f, clip.float_max, 0.01f);
    }

    @Test
    public void testApplyWithGrayImageAndROI_intMinAndIntMax() {
        GrayImage image = new ByteGrayImage(new int[][]{
                {11, 2, 3, 4},
                {12, 5, 6, 7},
                {13, 8, 9, 10},
                {14, 11, 12, 13}
        }, 4, 4);
        ROI roi = new ROI(1, 1, 3, 3);
        Clip clip = new Clip(0, 5);

        Image result = clip.apply(image, roi);

        ByteGrayImage expected = new ByteGrayImage(new int[][]{
                {1, 1, 1, 1},
                {1, 2, 2, 2},
                {1, 3, 3, 3},
                {1, 1, 1, 1}
        }, 4, 4);

        assertEquals(expected, result);
    }

    @Test
    public void testApplyWithRealGrayImageAndROI_floatMinAndFloatMax() {
        RealGrayImage image = new FloatGrayImage(new float[][]{
                {11.5f, 2.3f, 3.6f, 4.1f},
                {12.5f, 5.6f, 6.7f, 7.2f},
                {13.5f, 8.8f, 9.1f, 10.2f},
                {14.5f, 11.3f, 12.4f, 13.5f}
        }, 4, 4);
        ROI roi = new ROI(1, 1, 3, 3);
        Clip clip = new Clip(2.0f, 6.0f);

        Image result = clip.apply(image, roi);

        FloatGrayImage expected = new FloatGrayImage(new float[][]{
                {2.0f, 2.3f, 2.3f, 2.0f},
                {2.3f, 3.6f, 3.6f, 3.6f},
                {2.3f, 3.9f, 3.9f, 3.9f},
                {2.0f, 2.0f, 2.0f, 2.0f}
        }, 4, 4);

        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyWithNullImage() {
        Clip clip = new Clip(0, 10);
        Image image = null;

        clip.apply(image, new ROI(0, 0, 3, 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyWithNullROI() {
        Clip clip = new Clip(0, 10);
        GrayImage image = new ByteGrayImage(new int[][]{
                {1, 2, 3, 4},
                {1, 2, 3, 4},
                {1, 2, 3, 4},
                {1, 2, 3, 4}
        }, 4, 4);

        clip.apply(image, null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testApplyWithInvalidROI() {
        Clip clip = new Clip(0, 10);
        GrayImage image = new ByteGrayImage(new int[][]{
                {1, 2, 3, 4},
                {1, 2, 3, 4},
                {1, 2, 3, 4},
                {1, 2, 3, 4}
        }, 4, 4);

        ROI roi = new ROI(4, 4, 3, 3);

        clip.apply(image, roi);
    }

    @Test
    public void testMain() {
        String inputFile = "testImage.bmp";
        String outputFile = "outputImage.bmp";
        float min = 2.5f;
        float max = 7.5f;

        Clip clip = new Clip(min, max);

        Image image = new ByteGrayImage(new int[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        }, 4, 4);

        ImageInputStream is = new FileImageInputStream(new File(inputFile));
        Image image2 = is.read();
        is.close();

        Image result = clip.apply(image);

        ImageOutputStream os = new FileImageOutputStream(new File(outputFile));
        os.write(result);
        os.close();
    }
}