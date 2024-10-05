import java.util.*;
import java.io.*;

public class Main { // 빨간색은 1, 주황색은 2, 노랑색은 3, 초록색은 4, 파란색은 5
                    	// pid = -1 : 부모 노드
    static final int MAX_ID = 100005; // ID의 최대값입니다
    static final int MAX_DEPTH = 105; // 트리의 최대 깊이입니다
    static final int COLOR_MAX = 6;
	
    static int q = 0;
    
    static Node[] nodes = new Node[MAX_ID];
//    static boolean[] isRoot = new boolean[MAX_ID];
    static ArrayList<Integer> root = new ArrayList<>();
    static int result = 0;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        

        int max = Integer.parseInt(br.readLine());
        while(q++<max){
            st = new StringTokenizer(br.readLine());
            int m = Integer.parseInt(st.nextToken());
            if(m==100){
            	int mid = Integer.parseInt(st.nextToken());
            	int pid = Integer.parseInt(st.nextToken());
            	int color = Integer.parseInt(st.nextToken());
            	int max_depth = Integer.parseInt(st.nextToken());
            	
            	Node node = new Node();
            	node.mid = mid;
            	node.pid = pid;
            	node.color = color;
            	node.max_depth = max_depth;
            	node.lastUpdate = q;
            	node.childIds = new ArrayList<Integer>();
            	
            	if(pid == -1 || canMakeChildNode(pid)) {
                	if(pid == -1) {
                		root.add(mid);
                	}
                	else {
                		nodes[pid].childIds.add(mid);
                	}
            		nodes[mid] = node;
            	}
            }
            else if(m==200){
            	int mid = Integer.parseInt(st.nextToken());
            	int color = Integer.parseInt(st.nextToken());
            	changeColor(mid, color, q);
            }
            else if(m==300){
            	int mid = Integer.parseInt(st.nextToken());
            	sb.append(getColor(mid) + "\n");
            }
            else if(m==400){
            	for(Integer mid : root) {
            		getScore(nodes[mid], new boolean[COLOR_MAX]);
            	}
            	sb.append(result + "\n");
            	result = 0;
            }
        }

        System.out.print(sb);
    }
    
    
    private static boolean canMakeChildNode(int pid) {
    	int cnt = 1;
    	int newpid = pid;
		while(true) {
			if(nodes[newpid].max_depth <= cnt) {
				return false;
			}
			if(nodes[newpid].pid == -1)
				break;
			newpid = nodes[newpid].pid;
			cnt++;
		}
		return true;
	}


	public static void changeColor(int mid , int color, int q) {
    	nodes[mid].color = color;
    	nodes[mid].lastUpdate = q;
    }
    
    public static int getColor(int mid) {
    	int color = nodes[mid].color;
    	int pid = nodes[mid].pid;
    	int lastUpdated = nodes[mid].lastUpdate;
    	while(true) {
    		if(pid == -1)
    			break;
    		if(nodes[pid].lastUpdate > lastUpdated) {
    			lastUpdated = nodes[pid].lastUpdate;
    			color = nodes[pid].color;
    		}
    		pid = nodes[pid].pid;
    	}
    	return color;
    }
    
    public static boolean[] getScore(Node node, boolean color[]) {
    	boolean[] newcolor = new boolean[COLOR_MAX];
    	for(Integer child : node.childIds) {
    		boolean[] temp = getScore(nodes[child], color);
    		
        	for(int i = 1; i <= 5; i++) {
    			if(temp[i])
    				newcolor[i] = true;
    		}
    	}
    	newcolor[getColor(node.mid)] = true;
    	int cnt = 0;
    	for(int i = 1; i <= 5; i++) {
			if(newcolor[i])
				cnt++;
		}
		result += cnt*cnt;
    	return newcolor;
    }
    
    static class Node{
        int mid;
        int pid;
        int lastUpdate;
        int color;
        int max_depth;
        ArrayList<Integer> childIds = new ArrayList<>(); 
    }
}