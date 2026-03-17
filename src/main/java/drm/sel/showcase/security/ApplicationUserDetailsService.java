package drm.sel.showcase.security;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.UUID;

@Component
public class ApplicationUserDetailsService
        extends MappingSqlQuery<ApplicationUser>
        implements UserDetailsService {

    public ApplicationUserDetailsService(DataSource dataSource) {
        super(dataSource, "SELECT id, c_username, c_password FROM t_application_user WHERE c_username = :username");
        this.declareParameter(new SqlParameter("username", Types.VARCHAR));
        this.compile(); // to make that sqlQuery object immutable
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return this.executeByNamedParam(Map.of("username", username)).stream().findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find user: " + username));
    }

    @Override
    protected ApplicationUser mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return new ApplicationUser(
                rs.getObject("id", UUID.class),
                rs.getString("c_username"),
                rs.getString("c_password")
        );
    }
}
