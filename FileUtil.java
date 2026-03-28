import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    private static final String FILE_PATH = "notes.dat";

    // 保存笔记列表到本地文件
    public static void saveNotes(List<Note> noteList) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(noteList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 从本地文件加载笔记列表
    @SuppressWarnings("unchecked")
    public static List<Note> loadNotes() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<Note>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}