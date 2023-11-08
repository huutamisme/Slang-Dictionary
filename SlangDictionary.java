import javax.print.attribute.standard.JobName;
import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.function.BiPredicate;

public class SlangDictionary {
    private HashMap<String, SlangDefinitionList> dictionary;
    private List<String> searchedSlang;

    public SlangDictionary(){
        this.dictionary = new HashMap<>();
        this.searchedSlang = new ArrayList<>();
    }

    public SlangDictionary(SlangDictionary a){
        this.dictionary = a.dictionary;
    }

    public SlangDictionary(HashMap<String, SlangDefinitionList> a){
        this.dictionary = a;
    }

    public void ReadFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] arr = line.split("`");
                if (arr.length == 2) {
                    String slang = arr[0];
                    String definitions = arr[1];
                    String[] definitionArray = definitions.split("\\|");
                    SlangDefinitionList def_list = new SlangDefinitionList();
                    Collections.addAll(def_list, definitionArray);
                    this.dictionary.put(slang, def_list);
                }
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Map.Entry<String, SlangDefinitionList> entry : dictionary.entrySet()) {
                String slang = entry.getKey();
                SlangDefinitionList def_list = entry.getValue();
                String definitions = String.join("| ", def_list);
                String line = slang + "`" + definitions;
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDictionary(){
        Set<String> slangword = this.dictionary.keySet();
        for(String slang : slangword){
            System.out.println(slang + ":");
            dictionary.get(slang).showDefinition();
        }
    }


    public void searchBySlang() {
        JFrame searchFrame = new JFrame("Search Slang Word");
        searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        searchFrame.setSize(400, 400);

        JPanel searchPanel = new JPanel();
        searchFrame.add(searchPanel);

        JTextField searchField = new JTextField(20);
        searchPanel.add(searchField);

        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton);

        JTextArea resultTextArea = new JTextArea(20, 30);
        searchPanel.add(resultTextArea);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultTextArea.setText("");
                String slangWord = searchField.getText();
                searchedSlang.add(slangWord);
                SlangDefinitionList result = new SlangDefinitionList();
                if(dictionary.containsKey(slangWord)){
                    result = dictionary.get(slangWord);
                }
                if (result.isEmpty()) {
                    resultTextArea.setText("SlangWord not found");
                } else {
                    for (String definition : result) {
                        resultTextArea.append("- " + definition + "\n");
                    }
                }
            }
        });

        searchFrame.setVisible(true);
    }

    public void searchByDefinition() {
        JFrame searchFrame = new JFrame("Search Definition");
        searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        searchFrame.setSize(400, 400);

        JPanel searchPanel = new JPanel();
        searchFrame.add(searchPanel);

        JTextField searchField = new JTextField(20);
        searchPanel.add(searchField);

        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton);

        JTextArea resultTextArea = new JTextArea(20, 30);
        searchPanel.add(resultTextArea);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultTextArea.setText("");
                String keyword = searchField.getText();
                List<String> result = new ArrayList<>();
                for (Map.Entry<String, SlangDefinitionList> entry : dictionary.entrySet()) {
                    String slang = entry.getKey();
                    SlangDefinitionList definitions = entry.getValue();

                    String lw_case_Keyword = keyword.toLowerCase();

                    for (String definition : definitions) {
                        String lw_case_definition = definition.toLowerCase();
                        if (lw_case_definition.contains(lw_case_Keyword)) {
                            result.add(slang);
                            break;
                        }
                    }
                }
                if (result.isEmpty()) {
                    resultTextArea.setText("Definition not found");
                } else {
                    for (String definition : result) {
                        resultTextArea.append("- " + definition + "\n");
                    }
                }
            }
        });

        searchFrame.setVisible(true);
    }

    public void showHistory(){
        JFrame historyFrame = new JFrame("Seach History");
        historyFrame.setLayout(new BorderLayout());
        historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        historyFrame.setSize(400, 400);

        JPanel historyPanel = new JPanel();
        historyFrame.add(historyPanel, BorderLayout.NORTH);

        JTextArea resultTextArea = new JTextArea(20, 30);
        historyPanel.add(resultTextArea);

        JPanel buttonPanel = new JPanel();
        historyFrame.add(buttonPanel, BorderLayout.SOUTH);

        JButton clear_btn = new JButton("Clear");
        buttonPanel.add(clear_btn);

        clear_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultTextArea.setText("");
            }
        });

        for (String searched : searchedSlang){
            resultTextArea.append(searched + "\n");
        }

        historyFrame.setVisible(true);
    }

    public void addSlang() {
        JFrame addFrame = new JFrame("Add Slang Word");
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addFrame.setSize(400, 200);

        JPanel panel = new JPanel();
        addFrame.add(panel);
        panel.setLayout(new GridLayout(4, 1));

        JLabel slangWordLabel = new JLabel("Slang Word:");
        JTextField slangWordField = new JTextField();
        panel.add(slangWordLabel);
        panel.add(slangWordField);

        JLabel definitionLabel = new JLabel("Definition:");
        JTextField definitionField = new JTextField();
        panel.add(definitionLabel);
        panel.add(definitionField);

        JButton hidden_btn  = new JButton();
        hidden_btn.setVisible(false);
        panel.add(hidden_btn);

        JButton addButton = new JButton("Add");
        panel.add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String slangWord = slangWordField.getText();
                String definition = definitionField.getText();
                if (dictionary.containsKey(slangWord)) {
                    Object[] options = { "Overwrite", "Duplicate" };
                    int choice = JOptionPane.showOptionDialog(addFrame,
                            "Slang Word already exists. Do you want to overwrite it or duplicate it?",
                            "Slang Word Exists",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                            null, options, options[0]);

                    if (choice == 0) { // Overwrite
                        SlangDefinitionList defList = new SlangDefinitionList();
                        Collections.addAll(defList, definition);
                        dictionary.put(slangWord, defList);
                        JOptionPane.showMessageDialog(addFrame, "Slang Word has been overwritten successfully.");
                    } else if (choice == 1) { // Duplicate
                        SlangDefinitionList defList = dictionary.get(slangWord);
                        Collections.addAll(defList, definition);
                        dictionary.put(slangWord, defList);
                        JOptionPane.showMessageDialog(addFrame, "Slang Word has been duplicated successfully.");
                    }
                } else {
                    SlangDefinitionList defList = new SlangDefinitionList();
                    Collections.addAll(defList, definition);
                    dictionary.put(slangWord, defList);
                    JOptionPane.showMessageDialog(addFrame, "Slang Word has been added successfully.");
                }
                writeToFile("data.txt");

                // Clear input fields
                slangWordField.setText("");
                definitionField.setText("");
            }
        });

        addFrame.setVisible(true);
    }

    public void editSlang(){
        JFrame editFrame = new JFrame("Edit Slang Word");
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editFrame.setSize(400, 200);

        JPanel panel = new JPanel();
        editFrame.add(panel);
        panel.setLayout(new GridLayout(4, 1));

        JLabel slangWordLabel = new JLabel("Slang Word:");
        JTextField slangWordField = new JTextField();
        panel.add(slangWordLabel);
        panel.add(slangWordField);

        JLabel definitionLabel = new JLabel("Definition:");
        JTextField definitionField = new JTextField();
        panel.add(definitionLabel);
        panel.add(definitionField);

        JButton hidden_btn  = new JButton();
        hidden_btn.setVisible(false);
        panel.add(hidden_btn);

        JButton editButton = new JButton("Edit");
        panel.add(editButton);

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String slangWord = slangWordField.getText();
                String definition = definitionField.getText();
                if (dictionary.containsKey(slangWord)) {
                    SlangDefinitionList defList = new SlangDefinitionList();
                    Collections.addAll(defList, definition);
                    dictionary.put(slangWord, defList);
                    JOptionPane.showMessageDialog(editFrame, "Slang Word has been edited successfully.");
                } else {
                    int option = JOptionPane.showConfirmDialog(null,
                            "Slang Word doesn't exist in the dictionary. Do you want to add it to dictionary ?",
                            "Not exist",
                            JOptionPane.YES_NO_OPTION);

                    if (option == JOptionPane.YES_OPTION) {
                        SlangDefinitionList defList = new SlangDefinitionList();
                        Collections.addAll(defList, definition);
                        dictionary.put(slangWord, defList);
                        JOptionPane.showMessageDialog(editFrame, "Slang Word added successfully.");
                    } else {
                    }
                }

                writeToFile("data.txt");
                // Clear input fields
                slangWordField.setText("");
                definitionField.setText("");
            }
        });
        editFrame.setVisible(true);
    }

    public void deleteSlang(){
        JFrame deleteFrame = new JFrame("Delete Slang Word");
        deleteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        deleteFrame.setSize(400, 200);

        JPanel panel = new JPanel();
        deleteFrame.add(panel);
        panel.setLayout(new GridLayout(4, 1));

        JLabel slangWordLabel = new JLabel("Slang Word:");
        JTextField slangWordField = new JTextField();
        panel.add(slangWordLabel);
        panel.add(slangWordField);


        JButton hidden_btn  = new JButton();
        hidden_btn.setVisible(false);
        panel.add(hidden_btn);

        JButton deleteButton = new JButton("Delete");
        panel.add(deleteButton);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String slangWord = slangWordField.getText();
                if(dictionary.containsKey(slangWord)){
                    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete '" + slangWord + "' ?", "Confirm delete", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        dictionary.remove(slangWord);
                        JOptionPane.showMessageDialog(null, "Slang Word '" + slangWord + "' has been deleted successfully !");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Slang Word '" + slangWord + "' doesn't exist in dictionary !");
                }
                writeToFile("data.txt");
            }
        });
        deleteFrame.setVisible(true);
    }
    public void resetDictionary(){
        dictionary = new HashMap<>();
        ReadFromFile("slang.txt");
        JOptionPane.showMessageDialog(null, "Dictionary reset successfully !");
    }

    public void randomSlang(){
        JFrame randomFrame = new JFrame("Random slang word");
        randomFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        randomFrame.setSize(400, 400);
        randomFrame.setLayout(new BorderLayout());

        JPanel randomPanel = new JPanel(new FlowLayout());
        randomFrame.add(randomPanel, BorderLayout.NORTH);

        JLabel titleLabel = new JLabel("On this day slang word");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        GridBagConstraints titleConstraints = new GridBagConstraints();
        titleConstraints.insets = new Insets(10, 10, 10, 10);
        titleConstraints.gridx = 0;
        titleConstraints.gridy = 0;
        randomPanel.add(titleLabel, titleConstraints);

        JPanel textPanel = new JPanel();
        randomFrame.add(textPanel, BorderLayout.CENTER);

        JLabel slangLabel = new JLabel();
        slangLabel.setText(getRandomSlang());
        slangLabel.setFont(new Font("Serif", Font.BOLD, 24));
        GridBagConstraints slangConstraints = new GridBagConstraints();
        slangConstraints.insets = new Insets(10, 10, 10, 10);
        slangConstraints.gridx = 0;
        slangConstraints.gridy = 1;
        textPanel.add(slangLabel, slangConstraints);


        JPanel btnPanel = new JPanel(new GridLayout());
        randomFrame.add(btnPanel, BorderLayout.SOUTH);
        GridBagConstraints btnContraints = new GridBagConstraints();
        btnContraints.weightx = 5;

        JButton ok_btn = new JButton("OK");
        btnContraints.gridx = 0;
        btnContraints.gridy = 3;
        btnPanel.add(ok_btn, btnContraints);

        JButton random_btn = new JButton("Random");
        titleConstraints.gridx = 1;
        titleConstraints.gridy = 3;
        btnPanel.add(random_btn, btnContraints);

        random_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                slangLabel.setText(getRandomSlang());
            }
        });

        ok_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                randomFrame.dispose();
            }
        });
        randomFrame.setVisible(true);
    }

    private String getRandomSlang() {
        Random rand = new Random();
        List<String> slangList = new ArrayList<>(dictionary.keySet());
        if (slangList.isEmpty()) {
            return "No slang words found\", \"Add some slang words to the dictionary first.";
        }
        int randomIndex = rand.nextInt(slangList.size());
        String randomSlang = slangList.get(randomIndex);
        SlangDefinitionList definitionList = dictionary.get(randomSlang);

        StringBuilder result = new StringBuilder(randomSlang + ":\n");
        for (String definition : definitionList) {
            result.append("- ").append(definition).append("\n");
        }

        return result.toString();
    }

    public void quizSlang(){
        JFrame slangFrame = new JFrame("Quiz Slang Word");
        slangFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        slangFrame.setSize(600,600);
        slangFrame.setLayout(new GridLayout(2,1));

        Random rand = new Random();
        List<String> slangList = new ArrayList<>(dictionary.keySet());
        int randomIndex = rand.nextInt(slangList.size());
        String randomSlang = slangList.get(randomIndex);

        JPanel questionPanel = new JPanel();
        slangFrame.add(questionPanel);
        JLabel slangLabel = new JLabel();
        slangLabel.setText(randomSlang);
        slangLabel.setFont(new Font("Serif", Font.BOLD, 24));
        questionPanel.add(slangLabel);

        JPanel answerPanel = new JPanel();
        slangFrame.add(answerPanel);
        answerPanel.setLayout(new GridLayout(2,2));
        JButton btn_a1 = new JButton();
        JButton btn_a2 = new JButton();
        JButton btn_a3 = new JButton();
        JButton btn_a4 = new JButton();
        answerPanel.add(btn_a1);
        answerPanel.add(btn_a2);
        answerPanel.add(btn_a3);
        answerPanel.add(btn_a4);

        List<String> answerList = new ArrayList<>();

        SlangDefinitionList correctDefinition = dictionary.get(randomSlang);
        // Lấy định nghĩa chính xác và thêm vào danh sách đáp án
        StringBuilder correctAnswer = new StringBuilder();
        for (String definition : correctDefinition) {
            correctAnswer.append("- ").append(definition).append("\n");
        }
        answerList.add(correctAnswer.toString());

        // Tạo danh sách chứa tất cả các định nghĩa
        List<SlangDefinitionList> allDefinitions = new ArrayList<>();
        for (SlangDefinitionList definitions : dictionary.values()) {
            allDefinitions.add(definitions);
        }
        // xóa định nghĩa đúng ra khỏi danh sách
        allDefinitions.remove(correctAnswer);

        // Random ra 3 định nghĩa ngẫu nhiên từ danh sách tất cả các định nghĩa
        List<SlangDefinitionList> shuffledDefinitions = new ArrayList<>();
        Collections.shuffle(allDefinitions);

        for (int i = 0; i < 3; i++) {
                shuffledDefinitions.add(allDefinitions.get(i));
            }

        StringBuilder incorrectAnswer = new StringBuilder();
        for(SlangDefinitionList item : shuffledDefinitions){
            for(String definition: item){
                incorrectAnswer.append("- ").append(definition).append("\n");
            }
            answerList.add(incorrectAnswer.toString());
            incorrectAnswer.setLength(0);
        }


        if (answerList.size() >= 4) {
            // Random vị trí của đáp án đúng trong danh sách đáp án
            Collections.shuffle(answerList);

            // Gán các đáp án vào các nút
            btn_a1.setText(answerList.get(0));
            btn_a2.setText(answerList.get(1));
            btn_a3.setText(answerList.get(2));
            btn_a4.setText(answerList.get(3));
        }

        btn_a1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer(btn_a1.getText(), correctAnswer.toString());
                slangFrame.dispose();
            }
        });

        btn_a2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer(btn_a2.getText(), correctAnswer.toString());
                slangFrame.dispose();
            }
        });

        btn_a3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer(btn_a3.getText(), correctAnswer.toString());
                slangFrame.dispose();
            }
        });

        btn_a4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer(btn_a4.getText(), correctAnswer.toString());
                slangFrame.dispose();
            }
        });

        slangFrame.setVisible(true);
    }

    // Hàm kiểm tra đáp án
    private void checkAnswer(String selectedAnswer, String correctAnswer) {
        String message;
        if (selectedAnswer.equals(correctAnswer)) {
            message = " ༼ つ ◕_◕ ༽つ Congratulations! ";
        } else {
            message = "o(╥﹏╥)o You wrong! ";
        }

        // Tạo cửa sổ pop-up thông báo kết quả
        JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.INFORMATION_MESSAGE);
    }

    public void quizDefinition() {
        JFrame definitionFrame = new JFrame("Quiz Definition");
        definitionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        definitionFrame.setSize(600, 600);
        definitionFrame.setLayout(new GridLayout(2, 1));

        Random rand = new Random();
        List<String> slangList = new ArrayList<>(dictionary.keySet());
        int randomIndex = rand.nextInt(slangList.size());
        String randomSlang = slangList.get(randomIndex);
        // randomSlang là đáp án đúng

        StringBuilder question = new StringBuilder();
        for(String definition: dictionary.get(randomSlang)){
            question.append("- ").append(definition).append("\n");
        }

        JPanel questionPanel = new JPanel();
        definitionFrame.add(questionPanel);
        JLabel definitionLabel = new JLabel();
        definitionLabel.setText(question.toString());
        definitionLabel.setFont(new Font("Serif", Font.BOLD, 24));
        questionPanel.add(definitionLabel);

        JPanel answerPanel = new JPanel();
        definitionFrame.add(answerPanel);
        answerPanel.setLayout(new GridLayout(2, 2));
        JButton btn_a1 = new JButton();
        JButton btn_a2 = new JButton();
        JButton btn_a3 = new JButton();
        JButton btn_a4 = new JButton();
        answerPanel.add(btn_a1);
        answerPanel.add(btn_a2);
        answerPanel.add(btn_a3);
        answerPanel.add(btn_a4);

        List<String> answerList = new ArrayList<>();
        answerList.add(randomSlang);

        // Danh sách chưa tất cả SlangWord
        List<String> allSlangWord = new ArrayList<>();
        for(String slang : dictionary.keySet()){
            allSlangWord.add(slang);
        }
        // loại bỏ đáp án đúng ra khỏi danh sách
        allSlangWord.remove(randomSlang);

        // Random ra 3 slang word ngẫu nhiên khác
        List<String> shuffledSlangWords = new ArrayList<>();
        Collections.shuffle(allSlangWord);

        for (int i = 0; i < 3; i++) {
            answerList.add(allSlangWord.get(i));
        }

        // Random vị trí của đáp án đúng trong danh sách đáp án
        Collections.shuffle(answerList);

        // Gán các đáp án vào các nút
        if (answerList.size() >= 4){
            btn_a1.setText(answerList.get(0));
            btn_a2.setText(answerList.get(1));
            btn_a3.setText(answerList.get(2));
            btn_a4.setText(answerList.get(3));
        }

        btn_a1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer(btn_a1.getText(), randomSlang);
                definitionFrame.dispose();
            }
        });

        btn_a2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer(btn_a2.getText(), randomSlang);
                definitionFrame.dispose();
            }
        });

        btn_a3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer(btn_a3.getText(), randomSlang);
                definitionFrame.dispose();
            }
        });

        btn_a4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer(btn_a4.getText(), randomSlang);
                definitionFrame.dispose();
            }
        });

        definitionFrame.setVisible(true);
    }

}
