package name.marcocirillo.library.command;

import name.marcocirillo.library.fakedata.FakeDataGenerator;
import name.marcocirillo.library.fakedata.mapper.FakeDataToSqlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A command to generate fake books, authors, etc
 *
 * By default will save 10M books to `fakeData.sql`.
 *
 * To override the number of books, supply --numBooks=123
 * To override the filename, supply --outputFile=somefile.sql
 *
 * Usage: springboot:run generateFakeData [--numBooks] [--outputFile]
 * (or just use the Generate Fake Data run configuration in IntelliJ)
 */
@Component
public class GenerateFakeDataCommand implements ApplicationRunner {
    private static final Logger LOG = LoggerFactory.getLogger(GenerateFakeDataCommand.class);

    /**
     * Number of books to generate, overridden by --numBooks=123 flag
     */
    private static final int NUM_BOOKS = 10_000_000;

    /**
     * File to generate SQL INSERT statements into, overridden by --outputFile="somefile.sql" flag
     */
    private static final String OUTPUT_FILE = "fakeData.sql";

    private static final String USAGE = "Usage: springboot:run generateFakeData [--numBooks] [--outputFile]\n" +
            "Example: mvn springboot:run generateFakeData --numBooks=123 --outputFile=\"somefile.sql\"";

    private final ConfigurableApplicationContext context;
    private final FakeDataGenerator fakeDataGenerator;
    private final FakeDataToSqlMapper fakeDataToSqlMapper;

    @Autowired
    public GenerateFakeDataCommand(
            ConfigurableApplicationContext context,
            FakeDataGenerator fakeDataGenerator,
            FakeDataToSqlMapper fakeDataToSqlMapper
    ) {
        this.context = context;
        this.fakeDataGenerator = fakeDataGenerator;
        this.fakeDataToSqlMapper = fakeDataToSqlMapper;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!args.getNonOptionArgs().contains("generateFakeData")) {
            return; // this script was not called
        }

        int numBooks = NUM_BOOKS;
        if (args.containsOption("numBooks")) {
            try {
                numBooks = Integer.parseInt(args.getOptionValues("numBooks").get(0));
            } catch (Exception e) {
                LOG.error(String.format("failed to parse numBooks argument %n%s", USAGE), e);
            }
        }

        Path outputFile = Paths.get(OUTPUT_FILE);
        if (args.containsOption("outputFile")) {
            try {
                outputFile = Paths.get(args.getOptionValues("outputFile").get(0));
            } catch (Exception e) {
                LOG.error(String.format("failed to parse outputFile argument %n%s", USAGE), e);
            }
        }

        LOG.info(String.format("Generating %d fake books into %s", numBooks, outputFile));
        try(PrintWriter pw = new PrintWriter(Files.newBufferedWriter(outputFile))) {
            fakeDataGenerator.generate(numBooks)
                    .map(fakeDataToSqlMapper)
                    .forEach(pw::println);
        }
        LOG.info(String.format("Finished generating %d fake books into %s, shutting down", numBooks, outputFile));
        context.close();
    }


}