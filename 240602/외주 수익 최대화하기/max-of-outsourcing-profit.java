import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		
		int n = Integer.parseInt(br.readLine());
		
		int[][] arr = new int[n][2];
		int[] dp = new int[n+1];
		
		for (int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine());
			arr[i][0] = Integer.parseInt(st.nextToken());
			arr[i][1] = Integer.parseInt(st.nextToken());
		}
		
		for (int i = n-1; i >= 0; i--) {
			if(i + arr[i][0] <= n) {
				dp[i] = Math.max(dp[i+arr[i][0]] + arr[i][1], dp[i+1]);
			} else {
				dp[i] = dp[i+1];
			}
		}
		
		
		System.out.println(dp[0]);
		
	}
}