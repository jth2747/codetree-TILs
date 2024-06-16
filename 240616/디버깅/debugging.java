import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int N, M, H, res;
	static boolean flag;
	static int[][] lad;
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		st = new StringTokenizer(br.readLine(), " ");
		
		N = Integer.parseInt(st.nextToken()); //세로선
		M = Integer.parseInt(st.nextToken()); //놓여있는 가로선 수
		H = Integer.parseInt(st.nextToken()); //가로선
		
		lad = new int[H][N]; //사다리 배열
		
		int a = 0, b = 0;
		for (int i = 0; i < M; i++) { //사다리 세팅
			st = new StringTokenizer(br.readLine(), " ");
			a = Integer.parseInt(st.nextToken()) - 1;
			b = Integer.parseInt(st.nextToken()) - 1; //b번 세로선, b+1번 세로선
			lad[a][b] = 1;
			lad[a][b+1] = -1;
		}
		
		for (int i = 0; i <= 3; i++) {
			dfs(i, 0 ,0); //i = > ladder cnt
			if(flag) {
				res = i;
				break;
			}
		}
		
		if(flag) System.out.println(res);
		else System.out.println(-1);
		
	}

	private static void dfs(int lcnt, int idx, int cnt) {
		if(flag) return; //찾았으니 족족 리턴
		if(cnt == lcnt) {
			if(laddercheck()) { //i번째 사다리가 i번째에 도착하는가?
				flag = true;
			}
			return;
		}
		
		for (int i = idx; i < H; i++) {
			for (int j = 0; j < N - 1; j++) {
				if(lad[i][j]==0 && lad[i][j+1]==0) {
					lad[i][j] = 1;
					lad[i][j+1] = -1;
					dfs(lcnt, i, cnt + 1);
					lad[i][j] = 0;
					lad[i][j+1] = 0;
				}
			}
		}
	}

	private static boolean laddercheck() {
		for (int i = 0; i < N; i++) {
			int rpos = 0, cpos = i; //0, i부터 시작해서 사다리타자
			for (int j = 0; j < H; j++) {
				if(lad[rpos][cpos] == 1) { 
					rpos++; //1이면 우한칸 아래한칸이니까
					cpos++;
				} else if(lad[rpos][cpos] == -1) {
					rpos++;
					cpos--;
				} else { //0이면 그대로 밑으로
					rpos++;
				}
			}
			if(cpos != i) return false;
		}
		return true;
	}
}