package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSJshop;

public class JSJshopTest {

 private JSJshop jsJshop;

 @Test
 public void testJSJshopParsing() {
 jsJshop = new JSJshop("domainFile.shp", "problemFile.shp");
 assertNotNull(jsJshop.getTree());
 assertNotNull(jsJshop.getSolution());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJSJshopWithInvalidArguments() {
 new JSJshop(new String[0], "domainFile.shp", "problemFile.shp");
 }

 @Test(expected = NumberFormatException.class)
 public void testJSJshopWithInvalidNumberFormat() {
 new JSJshop(new String[]{"1", "two"}, "domainFile.shp", "problemFile.shp");
 }

 @Test
 public void testJSJshopSolving() {
 jsJshop = new JSJshop("domainFile.shp", "problemFile.shp");
 assertEquals(jsJshop.getSolution().planS().plan().toString(), "plan solution");
 }

 @Test
 public void testJSJshopSolvingMultiplePlans() {
 jsJshop = new JSJshop("domainFile.shp", "problemFile.shp", "all");
 assertEquals(jsJshop.getSolution().planS().plan().toString(), "plan solution 1");
 assertEquals(jsJshop.getSolution(1).planS().plan().toString(), "plan solution 2");
 }

 @Test
 public void testJSJshopWithNullPredicate() {
 jsJshop = new JSJshop(null, null);
 assertNull(jsJshop.getTree());
 assertNull(jsJshop.getSolution());
 }

 @Test
 public void testJSJshopWithEmptyFile() {
 JSJshop jsJshop = new JSJshop("emptyFile.shp", null);
 assertNull(jsJshop.getTree());
 assertNull(jsJshop.getSolution());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJSJshopWithInvalidFile() {
 new JSJshop("invalidFile.shp", null);
 }
}