import java.awt.List;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BWBar {
	
	public static void main(String args[]){
		
		//take input file and commands through command line
		//input file contains single line of an odd number of letters 
		//(6 B's, 6 W's and one x) ex: BWXBBWWBWWWBB
		//compile with:  javac BWBar.java
		//command line format: java BWBar [-cost] <BFS|DFS|UCS|GS|A-star> <inputfile>
		
		//step 1 is to determine what operations to commit
				//parse the command line for type of search and cost! 
			String costInput = "-cost";
			boolean costFlag = false;
			
			if(costInput.equals(args[0])) {
				System.out.println(costInput);
				costFlag = true;//set the cost flag to true, use this later to start calculation for cost
			}
		
		//get the file input
		String fileName = "";
		if(costFlag == true) {//if the cost arg is in the command line
			fileName = args[2];
		}
		else {//other wise the fileName will be located in the arg1
			fileName = args[1];
		}
		
		System.out.println(fileName);//confirm the fileName is correct
		
		BufferedReader bufferedReader = null;//initialize the buffered reader
		try {
			bufferedReader = new BufferedReader(new FileReader(fileName));//set reader to filename
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		String input = null;
		try {
			//the first step is to get the input
			input = bufferedReader.readLine();//read the input
			System.out.println("The input line is: " + input);//confirm output
		} catch (IOException e) {
			e.printStackTrace();
		}
		String finalResult = generateFinal(input);
		System.out.println("This is end goal: " + finalResult);
		
		String searchOption ="";
		if(costFlag == true) {//if the cost arg is in the command line
			searchOption = args[1];//then the type of search will be the arg1
		}
		else {//other wise the search option will be located in the arg0
			searchOption = args[0];
		}
		
		
		/*do i need to fully generate the tree of possible results? 
		 * how does the search functionality really work in this context?
		 * !!Must use next state queue. How to use?
		 * Utilize string queue to add states on to
		*/		
		switch(searchOption) {
			case "BFS" : //Breadth First Search, navigate the tree horizontally, finished!
				System.out.println("BFS");
				BFS(input, finalResult, costFlag);
				//need to generate states, output them, and then add to queue
				//first: generate states
				break;
			case "DFS" : 
				System.out.println("DFS");//Depth First Search, navigate the tree vertically, finished!
				DFS(input, finalResult, costFlag);
				break;
			case "UCS" : 
				System.out.println("UCS");
				UCS(input, finalResult, costFlag);
				break;
			case "GS" : //greedy search! make solutions based on how many tiles are out of place.
				System.out.println("GS");
				GS(input, finalResult, costFlag);
				break;
			case "A-star" : 
				System.out.println("A-star");
				AStar(input, finalResult, costFlag);
				break;
			default: 
				System.out.println("Not Correct Format");
				break;
		}
	}
	
	public static String generateFinal(String input) {
		int bChars = (input.length()-1)/2;//the number of characters of B and W
		//create the final string
		String finalString ="";//initialize result
		char simpleString[] = new char[input.length()];//initialize the intermediary array
		//generate characters to add to string
		for(int i = 0; i < bChars; i++) {
			simpleString[i] = 'B';
		}
		simpleString[bChars] = 'X';
		
		for(int i = bChars + 1; i < input.length(); i++) {
			simpleString[i] = 'W';
		}
		
		finalString = new String(simpleString);
		
		return finalString;
	}
	
	public static void BFS(String input, String finalResult, boolean costFlag) {//string is passed
		
		//step 1: start state is already in queue, remove from queue add to visited list
		//step 2: goal test
		//step 3: create states based on start
		//step 4: add new states to queue and continue, stop when the correct state has been found
		Queue<State> states = new LinkedList<>();

		ArrayList<String> visited = new ArrayList<String>();
		
		states.add(new State(input, null, null, null, -1));//add the start state to the queue and a null value for cost, parent path and how the state was generated
		
		while(states.isEmpty() == false){//while the queue is not empty
			
			State currState = states.poll();
			
			//System.out.println(currState.node);
			
			//check if the currentState is in the visited list or not
			if(visited.contains(currState.node) == true) {//not sure if the contains method should be used! does not always work properly
				continue;//move to the next state and skip the code below
			}
			
			//add it to the list of visited strings
			visited.add(currState.node);
			
			//add the previous string to the path it has taken to get here
			if(currState.path.contains(currState.node) == false)
				currState.path.add(currState.node);
			
			if(currState.getString().equals(finalResult)) {//goal test!!!!
				//need to return the path to this state, stored in each state list
				displayFinal(currState, costFlag);
				break;//exit the loop
			}
			//if not final, analyze and create other possible states
			
			char tempC[] = currState.getString().toCharArray();//get the character array from the string
			
			int xLoc = getXLoc(tempC);//location of the x character
			
			generateStatesBFS(tempC, finalResult, xLoc, states, currState.path, currState.stateGenCost, currState.whereX, costFlag);//generation of all the next states
		}
	}

	public static void DFS(String input, String finalResult, boolean costFlag) {//string is passed
		//DFS
		//step 1: create stack
		//step 2: implement same algorithm from BFS with stack
		//changes include popping and pushing states
		//test1: take exact code form BFS, change queue to stack
		
		Stack<State> states = new Stack<State>();

		LinkedList<String> visited = new LinkedList<String>();
		
		states.push(new State(input, null, null, null, -1));//add the start state to the stack and a null value in the arrayList

		while(states.isEmpty() == false){//while the stack is not empty
			//step one pop the latest state in the stack
			State currState = states.pop();
			
			//System.out.println(currState.node);
			//check if the currentState is in the visited list or not
			
			//System.out.println("Checking Visited");
			if(visited.contains(currState.node)) {//contains method started working properly after the datatype for the list was changed to String and not state
				continue;//move to the next state and skip the code below
			}
			
			//add it to the list of visited strings
			visited.add(currState.node);
			
			//add the previous string to the path it has taken to get here
			if(currState.path.contains(currState.node) == false)
				currState.path.add(currState.node);
			
			if(currState.getString().equals(finalResult)) {//goal test!!!!
				//need to return the path to this state, stored in each state list
				displayFinal(currState, costFlag);
				break;//exit the loop
			}
			//if not final, then analyze and create other possible states
			
			char tempC[] = currState.getString().toCharArray();//get the character array from the string
			
			int xLoc = getXLoc(tempC);//location of the x character
			
			generateStatesDFS(tempC, finalResult, xLoc, states, currState.path,  currState.stateGenCost, currState.whereX, costFlag);//generation of all the next states
		}
		
	}
	
	public static int getXLoc(char tempC[]) {
		//find the location of the X character with linear search
		int xLoc = 0;
		for(char x: tempC) {
			if(x == 'X') {
				//System.out.println("X is located in " + xLoc);
				return xLoc;//exit the loop once the location of x has been found
			}
			else {
				xLoc++;
			}
		}	
		return tempC.length -1;
	}
	
	public static void generateStatesBFS(char tempC[], String finalResult, int xLoc, Queue<State> strings,
			ArrayList<String> parentPath, ArrayList<Integer> locationX, ArrayList<Integer> pathCost, boolean costFlag) {
		
		//step two generate all the possible states from the current state and add them to the queue
				//how to generate the state, move the x to every position except itself and switch
		
		char finalS [] = finalResult.toCharArray();
		
		for (int i = 0; i < tempC.length; i++) {//go through all characters and switch it with x
					
			char newTemp[] = tempC.clone();//create a copy of the string for each iteration
			char switchHold; //will hold the character to be swapped
					
			if(i == xLoc) {// if the index is X then skip it
				continue;
			}
				
			else {
				//add the location of where the x moves to in the array list
				locationX.add(i);
				
				//calculate the cost of movement
				int cost  = Math.abs(xLoc - i);
				
				//add the cost value to the array list
				pathCost.add(cost);
				
				//swap step one: push current index to switchHold
				switchHold = newTemp[i];
				
				//swap step two: move x to the index
				newTemp[i] = 'X';
				
				//swap step three: move switchHold to index with X
				newTemp[xLoc] = switchHold;
						
				//swap step four: now add the newTemp array, as a String, to the queue
				String result = new String(newTemp);
				
				//step 5: calculate h-value
				int h = 0;//h value, number of tiles misplaced
				for(int j = 0; j < newTemp.length; j++) {
					if(newTemp[j] != finalS[j]) {
						h++;//increment the h value for this string
					}
				}
						
				//need to add the new string to the queue
				strings.add(new State(result, parentPath, pathCost, locationX, h));
						
				//print out the step and the adjusted string
				if(costFlag == true) {
					System.out.println("move " + i + " " + result + " (c=" + cost + ")");
				}
				else {
					System.out.println("move " + i + " " + result);
				}
			}
		}
	}

	public static void generateStatesDFS(char tempC[], String finalResult, int xLoc, Stack<State> strings,
			ArrayList<String> parentPath, ArrayList<Integer> locationX, ArrayList<Integer> pathCost, boolean costFlag) {
		//step two generate all the possible states from the current state and add them to the queue
				//how to generate the state, move the x to every position except itself and switch
		
		//ArrayList<Integer> pathCost = new ArrayList<Integer>();
		char finalS [] = finalResult.toCharArray();
		
		for (int i = 0; i < tempC.length; i++) {//go through all characters and switch it with x
					
			char newTemp[] = tempC.clone();//create a copy of the string for each iteration
			char switchHold; //will hold the character to be swapped
					
			if(i == xLoc) {// if the index is X then skip it
				continue;
			}
				
			else {
				//add the location of where the x moves to in the array list
				locationX.add(i);
				
				//calculate the cost of movement
				int cost  = Math.abs(xLoc - i);
				
				//add the cost value to the array list
				pathCost.add(cost);
				
				//swap step one: push current index to switchHold
				switchHold = newTemp[i];
				
				//swap step two: move x to the index
				newTemp[i] = 'X';
				
				//swap step three: move switchHold to index with X
				newTemp[xLoc] = switchHold;
						
				//swap step four: now add the newTemp array, as a String, to the queue
				String result = new String(newTemp);
				
				//step 5: calculate h-value
				int h = 0;//h value, number of tiles misplaced
				for(int j = 0; j < newTemp.length; j++) {
					if(newTemp[j] != finalS[j]) {
						h++;//increment the h value for this string
					}
				}
						
				//add the new state to the queue
				//concurrently create a new state and pass values for node, parent path, cost and the movement of the x location
				strings.push(new State(result, parentPath, locationX, pathCost, h));
						
				//print out the step and the adjusted string
				if(costFlag == true) {
					System.out.println("move " + i + " " + result + " (c=" + cost + ")");
				}
				else {
					System.out.println("move " + i + " " + result);
				}
			}
		}
	}
	
	public static void displayFinal(State finalState, boolean costFlag) {
		
		if(costFlag == true) {
			//print out the path and all according values
			for(int i = 0; i < finalState.path.size(); i++) {
				if(i == 0) {
					System.out.println("Final Result for: " + finalState.path.get(i));
				}
				else {
					System.out.println("Step " + i + ": Move " + finalState.whereX.get(i) + " " + finalState.path.get(i)
					+ " (c=" + finalState.stateGenCost.get(i) + ")");
				}
			}
		}
		else {
			//print out the path and all according values
			for(int i = 0; i < finalState.path.size(); i++) {
				if(i == 0) {
					System.out.println("Final Result for UCS: " + finalState.path.get(i));
				}
				else {
					System.out.println("Step " + i + ": Move " + finalState.whereX.get(i) + " " + finalState.path.get(i));
				}
			}
		}
		
		System.out.println("Final State: "+ finalState.node);
		
	}

	public static void GS(String input, String finalResult, boolean costFlag) {
		//take input and generate possible states
		//choose the next possible state based on how many tiles are out of place
		
		Queue<State> states = new LinkedList<>();

		ArrayList<String> visited = new ArrayList<String>();
		
		//add the start state to the queue and a null value for cost, parent path and how the state was generated
		//also not necesary to know how many tiles out of place! set to -1
		states.add(new State(input, null, null, null, -1));
		
		while(states.isEmpty() == false){//while the queue is not empty
		
			State currState = states.poll();//poll the Queue
			
			//System.out.println(currState.node);
			
			//check if the currentState is in the visited list or not
			if(visited.contains(currState.node) == true) {//not sure if the contains method should be used! does not always work properly
				continue;//move to the next state and skip the code below
			}
			
			//add it to the list of visited strings
			visited.add(currState.node);
			
			//add the previous string to the path it has taken to get here
			if(currState.path.contains(currState.node) == false)
				currState.path.add(currState.node);
			
			if(currState.getString().equals(finalResult)) {//goal test!!!!
				//need to return the path to this state, stored in each state list
				displayFinal(currState, costFlag);
				break;//exit the loop
			}
			//if not final, analyze and create other possible states
			char tempC[] = currState.getString().toCharArray();//get the character array from the string
			
			int xLoc = getXLoc(tempC);//location of the x character
			
			generateStatesGS(tempC, finalResult, xLoc, states, currState.path, currState.stateGenCost, currState.whereX, visited, costFlag);//generation of all the next states
		}
	}
	
	public static void generateStatesGS(char tempC[], String finalResult, int xLoc, Queue<State> strings,
			ArrayList<String> parentPath, ArrayList<Integer> locationX, ArrayList<Integer> pathCost, ArrayList<String> visited, boolean costFlag) {
		
		//step two generate all the possible states from the current state and add them to the queue
				//how to generate the state, move the x to every position except itself and switch
		
		ArrayList<State> gsStates = new ArrayList<State>();
		char finalS [] = finalResult.toCharArray();
		
		//add all possible states to list and compare after to see which one to pass
		for (int i = 0; i < tempC.length; i++) {//go through all characters and switch it with x
					
			char newTemp[] = tempC.clone();//create a copy of the string for each iteration
			char switchHold; //will hold the character to be swapped
					
			if(i == xLoc) {// if the index is X then skip it
				continue;
			}
				
			else {
				//add the location of where the x moves to in the array list
				locationX.add(i);
				
				//calculate the cost of movement
				int cost  = Math.abs(xLoc - i);
				
				//add the cost value to the array list
				pathCost.add(cost);
				
				//swap step one: push current index to switchHold
				switchHold = newTemp[i];
				
				//swap step two: move x to the index
				newTemp[i] = 'X';
				
				//swap step three: move switchHold to index with X
				newTemp[xLoc] = switchHold;
						
				//swap step four: now add the newTemp array, as a String, to the queue
				String result = new String(newTemp);
				
				//step 5: calculate h-value
				int h = 0;//h value, number of tiles misplaced
				for(int j = 0; j < newTemp.length; j++) {
					if(newTemp[j] != finalS[j]) {
						h++;//increment the h value for this string
					}
				}
						
				//need to add the new string to the list
				gsStates.add(new State(result, parentPath, pathCost, locationX, h));
						
				//print out the step and the adjusted string
				if(costFlag == true) {
					System.out.println("move " + i + " " + result + " (c=" + cost + ")");
				}
				else {
					System.out.println("move " + i + " " + result);
				}
			}
		}
		while(gsStates.isEmpty() == false) {//order gsStates and add them to the strings queue
			//search gsStates for lowest h-val and add to strings loop until gsStates is empty
			int min = 999;
			for(State s: gsStates) {
				if(s.hVal < min) {
					min = s.hVal;
				}
			}//at this point the minimum value should be returned 
			for(int i = 0; i < gsStates.size(); i++) {
				if(gsStates.get(i).hVal == min) {
					strings.add(gsStates.get(i));
					gsStates.remove(gsStates.get(i));
				}
			}
		}
		
	}

	public static void UCS(String input, String finalResult, boolean costFlag) {
		
		Queue<State> states = new LinkedList<>();

		ArrayList<String> visited = new ArrayList<String>();
		
		ArrayList<State> notQueue = new ArrayList<State>();//place all values in here first and then remove to place in other queue
		
		notQueue.add(new State(input, null, null, null, -1));//add the start state to the queue and a null value for cost, parent path and how the state was generated
		
		//for UCS before we poll, we must sort the queue based on number of moves executed ( gVal )
		//need to create a new queue
		//then go through queue and find states with lowest gVal and place into new queue
		
		while(notQueue.isEmpty() == false) {
			int min = 999;
			for(State s: notQueue) {
				if(s.gVal < min) {
					min = s.gVal;
				}
			}//at this point the minimum gVal is found
			for(int i = 0; i < notQueue.size(); i++) {//from this point we can add the minimum value to states queue
				if(notQueue.get(i).hVal == min) {
					states.add(notQueue.get(i));
					notQueue.remove(notQueue.get(i));
				}
			}
		}
		
		while(states.isEmpty() == false){//while the queue is not empty
			
			State currState = states.poll();
			
			//System.out.println(currState.node);
			
			//check if the currentState is in the visited list or not
			if(visited.contains(currState.node) == true) {//not sure if the contains method should be used! does not always work properly
				continue;//move to the next state and skip the code below
			}
			
			//add it to the list of visited strings
			visited.add(currState.node);
			
			//add the previous string to the path it has taken to get here
			if(currState.path.contains(currState.node) == false)
				currState.path.add(currState.node);
			
			if(currState.getString().equals(finalResult)) {//goal test!!!!
				//need to return the path to this state, stored in each state list
				displayFinal(currState, costFlag);
				break;//exit the loop
			}
			//if not final, analyze and create other possible states
			
			char tempC[] = currState.getString().toCharArray();//get the character array from the string
			
			int xLoc = getXLoc(tempC);//location of the x character
			
			generateStatesUCS(tempC, finalResult, xLoc, states, currState.path, currState.stateGenCost, currState.whereX, visited, costFlag);
		}//generation of all the next states
	}

	public static void generateStatesUCS(char tempC[], String finalResult, int xLoc, Queue<State> strings,
				ArrayList<String> parentPath, ArrayList<Integer> locationX, ArrayList<Integer> pathCost, ArrayList<String> visited, boolean costFlag) {
		//step two generate all the possible states from the current state and add them to the queue
		//how to generate the state, move the x to every position except itself and switch

		char finalS [] = finalResult.toCharArray();
		
		for (int i = 0; i < tempC.length; i++) {//go through all characters and switch it with x
					
			char newTemp[] = tempC.clone();//create a copy of the string for each iteration
			char switchHold; //will hold the character to be swapped
					
			if(i == xLoc) {// if the index is X then skip it
				continue;
			}
				
			else {
				//add the location of where the x moves to in the array list
				locationX.add(i);
				
				//calculate the cost of movement
				int cost  = Math.abs(xLoc - i);
				
				//add the cost value to the array list
				pathCost.add(cost);
				
				//swap step one: push current index to switchHold
				switchHold = newTemp[i];
				
				//swap step two: move x to the index
				newTemp[i] = 'X';
				
				//swap step three: move switchHold to index with X
				newTemp[xLoc] = switchHold;
						
				//swap step four: now add the newTemp array, as a String, to the queue
				String result = new String(newTemp);
				
				//step 5: calculate h-value
				int h = 0;//h value, number of tiles misplaced
				for(int j = 0; j < newTemp.length; j++) {
					if(newTemp[j] != finalS[j]) {
						h++;//increment the h value for this string
					}
				}
						
				//need to add the new string to the queue
				strings.add(new State(result, parentPath, pathCost, locationX, h));
						
				//print out the step and the adjusted string
				if(costFlag == true) {
					System.out.println("move " + i + " " + result + " (c=" + cost + ")");
				}
				else {
					System.out.println("move " + i + " " + result);
				}
			}
		}
	}
	
	public static void AStar(String input, String finalResult, boolean costFlag) {
		Queue<State> states = new LinkedList<>();

		ArrayList<String> visited = new ArrayList<String>();
		
		ArrayList<State> notQueue = new ArrayList<State>();//place all values in here first and then remove to place in other queue
		
		notQueue.add(new State(input, null, null, null, -1));//add the start state to the queue and a null value for cost, parent path and how the state was generated
		
		//for UCS before we poll, we must sort the queue based on number of moves executed ( gVal )
		//need to create a new queue
		//then go through queue and find states with lowest gVal and place into new queue
		
		while(notQueue.isEmpty() == false) {
			int min = 999;
			for(State s: notQueue) {
				if(s.fVal < min) {
					min = s.fVal;
				}
			}//at this point the minimum gVal is found
			for(int i = 0; i < notQueue.size(); i++) {//from this point we can add the minimum value to states queue
				if(notQueue.get(i).fVal == min) {
					states.add(notQueue.get(i));
					notQueue.remove(notQueue.get(i));
				}
			}
		}
		
		while(states.isEmpty() == false){//while the queue is not empty
			
			State currState = states.poll();
			
			//System.out.println(currState.node);
			
			//check if the currentState is in the visited list or not
			if(visited.contains(currState.node) == true) {//not sure if the contains method should be used! does not always work properly
				continue;//move to the next state and skip the code below
			}
			
			//add it to the list of visited strings
			visited.add(currState.node);
			
			//add the previous string to the path it has taken to get here
			if(currState.path.contains(currState.node) == false)
				currState.path.add(currState.node);
			
			if(currState.getString().equals(finalResult)) {//goal test!!!!
				//need to return the path to this state, stored in each state list
				displayFinal(currState, costFlag);
				break;//exit the loop
			}
			//if not final, analyze and create other possible states
			
			char tempC[] = currState.getString().toCharArray();//get the character array from the string
			
			int xLoc = getXLoc(tempC);//location of the x character
			
			generateStatesAStar(tempC, finalResult, xLoc, states, currState.path, currState.stateGenCost, currState.whereX, visited, costFlag);
		}//generation of all the next states
	}
	
	public static void generateStatesAStar(char tempC[], String finalResult, int xLoc, Queue<State> strings,
			ArrayList<String> parentPath, ArrayList<Integer> locationX, ArrayList<Integer> pathCost, ArrayList<String> visited, boolean costFlag) {
	//step two generate all the possible states from the current state and add them to the queue
	//how to generate the state, move the x to every position except itself and switch

		char finalS [] = finalResult.toCharArray();
		
		for (int i = 0; i < tempC.length; i++) {//go through all characters and switch it with x
					
			char newTemp[] = tempC.clone();//create a copy of the string for each iteration
			char switchHold; //will hold the character to be swapped
					
			if(i == xLoc) {// if the index is X then skip it
				continue;
			}
				
			else {
				//add the location of where the x moves to in the array list
				locationX.add(i);
				
				//calculate the cost of movement
				int cost  = Math.abs(xLoc - i);
				
				//add the cost value to the array list
				pathCost.add(cost);
				
				//swap step one: push current index to switchHold
				switchHold = newTemp[i];
				
				//swap step two: move x to the index
				newTemp[i] = 'X';
				
				//swap step three: move switchHold to index with X
				newTemp[xLoc] = switchHold;
						
				//swap step four: now add the newTemp array, as a String, to the queue
				String result = new String(newTemp);
				
				//step 5: calculate h-value
				int h = 0;//h value, number of tiles misplaced
				for(int j = 0; j < newTemp.length; j++) {
					if(newTemp[j] != finalS[j]) {
						h++;//increment the h value for this string
					}
				}
						
				//need to add the new string to the queue
				strings.add(new State(result, parentPath, pathCost, locationX, h));
						
				//print out the step and the adjusted string
				if(costFlag == true) {
					System.out.println("move " + i + " " + result + " (c=" + cost + ")");
				}
				else {
					System.out.println("move " + i + " " + result);
				}
			}
		}
	}
}
