package io.andromeda.fragments.feeds;

public enum FeedType {
    RSS_0_9("rss_0.9"),
    RSS_0_91("rss_0.91"),
    RSS_0_92("rss_0.92"),
    RSS_0_93("rss_0.93"),
    RSS_0_94("rss_0.94"),
    RSS_1_0("rss_1.0"),
    RSS_2_0("rss_2.0"),
    ATOM_3_0("atom_0.3");

    private final String name;

    private FeedType(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return name.equals(otherName);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
