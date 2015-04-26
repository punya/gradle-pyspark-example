import pyspark

def test_something():
    sc = pyspark.SparkContext()
    assert sc.parallelize([1, 2, 3]).sum() == 7
