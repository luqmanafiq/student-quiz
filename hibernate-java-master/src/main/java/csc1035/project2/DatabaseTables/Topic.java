package csc1035.project2.DatabaseTables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Identifies a topic linked to a question.
 */
@Entity
@Table(name = "tblTopic")
public class Topic {
    @Id
    @Column(name = "TopicName", nullable = false, length = 50)
    private String id;

    @Column(name = "TopicDescription")
    private String topicDescription;

    public Topic() {
    }

    public Topic(String id, String topicDescription) {
        this.id = id;
        this.topicDescription = topicDescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }

}