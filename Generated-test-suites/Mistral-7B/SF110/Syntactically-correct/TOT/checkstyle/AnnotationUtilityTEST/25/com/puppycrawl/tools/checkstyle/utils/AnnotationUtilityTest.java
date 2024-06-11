package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.*;
import org.junit.*;

public class AnnotationUtilityTest {
   private DetailAST ast;

   @Before
   public void setUp() {
      ast = new DetailAST();
   }

   @Test
   public void testContainsAnnotation() {
      // Test valid usage
      ast.setType(155);
      DetailAST annotationHolder = new DetailAST();
      annotationHolder.setType(158);
      ast.addChild(annotationHolder);
      DetailAST annotation = new DetailAST();
      annotation.setType(159);
      annotation.setText("AnnotationTest");
      annotationHolder.addChild(annotation);
      assertTrue(AnnotationUtility.containsAnnotation(ast, "AnnotationTest"));

      // Test null ast
      ast = null;
      try {
         AnnotationUtility.containsAnnotation(ast, "AnnotationTest");
      } catch (IllegalArgumentException e) {
         assertEquals("the ast is null", e.getMessage());
      }

      // Test empty ast
      ast = new DetailAST();
      assertFalse(AnnotationUtility.containsAnnotation(ast, "AnnotationTest"));

      // Test null annotation
      String annotationName = null;
      try {
         AnnotationUtility.containsAnnotation(ast, annotationName);
      } catch (IllegalArgumentException e) {
         assertEquals("the annotation is null", e.getMessage());
      }

      // Test empty annotation
      annotationName = " ";
      try {
         AnnotationUtility.containsAnnotation(ast, annotationName);
      } catch (IllegalArgumentException e) {
         assertEquals("the annotation is empty or spaces", e.getMessage());
      }
   }

   @Test
   public void testContainsAnnotationWithHolder() {
      // Test valid usage
      ast.setType(155);
      DetailAST annotationHolder = new DetailAST();
      annotationHolder.setType(158);
      ast.addChild(annotationHolder);
      DetailAST annotation = new DetailAST();
      annotation.setType(159);
      annotation.setText("AnnotationTest");
      annotationHolder.addChild(annotation);
      assertTrue(AnnotationUtility.containsAnnotation(ast));

      // Test null ast
      ast = null;
      try {
         AnnotationUtility.containsAnnotation(ast);
      } catch (IllegalArgumentException e) {
         assertEquals("the ast is null", e.getMessage());
      }

      // Test empty ast
      ast = new DetailAST();
      assertFalse(AnnotationUtility.containsAnnotation(ast));
   }

   @Test
   public void testGetAnnotationHolder() {
      // Test valid usage
      ast.setType(155);
      DetailAST annotationHolder = new DetailAST();
      annotationHolder.setType(158);
      ast.addChild(annotationHolder);
      DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
      assertEquals(annotationHolder, result);

      // Test null ast
      ast = null;
      try {
         AnnotationUtility.getAnnotationHolder(ast);
      } catch (IllegalArgumentException e) {
         assertEquals("the ast is null", e.getMessage());
      }

      // Test empty ast
      ast = new DetailAST();
      assertNull(AnnotationUtility.getAnnotationHolder(ast));

      // Test non-annotation holding node type
      ast.setType(156);
      DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
      assertNull(result);
   }

   @Test
   public void testGetAnnotation() {
      // Test valid usage
      ast.setType(155);
      DetailAST annotationHolder = new DetailAST();
      annotationHolder.setType(158);
      ast.addChild(annotationHolder);
      DetailAST annotation = new DetailAST();
      annotation.setType(159);
      annotation.setText("AnnotationTest");
      annotationHolder.addChild(annotation);
      DetailAST result = AnnotationUtility.getAnnotation(ast, "AnnotationTest");
      assertEquals(annotation, result);

      // Test null ast
      ast = null;
      try {
         AnnotationUtility.getAnnotation(ast, "AnnotationTest");
      } catch (IllegalArgumentException e) {
         assertEquals("the ast is null", e.getMessage());
      }

      // Test empty ast
      ast = new DetailAST();
      try {
         AnnotationUtility.getAnnotation(ast, "AnnotationTest");
      } catch (IllegalArgumentException e) {
         assertEquals("the ast is null", e.getMessage());
      }

      // Test non-existent annotation
      ast.setType(155);
      DetailAST annotationHolder = new DetailAST();
      annotationHolder.setType(158);
      ast.addChild(annotationHolder);
      assertNull(AnnotationUtility.getAnnotation(ast, "NonExistentAnnotation"));

      // Test non-annotation node type
      ast.setType(156);
      try {
         AnnotationUtility.getAnnotation(ast, "AnnotationTest");
      } catch (IllegalArgumentException e) {
         assertEquals("the ast is null", e.getMessage());
      }
   }
}