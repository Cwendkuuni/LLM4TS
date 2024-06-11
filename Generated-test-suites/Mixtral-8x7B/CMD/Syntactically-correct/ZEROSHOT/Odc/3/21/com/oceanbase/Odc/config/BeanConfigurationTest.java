import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.session.CookieSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Mock
    private CloudMetadataClient cloudMetadataClient;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(beanConfiguration, "cloudMetadataClient", cloudMetadataClient);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        Assert.assertNotNull(passwordEncoder);
        Assert.assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() throws Exception {
        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        Assert.assertNotNull(objectMapper);
        Assert.assertEquals(JacksonFactory.unsafeJsonMapper(), objectMapper.getFactory());
        Assert.assertEquals(2, objectMapper.getSerializerProvider().getRegisteredSerializerModules().size());
        Assert.assertTrue(objectMapper.getSerializerProvider().getRegisteredSerializerModules().stream()
                .anyMatch(module -> module.getClass().equals(JacksonModules.SensitiveInputHandlingModule.class)));
        Assert.assertTrue(objectMapper.getSerializerProvider().getRegisteredSerializerModules().stream()
                .anyMatch(module -> module.getClass().equals(JacksonModules.CustomOutputHandlingModule.class)));
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        Assert.assertNotNull(httpFirewall);
        Assert.assertTrue(httpFirewall instanceof DefaultHttpFirewall);
        DefaultHttpFirewall defaultHttpFirewall = (DefaultHttpFirewall) httpFirewall;
        Assert.assertTrue(defaultHttpFirewall.getAllowedHttpMethods().contains("GET"));
        Assert.assertTrue(defaultHttpFirewall.getAllowedHttpMethods().contains("POST"));
        Assert.assertTrue(defaultHttpFirewall.getAllowedUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        when(cloudMetadataClient.supportsCloudMetadata()).thenReturn(false);
        when(cloudMetadataClient.supportsTenantInstance()).thenReturn(false);
        when(cloudMetadataClient.needsOBTenantName()).thenReturn(false);
        when(cloudMetadataClient.needsSysTenantUser()).thenReturn(false);
        when(cloudMetadataClient.includeClusterNameForJdbcUsername())
                .thenReturn(true);
        when(cloudMetadataClient.oceanbaseAccessMode())
                .thenReturn(OceanBaseAccessMode.DIRECT);
        when(cloudMetadataClient.listInstances())
                .thenReturn(new ArrayList<OBInstance>());
        when(cloudMetadataClient.listTenants(anyString()))
                .thenReturn(new ArrayList<OBTenant>());
        when(cloudMetadataClient.getTenant(anyString(), anyString()))
                .thenReturn(null);
        when(cloudMetadataClient.listDatabaseUsers(anyString(), anyString()))
                .thenReturn(new ArrayList<OBDatabaseUser>());
        when(cloudMetadataClient.getTenantEndpoint(anyString(), anyString()))
                .thenReturn(new OBTenantEndpoint());
        when(cloudMetadataClient.getSysTenantUser(anyString()))
                .thenThrow(new UnsupportedException("CloudMetadata not supported"));

        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        Assert.assertNotNull(cloudMetadataClient);
        Assert.assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        Assert.assertNotNull(cookieSerializer);
        Assert.assertTrue(cookieSerializer instanceof DefaultCookieSerializer);
        DefaultCookieSerializer defaultCookieSerializer = (DefaultCookieSerializer) cookieSerializer;
        Assert.assertEquals("JSESSIONID", defaultCookieSerializer.getCookieName());
    }
}