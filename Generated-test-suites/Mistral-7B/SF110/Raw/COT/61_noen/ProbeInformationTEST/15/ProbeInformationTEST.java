import org.junit.Test;
import static org.junit.Assert.*;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

 private String targetName = "TestTarget";
 private String targetType = "TestType";
 private String bmClass = "TestBMClass";
 private String bmName = "TestBMName";
 private String bmDescription = "TestBMDescription";
 private String probeName = "TestProbeName";
 private int precision = 4;
 private String xmlRpcUrl = "http://test.com/probe";

 @Test
 public void testGetTargetType() {
 ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 String expected = targetType;
 String actual = probeInfo.getTargetType();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetBmName() {
 ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 String expected = bmName;
 String actual = probeInfo.getBmName();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetProbeName() {
 ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 String expected = probeName;
 String actual = probeInfo.getProbeName();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetTargetName() {
 ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 String expected = targetName;
 String actual = probeInfo.getTargetName();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetBmClass() {
 ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 String expected = bmClass;
 String actual = probeInfo.getBmClass();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetBmDescription() {
 ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 String expected = bmDescription;
 String actual = probeInfo.getBmDescription();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetPrecision() {
 ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 int expected = precision;
 int actual = probeInfo.getPrecision();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMeasureURI() {
 ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 String expected = Const.createMeasureURI(targetType, targetName, bmClass, bmName);
 String actual = probeInfo.getMeasureURI();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetXmlRpcUrl() {
 ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 String expected = xmlRpcUrl;
 String actual = probeInfo.getXmlRpcUrl();
 assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorNullTargetName() {
 ProbeInformation probeInfo = new ProbeInformation(null, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorNullTargetType() {
 ProbeInformation probeInfo = new ProbeInformation(targetName, null, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorNullBmClass() {
 ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, null, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorNullBmName() {
 ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, null, bmDescription, probeName, precision, xmlRpcUrl);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorNullBmDescription() {
 ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, null, probeName, precision, xmlRpcUrl);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorNullProbeName() {
 ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, null, precision, xmlRpcUrl);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorInvalidPrecision() {
 ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, -1, xmlRpcUrl);
 }

 @Test
 public void testEquals() {
 ProbeInformation probeInfo1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 ProbeInformation probeInfo2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 ProbeInformation probeInfo3 = new ProbeInformation("TestTarget2", targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 ProbeInformation probeInfo4 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);
 assertTrue(probeInfo1.equals(probeInfo1));
 assertTrue(probeInfo1.equals(probeInfo2));
 assertFalse(probeInfo1.equals(probeInfo3));
 assertFalse(probeInfo1.equals(probeInfo4));
 }

 @Test
 public void testHashCode() {
 ProbeInformation probeInfo1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 ProbeInformation probeInfo2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 int expected1 = targetName.hashCode() + 31 * (targetType != null ? targetType.hashCode() : 0) + 31 * (bmClass != null ? bmClass.hashCode() : 0) + 31 * (bmName != null ? bmName.hashCode() : 0) + 31 * (bmDescription != null ? bmDescription.hashCode() : 0) + 31 * probeName.hashCode() + precision;
 int expected2 = targetName.hashCode() + 31 * (targetType != null ? targetType.hashCode() : 0) + 31 * (bmClass != null ? bmClass.hashCode() : 0) + 31 * (bmName != null ? bmName.hashCode() : 0) + 31 * (bmDescription != null ? bmDescription.hashCode() : 0) + 31 * probeName.hashCode() + precision;
 int actual1 = probeInfo1.hashCode();
 int actual2 = probeInfo2.hashCode();
 assertEquals(expected1, actual1);
 assertEquals(expected2, actual2);
 }
}