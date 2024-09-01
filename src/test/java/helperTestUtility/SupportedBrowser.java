package helperTestUtility;

public enum SupportedBrowser {
    CHROMIUM("chromium"),WEBKIT("webkit"),FIREFOX("firefox"),CHROME("chrome"),EDGE("msedge");

    private final String browserName;

    // Constructor to initialize the browser name
    SupportedBrowser(String browserName) {
        this.browserName = browserName;
    }

    // Getter method to retrieve the browser name
    public String getBrowserName() {
        return browserName;
    }

    @Override
    public String toString() {
        return this.browserName;
    }
}
