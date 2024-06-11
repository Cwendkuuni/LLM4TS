import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;

public class LagoonGUITest {

 private LagoonGUI appFrame;

 @Before
 public void setUp() {
 appFrame = new LagoonGUI();
 appFrame.addWindowListener(new WindowAdapter() {
 @Override
 public void windowClosed(WindowEvent e) {
 appFrame = null;
 }
 });
 appFrame.show();
 }

 @Test
 public void testLoadProperties() {
 final String testSitemapFile = "testSitemap.xml";
 final String testSourceDir = "testSourceDir";
 final String testTargetURL = "http://test.com";
 appFrame.sitemapFile.setValue(testSitemapFile);
 appFrame.sourceDir.setValue(testSourceDir);
 appFrame.targetURL.setValue(testTargetURL);
 appFrame.loadProperties(new File("testProperties.xml"));
 assertEquals(testSitemapFile, appFrame.sitemapFile.getValue());
 assertEquals(testSourceDir, appFrame.sourceDir.getValue());
 assertEquals(testTargetURL, appFrame.targetURL.getValue());
 }

 @Test
 public void testSaveProperties() throws IOException {
 final String testSitemapFile = "testSitemap.xml";
 final String testSourceDir = "testSourceDir";
 final String testTargetURL = "http://test.com";
 final String testPassword = "testPassword";
 appFrame.sitemapFile.setValue(testSitemapFile);
 appFrame.sourceDir.setValue(testSourceDir);
 appFrame.targetURL.setValue(testTargetURL);
 appFrame.password = testPassword;
 appFrame.savePropertyFile();
 File testPropertiesFile = new File("testProperties.xml");
 Properties testProperties = new Properties();
 testProperties.load(new FileInputStream(testPropertiesFile));
 assertEquals(testSitemapFile, testProperties.getProperty("sitemapFile"));
 assertEquals(testSourceDir, testProperties.getProperty("sourceDir"));
 assertEquals(testTargetURL, testProperties.getProperty("targetURL"));
 assertEquals(testPassword, testProperties.getProperty("password"));
 testPropertiesFile.delete();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildNullProcessor() {
 appFrame.build(true);
 fail("Expected IllegalArgumentException with null processor");
 }

 @Test
 public void testBuildSuccess() throws IOException {
 final String testSitemapFile = "testSitemap.xml";
 final String testSourceDir = "testSourceDir";
 final String testTargetURL = "http://test.com";
 final String testPassword = "testPassword";
 appFrame.sitemapFile.setValue(testSitemapFile);
 appFrame.sourceDir.setValue(testSourceDir);
 appFrame.targetURL.setValue(testTargetURL);
 appFrame.password = testPassword;
 appFrame.init();
 assertEquals("Building...", appFrame.statusLabel.getText());
 appFrame.build(false);
 assertEquals("Build finished successfully", appFrame.progressArea.getText());
 }

 @Test(expected = FileNotFoundException.class)
 public void testBuildNotFoundSitemapFile() {
 final String testSourceDir = "testSourceDir";
 final String testTargetURL = "http://test.com";
 appFrame.sourceDir.setValue(testSourceDir);
 appFrame.targetURL.setValue(testTargetURL);
 appFrame.init();
 File testSitemapFile = new File("doesnotexist.xml");
 appFrame.sitemapFile.setValue(testSitemapFile.getAbsolutePath());
 appFrame.build(false);
 fail("Expected FileNotFoundException with nonexistent sitemap file");
 }

 @Test(expected = IOException.class)
 public void testBuildIOException() throws IOException {
 final String testSitemapFile = "testSitemap.xml";
 final String testSourceDir = "testSourceDir";
 final String testTargetURL = "http://test.com";
 final String testPassword = "testPassword";
 appFrame.sitemapFile.setValue(testSitemapFile);
 appFrame.sourceDir.setValue(testSourceDir);
 appFrame.targetURL.setValue(testTargetURL);
 appFrame.password = testPassword;
 FileInputStream fis = new FileInputStream(new File(testSitemapFile));
 InputSource is = new InputSource(fis);
 appFrame.init();
 try {
 appFrame.processor.build(false);
 fail("Expected IOException with I/O error");
 }
 finally {
 fis.close();
 }
 }

 @Test(expected = LagoonException.class)
 public void testBuildLagoonException() throws IOException {
 final String testSitemapFile = "testSitemap.xml";
 final String testSourceDir = "testSourceDir";
 final String testTargetURL = "http://test.com";
 final String testPassword = "testPassword";appFrame.sitemapFile.setValue(testSitemapFile); appFrame.sourceDir.setValue(testSourceDir); appFrame.targetURL.setValue(testTargetURL); appFrame.password = testPassword; Element sitemapTree = TreeBuilder.parseXML(is, false); PrintWriter pw = new PrintWriter(new TextAreaWriter(appFrame.progressArea), true); appFrame.processor = new LagoonProcessor(testTargetURL, sitemapTree, new File(testSourceDir), testPassword, pw, pw); appFrame.init(); try { appFrame.processor.build(false); fail("Expected LagoonException with build error"); } finally { appFrame.processor.destroy(); } }

 @Test
 public void testExit() {
 appFrame.exitButton.doClick();
 assertFalse(appFrame.isDisplayable());
 }
}