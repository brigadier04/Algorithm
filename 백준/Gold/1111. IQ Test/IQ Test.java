import java.util.*;
import java.io.*;

public class Main {
    static int size;
    static int[] nums;
    
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        size = Integer.parseInt(br.readLine());
        nums = new int[size];
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < size; i++) {
            nums[i] = Integer.parseInt(st.nextToken());
        }
        
        // ==========================================================================
        
        if(size == 1){
            // 숫자가 하나라면 어느 경우에서건 다음 수가 여러 개가 된다.
            System.out.println("A");
            return;
        } else if(size == 2){

            // size는 1~50
            // 각 숫자는 -100~100
            // 즉 곱하는 수는 -100~100까지

            // a가 어떠한 수든지 간에 b는 무조건 정수가 된다.
//            for (int i = -100; i <= 100; i++) {
//                if( checkPattern(i, nums[1] - nums[0] * i, 2) == 0 ){
//                    return;
//                }
//            }

            // testcase.ac에서의 반례 1개 발견
            // 3 /n -44 -43 100에서의 출력값: 20549 but 이건 곱하는 수가 -100~100이 아닌 듯
            // -44a + b = -43 -> b = -43 + 44a
            // -43a + b = 100 -> b = 100 + 43a -> a = 143
            // 만약 1 2 3 4 5라면
            // a + b = 2 , 2a + b = 3 -> b = 2 - a , b = 3 - 2a -> a = 1
            // 만약 -1 2라면
            // -a + b = 2 , b = 2 + a => 답이 무수히 많아진다
            // 만약 6 5 4 3 1 라면
            // 6a + b = 5 , 5a + b = 4 -> 5 - 6a = 4 - 5a -> 1 = a

            if(nums[0] == nums[1]){
                System.out.println(nums[0]);
            } else{
                System.out.println("A");
            }
            return;
        } else{
            if(nums[0] - nums[1] == 0){
                // 분모가 0이면 a가 나올 수가 없는 구조 <================================ think more
                for (int i = 2; i < size; i++) {
                    // 리스트에 있는 모든 수가 같다면 그 수를 출력함이 맞음
                    if(nums[i] != nums[0]){
                        System.out.println("B");
                        return;
                    }
                }

                System.out.println(nums[0]);
                return;
            }
            double check = ((double)nums[1] - nums[2]) / (nums[0] - nums[1]);
            if(check % 1 != 0) {
                // a가 정수가 아닌 경우 ... 따지는 게 맞는지 모르겠다.
                System.out.println("B");
                return;
            }

            checkPattern((int)check, nums[1] - nums[0] * (int)check, 3);
        }
    }

    public static void checkPattern(int a, int b, int index){
        if(index == size){
            System.out.println(nums[size-1] * a + b);
            return;
        }

        if( nums[index-1] * a + b == nums[index]){
            checkPattern(a, b, index+1);
        } else{
            System.out.println("B");
        }
    }
}
