package gen_algoritm.implementation;

import gen_algoritm.AlelInterface;
import gen_algoritm.CalcInterface;
import gen_algoritm.CalcResultInterface;

/**
 *
 * @author admin
 */
public class FuncItem implements CalcInterface<Double, Double, AlelInterface<Double>> {

    // y = a + b*x + c*x*x + d*x*sin(x) + e * sin(x) + f*sin(g*x) 
    @Override
    public CalcResultInterface calc(Double x, AlelInterface<Double>... param) {
        CalcResultInterface result = new FuncItemResult();
        double res = 0;
        int i = 0;
        for (AlelInterface<Double> a : param) {
            if ((i == 3)) {
                res = res + a.getValue() * Math.sin(x) * x;
            } else if (i == 4) {
                res = res + a.getValue() * Math.sin(x);
            }
            if (i == 5) {
                res = res + a.getValue() * Math.cos(x);
            } else if (i == 6) {
                res = res + a.getValue() / (x);
            } else if (i < 3) {
                res = res + a.getValue() * Math.pow(x, i);
            }

            i++;
        }
        result.setX(x);
        result.setY(res);
        return result;
    }

    @Override
    public CalcResultInterface<Double, Double> calc(Double x) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
