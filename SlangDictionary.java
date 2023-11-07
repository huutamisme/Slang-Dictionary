import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

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
                    String[] definitionArray = definitions.split("\\| ");
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
                SlangDefinitionList result = new SlangDefinitionList();
                if(dictionary.containsKey(slangWord)){
                    result = dictionary.get(slangWord);
                    searchedSlang.add(slangWord);
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
        historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        historyFrame.setSize(400, 400);

        JPanel historyPanel = new JPanel();
        historyFrame.add(historyPanel);

        JTextArea resultTextArea = new JTextArea(20, 30);
        historyPanel.add(resultTextArea);

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

}
