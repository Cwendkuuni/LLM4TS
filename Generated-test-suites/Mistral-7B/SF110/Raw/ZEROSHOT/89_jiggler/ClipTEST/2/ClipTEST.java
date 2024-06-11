package jigl.image.ops.levelOps;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import jigl.image.ops.SimpleOperator;
import jigl.image.ops.Clip;
import jigl.image.ops.ROI;
import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.types.NativeTypes;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;
import jigl.image.ColorImage;
import jigl.image.RealColorImage;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class ClipTest {

 private Clip clip;
 private Image image;
 private Image image2;
 private Image inputImage;
 private ROI roi;
 private RealGrayImage realGrayImage;
 private RealColorImage realColorImage;

 @Before
 public void setUp() {
 image = new GrayImage(1, 1, DataBufferByte.TYPE_BYTE);
 image2 = new GrayImage(1, 1, DataBufferByte.TYPE_BYTE);
 inputImage = new GrayImage(1, 1, DataBufferByte.TYPE_BYTE);
 when(inputImage.X()).thenReturn(1);
 when(inputImage.Y()).thenReturn(1);
 clip = new Clip(0.5f, 1.0f);
 realGrayImage = mock(RealGrayImage.class);
 realColorImage = mock(RealColorImage.class);
 roi = new ROI(0, 0, 1, 1);
 when(realGrayImage.X()).thenReturn(1);
 when(realGrayImage.Y()).thenReturn(1);
 when(realGrayImage.get(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn((byte) 128);
 when(realColorImage.plane(0)).thenReturn(realGrayImage);
 when(realColorImage.plane(1)).thenReturn(realGrayImage);
 when(realColorImage.plane(2)).thenReturn(realGrayImage);
 when(realColorImage.X()).thenReturn(1);
 when(realColorImage.Y()).thenReturn(1);
 }

 @Test
 public void testApplyGrayImage() {
 byte[] inputData = { (byte) 255, (byte) 127, (byte) 50, (byte) 200, (byte) 153, (byte) 56 };
 byte[] expectedData = { (byte) 127, (byte) 127, (byte) 50, (byte) 127 };
 inputImage.setData(new DataBufferByte(inputData, 0, inputData.length));
 when(image.getData()).thenReturn(inputImage.getData());
 when(image.X()).thenReturn(inputImage.X());
 when(image.Y()).thenReturn(inputImage.Y());

 clip.apply(image);
 image2 = clip.apply(image);
 byte[] outputData = new byte[inputImage.width() * inputImage.height()];
 image2.getData().getBytes(0, outputData, 0, outputData.length);

 assertArrayEquals(expectedData, outputData);
 }

 @Test
 public void testApplyRealGrayImage() {
 float[] inputData = { 255.0f, 127.0f, 50.0f, 200.0f, 153.0f, 56.0f };
 float[] expectedData = { 127.0f, 127.0f, 50.0f, 127.0f };
 when(realGrayImage.getData()).thenReturn(inputData);

 clip.apply(realGrayImage, roi);
 byte[] outputData = new byte[realGrayImage.width() * realGrayImage.height()];
 image2.getData().getBytes(0, outputData, 0, outputData.length);
 float[] outputDataFloat = new float[outputData.length / Byte.BYTES];
 ByteBuffer.wrap(outputData).asFloatBuffer().get(outputDataFloat);

 assertArrayEquals(expectedData, outputDataFloat, 0.001f);
 }

 @Test
 public void testApplyColorImage() {
 byte[][] inputData = { { (byte) 255, (byte) 0, (byte) 255 }, { (byte) 0, (byte) 255, (byte) 0 },
 { (byte) 255, (byte) 0, (byte) 255 } };
 byte[][] expectedData = { { (byte) 0, (byte) 0, (byte) 0 }, { (byte) 0, (byte) 0, (byte) 0 },
 { (byte) 0, (byte) 0, (byte) 0 } };
 ColorImage inputColorImage = new ColorImage(1, 1, 3, inputData);
 when(image.getData()).thenReturn(inputColorImage.getData());
 when(image.X()).thenReturn(inputColorImage.X());
 when(image.Y()).thenReturn(inputColorImage.Y());

 clip.apply(image);
 image2 = clip.apply(image);
 byte[][] outputData = new byte[3][image2.width() * image2.height()];
 image2.getData().getBytes(0, outputData[0], 0, outputData[0].length);
 image2.getData().getBytes(inputData[0].length, outputData[1], 0, outputData[1].length);
 image2.getData().getBytes(2 * inputData[0].length, outputData[2], 0, outputData[2].length);

 assertArrayEquals(expectedData[0], outputData[0]);
 assertArrayEquals(expectedData[1], outputData[1]);
 assertArrayEquals(expectedData[2], outputData[2]);
 }

 @Test
 public void testApplyRealColorImage() {
 float[][] inputData = { { 255.0f, 0.0f, 255.0f }, { 0.0f, 255.0f, 0.0f }, { 255.0f, 0.0f, 255.0f } };
 float[][] expectedData = { { 0.0f, 0.0f, 0.0f }, { 0.0f, 0.0f, 0.0f }, { 0.0f, 0.0f, 0.0f } };
 when(realColorImage.getData()).thenReturn(NativeTypes.toFloatArray(inputData));

 clip.apply(realColorImage, roi);
 byte[] outputData = new byte[realColorImage.width() * realColorImage.height() * 3];
 image2.getData().getBytes(0, outputData, 0, outputData.length);
 float[][] outputDataFloat = new float[3][image2.width() * image2.height()];
 ByteBuffer.wrap(outputData).asFloatBuffer().get(outputDataFloat);

 assertArrayEquals(expectedData, outputDataFloat, 0.001f);
 }

 @Test
 public void testMainWithValidArguments() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ImageInputStream inputStream = mock(ImageInputStream.class);
 ImageOutputStream outputStreamMock = mock(ImageOutputStream.class);
 when(inputStream.read()).thenReturn(inputImage);
 when(inputStream.close()).thenReturn(null);
 when(outputStreamMock.write(ArgumentMatchers.any(Image.class))).thenReturn(null);
 when(outputStreamMock.close()).thenReturn(null);

 String inputFile = "testImage.jpg";
 String outputFile = "testOutput.jpg";
 String[] argv = { "0.5", "1.0", inputFile, outputFile };
 Clip.main(argv);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMainWithInvalidArguments() {
 String inputFile = "testImage.jpg";
 String outputFile = "testOutput.jpg";
 String[] argv = { "0.5", "1.0", inputFile, "", outputFile };
 Clip.main(argv);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMainWithInvalidArguments2() {
 String inputFile = "testImage.jpg";
 String outputFile = "testOutput.jpg";
 String[] argv = { "0.5", "1.0", inputFile, outputFile, "wrongType" };
 Clip.main(argv);
 }
}