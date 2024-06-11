package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.LagoonException;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LagoonGUITest {

 private LagoonGUI frame;

 @Before
 public void setUp() {
 frame = new LagoonGUI();
 frame.show();
 }

 @After
 public void tearDown() {
 frame.exit();
 frame = null;
 }

 @Test
 public void testInitialize() throws IOException, LagoonException {
 final String sitemapFile = "test.xml";
 final String sourceDir = "src";
 final String targetURL = "http://localhost:8080";

 frame.sitemapFile.setValue(sitemapFile);
 frame.sourceDir.setValue(sourceDir);
 frame.targetURL.setValue(targetURL);
 frame.init();

 assertEquals("http://localhost:8080", frame.processor.getTargetURL());
 assertTrue(frame.processor != null);
 }

 @Test(expected = LagoonException.class)
 public void testInitializeWithInvalidSitemapFile() throws IOException {
 final String sitemapFile = "nonExistentFile.xml";
 final String sourceDir = "src";
 final String targetURL = "http://localhost:8080";

 frame.sitemapFile.setValue(sitemapFile);
 frame.sourceDir.setValue(sourceDir);
 frame.targetURL.setValue(targetURL);
 frame.init();
 }

 @Test(expected = LagoonException.class)
 public void testInitializeWithInvalidSourceDir() throws IOException, LagoonException {
 final String sitemapFile = "test.xml";
 final String sourceDir = "nonExistentDir";
 final String targetURL = "http://localhost:8080";

 frame.sitemapFile.setValue(sitemapFile);
 frame.sourceDir.setValue(sourceDir);
 frame.targetURL.setValue(targetURL);
 frame.init();
 }

 @Test
 public void testBuildWithoutChange() throws IOException, LagoonException {
 final String sitemapFile = "test.xml";
 final String sourceDir = "src";
 final String targetURL = "http://localhost:8080";

 frame.sitemapFile.setValue(sitemapFile);
 frame.sourceDir.setValue(sourceDir);
 frame.targetURL.setValue(targetURL);
 frame.init();
 frame.build(false);
 }

 @Test
 public void testBuildWithChange() throws IOException, LagoonException {
 final String sitemapFile = "test.xml";
 final String sourceDir = "src";
 final String targetURL = "http://localhost:8080";

 frame.sitemapFile.setValue(sitemapFile);
 frame.sourceDir.setValue(sourceDir);
 frame.targetURL.setValue(targetURL);
 frame.init();
 frame.sitemapFile.setValue("test2.xml");
 frame.build(false);
 }

 @Test(expected = IOException.class)
 public void testBuildWithFileNotFoundException() throws IOException, LagoonException {
 final String sitemapFile = "nonExistentFile.xml";
 final String sourceDir = "src";
 final String targetURL = "http://localhost:8080";

 frame.sitemapFile.setValue(sitemapFile);
 frame.sourceDir.setValue(sourceDir);
 frame.targetURL.setValue(targetURL);
 frame.init();
 frame.build(false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildWithNullTargetURL() throws IOException, LagoonException {
 frame.targetURL.setValue(null);
 frame.init();
 frame.build(false);
 }
}