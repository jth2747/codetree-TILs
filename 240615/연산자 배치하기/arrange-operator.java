import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int n;
	static int[] arr, op;
	static int MAX = Integer.MIN_VALUE;
	static int MIN = Integer.MAX_VALUE;
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		
		
		//변수
		n = Integer.parseInt(br.readLine());
		arr = new int[n];
		op = new int[3]; //+, -, *
		//세팅
		st = new StringTokenizer(br.readLine());
		for(int i=0; i<n; i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < 3; i++) {
			op[i] = Integer.parseInt(st.nextToken());
		}
		
		dfs(arr[0], 1);
		
		System.out.println(MIN + " " + MAX);
	}
	
	public static void dfs(int cal, int idx) {
		if(idx==arr.length) {
			MAX = Math.max(cal, MAX);
			MIN = Math.min(cal, MIN);
			
			return;
		}
		
		for (int i = 0; i < op.length; i++) {
			if(op[i] > 0) {
				op[i]--;
				//덧셈
				if(i==0) dfs(cal+arr[idx], idx+1);
				//뺄셈
				else if(i==1) dfs(cal-arr[idx], idx+1);
				//곱
				else dfs(cal*arr[idx], idx+1);
				
				op[i]++;
			}
		}
	}
}