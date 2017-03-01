package game_playing_agent;

import java.io.*;
import java.util.*;

public class my_agent {
	
	public static move final_move;
	public static boolean test = true;

	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		try {
			BufferedReader input_file = new BufferedReader(new FileReader("input.txt"));
			int N = Integer.parseInt(input_file.readLine());
			String algorithm = input_file.readLine();
			char player = input_file.readLine().charAt(0);
			int timer = Integer.parseInt(input_file.readLine());
			int empty = 0, cutoff;
//			System.out.println("N = "+ N +" algo = "+ algorithm +" player = "+ player +" cutoff = "+ cutoff);
			int[][] values = new int[N][N];
			char[][] grid = new char[N][N];
			Timer alarm = new Timer();
//			int cutoff_time;
			
			for(int i = 0; i < N; i++){
				String[] temp = input_file.readLine().split(" ");
				for(int j = 0; j < N; j++){
					values[i][j] = Integer.parseInt(temp[j]);
				}
			}
			
			for(int i = 0; i < N; i++){
				String temp = input_file.readLine();
				for(int j = 0; j < N; j++){
					if(temp.charAt(j) == '.'){
						empty++;
					}
					grid[i][j] = temp.charAt(j);
//					System.out.print(values[i][j] +" ");
				}
//				System.out.println();
			}
			
			float cutoff_time = (float)(timer) * 2/empty;
			
			alarm.schedule(new TimerTask() {
				@Override
				public void run(){
//					System.out.println("DONE!!");
//					test = false;
						try {
							printing_function(grid, final_move, player, N);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.exit(0);
				}
			}, (long) (cutoff_time * 1000));
			cutoff = 1;
			
			char opponent = 'O';
			
			if(player == 'O'){
				opponent = 'X';
			}
			
			char[] players = {player, opponent};
			
			while(test == true){
				final_move = alpha_beta_decision(grid, values, players, N, 0, cutoff);			
				cutoff++;
//				System.out.println("Something.");
			}	
			System.out.println("Out");
			alarm.cancel();
//			printing_function(grid, final_move, player, N);
			input_file.close();
			
			
//			System.exit(0);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void printing_function(char[][] grid, move my_move, char player, int N) throws IOException{
		BufferedWriter output_file = new BufferedWriter(new FileWriter(new File("output.txt")));
		char[][] new_grid = executeMove(my_move, grid, player, N);
		char A = (char) ((int)'A' + my_move.z);
		output_file.write(A);
		output_file.write(my_move.y + 1 + " " + my_move.x + "\n");
		
		for(int i = 0; i < N; i++){
			for(int j = 0; j < N; j++){
				output_file.write(new_grid[i][j]);
			}
			if(i < N - 1){
				output_file.write("\n");
			}
		}
		output_file.close();
	}
	
	public static void possible_stakes_and_raids(ArrayList<move> moves, char[][] grid, int[][] values, char player, int N){
		
		for(int i = 0; i < N; i++){
			for(int j = 0; j < N; j++){
				if(grid[i][j] == '.'){
					moves.add(new move("Stake", i, j, values[i][j]));
					int temp = check_for_raids(i, j, grid, values, player, N);
					if(temp > 0){
						moves.add(new move("Raid", i, j, temp + values[i][j]));
					}
				}
			}
		}
	}
	
	public static int check_for_raids(int r, int c, char[][] grid, int[][] values, char player, int N){
		int player_count = 0;
		int opponent_count = 0;
		
		if(c + 1 < N){
			if(grid[r][c + 1] == player){
				player_count++;
			}
			else if(grid[r][c + 1] != '.'){
				opponent_count += values[r][c + 1];
			}
		}
		if(c - 1 >= 0){
			if(grid[r][c - 1] == player){
				player_count++;
			}
			else if(grid[r][c - 1] != '.'){
				opponent_count += values[r][c - 1];
			}
		}
		if(r + 1 < N){
			if(grid[r + 1][c] == player){
				player_count++;
			}
			else if(grid[r + 1][c] != '.'){
				opponent_count += values[r + 1][c];
			}
		}
		if(r - 1 >= 0){
			if(grid[r - 1][c] == player){
				player_count++;
			}
			else if(grid[r - 1][c] != '.'){
				opponent_count += values[r - 1][c];
			}
		}
		
		if(player_count > 0 && opponent_count > 0){
			return opponent_count;
		}
		else{
			return 0;
		}
	}
	
	public static char[][] executeMove(move my_move, char[][] grid, char player, int N){
		char[][] new_grid = new char[N][N];
		for(int i = 0; i < N; i++){
			for(int j = 0; j < N; j++){
				new_grid[i][j] = grid[i][j];
			}
//			System.out.println();
		}
//		System.out.println();
//		System.out.println();
		if(my_move.x == "Stake"){
			new_grid[my_move.y][my_move.z] = player;
		}
		else{
			int r = my_move.y;
			int c = my_move.z;
			new_grid[r][c] = player;
			
			if(c + 1 < N){
				if(grid[r][c + 1] != '.'){
					new_grid[r][c + 1] = player;
				}
			}
			if(c - 1 >= 0){
				if(grid[r][c - 1] != '.'){
					new_grid[r][c - 1] = player;
				}
			}
			if(r + 1 < N){
				if(grid[r + 1][c] != '.'){
					new_grid[r + 1][c] = player;
				}
			}
			if(r - 1 >= 0){
				if(grid[r - 1][c] != '.'){
					new_grid[r - 1][c] = player;
				}
			}
		}
//		for(int i = 0; i < N; i++){
//			for(int j = 0; j < N; j++){
//				System.out.print(grid[i][j]);
//			}
//			System.out.println();
//		}
//		System.out.println("new_grid");
////		System.out.println();
//		for(int i = 0; i < N; i++){
//			for(int j = 0; j < N; j++){
//				System.out.print(grid[i][j]);
//			}
//			System.out.println();
//		}
//		System.out.println();
//		System.out.println();
		
		return new_grid;
	}
	
	public static int evaluation(char[][] grid, int[][] values, char player, int N){
		
		int value = 0;
		
		for(int r = 0; r < N; r++){
			for(int c = 0; c < N; c++){
				if(grid[r][c] == player){
					int player_count = 5;
					
					if(c + 1 < N && grid[r][c + 1] == '.'){
						player_count--;
					}
					if(c - 1 >= 0 && grid[r][c - 1] == '.'){
						player_count--;
					}
					if(r + 1 < N && grid[r + 1][c] == '.'){
						player_count--;
					}
					if(r - 1 >= 0 && grid[r - 1][c] == '.'){
						player_count--;
					}
					
					value += ((float)player_count/5) * values[r][c];
				}
				else if(grid[r][c] != '.'){
					int player_count = 5;
					
					if(c + 1 < N && grid[r][c + 1] == '.'){
						player_count--;
					}
					if(c - 1 >= 0 && grid[r][c - 1] == '.'){
						player_count--;
					}
					if(r + 1 < N && grid[r + 1][c] == '.'){
						player_count--;
					}
					if(r - 1 >= 0 && grid[r - 1][c] == '.'){
						player_count--;
					}
					value -= ((float)player_count/5) * values[r][c];
				}
			}
		}
		
		return value;
//		return utility(grid, values, player, N);
	}
	
	public static int utility(char[][] grid, int[][] values, char player, int N){
		int value = 0;
		
		for(int i = 0; i < N; i++){
			for(int j = 0; j < N; j++){
				if(grid[i][j] == player){
					value += values[i][j];
				}
				else if(grid[i][j] != '.'){
					value -= values[i][j];
				}
			}
		}
		
		return value;
	}
	
	public static move alpha_beta_decision(char[][] grid, int[][] values, char[] players, int N, int depth, int cutoff){
		int v = -9999, alpha = -9999, beta = 9999;
		move my_move = new move("", -1, -1, -1);
		ArrayList<move> moves = new ArrayList<move>();
//		ArrayList<move> raids = new ArrayList<move>();
		
		possible_stakes_and_raids(moves, grid, values, players[0], N);
		
		Collections.sort(moves, new Comparator<move>() {
			@Override
			public int compare(move move1, move move2) {
	    		return Integer.compare(move1.getValue()*-1, move2.getValue()*-1);
			}
		});
		// moves.sort((move1, move2) -> Integer.compare(move1.getValue()*-1, move2.getValue()*-1));
//		raids.sort((move1, move2) -> Integer.compare(move1.getValue(), move2.getValue()));
		
		for(move temp: moves){
			
//			for(int i = 0; i < N; i++){
//				for(int j = 0; j < N; j++){
//					System.out.print(grid[i][j]);
//				}
//				System.out.println();
//			}
//			System.out.println();
//			System.out.println();
			int new_value = min_value(executeMove(temp, grid, players[0], N), values, players, N, depth + 1, cutoff, alpha, beta);
//			System.out.println(temp.x + " " + temp.y + " " + temp.z + " " + temp.value + " " + new_value);
//			for(int i = 0; i < N; i++){
//				for(int j = 0; j < N; j++){
//					System.out.print(grid[i][j]);
//				}
//				System.out.println();
//			}
//			System.out.println();
//			System.out.println();
//			System.out.println(new_value);
			if(v < new_value){
				v = new_value;
				my_move = temp;
			}
		}
		
//		System.out.println(v);
		return my_move;
		
	}
	
	public static int min_value(char[][] grid, int[][] values, char[] players, int N, int depth, int cutoff, int alpha, int beta){
		
		if(depth == cutoff){
			return evaluation(grid, values, players[0], N);
		}
		else{
			int v = 9999;
			ArrayList<move> moves = new ArrayList<move>();
			
			possible_stakes_and_raids(moves, grid, values, players[1], N);
		
			if(moves.size() == 0){
				return utility(grid, values, players[0], N);
			}
			Collections.sort(moves, new Comparator<move>() {
			@Override
			public int compare(move move1, move move2) {
	    		return Integer.compare(move1.getValue()*-1, move2.getValue()*-1);
			}
		});
			// moves.sort((move1, move2) -> Integer.compare(move1.getValue()*-1, move2.getValue()*-1));
			
			for(move temp : moves){
				int new_value = max_value(executeMove(temp, grid, players[1], N), values, players, N, depth + 1, cutoff, alpha, beta);
				if(v > new_value){
					v = new_value;
					if(v <= alpha){
						return v;
					}
					else if(beta > v){
						beta = v;
					}
				}
			}
			
			return v;
		}
	}
	
	public static int max_value(char[][] grid, int[][] values, char[] players, int N, int depth, int cutoff, int alpha, int beta){
		
		if(depth == cutoff){
			return evaluation(grid, values, players[0], N);
		}
		else{
			int v = -9999;
			ArrayList<move> moves = new ArrayList<move>();
			
			possible_stakes_and_raids(moves, grid, values, players[0], N);
		
			if(moves.size() == 0){
				return utility(grid, values, players[0], N);
			}
			Collections.sort(moves, new Comparator<move>() {
			@Override
			public int compare(move move1, move move2) {
	    		return Integer.compare(move1.getValue()*-1, move2.getValue()*-1);
			}
		});
			// moves.sort((move1, move2) -> Integer.compare(move1.getValue()*-1, move2.getValue()*-1));
			
			for(move temp : moves){
				int new_value = min_value(executeMove(temp, grid, players[0], N), values, players, N, depth + 1, cutoff, alpha, beta);
				if(v < new_value){
					v = new_value;
					if(v >= beta){
						return v;
					}
					else if(alpha < v){
						alpha = v;
					}
				}
			}
			
			return v;
		}
	}

}

final class move {
	public String x;
	public int y;
	public int z;
	public int value;
	
	public move(String x, int y, int z, int value){
		this.x = x;
		this.y = y;
		this.z = z;
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
}
