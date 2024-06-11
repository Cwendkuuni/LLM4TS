package umd.cs.shop.test;

import static org.junit.Assert.*;
import org.junit.Test;
import umd.cs.shop.JSJshop;

public class JSJshopTest {

 private JSJshop jsJshop;

 @Test
 public void testJSJshopConstructionWithDomainAndProblemFiles() {
 jsJshop = new JSJshop("domain.txt", "problem.txt");
 assertNotNull(jsJshop.getTree());
 assertNotNull(jsJshop.getSolution());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJSJshopConstructionWithInvalidArgument() {
 jsJshop = new JSJshop();
 assertNull(jsJshop.getTree());
 assertNull(jsJshop.getSolution());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJSJshopConstructionWithInvalidNumberOfArguments() {
 jsJshop = new JSJshop("domain.txt", "problem.txt", "invalidArg");
 assertNull(jsJshop.getTree());
 assertNull(jsJshop.getSolution());
 }

 @Test
 public void testJSJshopConstructionWithTaskAtom() {
 jsJshop = new JSJshop("file.txt", new JSTaskAtom("task"));
 assertNotNull(jsJshop.getTree());
 assertNotNull(jsJshop.getSolution());
 }

 @Test
 public void testGetAddList() {
 List<JSLogicalAtom> addList = jsJshop.getAddList();
 assertNotNull(addList);
 }

 @Test
 public void testGetDeleteList() {
 List<JSLogicalAtom> deleteList = jsJshop.getDeleteList();
 assertNotNull(deleteList);
 }

 @Test
 public void testParser() {
 jsJshop.testParser();
 // Add assertions for correct parsing here
 }
}