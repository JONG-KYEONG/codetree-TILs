import java.util.*;
import java.io.*;

public class Main { 	// 맵의 상단으로만 출입 가능
								// 동서남북 골렘 모양. 아래 이동. 안되면 서쪽으로 이동 후 아래 이동. 서쪽으로 이동 시 반시께 방향으로 회전
								// 또 안되면 동쪽으로 이동 후 아래 이동. 시계 방향으로 회전
								// 가장 남쪽에 도착하면, 골렘 내에서 이동 가능. 해당 골렘의 출구가 다른 골렘과 인접해있다면 다른 골렘으로 이동 가능.
								// 갈 수 있는 모든 칸 중 가장 남쪽으로 이동하면 이동 종료함. 최종 위치가 됨
	
								// 정령의 최종 위치를 누적해야함. 골렘이 맵 밖에 있다면 무효.
	static int r, c, k;
	static int result;
	static int max;
	static int map[][]; // 0:빈칸, 음수: 출구, 나머지 골렘 넘버.
	static boolean []visited;
	static int dx[] = {-1, 0, 1, 0};
	static int dy[] = {0, 1, 0, -1};
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		result = 0;
		
		st = new StringTokenizer(br.readLine());
		r = Integer.parseInt(st.nextToken());
		c = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		map = new int [r+1][c+1];
		visited = new boolean[k+1];
		for(int i = 1; i <= k; i++) {
			st = new StringTokenizer(br.readLine());
			int y = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			int re[] = moveGol(i, y, d);
			if(re==null) {
//				System.out.println(i + "턴에 초기화");
				continue;
			}
			max = 0;
			visited = new boolean[k+1];
			visited[k] = true;
//			System.out.println(i + "턴에" + re[0] + " " +re[1] + " 에서 내림");
			moveTin(re[0], re[1]);
//			System.out.println("----------" + i + "턴에 " + max + "점 추가");
			result = result + max;
		}
		System.out.println(result);
	}

	
	private static void moveTin(int x, int y) {
//		System.out.println(map[x][y] + " 번 골렘으로 갈아탐 ***********************");
		max = Math.max(x+1, max);
		for(int i = 0; i < 4; i++) { // 출구 찾기
			int nx = x + dx[i];
			int ny = y + dy[i];
			if(map[nx][ny] < 0) {  // 출구 찾았다면
				for(int j = 0; j < 4; j++) {  // 출구에서 갈 수 있는 골렘 찾기
					if(i-j == 2 || j - i == 2) {
						continue;
					}
					int nnx = nx + dx[j];
					int nny = ny + dy[j];
					if(nnx>0 && nny >0 && nnx <=r && nny <= c) {
						if(map[nnx][nny] != 0) {
							int next = map[nnx][nny];
							if(next < 0) {
								next = next * (-1);
							}
							if(!visited[next]) {
								for(int q = 0; q < 4; q++) {
									int nnnx = nnx + dx[q];
									int nnny = nny + dy[q];
									if(map[nnnx][nnny] == next) {
										visited[next] = true;
										moveTin(nnnx, nnny);
										visited[next] = false;
									}
								}
							}
						}
					}
				}
			}
		}
		
	}

	public static int [] moveGol(int num, int y, int move) {
		int x = -1;
		
		while(true) {
			int down = x + 2;
			int center = x + 1;
			int left = y - 1;
			int right = y + 1;
			if(down <= r && left > 0 && right <=c) {
				if(map[down][y] == 0 && map[center][y] == 0 && map[x+1][left] == 0 && map[x+1][right] == 0) { // 아래로 이동 가능?
					x++;
					continue;
				}
				if(left - 1 > 0) {// 왼쪽 갔다가 아래로 이동 가능?
					if(map[x][y-2] == 0 && map[x+1][y-1] == 0 && map[down][y-1] == 0 && map[x+1][left-1] == 0) { // 왼쪽 갔다가 아래로 이동 가능?
						y--;
						x++;
						move = move - 1;
						if(move < 0)
							move = 3;
						continue;
					}
				}
				if(right + 1 <= c) {
					if(map[x][y+2] == 0 && map[x+1][y+1] == 0 && map[down][y+1] == 0 && map[x+1][right+1] == 0) { // 오른쪽 갔다가 아래로 이동 가능?
						move = (move + 1)%4;
						y++;
						x++;
						continue;
					}
				}
			}
			if(x <= 1) {
				map = new int[r+1][c+1];
				return null;
			}
			map[x][y] = num;
			map[x-1][y] = num;
			map[x+1][y] = num;
			map[x][y-1] = num;
			map[x][y+1] = num;
			
			int nx = x + dx[move];
			int ny = y + dy[move];
			map[nx][ny] = (-1)*num;
			int re[] = {x,y};
			return re;
		}
	}
}