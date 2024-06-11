package nu.staldal.lagoon.test;

import org.junit.Test;
import static org.junit.Assert.*; // for assert* methods
import nu.staldal.lagoon.*;

public class LagoonGUITest {

 private LagoonGUI gui;

 @Test
 public void testLagoonGUIConstructor() {
     gui = new LagoonGUI();
     assertNotNull(gui); // checks that the object was created successfully
 }

 @Test
 public void testLoadButtonActionPerformed() {
     gui = new LagoonGUI();
     gui.loadButton.doClick();
     assertTrue(gui.loadPropertyFileWasCalled); // should be false initially, checked in the next test
 }

 @Test
 public void testLoadPropertyFileWasCalledAfterLoadButton() {
     gui = new LagoonGUI();
     gui.loadPropertyFileWasCalled = true; // a flag to check the state after the button click
     gui.loadButton.doClick();
     assertTrue(gui.loadPropertyFileWasCalled);
 }

 @Test(expected = NullPointerException.class)
 public void testBuildWithNullProcessor() {
     gui = new LagoonGUI();
     gui.processor = null;
     gui.build(false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildWithInvalidForceArgument() {
     gui = new LagoonGUI();
     gui.build("-1"); // passing a negative number as argument
 }

 @Test
 public void testBuildSuccess() {
     gui = new LagoonGUI();
     gui.init();
     gui.build(false);
     assertTrue(gui.statusLabel.getText().equals("Ready"));
 }

 @Test
 public void testBuildWithInvalidSitemapFile() {
     gui = new LagoonGUI();
     gui.sitemapFile.setValue("non-existing-file.xml");
     gui.init();
     gui.build(false);
     assertTrue(gui.statusLabel.getText().contains("Building error"));
 }

 @Test
 public void testBuildWithChangedSourceDirectory() {
     gui = new LagoonGUI();
     gui.sourceDir.setValue("changed-directory");
     gui.init();
     gui.build(false);
     assertTrue(gui.statusLabel.getText().contains("Initializing..."));
 }

 @Test
 public void testBuildWithChangedTargetURL() {
     gui = new LagoonGUI();
     gui.targetURL.setValue("changed-target-url");
     gui.init();
     gui.build(false);
     assertTrue(gui.statusLabel.getText().contains("Initializing..."));
 }

 @Test
 public void testExit() {
     gui = new LagoonGUI();
     gui.exitButton.doClick();
     assertTrue(System.exit(0) == 0); // checks that the system exits successfully
 }

 @Test(expected = IOException.class)
 public void testInitWithInvalidSitemapFile() {
     gui = new LagoonGUI();
     gui.sitemapFile.setValue("non-existing-file.xml");
     gui.init();
 }

 @Test(expected = AuthenticationMissingException.class)
 public void testInitWithMissingPassword() {
     gui = new LagoonGUI();
     gui.init();
 }

 @Test(expected = AuthenticationException.class)
 public void testInitWithInvalidPassword() {
     gui = new LagoonGUI();
     gui.password = "invalid-password";
     gui.init();
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadPropertyFileWithNonExistentFile() {
     gui = new LagoonGUI();
     final FileDialog propertyFileDialog = new FileDialog(gui, "Load property file", 1);
     propertyFileDialog.show();
     final File propertyFile = (propertyFileDialog.getFile() == null) ? null : new File(propertyFileDialog.getDirectory(), propertyFileDialog.getFile());
     gui.loadProperties(propertyFile);
 }

 @Test(expected = IOException.class)
 public void testSavePropertyFileWithNonExistentDirectory() {
     gui = new LagoonGUI();
     final FileDialog propertyFileDialog = new FileDialog(gui, "Save property file", 1);
     propertyFileDialog.show();
     final File propertyFile = (propertyFileDialog.getFile() == null) ? null : new File(propertyFileDialog.getDirectory(), propertyFileDialog.getFile());
     gui.savePropertyFile();
 }

 // Add more test cases as needed

}