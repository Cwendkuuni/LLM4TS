package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import nu.staldal.lagoon.*;
import nu.staldal.xtree.*;

public class LagoonGUITest {

 private LagoonGUI appFrame;

 @Before
 public void setup() {
 appFrame = new LagoonGUI();
 }

 @Test
 public void testConstruction() {
 assertEquals("Lagoon", appFrame.getTitle());
 assertNotNull(appFrame.getInputPanel());
 assertNotNull(appFrame.getCenterPanel());
 assertNotNull(appFrame.getButtonPanel());
 assertNull(appFrame.getProcessor());
 assertEqualToLong(0L, appFrame.getSitemapLastModified());
 }

 @Test
 public void testLoadButtonActionPerformed() {
 appFrame.loadButton.doClick();
 assertNotNull(appFrame.getProcessor());
 }

 @Test
 public void testSaveButtonActionPerformed() {
 // To be implemented
 }

 @Test
 public void testBuildButtonActionPerformed() {
 // To be implemented
 }

 @Test
 public void testForceButtonActionPerformed() {
 // To be implemented
 }

 @Test
 public void testExitButtonActionPerformed() {
 appFrame.exitButton.doClick();
 assertTrue(appFrame.isClosed());
 }

 @Test
 public void testBuildWithInvalidSitemapFile() {
 appFrame.sitemapFile.setValue("nonExistingFile.xml");
 appFrame.buildButton.doClick();
 assertNull(appFrame.getProcessor());
 }

 @Test
 public void testBuildWithNewSitemapFile() throws Exception {
 final String xmlContent = "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">" +
         "<url>" +
         "<loc>http://example.com/page1</loc>" +
         "</url>" +
         "</urlset>";
 final File tempSitemapFile = File.createTempFile("tempSitemap", ".xml");
 try (FileWriter fileWriter = new FileWriter(tempSitemapFile)) {
 fileWriter.write(xmlContent);
 }
 appFrame.sitemapFile.setValue(tempSitemapFile.getAbsolutePath());
 appFrame.buildButton.doClick();
 assertNotNull(appFrame.getProcessor());
 tempSitemapFile.delete();
 }

 @Test
 public void testBuildWithExistingSitemapFileAndChangedSourceDir() throws Exception {
 final String xmlContent = "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">" +
         "<url>" +
         "<loc>http://example.com/page1</loc>" +
         "</url>" +
         "</urlset>";
 final File sitemapFile = File.createTempFile("tempSitemap", ".xml");
 try (FileWriter fileWriter = new FileWriter(sitemapFile)) {
 fileWriter.write(xmlContent);
 }
 appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 appFrame.sourceDir.setValue("target/test-classes");
 appFrame.buildButton.doClick();
 assertNotNull(appFrame.getProcessor());
 }

 @Test
 public void testBuildWithExistingSitemapFileAndUnchangedSourceDir() throws Exception {
 final String xmlContent = "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">" +
         "<url>" +
         "<loc>http://example.com/page1</loc>" +
         "</url>" +
         "</urlset>";
 final File sitemapFile = File.createTempFile("tempSitemap", ".xml");
 try (FileWriter fileWriter = new FileWriter(sitemapFile)) {
 fileWriter.write(xmlContent);
 }
 appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 appFrame.sourceDir.setValue(".");
 appFrame.buildButton.doClick();
 appFrame.buildButton.doClick();
 assertNotNull(appFrame.getProcessor());
 }

 @Test
 public void testInitWithMissingAuthentication() {
 appFrame.sitemapFile.setValue("sitemap.xml");
 appFrame.init();
 assertTrue(appFrame.isAuthenticationMissingDialogShown());
 }

 @Test
 public void testInitWithInvalidAuthentication() {
 appFrame.sitemapFile.setValue("sitemap.xml");
 appFrame.setAuthentication("invalidPassword");
 appFrame.init();
 assertTrue(appFrame.isAuthenticationDialogShown());
 }

 @Test
 public void testInitWithValidAuthentication() {
 appFrame.sitemapFile.setValue("sitemap.xml");
 appFrame.setAuthentication("correctPassword");
 appFrame.init();
 assertFalse(appFrame.isAuthenticationDialogShown());
 }

 @Test
 public void testInitWithInvalidSitemapFileLastModified() {
 appFrame.sitemapFile.setValue("sitemap.xml");
 appFrame.sitemapLastModified = System.currentTimeMillis() + 1000L;
 appFrame.init();
 assertTrue(appFrame.isInitDialogShown());
 }

 @Test
 public void testInitWithValidSitemapFileLastModified() {
 appFrame.sitemapFile.setValue("sitemap.xml");
 appFrame.sitemapLastModified = System.currentTimeMillis();
 appFrame.init();
 assertFalse(appFrame.isInitDialogShown());
 }

 private boolean isClosed() {
 return appFrame.isDisplayable() == false;
 }

 private boolean isAuthenticationMissingDialogShown() {
 return appFrame.getAuthenticationMissingDialog() != null;
 }

 private boolean isAuthenticationDialogShown() {
 return appFrame.getAuthenticationDialog() != null;
 }

 private boolean isInitDialogShown() {
 return appFrame.getInitDialog() != null;
 }
}