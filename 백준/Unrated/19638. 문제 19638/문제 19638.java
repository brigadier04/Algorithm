import java.util.*;
import java.io.*;

public class Main{
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int numOfPeople = Integer.parseInt(st.nextToken());
        long heightOfCenti = Long.parseLong(st.nextToken());
        int maxOfHammer = Integer.parseInt(st.nextToken());

        long[] heightOfPopulations = new long[numOfPeople];
        
        // NO 출력 시 거인의 나라에서 키가 가장 큰 거인의 키를 출력하려면 우선순위 큐 사용
        // 최소 힙 (작은 값이 먼저 나옴)
        // PriorityQueue<Integer> minPq = new PriorityQueue<>();

        // 최대 힙 (큰 값이 먼저 나옴)
        // PriorityQueue<Integer> maxPq = new PriorityQueue<>(Collections.reverseOrder());

        for(int i = 0; i < numOfPeople; i++){
            add(heightOfPopulations, Long.parseLong(br.readLine()));
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        int useHammer = 0;
        while(useHammer < maxOfHammer){
            long taller = peek(heightOfPopulations);
            if(taller >= heightOfCenti){
                useHammer++;
                if(taller != 1) taller = taller / 2;
                add(heightOfPopulations, taller);
            } else{
                bw.write("YES"+"\n");
                bw.write(useHammer+"\n");
                bw.flush();
                bw.close();
                return;
            }
        }

        long tallerAfter = peek(heightOfPopulations);

        if(tallerAfter < heightOfCenti){
            bw.write("YES"+"\n");
            bw.write(useHammer+"\n");
        } else{
            bw.write("NO"+"\n");
            bw.write(tallerAfter+"\n");
        }
        bw.flush();
        bw.close();
    }

    public static int numOfQueue = 0;
    public static void add(long[] queue, long height){
        queue[numOfQueue] = height;
        if(++numOfQueue == 1) return;

        int index = numOfQueue - 1;
        while(queue[index] > queue[(index-1)/2]){
            long temp = queue[index];
            queue[index] = queue[(index-1)/2];
            queue[(index-1)/2] = temp;

            index = (index-1)/2;
            if(index == 0) break;
        }
    }

    public static long peek(long[] queue){
        if(numOfQueue == 0) return -1;
        
        long taller = queue[0];
        if(numOfQueue == 1){
            numOfQueue = 0;
            return taller;
        } 

        queue[0] = queue[numOfQueue-1];
        numOfQueue--;

        int index = 0;
        long temp;
        while(index * 2 + 1 < numOfQueue){
            // 좌측 자식노드가 존재할 경우
            long left = queue[index * 2 + 1];
            if(index * 2 + 2 < numOfQueue){
                // 우측 자식노드가 존재할 경우
                long right = queue[index * 2 + 2];
                if(left >= right && left > queue[index]){
                    temp = left;
                    queue[index * 2 + 1] = queue[index];
                    queue[index] = left;
                    index = index * 2 + 1;
                } else if(right > left && right > queue[index]){
                    temp = right;
                    queue[index * 2 + 2] = queue[index];
                    queue[index] = temp;
                    index = index * 2 + 2;
                } else break;
            } else{
                // 우측 자식노드가 없는 경우
                if(left > queue[index]){
                    temp = left;
                    queue[index * 2 + 1] = queue[index];
                    queue[index] = left;
                }
                break;
            }
        }

        return taller;
    }
}