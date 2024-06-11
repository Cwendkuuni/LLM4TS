package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class ArrowTypeTest {
  @Test
  public void testArrowTypeConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node parameters = Node.newNode(Node.NEW_ARRAY, new Node[] { UNKNOWN_TYPE });
    Node returnType = INT_TYPE;

    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertEquals("Expected actual ArrowType instance to match expected ArrowType",
        arrowType, new ArrowType(registry, parameters, returnType));
  }

  @Test
  public void testSubtypeSameReturnTypeAndParams() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node parameters1 = Node.newNode(Node.NEW_ARRAY, new Node[] { INT_TYPE, INT_TYPE });
    Node returnType1 = INT_TYPE;
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);

    Node parameters2 = Node.newNode(Node.NEW_ARRAY, new Node[] { INT_TYPE, INT_TYPE });
    Node returnType2 = INT_TYPE;
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    assertTrue("Expected ArrowType1 to be a subtype of ArrowType2",
        arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testSubtypeDifferentReturnTypeButSameParams() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node parameters = Node.newNode(Node.NEW_ARRAY, new Node[] { INT_TYPE, INT_TYPE });
    Node returnType1 = INT_TYPE;
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType1);

    Node returnType2 = BOOLEAN_TYPE;
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType2);

    assertFalse("Expected ArrowType1 not to be a subtype of ArrowType2",
        arrowType1.isSubtype(arrowType2));
  }

  // ... Add the remaining test cases here ...
}