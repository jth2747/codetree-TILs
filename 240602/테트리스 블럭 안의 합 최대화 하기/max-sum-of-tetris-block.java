import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.StringTokenizer;

public class Main {
	static int n, m;
	static int[][] arr;
	static int max_sum;
	//19가지 모양
	static int[][][] dir = {
		{{0, 1, 1, 1}, {0, 0, 0, 0}}, //긴모양 2
		{{0, 0, 0, 0}, {0, 1, 1, 1}},
		{{0, 0, 1, 1}, {0, 1, 0, 1}}, //정사각형 1
		{{0, 1, 1, 0}, {0, 0, 0, 1}}, //기역 8
		{{0, 0, 0, 1}, {0, 1, 1, -2}},
		{{0, 0, 1, 1}, {0, 1, 0, 0}},
		{{0, 0, 0, -1}, {0, 1, 1, 0}},
		{{0, 0, -1, -1}, {0, 1, 0, 0}},
		{{0, 1, 0, 0}, {0, 0, 1, 1}},
		{{0, 0, 1, 1}, {0, 1, -1, 0}},
		{{0, 0, 0, 1}, {0, 1, 1, 0}},
		{{0, 1, 0, 1}, {0, 0, 1, 0}}, //사선 4개
		{{0, 0, -1, 0}, {0, 1, 0, 1}},
		{{0, 1, 0, 1}, {0, 0, -1, 0}},
		{{0, 0, 1, 0}, {0, 1, 0, 1}},
		{{0, 1, 1, -1}, {0, 0, 0, 1}}, //반십자가 4
		{{0, 0, 0, 1}, {0, 1, 1, -1}},
		{{0, 1, 1, -1}, {0, 0, 0, -1}},
		{{0, 1, 0, 0}, {0, -1, 1, 1}}
	};
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		arr = new int[n][m];
		
		max_sum = Integer.MIN_VALUE;
		for (int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < m; j++) {
				arr[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				for (int k = 0; k < dir.length; k++) {
					calc(i, j, k);
				}
			}
		}
		
		
		System.out.println(max_sum);
	}
	
	
	public static void calc(int r, int c, int seq) {
		int sum = 0;
		for (int i = 0; i < dir[seq][0].length; i++) {
			r += dir[seq][0][i];
			c += dir[seq][1][i];
			
			if(r >= n || c >= m || r < 0 || c < 0) {
				return;
			}
			
			sum += arr[r][c];
		}
		
		if(sum > max_sum) {
			max_sum = sum;
		}
	}
}