package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.jscomp.CodeConsumerTest;

public class CodeConsumerTest extends CodeConsumerTest {

  @Override
  protected CodeConsumer createCodeConsumer() {
    return new TestCodeConsumer();
  }

  public static class TestCodeConsumer extends CodeConsumer {

    @Override
    public void startSourceMapping(Node node) {
      super.startSourceMapping(node);
      // Add implementation for testing
    }

    @Override
    public void endSourceMapping(Node node) {
      super.endSourceMapping(node);
      // Add implementation for testing
    }

    @Override
    public boolean continueProcessing() {
      return super.continueProcessing();
      // Add implementation for testing
    }

    @Override
    public char getLastChar() {
      // Add implementation for testing
      return ' ';
    }

    @Override
    public void addIdentifier(String identifier) {
      super.addIdentifier(identifier);
      // Add implementation for testing
    }

    @Override
    public void append(String str) {
      super.append(str);
      // Add implementation for testing
    }

    @Override
    public void appendBlockStart() {
      super.appendBlockStart();
      // Add implementation for testing
    }

    @Override
    public void appendBlockEnd() {
      super.appendBlockEnd();
      // Add implementation for testing
    }

    @Override
    public void startNewLine() {
      super.startNewLine();
      // Add implementation for testing
    }

    @Override
    public void maybeLineBreak() {
      super.maybeLineBreak();
      // Add implementation for testing
    }

    @Override
    public void maybeCutLine() {
      super.maybeCutLine();
      // Add implementation for testing
    }

    @Override
    public void endLine() {
      super.endLine();
      // Add implementation for testing
    }

    @Override
    public void notePreferredLineBreak() {
      super.notePreferredLineBreak();
      // Add implementation for testing
    }

    @Override
    public void beginBlock() {
      super.beginBlock();
      // Add implementation for testing
    }

    @Override
    public void endBlock() {
      super.endBlock();
      // Add implementation for testing
    }

    @Override
    public void listSeparator() {
      super.listSeparator();
      // Add implementation for testing
    }

    @Override
    public void endStatement() {
      super.endStatement();
      // Add implementation for testing
    }

    @Override
    public void endStatement(boolean needSemiColon) {
      super.endStatement(needSemiColon);
      // Add implementation for testing
    }

    @Override
    public void maybeEndStatement() {
      super.maybeEndStatement();
      // Add implementation for testing
    }

    @Override
    public void endFunction() {
      super.endFunction();
      // Add implementation for testing
    }

    @Override
    public void endFunction(boolean statementContext) {
      super.endFunction(statementContext);
      // Add implementation for testing
    }

    @Override
    public void beginCaseBody() {
      super.beginCaseBody();
      // Add implementation for testing
    }

    @Override
    public void endCaseBody() {
      super.endCaseBody();
      // Add implementation for testing
    }

    @Override
    public void add(String newcode) {
      super.add(newcode);
      // Add implementation for testing
    }

    @Override
    public void appendOp(String op, boolean binOp) {
      super.appendOp(op, binOp);
      // Add implementation for testing
    }

    @Override
    public void addOp(String op, boolean binOp) {
      super.addOp(op, binOp);
      // Add implementation for testing
    }

    @Override
    public void addNumber(double x) {
      super.addNumber(x);
      // Add implementation for testing
    }

    @Override
    public boolean shouldPreserveExtraBlocks() {
      return super.shouldPreserveExtraBlocks();
      // Add implementation for testing
    }

    @Override
    public boolean breakAfterBlockFor(Node n, boolean statementContext) {
      return super.breakAfterBlockFor(n, statementContext);
      // Add implementation for testing
    }

    @Override
    public void endFile() {
      super.endFile();
      // Add implementation for testing
    }
  }

  @org.junit.Test
  public void testCodeConsumer() {
    CodeConsumer consumer = createCodeConsumer();
    consumer.addIdentifier("test");
    consumer.append(" = ");
    consumer.addNumber(1);
    consumer.append(";");
    consumer.maybeLineBreak();
    consumer.endLine();
    consumer.endStatement();
    consumer.endFunction();
    consumer.endFile();

    String expected = "test = 1;\n";
    assertEquals(expected, consumer.getCode());
  }
}