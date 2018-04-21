package chatComponents;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable{
	
	private static final long serialVersionUID = 6414629759883543691L;
	private ArrayList<String> members;
	
	public Group(String...members) {
		this.members = new ArrayList<String>();
		for (String member : members) {
			this.members.add(member);
		}
	}
	
	public ArrayList<String> getMembers() {
		return members;
	}
	
	public void addMember(String member) {
		members.add(member);
	}
	
	public boolean equals(Group other) {
		if (other.getMembers().size() != this.getMembers().size()) {
			return false;
		} else {
			for (String member : this.getMembers()) {
				if (!other.getMembers().contains(member)) {
					return false;
				}
			}
			return true;
		}
	}
	
	public String toString() {
		String output = "";
		for(String member : getMembers()) {
			output += member +", ";
		}
		if (output.length() > 0) {
			output = output.substring(0, output.length()-2);
		}
		return output;
	}
}
