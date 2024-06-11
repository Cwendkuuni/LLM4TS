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
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@ContextConfiguration(classes = BeanConfiguration.class)
@ActiveProfiles("test")
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

    @Autowired
    private NullCloudMetadataClient nullCloudMetadataClient;

    @Test
    public void testPasswordEncoder() {
        String password = "test";
        String encodedPassword = passwordEncoder.encode(password);
        Assert.assertFalse(password.equals(encodedPassword));
        Assert.assertTrue(passwordEncoder.matches(password, encodedPassword));
    }

    @Test
    public void testObjectMapper() {
        CustomOutputSerializer customOutputSerializer = objectMapper.getSerializationConfig().getSerializationProblemHandler()
                .getSerializer(I18nOutputSerializer.class);
        assertNotNull(customOutputSerializer);

        SensitivePropertyHandler sensitivePropertyHandler = new SensitivePropertyHandler() {
            @Override
            public String decrypt(String encryptedValue) {
                return null;
            }
        };
        ObjectMapper objectMapperWithSensitiveHandler = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
        assertEquals(objectMapper, objectMapperWithSensitiveHandler);
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertEquals(DefaultHttpFirewall.class, httpFirewall.getClass());
        httpFirewall.setAllowUrlEncodedSlash(true);
        assertEquals(true, httpFirewall.getAllowUrlEncodedSlash());
    }

    @Test
    public void testNullCloudMetadataClient() {
        Assert.assertTrue(nullCloudMetadataClient instanceof BeanConfiguration.NullCloudMetadataClient);
        Assert.assertFalse(cloudMetadataClient.supportsCloudMetadata());
        Assert.assertFalse(cloudMetadataClient.supportsTenantInstance());
        Assert.assertFalse(cloudMetadataClient.needsOBTenantName());
        Assert.assertFalse(cloudMetadataClient.needsSysTenantUser());
        Assert.assertTrue(cloudMetadataClient.includeClusterNameForJdbcUsername());
        Assert.assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());

        Assert.assertThrows(UnsupportedException.class,
                () -> cloudMetadataClient.listInstances());
        Assert.assertThrows(UnsupportedException.class,
                () -> cloudMetadataClient.listTenants("instanceId"));
        Assert.assertThrows(UnsupportedException.class,
                () -> cloudMetadataClient.getTenant("instanceId", "tenantId"));
        Assert.assertThrows(UnsupportedException.class,
                () -> cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
        Assert.assertThrows(UnsupportedException.class,
                () -> cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"));
        Assert.assertThrows(UnsupportedException.class,
                () -> cloudMetadataClient.getSysTenantUser("instanceId"));
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer serializer = cookieSerializer();
        assertEquals("JSESSIONID", serializer.getCookieName());
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
    }
}