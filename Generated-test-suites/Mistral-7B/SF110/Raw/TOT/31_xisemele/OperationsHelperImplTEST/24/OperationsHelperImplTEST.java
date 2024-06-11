// Test file for OperationsHelperImpl
package net.sf.xisemele.impl;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.*;

public class OperationsHelperImplTest {
   // Previous test case

   // Test for find method
   @Test
   public void testFind() {
      Document doc = createMockDocument();
      String path = "/root/element";
      Node element = doc.createElement("element");
      Node parent = doc.createElement("parent");
      parent.appendChild(element);
      doc.getDocumentElement().appendChild(parent);

      OperationsHelper helper = new OperationsHelperImpl();
      Node actual = helper.find(doc, path);

      Node expected = element;
      assertEquals(expected, actual);
   }

   // Test for nodeWithName method
   @Test
   public void testNodeWithName() {
      Document doc = createMockDocument();
      List<Node> nodes = new ArrayList<Node>() {{
         add(doc.createProcessingInstruction("xml", "version=\"1.0\""));
         add(doc.createElement("root"));
         add(doc.createElement("element1"));
         add(doc.createElement("element2"));
      }};

      OperationsHelper helper = new OperationsHelperImpl();
      Node actual = helper.nodeWithName(nodes, "element1");

      Node expected = doc.createElement("element1");
      assertEquals(expected, actual);
   }

   // Test for sublistWithName method
   @Test
   public void testSublistWithName() {
      Document doc = createMockDocument();
      List<Node> nodes = new ArrayList<Node>() {{
         add(doc.createProcessingInstruction("xml", "version=\"1.0\""));
         add(doc.createElement("root"));
         add(doc.createElement("element1"));
         add(doc.createElement("element1"));
         add(doc.createElement("element2"));
      }};

      OperationsHelper helper = new OperationsHelperImpl();
      List<Node> actual = helper.sublistWithName(nodes, "element1");

      List<Node> expected = new ArrayList<Node>() {{
         add(doc.createElement("element1"));
         add(doc.createElement("element1"));
      }};
      assertEquals(expected, actual);
   }

   // Error cases test
   @Test(expected = IllegalArgumentException.class)
   public void testFindNullDocument() {
      OperationsHelper helper = new OperationsHelperImpl();
      String path = "/root/element";
      Node actual = helper.find(null, path);
      fail("Expected IllegalArgumentException");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testFindEmptyPath() {
      Document doc = createMockDocument();
      OperationsHelper helper = new OperationsHelperImpl();
      Node actual = helper.find(doc, "");
      fail("Expected IllegalArgumentException");
   }

   @Test(expected = NullPointerException.class)
   public void testChildrenNullNode() {
      OperationsHelper helper = new OperationsHelperImpl();
      List<Node> actual = helper.children(null);
      fail("Expected NullPointerException");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testFindInvalidPath() {
      Document doc = createMockDocument();
      String path = "/root/nonExistentElement";
      OperationsHelper helper = new OperationsHelperImpl();
      Node actual = helper.find(doc, path);
      assertNull(actual);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testNodeWithNameNullNodes() {
      OperationsHelper helper = new OperationsHelperImpl();
      List<Node> actual = helper.nodeWithName(null, "element1");
      assertNull(actual);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testSublistWithNameNullNodes() {
      OperationsHelper helper = new OperationsHelperImpl();
      List<Node> actual = helper.sublistWithName(null, "element1");
      assertNull(actual);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testSublistWithNameEmptyNodes() {
      OperationsHelper helper = new OperationsHelperImpl();
      List<Node> actual = helper.sublistWithName(Collections.emptyList(), "element1");
      assertNull(actual);
   }
}