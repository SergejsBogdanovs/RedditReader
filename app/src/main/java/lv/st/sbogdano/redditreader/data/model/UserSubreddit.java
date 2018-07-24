package lv.st.sbogdano.redditreader.data.model;

public class UserSubreddit {

    public String name;

    public UserSubreddit() {
    }

    public UserSubreddit(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
