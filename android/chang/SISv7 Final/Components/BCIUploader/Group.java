import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;


public class Group{

	Profile[] users;

	// A map that maps the id to the profile index;
	Map<Integer, Integer> map = new HashMap<>();

	public final static Random random = new Random();

	public Group(){
		users = new Profile[7];
		users[0] = new Profile(1, 0.7);
		users[1] = new Profile(2, 0.7);
		users[2] = new Profile(376896, 1);
		users[3] = new Profile(376898, 0.7);
		users[4] = new Profile(376905, 0.7);
		users[5] = new Profile(376906, 0.7);
		users[6] = new Profile(376907, 0.7);

		map.put(376896, 2);
		map.put(376898, 3);
		map.put(376905, 4);
		map.put(376906, 5);
		map.put(376907, 6);
		map.put(1, 0);
		map.put(2, 1);
	}


	public boolean vote(int id){
		fetchUserData();
		int idx = map.get(id);
		double watermark =  users[idx].weight;
		int roll = random.nextInt(100);
		System.out.println("roll: "+roll+";"+" watermark: "+watermark);
		return roll<watermark;
	}

	public void increase(int id){
		int idx = map.get(id);
		Profile user = users[idx];
		if(user.weight>90) return;
		user.weight += 1;
		updateWeight(id, user.weight);
	}

	public void decrease(int id){
		int idx = map.get(id);
		Profile  user = users[idx];
		if(user.weight<10) return;
		user.weight -= 1;
		updateWeight(id, user.weight);
	}

	public void fetchUserData(){
		String QUERY = "SELECT * FROM `users` WHERE isSocialNetworkMember=1;";
		Data data = new Data("users", QUERY);
		int len = data.size();
		users = new Profile[len];
		for(int i = 0; i<len; i++){
			String id = data.getRecord(i, "uid");
			String weight = data.getRecord(i, "weight");
			users[i] = new Profile(Integer.valueOf(id), Double.valueOf(weight));
			map.put(Integer.valueOf(id), i);
		}
		for(int i=0; i<len; i++){
			System.out.println(users[i].weight);
		}
	}

	public void updateWeight(int id, double weight){
		String QUERY = "UPDATE users SET weight="+ weight + "WHERE uid="+id;
		String url = "http://ksiresearch.org/chronobot/PHP_Post.php";
        PostQuery.PostToPHP(url, QUERY);
	}

	private static void execute(String query) throws Exception {
        String url = "http://ksiresearch.org/chronobot/PHP_Post.php";
        PostQuery.PostToPHP(url, query);
    }



	public static void main(String[] args){
		Group group = new Group();
		group.fetchUserData();
		group.updateWeight(2, 70);
	}
}

class Profile{
	double weight;
	int id;

	public Profile(int id, double weight){
		this.weight = weight;
		this.id = id;
	}
}