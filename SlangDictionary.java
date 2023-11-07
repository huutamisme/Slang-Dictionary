import java.io.*;
import java.util.*;

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

    public SlangDefinitionList searchBySlang(String SlangWord){
        if (dictionary.containsKey(SlangWord)){
            SlangDefinitionList definitions = dictionary.get(SlangWord);
            searchedSlang.add(SlangWord);
            return definitions;
        }
        else {
            return null;
        }
    }

    public SlangDefinitionList searchByDefinition(String keyword){
        SlangDefinitionList SlangList = new SlangDefinitionList();

        for(Map.Entry<String, SlangDefinitionList> entry : dictionary.entrySet()){
            String slang = entry.getKey();
            SlangDefinitionList definitions = entry.getValue();

            String lw_case_Keyword = keyword.toLowerCase();

            for(String definition : definitions){
                String lw_case_definition = definition.toLowerCase();
                if(lw_case_definition.contains(lw_case_Keyword)) {
                    SlangList.add(slang);
                    break;
                }
            }
        }
        return SlangList;
    }

    public void showHistory(){
        System.out.println("Searched list: ");
        for (String searched : searchedSlang){
            System.out.println("- " + searched);
        }
    }
}
