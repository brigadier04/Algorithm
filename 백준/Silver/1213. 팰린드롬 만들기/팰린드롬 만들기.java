import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        char[] name = st.nextToken().toString().toCharArray();
        int[] alpha = new int[26];
        int odd = 0;
        int numOfOdd = -1;
        int max = 0;

        char current;
        for(int i = 0; i < name.length; i++){
            current = name[i];
            alpha[current - 'A']++;

            if(max < current - 'A') max = current - 'A';

            if(alpha[current - 'A'] % 2 == 1) odd++;
            else odd--;
        }

        if(odd > 1){
            System.out.println("I'm Sorry Hansoo");
            return;
        }

        StringBuilder sb = new StringBuilder();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        for(int i = 0; i < 26; i++){
            if(i == max){
                // 가장 큰 알파벳
                for(int j = 0; j < alpha[i] / 2; j++){
                    sb.append((char) (i + 'A'));
                }
                if(alpha[i] % 2 == 1){
                    bw.write(sb.toString());
                    bw.write((char) (i + 'A'));
                    bw.write(sb.reverse().toString()+"\n");
                } else{
                    bw.write(sb.toString());
                    if(numOfOdd >= 0) bw.write((char) numOfOdd + 'A');
                    bw.write(sb.reverse().toString()+"\n");
                }
                bw.flush();
                bw.close();
                return;
            } else{
                // 가장 큰 알파벳 아님
                for(int j = 0; j < alpha[i] / 2; j++){
                    sb.append((char) (i + 'A'));
                }
                if(alpha[i] % 2 == 1){
                    numOfOdd = i;
                }
            }
        }
    }
}
