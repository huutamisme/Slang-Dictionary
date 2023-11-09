import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.util.List;

public class Main {

    public static boolean RIGHT_TO_LEFT = false;
    public static void addComponents(Container pane, SlangDictionary dictionary){
        if(RIGHT_TO_LEFT){
            pane.setComponentOrientation(
                    ComponentOrientation.RIGHT_TO_LEFT);
        }

        pane.setLayout(new GridLayout(4,3));


        JButton searchBySlang_btn = new JButton("Search Slang Word");
        pane.add(searchBySlang_btn);
        searchBySlang_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dictionary.searchBySlang();
            }
        });

        JButton searchByDefinition_btn = new JButton("Search Definition");
        pane.add(searchByDefinition_btn);
        searchByDefinition_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dictionary.searchByDefinition();
            }
        });

        JButton showHistory_btn = new JButton("History");

        pane.add(showHistory_btn);
        showHistory_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dictionary.showHistory();
            }
        });

        JButton addSlangWord_btn = new JButton("Add Slang Word");
        pane.add(addSlangWord_btn);
        addSlangWord_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dictionary.addSlang();
            }
        });

        JButton editSlangWord_btn = new JButton("Edit Slang Word");
        pane.add(editSlangWord_btn);
        editSlangWord_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dictionary.editSlang();
            }
        });

        JButton deleteSlangWord_btn = new JButton("Delete Slang Word");
        pane.add(deleteSlangWord_btn);
        deleteSlangWord_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dictionary.deleteSlang();
            }
        });


        JButton randomSlangWord_btn = new JButton("Random Slang Word");
        pane.add(randomSlangWord_btn);
        randomSlangWord_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dictionary.randomSlang();
            }
        });

        JButton quiz_slang = new JButton("Quiz Slang Word");
        pane.add(quiz_slang);
        quiz_slang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dictionary.quizSlang();
            }
        });

        JButton quiz_definition = new JButton("Quiz Definition");
        pane.add(quiz_definition);
        quiz_definition.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dictionary.quizDefinition();
            }
        });

        JButton resetDictionary_btn = new JButton("Reset Dictionary");
        pane.add(resetDictionary_btn);
        resetDictionary_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dictionary.resetDictionary();
            }
        });

        JButton aboutme_btn = new JButton("About me");
        pane.add(aboutme_btn);
        aboutme_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "HỌ TÊN SINH VIÊN : HỒ HỮU TÂM \n MSSV: 21127421");
            }
        });

        JButton exit_btn = new JButton("Exit");
        pane.add(exit_btn);
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
        frame.setSize(500, 500);
        addComponents(frame.getContentPane(), dictionary);


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
