package draw_it.data.message;

import java.util.Date;

public class WordMessage extends Message {

    private String word;

    public WordMessage(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}
