import java.util.*;
import java.io.*;

public class Main { // 루돌프 : 가까운 산타, r 큰 산타, c 큰 산타. 8방향 중 가까워지는 곳으로 이동
							// 루돌프가 움직여서 충돌이 일어난 경우, 해당 산타는 C만큼의 점수를 얻게 됩니다. 이와 동시에 산타는 루돌프가 이동해온 방향으로 C 칸 만큼 밀려나게 됩니다.
							// 충돌 시 산타가 움직인 경우는 D 만큰 점수 얻고, 산타 이동방향 반대로 D만큼 튕겨남. 맵 밖이면 아웃 
							// 밀려났을 때 산타가 있으면 그 산타가 1칸 뒤로 빌려남. 계속 밀려나서 밖으로 나가면 아웃
							// 충돌 후 해당 산타 1턴 기절.
						// 산타 : 루돌프에게 가까워지게 이동, 가까워지지 않으면 이동X, 4방향 증 가장 가까워지는 곳으로 이동, 같으면 상-우-하-좌
						// 매턴 이후 산타들에게 1점씩 추가
	
	static int dx[] = {-1, 0, 1, 0};
	static int dy[] = {0, 1, 0, -1};
	static int rx[] = {-1, -1, 0, 1, 1, 1, 0, -1}; // 상 우 하 좌, 우측 하탄, 좌측 하단, 우측 상단, 좌측 상단.
	static int ry[] = {0, 1, 1, 1, 0, -1, -1, -1}; // 상 , 우측 상단,우,우측 하단, 하	,좌측 하단, 좌, 좌측 상단.
	static Node map[][];
	static int n, c, d, p, m;
	static Node resultSanta[]; 
	static Node ru;
	
	static class Node{
		int santaNum;
		int x;
		int y;
		int score;
		int sturn;
		int distance;
		boolean isOut;
		Node(int x, int y){
			this.santaNum = -1;
			this.x = x;
			this.y = y;
			this.score = 0;
			this.sturn = 0;
			this.distance = 0;
		}
		
		Node(int santaNum, int x, int y, int distance){
			this.santaNum = santaNum;
			this.x = x;
			this.y = y;
			this.score = 0;
			this.sturn = 0;
			this.distance = distance;
			this.isOut = false;
		}
	}
	
	public static void main(String[] args) throws IOException {
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    StringTokenizer st;
	    st = new StringTokenizer(br.readLine());
	    n = Integer.parseInt(st.nextToken());
	    m = Integer.parseInt(st.nextToken());
	    p = Integer.parseInt(st.nextToken());
	    c = Integer.parseInt(st.nextToken());
	    d = Integer.parseInt(st.nextToken());
	    
	    map = new Node[n+1][n+1];
	    resultSanta = new Node [p+1];
	    
	    st = new StringTokenizer(br.readLine());
	    int rx = Integer.parseInt(st.nextToken());
	    int ry = Integer.parseInt(st.nextToken());
	    ru = new Node(rx, ry);
	    
	    for(int i = 1 ; i <= p; i++) {
	    	st = new StringTokenizer(br.readLine());
	    	int sn = Integer.parseInt(st.nextToken());
	    	int x = Integer.parseInt(st.nextToken());
	    	int y = Integer.parseInt(st.nextToken());
	    	int distance = (rx-x)*(rx-x) + (ry-y)*(ry-y);
	    	resultSanta[sn] = new Node(sn, x, y, distance);
	    	map[x][y] = resultSanta[sn];
	    }
	    
    	
    	
	    int turn = 0;
	    while(turn<m) {
	    	turn++;
		    for(int i = 1 ; i <= p; i++) {
		    	resultSanta[i].distance = (resultSanta[i].x - ru.x)*(resultSanta[i].x - ru.x) + (resultSanta[i].y - ru.y)*(resultSanta[i].y - ru.y);
		    }
	    	
	    	moveRu(turn); // 루돌프 이동
	    	
	    	boolean isDone = true;  // 끝났는지 체크
	    	for(int i = 1; i <= p; i++) {
	    		if(!resultSanta[i].isOut && resultSanta[i].sturn < turn) {
	    			moveSan(i, turn);
	    			isDone = false;
	    		}
	    		if(resultSanta[i].sturn < turn) {
	    			isDone = false;
	    			resultSanta[i].distance = (resultSanta[i].x - ru.x)*(resultSanta[i].x - ru.x) + (resultSanta[i].y - ru.y)*(resultSanta[i].y - ru.y);
	    		}
	    	}
	    	
	    	if(isDone) { // 끝남
	    		break;
	    	}
	    	for(int i = 1; i <= p; i++) {
	    		if(!resultSanta[i].isOut) {
	    			resultSanta[i].score++;
	    		}
	    	}
	    }
	    
	    for(int i = 1 ; i <= p; i++) {
	    	System.out.print(resultSanta[i].score + " ");
	    }
	}
	
	public static void moveSan(int sn, int turn) {
		int dis = (resultSanta[sn].x - ru.x)*(resultSanta[sn].x - ru.x) + (resultSanta[sn].y - ru.y)*(resultSanta[sn].y - ru.y);
		
		int mindis =  Integer.MAX_VALUE;
		int move = -1;
		int x = 0;
		int y = 0;
		int distance = 0;
		for(int i = 0; i < 4; i++) {
			int nx = resultSanta[sn].x + dx[i];
			int ny = resultSanta[sn].y + dy[i];
			if(nx > 0 && ny > 0 && nx <= n && ny <= n) {
				if(map[nx][ny] == null) {
					int newdis = (nx - ru.x)*(nx - ru.x) + (ny - ru.y)*(ny - ru.y);
					if(newdis < dis && mindis > newdis) {
						mindis = newdis;
						x = nx;
						y = ny;
						distance = newdis;
						move = i;
					}
				}
			}
		}
		
		if(x != 0 && y != 0) {
			map[resultSanta[sn].x][resultSanta[sn].y] = null;
			resultSanta[sn].x = x;
			resultSanta[sn].y = y;
			resultSanta[sn].distance = distance;
			
			if(x == ru.x && y == ru.y) {
				meetRu(turn, sn, move);
			}
			else {
				map[x][y] = resultSanta[sn];
			}
		}
	}
	
	public static void meetRu(int turn, int santaNum, int move) { // 산타가 루돌프 만남
		resultSanta[santaNum].score += d;
		resultSanta[santaNum].sturn = turn + 1; 
		move = (move+2)%4;
		
		int x = resultSanta[santaNum].x;
		int y = resultSanta[santaNum].y;
		
		for(int i = 0 ; i < d; i++) { // 도착할 위치 잡기
			x += dx[move];
			y += dy[move];
		}
		
		if(x > 0 && y > 0 && x <= n && y <= n) {
			if(map[x][y]!=null) {  // 누군가 있으면
				santasaBack(x,y,move);
			}
			map[resultSanta[santaNum].x][resultSanta[santaNum].y] = null;
			resultSanta[santaNum].x = x;
			resultSanta[santaNum].y = y;
			resultSanta[santaNum].distance = (resultSanta[santaNum].x - ru.x)*(resultSanta[santaNum].x - ru.x) + (resultSanta[santaNum].y - ru.y)*(resultSanta[santaNum].y - ru.y);
			map[x][y] = resultSanta[santaNum];
		}
		else {
			map[resultSanta[santaNum].x][resultSanta[santaNum].y] = null;
			resultSanta[santaNum].isOut = true;
			return;
		}
	}
	
	public static void meetSan(int turn, int santaNum, int move) { // 루돌프가 산타 만남
		resultSanta[santaNum].score += c;
		resultSanta[santaNum].sturn = turn + 1; 
//		move = (move + 4)%8;
		
		int x = resultSanta[santaNum].x;
		int y = resultSanta[santaNum].y;
		
		for(int i = 0 ; i < c; i++) { // 도착할 위치 잡기
			x = x + rx[move];
			y = y + ry[move];
		}
		
		if(x > 0 && y > 0 && x <= n && y <= n) {
			if(map[x][y]!=null) {  // 누군가 있으면
				santaRuBack(x,y,move);
			}
			map[resultSanta[santaNum].x][resultSanta[santaNum].y] = null;
			resultSanta[santaNum].x = x;
			resultSanta[santaNum].y = y;
			resultSanta[santaNum].distance = (resultSanta[santaNum].x - ru.x)*(resultSanta[santaNum].x - ru.x) + (resultSanta[santaNum].y - ru.y)*(resultSanta[santaNum].y - ru.y);
			map[x][y] = resultSanta[santaNum];
		}
		else {
			map[resultSanta[santaNum].x][resultSanta[santaNum].y] = null;
			resultSanta[santaNum].isOut = true;
			return;
		}
	}
	
	public static void santaRuBack(int x, int y, int move) {  // 루돌프 충돌 시 산타 밀려나기.
		int sn = map[x][y].santaNum;
		
		x = x + rx[move];
		y = y + ry[move];
		
		if(x > 0 && y > 0 && x <= n && y <= n) {
			if(map[x][y]!=null) {  // 누군가 있으면
				santaRuBack(x, y, move);
			}

			map[resultSanta[sn].x][resultSanta[sn].y] = null;
			resultSanta[sn].x = x;
			resultSanta[sn].y = y;
			resultSanta[sn].distance = (resultSanta[sn].x - ru.x)*(resultSanta[sn].x - ru.x) + (resultSanta[sn].y - ru.y)*(resultSanta[sn].y - ru.y);
			map[x][y] = resultSanta[sn];
		}
		else {
			resultSanta[sn].isOut = true;
			map[resultSanta[sn].x][resultSanta[sn].y] = null;
			return;
		}
	}
	
	public static void santasaBack(int x, int y, int move) { // 산타 충돌시 산타 밀려나기
		int sn = map[x][y].santaNum;
		
		x = x + dx[move];
		y = y + dy[move];
		
		if(x > 0 && y > 0 && x <= n && y <= n) {
			if(map[x][y]!=null) {  // 누군가 있으면
				santasaBack(x, y, move);
			}

			map[resultSanta[sn].x][resultSanta[sn].y] = null;
			resultSanta[sn].x = x;
			resultSanta[sn].y = y;
			resultSanta[sn].distance = (resultSanta[sn].x - ru.x)*(resultSanta[sn].x - ru.x) + (resultSanta[sn].y - ru.y)*(resultSanta[sn].y - ru.y);
			map[x][y] = resultSanta[sn];
		}
		else {
			resultSanta[sn].isOut = true;
			map[resultSanta[sn].x][resultSanta[sn].y] = null;
			return;
		}
	}
	
	public static void moveRu(int turn) { 
		int sn = 0;
		int minDis = Integer.MAX_VALUE;
		int x = 0;
		int y = 0;
		
		for(int i = 1; i <= p; i++) { // 바라보고 이동할 산타 선택
			Node s = resultSanta[i];
			
			if(s.isOut) { // 아웃된 산타 아웃
				continue;
			}
			
			if(s.distance < minDis) { // 가까운 거리
				sn = s.santaNum;
				minDis = s.distance;
				x = s.x;
				y = s.y;
			}
			else if(s.distance == minDis) { // 같으면 x축 큰 산타
				if(x < s.x) {
					sn = s.santaNum;
					minDis = s.distance;
					x = s.x;
					y = s.y;
				}
				else if(x == s.x) { // 또 같으면 y축 큰 산타
					if(y < s.y) {
						sn = s.santaNum;
						minDis = s.distance;
						x = s.x;
						y = s.y;
					}
				}
			}
		}
		
		int move = -1;  // 상 , 우측 상단,우,우측 하단, 하	,좌측 하단, 좌, 좌측 상단.
						// 0    1    2    3      4     5    6   7
		if(y == ru.y) {
			if(x > ru.x) { // 하단
				move = 4;
			}
			else { // 상단
				move = 0;
			}
		}
		else if(x == ru.x) {
			if(y > ru.y) { // 우측
				move = 2;
			}
			else { // 좌측
				move = 6;
			}
		}
		else if(x > ru.x && y > ru.y) { // 우측 하단으로 이동
			move = 3;
		}
		else if(x > ru.x && y < ru.y) { // 좌측 하단으로 이동
			move = 5;
		}
		else if(x < ru.x && y > ru.y) { // 우측 상단으로 이동
			move = 1;
		}
		else if(x < ru.x && y < ru.y){ // 좌측 상단으로 이동
			move = 7;
		}
		
		ru.x += rx[move];
		ru.y += ry[move];
		
		if(map[ru.x][ru.y]!=null) {
			meetSan(turn, map[ru.x][ru.y].santaNum, move);
		}
		return;
	}
}