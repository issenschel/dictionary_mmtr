package pojo;

import java.util.ArrayList;
import java.util.List;

public class KeyValuePairGroup {
    private List<KeyValuePair> dictionary;
    private Integer count;

    public KeyValuePairGroup() {
        dictionary = new ArrayList<>();
        count = 0;
    }


    public KeyValuePairGroup(List<KeyValuePair> keyValuePairList, Integer count) {
        this.dictionary = keyValuePairList;
        this.count = count;
    }

    public List<KeyValuePair> getDictionary() {
        return dictionary;
    }

    public void setDictionary(List<KeyValuePair> dictionary) {
        this.dictionary = dictionary;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}

