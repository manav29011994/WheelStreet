package manav.com.wheelstreet;

import java.io.Serializable;

/**
 * Created by manav on 13/5/18.
 */

public class QuestionModel implements Serializable {
    private  String id;
    private  String question;
    private  String dataType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
