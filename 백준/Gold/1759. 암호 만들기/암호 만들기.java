// test 8

import java.util.*;
import java.io.*;

public class Main {
    static int length;
    static int numOfAlp;
    static char[] alps;
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        length = Integer.parseInt(st.nextToken());
        numOfAlp = Integer.parseInt(st.nextToken());

        alps = new char[numOfAlp];
        // 단순히 아래같이 하면 공백까지 하나의 char로 다루게 됨
//        alps = br.readLine().toCharArray();
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < numOfAlp; i++) {
            alps[i] = st.nextToken().charAt(0);
        }
        Arrays.sort(alps);

        backTraking(new char[length], 0, 0, 0, 0);
    }

    public static void backTraking(char[] output, int index, int checking, int vowel, int cons){
        if(index == length){
            // 암호조합의 길이가 최대치인 경우에서 다시 따져야 한다

            if(vowel >= 1 && cons >= 2) {
                // 아래는 배열 형태로 출력됨 eg. [a, e, i, s]
//            System.out.println(Arrays.toString(output));
                System.out.println(new String(output));
            }
            return;
        }

        for(int i = checking; i < numOfAlp; i++){
            char c = alps[i];
            output[index] = c;
            if(c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u'){
                backTraking(output, index+1, i+1, vowel+1, cons);
            } else { backTraking(output, index+1, i+1, vowel, cons+1); }
        }
    }
}
