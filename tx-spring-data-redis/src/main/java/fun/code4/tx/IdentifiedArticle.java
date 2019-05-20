package fun.code4.tx;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class IdentifiedArticle {

    @JsonIgnore
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
