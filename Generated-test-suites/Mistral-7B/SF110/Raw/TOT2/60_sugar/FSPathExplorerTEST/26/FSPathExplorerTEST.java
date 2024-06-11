import org.junit.Test;import static org.junit.Assert.*;import static net.sf.sugar.fspath.cli.FSPathExplorer.*;

public class FSPathExplorerTest {

 private File tempDirectory;

 @Before
 public void setUp() throws Exception {
 tempDirectory = File.createTempDirectory("FSPathExplorerTest", null);
 System.setProperty("user.dir", tempDirectory.getAbsolutePath());
 }

 @After
 public void tearDown() throws Exception {
 if (tempDirectory != null && tempDirectory.isDirectory()) {
 tempDirectory.delete();
 }
 }

 @Test
 public void testCreateFSPathRootPathIsProvided() {
 FSPathExplorer explorer = new FSPathExplorer();
 File rootPath = new File("/");
 explorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath()});
 explorer.createFSPath();
 assertEquals(rootPath, explorer.fsPath.getRootDirectory());
 }

 @Test
 public void testCreateFSPathNoRootPathIsProvided() {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.createFSPath();
 assertNotNull(explorer.fsPath);
 }

 @Test
 public void testFindEmptyDirectory() {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.createFSPath();
 String query = "//dir";
 FSPathResultList results = explorer.fsPath.query(query);
 assertEquals(0, results.size());
 }

 @Test
 public void testFindFileInRootDirectory() {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.createFSPath();
 File testFile = File.createTempFile("testFile", null);
 explorer.fsPath.getRootDirectory().mkdirs();
 testFile.createNewFile();
 String query = "//file[@name = '" + testFile.getName() + "']";
 FSPathResultList results = explorer.fsPath.query(query);
 assertEquals(1, results.size());
 assertEquals(testFile.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test
 public void testFindDirectoryWithSubDirectories() {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.createFSPath();
 File testDirectory = new File(tempDirectory, "testDirectory");
 testDirectory.mkdirs();
 File subDirectory = new File(testDirectory, "subDirectory");
 subDirectory.mkdir();
 String query = "//dir[@name = 'testDirectory']//dir[@name = 'subDirectory']";
 FSPathResultList results = explorer.fsPath.query(query);
 assertEquals(1, results.size());
 assertEquals(subDirectory.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test(expected = IOException.class)
 public void testInvalidQuery() {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.createFSPath();
 String query = "//file[@name = 'asdfasdfasdf']";
 explorer.fsPath.query(query);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOption() {
 FSPathExplorer explorer = new FSPathExplorer();
 String[] args = {"-xyz", "-find", "testQuery"};
 explorer.configureExplorer(args);
 }
}