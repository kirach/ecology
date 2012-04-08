package controllers;

import play.*;
import play.mvc.*;
import play.data.*;

import views.html.*;

import models.*;

import java.util.*;

public class SubstanceController extends Controller {

	static Form<Substance> substanceForm = form(Substance.class);

	/**
	 * List all substances from db.
	 */
	public static Result list() {
		List<Substance> allSubstances = Substance.all();
		return ok(
			listSubstances.render(allSubstances)
		);
	}

	/**
	 * Display the 'new substance' form.
	 */
	public static Result create() {
		return ok(
			createSubstance.render(substanceForm)
		);
	}

	/**
	 * Handle the 'new substance' form submission.
	 */
	public static Result save() {
		Form<Substance> filledForm = substanceForm.bindFromRequest();
		if(filledForm.hasErrors()) {
			return badRequest(createSubstance.render(filledForm));
		}
		filledForm.get().save();
		return redirect(
			routes.SubstanceController.list()
		);
	}
}
