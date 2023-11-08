import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class Main {

    public static boolean RIGHT_TO_LEFT = false;
    public static void addComponents(Container pane, SlangDictionary dictionary){
        if(RIGHT_TO_LEFT){
            pane.setComponentOrientation(
                    ComponentOrientation.RIGHT_TO_LEFT);
        }

        pane.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5); // Khoảng cách giữa các nút
        constraints.weightx = 1.0;

        JButton searchBySlang_btn = new JButton("Search Slang Word");
        constraints.gridx = 0;
        constraints.gridy = 0;
        pane.add(searchBySlang_btn,constraints);
        searchBySlang_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dictionary.searchBySlang();
            }
        });

        JButton searchByDefinition_btn = new JButton("Search Definition");
        constraints.gridx = 1;
        constraints.gridy = 0;
        pane.add(searchByDefinition_btn,constraints);
        searchByDefinition_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dictionary.searchByDefinition();
            }
        });

        JButton showHistory_btn = new JButton("History");
        constraints.gridx = 2;
        constraints.gridy = 0;
        pane.add(showHistory_btn, constraints);
        showHistory_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dictionary.showHistory();
            }
        });

        JButton addSlangWord_btn = new JButton("Add Slang Word");
        constraints.gridx = 0;
        constraints.gridy = 1;
        pane.add(addSlangWord_btn, constraints);
        addSlangWord_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dictionary.addSlang();
            }
        });

        JButton editSlangWord_btn = new JButton("Edit Slang Word");
        constraints.gridx = 1;
        constraints.gridy = 1;
        pane.add(editSlangWord_btn, constraints);
        editSlangWord_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dictionary.editSlang();
            }
        });

        JButton deleteSlangWord_btn = new JButton("Delete Slang Word");
        constraints.gridx = 2;
        constraints.gridy = 1;
        pane.add(deleteSlangWord_btn, constraints);

        JButton resetDictionary_btn = new JButton("Reset Dictionary");
        constraints.gridx = 3;
        constraints.gridy = 1;
        pane.add(resetDictionary_btn, constraints);
        resetDictionary_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dictionary.resetDictionary();
            }
        });

        JButton randomSlangWord_btn = new JButton("Random Slang Word");
        constraints.gridx = 0;
        constraints.gridy = 2;
        pane.add(randomSlangWord_btn, constraints);
        randomSlangWord_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dictionary.randomSlang();
            }
        });

        JButton quizSlangWord_btn = new JButton("Quiz Slang Word");
        constraints.gridx = 1;
        constraints.gridy = 2;
        pane.add(quizSlangWord_btn, constraints);

        JButton quizDefinition_btn = new JButton("Quiz Definition");
        constraints.gridx = 2;
        constraints.gridy = 2;
        pane.add(quizDefinition_btn, constraints);

        JButton aboutme_btn = new JButton("About me");
        constraints.gridx = 0;
        constraints.gridy = 3;
        pane.add(aboutme_btn, constraints);

        JButton exit_btn = new JButton("Exit");
        constraints.gridx = 1;
        constraints.gridy = 3;
        pane.add(exit_btn, constraints);
        exit_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }


    private static void createAndShowGUI(SlangDictionary dictionary){
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame("Slang Word Dictionary");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addComponents(frame.getContentPane(), dictionary);

        frame.pack();
        frame.setVisible(true);

    }
    public static void main (String[] args) {
        SlangDictionary dictionary = new SlangDictionary();
        dictionary.ReadFromFile("slang.txt");

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI(dictionary);
            }
        });

    }
}
