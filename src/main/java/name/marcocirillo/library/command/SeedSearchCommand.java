package name.marcocirillo.library.command;

import name.marcocirillo.library.seed.SeedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * A command to seed the search index
 *
 * Usage: springboot:run seedSearch
 * (or just use the Seed Search run configuration in IntelliJ)
 */
@Component
public class SeedSearchCommand implements ApplicationRunner {
    private static final Logger LOG = LoggerFactory.getLogger(SeedSearchCommand.class);

    private final ConfigurableApplicationContext context;
    private final SeedService seedService;

    @Autowired
    public SeedSearchCommand(
            ConfigurableApplicationContext context,
            SeedService seedService
    ) {
        this.context = context;
        this.seedService = seedService;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!args.getNonOptionArgs().contains("seedSearch")) {
            return; // this script was not called
        }

        LOG.info("Seeding search index");
        try {
            seedService.seedSearch();
            LOG.info("Finished seeding, shutting down");
        } catch (Exception e) {
            LOG.error("Seeding failed", e);
        }

        context.close();
    }


}