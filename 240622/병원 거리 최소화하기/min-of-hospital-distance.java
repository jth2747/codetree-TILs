import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

public class Main {
	static int n, m;
	static int[][] arr;
	static int[][] res;
	static Info[] slist;
	static ArrayList<Pos> hosp, people;
	public static void main(String[] args) throws IOException {
		//System.setIn(new FileInputStream("src/input.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		
		arr = new int[n][n];
		hosp = new ArrayList<Pos>();
		people = new ArrayList<Pos>();
		
		for (int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < n; j++) {
				arr[i][j] = Integer.parseInt(st.nextToken());
				
				if(arr[i][j] == 1) {
					people.add(new Pos(i, j));
				} else if (arr[i][j] == 2) {
					hosp.add(new Pos(i, j));
				}
			}
		}
		
		res = new int[people.size()][hosp.size()];
		
		slist = new Info[hosp.size()];
		
		for (int i = 0; i < res[0].length; i++) {
			process(i);
		}
		
		Arrays.sort(slist);
		
		int ans = 0;
		for (int i = 0; i < people.size(); i++) {
			int minN = Integer.MAX_VALUE;
			for (int j = 0; j < m; j++) {
				if(res[i][slist[j].idx] < minN) {
					minN = res[i][slist[j].idx];
				}
			}
			
			ans += minN;
		}
		
		System.out.println(ans);
	}
	
	public static void process(int hnum) {
		int sum = 0;
		for (int i = 0; i < people.size(); i++) {
			int dist = 0;
			dist = Math.abs(people.get(i).r - hosp.get(hnum).r)
				+ Math.abs(people.get(i).c - hosp.get(hnum).c);
			
			sum += dist;
			res[i][hnum] = dist;
		}
		
		slist[hnum] = new Info(hnum, sum);
	}
	
	public static class Pos {
		int r;
		int c;
		
		public Pos(int r, int c) {
			this.r = r;
			this.c = c;
		}
	}
	
	public static class Info implements Comparable<Info> {
		int idx;
		int sum;
		
		public Info(int idx, int sum) {
			this.idx = idx;
			this.sum = sum;
		}
		
		@Override
		public int compareTo(Info o) {
			return this.sum - o.sum; //오름차순, 자신 - 남꺼
		}
	}
}