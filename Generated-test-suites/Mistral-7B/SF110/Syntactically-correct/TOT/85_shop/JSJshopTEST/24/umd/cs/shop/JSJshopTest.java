import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSJshop;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSJshopNode;
import umd.cs.shop.JSListLogicalAtoms;

public class JSJshopTest {

 private JSJshop jsJshop;
 private JSPlanningDomain domain;
 private JSPlanningProblem problem;

 @Before
 public void setUp() {
 jsJshop = new JSJshop("domain.shp", "problem.shp");
 domain = jsJshop.dom();
 problem = jsJshop.prob();
 }

 @Test
 public void testParsingFile() {
 assertTrue(jsJshop.parserFile("farp.shp"));
 }

 @Test
 public void testCreatingJSJshopObject() {
 JSJshop jsJshop1 = new JSJshop();
 JSJshop jsJshop2 = new JSJshop("testDomain.shp", "testProblem.shp");
 assertNotNull(jsJshop1.getTree());
 assertNotNull(jsJshop2.getTree());
 }

 @Test
 public void testSettingFileWithPredicate() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.setFile("testFile.shp", new JSTaskAtom("testPredicate"));
 JSJshopNode tree = jsJshop.getTree();
 assertNotNull(tree);
 }

 @Test
 public void testGettingSolution() {
 JSListLogicalAtoms addList = jsJshop.getAddList();
 JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
 JSPlan sol = jsJshop.sol();
 JSJshopNode tree = jsJshop.getTree();
 JSListLogicalAtoms expectedAddList = new JSListLogicalAtoms();
 JSListLogicalAtoms expectedDeleteList = new JSListLogicalAtoms();
 // Add some logical atoms to the expected lists based on the given class
 expectedAddList.add(new JSLogicalAtom("test1"));
 expectedAddList.add(new JSLogicalAtom("test2"));
 expectedDeleteList.add(new JSLogicalAtom("test3"));
 assertEquals(expectedAddList, jsJshop.getAddList());
 assertEquals(expectedDeleteList, jsJshop.getDeleteList());
 assertNotNull(sol);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidNumberOfArgumentsInMainMethod() {
 new JSJshop("", "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidDomainFile() {
 jsJshop = new JSJshop();
 jsJshop.parserFile("invalidDomain.shp");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidProblemFile() {
 jsJshop = new JSJshop();
 jsJshop.parserFile("invalidProblem.shp");
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidParameterInMainMethod() {
 new JSJshop(new String[]{"", "problem.shp", "invalid"});
 }

 @Test(expected = JSParserError.class)
 public void testParsingError() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.parserFile("parsingError.shp");
 }
}