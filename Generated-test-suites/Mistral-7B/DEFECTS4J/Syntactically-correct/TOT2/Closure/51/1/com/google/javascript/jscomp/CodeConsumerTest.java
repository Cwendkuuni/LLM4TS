package com.example.junit.test.codeconsumer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CodeConsumerTest {
  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.add("Hello World");
    assertEquals('W', consumer.getLastChar());
  }

  @Test
  public void testAppendEmptyString() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("");
    assertEquals("", consumer.toCodeString());
  }

  @Test
  public void testAppendBlockStartAndEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("Test Code");
    consumer.endBlock();
    assertEquals("{\nTest Code\n}", consumer.toCodeString());
  }

  @Test
  public void testAddNumberNegativeExponent() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(-123.456e-5);
    assertEquals("-1.23456E-05", consumer.toCodeString().substring(consumer.getLastIndexOf("E"), consumer.getLastIndexOf(";") + 2));
  }

  @Test
  public void testAddOpPrefixOperator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.add("x++");
    consumer.addOp("++", true);
    assertEquals("x++\n++", consumer.toCodeString());
  }

  // Add more test cases as needed

  @After
  public void tearDown() {
    // Perform cleanup if needed
  }
}