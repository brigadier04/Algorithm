import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        // [0] 세로로 둘 때 [1] 가로로 두되, 시작하는 순간일 때
        // [2] 가로로 두되, 끝나는 순간일 때
        int[][] dp = new int[n][3];
        dp[0][0] = dp[0][1] = 1; dp[0][2] = 0;
        for (int i = 1; i < n; i++) {
            dp[i][0] = (dp[i-1][0] + dp[i-1][2]) % 10007;
            dp[i][1] = dp[i][0];
            dp[i][2] = dp[i-1][1];
        }

        System.out.println((dp[n-1][0] + dp[n-1][2]) % 10007);
    }
}
