import junit.framework.TestCase;
import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest extends TestCase {

    private Clip clip;
    private GrayImage image;
    private RealGrayImage realImage;
    private ColorImage colorImage;
    private RealColorImage realColorImage;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        image = new BufferedGrayImage(10, 10, DataBuffer.TYPE_BYTE);
        realImage = new BufferedRealGrayImage(10, 10, DataBuffer.TYPE_FLOAT);
        colorImage = new BufferedColorImage(10, 10, new RGBColorModel(3, 3, 3, null), new DataBufferByte[3]);
        realColorImage = new BufferedRealColorImage(10, 10, new RGBColorModel(3, 3, 3, null), new DataBufferFloat[3]);
        clip = new Clip(0, 5);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        if (image != null) {
            image.dispose();
        }
        if (realImage != null) {
            realImage.dispose();
        }
        if (colorImage != null) {
            colorImage.dispose();
        }
        if (realColorImage != null) {
            realColorImage.dispose();
        }
    }

    // Test constructor with int min and max
    public void testClipConstructorInt() {
        assertEquals("Constructor with int min and max should initialize int_min and int_max correctly.", 0, clip.getInt_min());
        assertEquals("Constructor with int min and max should initialize int_min and int_max correctly.", 5, clip.getInt_max());

        clip = new Clip(10, 20);
        assertEquals("Constructor with int min and max should initialize int_min and int_max correctly.", 10, clip.getInt_min());
        assertEquals("Constructor with int min and max should initialize int_min and int_max correctly.", 20, clip.getInt_max());
    }

    // Test constructor with float min and max
    public void testClipConstructorFloat() {
        clip = new Clip(2.5f, 4.5f);
        assertEquals("Constructor with float min and max should initialize float_min and float_max correctly.", 2.5f, clip.getFloat_min(), 0.001f);
        assertEquals("Constructor with float min and max should initialize float_min and float_max correctly.", 4.5f, clip.getFloat_max(), 0.001f);
    }

    // Test apply method with GrayImage
    public void testApplyGrayImage() {
        setPixelValues(image, 5, 5, 100);

        clip.apply(image);

        int expectedMin = Math.min(5, this.getInt_min());
        int expectedMax = Math.min(5, this.getInt_max());

        for (int y = 0; y < image.Y(); ++y) {
            for (int x = 0; x < image.X(); ++x) {
                int actualValue = image.get(x, y).intValue();
                int expectedValue = Math.min(Math.max(actualValue, expectedMin), expectedMax);
                assertEquals("Apply method with GrayImage should set pixel values correctly.", expectedValue, actualValue);
            }
        }
    }

    // Test apply method with RealGrayImage
    public void testApplyRealGrayImage() {
        setPixelValues(realImage, 5, 5, 2.5f);

        clip.apply(realImage);

        float expectedMin = Math.min(5, this.getFloat_min());
        float expectedMax = Math.min(5, this.getFloat_max());

        for (int y = 0; y < realImage.Y(); ++y) {
            for (int x = 0; x < realImage.X(); ++x) {
                float actualValue = realImage.get(x, y).floatValue();
                float expectedValue = Math.min(Math.max(actualValue, expectedMin), expectedMax);
                assertEquals("Apply method with RealGrayImage should set pixel values correctly.", expectedValue, actualValue, 0.001f);
            }
        }
    }

    // Test apply method with ColorImage
    public void testApplyColorImage() {
        Color color = new Color(1.0f, 2.0f, 3.0f);
        setPixelValues(colorImage, 5, 5, color);

        clip.apply(colorImage);

        Color expectedColor = new Color(Math.min(5, this.getFloat_min()), Math.min(5, this.getFloat_min()), Math.min(5, this.getFloat_min()));
        Color actualColor = colorImage.getPixelColor(5, 5);
        assertEquals("Apply method with ColorImage should set pixel colors correctly.", expectedColor, actualColor);
    }

    // Test apply method with RealColorImage
    public void testApplyRealColorImage() {
        Color color = new Color(1.0f, 2.0f, 3.0f);
        setPixelValues(realColorImage, 5, 5, color);

        clip.apply(realColorImage);

        Color expectedColor = new Color(Math.min(5, this.getFloat_min()), Math.min(5, this.getFloat_min()), Math.min(5, this.getFloat_min()));
        Color actualColor = realColorImage.getPixelColor(5, 5);
        assertEquals("Apply method with RealColorImage should set pixel colors correctly.", expectedColor, actualColor);
    }

    // Test apply method with negative ROI
    public void testApplyNegativeROI() {
        GrayImage image = new BufferedGrayImage(10, 10, DataBuffer.TYPE_BYTE);
        setPixelValues(image, 5, 5, 100);

        ROI roi = new ROI(-5, -5, 10, 10);

        clip.apply(image, roi);

        for (int y = 0; y < image.Y(); ++y) {
            for (int x = 0; x < image.X(); ++x) {
                int actualValue = image.get(x, y).intValue();
                int expectedValue = Math.min(Math.max(actualValue, 5), 5 + this.getInt_max());
                assertEquals("Apply method with negative ROI should set pixel values correctly.", expectedValue, actualValue);
            }
        }
    }

    // Test apply method with out-of-bounds ROI
    public void testApplyOutOfBoundsROI() {
        GrayImage image = new BufferedGrayImage(10, 10, DataBuffer.TYPE_BYTE);
        setPixelValues(image, 5, 5, 100);

        ROI roi = new ROI(15, 15, 10, 10);

        try {
            clip.apply(image, roi);
            fail("Apply method with out-of-bounds ROI should throw an exception.");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    // Test apply method with null Image
    public void testApplyNullImage() {
        try {
            clip.apply(null, new ROI(0, 0, 10, 10));
            fail("Apply method with null Image should throw an exception.");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // Test apply method with null ROI
    public void testApplyNullROI() {
        GrayImage image = new BufferedGrayImage(10, 10, DataBuffer.TYPE_BYTE);
        setPixelValues(image, 5, 5, 100);

        try {
            clip.apply(image, null);
            fail("Apply method with null ROI should throw an exception.");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    private void setPixelValues(BufferedImage image, int x, int y, Object value) {
        WritableRaster raster = image.getRaster();
        if (value instanceof Integer) {
            raster.setObjectValue(x, y, new Integer(value));
        } else if (value instanceof Float) {
            raster.setObjectValue(x, y, new Float(value));
        } else if (value instanceof Color) {
            raster.setObjectValue(x, y, new Color((Integer)value.getRed(), (Integer)value.getGreen(), (Integer)value.getBlue()));
        } else if (value instanceof RealColor) {
            raster.setObjectValue(x, y, new RealColor((((RealColor)value).getRed().floatValue()), ((RealColor)value).getGreen().floatValue(), ((RealColor)value).getBlue().floatValue()));
        }
    }

    // Test main method
    public void testMainMethod() {
        String[] argv = {"1.0f", "2.5f", "input.jpg", "output.jpg"};

        Clip.main(argv);

        // Check if input.jpg file is read correctly
        Image inputImage = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_GRAY);
        try (FileInputStream fis = new FileInputStream("input.jpg")) {
            ImageInputStream is = ImageIO.createImageInputStream(fis);
            Image image = is.read();
            is.close();
            assertEquals("Input image should be read correctly.", inputImage, image);
        } catch (IOException e) {
            fail("Main method should read input image correctly.");
        }

        // Check if Clip instance is created correctly
        Clip clip = new Clip(1.0f, 2.5f);
        assertEquals("Clip instance should be created with correct min and max values.", 1.0f, clip.getFloat_min(), 0.001f);
        assertEquals("Clip instance should be created with correct min and max values.", 2.5f, clip.getFloat_max(), 0.001f);

        // Check if output.jpg file is written correctly
        Image outputImage = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_GRAY);
        setPixelValues(outputImage, 5, 5, 100);
        Clip clipInstance = new Clip(1.0f, 2.5f);
        clipInstance.apply(outputImage);
        ImageOutputStream os = new FileImageOutputStream("output.jpg");
        os.write(outputImage, "jpg");
        os.close();

        Image expectedOutputImage = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_GRAY);
        setPixelValues(expectedOutputImage, 5, 5, 2.5f);
        clipInstance.apply(expectedOutputImage);

        for (int y = 0; y < expectedOutputImage.Y(); ++y) {
            for (int x = 0; x < expectedOutputImage.X(); ++x) {
                int actualValue = outputImage.get(x, y).intValue();
                int expectedValue = expectedOutputImage.get(x, y).intValue();
                assertEquals("Output image should be written correctly.", expectedValue, actualValue);
            }
        }
    }
}