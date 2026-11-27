import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        char[] record = br.readLine().toCharArray();
        String[] division = new String[2500];


        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        int numOfDuck = 0;
        int max = 0;
        for(int i = 0; i < record.length; i++){
            char current = record[i];
            char before;
            if(current == 'q') before = '\0';
            else if(current == 'u') before = 'q';
            else if(current == 'a') before = 'u';
            else if(current == 'c') before = 'a';
            else before = 'c';

            // bw.write("현재 "+(i+1)+"번째 문자는 "+current+"입니다.\n");
            if(max == 0){
                if(current == 'q'){
                    // bw.write("division 첫 개시했습니다.\n");
                    max++; numOfDuck++; division[0] = "q";
                } else{
                    bw.write(-1+"\n");
                    bw.flush();
                    bw.close();
                    return;
                }
            } else{
                // bw.write("division 첫 개시 이후입니다.\n");
                boolean origin = false;
                for(int j = 0; j < max; j++){
                    // bw.write((j+1)+"번째 오리의 현재 소리는 "+division[j]+", 길이는 "+division[j].length()+"\n");

                    if(division[j].length() == 0){
                        if(current == 'q'){
                            origin = true; numOfDuck++;
                            division[j] = "q"; 
                            break;
                        }
                    } else if(division[j].toCharArray()[division[j].length()-1] == before){
                        // bw.write("기존 문자열 뒤에 붙일 수 있습니다.\n");
                        origin = true;
                        if(current == 'k'){
                            numOfDuck--; division[j] = "";

                            // bw.write("꽥 다 참. 해당 배열을 비웁니다.\n크기는 "+division[j].length()+"\n\n\n");
                        } else{
                            division[j] = division[j] + current;
                        }
                        break;
                    }
                }
                if(!origin){
                    if(current == 'q'){
                        max++; numOfDuck++; division[max-1] = "q";

                        // bw.write("새로운 오리 등장\n\n\n현재 모든 오리의 소리는\n");
                        // for(int j = 0; j < max; j++){
                        //     bw.write(division[j]+"\n");
                        // }
                    } else{
                        bw.write(-1+"\n");
                        bw.flush();
                        bw.close();
                        return;
                    }
                }
            }
        }

        if(numOfDuck > 0) bw.write(-1+"\n");
        else bw.write(max+"\n");
        bw.flush();
        bw.close();
        return;
    }
}