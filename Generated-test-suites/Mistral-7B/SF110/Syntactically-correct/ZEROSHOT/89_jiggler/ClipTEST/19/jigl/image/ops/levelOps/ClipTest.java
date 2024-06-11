package jigl.image.ops.levelOps;

import static org.junit.Assert.*;

import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;
import org.junit.Test;

public class ClipTest {

 @Test
 public void testApplyGrayImageIntMinIntMax() {
 GrayImage inputImage = new ByteGrayImage(5, 5, new byte[25]);
 for (int i = 0; i < 25; i++) {
 inputImage.set(i / 5, i % 5, (byte) (i % 30 + 1));
 }
 ROI roi = new ROI(1, 1, 3, 3);
 int min = 5;
 int max = 15;
 Clip clip = new Clip(min, max);
 Image outputImage = clip.apply(inputImage, roi);

 byte[] expectedOutput = {10, 10, 10, 10, 10, 11, 11, 11, 11, 11};
 byte[] actualOutput = new byte[outputImage.X() * outputImage.Y()];
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 actualOutput[y * outputImage.X() + x] = outputImage.get(x, y).byteValue();
 }
 }
 assertArrayEquals("Expected and actual output arrays do not match.", expectedOutput, actualOutput);
 }

 @Test
 public void testApplyGrayImageFloatMinFloatMax() {
 GrayImage inputImage = new ByteGrayImage(5, 5, new byte[25]);
 for (int i = 0; i < 25; i++) {
 inputImage.set(i / 5, i % 5, (byte) (i % 30 + 1));
 }
 ROI roi = new ROI(1, 1, 3, 3);
 float min = 5.5f;
 float max = 15.5f;
 Clip clip = new Clip(min, max);
 Image outputImage = clip.apply(inputImage, roi);

 float[] expectedOutput = {6.5f, 6.5f, 6.5f, 6.5f, 6.5f, 7.5f, 7.5f, 7.5f, 7.5f, 7.5f};
 float[] actualOutput = new float[outputImage.X() * outputImage.Y()];
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 actualOutput[y * outputImage.X() + x] = outputImage.get(x, y).floatValue();
 }
 }
 assertArrayEquals("Expected and actual output arrays do not match.", expectedOutput, actualOutput, 0.01f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyGrayImageInvalidMin() {
 GrayImage inputImage = new ByteGrayImage(5, 5, new byte[25]);
 for (int i = 0; i < 25; i++) {
 inputImage.set(i / 5, i % 5, (byte) (i % 30 + 1));
 }
 ROI roi = new ROI(1, 1, 3, 3);
 int min = 10;
 int max = 15;
 Clip clip = new Clip(min, max);
 clip.apply(inputImage, roi);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyGrayImageInvalidMax() {
 GrayImage inputImage = new ByteGrayImage(5, 5, new byte[25]);
 for (int i = 0; i < 25; i++) {
 inputImage.set(i / 5, i % 5, (byte) (i % 30 + 1));
 }
 ROI roi = new ROI(1, 1, 3, 3);
 int min = 5;
 int max = 4;
 Clip clip = new Clip(min, max);
 clip.apply(inputImage, roi);
 }

 @Test
 public void testApplyRealGrayImageIntMinIntMax() {
 RealGrayImage inputImage = new FloatGrayImage(5, 5, new float[25]);
 for (int i = 0; i < 25; i++) {
 inputImage.set(i / 5, i % 5, i % 30 + 1.0f);
 }
 ROI roi = new ROI(1, 1, 3, 3);
 int min = 5;
 int max = 15;
 Clip clip = new Clip(min, max);
 Image outputImage = clip.apply(inputImage, roi);

 float[] expectedOutput = {6.5f, 6.5f, 6.5f, 6.5f, 6.5f, 7.5f, 7.5f, 7.5f, 7.5f, 7.5f};
 float[] actualOutput = new float[outputImage.X() * outputImage.Y()];
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 actualOutput[y * outputImage.X() + x] = outputImage.get(x, y).floatValue();
 }
 }
 assertArrayEquals("Expected and actual output arrays do not match.", expectedOutput, actualOutput, 0.01f);
 }

 @Test
 public void testApplyRealGrayImageFloatMinFloatMax() {
 RealGrayImage inputImage = new FloatGrayImage(5, 5, new float[25]);
 for (int i = 0; i < 25; i++) {
 inputImage.set(i / 5, i % 5, i % 30 + 1.0f);
 }
 ROI roi = new ROI(1, 1, 3, 3);
 float min = 5.5f;
 float max = 15.5f;
 Clip clip = new Clip(min, max);
 Image outputImage = clip.apply(inputImage, roi);

 float[] expectedOutput = {6.5f, 6.5f, 6.5f, 6.5f, 6.5f, 7.5f, 7.5f, 7.5f, 7.5f, 7.5f};
 float[] actualOutput = new float[outputImage.X() * outputImage.Y()];
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 actualOutput[y * outputImage.X() + x] = outputImage.get(x, y).floatValue();
 }
 }
 assertArrayEquals("Expected and actual output arrays do not match.", expectedOutput, actualOutput, 0.01f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyRealGrayImageInvalidMin() {
 RealGrayImage inputImage = new FloatGrayImage(5, 5, new float[25]);
 for (int i = 0; i < 25; i++) {
 inputImage.set(i / 5, i % 5, i % 30 + 1.0f);
 }
 ROI roi = new ROI(1, 1, 3, 3);
 int min = 10;
 int max = 15;
 Clip clip = new Clip(min, max);
 clip.apply(inputImage, roi);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyRealGrayImageInvalidMax() {
 RealGrayImage inputImage = new FloatGrayImage(5, 5, new float[25]);
 for (int i = 0; i < 25; i++) {
 inputImage.set(i / 5, i % 5, i % 30 + 1.0f);
 }
 ROI roi = new ROI(1, 1, 3, 3);
 int min = 5;
 int max = 4;
 Clip clip = new Clip(min, max);
 clip.apply(inputImage, roi);
 }

 @Test
 public void testApplyColorImage() {
 ByteRGBImage inputImage = new ByteRGBImage(5, 5, new byte[15 * 15 * 3]);
 for (int i = 0; i < 15 * 15 * 3; i += 3) {
 inputImage.set(i / (15 * 15), i % (15 * 15) / 3, i % 3, (byte) (i % 30 + 1));
 }
 ROI roi = new ROI(1, 1, 3, 3);
 int min = 5;
 int max = 15;
 Clip clip = new Clip(min, max);
 Image outputImage = clip.apply(inputImage);

 byte[][] expectedOutputPlanes = new byte[3][3 * 3];
 for (int planeIndex = 0; planeIndex < 3; planeIndex++) {
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 expectedOutputPlanes[planeIndex][y * 3 + x] = (byte) ((i % 30 + 1) % 3);
 }
 }
 }
 byte[] actualOutputBytes = new byte[outputImage.X() * outputImage.Y() * 3];
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 for (int planeIndex = 0; planeIndex < 3; planeIndex++) {
 actualOutputBytes[(y * outputImage.X() + x) * 3 + planeIndex] = outputImage.getPlane(planeIndex).get(x, y).byteValue();
 }
 }
 }
 assertArrayEquals("Expected and actual output planes do not match.", expectedOutputPlanes, outputImage.getPlanes());
 }

 @Test
 public void testApplyColorImageFloatMinFloatMax() {
 FloatRGBImage inputImage = new FloatRGBImage(5, 5, new float[15 * 15 * 3]);
 for (int i = 0; i < 15 * 15 * 3; i += 3) {
 inputImage.set(i / (15 * 15), i % (15 * 15) / 3, i % 3, (i % 30 + 1.0f) / 3.0f);
 }
 ROI roi = new ROI(1, 1, 3, 3);
 float min = 5.5f;
 float max = 15.5f;
 Clip clip = new Clip(min, max);
 Image outputImage = clip.apply(inputImage);

 float[][] expectedOutputPlanes = new float[3][3 * 3];
 for (int planeIndex = 0; planeIndex < 3; planeIndex++) {
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 expectedOutputPlanes[planeIndex][y * 3 + x] = ((i % 30 + 1.0f) / 3.0f) % 2.0f + 0.5f;
 }
 }
 }
 float[] actualOutputFloat = new float[outputImage.X() * outputImage.Y() * 3];
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 for (int planeIndex = 0; planeIndex < 3; planeIndex++) {
 actualOutputFloat[(y * outputImage.X() + x) * 3 + planeIndex] = outputImage.getPlane(planeIndex).get(x, y).floatValue();
 }
 }
 }
 assertArrayEquals("Expected and actual output planes do not match.", expectedOutputPlanes, outputImage.getPlanes(), 0.01f);
 }
}