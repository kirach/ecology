package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.validation.Constraints.*;

import java.util.*;
import java.text.DecimalFormat;

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
		@Required public Double l;
	}

	/**
	 * Helper object for fetching form's data.
	 */
	static Form<Calculations> calculateForm = form(Calculations.class);
  
	/**
	 * Main page.
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

			//формат для вывода десятичных чисел
			DecimalFormat decimalFormat = new DecimalFormat("##0.####");

			//вектор показателей(концентраций веществ)
			double y;
			y = 2.5D * (Math.sqrt(filledForm.get().n_sh)) - 0.13D - 0.75 * Math.sqrt(filledForm.get().r_big) * (Math.sqrt(filledForm.get().n_sh) - 0.1D);

			//коэффициент Шези
			boolean ifHLessThan5 = filledForm.get().h_big <= 5;
			double c_big = Math.pow(filledForm.get().r_big, y) / filledForm.get().n_sh;

			//условие для применения метода Фролова-Родзиллера
			double qByQBig = filledForm.get().q / filledForm.get().q_big;
			boolean condForFrRodz = (qByQBig >= 0.0025) && (qByQBig <= 0.1);

			//расчет коэффициента турбулентной диффузии
			double d_big;

			if (!condForFrRodz){
				//если НЕ выполняется - то по формуле Краушева
				d_big = (9.81 * filledForm.get().r_big * filledForm.get().h_big * 2.6) / ((0.7 * c_big + 6) * c_big);
			} else {
				//если выполняется - то для летнего времени
				d_big = (9.81 * filledForm.get().h_big * filledForm.get().r_big) / (37 * filledForm.get().n_sh * Math.pow(c_big,2));
			}

			//альфа - коээфициент, учитывающий гидравлические условия в водном объекте
			double alpha;
			alpha = filledForm.get().phi * filledForm.get().chi * Math.pow((d_big/Math.pow(filledForm.get().q, 2)), (double)1/(double)3);

			//коэффициент смешения
			double hamma;
			hamma = (1 - Math.pow(Math.E,(-alpha) * Math.pow(filledForm.get().l, (double)1/(double)3))) /
			        (1 +(filledForm.get().q_big/filledForm.get().q)*(Math.pow(Math.E,(-alpha) * Math.pow(filledForm.get().l, (double)1/(double)3))));

			//кратность основного разбавления
			double n;
			n = (filledForm.get().q + hamma * filledForm.get().q_big) / filledForm.get().q;

			return ok(
				report.render(filledForm, decimalFormat, y, ifHLessThan5, c_big, qByQBig, condForFrRodz, d_big, alpha, hamma, n)
			);

		}
	}
}
