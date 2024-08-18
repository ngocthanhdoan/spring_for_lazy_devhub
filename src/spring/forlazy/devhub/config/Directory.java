package spring.forlazy.devhub.config;

public enum Directory {
    JAVA_SOURCE("src/main/java"),
    RESOURCES("src/main/resources"),
    STATIC_RESOURCES("src/main/resources/static"),
    TEMPLATES("src/main/resources/templates"),
    TEST_JAVA_SOURCE("src/test/java"),
    TEST_RESOURCES("src/test/resources");

    private final String path;

    Directory(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}