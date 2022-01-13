//PITT CS 1501
// Project 1 make a DLB 
//Implements the DLMnode interface

import java.util.*;
import java.lang.*;

public class DLBnode { 	
	
	private static final int asciiChars = 256;	// constant R
	private Node root;							//head root of the trie
	private int numOfKeys;						//number of complete keys in trie

	private class Node{
		char data;
		Node sibling;
		Node child;
		Node(char data, Node nextRight, Node nextDown){
			this.data = data;
			this.sibling = nextRight;
			this.child = nextDown;
		}
	}
	public DLBnode(){
		root = null;
	}
	public boolean insert(String key){
		/**
		 * check if string is in tree - 
		 * add nodes if needed
		 * if prefix is already in DLB just add chars where needed + $
		 */

        
        key += '$';    
        
        Node curr = root;
        //System.out.println("We want to insert the following String into the DLB: " + data);
        
        if(root == null)
        {
            root = new Node(key.charAt(0), null, null);   
            curr = root;

            for(int i = 1; i < key.length(); i++) 
            {   
                if(curr.child == null) //keep adding char in key as children
                {
                    curr.child = new Node(key.charAt(i), null, null);
                    curr = curr.child;
                }
            }
        }
        else
        {
            for(int i = 0; i < key.length(); i++)
            {
                if(curr.data != '$' && curr.child == null)				//adds new child node till end of string and doesnt hit- $
                {
                    //System.out.println("Creating a new child...");
                    curr.child = new Node(key.charAt(i), null, null);
                    curr = curr.child;
                }

                else if(curr.child != null && curr.data == key.charAt(i))	//curr node == data --> keep moving down
                {
                    //System.out.println("Traversing child...");
                    curr = curr.child;
                }
                
                else if(curr.sibling != null && curr.data != key.charAt(i))	//if not the same char, keep moving right and decrement i
                {
                    curr = curr.sibling;
                    i--;    //Readjust i
                }
                else if(curr.data != key.charAt(i) && key.charAt(i) != '$' && curr.sibling == null)	//if siblings dont have required char, add new sibling of required char
                {
                    //System.out.println("Creating sibling...");
                    curr.sibling = new Node(key.charAt(i), null, null);
                    curr = curr.sibling;
                }  
            }
        }
        //If we reach the Terminator Char, we're done. (The insertion was successful)
        //if(curr.data == '$')
        //{
        //    successful_insertion = true;
        //}
        return true;
		
	}
		
	
	public boolean contains(String key) throws IllegalArgumentException{
		/**
		 * go right till value is found or null(reching the end of LL without finding value)
		 * then go down till value is found or null(key not found in DLB return false)
		 * if key is found, must have $ at end, return true
		 */

		
        //StringBuilder sb = new StringBuilder();
        String temp = "";
        //boolean iskey = false;

        Node curr = root;   

        for(int i = 0; i < key.length(); i++)
        {
            if(curr.data == key.charAt(i) && curr.child != null)	//if curr == matching char and can still go down
            {
                //Build up the string...
               // sb.append(curr.data);
            	temp += curr.data;
                curr = curr.child;
            }
            else if(curr.data != key.charAt(i) && curr.sibling != null) //if not curr doesnt have regiured data, keep going right
            {
                curr = curr.sibling;
                i--;    //Readjust i
            }
            else if(curr.data == '$' && curr.sibling != null)	//if curr is $, keep going right till found
            {
                curr = curr.sibling;
                i--;    //Readjust i
            }
        }

        if(temp.equals(key))
            return true;
        
        return false;
	}



	public int size(){
		return numOfKeys;
	}

	public boolean isEmpty(){
		return size() == 0;
	}

	public ArrayList<String> getPrediction(String key){
		/**
		* if its the first time user is seaching, User DLB is empty, 
		* return 1st 5 results from dictionary DLB
		* and retun time it takes to serch for the prediections
		*
		* if its not the first time user is seaching, User DLB is not empty, 
		* return 1st 5 predictions from User DLB
		* if User DLB cannot return all 5 predictions, 
		* fill the remaining predictions with predictions from the Dictonary DLB
		**/

		ArrayList<String> output = new ArrayList<String>();
		String child_string = key;

		Node curr = root;
		Node tempBeginningNode = null;

		//Navigate current to where key starts
		if(contains(key)){

			for(int i = 0; i < key.length(); i++)
      	 	{
	            if(curr.child != null && curr.data == key.charAt(i))	//nagigate down DLB if curr == data and can go down
	            {
	                //Build up the string...
	                //sb.append(curr.data);
	                curr = curr.child;
	                tempBeginningNode = curr;
	            }
	            else if(curr.data != key.charAt(i) && curr.sibling != null)	//navigate right if curr!=data and can go right
	            {
	            	//System.out.println("Traversing to sibling: " + curr.sibling.data);
	            	//System.out.println("here");

	                curr = curr.sibling;
	                i--;    //Readjust i
	            }
	            
   	    	}

		}
		//System.out.println("traversed node's data: " + curr.data);

		output = getPrediction(curr, output, child_string);
		//System.out.println(output);
		return output;

	}
	private ArrayList<String> getPrediction(Node curr, ArrayList<String> output, String child_string){
		//System.out.println("getPrediction print statment");

		String sibling_string = "";
		//base case if we're at the terminating char and node has no children, append and return that word
		if (curr.data == '$' && curr.sibling == null) {
			output.add(child_string);
			//System.out.println(child_string);
			//System.out.println(output);
			//child_string = "";
			return output;
			
		}
		//base case if current node is terminating char but has a sibling , append word but keep going
		else if (curr.data == '$' && curr.sibling != null){
			sibling_string = child_string;
			output.add(sibling_string);
			getPrediction(curr.sibling, output, child_string);

		}
		//recursive case for no siblings, build up the tempString with current char and recurse down
		else if (curr.child != null && curr.sibling == null){
			child_string += curr.data;
			getPrediction(curr.child, output, child_string);
		}
		//recursive case is current node has both a child and sibling, add current char to tempString and 
		//recurse both down and right; junction point
		else if (curr.sibling != null && curr.child != null){
			sibling_string += child_string;
			child_string += curr.data;

			getPrediction(curr.child, output, child_string);
			getPrediction(curr.sibling, output, sibling_string);
		}
		return output;
	}

}
