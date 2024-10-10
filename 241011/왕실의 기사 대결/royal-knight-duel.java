import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	static int[] dr = {-1, 0, 1, 0}; //상우하좌
	static int[] dc = {0, 1, 0, -1};
	static int L, N, Q, curNum;
	static Knight[] kList;
	static int[][] obs, map;
	static int[] health, w, h, d;
	static int[] checked, moved, out;
	public static void main(String[] args) throws IOException {
		//System.setIn(new FileInputStream("src/input.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		st = new StringTokenizer(br.readLine());
		L = Integer.parseInt(st.nextToken()); //맵크기
		N = Integer.parseInt(st.nextToken()); //기사 수
		Q = Integer.parseInt(st.nextToken()); //명령 수
		
		obs = new int[L][L];
		map = new int[L][L];
		
		for (int i = 0; i < L; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < L; j++) {
				obs[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		kList = new Knight[N+1];
		health = new int[N+1];
		w = new int[N+1];
		h = new int[N+1];
		d = new int[N+1];
		out = new int[N+1];
		
		for (int i = 1; i <= N; i++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken()) - 1;
			int c = Integer.parseInt(st.nextToken()) - 1;
			int wide = Integer.parseInt(st.nextToken());
			int height = Integer.parseInt(st.nextToken());
			int k = Integer.parseInt(st.nextToken());
			
			kList[i] = new Knight(r, c);
			health[i] = k;
			w[i] = wide;
			h[i] = height;
		}
		
		
		for (int i = 0; i < Q; i++) {
			drawMap();
			st = new StringTokenizer(br.readLine());
			curNum = Integer.parseInt(st.nextToken());
			int curDir = Integer.parseInt(st.nextToken());
			
			//디버그용 int -> boolean
			checked = new int[N+1];
			moved = new int[N+1];
			
			boolean cflag = checkQ(curNum, curDir);
			
			if(cflag) {
				move(curNum, curDir);
				drawMap();
				damage();
			}
			
			
			
			for (int j = 1; j <= N; j++) {
				if(health[j] <= 0) {
					out[j] = 1;
				}
			}
		}
		
		int ans = 0;
		for (int i = 1; i <= N; i++) {
			if(out[i] == 1) continue;
			
			ans += d[i];
		}
		
		System.out.println(ans);
	}
	
	public static void damage() {
		for (int i = 0; i < L; i++) {
			for (int j = 0; j < L; j++) {
				if(map[i][j] == 0) continue;
				
				if(map[i][j] == curNum) continue;
				if(moved[map[i][j]] == 0) continue;
				
				if(obs[i][j] == 1) {
					health[map[i][j]]--;
					d[map[i][j]]++;
				}
			}
		}
	}
	
	public static void move(int knum, int dir) {
		for (int i = 1; i <= N; i++) {
			if(moved[i] == 0) continue;
			
			int nr = kList[i].r + dr[dir];
			int nc = kList[i].c + dc[dir];
			
			kList[i].r = nr;
			kList[i].c = nc;
		}
	}
	
	public static boolean check(int knum, int dir) {
		checked[knum] = 1;
		int currfrom = kList[knum].r;
		int curcfrom = kList[knum].c;
		int currto = currfrom + w[knum];
		int curcto = curcfrom + h[knum];
//		if(dir == 0) {
//			currto = currfrom + 1;
//		} else if(dir == 1) {
//			curcfrom = curcto;
//			curcto = curcto + 1;
//		} else if(dir == 2) {
//			currfrom = currto;
//			currto = currto + 1;
//		} else {
//			curcto = curcfrom + 1;
//		}
		for (int i = currfrom; i < currto; i++) {
			for (int j = curcfrom; j < curcto; j++) { 
				int nr = i + dr[dir];
				int nc = j + dc[dir];
				
				if(nr < 0 || nc < 0 || nr >= L || nc >= L) {
					return false;
				}
				
				if(obs[nr][nc] == 2) return false;
				
				//나자신은 다음꺼그냥 보기
				//if(map[nr][nc] == knum) continue;
				
				if(map[nr][nc] != 0) {
					if(checked[map[nr][nc]] == 1) continue;
					
					boolean flag = check(map[nr][nc], dir);
					
					if(!flag) return false;
				}
			}
		}
		
		moved[knum] = 1;
		return true;
	}
	
	public static boolean checkQ(int knum, int dir) {
		Queue<Integer> q = new LinkedList<Integer>();
		
		q.offer(knum);
		checked[knum] = 1;
		
		
		
		while(!q.isEmpty()) {
			int cur = q.poll();
			
			int currfrom = kList[cur].r;
			int curcfrom = kList[cur].c;
			int currto = currfrom + w[cur];
			int curcto = curcfrom + h[cur];
			for (int i = currfrom; i < currto; i++) {
				for (int j = curcfrom; j < curcto; j++) { 
					int nr = i + dr[dir];
					int nc = j + dc[dir];
					
					if(nr < 0 || nc < 0 || nr >= L || nc >= L) {
						return false;
					}
					
					if(obs[nr][nc] == 2) return false;
					
					//나자신은 다음꺼그냥 보기
					if(map[nr][nc] == cur) continue;
					
					if(map[nr][nc] != 0) {
						q.offer(map[nr][nc]);
						checked[map[nr][nc]] = 1;
					}
				}
			}
			
		}
		
		
		for (int i = 1; i <= N; i++) {
			if(checked[i] == 1) moved[i] = 1;
		}
		
		return true;
	}
	
	public static void drawMap() {
		map = new int[L][L];
		for (int i = 1; i <= N; i++) {
			if(out[i] == 1) continue;
			int curr = kList[i].r;
			int curc = kList[i].c;
			
			for (int j = curr; j < curr+w[i]; j++) {
				for (int k = curc; k < curc+h[i]; k++) {
					map[j][k] = i;
				}
			}
		}
	}
	
	public static class Knight	{
		int r;
		int c;
		
		public Knight(int r, int c) {
			this.r = r;
			this.c = c;
		}
	}
}