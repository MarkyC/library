package name.marcocirillo.library.fakedata;

import java.util.stream.Stream;

public interface FakeDataGenerator {
    Stream<FakeData> generate(int numBooks);
}
