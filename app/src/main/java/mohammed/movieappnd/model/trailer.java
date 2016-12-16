package mohammed.movieappnd.model;

/**
 * Created by gmgn on 4/22/2016.
 */
public class trailer {
    String key, name;

    public trailer() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = "https://www.youtube.com/watch?v=" + key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
