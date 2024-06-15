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
			
			dir[n] = d;
			
			//왼쪽 조사
			for (int j = n-1; j >= 0 ; j--) {
				if(arr[j][2] != arr[j+1][6]) {
					dir[j] = dir[j+1] * -1;
				}
			}
			
			//오른쪽 조사
			for (int j = n+1; j < 4; j++) {
				if(arr[j][6] != arr[j-1][2]) {
					dir[j] = dir[j-1] * -1;
				}
			}
			

			for (int j = 0; j < dir.length; j++) {
				if(dir[j] == 0) continue;
				rotate(j, dir[j]);
			}
		}
		
		int ans = (1 * arr[0][0]) + (2 * arr[1][0]) + 
				(4 * arr[2][0]) + (8 * arr[3][0]);
		
		System.out.println(ans);
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