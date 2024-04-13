import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {
	static int[] dr = {0, 1, 0, -1}; //우하좌상
	static int[] dc = {1, 0, -1, 0};
	static int n,m,k;
	static int[][] map;
	static int[][] teamNum;
	static ArrayList<Team>[] team;
	static int ans;
	public static void main(String[] args) throws IOException {
		//System.setIn(new FileInputStream("input.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		st = new StringTokenizer(br.readLine());
		
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		
		//어레이리스트 초기화
		team = new ArrayList[m];
		for (int i = 0; i < m; i++) {
			team[i] = new ArrayList<Team>();
		}
		
		map = new int[n][n];
		for (int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < n; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		//팀을 어레이리스트에 넣기
		int tNum = 0;
		boolean[][] visited = new boolean[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if(map[i][j] == 1) {
					visited[i][j] = true;
					teamSet(visited, tNum, 1, i, j);
					tNum++;
				}
			}
		}
		
		//각칸의 팀넘버 넣기
		teamNum = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				teamNum[i][j] = -1;
			}
		}
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < team[i].size(); j++) {
				teamNum[team[i].get(j).r][team[i].get(j).c] = i;
			}
		}
		
		ans = 0;
		for (int i = 0; i < k; i++) {
			//각 팀의 운동
			for (int j = 0; j < m; j++) {
				move(j);				
			}
			//공던지기 방향 체크
			int div = (i / n) % 4;
			int rest = (i % n);
			
			int ctNum = throwBall(div, rest);
			
			if(ctNum != -1) {
				changeTeam(ctNum);				
			}
		}
		
		
		
		
		System.out.println(ans);
	}
	
	public static void changeTeam(int tNum) {
		int size = team[tNum].size();
		
		Team[] temp = new Team[size];
		
		int idx = 0;
		for (int i = size-1; i >= 0; i--) {
			if(team[tNum].get(i).num == 3) {
				team[tNum].get(i).num = 1;
			} else if(team[tNum].get(i).num == 1) {
				team[tNum].get(i).num = 3;
			}
			
			temp[idx++] = team[tNum].get(i);
			map[team[tNum].get(i).r][team[tNum].get(i).c] = 4;
			//방향만 바뀌는거라서 teamNum 배열 변경은 필요없다
			team[tNum].remove(i);
		}
		
		for (int i = 0; i < size; i++) {
			team[tNum].add(temp[i]);
			map[team[tNum].get(i).r][team[tNum].get(i).c] = team[tNum].get(i).num;
		}
	}
	
	public static int throwBall(int dir, int rest) {
		if(dir==0) {
			for (int i = 0; i < n; i++) {
				//사람 만날때까지
				if(map[rest][i] == 4 || map[rest][i] == 0) continue;
				
				getPoint(teamNum[rest][i], rest, i);
				
				return teamNum[rest][i];
			}
		}
		else if(dir==1) {
			for (int i = n-1; i >= 0; i--) {
				if(map[i][rest] == 4 || map[i][rest] == 0) continue;
				
				getPoint(teamNum[i][rest], i, rest);
				
				return teamNum[i][rest];
			}
		}
		else if(dir==2) {
			for (int i = n-1; i >= 0; i--) {
				if(map[(n-1)-rest][i] == 4 || map[(n-1)-rest][i] == 0) continue;
				
				getPoint(teamNum[(n-1)-rest][i], (n-1)-rest, i);
				
				return teamNum[(n-1)-rest][i];
			}
		}
		else {
			for (int i = 0; i < n; i++) {
				if(map[i][(n-1)-rest] == 4 || map[i][(n-1)-rest] == 0) continue;
				
				getPoint(teamNum[i][(n-1)-rest], i, (n-1)-rest);
				
				return teamNum[i][(n-1)-rest];
			}
		}
		return -1;
	}
	
	public static void getPoint(int tNum, int r, int c) {
		for (int i = 0; i < team[tNum].size(); i++) {
			if(team[tNum].get(i).r == r && team[tNum].get(i).c == c) {
				ans += ((i+1) * (i+1));
			}
		}
	}
	
	public static void move(int tNum) {
		int size = team[tNum].size();
		int fr = team[tNum].get(0).r;
		int fc = team[tNum].get(0).c;
		int nr = fr;
		int nc = fc;
		//1번 위치
		for (int i = 0; i < dc.length; i++) {
			nr = team[tNum].get(0).r + dr[i];
			nc = team[tNum].get(0).c + dc[i];
			
			if(nr >= n || nc >= n || nr < 0 || nc < 0) continue;
			
			if(map[nr][nc] < 3) continue;
			
			
			//map 에서 원래 칸부터 4로 바꿔주기
			map[team[tNum].get(0).r][team[tNum].get(0).c] = 4;
			teamNum[team[tNum].get(0).r][team[tNum].get(0).c] = -1;
			//리스트 값도 이동할 칸으로 바꿔주기
			team[tNum].get(0).r = nr;
			team[tNum].get(0).c = nc;
			//map 도 이동한 칸으로 적용해주기
			map[nr][nc] = 1;
			teamNum[nr][nc] = tNum;
			break;
		}
		/*
		if(team[tNum].get(0).r==fr && team[tNum].get(0).c==fc) {
			//1번이 갈곳이 없었음
			return;
		}
		*/
		//1번 뒤따라가기
		for (int i = 1; i < size; i++) {
			int tr = team[tNum].get(i).r;
			int tc = team[tNum].get(i).c;
			
			if(map[tr][tc] != 1) {
				map[tr][tc] = 4;				
				teamNum[tr][tc] = -1;
			}
			
			team[tNum].get(i).r = fr;
			team[tNum].get(i).c = fc;
			
			map[team[tNum].get(i).r][team[tNum].get(i).c] = team[tNum].get(i).num;
			teamNum[team[tNum].get(i).r][team[tNum].get(i).c] = tNum;
			
			fr = tr;
			fc = tc;
		}
	}
	
	public static void teamSet(boolean[][] visited, int tNum, int cnt, int r, int c) {
		team[tNum].add(new Team(map[r][c], cnt, r, c));
		
		if(map[r][c] == 3) return;
		
		for (int d = 0; d < dc.length; d++) {
			int nr = r + dr[d];
			int nc = c + dc[d];
			
			if(nr >= n || nc >= n || nr < 0 || nc < 0) continue;
			
			//멤버 1,2,3 이 아니면 그냥 패스
			if(map[nr][nc]==4 || map[nr][nc] == 0) {
				continue;
			}
			
			//나보다 작으면
			if(map[nr][nc] < map[r][c]) {
				continue;
			}
			
			//차이가 1보다 크면, 1 -> 3 이렇게 가는 경우
			if(map[nr][nc] - map[r][c] > 1) {
				continue;
			}
			
			if(visited[nr][nc]) {
				continue;
			}
			
			visited[nr][nc] = true;
			cnt++;
			teamSet(visited, tNum, cnt, nr, nc);
			visited[nr][nc] = false;
		}
	}
	
	//팀 넘버는 어레이리스트 배열의 숫자
	public static class Team {
		int num; //얘는 숫자값 1, 3 체크용
		int seq; //얘는 순번
		int r;
		int c;
		
		public Team(int num, int seq, int r, int c) {
			this.num = num;
			this.seq = seq;
			this.r = r;
			this.c = c;
		}
	}
}