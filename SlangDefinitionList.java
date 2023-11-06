import java.util.*;
public class SlangDefinitionList extends ArrayList<String> {
    private List<String> definitions;

    public SlangDefinitionList() {
        this.definitions = new ArrayList<>();
    }

    public SlangDefinitionList(List<String> a){
        this.definitions = a;
    }

    public SlangDefinitionList(SlangDefinitionList a){
        this.definitions = a.definitions;
    }

    public void addDefinition(String definition) {
        this.definitions.add(definition);
    }

    public List<String> getDefinition(){
        return this.definitions;
    }

    public void removeDefinition(String definition){
        this.definitions.remove(definition);
    }

    public void updateDefinition(String prev_def, String cur_def){
        int index = this.definitions.indexOf(prev_def);
        if (index != -1){
            definitions.set(index, cur_def);
        }
    }

}
