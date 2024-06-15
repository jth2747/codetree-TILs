import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int[][] arr;
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		arr = new int[4][8];
		
		for (int i = 0; i < 4; i++) {
			String temp = br.readLine();
			for (int j = 0; j < 8; j++) {
				arr[i][j] = temp.charAt(j) - '0';
			}
		}
		
		int k = Integer.parseInt(br.readLine());
		int n = 0, d = 0;
		for (int i = 0; i < k; i++) {
			st = new StringTokenizer(br.readLine());
			
			n = Integer.parseInt(st.nextToken()) - 1;
			d = Integer.parseInt(st.nextToken());
			
			int[] dir = new int[4];
			boolean[] visited = new boolean[4];
			
			dir[n] = d;
			visited[n] = true;
			rotateCheck(n, d, dir, visited);

			for (int j = 0; j < dir.length; j++) {
				if(dir[j] == 0) continue;
				rotate(j, dir[j]);
			}
		}
		
		int ans = (1 * arr[0][0]) + (2 * arr[1][0]) + 
				(4 * arr[2][0]) + (8 * arr[3][0]);
		
		System.out.println(ans);
	}
	
	public static void rotateCheck(int n, int d, int[] dir, boolean[] visited) {
		if(n+1 >= 0 && n+1 < 4) {
			if(!visited[n+1] && arr[n][2] != arr[n+1][6]) {
				dir[n+1] = d * -1;
				visited[n+1] = true;
				rotateCheck(n+1, dir[n+1], dir, visited);
			}
			visited[n+1] = true;
		}
		if(n-1 >= 0 && n-1 < 4) {
			if(!visited[n-1] && arr[n][6] != arr[n-1][2]) {
				dir[n-1] = d * -1;
				visited[n-1] = true;
				rotateCheck(n-1, dir[n-1], dir, visited);				
			}
			visited[n-1] = true;
		}
		
		
	}
	
	public static void rotate(int num, int dir) {
		int[] temp = new int[8];
		
		for (int i = 0; i < 8; i++) {
			int nr = (i + dir + 8) % 8;
			
			temp[nr] = arr[num][i];
		}
		
		for (int i = 0; i < 8; i++) {
			arr[num][i] = temp[i];
		}
	}
}