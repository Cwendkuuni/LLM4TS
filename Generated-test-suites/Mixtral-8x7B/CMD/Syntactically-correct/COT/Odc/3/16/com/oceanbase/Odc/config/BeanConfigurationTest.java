import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Test
    public void testPasswordEncoderBean() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertEquals(BCryptPasswordEncoder.class, passwordEncoder.getClass());
    }

    @Test
    public void testObjectMapperBean() throws Exception {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
        // Add more tests to ensure custom serializers are registered correctly
    }

    @Test
    public void testDefaultHttpFirewallBean() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertEquals(DefaultHttpFirewall.class, defaultHttpFirewall.getClass());
    }

    @Test
    public void testCloudMetadataClientBean() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertEquals(NullCloudMetadataClient.class, cloudMetadataClient.getClass());
    }

    @Test
    public void testCookieSerializerBean() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertNotNull(nullCloudMetadataClient);

        // Test various methods of NullCloudMetadataClient
        when(sensitivePropertyHandler.decrypt("secret")).thenReturn("decryptedSecret");
        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());
        ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
        nullCloudMetadataClient.setObjectMapper(objectMapper);

        assertEquals(false, nullCloudMetadataClient.supportsCloudMetadata());
        assertEquals(false, nullCloudMetadataClient.supportsTenantInstance());
        assertEquals(false, nullCloudMetadataClient.needsOBTenantName());
        assertEquals(false, nullCloudMetadataClient.needsSysTenantUser());
        assertEquals(true, nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());

        List<OBInstance> instances = new ArrayList<>();
        instances.add(new OBInstance());
        when(nullCloudMetadataClient.listInstances()).thenReturn(instances);
        assertEquals(instances, nullCloudMetadataClient.listInstances());

        List<OBTenant> tenants = new ArrayList<>();
        tenants.add(new OBTenant());
        when(nullCloudMetadataClient.listTenants("instanceId")).thenReturn(tenants);
        assertEquals(tenants, nullCloudMetadataClient.listTenants("instanceId"));

        OBTenantEndpoint endpoint = new OBTenantEndpoint();
        endpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
        when(nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"))
                .thenReturn(endpoint);
        assertEquals(endpoint, nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"));

        OBDatabaseUser sysTenantUser = new OBDatabaseUser();
        when(nullCloudMetadataClient.getSysTenantUser("instanceId")).thenReturn(sysTenantUser);
        assertEquals(sysTenantUser, nullCloudMetadataClient.getSysTenantUser("instanceId"));
    }
}