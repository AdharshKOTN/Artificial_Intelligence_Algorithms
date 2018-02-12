import java.util.ArrayList;

public class State {
	
	//the data for the state is the string
	String node;
	//every state will have a list associated with it
	ArrayList<String> path = new ArrayList<String>();
	//store how the string was generated
	ArrayList<Integer> whereX = new ArrayList<Integer>();//all we need to know is what position the x moved to! therefore parallel arraylist can be utilized
	//store the cost of any state generation
	ArrayList<Integer> stateGenCost = new ArrayList<Integer>();
	//store the h-Value, number of tiles out of place
	int hVal;
	//store the g-value, does not have to be an array list because it is essentially a running total
	int gVal;
	//store the f-value
	int fVal;
	
	public State(String node, ArrayList<String> previousPath, ArrayList<Integer> whereX, ArrayList<Integer> stateGenCost, int hVal) {
		this.node = node;
		this.gVal = 0;//initialize the g-val
		if(previousPath != null) {//if not the first state
			for(String s: previousPath) {//iterate through the list and add each string to the next state path
				path.add(s);//add the path with constructor
				this.gVal++;//updating the gVal happens whenever the state is created for however many states have been created thus far
			}
			for(Integer i:whereX) {//iterate through the list and add each location of the movement of x
				this.whereX.add(i);//add the values to the arraylist
			}
			for(Integer i: stateGenCost) {//iterate through the list and add each location of the movement of x
				this.stateGenCost.add(i);//add the values to the arraylist
			}
			this.hVal = hVal;//assign the h-val
			
			this.fVal = this.gVal + this.hVal;//provide the fVal for the state
		}
	}

	public String getString() {
		return node;
	}
	
	/*public String toString() {
		System.out.print("this is the node and path: " + node);
		
		for(int i = path.size() -1; i >= 0; i--) {
			System.out.print(" " + path.get(i));
		}
		return "\n";
	}*/
}
