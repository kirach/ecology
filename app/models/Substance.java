package models;

import java.util.*;

import play.db.ebean.*;
import play.data.validation.Constraints.*;

import javax.persistence.*;

/**
 * Representation of substance in data base.
 */
@Entity
public class Substance extends Model {

	@Id
	public Long id;

	@Required
	public String name;

	public static Finder<Long, Substance> find = new Finder(
		Long.class, Substance.class
	);

	public static List<Substance> all() {
		return find.all();
	}

	public static void create(Substance substance) {
		substance.save();
	}

	public static void delete(Long id) {
		find.ref(id).delete();
	}
}
