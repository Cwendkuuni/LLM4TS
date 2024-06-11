package umd.cs.shop.test;

import static org.junit.Assert.*;
import umd.cs.shop.JSJshop;
import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSJshopNode;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class JSJshopTest {

 private JSJshop jsShop;
 private JSPlanningDomain domain;
 private JSPlanningProblem problem;
 private JSPlan plan;
 private JSListLogicalAtoms addList;
 private JSListLogicalAtoms deleteList;

 @Before
 public void setUp() {
 jsShop = new JSJshop();
 domain = jsShop.dom();
 problem = new JSPlanningProblem("testDomain", "testProblem");
 jsShop.parserFile("testDomain.shp");
 jsShop.parserFile("testProblem.shp");
 }

 @After
 public void tearDown() {
 jsShop = null;
 domain = null;
 problem = null;
 plan = null;
 addList = null;
 deleteList = null;
 }

 @Test
 public void testParserFile() {
 File file = new File("testParserFile.shp");
 JSUtil.initParseTable(new StreamTokenizer(new FileReader(file)));
 jsShop.parserFile(file.getAbsolutePath());
 assertTrue(true);
 }

 @Test
 public void testCreateJSJshopWithDomainAndProblemFiles() {
 JSJshop jsShop = new JSJshop("testDomain.shp", "testProblem.shp");
 assertNotNull(jsShop.getTree());
 assertNotNull(jsShop.getSolution());
 }

 @Test
 public void testCreateJSJshopWithTaskAtom() {
 JSJshop jsShop = new JSJshop("testDomain.shp", new JSTaskAtom("testTaskAtom"));
 assertNotNull(jsShop.getTree());
 assertNotNull(jsShop.getSolution());
 }

 @Test
 public void testGetAddListAndDeleteList() {
 plan = new JSPlan("testPlan");
 addList = jsShop.getAddList();
 assertNotNull(addList);
 deleteList = jsShop.getDeleteList();
 assertNotNull(deleteList);
 }

 @Test
 public void testGetTree() {
 JSJshopNode tree = jsShop.getTree();
 assertNotNull(tree);
 }

 @Test
 public void testGetSolution() {
 JSPlan sol = jsShop.getSolution().planS().plan();
 assertNotNull(sol);
 }

 @Test
 public void testSetFile() throws IOException {
 JSJshop jsShop = new JSJshop();
 String fileName = "testFile.shp";
 String pred = "testPredicate";
 JSJshopNode tree = jsShop.setFile(fileName, new JSTaskAtom(pred)).getTree();
 assertNotNull(tree);
 }

 @Test(expected = NullPointerException.class)
 public void testSetFileNullFile() {
 JSJshop jsShop = new JSJshop();
 jsShop.setFile(null, new JSTaskAtom("testPredicate"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetFileInvalidPredicate() {
 JSJshop jsShop = new JSJshop();
 jsShop.setFile("testFile.shp", null);
 }

 @Test(expected = FileNotFoundException.class)
 public void testSetFileNonExistentFile() {
 JSJshop jsShop = new JSJshop();
 jsShop.setFile("testNonExistentFile.shp", new JSTaskAtom("testPredicate"));
 }

}