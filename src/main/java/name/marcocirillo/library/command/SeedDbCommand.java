package name.marcocirillo.library.command;

import name.marcocirillo.library.seed.SeedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A command to seed the DB
 *
 * By default will try to seed the DB with `fakeData.sql`.
 * To override the filename, supply --inputFile=somefile.sql
 *
 * Usage: springboot:run seedDb [--inputFile]
 * (or just use the Seed DB run configuration in IntelliJ)
 */
@Component
public class SeedDbCommand implements ApplicationRunner {
    private static final Logger LOG = LoggerFactory.getLogger(SeedDbCommand.class);

    /**
     * File to seed the DB with, overridden by --inputFile="somefile.sql" flag
     */
    private static final String INPUT_FILE = "fakeData.sql";

    private static final String USAGE = "Usage: springboot:run seedDb [--inputFile]\n" +
            "Example: mvn springboot:run seedDb --inputFile=\"somefile.sql\"";

    private final ConfigurableApplicationContext context;
    private final SeedService seedService;

    @Autowired
    public SeedDbCommand(
            ConfigurableApplicationContext context,
            SeedService seedService
    ) {
        this.context = context;
        this.seedService = seedService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!args.getNonOptionArgs().contains("seedDb")) {
            return; // this script was not called
        }

        Path inputFile = Paths.get(INPUT_FILE);
        if (args.containsOption("inputFile")) {
            try {
                inputFile = Paths.get(args.getOptionValues("inputFile").get(0));
            } catch (Exception e) {
                LOG.error(String.format("failed to parse inputFile argument %n%s", USAGE), e);
            }
        }

        LOG.info(String.format("Seeding DB with %s", inputFile));
        try {
            seedService.seed(inputFile);
            LOG.info("Finished seeding, shutting down");
        } catch (Exception e) {
            LOG.error("Seeding failed", e);
        }

        context.close();
    }


}