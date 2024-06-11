import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.SimpleOperator;
import jigl.image.ops.ROI;
import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.ColorImage;
import jigl.image.RealColorImage;
import jigl.image.Image;
import jigl.image.ImageInputStream;
import jigl.image.ImageOutputStream;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class ClipTest {

 @Test
 public void testGrayImageIntMinIntMax() {
 GrayImage inputImage = new GrayImage(5, 5);
 inputImage.set(0, 0, (byte) 100);
 inputImage.set(0, 1, (byte) 150);
 inputImage.set(0, 2, (byte) 75);
 inputImage.set(0, 3, (byte) 200);
 inputImage.set(0, 4, (byte) 120);

 ByteArrayInputStream inputStream = new ByteArrayInputStream(inputImage.getData().getData());
 ImageInputStream imageIS = new ImageInputStream(inputStream);
 Image inputImageObj = imageIS.read();
 imageIS.close();

 Clip clip = new Clip(50, 150);
 Image outputImage = clip.apply((GrayImage) inputImageObj);
 assertNotNull(outputImage);

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ImageOutputStream imageOS = new ImageOutputStream(outputStream);
 imageOS.write(outputImage);
 Image outputImageObj = Image.read(outputStream.toByteArray());
 imageOS.close();

 assert (outputImageObj.get(0, 0) == 50);
 assert (outputImageObj.get(0, 1) == 150);
 assert (outputImageObj.get(0, 2) == 75);
 assert (outputImageObj.get(0, 3) == 150);
 assert (outputImageObj.get(0, 4) == 120);
 }

 @Test
 public void testGrayImageFloatMinFloatMax() {
 GrayImage inputImage = new GrayImage(5, 5);
 inputImage.set(0, 0, (byte) 100);
 inputImage.set(0, 1, (byte) 150);
 inputImage.set(0, 2, (byte) 75);
 inputImage.set(0, 3, (byte) 200);
 inputImage.set(0, 4, (byte) 120);

 ByteArrayInputStream inputStream = new ByteArrayInputStream(inputImage.getData().getData());
 ImageInputStream imageIS = new ImageInputStream(inputStream);
 Image inputImageObj = imageIS.read();
 imageIS.close();

 Clip clip = new Clip(0.5f, 1.5f);
 Image outputImage = clip.apply((GrayImage) inputImageObj);
 assertNotNull(outputImage);

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ImageOutputStream imageOS = new ImageOutputStream(outputStream);
 imageOS.write(outputImage);
 Image outputImageObj = Image.read(outputStream.toByteArray());
 imageOS.close();

 assert (outputImageObj.get(0, 0) == 52);
 assert (outputImageObj.get(0, 1) == 148);
 assert (outputImageObj.get(0, 2) == 75);
 assert (outputImageObj.get(0, 3) == 150);
 assert (outputImageObj.get(0, 4) == 119);
 }

 @Test
 public void testGrayImageInvalidMinMax() {
 GrayImage inputImage = new GrayImage(5, 5);
 inputImage.set(0, 0, (byte) 100);
 inputImage.set(0, 1, (byte) 150);
 inputImage.set(0, 2, (byte) 75);
 inputImage.set(0, 3, (byte) 200);
 inputImage.set(0, 4, (byte) 120);

 ByteArrayInputStream inputStream = new ByteArrayInputStream(inputImage.getData().getData());
 ImageInputStream imageIS = new ImageInputStream(inputStream);
 Image inputImageObj = imageIS.read();
 imageIS.close();

 Clip clip = new Clip(1.5f, 0.5f);
 try {
 Image outputImage = clip.apply((GrayImage) inputImageObj);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testRealGrayImageIntMinIntMax() {
 RealGrayImage inputImage = new RealGrayImage(5, 5);
 inputImage.set(0, 0, 100.0f);
 inputImage.set(0, 1, 150.0f);
 inputImage.set(0, 2, 75.0f);
 inputImage.set(0, 3, 200.0f);
 inputImage.set(0, 4, 120.0f);

 ByteArrayInputStream inputStream = new ByteArrayInputStream(inputImage.getData().getData());
 ImageInputStream imageIS = new ImageInputStream(inputStream);
 Image inputImageObj = imageIS.read();
 imageIS.close();

 Clip clip = new Clip(50.0f, 150.0f);
 Image outputImage = clip.apply((RealGrayImage) inputImageObj);
 assertNotNull(outputImage);

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ImageOutputStream imageOS = new ImageOutputStream(outputStream);
 imageOS.write(outputImage);
 RealGrayImage outputImageObj = (RealGrayImage) Image.read(outputStream.toByteArray());
 imageOS.close();

 assert (outputImageObj.get(0, 0) == 50.0f);
 assert (outputImageObj.get(0, 1) == 150.0f);
 assert (outputImageObj.get(0, 2) == 75.0f);
 assert (outputImageObj.get(0, 3) == 150.0f);
 assert (outputImageObj.get(0, 4) == 120.0f);
 }

 @Test
 public void testRealGrayImageFloatMinFloatMax() {
 RealGrayImage inputImage = new RealGrayImage(5, 5);
 inputImage.set(0, 0, 100.0f);
 inputImage.set(0, 1, 150.0f);
 inputImage.set(0, 2, 75.0f);
 inputImage.set(0, 3, 200.0f);
 inputImage.set(0, 4, 120.0f);

 ByteArrayInputStream inputStream = new ByteArrayInputStream(inputImage.getData().getData());
 ImageInputStream imageIS = new ImageInputStream(inputStream);
 Image inputImageObj = imageIS.read();
 imageIS.close();

 Clip clip = new Clip(0.5f, 1.5f);
 Image outputImage = clip.apply((RealGrayImage) inputImageObj);
 assertNotNull(outputImage);

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ImageOutputStream imageOS = new ImageOutputStream(outputStream);
 imageOS.write(outputImage);
 RealGrayImage outputImageObj = (RealGrayImage) Image.read(outputStream.toByteArray());
 imageOS.close();

 assert (outputImageObj.get(0, 0) == 0.5f);
 assert (outputImageObj.get(0, 1) == 1.4f);
 assert (outputImageObj.get(0, 2) == 75.0f);
 assert (outputImageObj.get(0, 3) == 150.0f);
 assert (outputImageObj.get(0, 4) == 118.5f);
 }

 @Test
 public void testRealGrayImageInvalidMinMax() {
 RealGrayImage inputImage = new RealGrayImage(5, 5);
 inputImage.set(0, 0, 100.0f);
 inputImage.set(0, 1, 150.0f);
 inputImage.set(0, 2, 75.0f);
 inputImage.set(0, 3, 200.0f);
 inputImage.set(0, 4, 120.0f);

 ByteArrayInputStream inputStream = new ByteArrayInputStream(inputImage.getData().getData());
 ImageInputStream imageIS = new ImageInputStream(inputStream);
 Image inputImageObj = imageIS.read();
 imageIS.close();

 Clip clip = new Clip(1.5f, 0.5f);
 try {
 Image outputImage = clip.apply((RealGrayImage) inputImageObj);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testColorImageIntMinIntMax() {
 ColorImage inputImage = new ColorImage(5, 5, new GrayImage(5, 5), new GrayImage(5, 5), new GrayImage(5, 5));
 inputImage.setPlane(0, 0, 100);
 inputImage.setPlane(0, 1, 150);
 inputImage.setPlane(0, 2, 75);
 inputImage.setPlane(0, 3, 200);
 inputImage.setPlane(0, 4, 120);

 ByteArrayInputStream inputStream = new ByteArrayInputStream(inputImage.getData().getData());
 ImageInputStream imageIS = new ImageInputStream(inputStream);
 Image inputImageObj = imageIS.read();
 imageIS.close();

 Clip clip = new Clip(50, 150);
 Image outputImage = clip.apply((ColorImage) inputImageObj);
 assertNotNull(outputImage);

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ImageOutputStream imageOS = new ImageOutputStream(outputStream);
 imageOS.write(outputImage);
 ColorImage outputImageObj = (ColorImage) Image.read(outputStream.toByteArray());
 imageOS.close();

 assert (outputImageObj.getPlane(0).get(0, 0) == 50);
 assert (outputImageObj.getPlane(0).get(0, 1) == 150);
 assert (outputImageObj.getPlane(0).get(0, 2) == 75);
 assert (outputImageObj.getPlane(0).get(0, 3) == 150);
 assert (outputImageObj.getPlane(0).get(0, 4) == 120);
 }

 @Test
 public void testColorImageFloatMinFloatMax() {
 ColorImage inputImage = new ColorImage(5, 5, new RealGrayImage(5, 5), new RealGrayImage(5, 5), new RealGrayImage(5, 5));
 inputImage.setPlane(0, 0, 100.0f);
 inputImage.setPlane(0, 1, 150.0f);
 inputImage.setPlane(0, 2, 75.0f);
 inputImage.setPlane(0, 3, 200.0f);
 inputImage.setPlane(0, 4, 120.0f);

 ByteArrayInputStream inputStream = new ByteArrayInputStream(inputImage.getData().getData());
 ImageInputStream imageIS = new ImageInputStream(inputStream);
 Image inputImageObj = imageIS.read();
 imageIS.close();

 Clip clip = new Clip(0.5f, 1.5f);
 Image outputImage = clip.apply((ColorImage) inputImageObj);
 assertNotNull(outputImage);

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ImageOutputStream imageOS = new ImageOutputStream(outputStream);
 imageOS.write(outputImage);
 ColorImage outputImageObj = (ColorImage) Image.read(outputStream.toByteArray());
 imageOS.close();

 assert (outputImageObj.getPlane(0).get(0, 0) == 0.5f);
 assert (outputImageObj.getPlane(0).get(0, 1) == 1.4f);
 assert (outputImageObj.getPlane(0).get(0, 2) == 75.0f);
 assert (outputImageObj.getPlane(0).get(0, 3) == 150.0f);
 assert (outputImageObj.getPlane(0).get(0, 4) == 118.5f);
 }

 @Test
 public void testColorImageInvalidMinMax() {
 ColorImage inputImage = new ColorImage(5, 5, new RealGrayImage(5, 5), new RealGrayImage(5, 5), new RealGrayImage(5, 5));
 inputImage.setPlane(0, 0, 100.0f);
 inputImage.setPlane(0, 1, 150.0f);
 inputImage.setPlane(0, 2, 75.0f);
 inputImage.setPlane(0, 3, 200.0f);
 inputImage.setPlane(0, 4, 120.0f);

 ByteArrayInputStream inputStream = new ByteArrayInputStream(inputImage.getData().getData());
 ImageInputStream imageIS = new ImageInputStream(inputStream);
 Image inputImageObj = imageIS.read();
 imageIS.close();

 Clip clip = new Clip(1.5f, 0.5f);
 try {
 Image outputImage = clip.apply((ColorImage) inputImageObj);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }
}