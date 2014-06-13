package credit;

public class Credit {
	public static void main(String[] args) {
		int amount = 40 * 10000;
		int years = 30;
		double rate = 0.045;
		System.out.println("等额本息");
		debx(amount, years, rate);
		System.out.println("等额本金");
		debj(amount, years, rate);
	}

	/**
	 * 等额本息
	 * 
	 * @param amount
	 *            贷款总额
	 * @param years
	 *            贷款年限
	 * @param rete
	 *            贷款利率
	 */
	public static void debx(double amount, int years, double rate) {
		int months = years * 12;// 还款月数
		double mrate = rate / 12;// 每月利率
		double powers = Math.pow(1 + mrate, months);
		double myhk = Math.ceil((amount * mrate * powers) / (powers - 1) * 100) / 100;// 每月还款

		System.out.println("贷款总额:" + amount + " 贷款年限:" + years + " 贷款利率:"
				+ rate);
		System.out.println("每月还款:" + myhk);
		System.out.println("还款总额:" + myhk * months);
	}

	/**
	 * 等额本金
	 * 
	 * @param amount
	 *            贷款总额
	 * @param years
	 *            贷款年限
	 * @param rete
	 *            贷款利率
	 */
	public static void debj(double amount, int years, double rate) {
		double yhcount = 0;// 已还本金
		int months = years * 12;// 还款月数
		double mrate = rate / 12;// 每月利率
		double mamount = Math.ceil(amount / months * 100 * 1.0) / 100;// 每月还本金

		double[] yhks = new double[months];// 每月还款
		double[] lxs = new double[months];// 每月利息
		double[] nhks = new double[years];// 每年还款
		double hkze = 0;
		int nhkindex = 0;// 年还款索引
		for (int i = 0; i < months; i++) {
			double lx = Math.ceil((amount - yhcount) * mrate * 100) / 100;// 利息
			double yhk = mamount + lx;
			yhcount += mamount;
			lxs[i] = lx;
			yhks[i] = yhk;
			hkze += yhk;
			nhks[nhkindex] += yhk;
			if ((i + 1) % 12 == 0) {
				nhkindex++;
			}
		}
		System.out.println("贷款总额:" + amount + " 贷款年限:" + years + " 贷款利率:"
				+ rate);
		// 计算结束。输出每月还款和还款总额
		System.out.println("每月还本金:" + mamount);
//		for (int i = 0; i < 1; i++) {
			 for (int i = 0; i < months; i++) {
			double yhk = yhks[i];
			System.out.println("第" + (i + 1) + "月还款:" + yhk + " 利息:" + lxs[i]);
		}
		// for (int i = 0; i < years; i++) {
		// System.out.println("第" + (i + 1) + "年还款:" + nhks[i]);
		// }
		System.out.println("总还款:" + hkze);
	}
}
