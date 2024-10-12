import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	static int[] dr = {-1, 0, 1, 0};
	static int[] dc = {0, 1, 0, -1};
	static int [][] orgMap, map, visited, nextMap;
	static int[] yumul;
	static int yidx;
	static int K, M, ans;
	static int fRot, fR, fC;
	static int maxVal, firstMax;
	static ArrayList<Pos> list;
	static boolean flag;
	public static void main(String[] args) throws IOException {
		//System.setIn(new FileInputStream("src/input.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		st = new StringTokenizer(br.readLine());
		K = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		yumul = new int[M];
		
//		이 유적지는 5×5 격자 형태로 되어 있으며, 
		orgMap = new int[5][5];
		
		for (int i = 0; i < 5; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < 5; j++) {
				orgMap[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < M; i++) {
			yumul[i] = Integer.parseInt(st.nextToken());
		}
		
		map = new int[5][5];
		
		for (int tc = 0; tc < K; tc++) {
			ans = 0;
			maxVal = 0;
			firstMax = Integer.MIN_VALUE;
			nextMap = new int[5][5];
			fRot = 0;
			fR = 0;
			fC = 0;
			//yidx = 0;
			//yidx = 0;
			//90도, 180도, 270도 중 하나의 각도, 90도 회전을 몇번할지에 대한 체크
			for (int rot = 1; rot <= 3; rot++) {
				//yidx = 0;
				for (int r = 1; r <= 3; r++) {
					for (int c = 1; c <= 3; c++) {
						mapSet();
						
						
						flag = false;
						simulate(r, c, rot);
					}
				}
			}
			
			//다 돌았는데도 0이면
			if(maxVal == 0) break;
			
			mapSet();
			map = rotate(fR, fC, fRot);
			
			finalCheck();
			
			System.out.print(ans + " ");
			
			//orgMap 변경
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					orgMap[i][j] = map[i][j];
				}
			}
		}
		
		//탐사 진행 과정에서 어떠한 방법을 사용하더라도 유물을 획득할 수 없었다면 모든 탐사는 그 즉시 종료됩니다. 
		//이 경우 얻을 수 있는 유물이 존재하지 않음으로, 
		//종료되는 턴에 아무 값도 출력하지 않음에 유의합니다.
//
//
//		첫 번째 줄에 탐사의 반복 횟수 K와 벽면에 적힌 유물 조각의 개수 M이 공백을 사이에 두고 주어집니다.
//
//		그 다음 5개의 줄에 걸쳐 유물의 각 행에 있는 유물 조각에 적혀 있는 숫자들이 공백을 사이에 두고 순서대로 주어집니다.
//
//		그 다음 줄에는 벽면에 적힌 M개의 유물 조각 번호가 공백을 사이에 두고 순서대로 주어집니다.
//
//		단, 초기에 주어지는 유적지에서는 탐사 진행 이전에 유물이 발견되지 않으며, 첫 번째 턴에서 탐사를 진행한 이후에는 항상 유물이 발견됨을 가정해도 좋습니다.
		
		
		
		
		
		
	}
	
	public static void finalCheck() {
		int finalCnt = 0;
		while(true) {
			finalCnt = 0;
			list = new ArrayList<Pos>();
			
			visited = new int[5][5];
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					if(visited[i][j] == 1) continue;
					
					int temp = bfs(i, j);
					
					
					
					if(temp >= 3) {
						list.add(new Pos(i, j));
						finalCnt += temp;
					}
					
				}
			}
			
			
			//3칸이상 붙어있는게 없으면 끝
			if(finalCnt == 0) break;
			
			
			//지우기
			visited = new int[5][5];
			for (int i = 0; i < list.size(); i++) {
				remove(i, map[list.get(i).r][list.get(i).c]);
			}
			
			//채우기
			fill();
			
			ans += finalCnt;
		}
	}
	
	public static void mapSet() {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				map[i][j] = orgMap[i][j];
			}
		}
	}
	
	public static void simulate(int r, int c, int rotCnt) {
		map = rotate(r, c, rotCnt);
		
		int cntCheck = 0; //
		
		
		int max = 0;
		
		//유물 1차 탐색
		visited = new int[5][5];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if(visited[i][j] == 1) continue;
				
				int temp = bfs(i, j);
				
				if(temp >= 3) {
					cntCheck += temp;
				}
				
			}
		}
		
		if(cntCheck > maxVal) {
			fRot = rotCnt;
			fR = r;
			fC = c;
			maxVal = cntCheck;
		}
			
		
	}
	
	public static void fill() {
		int idx = 0;
		
		for (int c = 0; c < 5; c++) {
			for (int r = 4; r >= 0; r--) {
				if(map[r][c] != -1) continue;
				
				map[r][c] = yumul[(yidx+M)%M];
				yidx++;
			}
		}
	}
	
	public static void remove(int idx, int num) {
		Queue<Pos> q = new LinkedList<Pos>();
		
		q.offer(list.get(idx));
		visited[list.get(idx).r][list.get(idx).c] = 1;
		//-1로 비우기
		map[list.get(idx).r][list.get(idx).c] = -1;
		
		while(!q.isEmpty()) {
			Pos cur = q.poll();
			
			for (int i = 0; i < dc.length; i++) {
				int nr = cur.r + dr[i];
				int nc = cur.c + dc[i];
				
				if(nr < 0 || nc < 0 || nr >= 5 || nc >= 5) continue;
				
				if(visited[nr][nc] == 1) continue;
				
				if(map[nr][nc] != num) continue;
				
				q.offer(new Pos(nr, nc));
				visited[nr][nc] = 1;
				map[nr][nc] = -1;
			}
		}
	}
	
	public static int bfs(int r, int c) {
		Queue<Pos> q = new LinkedList<Pos>();
		
		q.offer(new Pos(r, c));
		visited[r][c] = 1;
		
		int cnt = 1;
		
		while(!q.isEmpty()) {
			Pos cur = q.poll();
			
			for (int i = 0; i < dc.length; i++) {
				int nr = cur.r + dr[i];
				int nc = cur.c + dc[i];
				
				if(nr < 0 || nc < 0 || nr >= 5 || nc >= 5) continue;
				
				if(visited[nr][nc] == 1) continue;
				
				if(map[nr][nc] != map[cur.r][cur.c]) continue;
				
				q.offer(new Pos(nr, nc));
				visited[nr][nc] = 1;
				cnt++;
			}
		}
		
		return cnt;
	}
	
	
	public static int[][] rotate(int r, int c, int cnt) {
		int[][] tmap = new int[5][5];
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				tmap[i][j] = map[i][j];
			}
		}
		
		Queue<Integer> q = new LinkedList<Integer>();
		for (int t = 0; t < cnt; t++) {
			q.clear();
			for (int i = c-1; i <= c+1; i++) {
				for (int j = r+1; j >= r-1; j--) {
					q.offer(tmap[j][i]);
				}
			}
			
			for (int i = r-1; i <= r+1; i++) {
				for (int j = c-1; j <= c+1; j++) {
					tmap[i][j] = q.poll();
				}
			}
		}
		
		
		return tmap;
	}
	
	
	public static class Pos {
		int r;
		int c;
		
		public Pos(int r, int c) {
			this.r = r;
			this.c = c;
		}
	}
}