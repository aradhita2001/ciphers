import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class TestVigenereCipher {
    
    public static void main(String[] args) throws IOException {
        
        if(args.length == 0){
            System.out.println("Please retry with atleast one command line argument for the file to read");
        }
        String path ="texts\\"  + args[0] + ".txt";
        String enPath, dePath;

        if(args.length >= 3){
            enPath = "texts\\"  + args[1] + ".txt";
            dePath = "texts\\"  + args[2] + ".txt";
        }
        else{
            enPath = "texts\\Vencrypted-"  + args[0] + ".txt";
            dePath = "texts\\Vdecrypted-"  + args[0] + ".txt";
        }


        int key[] = {6,12,21,20,5};
        
        VigenereCipher vc = new VigenereCipher(key);
        VigenereBreaker vb = new VigenereBreaker();
        
        File inFile = new File(path);
        Scanner input = new Scanner(inFile);

        FileWriter myWriter = new FileWriter(enPath);
        String text = "";
        
        while(input.hasNextLine()){
            
            text = text + input.nextLine() + "\n";
             
        }
        myWriter.write(vc.encrypt(text));
        myWriter.close();
        input.close();
        


        inFile = new File(enPath);
        input = new Scanner(inFile);
        String encrypted = "";

        myWriter = new FileWriter(dePath);
        
        while(input.hasNextLine()){
            encrypted = encrypted + input.nextLine() + "\n";
            
        }
        myWriter.write(vb.breakForLanguage(encrypted, "English"));
        myWriter.close();

        input.close();
        myWriter.close();
        
    }
}
