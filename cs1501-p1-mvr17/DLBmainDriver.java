import java.util.*;
import java.io.*;
import java.lang.*;

public class DLBmainDriver
{
    public static void main(String[] args) throws Exception
    {
        String input = "";
        double totalTime = 0; 
        double numIterations = 0;
        ArrayList<String> userHistoryArr = new ArrayList<String>();
        File userHistoryTxt = new File("user_history.txt");
        FileWriter writer = new FileWriter(userHistoryTxt);
        ArrayList<String> predictOutput = new ArrayList<String>();
        boolean flag = false; 

        //creates and fills the Dictionary DLB
        BufferedReader br = new BufferedReader(new FileReader("dictionary.txt"));
        DLBnode dictionaryTrie = new DLBnode();
        DLBnode userHistoryTrie = new DLBnode();

        while(br.ready()){
            String word = br.readLine();
            dictionaryTrie.insert(word);
        }


        //work to search and test for sentinel value '!' to end program.
        //after user enters '!' program outputs the average time required to
        //produce a list of predictions.
        boolean continue_loop = true;
        int inputAsInt = 0;

        Scanner user_input = new Scanner(System.in);
        //if(user_input.next().charAt(0) == SENTINEL_CHAR)
         //   sentinel_value = false;

        while (continue_loop){
            //user_input.next().charAt(0) != SENTINEL_CHAR
            System.out.println("Enter A Character: ");

            String line = user_input.nextLine();
            

            //System.out.println(input);
            if(line.length() != 1)
            {
                System.out.println("please enter one fucking charcter at a time");
            }
            else if(line.equals("!"))
            {

                System.out.println("bye");
                continue_loop = false;
                System.out.printf("Average Time: %.6f\n",  totalTime/numIterations);
                int i =0;
                for(String word : userHistoryArr){
                    writer.write(predictOutput.get(i) + "\n");
                i++;
                }
                writer.close();
                 //writer.write(predictOutput.get(4) + "\n");


                System.exit(0);
            }
            else if(line.equals("1") || line.equals("2") || line.equals("3") || line.equals("4") || line.equals("5"))
            {
                inputAsInt = Integer.parseInt(line);
                flag = true; 

                
            }   
            else
            {    

                input += line;
                //code to get the predictions from Dictionary DLB into an ArrayList and
                //print the first 5 predictions and time it takes to get the predictions
                double startTime = System.nanoTime();
                System.out.println(input);
                if(dictionaryTrie.contains(input))
                {
                    predictOutput = dictionaryTrie.getPrediction(input);
                    // ArrayList<String> predictOutput;
                    // if(numIterations == 0)
                    //     predictOutput =  dictionaryTrie.getPrediction(input);
                    // else
                    //     predictOutput = userHistoryTrie.getPrediction(input);


                    //timing operations
                    double endTime = System.nanoTime();
                    double opTime = ((endTime - startTime)/Math.pow(10, 9));
                    totalTime += opTime;
                    numIterations++;


                    //get the 5 predictions
                    for (int i = 0; i < 5 && i < predictOutput.size(); i++){
                            System.out.print("(" + (i+1) + ") " + predictOutput.get(i) + "    ");
                    }
                    System.out.println();
                    System.out.printf("(%.6fs)\n",  opTime);
                    //System.out.println(opTime);
                }
                if(!dictionaryTrie.contains(input))
                {
                    System.out.println("prefix does not exist in dictionary trie");
                    //input = "";
                }
            }

            if(flag)
            {

                //code for the user to select one of the predictied words 
                 
                switch(inputAsInt){
                    case 1:
                        System.out.println("WORD COMPLETED:  " + predictOutput.get(0));
                        userHistoryTrie.insert(predictOutput.get(0));
                        userHistoryArr.add(predictOutput.get(0));
                        input = "";
                        flag = false;
                        //predictOutput.clear();
                        break;
                    case 2:
                        System.out.println("WORD COMPLETED:  " + predictOutput.get(1));
                        userHistoryTrie.insert(predictOutput.get(1));
                        userHistoryArr.add(predictOutput.get(1));
                        input = "";
                        flag = false;
                        //predictOutput.clear();
                        break;
                    case 3:
                        System.out.println("WORD COMPLETED:  " + predictOutput.get(2));
                        userHistoryTrie.insert(predictOutput.get(2));
                        userHistoryArr.add(predictOutput.get(2));
                        input = "";
                        flag = false;
                        //predictOutput.clear();
                        break;
                    case 4:
                        System.out.println("WORD COMPLETED:  " + predictOutput.get(3));
                        userHistoryTrie.insert(predictOutput.get(3));
                        userHistoryArr.add(predictOutput.get(3));
                        input = "";
                        flag = false;
                        //predictOutput.clear();
                        break;
                    case 5:
                        System.out.println("WORD COMPLETED:  " + predictOutput.get(4));
                        userHistoryTrie.insert(predictOutput.get(4));
                         userHistoryArr.add(predictOutput.get(4));
                        input = "";
                        flag = false;
                        //predictOutput.clear();
                        break;
                   }
            }
        }
    }
}