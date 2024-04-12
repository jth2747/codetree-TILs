import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {
	static int[] dr = {0, 1, 0, -1}; //우하좌상
	static int[] dc = {1, 0, -1, 0};
	static ArrayList<Integer> monster;
	static int[][] map;
	static int[][] snail;
	static int n, m;
	static int ans;
	public static void main(String[] args) throws IOException {
		//System.setIn(new FileInputStream("input.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		//IO
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		
		map = new int[n][n];
		for (int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < n; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		//회전 맵부터 세팅
		snail = new int[n][n];
		snail(n/2, n/2, 2);
		
		//라운드만큼 진행
		for (int i = 0; i < m; i++) {
			st = new StringTokenizer(br.readLine());
			//1. 주어진 방향, 공격 칸수 만큼 공격한다.
			int pdir = Integer.parseInt(st.nextToken());
			int pstat = Integer.parseInt(st.nextToken());
			
			attack(pdir, pstat);
			
			//2. 빈공간을 채운다.
			monster = new ArrayList<>();
			
			//2-1. 중앙부터 시작해서 몬스터들을 리스트에 채운다, 0은 채우지 않음
			//그냥 순서대로 모든칸 보기위해 n*n
			int r = n/2;
			int c = n/2;
			for (int j = 0; j < n*n; j++) {
				//플레이어칸은 패스
				int nr = r + dr[snail[r][c]];
				int nc = c + dc[snail[r][c]];
				
				if(nr>=n || nc>=n || nr<0 || nc<0) {
					continue;
				}
				
				//몬스터면 넣는다.
				if(map[nr][nc] != 0) {
					monster.add(map[nr][nc]);
				}
				
				r = nr;
				c = nc;
			}
			
			//2-2. 반복체크해서 4번이상이면 제거
			//2-2-1. 연속된 숫자로 넣기 1, 1, 1, 2, 3, 4, 5, 1, 1, ....
			while(true) {
				boolean isDone = true;
				int[] check = new int[monster.size()];
				int val = monster.get(0);
				check[0] = 1;
				
				for (int j = 1; j < check.length; j++) {
					if(monster.get(j) == monster.get(j-1)) {
						check[j] = check[j-1] + 1;
					} else {
						check[j] = 1;
					}
					
					if(check[j]>=4) isDone = false;
				}
				if(isDone) break;
				remove(check);
			}
			
			int[] copy = new int[monster.size()+1];
			for (int j = 0; j < monster.size(); j++) {
				copy[j] = monster.get(j);
			}
			//초기화
			monster = new ArrayList<>();
			int idx = 0;
			int num;
			int totCnt = 1;
			while(true) {
				if(idx == copy.length - 1) break;
				
				//다음꺼랑 같으면
				if(copy[idx] == copy[idx+1]) {
					totCnt++;
				} else { //다르면
					//그대로 리스트에 넣는다
					monster.add(totCnt);
					monster.add(copy[idx]);
					totCnt = 1;
				}
				
				idx++;
				
			}
			
			
			mapSet(n/2, n/2);
		}
		
		
		System.out.println(ans);
		
	}
	
	public static void mapSet(int r, int c) {
		map = new int[n][n];
		
		for (int i = 0; i < monster.size(); i++) {
			int nr = r + dr[snail[r][c]];
			int nc = c + dc[snail[r][c]];
			
			if(nr >= n || nc >=n || nr < 0 || nc < 0) break;
			
			map[nr][nc] = monster.get(i);
			r = nr;
			c = nc;
		}
	}
	
	public static void remove(int[] check) {
		int idx = check.length - 1;
		while(true) {
			if(idx<0) break;
			
			if(check[idx] >= 4) {
				int endIdx = idx - check[idx];
				
				for (int i = idx; i > endIdx; i--) {
					ans += monster.get(i);
					monster.remove(i);
				}
				
			}
			idx -= check[idx];
		}
	}
	
	public static void attack(int dir, int stat) {
		//플레이어의 위치는 항상 중앙이다.
		int r = n/2;
		int c = n/2;
		
		//1-1. 공격력만큼 나아가면서, map 칸을 비워준다.
		for (int i = 0; i < stat; i++) {
			int nr = r + dr[dir];
			int nc = c + dc[dir];
			
			//floor(n/2) 라서 격자밖은 생각할 필요가 없다.
			
			//비워준다, 몬스터 숫자는 1,2,3
			ans += map[nr][nc];
			map[nr][nc] = 0;
			r = nr;
			c = nc;
		}
	}
	
	public static void snail(int r, int c, int dir) {
		//1,1, 2,2, 3,3, .... n,n,n+1
		for (int seq = 1; seq < n; seq++) {
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < seq; j++) {
					snail[r][c] = dir;
					
					int nr = r + dr[dir];
					int nc = c + dc[dir];
					
					r = nr;
					c = nc;
				}
				dir = (dir+4 - 1) % 4;
			}
		}
		
		for (int i = 0; i < n; i++) {
			snail[0][i] = dir;
		}
	}
}