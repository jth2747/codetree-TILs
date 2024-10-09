import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int[] dr = {-1, 0, 1, 0}; //상우하좌 (산타용)
	static int[] dc = {0, 1, 0, -1};
	static int[] dr8 = {-1, -1, 0, 1, 1, 1, 0, -1};
	static int[] dc8 = {0, 1, 1, 1, 0, -1, -1, -1};
	static int N, M, P, C, D, round;
	static int rdr, rdc;
	static int[][] map;
	static int[] isFaint, out, score;
	static Santa[] sanList;
	public static void main(String[] args) throws IOException {
		//System.setIn(new FileInputStream("src/input.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken()); //N*N
		M = Integer.parseInt(st.nextToken()); //게임 턴수
		P = Integer.parseInt(st.nextToken()); //산타 수
		C = Integer.parseInt(st.nextToken()); //루돌프의 힘
		D = Integer.parseInt(st.nextToken()); //산타의 힘
		
		map = new int[N][N];
		
		st = new StringTokenizer(br.readLine());
		
		//루돌프 세팅
		rdr = Integer.parseInt(st.nextToken()) - 1;
		rdc = Integer.parseInt(st.nextToken()) - 1;
		
		sanList = new Santa[P+1];
		for (int i = 0; i < P; i++) {
			st = new StringTokenizer(br.readLine());
			int num = Integer.parseInt(st.nextToken());
			int r = Integer.parseInt(st.nextToken()) - 1;
			int c = Integer.parseInt(st.nextToken()) - 1;
			
			sanList[num] = new Santa(r, c);
		}
		isFaint = new int[P+1]; // 0 0 0 0 0 ..
		out = new int[P+1];
		score = new int[P+1];
		round = 0;
		
		for (int i = 0; i < M; i++) {
			drawMap();
			// 현재 턴 값을 저장
			round = i;
			
			//1. 루돌프움직임
			rudolfMove();
			
			//3. 산타움직임
			for (int j = 1; j <= P; j++) {
				if(i < isFaint[j]) continue;
				if(out[j] == 1) continue;
				
				santaMove(j);
			}
			
			boolean isEnd = true;
			for (int j = 1; j <= P; j++) {
				if(out[j]==0) isEnd = false;
			}
			
			if(isEnd) break;
			
			
			for (int j = 1; j <= P; j++) {
				if(out[j]==0) score[j]++;
			}
			/*
			System.out.println("round: " + round + "-----------------------------------------------------------------------------");
			System.out.print("out: => ");
			for (int j = 1; j <= P; j++) {
				System.out.printf("%3d", out[j]);
				System.out.printf(" ");
			}
			System.out.println();
			System.out.print("scr: => ");
			for (int j = 1; j <= P; j++) {
				System.out.printf("%3d", score[j]);
				System.out.printf(" ");
			}
			System.out.println();
			*/
		}
		
		for (int j = 1; j <= P; j++) {
			System.out.print(score[j] + " ");
		}
	}
	
	public static void drawMap() {
		for (int i = 1; i <= P; i++) {
			if(out[i] == 1) continue;
			Santa cur = sanList[i];
			map[cur.r][cur.c] = i;
		}
		
		//map[rdr][rdc] = 99;
	}
	public static void santaMove(int sNum) {
		//4. 산타 움직일 방향 선택
		int dir = findDir(sNum);
		
		if(dir == -1) return;
		
		int nr = sanList[sNum].r + dr[dir];
		int nc = sanList[sNum].c + dc[dir];
		
		map[sanList[sNum].r][sanList[sNum].c] = 0;
		map[nr][nc] = sNum;
		
		sanList[sNum].r = nr;
		sanList[sNum].c = nc;
		

		// if 해당 칸에 루돌프가 있다면
		// 5. 충돌
		if(nr == rdr && nc == rdc) {
			crash(false, dir, nr, nc);			
		}
	}
	
	public static int findDir(int sNum) {
		int dir = -1;
		int curr = sanList[sNum].r;
		int curc = sanList[sNum].c;
		int max = Integer.MIN_VALUE;
		
		for (int i = 0; i < dc.length; i++) {
			int nr = curr + dr[i];
			int nc = curc + dc[i];
			
			if(nr < 0 || nc < 0 || nr >= N || nc >= N) continue;
			
			//다른산타가 있으면
			if(map[nr][nc] != 0) continue;
			
			//기존 루돌프와의 거리
			int orgDir = (curr-rdr)*(curr-rdr) + (curc-rdc)*(curc-rdc);
			//움직일 위치에서의 루돌프와의 거리
			int nexDir = (nr-rdr)*(nr-rdr) + (nc-rdc)*(nc-rdc);
			
			if(orgDir < nexDir) continue;
			
			int diff = orgDir - nexDir;
			
			//같음은 체크하지 않음
			if(diff > max) {
				max = diff;
				dir = i;
			}
		}
		
		return dir;
	}
	
	public static void rudolfMove() {
		//2. 가까운 산타선택
		int dir = findSanta();
		
		rdr += dr8[dir];
		rdc += dc8[dir];
		
		//if 해당칸에 산타가 있다면
				//5. 충돌
		if(map[rdr][rdc] != 0) {
			crash(true, dir, rdr, rdc);
		}
	}
	
	public static int findSanta() {
		int min = Integer.MAX_VALUE;
		int sanNum = 0;
		
		int sr = 0;
		int sc = 0;
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				// 아무것도 없으면 패스
				if(map[i][j] == 0) continue;
				
				// 산타가 있다, 거리 계산
				sr = sanList[map[i][j]].r;
				sc = sanList[map[i][j]].c;
				
				int dis = (rdr-sr)*(rdr-sr) + (rdc-sc)*(rdc-sc);
				
				// 같으면 우선순위에 따라 최신화해준다
				if(dis <= min) {
					min = dis;
					sanNum = map[i][j];
				}
			}
		}
		
		sr = sanList[sanNum].r;
		sc = sanList[sanNum].c;
		
		if(sr < rdr && sc == rdc) return 0;
		else if(sr < rdr && sc > rdc) return 1;
		else if(sr == rdr && sc > rdc) return 2;
		else if(sr > rdr && sc > rdc) return 3;
		else if(sr > rdr && sc == rdc) return 4;
		else if(sr > rdr && sc < rdc) return 5;
		else if(sr == rdr && sc < rdc) return 6;
		else return 7;
	}
	
	public static void crash(boolean isRud, int dir, int r, int c) {
		int exScore = 0;
		//int exDis = 0;
		int exDir = 0;
		
		//8방향으로 맞추기 위함
		if(!isRud) dir = dir * 2;
		
		// 여기서 dir 은 8방향 기준임
		if(isRud) {
			exScore = C;
			exDir = dir;
		} else {
			exScore = D;
			exDir = (dir+4) % 8;
		}
		
		int sNum = map[r][c];
		
		//누가 충돌했든 일단 기절처리
		isFaint[sNum] = round + 2;
		
		//점수 처리
		score[sNum] += exScore;
		
		// 산타 밀려날 위치
		int nr = sanList[sNum].r + (dr8[exDir] * exScore);
		int nc = sanList[sNum].c + (dc8[exDir] * exScore);
		
		// 암튼 일단 튕겨남
		map[r][c] = 0;
		
		if(nr < 0 || nc < 0 || nr >= N || nc >= N) {
			// out
			out[sNum] = 1;
			return;
		}
		
		// 일단 이동함
		// 이동하지마 
		sanList[sNum].r = nr;
		sanList[sNum].c = nc;
		
		// if 산타가 있으면 상호작용
		// 6. 상호작용
		if(map[nr][nc] != 0) {
			interact(map[nr][nc], exScore, exDir);
		} else {
		}
		map[nr][nc] = sNum;
		
	}
	
	public static void interact(int curNum, int val, int dir) {
		int nextNum = 0;
		while(true) {
			int curr = sanList[curNum].r;
			int curc = sanList[curNum].c;
			
			// 1칸 이동할 위치
			int nr = sanList[curNum].r + dr8[dir];
			int nc = sanList[curNum].c + dc8[dir];
			
			// 게임판 바깥
			if(nr < 0 || nc < 0 || nr >= N || nc >= N) {
				out[curNum] = 1;
				break;
			}
			
			sanList[curNum].r = nr;
			sanList[curNum].c = nc;
			
			if(map[nr][nc] == 0) {
				//map[curr][curc] = 0;
				map[nr][nc] = curNum;
				break;
			} else {
				int temp = curNum;
				curNum = map[nr][nc];
				
				map[nr][nc] = 0;
				map[nr][nc] = temp;
			}
			
			// 반복 계속 돌기위한 산타넘버 체인지
			//curNum = nextNum;
		}
	}
	
	public static class Santa {
		int r;
		int c;
		
		public Santa(int r, int c) {
			this.r = r;
			this.c = c;
		}
	}
}