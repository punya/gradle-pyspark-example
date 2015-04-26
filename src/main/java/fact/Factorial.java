package fact;

import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;

public class Factorial {
    public static class SingleValue {
        private long value;
        public SingleValue(long value) {
            this.value = value;
        }
        public long getValue() {
            return value;
        }
        public void setValue(long value) {
            this.value = value;
        }
    }
    public static long fact(int n) {
        long result = 1;
        for (int i = 1; i <= n; ++i) {
            result *= i;
        }
        return result;
    }
    public static DataFrame fact(DataFrame nums) {
        return nums.sqlContext().createDataFrame(nums.toJavaRDD().map(row -> {
            return new SingleValue(fact(row.getInt(0)));
        }), SingleValue.class);
    }
}
