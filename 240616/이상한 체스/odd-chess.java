import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

public class Main {
	static int[] dr = {-1, 0, 1, 0}; //상우하좌
	static int[] dc = {0, 1, 0, -1};
	static int n, m;
	static int[][] arr;
	static int[][] visited;
	static ArrayList<Pos> pos;
	static int minn;
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		st = new StringTokenizer(br.readLine());
		
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		
		arr = new int[n][m];
		visited = new int[n][m];
		pos = new ArrayList<Pos>();
		minn = Integer.MAX_VALUE;
		for (int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < m; j++) {
				arr[i][j] = Integer.parseInt(st.nextToken());
				
				if(arr[i][j] == 6) visited[i][j] = 1;
				
				if(arr[i][j] != 0 && arr[i][j] != 6) {
					visited[i][j] = 1;
					pos.add(new Pos(arr[i][j], i, j));
				}
			}
		}
		
		int[] num = new int[pos.size()];
		dfs(0, num);
		
		System.out.println(minn);
	}
	
	public static void dfs(int seq, int[] num) {
		if(seq == pos.size()) {
			int[][] visited = new int[n][m];
			process(visited, num);
			return;
		}
		
		for (int i = 0; i < 4; i++) {
			if(pos.get(seq).num == 2) {
				if(i == 2 || i ==3) {
					continue;
				}
			}
			if(pos.get(seq).num == 5) {
				if(i==1 || i == 2 || i ==3) {
					continue;
				}
			}
			
			num[seq] = i;
			dfs(seq+1, num);
		}
	}
	
	public static void process(int[][] visited, int[] num) {
		for (int i = 0; i < pos.size(); i++) {
			int cur = pos.get(i).num;
			
			if(cur == 1) {
				check(visited, num[i], pos.get(i).r, pos.get(i).c);
			} else if (cur == 2) {
				check(visited, num[i], pos.get(i).r, pos.get(i).c);
				check(visited, (num[i] + 2), pos.get(i).r, pos.get(i).c);
			} else if (cur == 3) {
				check(visited, num[i], pos.get(i).r, pos.get(i).c);
				check(visited, (num[i] + 1) % 4, pos.get(i).r, pos.get(i).c);
			} else if (cur == 4) {
				check(visited, num[i], pos.get(i).r, pos.get(i).c);
				check(visited, (num[i] + 1) % 4, pos.get(i).r, pos.get(i).c);
				check(visited, (num[i] - 1 + 4) % 4, pos.get(i).r, pos.get(i).c);
			} else {
				for (int d = 0; d < dr.length; d++) {
					check(visited, d, pos.get(i).r, pos.get(i).c);
				}
			}
		}
		
		
		int cnt = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if(visited[i][j] == 0 && arr[i][j] == 0) {
					cnt++;
				}
			}
		}
		
		if(minn > cnt) minn = cnt;
	}
	
	public static void check(int[][] visited, int dir, int r, int c) {
		visited[r][c] = 1;
		int nr = r;
		int nc = c;
		
		while(true) {
			nr += dr[dir];
			nc += dc[dir];
			
			if(nr < 0 || nc < 0 || nr >= n || nc >= m) {
				break;
			}
			
			if(arr[nr][nc] == 6) {
				break;
			}
			
			visited[nr][nc] = 1;
		}
	}
	
	public static class Pos {
		int num;
		int r;
		int c;
		
		public Pos(int num, int r, int c) {
			this.num = num;
			this.r = r;
			this.c = c;
		}
	}
}