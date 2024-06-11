import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.oceanbase.odc.service.session.CookieSerializer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.CookieSerializer;

import java.util.ArrayList;
import java.util.List;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration;
    private SensitivePropertyHandler sensitivePropertyHandler;
    private CloudMetadataClient cloudMetadataClient;

    @Before
    public void setUp() {
        sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        cloudMetadataClient = mock(CloudMetadataClient.class);

        beanConfiguration = new BeanConfiguration() {
            @Override
            public CloudMetadataClient cloudMetadataClient() {
                return cloudMetadataClient;
            }

            @Override
            public SensitivePropertyHandler sensitivePropertyHandler() {
                return sensitivePropertyHandler;
            }
        };
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertEquals(BCryptPasswordEncoder.class, passwordEncoder.getClass());
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);

        CustomOutputSerializer customOutputSerializer = JacksonModules.getCustomOutputSerializer(objectMapper);
        assertNotNull(customOutputSerializer);
        assertEquals(I18nOutputSerializer.class, customOutputSerializer.getSerializer(Internationalizable.class).getClass());
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(httpFirewall);
        assertEquals(DefaultHttpFirewall.class, httpFirewall.getClass());
    }

    @Test
    public void testNullCloudMetadataClient() {
        when(cloudMetadataClient.supportsCloudMetadata()).thenReturn(false);

        CloudMetadataClient cloudMetadataClientBean = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClientBean);
        assertEquals(NullCloudMetadataClient.class, cloudMetadataClientBean.getClass());
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }

    @Test
    public void testNullCloudMetadataClientSupportsTenantInstance() {
        when(cloudMetadataClient.supportsTenantInstance()).thenReturn(false);

        CloudMetadataClient cloudMetadataClientBean = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClientBean);
        assertEquals(NullCloudMetadataClient.class, cloudMetadataClientBean.getClass());
    }

    @Test
    public void testNullCloudMetadataClientNeedsOBTenantName() {
        when(cloudMetadataClient.needsOBTenantName()).thenReturn(false);

        CloudMetadataClient cloudMetadataClientBean = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClientBean);
        assertEquals(NullCloudMetadataClient.class, cloudMetadataClientBean.getClass());
    }

    @Test
    public void testNullCloudMetadataClientNeedsSysTenantUser() {
        when(cloudMetadataClient.needsSysTenantUser()).thenReturn(false);

        CloudMetadataClient cloudMetadataClientBean = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClientBean);
        assertEquals(NullCloudMetadataClient.class, cloudMetadataClientBean.getClass());
    }

    @Test
    public void testNullCloudMetadataClientIncludeClusterNameForJdbcUsername() {
        when(cloudMetadataClient.includeClusterNameForJdbcUsername()).thenReturn(false);

        CloudMetadataClient cloudMetadataClientBean = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClientBean);
        assertEquals(NullCloudMetadataClient.class, cloudMetadataClientBean.getClass());
    }

    @Test
    public void testNullCloudMetadataClientOceanbaseAccessMode() {
        when(cloudMetadataClient.oceanbaseAccessMode()).thenReturn(OceanBaseAccessMode.DIRECT);

        CloudMetadataClient cloudMetadataClientBean = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClientBean);
        assertEquals(NullCloudMetadataClient.class, cloudMetadataClientBean.getClass());
    }

    @Test
    public void testNullCloudMetadataClientListInstances() {
        when(cloudMetadataClient.listInstances()).thenThrow(new UnsupportedException("CloudMetadata not supported"));

        CloudMetadataClient cloudMetadataClientBean = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClientBean);
        assertEquals(NullCloudMetadataClient.class, cloudMetadataClientBean.getClass());
    }

    @Test
    public void testNullCloudMetadataClientListTenants() {
        when(cloudMetadataClient.listTenants("instanceId")).thenThrow(new UnsupportedException("CloudMetadata not supported"));

        CloudMetadataClient cloudMetadataClientBean = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClientBean);
        assertEquals(NullCloudMetadataClient.class, cloudMetadataClientBean.getClass());
    }

    @Test
    public void testNullCloudMetadataClientGetTenant() {
        when(cloudMetadataClient.getTenant("instanceId", "tenantId"))
                .thenThrow(new UnsupportedException("getTenant not supported in non-could"));

        CloudMetadataClient cloudMetadataClientBean = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClientBean);
        assertEquals(NullCloudMetadataClient.class, cloudMetadataClientBean.getClass());
    }

    @Test
    public void testNullCloudMetadataClientListDatabaseUsers() {
        when(cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"))
                .thenThrow(new UnsupportedException("CloudMetadata not supported"));

        CloudMetadataClient cloudMetadataClientBean = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClientBean);
        assertEquals(NullCloudMetadataClient.class, cloudMetadataClientBean.getClass());
    }

    @Test
    public void testNullCloudMetadataClientGetTenantEndpoint() {
        OBTenantEndpoint tenantEndpoint = beanConfiguration.cloudMetadataClient().getTenantEndpoint("instanceId", "tenantId");
        assertNotNull(tenantEndpoint);
        assertEquals(OceanBaseAccessMode.DIRECT, tenantEndpoint.getAccessMode());
    }

    @Test
    public void testNullCloudMetadataClientGetSysTenantUser() {
        when(cloudMetadataClient.getSysTenantUser("instanceId"))
                .thenThrow(new UnsupportedException("CloudMetadata not supported"));

        CloudMetadataClient cloudMetadataClientBean = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClientBean);
        assertEquals(NullCloudMetadataClient.class, cloudMetadataClientBean.getClass());
    }

    @Test
    public void testNullCloudMetadataClientSupportsTenantInstance() {
        when(cloudMetadataClient.supportsTenantInstance()).thenReturn(true);

        assertThrows(UnsupportedException.class, () -> beanConfiguration.cloudMetadataClient().listInstances());
    }

    @Test
    public void testNullCloudMetadataClientNeedsOBTenantName() {
        when(cloudMetadataClient.needsOBTenantName()).thenReturn(true);

        assertThrows(UnsupportedException.class, () -> beanConfiguration.cloudMetadataClient().listTenants("instanceId"));
    }

    @Test
    public void testNullCloudMetadataClientNeedsSysTenantUser() {
        when(cloudMetadataClient.needsSysTenantUser()).thenReturn(true);

        assertThrows(UnsupportedException.class, () -> beanConfiguration.cloudMetadataClient().getSysTenantUser("instanceId"));
    }
}