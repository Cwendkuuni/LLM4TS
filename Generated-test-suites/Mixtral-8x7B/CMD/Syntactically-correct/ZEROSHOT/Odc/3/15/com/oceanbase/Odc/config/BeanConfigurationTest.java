import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.encoding.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {BeanConfiguration.class}, loader = AnnotationConfigContextLoader.class)
public class BeanConfigurationTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpFirewall httpFirewall;

    @Autowired
    private CloudMetadataClient cloudMetadataClient;

    @Autowired
    private CookieSerializer cookieSerializer;

    @Before
    public void setUp() {
        // Initialize mocks
        SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        when(sensitivePropertyHandler.decrypt(anyString())).thenReturn("decrypted");

        Validator validator = mock(LocalValidatorFactoryBean.class);

        // Override bean definitions for testing
        replaceBean(applicationContext, "sensitivePropertyHandler", sensitivePropertyHandler);
        replaceBean(applicationContext, "validator", validator);
    }

    @Test
    public void testPasswordEncoder() {
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        Assert.assertFalse(rawPassword.equals(encodedPassword));

        Assert.assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    public void testObjectMapper() throws Exception {
        String inputJson = "{\"key\":\"value\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        I18nOutputSerializer i18nOutputSerializer = new I18nOutputSerializer();
        objectMapper.registerModule(new JacksonModules.CustomOutputHandling(i18nOutputSerializer));

        Object obj = objectMapper.readValue(inputJson, Object.class);
        assertEquals("value", obj.toString());
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertEquals(true, httpFirewall.getAllowUrlEncodedSlash());
    }

    @Test
    public void testNullCloudMetadataClient() {
        assertEquals(false, cloudMetadataClient.supportsCloudMetadata());
        assertEquals(false, cloudMetadataClient.supportsTenantInstance());
        assertEquals(false, cloudMetadataClient.needsOBTenantName());
        assertEquals(false, cloudMetadataClient.needsSysTenantUser());
        assertEquals(true, cloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());

        Assert.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listInstances());
        Assert.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listTenants(anyString()));
        Assert.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getTenant(anyString(), anyString()));
        Assert.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listDatabaseUsers(anyString(), anyString()));
        Assert.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getTenantEndpoint(anyString(), anyString()));
        Assert.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getSysTenantUser(anyString()));
    }

    @Test
    public void testCookieSerializer() {
        cookieSerializer.afterPropertiesSet();
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }

    private <T> void replaceBean(org.springframework.context.ApplicationContext context, String beanName, T newBean) {
        ((org.springframework.context.support.AbstractApplicationContext) context)
                .getBeanFactory()
                .registerSingleton(beanName, newBean);
    }

    @Configuration
    static class TestConfiguration {

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public ObjectMapper objectMapper(SensitivePropertyHandler sensitivePropertyHandler) {
            CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                    .addSerializer(Internationalizable.class, new I18nOutputSerializer());
            return JacksonFactory.unsafeJsonMapper()
                    .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                    .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
        }

        @Bean
        public HttpFirewall defaultHttpFirewall() {
            DefaultHttpFirewall defaultHttpFirewall = new DefaultHttpFirewall();
            defaultHttpFirewall.setAllowUrlEncodedSlash(true);
            return defaultHttpFirewall;
        }

        @Bean
        @ConditionalOnMissingBean
        public CloudMetadataClient cloudMetadataClient() {
            return new NullCloudMetadataClient();
        }

        @Bean
        @Primary
        public CookieSerializer cookieSerializer() {
            DefaultCookieSerializer serializer = new DefaultCookieSerializer();
            serializer.setCookieName("JSESSIONID");
            return serializer;
        }

        @Bean
        public SensitivePropertyHandler sensitivePropertyHandler() {
            return mock(SensitivePropertyHandler.class);
        }

        @Bean
        public LocalValidatorFactoryBean validator() {
            return mock(LocalValidatorFactoryBean.class);
        }
    }

    static class TestOBInstance implements OBInstance {

        private String id;

        public TestOBInstance(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public void setId(String id) {
            this.id = id;
        }
    }

    static class TestOBTenant implements OBTenant {

        private String id;

        public TestOBTenant(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public void setId(String id) {
            this.id = id;
        }
    }

    static class TestOBDatabaseUser implements OBDatabaseUser {

        private String id;

        public TestOBDatabaseUser(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public void setId(String id) {
            this.id = id;
        }
    }

    static class TestOBTenantEndpoint implements OBTenantEndpoint {

        private OceanBaseAccessMode accessMode;

        public TestOBTenantEndpoint(OceanBaseAccessMode accessMode) {
            this.accessMode = accessMode;
        }

        @Override
        public OceanBaseAccessMode getAccessMode() {
            return accessMode;
        }

        @Override
        public void setAccessMode(OceanBaseAccessMode accessMode) {
            this.accessMode = accessMode;
        }
    }
}