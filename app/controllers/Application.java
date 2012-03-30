package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.validation.Constraints.*;

import java.util.*;

import views.html.*;

/**
 * Main application' class.
 */
public class Application extends Controller {

	/**
	 * Describes the calculation form.
	 */
	public static class Calculations {
		@Required public Double q;
		@Required public Double q_big;
		@Required public Double r_big;
		@Required public Double h_big;
		@Required public Double n_sh;
		@Required public Double phi;
		@Required public Double chi;
	}

	static Form<Calculations> calculateForm = form(Calculations.class);
  
	/**
	 * Main, page.
	 */
	public static Result index() {
		return ok(index.render(calculateForm));
	}

	/**
	 * Calculation.
	 */
	public static Result calculate() {
		Form<Calculations> filledForm = calculateForm.bindFromRequest();
		if(filledForm.hasErrors()){
			return badRequest(
				index.render(filledForm)
			);
		} else {
			return TODO;
		}
	}
  
}
