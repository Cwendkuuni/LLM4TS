package test.Newzgrabber;

import org.junit.Test;
import static org.junit.Assert.*;
import Newzgrabber.*;

import java.util.Vector;

public class NewzgrabberTest {

 @Test
 public void testSetupBasicSystem() {
 Newzgrabber.setupBasicSystem();
 assertNotNull(Newzgrabber.Newzdirectory);
 }

 @Test
 public void testFindDirectory() {
 String classfile = "Newz.jar";
 String expected = System.getProperty("user.dir") + File.separator + "lib" + File.separator + "Newz.jar";
 String actual = Newzgrabber.findDirectory(classfile);
 assertEquals(expected, actual);
 }

 @Test
 public void testUpdateIni() {
 String inifile = "test.ini";
 Newzgrabber.updateIni(inifile, "Section1", "Key1", "Value1");
 Newzgrabber.updateIni(inifile, "Section1", "Key1", "Value2");
 Vector<String[]> iniVector = NewIni(inifile).getIniVector();
 String actualValue = "";
 for (String[] stringArr : iniVector) {
 if (stringArr[0].equals("Key1")) {
 actualValue = stringArr[1];
 break;
 }
 }
 assertEquals("Value2", actualValue);
 }

 @Test(expected = Exception.class)
 public void testUpdateIniInvalidFile() {
 String inifile = "nonexistent.ini";
 Newzgrabber.updateIni(inifile, "Section1", "Key1", "Value1");
 }

 @Test
 public void testNewzgrabberBatchMode() {
 Newzgrabber newzgrabber = new Newzgrabber("");
 assertFalse(newzgrabber.IsBatch);
 Newzgrabber.main(new String[]{"-batch"});
 Newzgrabber newzgrabberBatch = Newzgrabber.getInstance();
 assertTrue(newzgrabberBatch.IsBatch);
 }

 @Test
 public void testNewzgrabberVerboseMode() {
 Newzgrabber newzgrabber = new Newzgrabber("");
 assertFalse(Newzgrabber.verbose);
 Newzgrabber.main(new String[]{"-verbose"});
 assertTrue(Newzgrabber.verbose);
 }

 @Test
 public void testNewzgrabberDebugMode() {
 Newzgrabber newzgrabber = new Newzgrabber("");
 assertFalse(Newzgrabber.debug);
 Newzgrabber.main(new String[]{"-debug"});
 assertTrue(Newzgrabber.debug);
 }

 @Test
 public void testNewzgrabberBatchDownload() {
 Newzgrabber newzgrabberBatch = new Newzgrabber("");
 Newzgrabber.main(new String[]{"-batch"});
 Vector<Object[]> groupDownloadCount = Newzgrabber.getGroupDownloadCount();
 assertEquals(1, groupDownloadCount.size());
 Object[] downloadData = groupDownloadCount.elementAt(0);
 String group = (String) downloadData[0];
 int downloads = ((Integer) downloadData[1]).intValue();
 Newzgrabber newzgrabberInstance = Newzgrabber.getInstance();
 assertEquals(group, newzgrabberInstance.getLastBatchGroup());
 }
}