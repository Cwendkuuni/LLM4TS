package com.puppycrawl.tools.checkstyle.utils;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidAnnotation() {
        DetailAST ast = createMockAST();
        String annotation = "MyAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotationWithNullAST() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(null, "MyAnnotation"));
    }

    @Test
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = createMockAST();
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, null));
    }

    @Test
    public void testContainsAnnotationWithEmptyAnnotation() {
        DetailAST ast = createMockAST();
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test
    public void testContainsAnnotationWithNonExistentAnnotation() {
        DetailAST ast = createMockAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "NonExistentAnnotation"));
    }

    @Test
    public void testGetAnnotationHolderWithValidASTType() {
        DetailAST ast = createMockASTWithAnnotation();
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertThat(annotationHolder).isNotNull();
        assertThat(annotationHolder.getType()).isEqualTo(TokenTypes.ANNOTATION);
    }

    @Test
    public void testGetAnnotationHolderWithInvalidASTType() {
        DetailAST ast = createMockAST();
        assertThrows(IllegalStateException.class, () -> AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolderWithNullAST() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotationWithValidASTAndAnnotation() {
        DetailAST ast = createMockASTWithAnnotation();
        String annotation = "MyAnnotation";
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertThat(result).isNotNull();
        assertThat(result.getText()).isEqualTo(annotation);
    }

    @Test
    public void testGetAnnotationWithNullAST() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, "MyAnnotation"));
    }

    @Test
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = createMockASTWithAnnotation();
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotationWithInvalidAnnotation() {
        DetailAST ast = createMockASTWithAnnotation();
        String annotation = "InvalidAnnotation";
        assertNull(AnnotationUtility.getAnnotation(ast, annotation));
    }

    @Test
    public void testGetAnnotationWithWhitespaceAnnotation() {
        DetailAST ast = createMockASTWithAnnotation();
        String annotation = "";
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(ast, annotation));
    }

    private DetailAST createMockAST() {
        return new DetailAST() {
            @Override
            public int getType() {
                return 1; // Mock arbitrary type
            }
        };
    }

    private DetailAST createMockASTWithAnnotation() {
        DetailAST annotation = new DetailAST() {
            @Override
            public int getType() {
                return TokenTypes.ANNOTATION;
            }

            @Override
            public DetailAST findFirstToken(int type) {
                return new DetailAST() {
                    @Override
                    public int getType() {
                        return type;
                    }

                    @Override
                    public DetailAST getFirstChild() {
                        return new DetailAST() {
                            @Override
                            public int getType() {
                                return TokenTypes.ANNOTATION;
                            }

                            @Override
                            public String getText() {
                                return "MyAnnotation";
                            }
                        };
                    }
                };
            }
        };
        DetailAST ast = new DetailAST() {
            @Override
            public int getType() {
                return 155; // Mock arbitrary type
            }

            @Override
            public DetailAST findFirstToken(int type) {
                if (type == TokenTypes.ANNOTATION) {
                    return annotation;
                }
                return null;
            }
        };
        return ast;
    }
}