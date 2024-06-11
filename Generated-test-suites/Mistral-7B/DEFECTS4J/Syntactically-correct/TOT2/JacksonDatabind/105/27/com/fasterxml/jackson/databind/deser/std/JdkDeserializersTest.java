package com.example.junit.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers;

public class JdkDeserializersTest {

  @Test
  void testFind() {
    JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, "java.util.UUID");
    Assertions.assertNotNull(deserializer);
    Assertions.assertTrue(deserializer instanceof JdkDeserializers.UUIDDeserializer);

    deserializer = JdkDeserializers.find(String.class, "java.lang.String");
    Assertions.assertNull(deserializer);

    deserializer = JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer");
    Assertions.assertNotNull(deserializer);
    Assertions.assertTrue(deserializer instanceof JdkDeserializers.ByteBufferDeserializer);
  }

  @Test
  void testFindInvalidClassName() {
    Assertions.assertNull(JdkDeserializers.find(UUID.class, "java.invalid.ClassName"));
  }

  @Test
  void testFindNullRawType() {
    Assertions.assertNull(JdkDeserializers.find(null, "java.util.UUID"));
  }

  @Test
  void testFindNullClsName() {
    Assertions.assertNull(JdkDeserializers.find(UUID.class, null));
  }

  @Test
  void testFindUnknownClass() {
    Assertions.assertNull(JdkDeserializers.find(new Object().getClass(), "java.unknown.Class"));
  }

  @Test
  void testFindNullClassNames() {
    _classNames.clear();
    Assertions.assertNull(JdkDeserializers.find(UUID.class, "java.util.UUID"));
  }
}