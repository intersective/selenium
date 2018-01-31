package model.jobsmart;


import java.util.ArrayList;
import java.util.Objects;


public class MileStone {

	private String id;
	private String name;
	private ArrayList<ActivitySequence> actvitySequences;
	
	public MileStone(String id, String name, ArrayList<ActivitySequence> actvitySequences) {
		super();
		this.id = id;
		this.name = name;
		this.actvitySequences = actvitySequences;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public ArrayList<ActivitySequence> getActvitySequences() {
		return actvitySequences;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, actvitySequences);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MileStone other = (MileStone) obj;
		if (actvitySequences == null) {
			if (other.actvitySequences != null)
				return false;
		} else if (!actvitySequences.equals(other.actvitySequences))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
