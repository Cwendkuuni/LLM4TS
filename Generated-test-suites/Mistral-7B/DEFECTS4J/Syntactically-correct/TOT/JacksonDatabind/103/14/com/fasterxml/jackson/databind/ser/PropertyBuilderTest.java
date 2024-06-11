package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.testng.annotations.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PropertyBuilderTest {

    private ObjectMapper mapper;

    @BeforeClass
    public void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testSimpleProperty() throws Exception {
        BeanDescription beanDesc = mapper.introspect(SimpleBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(), null, SimpleBean.class, null, null, null, null, false);
        ObjectWriter objectWriter = mapper.writerWithView(MyView.class);
        String json = objectWriter.writeValueAsString(new SimpleBean("John"));
        Assert.assertEquals(json, "{\"name\":\"John\"}");
    }

    @Test
    public void testDefaultValue() throws Exception {
        BeanDescription beanDesc = mapper.introspect(DefaultValueBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(), null, DefaultValueBean.class, null, null, null, null, false);
        ObjectWriter objectWriter = mapper.writerWithView(MyView.class);
        String json = objectWriter.writeValueAsString(new DefaultValueBean());
        Assert.assertEquals(json, "{\"intValue\":0,\"stringValue\":\"\"}");
    }

    @Test
    public void testUseRealPropertyDefaults() throws Exception {
        BeanDescription beanDesc = mapper.introspect(UseRealPropertyDefaultsBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        BeanPropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(), null, UseRealPropertyDefaultsBean.class, null, null, null, null, false);
        ObjectWriter objectWriter = mapper.writerWithView(MyView.class);
        String json = objectWriter.writeValueAsString(new UseRealPropertyDefaultsBean());
        Assert.assertEquals(json, "{\"intValue\":1,\"stringValue\":\"test\"}");
        Assert.assertEquals(propertyBuilder.getPropertyDefaultValue("intValue", null, UseRealPropertyDefaultsBean.class.getType()), 1);
        Assert.assertEquals(propertyBuilder.getPropertyDefaultValue("stringValue", null, UseRealPropertyDefaultsBean.class.getType()), "test");
    }

    @Test
    public void testNonDefaultInclude() throws Exception {
        BeanDescription beanDesc = mapper.introspect(NonDefaultIncludeBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        BeanPropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(), null, NonDefaultIncludeBean.class, null, null, null, null, false);
        ObjectWriter objectWriter = mapper.writerWithView(MyView.class);
        String json = objectWriter.writeValueAsString(new NonDefaultIncludeBean());
        Assert.assertEquals(json, "{}");
        Assert.assertNull(propertyBuilder.getPropertyDefaultValue("intValue", null, NonDefaultIncludeBean.class.getType()));
        Assert.assertNull(propertyBuilder.getPropertyDefaultValue("stringValue", null, NonDefaultIncludeBean.class.getType()));
    }

    @Test
    public void testNullSerialization() throws Exception {
        BeanDescription beanDesc = mapper.introspect(NullSerializationBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(), null, NullSerializationBean.class, null, null, null, null, false);
        ObjectWriter objectWriter = mapper.writerWithView(MyView.class);
        String json1 = objectWriter.writeValueAsString(new NullSerializationBean(null));
        Assert.assertEquals(json1, "{}");
        String json2 = objectWriter.writeValueAsString(new NullSerializationBean(null, null));
        Assert.assertEquals(json2, "{\"inner\":null}");
    }

    @Test
    public void testCustomNullSerializer() throws Exception {
        BeanDescription beanDesc = mapper.introspect(CustomNullSerializerBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(), null, CustomNullSerializerBean.class, null, null, null, null, false);
        ObjectWriter objectWriter = mapper.writerWithView(MyView.class);
        String json = objectWriter.writeValueAsString(new CustomNullSerializerBean(null));
        Assert.assertEquals(json, "null");
    }

    @Test
    public void testIgnoreProperties() throws Exception {
        BeanDescription beanDesc = mapper.introspect(IgnorePropertiesBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(), null, IgnorePropertiesBean.class, null, null, null, null, false);
        ObjectWriter objectWriter = mapper.writerWithView(MyView.class);
        String json = objectWriter.writeValueAsString(new IgnorePropertiesBean());
        Assert.assertEquals(json, "{\"notNullString\":\"test\"}");
    }

    @Test
    public void testEmptyCollections() throws Exception {
        BeanDescription beanDesc = mapper.introspect(EmptyCollectionsBean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(), null, EmptyCollectionsBean.class, null, null, null, null, false);
        ObjectWriter objectWriter = mapper.writerWithView(MyView.class);
        String json1 = objectWriter.writeValueAsString(new EmptyCollectionsBean());
        Assert.assertEquals(json1, "{}");
        String json2 = objectWriter.writeValueAsString(new EmptyCollectionsBean(Collections.emptyList(), Collections.emptyList()));
        Assert.assertEquals(json2, "{\"list\":[],\"set\":[]}");
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    static class SimpleBean {
        private String name;

        public SimpleBean(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    static class DefaultValueBean {
        private int intValue = 0;
        private String stringValue = "";

        public DefaultValueBean() {
        }

        public int getIntValue() {
            return intValue;
        }

        public String getStringValue() {
            return stringValue;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    static class UseRealPropertyDefaultsBean {
        private int intValue = 1;
        private String stringValue = "test";

        public UseRealPropertyDefaultsBean() {
        }

        public int getIntValue() {
            return intValue;
        }

        public String getStringValue() {
            return stringValue;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    static class NonDefaultIncludeBean {
        private Integer intValue;
        private String stringValue;

        public NonDefaultIncludeBean() {
        }

        public Integer getIntValue() {
            return intValue;
        }

        public String getStringValue() {
            return stringValue;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class NullSerializationBean {
        private Integer intValue;
        private NullSerializationInner inner;

        public NullSerializationBean(Integer intValue, NullSerializationInner inner) {
            this.intValue = intValue;
            this.inner = inner;
        }

        public Integer getIntValue() {
            return intValue;
        }

        public NullSerializationInner getInner() {
            return inner;
        }
    }

    static class NullSerializationInner {
        // empty class
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class CustomNullSerializerBean {
        private Object customNull;

        public CustomNullSerializerBean(Object customNull) {
            this.customNull = customNull;
        }

        public Object getCustomNull() {
            return customNull;
        }
    }

    static class IgnorePropertiesBean {
        @JsonIgnore
        private String notNullString = "test";

        public String getNotNullString() {
            return notNullString;
        }
    }

    static class EmptyCollectionsBean {
        private List<String> list;
        private Set<String> set;

        public EmptyCollectionsBean() {
        }

        public EmptyCollectionsBean(List<String> list, Set<String> set) {
            this.list = list;
            this.set = set;
        }

        public List<String> getList() {
            return list;
        }

        public Set<String> getSet() {
            return set;
        }
    }

    static class MyView {
    }
}