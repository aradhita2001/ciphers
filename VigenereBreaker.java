
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class VigenereBreaker {
    private int[] finalKey;
    private static HashMap<String, HashSet<String>> dictionary;
    static final String[] languages = {"Danish", "Dutch", "English", "French", "German", "Italian", "Portuguese", "Spanish"};

    static{

        dictionary = new HashMap<String, HashSet<String>>();
        
        for (String language : languages){
            
            String fileName = "dictionaries\\" + language;
            File dictionaryFile = new File(fileName);
            
        
            HashSet<String> currentDictionary = readDictionary(dictionaryFile);
            dictionary.put(language, currentDictionary);
        }
    }

    public static HashSet<String> readDictionary(File file) {
     
        try (Scanner input = new Scanner(file)) {
            HashSet<String> words = new HashSet<String> ();

            while(input.hasNextLine()){
                words.add(input.nextLine().strip());
            }

            input.close();
            
            return words;
        } catch (FileNotFoundException e) {
            
            e.printStackTrace();
        }
        return null;
    }

    private String sliceString(String message, int whichSlice, int totalSlices) {
        //REPLACE WITH YOUR CODE
        StringBuilder answer = new StringBuilder();
        for (int i = whichSlice; i < message.length(); i += totalSlices) {
         
            answer.append(message.charAt(i));
        }
        return answer.toString();
    }

    private int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        //WRITE YOUR CODE HERE
        
        CaesarCracker cck = new CaesarCracker(mostCommon);
        
        for(int i = 0; i < klength; i++){
         
            String encryptedSlice = sliceString(encrypted, i, klength);
            key[i] = cck.getKey(encryptedSlice);
        }
        
        return key;
    }
    
    private int countWords (String message, HashSet<String> dictionary) {
    
        int count = 0;
    
        for (String word : message.split("\\W+")) {

            if (dictionary.contains(word.toLowerCase())){
             
                count = count + 1;
            }
            
        }
        
        return count;
    }
    
    private char mostCommonCharIn(HashSet<String> dictionary){
     
        HashMap<Character, Integer> map = new HashMap<Character, Integer> ();
        
        for(String word : dictionary) {
            
            for(int i = 0; i < word.length(); i++){
             
                char c = word.charAt(i);
                
                if(map.containsKey(c)){
                 
                    map.put(c, map.get(c) + 1);
                }
                else{
                    
                    map.put(c,1);
                }
            }
            
        }
        
        char answer = 'a';
        int max = 0;
        
        for(char c : map.keySet()){
            
            if(map.get(c) > max){
                
                max = map.get(c);
                answer = c;
            }
            
        }
        
        return answer;
    }

    public String breakForLanguage (String encrypted, String language) {
        
        String decrypted = "";
        int words = 0;
        HashSet<String> currDict = dictionary.get(language);
        
        for(int i = 1; i < 100; i++){
            
            int[] key = tryKeyLength(encrypted, i, mostCommonCharIn(currDict));
            VigenereCipher vc = new VigenereCipher(key);
            
            String currentDecryption = vc.decrypt(encrypted);
            
            if (countWords(currentDecryption, currDict) > words){
             
                decrypted = currentDecryption;
                words = countWords(currentDecryption, currDict);
                finalKey = key;
                
            }
            
        }
        
        return decrypted;
    }
    
    public String breakForAllLangs(String encrypted){
     
        String decrypted = "";
        int maxWords = 0;
        String lang = "";
        int[] key = {};
        
        for (String language : languages){

            String current = breakForLanguage(encrypted, language);
            int words = countWords(current, dictionary.get(language));
            
            if(words > maxWords) {
                
                decrypted = current;
                maxWords = words;
                lang = language;
                key = finalKey;
            }
            
        }
        
        System.out.println(decrypted);
        System.out.println(lang);
        for(int i = 0; i < key.length; i++){
            System.out.println(key[i]);
        }

        return decrypted;
    }
}
