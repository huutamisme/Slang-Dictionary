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
                        JOptionPane.showMessageDialog(addFrame, "Slang Word overwritten successfully.");
                    } else if (choice == 1) { // Duplicate
                        SlangDefinitionList defList = dictionary.get(slangWord);
                        Collections.addAll(defList, definition);
                        dictionary.put(slangWord, defList);
                        JOptionPane.showMessageDialog(addFrame, "Slang Word duplicated successfully.");
                    }
                } else {
                    SlangDefinitionList defList = new SlangDefinitionList();
                    Collections.addAll(defList, definition);
                    dictionary.put(slangWord, defList);
                    JOptionPane.showMessageDialog(addFrame, "Slang Word added successfully.");
                }

                // Clear input fields
                slangWordField.setText("");
                definitionField.setText("");
            }
        });

        addFrame.setVisible(true);
    }

    public void editSlang(){
        JFrame editFrame = new JFrame("Add Slang Word");
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
                    JOptionPane.showMessageDialog(editFrame, "Slang Word edited successfully.");
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
                // Clear input fields
                slangWordField.setText("");
                definitionField.setText("");
            }
        });
        editFrame.setVisible(true);
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
}
