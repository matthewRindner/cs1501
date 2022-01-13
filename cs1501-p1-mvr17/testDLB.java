import java.io.*;
import java.util.*;
public class testDLB
{
    public static void main(String[] args) throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader("dictionary.txt"));
        DLBnode dictionaryTrie = new DLBnode();

        int total_dictionary_words = 0, words_in_dlb = 0;

        while(br.ready())
        {
            String word = br.readLine();
            total_dictionary_words++;
            if(dictionaryTrie.insert(word))
            {
                words_in_dlb++;
                //System.out.println(word);
            }
            else
            {
                //System.out.println("I failed at: " + word);
            }
        }
        System.out.println("Passed inserts: " + words_in_dlb + "\nFailed: " + (total_dictionary_words - words_in_dlb) + "\nTotal: " + total_dictionary_words);
        //Should be:
        //Passed: 126328
        //Failed: 0
        //Total: 126328

        System.out.println(dictionaryTrie.contains("zounds"));

        ArrayList<String> predictOutput =  dictionaryTrie.getPrediction("C");
        for (int i =0; i < 5 && i < predictOutput.size(); i++){
            System.out.println(predictOutput.get(i));
        }
    }
}