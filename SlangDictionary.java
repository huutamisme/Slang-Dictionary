import java.util.HashMap;

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

}
