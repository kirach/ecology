package controllers;

import play.*;
import play.data.validation.Error;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.mvc.*;

import java.text.NumberFormat;
import java.text.DecimalFormat;

import models.*;

public class Application extends Controller {

	public static void index() {
		render();
	}

	public static void calculate(@Required Double q, @Required Double q_big, @Required Double r_big,
	                             @Required Double h_big, @Required Double n_sh, @Required Double phi,
	                             @Required Double chi) {
		//проверяем параметры
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			index();
		}

		//вектор показателей(концентраций веществ)
		double y;
		y = 2.5D * (Math.sqrt(n_sh)) - 0.13D - 0.75 * Math.sqrt(r_big) * (Math.sqrt(n_sh) - 0.1D);

		//коэффициент Шези
		boolean ifHLessThan5 = h_big <= 5;
		double c_big = Math.pow(r_big, y) / n_sh;

		//условие для применения метода Фролова-Родзиллера
		double qByQBig = q / q_big;
		Logger.info("qByQBig: " + qByQBig);
		boolean condForFrRodz = (qByQBig >= 0.0025) && (qByQBig <= 0.1);

		//расчет коэффицимета турбулентной диффузии
		double d_big;

		if (!condForFrRodz){
			//если НЕ выполняется - то по формуле Краушева
			d_big = (9.81 * r_big * h_big * 2.6) / ((0.7 * c_big + 6) * c_big);
		} else {
			//если выполняется - то для летнего времени
			d_big = (9.81 * h_big * r_big) / (37 * n_sh * Math.pow(c_big,2));
		}

		NumberFormat nf = NumberFormat.getNumberInstance();
		DecimalFormat df = (DecimalFormat)nf;
		df.applyPattern("##0.########");
		double alpha;
		double oneByThree = (double)1/(double)3;
		Logger.info("oneByThree: " + oneByThree);
		Logger.info("d_big: " + d_big);
		Logger.info("q squared: " + df.format(Math.pow(q, 2)));
		double tmp = Math.pow((d_big/Math.pow(q, 2)), oneByThree);
		Logger.info("tmp:" + tmp);
		alpha = phi * chi * tmp;

		render(y, ifHLessThan5, c_big, qByQBig, condForFrRodz, q, q_big, r_big, h_big, n_sh, phi, chi, d_big, 
		       alpha);
	}
}
