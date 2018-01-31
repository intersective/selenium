package model;

import java.util.ArrayList;
import java.util.Objects;

public class Workflow {

	private String name;
	private String id;
	private ArrayList<String> sequences;
	
	public Workflow(String name, String id, ArrayList<String> sequences) {
		super();
		this.name = name;
		this.id = id;
		this.sequences = sequences;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public ArrayList<String> getSequences() {
		return sequences;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, sequences);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Workflow other = (Workflow) obj;
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
		if (sequences == null) {
			if (other.sequences != null)
				return false;
		} else if (!sequences.equals(other.sequences))
			return false;
		return true;
	}
	
}
