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
		users[0] = new Profile(1, 0.9);
		users[1] = new Profile(2, 0.5);
		users[2] = new Profile(376896, 0.5);
		users[3] = new Profile(376898, 0.5);
		users[4] = new Profile(376905, 0.5);
		users[5] = new Profile(376906, 0.5);
		users[6] = new Profile(376907, 0.5);

		map.put(376896, 2);
		map.put(376898, 3);
		map.put(376905, 4);
		map.put(376906, 5);
		map.put(376907, 6);
		map.put(1, 0);
		map.put(2, 1);
	}


	public boolean vote(int id){
		int idx = map.get(id);
		double watermark =  10*users[idx].weight;
		int roll = random.nextInt(10);
		System.out.println("roll: "+roll+";"+" watermark: "+watermark);
		return roll<watermark;
	}

	public void increase(int id){
		int idx = map.get(id);
		Profile user = users[idx];
		if(user.weight>0.9) return;
		user.weight += 0.1;
	}

	public void decrease(int id){
		int idx = map.get(id);
		Profile  user = users[idx];
		if(user.weight<0.1) return;
		user.weight -= 0.1;
	}




	public static void main(String[] args){
		Group group = new Group();
		group.vote(1);
		group.vote(1);
		group.vote(1);
		group.vote(1);
		group.vote(1);
		group.vote(1);
		group.vote(1);
		group.vote(1);
		group.vote(1);
		System.out.println(group.users[1].weight);
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