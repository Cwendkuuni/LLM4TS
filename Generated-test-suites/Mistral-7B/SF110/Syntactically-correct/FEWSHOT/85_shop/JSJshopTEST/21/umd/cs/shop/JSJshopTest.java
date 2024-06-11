package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;

public class JSJshopTest {

 private JSJshop jsJshop;

 @Test
 public void testMain() {
 String[] args = new String[]{"domainDef-file-name", "problemDef-file-name"};
 JSJshop.main(args);
 // Add assertions here to test the output of the main method
 }

 @Test
 public void testJSJshop() {
 String nameDomainFile = "domain.shp";
 String nameProblemFile = "problem.shp";
 JSJshop jsJshop = new JSJshop(nameDomainFile, nameProblemFile);
 // Add assertions here to test the behavior of the JSJshop object
 }

 @Test
 public void testJSJshopWithFileAndPredicate() {
 String nameFile = "file.shp";
 JSTaskAtom predicate = new JSTaskAtom("predicate");
 JSJshop jsJshop = new JSJshop(nameFile, predicate);
 // Add assertions here to test the behavior of the JSJshop object
 }

 @Test
 public void testSetFile() {
 String nameFile = "file.shp";
 JSTaskAtom predicate = new JSTaskAtom("predicate");
 JSJshop jsJshop = new JSJshop();
 jsJshop.setFile(nameFile, predicate);
 // Add assertions here to test the behavior of the setFile method
 }

 @Test
 public void testGetTree() {
 JSJshop jsJshop = new JSJshop();
 JSJshopNode tree = jsJshop.getTree();
 // Add assertions here to test the behavior of the getTree method
 }

 @Test
 public void testGetSolution() {
 JSJshop jsJshop = new JSJshop();
 JSPairPlanTSListNodes solution = jsJshop.getSolution();
 // Add assertions here to test the behavior of the getSolution method
 }

 @Test
 public void testGetAddList() {
 JSJshop jsJshop = new JSJshop();
 JSListLogicalAtoms addList = jsJshop.getAddList();
 // Add assertions here to test the behavior of the getAddList method
 }

 @Test
 public void testGetDeleteList() {
 JSJshop jsJshop = new JSJshop();
 JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
 // Add assertions here to test the behavior of the getDeleteList method
 }
}