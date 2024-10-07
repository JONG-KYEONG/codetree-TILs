import java.util.*;
import java.io.*;

public class Main {	// 처음 출발지 0,
					// 랜드마크 건설
					// 여행 상품 (id, 매출, 도착지)
					// 아이디를 가지고 여행 상품 삭제할 수 있음
					// 조건에 맞는 최적의 상품을 선택하여 판매 -> 매출 - 비용 최대인 상품 교려. 같을 시 id 작은 값
					// 비용 출발지에서 도착지까지의 최단 거리
					// 도달 불가, 비용이 매출보다 크면 -> 판매 불가
					// 우선순위 높은 거 판매, 후 출력, 삭제 없으면 -1 출력 삭제X
					// 여행 상품의 출발지 변경 전부 변경함.
	
	static int q, n, m;
	static boolean isChange = false;
	static int start = 0;
//	static ArrayList<Integer>[][] map;
//	static int[][] map;
	static ArrayList<Node> [] nodeList;  
	static ArrayList<Integer> idList;
	static int product[][] = new int[30000][3]; // id 매출 도착지
	static int distance[] = new int [2000];

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st;
		idList = new ArrayList<>();
		
		q = Integer.parseInt(br.readLine());
		
		while(q-->0) {
			st = new StringTokenizer(br.readLine());
			int num = Integer.parseInt(st.nextToken());
			if(num == 100) {
				n = Integer.parseInt(st.nextToken());
				m = Integer.parseInt(st.nextToken());
//				map = new ArrayList[n][n];
//				for(int i = 0 ; i < n; i++) {
//					for(int j = 0; j < n; j++) {
//						map[i][j] = new ArrayList<>();
//					}
//				}
//				map = new int [n][n];
				nodeList = new ArrayList[n];

				for(int i = 0; i < n; i++) {
					nodeList[i] = new ArrayList<>();
				}
				for(int i = 0 ; i < m; i++) {
					int x = Integer.parseInt(st.nextToken());
					int y = Integer.parseInt(st.nextToken());
					int dis = Integer.parseInt(st.nextToken());
					nodeList[x].add(new Node(y, dis));
					nodeList[y].add(new Node(x, dis));
//					if(map[x][y] == 0 || map[x][y] > dis) {
//						map[x][y] = dis;
//						map[y][x] = dis;
//					}
				}
                dijstra();
			}
			else if(num == 200) {
				int id = Integer.parseInt(st.nextToken());
				int revenue = Integer.parseInt(st.nextToken());
				int dest = Integer.parseInt(st.nextToken());
				product[id][0] = id;
				product[id][1] = revenue;
				product[id][2] = dest;
				idList.add(id);
                dijstra();
			}
			else if(num == 300) {
				int id = Integer.parseInt(st.nextToken());
				if(idList.contains(id)) {
					product[id][0] = 0;
					product[id][1] = 0;
					product[id][2] = 0;
					idList.remove(idList.indexOf(id));
				}
				dijstra();
			}
			else if(num == 400) {
				dijstra();
				int max = Integer.MIN_VALUE;
				int id = -1;
				for(Integer i : idList) {
					int cost = product[i][1] - distance[product[i][2]];
//					sb.append("최단 거리 "+i+": " + cost + "  ");
					if(max <= cost) {
						if(max == cost) {
							if (i < id) {
								max = product[i][1] - distance[product[i][2]];
								id = i;
							}
						}
						else {
							max = product[i][1] - distance[product[i][2]];
							id = i;
						}

					}
				}
				if(id != -1) {
					if(max < 0) {
						id = -1;
					}
					else {
						distance[product[id][2]] = Integer.MAX_VALUE;
						product[id][0] = 0;
						product[id][1] = 0;
						product[id][2] = 0;
						idList.remove(idList.indexOf(id));						
					}
				}
//				sb.append("\n");
				sb.append(id + "\n");
			}
			else {
				start = Integer.parseInt(st.nextToken());
				dijstra();
			}
		}
		System.out.print(sb);
		
	}
	
	static class Node implements Comparable<Node>{
		int idx;
		int dis;
		Node(int idx, int dis){
			this.idx = idx;
			this.dis = dis;
		}
		@Override
		public int compareTo(Node o) {
			return this.dis - o.dis;
		}
	}
	
	public static void dijstra() {
		Arrays.fill(distance, Integer.MAX_VALUE);
		PriorityQueue<Node> pq = new PriorityQueue<>();
		
		pq.add(new Node(start, 0));
		
		distance[start] = 0;
		
		while(!pq.isEmpty()) {
			Node node = pq.poll();
		    for(Node next : nodeList[node.idx]){
		        if(distance[next.idx] > distance[node.idx] + next.idx){
		        	distance[next.idx] = distance[node.idx] + next.dis;
		            pq.add(new Node(next.idx, distance[next.idx]));
		        }
		    }
		}
	}

}