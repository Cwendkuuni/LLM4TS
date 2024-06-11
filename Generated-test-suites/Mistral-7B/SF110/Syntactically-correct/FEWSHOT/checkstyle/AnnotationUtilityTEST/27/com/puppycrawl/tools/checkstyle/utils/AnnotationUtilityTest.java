package com.puppycrawl.tools.checkstyle.utils;

import static com.google.common.truth.Truth.assertThat; // using Google Truth for assertions
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.common.base.*;
import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation_NullAst() {
        assertThrows(IllegalArgumentException.class, () ->
                AnnotationUtility.containsAnnotation(null, "annotation"));
    }

    @Test
    public void testContainsAnnotation_NullAnnotation() {
        DetailAST ast = createMockAST(); // Assuming you have a method to create a mock DetailAST
        assertThrows(IllegalArgumentException.class, () ->
                AnnotationUtility.containsAnnotation(ast, null));
    }

    @Test
    public void testContainsAnnotation_EmptyAnnotation() {
        DetailAST ast = createMockAST();
        assertThrows(IllegalArgumentException.class, () ->
                AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test
    public void testContainsAnnotation_AstWithoutAnnotation() {
        DetailAST ast = createMockAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testContainsAnnotation_AstWithSingleAnnotation() {
        DetailAST annotationHolder = createMockAnnotationHolder(); // Assuming you have a method to create a mock DetailAST for annotation holder
        DetailAST ast = new DetailAST() {
            @Override
            public DetailAST getFirstChild() {
                return annotationHolder;
            }
        };
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testContainsAnnotation_AstWithMultipleAnnotations() {
        DetailAST annotationHolder1 = createMockAnnotationHolder();
        DetailAST annotationHolder2 = createMockAnnotationHolder();
        DetailAST ast = new DetailAST() {
            @Override
            public DetailAST getFirstChild() {
                return annotationHolder1;
            }

            @Override
            public DetailAST getNextSibling() {
                return annotationHolder2;
            }
        };
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation1"));
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation2"));
    }

    @Test
    public void testContainsAnnotationHolder_NullAst() {
        assertThrows(IllegalArgumentException.class, () ->
                AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotationHolder_InvalidAstType() {
        DetailAST ast = createMockAST();
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_AnnotationType155() {
        DetailAST annotationHolder = createMockAnnotationHolder();
        DetailAST ast = new DetailAST() {
            @Override
            public DetailAST getFirstChild() {
                return annotationHolder;
            }

            @Override
            public int getType() {
                return 155;
            }
        };
        assertEquals(annotationHolder, AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_AnnotationType16() {
        DetailAST annotationHolder = createMockAnnotationHolder();
        DetailAST ast = new DetailAST() {
            @Override
            public DetailAST getFirstChild() {
                return annotationHolder;
            }

            @Override
            public int getType() {
                return 16;
            }
        };
        assertEquals(annotationHolder, AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_InvalidAnnotationType() {
        DetailAST ast = createMockAST();
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotation_NullAst() {
        assertThrows(IllegalArgumentException.class, () ->
                AnnotationUtility.getAnnotation(null, "annotation"));
    }

    @Test
    public void testGetAnnotation_NullAnnotation() {
        DetailAST ast = createMockAST();
        assertThrows(IllegalArgumentException.class, () ->
                AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotation_EmptyAnnotation() {
        DetailAST ast = createMockAST();
        assertNull(AnnotationUtility.getAnnotation(ast, ""));
    }

    @Test
    public void testGetAnnotation_AstWithoutAnnotation() {
        DetailAST ast = createMockAST();
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotation_AstWithSingleAnnotation() {
        DetailAST annotation = createMockAnnotation(); // Assuming you have a method to create a mock DetailAST for annotation
        DetailAST annotationHolder = createMockAnnotationHolder();
        DetailAST ast = new DetailAST() {
            @Override
            public DetailAST getFirstChild() {
                return annotationHolder;
            }

            @Override
            public DetailAST findFirstToken(int type) {
                if (type == 158) {
                    return annotation;
                }
                return null;
            }
        };
        assertEquals(annotation, AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotation_AstWithMultipleAnnotations() {
        DetailAST annotation1 = createMockAnnotation();
        DetailAST annotation2 = createMockAnnotation();
        DetailAST annotationHolder1 = createMockAnnotationHolder();
        DetailAST annotationHolder2 = createMockAnnotationHolder();
        DetailAST ast = new DetailAST() {
            @Override
            public DetailAST getFirstChild() {
                return annotationHolder1;
            }

            @Override
            public DetailAST getNextSibling() {
                return annotationHolder2;
            }

            @Override
            public DetailAST findFirstToken(int type) {
                if (type == 158 && getChildIndex(this, annotationHolder1) == 0) {
                    return annotation1;
                }
                if (type == 158 && getChildIndex(this, annotationHolder2) == 1) {
                    return annotation2;
                }
                return null;
            }
        };
        assertThat(AnnotationUtility.getAnnotation(ast, "annotation1")).isEqualTo(annotation1);
        assertThat(AnnotationUtility.getAnnotation(ast, "annotation2")).isEqualTo(annotation2);
    }

    // Add additional test methods if needed for other methods in AnnotationUtility class

    private static DetailAST createMockAnnotation() {
        // Implementation to create a mock DetailAST for annotation
    }

    private static DetailAST createMockAnnotationHolder() {
        // Implementation to create a mock DetailAST for annotation holder
    }

    private static int getChildIndex(DetailAST parent, DetailAST child) {
        // Implementation to get the index of a child DetailAST in its parent DetailAST
    }
}