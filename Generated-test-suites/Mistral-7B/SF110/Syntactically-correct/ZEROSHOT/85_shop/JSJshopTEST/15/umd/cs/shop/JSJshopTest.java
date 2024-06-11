package umd.cs.shop.test;

import static org.junit.Assert.*;
import org.junit.Test;
import umd.cs.shop.JSJshop;

public class JSJshopTest {

 private JSJshop jsJshop;

 @Test
 public void testJSJshopMain() {
 JSJshop jsJshop = new JSJshop();
 String[] args = new String[]{"domainDef-file-name", "problemDef-file-name"};
 jsJshop.main(args);
 assertTrue(jsJshop.getTree() != null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJSJshopMainInvalidArguments() {
 JSJshop jsJshop = new JSJshop();
 String[] args = new String[]{"java", "JSJshop"};
 jsJshop.main(args);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testJSJshopParserFile() {
 jsJshop = new JSJshop();
 jsJshop.parserFile("path/to/valid/file.shp");
 assertTrue(jsJshop.getTree() != null);
 }

 @Test(expected = FileNotFoundException.class)
 public void testJSJshopParserFileNotFound() {
 jsJshop = new JSJshop();
 jsJshop.parserFile("path/to/nonexistent/file.shp");
 fail("Should have thrown FileNotFoundException");
 }

 @Test
 public void testJSJshopSetFile() {
 jsJshop = new JSJshop();
 String nameFile = "path/to/file.shp";
 JSTaskAtom pred = new JSTaskAtom("test-predicate");
 JSJshopNode tree = jsJshop.setFile(nameFile, pred);
 assertNotNull(tree);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJSJshopSetFileInvalidFile() {
 jsJshop = new JSJshop();
 String nameFile = "invalid-file";
 JSTaskAtom pred = new JSTaskAtom("test-predicate");
 jsJshop.setFile(nameFile, pred);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testJSJshopGetAddList() {
 jsJshop = new JSJshop();
 jsJshop.parserFile("path/to/valid/file.shp");
 JSListLogicalAtoms addList = jsJshop.getAddList();
 assertNotNull(addList);
 }

 @Test
 public void testJSJshopGetDeleteList() {
 jsJshop = new JSJshop();
 jsJshop.parserFile("path/to/valid/file.shp");
 JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
 assertNotNull(deleteList);
 }

 @Test
 public void testJSJshopGetSolution() {
 jsJshop = new JSJshop();
 jsJshop.parserFile("path/to/valid/file.shp");
 JSPairPlanTSListNodes solution = jsJshop.getSolution();
 assertNotNull(solution);
 }

 @Test
 public void testJSJshopGetTree() {
 jsJshop = new JSJshop();
 jsJshop.parserFile("path/to/valid/file.shp");
 JSJshopNode tree = jsJshop.getTree();
 assertNotNull(tree);
 }
}