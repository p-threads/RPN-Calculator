//Author: Ke Shi
//Date:   05/July/2017
//Func:   RPN Calculator

import java.io.*;
import java.util.*;

public class Calculator {
	public static Stack<Double> st = new Stack<>();
	public static Stack<Double> undoSt = new Stack<>();
	public static String last_move;

	public Calculator(Stack my_st, Stack my_undoSt, String my_last_move){
		Stack st = my_st;
		Stack undoSt = my_undoSt;
		last_move = my_last_move;
	}

	/*the calculate function*/
	/*3 paremeters: compute stack, redo stack, an indicator indicates the type of last move*/
	public static void calculate(Stack my_st, Stack my_undoSt, String my_last_move){
		//get user input from the command line
		Console console = System.console();
		String user_input = console.readLine();
		//parse user input
		String[] tokens = user_input.split(" ");
		//record the position of each argument and operator
		int position = -1;
		//a set of basic operations
		String[] op = {"+", "-", "*", "/"};
		//iterate input elements
		for(int i = 0; i < tokens.length; i++){
			String token = tokens[i];
			position += 1;
			if(Arrays.asList(op).contains(token)){
				if(st.size() < 2){
					//format error output
					System.out.println("operator"+token+"(position: "+String.valueOf(position*2+1)+"): insufficient parameters");
					break;
				}
				else{
					Double arg2 = (Double)st.pop();
					Double arg1 = (Double)st.pop();
					undoSt.push(arg2);
					undoSt.push(arg1);
					last_move = "cal";

					if(token.equals("+")){
						double result = arg1 + arg2; 
						st.push(result);
					}
					else if (token.equals("-")) {
						double result = arg1 - arg2;
						st.push(result);
					}
					else if (token.equals("*")) {
						double result = arg1 * arg2;
						st.push(result);
					}
					else if (token.equals("/")) {
						double result = arg1 / arg2;
						st.push(result);
					}
				}
			}
			else if (token.equals("sqrt")) {
				if(st.size() < 1){
					//format output
					System.out.println("operator"+token+"(position: "+String.valueOf(position*2+1)+"): insufficient parameters");
					break;
				}
				else {
					Double result = Math.sqrt((Double)st.peek());
					undoSt.push(st.pop());
					st.push(result);
					last_move = "calsqrt";
				}
			}
			else if (token.equals("undo")) {
				if(last_move.equals("calsqrt")){
					st.pop();
					st.push(undoSt.pop());
				}
				else if (last_move.equals("cal")) {
					st.pop();
					st.push(undoSt.pop());
					st.push(undoSt.pop());
				}
				else if (last_move.equals("append")){
					st.pop();
				}
			}
			else if (token.equals("clear")) {
				st.clear();
			}
			else{
				st.push(new Double(token));
				undoSt.push(new Double(token));
				last_move = "append";
			}
		}
		//format output string
		String strStack = Arrays.toString(st.toArray());
		strStack = strStack.substring(1, strStack.length()-1);
		strStack = deleteAll(strStack, ",");

		System.out.println("stack: "+ strStack);
		calculate(st, undoSt, last_move);
		return;				
	}
   private static String deleteAll(String strValue, String charToRemove) {
		return strValue.replaceAll(charToRemove, "");

	}
   public static void main(String[] args) {
    	Calculator.calculate(new Stack(), new Stack(), "append");
   }
}