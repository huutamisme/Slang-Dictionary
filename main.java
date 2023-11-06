import java.util.*;

public class Main {
    public static void main (String[] args) {
        SlangDictionary dictionary = new SlangDictionary();
        dictionary.ReadFromFile("slang.txt");
        dictionary.showDictionary();
    }
}
