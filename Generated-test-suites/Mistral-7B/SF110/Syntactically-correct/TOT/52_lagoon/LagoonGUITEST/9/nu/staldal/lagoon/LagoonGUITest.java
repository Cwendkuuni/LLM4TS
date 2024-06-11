package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.LagoonException;

import java.awt.event.ActionEvent;
import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LagoonGUITest {

 private LagoonGUI gui;

 @Before
 public void setUp() {
 gui = new LagoonGUI();
 gui.show();
 }

 @After
 public void tearDown() {
 gui.exit();
 }

 @Test
 public void testLoadProperties() {
 final File samplePropertyFile = new File("sample.properties");
 try {
 gui.loadProperties(samplePropertyFile);
 assertEquals("sample.xml", gui.sitemapFile.getValue());
 assertEquals("/path/to/source", gui.sourceDir.getValue());
 assertEquals("http://target.com", gui.targetURL.getValue());
 } catch (LagoonException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testSaveProperties() {
 final File tempPropertyFile = File.createTempFile("temp", ".properties");
 gui.savePropertyFile();
 assertTrue(tempPropertyFile.exists());
 try {
 gui.loadProperties(tempPropertyFile);
 assertEquals("temp.xml", gui.sitemapFile.getValue());
 assertEquals("/path/to/source", gui.sourceDir.getValue());
 assertEquals("http://target.com", gui.targetURL.getValue());
 } catch (LagoonException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 tempPropertyFile.delete();
 }

 @Test
 public void testBuildWithoutChangedFiles() {
 gui.build(false);
 // Add assertions here, e.g. checking the progressArea and statusLabel contents
 }

 @Test
 public void testBuildWithChangedFiles() {
 final File sampleSitemapFile = new File("sample.xml");
 final File sampleSourceDir = new File("sample/source");
 final File sampleTargetDir = new File("sample/target");
 gui.sitemapFile.setValue(sampleSitemapFile.getAbsolutePath());
 gui.sourceDir.setValue(sampleSourceDir.getAbsolutePath());
 gui.targetURL.setValue("http://target.com");
 gui.build(false);
 // Add assertions here, e.g. checking the progressArea and statusLabel contents

 sampleSourceDir.mkdirs();
 sampleSourceDir.deleteOnExit();
 sampleTargetDir.mkdirs();
 sampleTargetDir.deleteOnExit();
 sampleSitemapFile.deleteOnExit();

 try {
 Thread.sleep(1000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

 gui.sitemapFile.setValue(sampleSitemapFile.getAbsolutePath());
 gui.sourceDir.setValue(sampleSourceDir.getAbsolutePath());
 gui.targetURL.setValue("http://target.com");
 gui.build(false);
 // Add assertions here, e.g. checking the progressArea and statusLabel contents
 }

 @Test
 public void testBuildWithForceFlag() {
 final File sampleSitemapFile = new File("sample.xml");
 final File sampleSourceDir = new File("sample/source");
 final File sampleTargetDir = new File("sample/target");
 gui.sitemapFile.setValue(sampleSitemapFile.getAbsolutePath());
 gui.sourceDir.setValue(sampleSourceDir.getAbsolutePath());
 gui.targetURL.setValue("http://target.com");
 gui.build(true);
 // Add assertions here, e.g. checking the progressArea and statusLabel contents
 }

 @Test
 public void testWindowEvents() {
 final WindowEvent windowEvent = new WindowEvent(gui, WindowEvent.WINDOW_CLOSING);
 gui.windowClosing(windowEvent);
 // Add assertions here, e.g. checking the statusLabel and progressArea contents
 }
}