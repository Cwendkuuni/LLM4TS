package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.IntType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionType;

public class FunctionBuilderTest {
  // Test cases for all methods
  // ...

  @Test
  public void testWithParamsEdgeCases() {
    FunctionParamBuilder functionParamBuilder = null; // Test for null input
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    try {
      functionBuilder.withParams(functionParamBuilder);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Pass
    }

    // Test for empty list of FunctionParamBuilder
    FunctionParamBuilder emptyFunctionParamBuilder = new FunctionParamBuilder();
    FunctionBuilder functionBuilderEmpty = functionBuilder.withParams(emptyFunctionParamBuilder);
    assertNotNull(functionBuilderEmpty.build());

    // Test for list with multiple FunctionParamBuilder
    FunctionParamBuilder functionParamBuilder1 = new FunctionParamBuilder();
    FunctionParamBuilder functionParamBuilder2 = new FunctionParamBuilder();
    FunctionBuilder functionBuilderMultiple = functionBuilder.withParams(functionParamBuilder1).withParams(functionParamBuilder2);
    assertNotNull(functionBuilderMultiple.build());
  }
}

// Test cases for all methods
// ...