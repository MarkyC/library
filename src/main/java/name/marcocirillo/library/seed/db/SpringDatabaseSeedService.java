package name.marcocirillo.library.seed.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.nio.file.Path;
import java.sql.SQLException;

@Service
public class SpringDatabaseSeedService implements DatabaseSeedService {
    private final DataSource dataSource;

    @Autowired
    public SpringDatabaseSeedService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Transactional(rollbackOn = Exception.class) // rollback on any errors
    @Override
    public void seed(Path sqlFile) throws SQLException {
        ScriptUtils.executeSqlScript(dataSource.getConnection(), new FileSystemResource(sqlFile));
    }
}
