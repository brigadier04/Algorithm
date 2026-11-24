import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int col, row;
        StringTokenizer st = new StringTokenizer(br.readLine());
        col = Integer.parseInt(st.nextToken());
        row = Integer.parseInt(st.nextToken());

        int[][] map = new int[row][col];
        int[][] visit = new int[row][col];

        for(int i = 0; i < row; i++){
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < col; j++){
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        Queue<int[]> q = new LinkedList<>();  
        q.add(new int[]{0, 0});
        visit[0][0] = 1;
        while(!q.isEmpty()){
            int n[] = q.poll();
            int r = n[0], c = n[1];
            if((c+1) < col && map[r][c+1] == 1 && visit[r][c+1] == 0){
                visit[r][c+1] = 1;
                q.add(new int[]{r, c+1});
            }
            if((r+1) < row && map[r+1][c] == 1 && visit[r+1][c] == 0){
                visit[r+1][c] = 1;
                q.add(new int[]{r+1, c});
            }
        }

        if(visit[row-1][col-1] == 1) System.out.println("Yes");
        else System.out.println("No");
    }
}