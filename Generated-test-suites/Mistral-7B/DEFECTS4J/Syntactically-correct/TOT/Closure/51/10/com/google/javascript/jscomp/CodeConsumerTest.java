package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = Node.newIntLiteral(10);
    consumer.startSourceMapping(node);
    consumer.append("someCode");
    consumer.endSourceMapping(node);

    String expectedCode = "10 // someCode";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();

    assertFalse(consumer.continueProcessing());

    consumer = new TestCodeConsumer();
    consumer.append("someCode");

    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("someCode");

    char lastChar = consumer.getLastChar();
    assertEquals('d', lastChar);
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("someCode");

    String expectedCode = "someCode";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("int a = 10;");
    consumer.endStatement();
    String expectedCode = "int a = 10;\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("int a = 10;");
    consumer.endBlock();
    String expectedCode = "{\nint a = 10;\n}";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("a");
    String expectedCode = "a";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(10.5);
    String expectedCode = "10.5";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAppendOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendOp("+", false);
    String expectedCode = "+";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = Node.newLiteral(Node.NEW_ARR, Node.newIntLiteral(1), Node.newIntLiteral(2));
    boolean result = consumer.shouldPreserveExtraBlocks();
    assertFalse(result);
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = Node.newLiteral(Node.NEW_ARR, Node.newIntLiteral(1), Node.newIntLiteral(2));

    assertTrue(consumer.breakAfterBlockFor(node, false));
    String expectedCode = "[\n1,\n2\n]";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);

    assertFalse(consumer.breakAfterBlockFor(node, true));
    expectedCode = "[1,\n2]";
    actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    String expectedCode = "";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    return 'd';
  }
}