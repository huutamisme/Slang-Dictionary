import java.io.*;
import java.util.*;

public class SlangDictionary {
    private HashMap<String, SlangDefinitionList> dictionary;

    public SlangDictionary(){
        this.dictionary = new HashMap<>();
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
}
