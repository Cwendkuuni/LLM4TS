import org.junit.Assert;
import org.junit.Test;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.SimpleOperator;
import jigl.image.ops.GrayImage;
import jigl.image.ops.RealGrayImage;
import jigl.image.ops.ROI;
import jigl.image.Image;
import jigl.image.types.IntegerType;
import jigl.image.types.FloatType;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

public class ClipTest {

    @Test
    public void testConstructorWithIntMinAndIntMax() {
        Clip clip = new Clip(1, 5);
        Assert.assertEquals(1, clip.int_min);
        Assert.assertEquals(5, clip.int_max);
    }

    @Test
    public void testConstructorWithFloatMinAndFloatMax() {
        Clip clip = new Clip(2.5f, 7.5f);
        Assert.assertEquals(2.5f, clip.float_min);
        Assert.assertEquals(7.5f, clip.float_max);
    }

    @Test
    public void testApplyGrayImageWithIntMinAndIntMax() {
        GrayImage image = new GrayImage(3, 3, new IntegerType());
        image.set(1, 1, 10);
        image.set(1, 2, 15);
        image.set(2, 1, 5);
        image.set(2, 2, 12);

        Clip clip = new Clip(5, 10);
        Image result = clip.apply(image);

        Assert.assertEquals(5, result.getPixel(1, 1));
        Assert.assertEquals(10, result.getPixel(1, 2));
        Assert.assertEquals(5, result.getPixel(2, 1));
        Assert.assertEquals(10, result.getPixel(2, 2));

        // test pixels outside ROI
        Assert.assertEquals(10, image.getPixel(0, 0));
        Assert.assertEquals(15, image.getPixel(3, 3));
    }

    @Test
    public void testApplyRealGrayImageWithFloatMinAndFloatMax() {
        RealGrayImage image = new RealGrayImage(3, 3, new FloatType());
        image.set(1, 1, 11.5f);
        image.set(1, 2, 17.5f);
        image.set(2, 1, 2.5f);
        image.set(2, 2, 13.5f);

        Clip clip = new Clip(7.5f, 12.5f);
        Image result = clip.apply(image);

        Assert.assertEquals(7.5f, result.getPixel(1, 1), 0.1f);
        Assert.assertEquals(12.5f, result.getPixel(1, 2), 0.1f);
        Assert.assertEquals(7.5f, result.getPixel(2, 1), 0.1f);
        Assert.assertEquals(12.5f, result.getPixel(2, 2), 0.1f);

        // test pixels outside ROI
        Assert.assertEquals(11.5f, image.getPixel(0, 0), 0.1f);
        Assert.assertEquals(17.5f, image.getPixel(3, 3), 0.1f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyWithNullImage() {
        Clip clip = new Clip(0, 10);
        clip.apply(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyWithNullROI() {
        GrayImage image = new GrayImage(3, 3, new IntegerType());
        Clip clip = new Clip(5, 10);
        clip.apply(image, null);
    }

    @Test
    public void testMainMethod() throws Exception {
        String inputFile = "test_image.bmp";
        float minValue = 2.5f;
        float maxValue = 7.5f;

        Image image = new GrayImage(3, 3, new IntegerType());
        image.set(1, 1, 10);
        image.set(1, 2, 15);
        image.set(2, 1, 5);
        image.set(2, 2, 12);

        ImageOutputStream outputStream = new FileImageOutputStream("output_image.bmp");

        Clip.main(new String[]{"2.5", "7.5", inputFile, "output_image.bmp"});

        Image resultImage = ImageIO.read(new File("output_image.bmp"));
        GrayImage resultGrayImage = (GrayImage) resultImage;

        Assert.assertEquals(5, resultGrayImage.getPixel(1, 1));
        Assert.assertEquals(10, resultGrayImage.getPixel(1, 2));
        Assert.assertEquals(5, resultGrayImage.getPixel(2, 1));
        Assert.assertEquals(10, resultGrayImage.getPixel(2, 2));

        outputStream.close();
        File outputFile = new File("output_image.bmp");
        outputFile.delete();
    }
}