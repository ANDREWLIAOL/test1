import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class NotepadApp extends JFrame implements ActionListener {
    private List<Note> noteList;
    private DefaultTableModel tableModel;
    private JTable noteTable;
    private JTextField searchField;

    public NotepadApp() {
        noteList = FileUtil.loadNotes();
        initFrame();
        initComponents();
        refreshTable();
    }

    private void initFrame() {
        setTitle("单机版个人记事本");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        JButton searchBtn = new JButton("搜索");
        JButton addBtn = new JButton("新增笔记");
        searchBtn.addActionListener(this);
        addBtn.addActionListener(this);
        topPanel.add(new JLabel("搜索标题："));
        topPanel.add(searchField);
        topPanel.add(searchBtn);
        topPanel.add(addBtn);
        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "标题", "创建时间"};
        tableModel = new DefaultTableModel(columnNames, 0);
        noteTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(noteTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton viewBtn = new JButton("查看详情");
        JButton editBtn = new JButton("修改笔记");
        JButton deleteBtn = new JButton("删除笔记");
        viewBtn.addActionListener(this);
        editBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        bottomPanel.add(viewBtn);
        bottomPanel.add(editBtn);
        bottomPanel.add(deleteBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Note note : noteList) {
            Vector<String> row = new Vector<>();
            row.add(note.getId());
            row.add(note.getTitle());
            row.add(note.getCreateTime());
            tableModel.addRow(row);
        }
    }

    private void searchNotes() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            refreshTable();
            return;
        }
        tableModel.setRowCount(0);
        for (Note note : noteList) {
            if (note.getTitle().contains(keyword)) {
                Vector<String> row = new Vector<>();
                row.add(note.getId());
                row.add(note.getTitle());
                row.add(note.getCreateTime());
                tableModel.addRow(row);
            }
        }
    }

    private void showNoteDialog(Note noteParam) {
        boolean isEdit = noteParam != null;
        Note currentNote = noteParam == null ? new Note() : noteParam;

        JDialog dialog = new JDialog(this, isEdit ? "修改笔记" : "新增笔记", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JTextField titleField = new JTextField(currentNote.getTitle());
        JTextArea contentArea = new JTextArea(currentNote.getContent());
        contentArea.setLineWrap(true);
        inputPanel.add(new JLabel("标题："));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("内容："));
        inputPanel.add(new JScrollPane(contentArea));
        dialog.add(inputPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("保存");
        JButton cancelBtn = new JButton("取消");
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            String content = contentArea.getText().trim();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "标题不能为空！");
                return;
            }
            currentNote.setTitle(title);
            currentNote.setContent(content);
            if (!isEdit) {
                currentNote.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                noteList.add(currentNote);
            }
            FileUtil.saveNotes(noteList);
            refreshTable();
            dialog.dispose();
        });

        cancelBtn.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private Note getSelectedNote() {
        int row = noteTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "请先选择一条笔记！");
            return null;
        }
        String id = tableModel.getValueAt(row, 0).toString();
        for (Note note : noteList) {
            if (note.getId().equals(id)) {
                return note;
            }
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd) {
            case "搜索":
                searchNotes();
                break;
            case "新增笔记":
                showNoteDialog(null);
                break;
            case "查看详情":
                Note viewNote = getSelectedNote();
                if (viewNote != null) {
                    JOptionPane.showMessageDialog(this,
                        "标题：" + viewNote.getTitle() + "\n\n内容：\n" + viewNote.getContent(),
                        "笔记详情", JOptionPane.INFORMATION_MESSAGE);
                }
                break;
            case "修改笔记":
                Note editNote = getSelectedNote();
                if (editNote != null) showNoteDialog(editNote);
                break;
            case "删除笔记":
                Note delNote = getSelectedNote();
                if (delNote != null) {
                    int confirm = JOptionPane.showConfirmDialog(this, "确定删除该笔记？");
                    if (confirm == JOptionPane.YES_OPTION) {
                        noteList.remove(delNote);
                        FileUtil.saveNotes(noteList);
                        refreshTable();
                    }
                }
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NotepadApp().setVisible(true));
    }
}