import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/*
 * 코드트리_나무박멸
 */
public class Main {
	static int[] dr = {-1, 0, 1, 0}; //상우하좌
	static int[] dc = {0, 1, 0, -1};
	static int[] sdr = {-1, 1, 1, -1}; //대각선 움직임
	static int[] sdc = {1, 1, -1, -1};
	static int n,m,k,killYear;
	static int maxKill, maxr, maxc;
	static int[][] map;
	static int[][] kill; //제초제
	static boolean[][] tree;
	static int ans;
	public static void main(String[] args) throws IOException {
		//System.setIn(new FileInputStream("input.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		st = new StringTokenizer(br.readLine());
		
		n = Integer.parseInt(st.nextToken()); //격자크기
		m = Integer.parseInt(st.nextToken()); //라운드 수
		k = Integer.parseInt(st.nextToken()); //대각선 체크 
		killYear = Integer.parseInt(st.nextToken()); //제초제 유지연도
		
		map = new int[n][n];
		kill = new int[n][n];
		tree = new boolean[n][n];
		for (int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < n; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				//나무인지 체크
				//계속 업데이트가 필요??????
				if(map[i][j] > 0) tree[i][j] = true;
			}
		}
		
		//연도만큼 m
		for (int year = 0; year < m; year++) {
			//나무 제초제 뿌릴때 최대값 저장용
			maxKill = -1;
			maxr = 0;
			maxc = 0;
			// 0. 제초제 초기화 
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if(kill[i][j] != 0) {
						kill[i][j]--;
					}
				}
			}
			
			// 1. 나무가 성장한다.
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					//나무면 성장
					if(map[i][j] > 0) {
						grow(i, j);
					}
				}
			}
			
			// 2. 나무가 번식한다.
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					//나무면 번식 가능한지 확인, 번식된 나무면 패스
					if(map[i][j] > 0 && tree[i][j]) {
						spread(i, j);
					}
				}
			}
			// 2-1. 나무 배열에 이제 나무 적용
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					//번식된 나무라 나무가 있는데, 나무 배열이 false면
					if(map[i][j] > 0 && !tree[i][j]) {
						tree[i][j] = true;
					}
				}
			}
			
			// 3. 제초제 뿌리기
			// 3-1. 제초제 뿌릴 칸 찾기
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					//빈칸은 0이라 굳이 체크하지 않음
					if(map[i][j] > 0) {
						killTree(i, j);
					}
				}
			}
			
			// 3-2. 제초제 뿌리기
			spreadKill(maxr, maxc);
			
			if(maxKill != -1) {
				ans += maxKill;
			}
		}
		
		System.out.println(ans);
	}
	
	public static void spreadKill(int r, int c) {
		//지금칸에 뿌리기
		kill[r][c] = killYear + 1;
		map[r][c] = 0;
		tree[r][c] = false;
		
		
		for (int d = 0; d < dc.length; d++) {
			int curr = r;
			int curc = c;
			for (int i = 0; i < k; i++) {
				int nr = curr + sdr[d];
				int nc = curc + sdc[d];
				
				if(nr >= n || nc >= n || nr < 0 || nc < 0) continue;
				
				//벽이거나 빈칸이면 거기까지만 뿌리고 break;
				if(map[nr][nc] == -1 || map[nr][nc] == 0) {
					kill[nr][nc] = killYear+1;
					break;
				}
				
				//제초제 뿌리기
				kill[nr][nc] = killYear+1;
				map[nr][nc] = 0;
				tree[nr][nc] = false;
				
				curr = nr;
				curc = nc;
			}
		}
	}
	
	public static void killTree(int r, int c) {
		int killCnt = map[r][c];
		
		for (int d = 0; d < dc.length; d++) {
			int curr = r;
			int curc = c;
			for (int i = 0; i < k; i++) {
				int nr = curr + sdr[d];
				int nc = curc + sdc[d];
				
				if(nr >= n || nc >= n || nr < 0 || nc < 0) continue;
				
				//벽이거나 빈칸이면 거기까지만 뿌린다
				//여기선 확인용이므로 뿌리거나 하는 등의 후처리는 나중에
				if(map[nr][nc] == -1 || map[nr][nc] == 0) break; 
				
				killCnt += map[nr][nc];
				
				curr = nr;
				curc = nc;
			}
		}
		
		if(killCnt > maxKill) {
			maxKill = killCnt;
			maxr = r;
			maxc = c;
		}
	}
	
	public static void spread(int r, int c) {
		int cnt = 0;
		
		for (int i = 0; i < dc.length; i++) {
			int nr = r + dr[i];
			int nc = c + dc[i];
			
			if(nr >= n || nc >= n || nr < 0 || nc < 0) continue;
			
			//제초제면 패스
			if(kill[nr][nc] > 0) continue;
			
			//그 칸이 원래 나무가 있는 칸이면 패스
			if(tree[nr][nc]) continue;
			
			//map값을 체크하지는 않음
			
			//벽이면 패스
			if(map[nr][nc] == -1) continue;
			
			cnt++;
		}
		
		//번식할 곳이 없음
		if(cnt==0) return;
		
		int treeCnt = (map[r][c] / cnt);
		for (int i = 0; i < dc.length; i++) {
			int nr = r + dr[i];
			int nc = c + dc[i];
			
			if(nr >= n || nc >= n || nr < 0 || nc < 0) continue;
			
			//제초제면 패스
			if(kill[nr][nc] > 0) continue;
			
			//그 칸이 원래 나무가 있는 칸이면 패스
			if(tree[nr][nc]) continue;
			
			//map값을 체크하지는 않음
			
			//벽이면 패스
			if(map[nr][nc] == -1) continue;
			
			//0이 아닐수도 있으니 +=
			map[nr][nc] += treeCnt;
		}
	}
	
	public static void grow(int r, int c) {
		int cnt = 0;
		
		for (int i = 0; i < dc.length; i++) {
			int nr = r + dr[i];
			int nc = c + dc[i];
			
			if(nr >= n || nc >= n || nr < 0 || nc < 0) continue;
			
			if(map[nr][nc] > 0) {
				cnt++;
			}
			
		}
		
		//사방에 나무 있는 만큼 성장
		map[r][c] += cnt;
	}
}