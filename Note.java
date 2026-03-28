import java.io.Serializable;
import java.util.UUID;

public class Note implements Serializable {
    private String id;
    private String title;
    private String content;
    private String createTime;

    public Note() {
        this.id = UUID.randomUUID().toString();
    }

    public Note(String title, String content, String createTime) {
        this();
        this.title = title;
        this.content = content;
        this.createTime = createTime;
    }

    // Getter & Setter
    public String getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
}