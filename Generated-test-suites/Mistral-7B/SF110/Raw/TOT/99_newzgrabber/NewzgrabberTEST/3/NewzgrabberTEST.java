package NewzgrabberTests;

import static org.junit.Assert.*;
import Newzgrabber.Newzgrabber;
import Newzgrabber.OptionsPanel;
import Newzgrabber.GroupsPanel;
import Newzgrabber.SearchPanel;
import Newzgrabber.TransferPanel;

import java.io.File;
import java.util.Vector;

public class NewzgrabberTest {

 @ org.junit.BeforeClass
 static void setup() {
 Newzgrabber.verbose = false;
 Newzgrabber.debug = false;
 Newzgrabber.batch = false;
 Newzgrabber.IsBatch = false;
 Newzgrabber.JarFilename = "Newz.jar";
 Newzgrabber.BatchFilename = "batch.ini";
 Newzgrabber.NewzInifilename = "Newzgrabber.ini";
 }

 @ org.junit.Test
 public void testNewzgrabberConstructor() {
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
 assertEquals(false, Newzgrabber.IsBatch);
 assertNotNull(newzgrabber.getContentPane());
 }

 @ org.junit.Test
 public void testNewzgrabberBatchConstructor() {
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
 Newzgrabber.IsBatch = true;
 Newzgrabber newzbatch = new Newzgrabber("Newzbatch");
 assertEquals(true, newzbatch.IsBatch);
 assertNull(newzbatch.getContentPane());
 }

 @ org.junit.Test
 public void testSetupBasicSystem() {
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
 assertNotNull(Newzgrabber.Newzdirectory);
 }

 @ org.junit.Test
 public void testFindDirectory() {
 String classfile = "Newz.jar";
 String actualDirectory = Newzgrabber.findDirectory(classfile);
 String expectedDirectory = System.getProperty("user.dir") + File.separator + ".." + File.separator;
 assertEquals(expectedDirectory, actualDirectory);
 }

 @ org.junit.Test
 public void testUpdateIni() {
 String inifile = "Newzgrabber.ini";
 String prop = "Messageids";
 String key = "TestKey";
 String value = "TestValue";
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
 newzgrabber.updateIni(inifile, prop, key, value);
 Vector<Object[]> iniVector = newzgrabber.getIniVector();
 Object[] iniEntry = null;
 for (Object object : iniVector) {
 Object[] iniEntryObj = (Object[]) object;
 String keyObj = (String) iniEntryObj[0];
 if (keyObj.equals(key)) {
 iniEntry = iniEntryObj;
 break;
 }
 }
 assertEquals(value, iniEntry[1]);
 }

 @ org.junit.Test
 public void testWindowListeners() {
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
 newzgrabber.addWindowListener(new WindowAdapter() {
 @Override
 public void windowClosing(WindowEvent e) {
 fail("windowClosing should not be called");
 }
 });
 newzgrabber.setVisible(true);
 newzgrabber.dispose();
 }

 @ org.junit.Test
 public void testNewzbatch() {
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
 Newzgrabber.IsBatch = true;
 Newzbatch newzbatch = new Newzbatch();
 assertFalse(newzbatch.isVisible());
 }

 @ org.junit.Test
 public void testNewzgrabberComponents() {
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
 assertNotNull(Newzgrabber.jtp);
 assertNotNull(Newzgrabber.searchpanel);
 assertNotNull(Newzgrabber.optionspanel);
 assertNotNull(Newzgrabber.groupspanel);
 assertNotNull(Newzgrabber.transferpanel);
 }

 @ org.junit.Test
 public void testGettingBatchInfo() {
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
 Newzgrabber.IsBatch = true;
 Newzgrabber newzbatch = new Newzbatch();
 String[] BatchGroups = newzbatch.getGroups();
 assertNotNull(BatchGroups);
 assertFalse(BatchGroups.length == 0);
 for (String group : BatchGroups) {
 System.out.println("Testing batch group: " + group);
 // Add testing logic for getting batch jobs, line data, subject lines, and downloading articles here
 }
 }
}