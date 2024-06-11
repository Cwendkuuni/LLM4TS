package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSJshopTest {

 private JSJshop jsJshop;

 @Test
 public void testParserFile() {
 JSJshop jsJshop = new JSJshop();
 jsJshop.testParser(); // Test the parser method with a valid file
 try {
 jsJshop.parserFile("invalidFile.shp"); // Test the parser method with an invalid file
 fail("Expected a JSParserError to be thrown");
 } catch (JSParserError e) {
 // Expected exception
 }
 }

 @Test
 public void testSettingFileAndPredicate() {
 JSJshop jsJshop = new JSJshop();
 JSListLogicalAtoms addList = new JSListLogicalAtoms();
 JSListLogicalAtoms deleteList = new JSListLogicalAtoms();
 JSTaskAtom predicate = new JSTaskAtom("predicate");
 JSJshopNode tree = jsJshop.setFile("validFile.shp", predicate);
 assertEquals("Tree should not be null", tree, jsJshop.getTree());
 assertEquals("Add list should not be null", addList, jsJshop.getAddList());
 assertEquals("Delete list should not be null", deleteList, jsJshop.getDeleteList());
 }

 @Test
 public void testSettingFileAndPredicateWithInvalidFile() {
 JSJshop jsJshop = new JSJshop();
 JSListLogicalAtoms addList = new JSListLogicalAtoms();
 JSListLogicalAtoms deleteList = new JSListLogicalAtoms();
 JSTaskAtom predicate = new JSTaskAtom("predicate");
 jsJshop.setFile("invalidFile.shp", predicate);
 assertNull("Tree should be null when file is invalid", jsJshop.getTree());
 }

 @Test
 public void testSettingFileAndPredicateWithNullPredicate() {
 JSJshop jsJshop = new JSJshop();
 JSListLogicalAtoms addList = new JSListLogicalAtoms();
 JSListLogicalAtoms deleteList = new JSListLogicalAtoms();
 jsJshop.setFile("validFile.shp", null);
 assertNull("Tree should be null when predicate is null", jsJshop.getTree());
 }

}